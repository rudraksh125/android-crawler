package com.nofatclips.crawler;

import static com.nofatclips.crawler.Resources.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
//import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import com.jayway.android.robotium.solo.Solo;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;

// Automation implements the methods to interact with the application via the Instrumentation (Robot)
// and to extract informations from it (Extractor); the Robotium framework is used where possible

public class Automation implements Robot, Extractor, TaskProcessor {
	
//	private Instrumentation inst;
	@SuppressWarnings("rawtypes")
	private ActivityInstrumentationTestCase2 test; // The test case used to crawl the application
	private Activity theActivity; // Current Activity
	private Map<Integer,View> theViews = new HashMap<Integer,View> (); // A list of widgets with an id
	private ArrayList<View> allViews = new ArrayList<View>(); // A list of all widgets
	private Solo solo; // Robotium
	private Extractor extractor;
	private Restarter restarter;
	private TabHost	tabs; // Reference to the TabHost widget if present
	private int tabNum; // Number of tabs used by the Activity
	
	// A Trivial Extractor is provided if none is assigned
	public Automation () {
		setExtractor (new TrivialExtractor());
	}

	public Automation (Extractor e) {
		setExtractor (e);
	}
	
	// Initializations
	@SuppressWarnings("rawtypes")
	public void bind (ActivityInstrumentationTestCase2 test) {		
		this.test = test;
//		this.theActivity = this.test.getActivity();
		this.solo = new Solo (this.test.getInstrumentation(), test.getActivity());
		afterRestart();
		this.theActivity = solo.getCurrentActivity();
		Log.w ("nofatclips","--->" + theActivity.getLocalClassName());
	}
	
	@Override
	public void execute (Trace t) {
		process (t);
	}
	
	@Override
	public void process (Trace t) {
		Log.i ("nofatclips", "Restarting");
		this.restarter.restart();
		afterRestart();
		extractState();
		Log.i ("nofatclips", "Playing Trace " + t.getId());
		for (Transition step: t) {
			for (UserInput i: step) {
				setInput(i);
			}
			fireEvent (step.getEvent());
		}
	}

	@Override
	public void finalize() {
		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		theActivity.finish();
	}

	@Override
	public void fireEvent(UserEvent e) {
		if (e.getWidgetId().equals("-1")) {
			Log.d("nofatclips", "Firing event: type= " + e.getType() + " name=" + e.getWidgetName() + " widget="+ e.getWidgetType());
			fireEvent (e.getWidgetName(), e.getWidgetType(), e.getType(), e.getValue());			
		} else {
			Log.d("nofatclips", "Firing event: type= " + e.getType() + " id=" + e.getWidgetId() + " widget="+ e.getWidgetType());
			fireEvent (Integer.parseInt(e.getWidgetId()), e.getWidgetType(), e.getType(), e.getValue());
		}
		solo.sleep(SLEEP_AFTER_EVENT);
		extractState();
	}

	@Override
	public void setInput(UserInput i) {
		Log.d("nofatclips", "Setting input: type= " + i.getType() + " id=" + i.getWidgetId() + " value="+ i.getValue());
		setInput (Integer.parseInt(i.getWidgetId()), i.getType(), i.getValue());
	}
	
	public void swapTab (String tab) {
		swapTab (this.tabs, Integer.valueOf(tab));
	}

	public void swapTab (int tab) {
		swapTab (this.tabs, tab);
	}
	
//	private void fireEvent (int widgetId, String widgetType, String eventType) {
//		fireEvent(widgetId, widgetType, eventType, null);
//	}
	
	private void fireEvent (int widgetId, String widgetType, String eventType, String value) {
		View v = getWidget(widgetId);
		if (v == null) {
			v = theActivity.findViewById(widgetId);
		}
		fireEvent(v, eventType, value);
	}

