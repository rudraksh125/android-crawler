package it.unina.androidripper.automation;

import static com.nofatclips.androidtesting.model.InteractionType.*;
import static com.nofatclips.androidtesting.model.SimpleType.BUTTON;
import static com.nofatclips.androidtesting.model.SimpleType.MENU_ITEM;
import static it.unina.androidripper.automation.Resources.*;
import static it.unina.androidripper.automation.RobotUtilities.*;
import it.unina.android.hardware.mock.MockSensorEvent;
import it.unina.android.hardware.mock.MockSensorEventFactory;
import it.unina.android.hardware.mock.MockSensorManager;
import it.unina.androidripper.automation.utils.ActivityReflectionCache;
import it.unina.androidripper.automation.utils.ActivityReflectionCacheElement;
import it.unina.androidripper.automation.utils.AndroidConsoleSocket;
import it.unina.androidripper.helpers.PackageManagerHelper;
import it.unina.androidripper.helpers.ReflectionHelper;
import it.unina.androidripper.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;

// Automation implements the methods to interact with the application via the Instrumentation (Robot)
// and to extract informations from it (Extractor); the Robotium framework is used where possible

public class Automation implements Robot, Extractor, TaskProcessor, ImageCaptor, EventFiredListener {
	
//	private Instrumentation inst;
	
	private Map<Integer,View> theViews = new HashMap<Integer,View> (); // A list of widgets with an id
	private ArrayList<View> allViews = new ArrayList<View>(); // A list of all widgets
	private Solo solo; // Robotium
	private Extractor extractor;
	private Restarter restarter;
	private TabHost	tabs; // Reference to the TabHost widget if present
	private Robot theRobot;
	private UserEvent currentEvent;
	private ImageCaptor imageCaptor;
	private boolean precrawlNeeded = true;
	
	public final static String SEPARATOR = ".-.-.";
	
	/** @author nicola amatucci - sensori/reflection */
	//settati da GuiTreeEngine.setUp()
	public LocationManager locationManager;
	public PackageManagerHelper packageManagerHelper;
	/** @author nicola amatucci - sensori/reflection */	
	
	// A Trivial Extractor is provided if none is assigned
	public Automation () {
		TrivialExtractor te = new TrivialExtractor(); 
		setExtractor (te);
		this.imageCaptor = te;
		setRobot (this);
		RobotUtilities.addListener(this);
	}

	public Automation (Extractor e) {
		setExtractor (e);
	}
	
	// Initializations
	@SuppressWarnings("rawtypes")
	public void bind (ActivityInstrumentationTestCase2 test) {
//		this.test = test;
//		this.theActivity = this.test.getActivity();
		this.solo = RobotUtilities.createRobotium (test);
		afterRestart();
		refreshCurrentActivity();
		Log.w ("androidripper","--->" + ExtractorUtilities.getActivity().getLocalClassName());
	}

	// Initializations
	public void bindInstrumentationTestCase(InstrumentationTestCase test, Activity activity) {
//		this.test = test;
//		this.theActivity = this.test.getActivity();
		this.solo = RobotUtilities.createRobotiumWithInstrumentationTestCase (test, activity);
		afterRestart();
		refreshCurrentActivity();
		Log.w ("androidripper","--->" + ExtractorUtilities.getActivity().getLocalClassName());
	}
	public void execute (Trace t) {
		this.theRobot.process (t);
	}
	
	public void process (Trace t) {
		Log.i ("androidripper", "Restarting");
		if (FORCE_RESTART) {
			this.restarter.restart();
			this.precrawlNeeded = true;
		}
		afterRestart();
		extractState();
		Log.i ("androidripper", "Playing Trace " + t.getId());
		for (Transition step: t) {
			process (step);
		}
	}
	
	public void process (Transition step) {
		for (UserInput i: step) {
			setInput(i);
		}
		fireEvent (step.getEvent());		
	}

	public void finalize() {
		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
	}

