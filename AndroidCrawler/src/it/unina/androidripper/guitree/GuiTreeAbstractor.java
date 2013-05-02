package it.unina.androidripper.guitree;

import it.unina.androidripper.helpers.ReflectionHelper;
import it.unina.androidripper.model.*;
import it.unina.androidripper.storage.PersistenceFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.nofatclips.androidtesting.guitree.*;
import com.nofatclips.androidtesting.model.*;
import com.nofatclips.dictionary.ContentTypeDetector;

import static it.unina.androidripper.Resources.*;
import static it.unina.androidripper.automation.AbstractorUtilities.*;

public class GuiTreeAbstractor implements Abstractor, FilterHandler, SaveStateListener {

	private GuiTree theSession;
	private StartActivity baseActivity;
	private HashSet<Filter> filters;
	private int eventId=0;
	private int inputId=0;
	private int activityId=0;
	private int widgetId=0;
	private TypeDetector detector;
	private List<AbstractorListener> theListeners = new ArrayList<AbstractorListener>();
	public final static String ACTOR_NAME = "GuiTreeAbstractor";
	private static final String EVENT_PARAM_NAME = "eventId";
	private static final String INPUT_PARAM_NAME = "inputId";
	private static final String ACTIVITY_PARAM_NAME = "activityId";
	private static final String WIDGET_PARAM_NAME = "widgetId";

	public GuiTreeAbstractor () throws ParserConfigurationException {
		this (new GuiTree());
	}
	
	public GuiTreeAbstractor(GuiTree s) {
		super();
		this.filters = new HashSet<Filter>();
		setTheSession(s);
		PersistenceFactory.registerForSavingState(this);
	}

	public GuiTree getTheSession() {
		return this.theSession;
	}

	public void setTheSession(GuiTree theSession) {
		this.theSession = theSession;
	}
	
	public TypeDetector getTypeDetector () {
		return this.detector;
	}
	
	public void setTypeDetector (TypeDetector t) {
		this.detector = t;
	}

	public ActivityState createActivity (ActivityDescription desc) {
		return createActivity (desc,false);
	}
	
	// If the boolean parameter is omitted, the overloading method will default to a Final Activity
	public ActivityState createActivity (ActivityDescription desc, boolean start) {
		ActivityState newActivity = (start)?StartActivity.createActivity(getTheSession()):FinalActivity.createActivity(getTheSession());
		newActivity.setName(desc.getActivityName());
		newActivity.setTitle(desc.getActivityTitle());
		newActivity.setUniqueId(getUniqueActivityId());
		newActivity.setId(newActivity.getUniqueId());
		for (Filter f: this.filters) {
			f.clear();
		}
		boolean hasDescription = updateDescription(newActivity, desc, false);
		if (!hasDescription) newActivity.markAsExit();
		for (AbstractorListener listener: this.theListeners) {
			listener.onNewActivity(newActivity);
		}
		return newActivity;
	}

	public boolean updateDescription (ActivityState newActivity, ActivityDescription desc) {
		return updateDescription  (newActivity, desc, true);
	}

