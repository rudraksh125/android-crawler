package com.nofatclips.crawler;

import static com.nofatclips.crawler.Resources.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
//import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import com.jayway.android.robotium.solo.Solo;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;

public class Automation implements Robot, Extractor {
	
//	private Instrumentation inst;
	@SuppressWarnings("rawtypes")
	private ActivityInstrumentationTestCase2 test;
	private Activity theActivity;
	private Map<Integer,View> theViews = new HashMap<Integer,View> ();
	private Solo solo;
	private Extractor extractor;
	private Restarter restarter;
	private TabHost	tabs;
	private int tabNum;
	
	public Automation () {
		setExtractor (new TrivialExtractor());
	}

	public Automation (Extractor e) {
		setExtractor (e);
	}
	
	@SuppressWarnings("rawtypes")
	public void bind (ActivityInstrumentationTestCase2 test) {		
		this.test = test;
		this.theActivity = this.test.getActivity();
		this.solo = new Solo (this.test.getInstrumentation(), theActivity);
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
		Log.d("nofatclips", "Firing event: type= " + e.getType() + " id=" + e.getWidgetId() + " widget="+ e.getWidgetType());
		fireEvent (Integer.parseInt(e.getWidgetId()), e.getWidgetType(), e.getType(), e.getValue());
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
	
	private void setInput (int widgetId, String widgetType, String value) {
		View v = getWidget(widgetId);
		if (v == null) {
			v = theActivity.findViewById(widgetId);
		}
		if (widgetType == "editText") {
			solo.enterText((EditText)v, value);
		} else if (widgetType == "button") {
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
	
	public void retrieveWidgets () {
		theViews.clear();
		Log.i("nofatclips", "Retrieving widgets");
		for (View w: solo.getCurrentViews()) {
			Log.d("nofatclips", "Found widget: id=" + w.getId() + " ("+ w.toString() + ")");
			theViews.put(w.getId(), w);
			if (w instanceof TabHost) {
				setTabs((TabHost)w);
				Log.d("nofatclips", "Found tabhost: id=" + w.getId());
			}
		}
	}

	public Map<Integer,View> getWidgets () {
		return this.theViews;
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
		solo.sleep(200);		
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
					return getWidgets().values().iterator();
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