	public void fireEvent(UserEvent e) {
		this.currentEvent = e;
		String eventType = e.getType();
		String eventValue = e.getValue();
		if (eventType.equals(BACK) || eventType.equals(SCROLL_DOWN)) { // Special events
			Log.d("androidripper", "Firing event: type= " + eventType);
			fireEventOnView(null, eventType, null);
		} else if (eventType.equals(CLICK_ON_TEXT)) {
			Log.d("androidripper", "Firing event: type= " + eventType + " value= " + eventValue);
			fireEventOnView(null, eventType, eventValue);
		} else {
			View v = null;
			if (e.getWidget().getIndex()<getAllWidgets().size()) {
				v = getAllWidgets().get(e.getWidget().getIndex()); // Search widget by index
			}
			if ((v!=null) && checkWidgetEquivalence(v, Integer.parseInt(e.getWidgetId()), e.getWidgetType(), e.getWidgetName())) { // Widget found
				Log.i("androidripper", "Firing event: type= " + eventType + " index=" + e.getWidget().getIndex() + " widget="+ e.getWidgetType());
				fireEventOnView (v, eventType, eventValue);
			} else if (e.getWidgetId().equals("-1")) { // Widget not found. Search widget by name
				Log.i("androidripper", "Firing event: type= " + eventType + " name=" + e.getWidgetName() + " widget="+ e.getWidgetType());
				fireEvent (e.getWidgetName(), e.getWidget().getSimpleType(), eventType, eventValue);
			} else { // Widget not found. Search widget by id
				Log.i("androidripper", "Firing event: type= " + eventType + " id=" + e.getWidgetId() + " widget="+ e.getWidgetType());
				fireEvent (Integer.parseInt(e.getWidgetId()), e.getWidgetName(), e.getWidget().getSimpleType(), eventType, eventValue);
			}
		}
		this.currentEvent = null;
	}

	public void setInput(UserInput i) {
		Log.d("androidripper", "Setting input: type= " + i.getType() + " id=" + i.getWidgetId() + " value="+ i.getValue());
		setInput (Integer.parseInt(i.getWidgetId()), i.getType(), i.getValue(), i.getWidgetName(), i.getWidgetType());
	}
	
	public void swapTab (String tab) {
		RobotUtilities.swapTab (this.tabs, tab);
	}

	public void swapTab (int tab) {
		RobotUtilities.swapTab (this.tabs, tab);
	}
	
	private void fireEvent (int widgetId, String widgetName, String widgetType, String eventType, String value) {
		View v = getWidget(widgetId, widgetType, widgetName);
		if (v == null) {
			v = getWidget(widgetId);
		}
		if (v == null) {
			v = ExtractorUtilities.findViewById(widgetId);
		}
		fireEventOnView(v, eventType, value);
	}

	private void fireEvent (String widgetName, String widgetType, String eventType, String value) {
		View v = null;
		if (widgetType.equals(BUTTON)) {
			v = solo.getButton(widgetName);
		} else if (widgetType.equals(MENU_ITEM)) {
			v = solo.getText(widgetName);
		}
		if (v == null) {
			for (View w: getAllWidgets()) {
				if (w instanceof Button) {
					Button candidate = (Button) w;
					if (candidate.getText().equals(widgetName)) {
						v = candidate;
					}
				}
				if (v!=null) break;
			}
		}
		fireEventOnView(v, eventType, value);
	}
	
	private void fireEventOnView (View v, String eventType, String value) {
		injectInteraction(v, eventType, value);
		wait(SLEEP_AFTER_EVENT);
		waitOnThrobber();
		refreshCurrentActivity();
		extractState();
	}
	
