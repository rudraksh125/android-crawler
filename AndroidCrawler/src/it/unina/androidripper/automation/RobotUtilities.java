package it.unina.androidripper.automation;

import static android.content.Context.WINDOW_SERVICE;
import static android.view.Surface.ROTATION_0;
import static android.view.Surface.ROTATION_180;
import static it.unina.androidripper.Resources.TAG;
import it.unina.androidripper.model.EventFiredListener;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import com.jayway.android.robotium.solo.Solo;

public class RobotUtilities {
	
	static private Solo solo;
	static private Instrumentation instrum;
	static private List<EventFiredListener> listeners = new ArrayList<EventFiredListener>();
	
	public static Solo createRobotium (ActivityInstrumentationTestCase2<?> test) {
		instrum = test.getInstrumentation();
		solo = new Solo (instrum, test.getActivity());
		return solo;
	}
	
	public static void addListener(EventFiredListener cil) {
		listeners.add(cil);
	}

	public static Instrumentation getInstrumentation() {
		return instrum;
	}
	
	// Click interactions
	
	public static void click (View v) {
		assertNotNull(v,"Cannot click: the widget does not exist");
		for (EventFiredListener l: listeners) {
			l.onClickEventFired(v);
		}
		solo.clickOnView(v);
	}
	
	@SuppressWarnings("deprecation")
	public static void drag (View v){
		solo.setSlidingDrawer((SlidingDrawer) v, Solo.OPENED);
	}
	
	public static void longClick (View v) {
		assertNotNull(v, "Cannot longClick: the widget does not exist");
		for (EventFiredListener l: listeners) {
			l.onLongClickEventFired(v);
		}
		solo.clickLongOnView(v);
	}
	
	public static void clickOnText (String text) {
		solo.clickOnText (text);
	}
	
	// List interactions
	
	public static void selectListItem (ListView l, String item) {
		selectListItem (l, item, false);
	}

	public static void selectListItem (ListView l, String item, boolean longClick) {
		selectListItem (l, Integer.valueOf(item), longClick);
	}
	
	public static void selectListItem (ListView l, int num, boolean longClick) {
		
		if (l==null) {
			List<ListView> lists = solo.getCurrentViews(ListView.class);
			if (lists.size()>0) {
				l = lists.get(0);
			}
		}
		
		assertNotNull(l, "Cannon select list item: the list does not exist");
		requestFocus(l);
		Log.i(TAG, "Swapping to listview item " + num);
		solo.sendKey(Solo.DOWN);

		final ListView theList = l;
		final int n = Math.min(l.getCount(), Math.max(1,num))-1;
		runOnUiThread(new Runnable() {
			public void run() {
				theList.setSelection(n);
			}
		});
		sync();
		
		if (n<l.getCount()/2) {
			solo.sendKey(Solo.DOWN);
			solo.sendKey(Solo.UP);
		} else {
			solo.sendKey(Solo.UP);			
			solo.sendKey(Solo.DOWN);
		}
		sync();
		
		View v = l.getSelectedView();
		if (longClick) {
			longClick(v);
		} else {
			click (v);
		}
	}
	
	// Spinner interactions
	
	public static void selectSpinnerItem (Spinner l, String item) {
		selectSpinnerItem (l, Integer.valueOf(item));
	}

	public static void selectSpinnerItem (final Spinner s, int num) {
		assertNotNull(s, "Cannon press spinner item: the spinner does not exist");
		Log.i(TAG, "Clicking the spinner view");
		click(s);
		sync();
		selectListItem(solo.getCurrentViews(ListView.class).get(0), num, false);
	}

	// Text interactions
	
	public static void typeText (EditText v, String value) {
		solo.enterText(v, value);
	}
	
	public static void writeText (EditText v, String value) {
		typeText (v, "");
		typeText (v, value);
	}
	
	public static void enterText (EditText v, String value) {
		writeText (v, value);
		solo.sendKey(Solo.ENTER);
	}

	public static void focus (View v, String value) {
		click (v);
		writeText((EditText)v, value);
	}
	
	// Radio Interactions
	
	public static void selectRadioItem (RadioGroup r, String value) {
		selectRadioItem (r, Integer.valueOf(value));
	}

	public static void selectRadioItem (final RadioGroup r, int num) {
		if (num<1) assertNotNull(null, "Cannot press radio group item: the index must be a positive number");
		assertNotNull(r, "Cannon press radio group item: the radio group does not exist");
		Log.i(TAG, "Selecting from the Radio Group view");
		click(r.getChildAt(num-1));
		sync();
	}
	