	public TestCaseWidget createWidget (View v) {
		TestCaseWidget w = TestCaseWidget.createWidget(getTheSession());
		String id = String.valueOf(v.getId());
//		String text = "";
		String name = detectName(v);
		int type = 0;
		if (v instanceof TextView) {
//			TextView t = (TextView)v;
//			text = t.getText().toString();
//			if (v instanceof EditText) {
//				CharSequence hint = ((EditText)v).getHint();
//				name = (hint==null)?"":hint.toString();
//			}
//			if (v instanceof RadioGroup) {
//				
//			}
			type = ((TextView)v).getInputType();
			if (type!=0) {
				w.setTextType("" + type);
			}
		}
		w.setIdNameType(id, name, getType(v));
		w.setUniqueId(getUniqueWidgetId());
//		if (type!=0) {
//			w.setTextType("" + type);
//		}
		w.setSimpleType(getTypeDetector().getSimpleType(v));
		
		/** @author nicola */
		if (	//Resources.DETECT_ON_FOCUS_CHANGE_LISTENER &&
				v.isFocusable()
			&&	v.isFocusableInTouchMode()
			&&	ReflectionHelper.checkIfFieldIsSet(v, "android.view.View", "mOnFocusChangeListener")
		)
		{
			w.setSimpleType(SimpleType.FOCUSABLE_EDIT_TEXT);
		}
		/** @author nicola */
		
		setCount (v,w);
		setValue (v,w);
		w.setAvailable((v.isEnabled())?"true":"false");
		w.setClickable((v.isClickable())?"true":"false");
		w.setLongClickable((v.isLongClickable())?"true":"false");
//		w.setIndex(desc.getWidgetIndex(v));
		
		
		
		/** @author nicola */
		//if duplicato perché nome e valore vengono settati dopo l'if precedente
		if (v instanceof TextView) {
			//default
			String txtId = reflectTextualIDbyNumericalID(v.getId());
			Log.v("androidripper", "TextualID : " + txtId);
			w.setContentType(ContentTypeDetector.detect(w, txtId));
			Log.v("androidripper", "ContentType detected : " + w.getContentType());			
		}
		/** @author nicola */
		
		return w;
	}

//	public static String detectName (View v) {
//		String name = "";
//		if (v instanceof TextView) {
//			TextView t = (TextView)v;
//			name = t.getText().toString();
//			if (v instanceof EditText) {
//				CharSequence hint = ((EditText)v).getHint();
//				name = (hint==null)?"":hint.toString();
//			}
//		} else if (v instanceof RadioGroup) {
//			RadioGroup g = (RadioGroup)v;
//			int max=g.getChildCount();
//			String text = "";
//			for (int i=0; i<max; i++) {
//				View c = g.getChildAt(i);
//				text = detectName (c);
//				if (!text.equals("")) {
//					name = text;
//					break;
//				}
//			}
//		}
//		return name;
//	}
	