	private void injectInteraction (View v, String interactionType, String value) {
		if (v!=null) {
			requestView(v);
		}
		
		if (interactionType.equals(CLICK)) {
			click (v);
		
		/** @author Nicola */
		} else if (interactionType.equals(FOCUS)) {
				click (v);
		/** @author Nicola */
		
		} else if (interactionType.equals(DRAG)) {
			click (v);
	
		} else if (interactionType.equals(LONG_CLICK)) {
			longClick(v);
		} else if (interactionType.equals(BACK)) {
			goBack();
		} else if (interactionType.equals(OPEN_MENU)) {
			openMenu();
		} else if (interactionType.equals(SCROLL_DOWN)) {
			scrollDown();
		} else if (interactionType.equals(CHANGE_ORIENTATION)) {
			changeOrientation();
		} else if (interactionType.equals(CLICK_ON_TEXT)) {
			clickOnText(value);
		} else if (interactionType.equals(PRESS_KEY)) {
			pressKey(value);
		} else if (interactionType.equals(SWAP_TAB) && (value!=null)) {
			if (v instanceof TabHost) {
				RobotUtilities.swapTab (v, value);
			} else {
				swapTab (value);
			}
		} else if (interactionType.equals(LIST_SELECT)) {
			selectListItem((ListView)v, value);
		} else if (interactionType.equals(LIST_LONG_SELECT)) {
			selectListItem((ListView)v, value, true);
		} else if (interactionType.equals(SPINNER_SELECT)) {
			selectSpinnerItem((Spinner)v, value);
		} else if (interactionType.equals(RADIO_SELECT)) {
			selectRadioItem((RadioGroup)v, value);
		} else if (interactionType.equals(TYPE_TEXT)) {
			typeText((EditText)v, value);
		
		/** @author nicola */
		} else if (interactionType.equals(WRITE_TEXT)) {
			writeText((EditText)v, value);
		/** @author nicola */
			
		} else if (interactionType.equals(SEARCH_TEXT)) {
			searchText((EditText)v, value);	
			
		} else if (interactionType.equals(SET_BAR)) {
			setProgressBar(v, value);
			
		/** @author nicola amatucci - sensori/reflection */
		} else  if (
					interactionType.equals(ORIENTATION_SENSOR_EVENT) ||
					interactionType.equals(ACCELEROMETER_SENSOR_EVENT) ||
					interactionType.equals(MAGNETIC_FIELD_SENSOR_EVENT) ||
					interactionType.equals(TEMPERATURE_SENSOR_EVENT) ||
					interactionType.equals(AMBIENT_TEMPERATURE_SENSOR_EVENT)
					) {
			fireSensorEvent(value, interactionType);						
		} else if ( interactionType.equals(GPS_LOCATION_CHANGE_EVENT) ) {
			fireGPSLocationChangeEvent(value);
		} else if ( interactionType.equals(GPS_PROVIDER_DISABLE_EVENT) ) {
			toggleGPSLocationProvider();
		} else if ( interactionType.equals(INCOMING_CALL_EVENT) ) {
			AndroidConsoleSocket.callNumber("1234");
			try { Thread.sleep(2000); } catch(Exception ex) { }
			AndroidConsoleSocket.hangUp("1234");
		} else if ( interactionType.equals(INCOMING_SMS_EVENT) ) {
			AndroidConsoleSocket.sendSMS("1234", "THIS IS A TEST");
			/** @author nicola amatucci - sensori/reflection */
		} else {
			return;
		}
	}
		
	/** @author nicola amatucci - sensori/reflection */	
	private void fireSensorEvent(String value, String interactionType)
	{
		if (value != null && interactionType != null)
		{
			String[] stringValues = value.split("\\|");
			
			if (stringValues != null && stringValues.length == 3)
			{
				float[] floatValues = new float[3];
				floatValues[0] = Float.parseFloat(stringValues[0]);
				floatValues[1] = Float.parseFloat(stringValues[1]);
				floatValues[2] = Float.parseFloat(stringValues[2]);
				
				MockSensorEvent event = null;
				
				if (interactionType.equals(ORIENTATION_SENSOR_EVENT)) event = MockSensorEventFactory.buildOrientationEvent(floatValues);
				if (interactionType.equals(ACCELEROMETER_SENSOR_EVENT)) event = MockSensorEventFactory.buildAccelerometerEvent(floatValues);
				if (interactionType.equals(MAGNETIC_FIELD_SENSOR_EVENT)) event = MockSensorEventFactory.buildMagneticFieldEvent(floatValues);
				if (interactionType.equals(TEMPERATURE_SENSOR_EVENT)) event = MockSensorEventFactory.buildTemperatureEvent(floatValues);
				if (interactionType.equals(AMBIENT_TEMPERATURE_SENSOR_EVENT)) event = MockSensorEventFactory.buildAmbientTemperatureEvent(floatValues);

				/*
				 * NOTA: 	non e' presente nella precedente versione. Serve ad evitare
				 * 			problemi di chiamate cross-thread
				 */
				final MockSensorEvent eventToPost = event;
				solo.getCurrentActivity().runOnUiThread( new Runnable() {

					@Override
					public void run() {
						MockSensorManager.getInstance().riseSensorEvent(eventToPost);						
					}

				});
			}
		}
	}
	