	private void fireEvent (String widgetName, String widgetType, String eventType, String value) {
		View v = null;
		if (widgetType.endsWith("Button")) {
			v = solo.getButton(widgetName);
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
		fireEvent(v, eventType, value);
	}
	
	private void fireEvent (View v, String eventType, String value) {
		if (eventType == "click") {
			TouchUtils.clickView(test, v);
		} else if (eventType == "swapTab" && value!=null) {
			if (v instanceof TabHost) {
				swapTab ((TabHost)v, value);
			} else {
				swapTab (value);
			}
		} else {
			return;
		}
		this.theActivity = solo.getCurrentActivity();		
	}

	private void setInput (int widgetId, String inputType, String value) {
		View v = getWidget(widgetId);
		if (v == null) {
			v = theActivity.findViewById(widgetId);
		}
		if (inputType == "editText") {
			solo.enterText((EditText)v, value);
		} else if (inputType == "click") {
			TouchUtils.clickView(test, v);
		}
	}

	private void swapTab (TabHost t, String tab) {
		swapTab (t, Integer.valueOf(tab));
	}

	private void swapTab (final TabHost t, int num) {
		final int n = Math.min(this.tabNum, Math.max(1,num))-1;
		Log.i("nofatclips", "Swapping to tab " + num);
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				t.setCurrentTab(n);
			}
		});
		this.test.getInstrumentation().waitForIdleSync();
	}
	
	public void clearWidgetList() {
		theViews.clear();
		allViews.clear();		
	}
	
	public void retrieveWidgets () {
		clearWidgetList();
		Log.i("nofatclips", "Retrieving widgets");
		for (View w: solo.getCurrentViews()) {
			Log.d("nofatclips", "Found widget: id=" + w.getId() + " ("+ w.toString() + ")");
			if (!theViews.containsKey(w.getId())) {
				allViews.add(w);
			}
			if (w.getId()>0) {
				theViews.put(w.getId(), w); // Add only if the widget has a valid ID
			}
			if (w instanceof TabHost) {
				setTabs((TabHost)w);
				Log.d("nofatclips", "Found tabhost: id=" + w.getId());
			}
		}
	}

	public Map<Integer,View> getWidgets () {
		return this.theViews;
	}

	public ArrayList<View> getAllWidgets () {
		return this.allViews;
	}

	public Activity getActivity() {
		return this.theActivity;
	}

	public void setExtractor (Extractor e) {
		this.extractor = e;
	}

	public void setRestarter (Restarter r) {
		this.restarter = r;
	}
	
	public void setTabs (TabHost t) {
		this.tabs = t;
		this.tabNum = t.getTabWidget().getTabCount();		
	}

	public void afterRestart() {
		solo.sleep(SLEEP_AFTER_RESTART);
		Log.d("nofatclips", "Ready to operate after restarting...");
	}
	
	public String getAppName () {
		return solo.getCurrentActivity().getApplicationInfo().toString();
	}

	@Override
	public View getWidget (int id) {
		return this.extractor.getWidget(id);
	}
	
	@Override
	public ActivityDescription describeActivity() {
		return this.extractor.describeActivity();
	}

	@Override
	public void extractState() {
		this.extractor.extractState();
	}
	
	@Override
	public int getNumTabs() {
		return extractor.getNumTabs();
	}

	// The TrivialExtractor uses the same methods available in Automation to create
	// a description of the Activity, which is basically the name and a list of widgets
	// in the Activity.
	
	public class TrivialExtractor implements Extractor {

		@Override
		public void extractState() {
			retrieveWidgets();
		}

		@Override
		public View getWidget (int key) {
			return getWidgets().get(key);
		}
		
		@Override
		public int getNumTabs () {
			return tabNum;
		}

		@Override
		public ActivityDescription describeActivity() {
			return new ActivityDescription() {
				
				@Override
				public Iterator<View> iterator() {
					return getAllWidgets().iterator();
				}

				@Override
				public String getActivityName() {
					// TODO Auto-generated method stub
//					return getActivity().getLocalClassName();
					return getActivity().getClass().getSimpleName();
				}
				
				@Override
				public String toString() {
					return getActivityName();
				}

			};
		}

	}
	
}