	public boolean updateDescription (ActivityState newActivity, ActivityDescription desc, boolean detectDuplicates) {
		boolean hasDescription = false;
		
		/** @author nicola amatucci - sensori/reflection */
		if ( it.unina.androidripper.planning.Resources.REFLECT_ACTIVITY_LISTENERS && desc.hasMenu() )
		{
			addActivitySupportedEvent(newActivity, InteractionType.OPEN_MENU);

			//Verifica che al menu sia agganciato un listener
			//if ( Resources.REFLECT_ACTIVITY_LISTENERS && desc.hasOnOptionsItemSelected() )
		}
		
		if ( it.unina.androidripper.planning.Resources.REFLECT_ACTIVITY_LISTENERS && desc.handlesKeyPress() )
		{
			addActivitySupportedEvent(newActivity, InteractionType.PRESS_KEY);
			
			//NOTA:
			//tipicamente OnKeyPress e' utilizzato per supportare il tasto Back
			//per cui si puo' ipotizzare che possa essere scatenato queste evento
			addActivitySupportedEvent(newActivity, InteractionType.BACK);
		}
		
		if ( it.unina.androidripper.planning.Resources.REFLECT_ACTIVITY_LISTENERS && desc.handlesLongKeyPress() )
		{
			//TODO: da rendere costante
			addActivitySupportedEvent(newActivity, "_longKeyPress");
		}
		
		if ( desc.isTabActivity() )
		{
			//addActivitySupportedEvent(newActivity, InteractionType.SWAP_TAB);
			Log.v("GuiTreeAbstracor", "Activity is TabActivity");
		}
		/** @author nicola amatucci - sensori/reflection */	
		
		for (View v: desc) {
			hasDescription = true;
			if (!v.isShown()) continue;
			TestCaseWidget w = createWidget (v);
			w.setIndex(desc.getWidgetIndex(v));
			if (detectDuplicates && newActivity.hasWidget(w)) continue;
			newActivity.addWidget(w);
			
			/** @author nicola amatucci - sensori/reflection */
			if (it.unina.androidripper.planning.Resources.REFLECT_WIDGETS)
				reflectWidget(newActivity, v, w);
			/** @author nicola amatucci - sensori/reflection */

			for (Filter f: this.filters) {
				f.loadItem(v, w);
			}
		}
		
		/** @author nicola amatucci - sensori/reflection */
		if ( it.unina.androidripper.planning.Resources.USE_SENSORS && desc.usesSensorsManager() )
		{
			for (Integer s : it.unina.androidripper.planning.Resources.SENSOR_TYPES)
			{
				switch (s)
				{
					case android.hardware.Sensor.TYPE_ACCELEROMETER:
						addActivitySupportedEvent(newActivity, InteractionType.ACCELEROMETER_SENSOR_EVENT);
						break;
						
					case android.hardware.Sensor.TYPE_ORIENTATION:
						addActivitySupportedEvent(newActivity, InteractionType.ORIENTATION_SENSOR_EVENT);
						break;
						
					case android.hardware.Sensor.TYPE_MAGNETIC_FIELD:
						addActivitySupportedEvent(newActivity, InteractionType.MAGNETIC_FIELD_SENSOR_EVENT);
						break;
						
					case android.hardware.Sensor.TYPE_TEMPERATURE:
						addActivitySupportedEvent(newActivity, InteractionType.TEMPERATURE_SENSOR_EVENT);
						break;
				}
			}
		}
		
		if ( it.unina.androidripper.planning.Resources.USE_GPS && desc.usesLocationManager() )
		{
			addActivitySupportedEvent(newActivity, InteractionType.GPS_LOCATION_CHANGE_EVENT);
			addActivitySupportedEvent(newActivity, InteractionType.GPS_PROVIDER_DISABLE_EVENT);
		}
		
		if ( it.unina.androidripper.planning.Resources.SIMULATE_INCOMING_CALL )
		{
			addActivitySupportedEvent(newActivity, InteractionType.INCOMING_CALL_EVENT);
		}
		
		if ( it.unina.androidripper.planning.Resources.SIMULATE_INCOMING_SMS )
		{
			addActivitySupportedEvent(newActivity, InteractionType.INCOMING_SMS_EVENT);
		}
		/** @author nicola amatucci - sensori/reflection */
		
		return hasDescription;
	}
	
	/** @author nicola amatucci - sensori/reflection */
	private void reflectWidget(ActivityState a, View v, TestCaseWidget w)
	{
		HashMap<String, Boolean> listenersMap = null;
		
		//TODO: casi particolari?
		if (v instanceof android.opengl.GLSurfaceView)
			listenersMap = null;
		else if (v instanceof View)
			listenersMap = ReflectionHelper.reflectViewListeners(v);
		
		if ( listenersMap != null )
			for ( String key : listenersMap.keySet() )
				if ( listenersMap.get(key) )
					addSupportedEvent( a, w.getUniqueId(), key ); //addSupportedEvent( a, w.getUniqueId(), listenerNameToInteractionType(key) );
		
		try
		{
			// Class.isInstance <-> instanceof
			if (	Class.forName("com.android.internal.view.menu.IconMenuItemView").isInstance(v) &&
					a.supportsEvent( SupportedEvent.GENERIC_ACTIVITY_UID , InteractionType.OPEN_MENU))
			{
				addSupportedEvent( a, w.getUniqueId(), InteractionType.CLICK );
			}
		}
		catch(Exception ex)
		{
			//ignored
		}
		
		if (v instanceof android.widget.TabHost)
		{
			addSupportedEvent( a, w.getUniqueId(), InteractionType.SWAP_TAB );
		}
	}
	