	private void fireGPSLocationChangeEvent(String value)
	{
		//abilita il provider se disabilitato
		if ( locationManager.isProviderEnabled( it.unina.androidripper.planning.Resources.TEST_LOCATION_PROVIDER ) == false )
			locationManager.setTestProviderEnabled(it.unina.androidripper.planning.Resources.TEST_LOCATION_PROVIDER, true);
		
		if (value != null)
		{
			String[] stringValues = value.split("\\|");
			
			if (stringValues != null && stringValues.length == 3)
			{
				double[] doubleValues = new double[3];
				doubleValues[0] = Double.parseDouble(stringValues[0]); //latitude
				doubleValues[1] = Double.parseDouble(stringValues[1]); //longitude
				doubleValues[2] = Double.parseDouble(stringValues[2]); //altitude
				
		        Location location = new Location(it.unina.androidripper.planning.Resources.TEST_LOCATION_PROVIDER);
		        location.setLatitude(doubleValues[0]);
		        location.setLongitude(doubleValues[1]);
		        location.setAltitude(doubleValues[2]);
		        locationManager.setTestProviderLocation(it.unina.androidripper.planning.Resources.TEST_LOCATION_PROVIDER, location);
			}
		}
	}
	
	private void toggleGPSLocationProvider()
	{
		//disabilita il provider
		if ( locationManager.isProviderEnabled( it.unina.androidripper.planning.Resources.TEST_LOCATION_PROVIDER ) == true )
		{
			//disabilita il gps
			locationManager.setTestProviderEnabled(it.unina.androidripper.planning.Resources.TEST_LOCATION_PROVIDER, false);
			
			//pausa di un secondo
			try { Thread.sleep(1000); } catch (InterruptedException e) { }
			
			//abilita il gps
			locationManager.setTestProviderEnabled(it.unina.androidripper.planning.Resources.TEST_LOCATION_PROVIDER, true);
		}
	}	
	/** @author nicola amatucci - sensori/reflection */
	
	private void refreshCurrentActivity() {
		ExtractorUtilities.setActivity(solo.getCurrentActivity());
		Log.i("androidripper", "Current activity is " + getActivity().getLocalClassName());
	}

	private void setInput (int widgetId, String inputType, String value, String widgetName, String widgetType) {
		View v = getWidget(widgetId, widgetType, widgetName);
		if (v == null) {
			v = getWidget(widgetId);
		}
		if (v == null) {
			v = ExtractorUtilities.findViewById(widgetId);
		}
		if (v == null) {
			for (View w: getAllWidgets()) {
				if (w instanceof Button || w instanceof RadioGroup) {
					if (!AbstractorUtilities.getType(w).equals(widgetType)) continue;
					v = (AbstractorUtilities.detectName(w).equals(widgetName))?w:null;
//					Button candidate = (Button) w;
//					if (candidate.getText().equals(widgetName)) {
//						v = candidate;
//					}
				}
				if (v!=null) break;
			}
		}

		injectInteraction(v, inputType, value);
	}

	public static boolean isInAndOutFocusEnabled() {
		return IN_AND_OUT_FOCUS;
	}		

	protected void requestView (View v) {
		RobotUtilities.requestView(v, isInAndOutFocusEnabled());
	}
		
	public void wait (int milli) {
		RobotUtilities.wait(milli);
	}

	public void clearWidgetList() {
		theViews.clear();
		allViews.clear();		
	}
	