	// Key interactions
	
	public static void pressKey (String keyCode) {
		pressKey (Integer.parseInt(keyCode));
	}

	public static void pressKey (int keyCode) {
		solo.sendKey(keyCode);
		for (EventFiredListener l: listeners) {
			l.onKeyEventFired(keyCode);
		}
	}
	
	// Special interactions
	
	public static void goBack() {
		solo.goBack();
	}

	public static void openMenu() {
		solo.sendKey(Solo.MENU);
	}
	
	public static void scrollDown() {
		solo.scrollDown();
	}
	
	public static void changeOrientation() {
		Display display = ((WindowManager) getInstrumentation().getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		int angle = display.getRotation();
		int newAngle = ((angle==ROTATION_0)||(angle==ROTATION_180))?Solo.LANDSCAPE:Solo.PORTRAIT;
		solo.setActivityOrientation(newAngle);
	}
	
	// ActionBar interactions
	
	public static void ActionBarHome () {
		solo.clickOnActionBarHomeButton();
	}
	
	// Progress Bar interactions
	
	public static void setProgressBar (View v, String value) {
		setProgressBar((ProgressBar)v, Integer.parseInt(value));
	}

	public static void setProgressBar (ProgressBar v, int value) {
		solo.setProgressBar(v, value);
	}
	
	// Tab interactions

	public static void swapTab (View v, String tab) {
		swapTab ((TabHost)v, Integer.valueOf(tab));
	}

	public static void swapTab (TabHost t, String tab) {
		swapTab (t, Integer.valueOf(tab));
	}

	public static void swapTab (final TabHost t, int num) {
		assertNotNull(t, "Cannon swap tab: the tab host does not exist");
		int count = t.getTabWidget().getTabCount();
		ActivityInstrumentationTestCase2.assertTrue("Cannot swap tab: tab index out of bound", num<=count);
		final int n = Math.min(count, Math.max(1,num))-1;
		Log.i(TAG, "Swapping to tab " + num);
		click (t.getTabWidget().getChildAt(n));
	}

	// Scroll and search methods
	
	// Scroll the view to the top. Only works for ListView and ScrollView. Support for GridView and others must be added
	public static void home () {
		
		// Scroll listviews up
		final ArrayList<ListView> viewList = solo.getCurrentViews(ListView.class);
		if (viewList.size() > 0) {
			runOnUiThread(new Runnable() {
				public void run() {
					viewList.get(0).setSelection(0);
				}
			});
		}
		
		// Scroll scrollviews up
		final ArrayList<ScrollView> viewScroll = solo.getCurrentViews(ScrollView.class);
		if (viewScroll.size() > 0) {
			runOnUiThread(new Runnable() {
				public void run() {
					 viewScroll.get(0).fullScroll(ScrollView.FOCUS_UP);
				}
			});
		}
		
	}

	// Scroll until the view is on the screen if IN_AND_OUT_OF_FOCUS is enabled or if the force parameter is true 
	protected static void requestView (final View v, boolean force) {
		if (force) {
			home();
			solo.sendKey(Solo.UP); // Solo.waitForView() requires a widget to be focused		
			solo.waitForView(v, 1000, true);
		}
		requestFocus(v);
	}		

	protected static void requestFocus (final View v) {
		runOnUiThread(new Runnable() {
			public void run() {
				v.requestFocus();		
			}
		});
		sync();
	}
	
	// Utility methods
	
	public Activity getActivity() {
		return ExtractorUtilities.getActivity();
	}

	protected static void runOnUiThread (Runnable action) {
		ExtractorUtilities.getActivity().runOnUiThread(action);		
	}
	
	public static void sync() {
		getInstrumentation().waitForIdleSync();
	}
	
	public static void wait (int milli) {
		Log.i(TAG, "Waiting for " + ((milli>=1000)?(milli/1000 + " sec."):(milli + " msec.")));
		solo.sleep(milli);
	}
	
	protected static void assertNotNull (final View v) {
		ActivityInstrumentationTestCase2.assertNotNull(v);
	}

	protected static void assertNotNull (final View v, String errorMessage) {
		ActivityInstrumentationTestCase2.assertNotNull(errorMessage, v);
	}

	public static Solo createRobotiumWithInstrumentationTestCase(InstrumentationTestCase test, Activity activity) {
		instrum = test.getInstrumentation();
		solo = new Solo (instrum, activity);
		return solo;
	}

}