	/*
	private String listenerNameToInteractionType(String listenerName)
	{
		//TODO
		return listenerName;
	}
	*/
	
	private void addActivitySupportedEvent(ActivityState a, String eventType)
	{
		addSupportedEvent(a, SupportedEvent.GENERIC_ACTIVITY_UID, eventType);
	}
	
	private void addSupportedEvent(ActivityState a, String uid, String eventType)
	{
		SupportedEvent supportedEvent = TestCaseSupportedEvent.createSupportedEvent(getTheSession());
		supportedEvent.setWidgetUniqueId(uid);
		supportedEvent.setEventType(eventType);
		a.addSupportedEvent( supportedEvent );
	}
	/** @author nicola amatucci - sensori/reflection */
	
//	@SuppressWarnings("rawtypes")
//	private void setCount (View v, WidgetState w) {
//		// For lists, the count is set to the number of rows in the list (inactive rows - e.g. separators - count as well)
//		if (v instanceof AdapterView) {
//			w.setCount(((AdapterView)v).getCount());
//			return;
//		}
//		
//		// For Spinners, the count is set to the number of options
//		if (v instanceof AbsSpinner) {
//			w.setCount(((AbsSpinner)v).getCount());
//			return;
//		}
//		
//		// For the tab layout host, the count is set to the number of tabs
//		if (v instanceof TabHost) {
//			w.setCount(((TabHost)v).getTabWidget().getTabCount());
//			return;
//		}
//		
//		// For grids, the count is set to the number of icons, for RadioGroups it's set to the number of RadioButtons
//		if (v instanceof ViewGroup) {
//			w.setCount(((ViewGroup)v).getChildCount());
//			return;
//		}
//		
//		// For progress bars, seek bars and rating bars, the count is set to the maximum value allowed
//		if (v instanceof ProgressBar) {
//			w.setCount(((ProgressBar)v).getMax());
//			return;
//		}
//		
//	}
	
//	private void setValue (View v, WidgetState w) {
//		
//		// Checkboxes, radio buttons and toggle buttons -> the value is the checked state (true or false)
//		if (v instanceof Checkable) {
//			w.setValue(String.valueOf(((Checkable) v).isChecked()));
//		}
//
//		// Textview, Editview et al. -> the value is the displayed text
//		if (v instanceof TextView) {
//			w.setValue(((TextView) v).getText().toString());
////			Log.e("androidripper", "Hint for " + (((TextView) v).getText().toString()) + " = " + (((TextView) v).getHint()));
//			return;
//		}
//		
//		// Progress bars, seek bars and rating bars -> the value is the current progress
//		if (v instanceof ProgressBar) {
//			w.setValue(String.valueOf(((ProgressBar) v).getProgress()));
//		}
//				
//	}
	
	public void setBaseActivity (ActivityDescription desc) {
		this.baseActivity = (StartActivity) createActivity(desc,true);
	}
	
	public ActivityState getBaseActivity () {
		return this.baseActivity;
	}

	public void setStartActivity (Transition theStep, ActivityState theActivity) {
		theStep.setStartActivity ((ACTIVITY_DESCRIPTION_IN_SESSION)?theActivity:stubActivity(theActivity));
	}

	public void setFinalActivity (Trace theTask, ActivityState theActivity) {
		theTask.setFinalActivity ((ACTIVITY_DESCRIPTION_IN_SESSION)?theActivity:stubActivity(theActivity));
	}
	
	private TestCaseActivity stubActivity (ActivityState theActivity) {
		TestCaseActivity theStub = ((TestCaseActivity)theActivity).clone();
		theStub.resetDescription();
		theStub.setDescriptionId(theActivity.getId());
		return theStub;
	}

	public Iterator<Filter> iterator() {
		return this.filters.iterator();
	}

	public void addFilter(Filter f) {
		this.filters.add(f);
	}

	public UserEvent createEvent (String type) {
		return createEvent (null, type);
	}