	public void retrieveWidgets () {
		RobotUtilities.home();
		clearWidgetList();
		Log.i("androidripper", "Retrieving widgets");
		ArrayList<View> viewList = (isInAndOutFocusEnabled())?solo.getViews():solo.getCurrentViews();
		for (View w: viewList) {
			String text = (w instanceof TextView)?": "+((TextView)w).getText().toString():"";
			Log.d("androidripper", "Found widget: id=" + w.getId() + " ("+ w.toString() + ")" + text); // + " in window at [" + xy[0] + "," + xy[1] + "] on screen at [" + xy2[0] + "," + xy2[1] +"]");			
			allViews.add(w);
			if (w.getId()>0) {
				theViews.put(w.getId(), w); // Add only if the widget has a valid ID
			}
			if (w instanceof TabHost) {
				setTabs((TabHost)w);
				Log.d("androidripper", "Found tabhost: id=" + w.getId());
			}
		}
	}
	
	public void setRobot (Robot r) {
		this.theRobot = r;
	}

	public Map<Integer,View> getWidgets () {
		return this.theViews;
	}

	public ArrayList<View> getAllWidgets () {
		return this.allViews;
	}

	public Activity getActivity() {
		return ExtractorUtilities.getActivity();
	}

	public void setExtractor (Extractor e) {
		this.extractor = e;
	}

	public void setRestarter (Restarter r) {
		this.restarter = r;
	}
	
	public void setTabs (TabHost t) {
		this.tabs = t;
//		this.tabNum = t.getTabWidget().getTabCount();
	}

	public void afterRestart() {
		solo.setActivityOrientation(Solo.PORTRAIT);
		wait(SLEEP_AFTER_RESTART);
		waitOnThrobber();
		if ((PRECRAWLING.length>0) && this.precrawlNeeded) {
			this.precrawlNeeded = false;
			refreshCurrentActivity();
			extractState();
			processPrecrawling();
		}
		Log.d("androidripper", "Ready to operate after restarting...");
	}
	
	private void processPrecrawling() {
		Log.i("androidripper", "Processing precrawling");
		String[] params = new String[3];
		int paramCount=0;
		for (String s: PRECRAWLING) {
			if ((s == null) || s.equals(SEPARATOR)) {
				switch (paramCount) {
					case 0: continue;
					case 1: {
						Log.i ("androidripper", "Firing event " + params[0]);
						fireEventOnView(null, params[0], null);
						break;
					}
					case 2: {
						Log.i ("androidripper", "Firing event " + params[0] + " with value: " + params[1]);
						fireEventOnView(null, params[0], params[1]);
						break;
					}	
					case 3: {
						View v = getWidget(Integer.parseInt(params[1]));
						Log.i ("androidripper", "Firing event " + params[0] + " on widget #" + params[1] + " with value: " + params[2]);
						fireEventOnView(v, params[0], params[2]);
						break;
					}
				};
				paramCount = 0;
			} else {
				params[paramCount] = s;
				paramCount++;
			}
		}
	}
	
	public void waitOnThrobber() {
		int sleepTime = SLEEP_ON_THROBBER;
		if (sleepTime==0) return;
		
		boolean flag;
		do {
			flag = false;
			int oldId = 0;
			ArrayList<ProgressBar> bars = solo.getCurrentViews(ProgressBar.class);
				for (ProgressBar b: bars) {
					if (b.isShown() &&  b.isIndeterminate()) {
						int newId = b.getId();
						if (newId != oldId) { // Only log if the throbber changed since the last time
							Log.d("androidripper", "Waiting on Progress Bar #" + newId);
							oldId = newId;
						}
						flag = true;
						wait(500);
						sleepTime-=500;
					}
				}
		} while (flag && (sleepTime>0));
		sync();
	}
	
	public String getAppName () {
		return solo.getCurrentActivity().getApplicationInfo().toString();
	}

	public View getWidget (int id) {
		return this.extractor.getWidget(id);
	}
	
	public View getWidget (int theId, String theType, String theName) {
		for (View testee: getWidgetsById(theId)) {
			if (checkWidgetEquivalence(testee, theId, theType, theName)) {
				return testee;
			}
		}
		return null;
	}
	
	public boolean checkWidgetEquivalence (View testee, int theId, String theType, String theName) {
		Log.i("androidripper", "Retrieved from return list id=" + testee.getId());
		
		String testeeType = AbstractorUtilities.getType(testee); //testee.getClass().getName();
		Log.d("androidripper", "Testing for type (" + testeeType + ") against the original (" + theType + ")");
		if ( !(theType.equals(testeeType)) ) return false;
		
		String testeeName = AbstractorUtilities.detectName(testee);
		Log.d("androidripper", "Testing for name (" + testeeName + ") against the original (" + theName + ")");
		return ( (theName.equals(testeeName)) && (theId == testee.getId()) );
	}
	
	public ArrayList<View> getWidgetsById (int id) {
		ArrayList<View> theList = new ArrayList<View>();
		for (View theView: getAllWidgets()) {
			if (theView.getId() == id) {
				Log.d("androidripper", "Added to return list id=" + id);
				theList.add(theView);
			}
		}
		return theList;
	}

	public ActivityDescription describeActivity() {
		return this.extractor.describeActivity();
	}

	public void extractState() {
		this.extractor.extractState();
	}
	
	public Bitmap captureImage() {
		return this.imageCaptor.captureImage();
	}
	
	// This methods call the Abstractor Utility methods to describe the current event
	
	public void onClickEventFired (View v) {
		AbstractorUtilities.describeCurrentEvent (this.currentEvent, v);
	}

	public void onLongClickEventFired (View v) {
		onClickEventFired (v);
	}
	
	public void onKeyEventFired (int ignore) {
		AbstractorUtilities.describeKeyEvent (this.currentEvent);
	}

	// The TrivialExtractor uses the same methods available in Automation to create
	// a description of the Activity, which is basically the name and a list of widgets
	// in the Activity.
	
	/** @author nicola amatucci - sensori/reflection */	
	ActivityReflectionCache activityCache = new ActivityReflectionCache();
	/** @author nicola amatucci - sensori/reflection */	
	
	
	public class TrivialExtractor implements Extractor, ImageCaptor {

		public void extractState() {
			retrieveWidgets();
		}

		public View getWidget (int key) {
			return getWidgets().get(key);
		}

		public Activity getActivity() {
			return ExtractorUtilities.getActivity();
		}