	public UserEvent createEvent (WidgetState target, String type) {
		TestCaseEvent newEvent = TestCaseEvent.createEvent(getTheSession());
		if (target == null) {
			target = TestCaseWidget.createWidget(getTheSession());
			target.setType("null");
			target.setId("-1");
			target.setSimpleType("null");
			target.setUniqueId("w0");
			newEvent.setWidget (target);
		} else {
			newEvent.setWidget (target.clone());
		}
		newEvent.setType(type);
		newEvent.setId(getUniqueEventId());
		for (AbstractorListener listener: this.theListeners) {
			listener.onNewEvent(newEvent);
		}
		return newEvent;
	}

	public UserInput createInput(WidgetState target, String value, String type) {
		TestCaseInput newInput = TestCaseInput.createInput(getTheSession());
		newInput.setWidget (target.clone());
		newInput.setValue(value);
		newInput.setType(type);
		newInput.setId(getUniqueInputId());
		for (AbstractorListener listener: this.theListeners) {
			listener.onNewInput(newInput);
		}
		return newInput;
	}

	public Trace createTrace(Trace head, Transition tail) {
		TestCaseTrace t;
		if (head!= null) {
			t = ((TestCaseTrace)head).clone();
		} else {
			t = new TestCaseTrace (getTheSession());
		}
		t.addTransition(tail);
		return t;
	}
	
	public Trace importTask (Element fromXml) {
		TestCaseTrace imported = new TestCaseTrace (getTheSession());
		Element task = (Element)getTheSession().getDom().adoptNode(fromXml);
		imported.setElement(task);
		return imported;
	}

	public ActivityState importState (Element fromXml) {
		return getTheSession().importState(fromXml);
	}

	public Transition createStep (ActivityState start, Collection<UserInput> inputs, UserEvent event) {
		Transition t = TestCaseTransition.createTransition(start.getElement().getOwnerDocument());
		try {
			setStartActivity(t, StartActivity.createActivity(start));
			for (UserInput inPut: inputs) {
				t.addInput(inPut);
			}
			t.setEvent (event);
		}
		catch (DOMException e) {
			Log.i("androidripper", "Abstractor->createStep(activity): " + ((e.code==DOMException.HIERARCHY_REQUEST_ERR)?"HIERARCHY_REQUEST_ERR":String.valueOf(e.code)));
		}
		return t;
	}
	
	public void registerListener(AbstractorListener theListener) {
		this.theListeners.add(theListener);
	}
	
	public String getUniqueEventId () {
		int ret = this.eventId;
		this.eventId++;
		return "e" + ret;
	}

	public String getUniqueActivityId () {
		this.activityId++;
		return "a" + this.activityId;
	}

	public String getUniqueInputId () {
		int ret = this.inputId;
		this.inputId++;
		return "i" + ret;
	}

	public String getUniqueWidgetId () {
		int ret = this.widgetId;
		this.widgetId++;
		return "w" + ret;
	}

	public SessionParams onSavingState() {
		SessionParams state = new SessionParams();
		state.store(EVENT_PARAM_NAME, this.eventId);
		state.store(INPUT_PARAM_NAME, this.inputId);
		state.store(ACTIVITY_PARAM_NAME, this.activityId);
		state.store(WIDGET_PARAM_NAME, this.widgetId);
		return state;
	}
	
	public void onLoadingState(SessionParams sessionParams) {
		this.eventId = sessionParams.getInt(EVENT_PARAM_NAME);
		this.inputId = sessionParams.getInt(INPUT_PARAM_NAME);
		this.activityId = sessionParams.getInt(ACTIVITY_PARAM_NAME);
		this.widgetId = sessionParams.getInt(WIDGET_PARAM_NAME);
		Log.d("androidripper", "Restored abstractor counters to: event = " + eventId + " - input = " + inputId + " - activity = " + activityId);
	}

	public String getListenerName() {
		return ACTOR_NAME;
	}

}