		public ActivityDescription describeActivity() {
			return new ActivityDescription() {
				
				public Iterator<View> iterator() {
					return getAllWidgets().iterator();
				}
				
				public int getWidgetIndex(View v) {
					return getAllWidgets().indexOf(v);
				}

				public String getActivityName() {
//					return getActivity().getLocalClassName();
					return getActivity().getClass().getSimpleName();
				}

				public String getActivityTitle() {
					return getActivity().getTitle().toString();
				}

				public String toString() {
					return getActivityName();
				}

				/** @author nicola amatucci - sensori/reflection */
				public boolean usesSensorsManager()
				{
					if (it.unina.androidripper.planning.Resources.USE_SENSORS)
					{
						ActivityReflectionCacheElement a = activityCache.get( getActivity().getClass().getCanonicalName() );
						
						if (a == null || a.usesSensors == null)
						{
							a = new ActivityReflectionCacheElement();
							a.usesSensors = it.unina.androidripper.helpers.ReflectionHelper.scanClassForInterface(getActivity().getClass(), "it.unina.android.hardware.SensorEventListener");
							activityCache.put( getActivity().getClass().getCanonicalName(), a );
						}
						return a.usesSensors;
					}
					else
					{
						return false;
					}
				}
				
				public boolean usesLocationManager()
				{
					if (it.unina.androidripper.planning.Resources.USE_GPS)
					{
						ActivityReflectionCacheElement a = activityCache.get( getActivity().getClass().getCanonicalName() );
						
						if (a == null || a.usesLocation == null)
						{						
							a = new ActivityReflectionCacheElement();
							a.usesLocation = it.unina.androidripper.helpers.ReflectionHelper.scanClassForInterface(getActivity().getClass(), "android.location.LocationListener");
							activityCache.put( getActivity().getClass().getCanonicalName(), a );
						}						
						return a.usesLocation;
					}
					else
					{
						return false;
					}
				}
				
				public boolean hasMenu()
				{
					ActivityReflectionCacheElement a = activityCache.get( getActivity().getClass().getCanonicalName() );
					
					if (a == null || a.hasMenu == null)
					{
						a = new ActivityReflectionCacheElement();
						a.hasMenu =
								(
								ReflectionHelper.hasDeclaredMethod(getActivity().getClass(), "onCreateOptionsMenu")
								|| ReflectionHelper.hasDeclaredMethod(getActivity().getClass(), "onPrepareOptionsMenu")
								);
								//&& ReflectionHelper.hasDeclaredMethod(getActivity().getClass(), "onOptionsItemSelected"); //tipicamente onCreateOptionsMenu basta
						activityCache.put( getActivity().getClass().getCanonicalName(), a );
					}
					
					return a.hasMenu;
				}
				
				public boolean hasOnOptionsItemSelected()
				{
					ActivityReflectionCacheElement a = activityCache.get( getActivity().getClass().getCanonicalName() );
					
					if (a == null || a.hasOnOptionsItemSelected == null)
					{
						a = new ActivityReflectionCacheElement();
						a.hasOnOptionsItemSelected =
								ReflectionHelper.hasDeclaredMethod(getActivity().getClass(), "onOptionsItemSelected");
						activityCache.put( getActivity().getClass().getCanonicalName(), a );
					}
					
					return a.hasOnOptionsItemSelected;
				}				

				public boolean handlesKeyPress()
				{
					ActivityReflectionCacheElement a = activityCache.get( getActivity().getClass().getCanonicalName() );
					
					if (a == null || a.handlesKeyPress == null)
					{
						a = new ActivityReflectionCacheElement();
						a.handlesKeyPress =
								ReflectionHelper.hasDeclaredMethod(getActivity().getClass(), "onKeyDown");
								//&& ReflectionHelper.hasDeclaredMethod(getActivity().getClass(), "onOptionsItemSelected"); //tipicamente onCreateOptionsMenu basta
						activityCache.put( getActivity().getClass().getCanonicalName(), a );
					}
					
					return a.handlesKeyPress;
				}
				
				public boolean handlesLongKeyPress()
				{
					ActivityReflectionCacheElement a = activityCache.get( getActivity().getClass().getCanonicalName() );
					
					if (a == null || a.handlesLongKeyPress == null)
					{
						a = new ActivityReflectionCacheElement();
						a.handlesLongKeyPress =
								ReflectionHelper.hasDeclaredMethod(getActivity().getClass(), "onKeyLongPress");
								//&& ReflectionHelper.hasDeclaredMethod(getActivity().getClass(), "onOptionsItemSelected"); //tipicamente onCreateOptionsMenu basta
						activityCache.put( getActivity().getClass().getCanonicalName(), a );
					}
					
					return a.handlesLongKeyPress;
				}
				
				public boolean isTabActivity() 
				{
					ActivityReflectionCacheElement a = activityCache.get( getActivity().getClass().getCanonicalName() );
					
					if (a == null || a.isTabActivity == null)
					{
						a = new ActivityReflectionCacheElement();
						a.isTabActivity = ReflectionHelper.isDescendant(getActivity().getClass(), android.app.TabActivity.class);
						activityCache.put( getActivity().getClass().getCanonicalName(), a );
					}
					
					return a.isTabActivity;
				}
				/** @author nicola amatucci - sensori/reflection */	
			};
		}
		
		public Bitmap captureImage()
		{
			ArrayList<View> views = solo.getViews();
			if (views != null && views.size() > 0)
			{
				final View view = views.get(0);
				final boolean flag = view.isDrawingCacheEnabled();
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						if (!flag) {
							view.setDrawingCacheEnabled(true);
						}
			            view.buildDrawingCache();
					}
				});
				sync();
				Bitmap b = view.getDrawingCache();
	            b = b.copy(b.getConfig(), false);
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						if (!flag) {
							view.setDrawingCacheEnabled(false);
						}
					}
				});
				return b;
			}
			return null;
		}

	}
	
}
