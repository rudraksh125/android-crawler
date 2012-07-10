package com.nofatclips.crawler;

import com.nofatclips.androidtesting.model.InteractionType;
import com.nofatclips.crawler.model.Comparator;
import com.nofatclips.crawler.model.StrategyCriteria;
import com.nofatclips.crawler.planning.UserFactory;
import com.nofatclips.crawler.planning.adapters.InteractorAdapter;
import com.nofatclips.crawler.storage.*;
import com.nofatclips.crawler.strategy.comparator.*;
import com.nofatclips.crawler.planning.interactors.*;
import com.nofatclips.crawler.strategy.criteria.*;
import android.util.Log;

import static com.nofatclips.androidtesting.model.SimpleType.*;
import static com.nofatclips.androidtesting.model.InteractionType.*;
import static android.view.KeyEvent.*;

@SuppressWarnings("unused")
public class Resources {

//	public static String PACKAGE_NAME = "com.softmimo.android.mileagetracker";
//	public static String CLASS_NAME = "com.softmimo.android.mileagetracker.ListView";
//	public static String FILE_NAME = "mileage_tracker.xml"; // Output
//	public static Comparator COMPARATOR = new NameComparator();
//	public static int SLEEP_AFTER_EVENT = 2000;
//	public static int SLEEP_AFTER_RESTART = 2000;

//	public static String PACKAGE_NAME = "com.blazing_skies.caloriecalculator";
//	public static String CLASS_NAME = "com.blazing_skies.caloriecalculator.PreMainActivity";
//	public static Comparator COMPARATOR = new EditTextComparator();
//	public static int SLEEP_AFTER_EVENT = 300;
//	public static int SLEEP_AFTER_RESTART = 2000;
//	public static int SLEEP_AFTER_TASK = 5000;

	public static String PACKAGE_NAME = "com.evancharlton.mileage";
	public static String CLASS_NAME = "com.evancharlton.mileage.Mileage";
	public static Comparator COMPARATOR = new CustomWidgetsComparator(CustomWidgetsComparator.IGNORE_ACTIVITY_NAME, EDIT_TEXT, BUTTON, LIST_VIEW, MENU_VIEW, IMAGE_VIEW);
//	public static Comparator COMPARATOR = new CustomWidgetsDeepComparator(CustomWidgetsComparator.IGNORE_ACTIVITY_NAME, BUTTON, LIST_VIEW, MENU_VIEW, IMAGE_VIEW);
	public static int SLEEP_AFTER_EVENT = 4000;
	public static int SLEEP_AFTER_RESTART = 4000;
	
//	public static String PACKAGE_NAME = "net.sf.andbatdog.batterydog";
//	public static String CLASS_NAME = "net.sf.andbatdog.batterydog.BatteryDog";
//	public static String FILE_NAME = "batterydog.xml"; // Output
//	public static Comparator COMPARATOR = new NameComparator();
//	public static int SLEEP_AFTER_EVENT = 5000;
//	public static int SLEEP_AFTER_RESTART = 2000;

	// Wordpress 1
//	public static String PACKAGE_NAME = "org.wordpress.android";
//	public static String CLASS_NAME = "org.wordpress.android.splashScreen";
//	public static String FILE_NAME = "wordpress.xml"; // Output
//	public static Comparator COMPARATOR = new CustomWidgetsDeepComparator(CustomWidgetsComparator.IGNORE_ACTIVITY_NAME, EDIT_TEXT, BUTTON, MENU_VIEW, DIALOG_VIEW, LIST_VIEW);
//	public static int SLEEP_AFTER_EVENT = 500;
//	public static int SLEEP_AFTER_RESTART = 6000;
//	public static StrategyCriteria[] ADDITIONAL_CRITERIAS = new StrategyCriteria[] {
//		new AfterEventDontExplore("Remove Blog", "Add"),
//	};

//	// Wordpress 2 (Beta)
//	public static String PACKAGE_NAME = "org.wordpress.android";
//	public static String CLASS_NAME = "org.wordpress.android.Dashboard";
////	public static String FILE_NAME = "wordpress2.xml"; // Output
//	public static Comparator COMPARATOR = new CustomWidgetsComparator(EDIT_TEXT, BUTTON, MENU_VIEW, DIALOG_VIEW, LIST_VIEW, SINGLE_CHOICE_LIST, MULTI_CHOICE_LIST);
//	public static int SLEEP_AFTER_EVENT = 3000;
//	public static int SLEEP_AFTER_RESTART = 1000;
//	public static StrategyCriteria[] ADDITIONAL_CRITERIAS = new StrategyCriteria[] {
//		new AfterEventDontExplore("Remove Blog"),
//		new AfterWidgetDontExplore (2131165200),
//	};
//	static {
//		UserFactory.denyInteractionOnIds(TYPE_TEXT, 2131165215, 2131165217); // Don't change user and password
//		UserFactory.denyInteractionOnIds(CLICK, 2131165204, 2131165351); // Don't click Linear Layout container, don't check Geotag Posts
//	}
//	//	public static InteractorAdapter[] ADDITIONAL_INPUTS = new InteractorAdapter[] { // Username and password
//	//	new FixedValueEditor().addIdValuePair(2131165215, "").addIdValuePair(2131165217, ""),
//	//};

//	public static String PACKAGE_NAME = "com.bwx.bequick";
//	public static String CLASS_NAME = "com.bwx.bequick.ShowSettingsActivity";
//	public static String FILE_NAME = "quick_settings.xml"; // Output
//	public static Comparator COMPARATOR = new EditTextComparator();
//	public static int SLEEP_AFTER_EVENT = 200;
//	public static int SLEEP_AFTER_RESTART = 0;

//	public static String PACKAGE_NAME = "net.bible.android.activity";
//	public static String CLASS_NAME = "net.bible.android.activity.StartupActivity";
//	public static String FILE_NAME = "bible.xml"; // Output
//	public static Comparator COMPARATOR = new CustomWidgetsComparator(EDIT_TEXT, BUTTON, MENU_VIEW, DIALOG_VIEW, LIST_VIEW);
//	public static int SLEEP_AFTER_EVENT = 1500;
//	public static int SLEEP_AFTER_RESTART = 10000;
//	public static boolean EVENT_WHEN_NO_ID = true; // Whether to inject events on widgets without ID or not 
//	static {
//		UserFactory.addEvent(CLICK, BUTTON, SPINNER);
//		UserFactory.addEvent(LONG_CLICK, WEB_VIEW);
//	}
	
//	public static String PACKAGE_NAME = "com.ichi2.anki";
//	public static String CLASS_NAME = "com.ichi2.anki.StudyOptions";
//	public static String FILE_NAME = "anki_guitree.xml"; // Output
//	public static Comparator COMPARATOR = new ButtonComparator();
//	public static int SLEEP_AFTER_EVENT = 10000;
//	public static int SLEEP_AFTER_RESTART = 4000;

//	// API Demos
//	
//	public static String PACKAGE_NAME = "com.example.android.apis";
//	public static String CLASS_NAME = "com.example.android.apis.ApiDemos";
//	public static Comparator COMPARATOR = new CustomWidgetsDeepComparator(TEXT_VIEW, DIALOG_VIEW, MENU_VIEW, BUTTON);
//	public static int SLEEP_AFTER_EVENT = 2000;
//	public static int SLEEP_AFTER_RESTART = 500;
//	public static boolean EVENT_WHEN_NO_ID = true; // Whether to inject events on widgets without ID or not 
//	public static StrategyCriteria[] ADDITIONAL_CRITERIAS = new StrategyCriteria[] {
//		new AfterEventDontExplore("1. Array", "13. Slow Adapter", "14. Efficient Adapter", "4. ListAdapter", "9. Array (Overlay)"),
//	};

//	public static String PACKAGE_NAME = "com.googlecode.andoku";
//	public static String CLASS_NAME = "com.googlecode.andoku.MainActivity";
//	public static String FILE_NAME = "andoku.xml"; // Output
//	public static Comparator COMPARATOR = new CustomWidgetsDeepComparator(EDIT_TEXT, BUTTON, CHECKBOX);
//	public static int SLEEP_AFTER_EVENT = 1000;
//	public static int SLEEP_AFTER_RESTART = 2000;
//	public static int MAX_EVENTS_PER_WIDGET = 11; // For GroupViews (0 = try all items in the group)
//	public static boolean EVENT_WHEN_NO_ID = true; // Whether to inject events on widgets without ID or not
//	public static StrategyCriteria[] ADDITIONAL_CRITERIAS = new StrategyCriteria[] {
//		new AfterEventDontExplore("Reset All Puzzles"),
//	};

//	public static String PACKAGE_NAME = "de.fmaul.android.cmis";
//	public static String CLASS_NAME = "de.fmaul.android.cmis.HomeActivity";
//	public static String FILE_NAME = "cmis.xml"; // Output
//	public static Comparator COMPARATOR = new CustomWidgetsComparator(EDIT_TEXT, BUTTON);
//	public static int SLEEP_AFTER_EVENT = 2000;
//	public static int SLEEP_AFTER_RESTART = 2000;

//	public static String PACKAGE_NAME = "cz.romario.opensudoku";
//	public static String CLASS_NAME = "cz.romario.opensudoku.gui.FolderListActivity";
//	public static String FILE_NAME = "opensudoku.xml"; // Output
//	public static Comparator COMPARATOR = new CustomWidgetsComparator(BUTTON, EDIT_TEXT, LIST_VIEW);
//	public static int SLEEP_AFTER_EVENT = 2000;
//	public static int SLEEP_AFTER_RESTART = 2000;
//	public static int MAX_EVENTS_PER_WIDGET = 5; // For GroupViews (0 = try all items in the group)
//	public static StrategyCriteria[] ADDITIONAL_CRITERIAS = new StrategyCriteria[] {
//		new AfterEventDontExplore("Delete folder", "Delete puzzle"),
//	};
	
//	public static String PACKAGE_NAME = "org.chemlab.dealdroid";
//	public static String CLASS_NAME = "org.chemlab.dealdroid.Preferences";
//	public static String FILE_NAME = "dealdroid.xml"; // Output
//	public static Comparator COMPARATOR = new CustomWidgetsComparator(EDIT_TEXT, BUTTON);
//	public static int SLEEP_AFTER_EVENT = 1000;
//	public static int SLEEP_AFTER_RESTART = 1000;

//	public static String PACKAGE_NAME = "com.kiwifruitmobile.sudoku";
//	public static String CLASS_NAME = "com.kiwifruitmobile.sudoku.MainActivity";
//	public static String FILE_NAME = "supersudoku.xml"; // Output
//	public static Comparator COMPARATOR = new CustomWidgetsComparator(CustomWidgetsComparator.IGNORE_ACTIVITY_NAME, EDIT_TEXT, BUTTON);
//	public static int SLEEP_AFTER_EVENT = 200;
//	public static int SLEEP_AFTER_RESTART = 2000;

//	public static String PACKAGE_NAME = "com.saatcioglu.android.guessthenumber";
//	public static String CLASS_NAME = "com.saatcioglu.android.guessthenumber.GfxMain";
//	public static Comparator COMPARATOR = new CustomWidgetsComparator(BUTTON);
//	public static int SLEEP_AFTER_EVENT = 1000;
//	public static int SLEEP_AFTER_RESTART = 1000;

//	public static String PACKAGE_NAME = "org.connectbot";
//	public static String CLASS_NAME = "org.connectbot.HostListActivity";
//	public static String FILE_NAME = "connectbot.xml"; // Output
//	public static Comparator COMPARATOR = new CustomWidgetsDeepComparator(EDIT_TEXT, BUTTON);
//	public static int SLEEP_AFTER_EVENT = 2000;
//	public static int SLEEP_AFTER_RESTART = 2000;
//	public static StrategyCriteria[] ADDITIONAL_CRITERIAS = new StrategyCriteria[] {
////		new AfterEventDontExplore("Reset All Puzzles"),
//	};

//	public static String PACKAGE_NAME = "com.google.android.diskusage";
//	public static String CLASS_NAME = "com.google.android.diskusage.SelectActivity";
//	public static String FILE_NAME = "diskusage.xml"; // Output
//	public static Comparator COMPARATOR = new CustomWidgetsDeepComparator(DIALOG_VIEW, BUTTON);
//	public static int SLEEP_AFTER_EVENT = 4000;
//	public static int SLEEP_AFTER_RESTART = 1000;

//	public static String PACKAGE_NAME = "net.mandaria.tippytipper";
//	public static String CLASS_NAME = "net.mandaria.tippytipper.activities.TippyTipper";
//	public static String FILE_NAME = "tippytipper.xml"; // Output
//	public static Comparator COMPARATOR = new CustomWidgetsDeepComparator(DIALOG_VIEW, BUTTON);
//	public static int SLEEP_AFTER_EVENT = 2000;
//	public static int SLEEP_AFTER_RESTART = 1000;
//	public static boolean EVENT_WHEN_NO_ID = true; // Whether to inject events on widgets without ID or not

//	public static String PACKAGE_NAME = "com.FireFart.Permissions";
//	public static String CLASS_NAME = "com.FireFart.Permissions.Permissions";
//	public static String FILE_NAME = "permissions.xml"; // Output
//	public static Comparator COMPARATOR = new CustomWidgetsDeepComparator(TEXT_VIEW);
//	public static int SLEEP_AFTER_EVENT = 3400;
//	public static int SLEEP_AFTER_RESTART = 3400;
//	public static int MAX_EVENTS_PER_WIDGET = 4; // For GroupViews (0 = try all items in the group)

//	public static String PACKAGE_NAME = "ee.smkv.calc.loan";
//	public static String CLASS_NAME = "ee.smkv.calc.loan.MainActivity";
//	public static Comparator COMPARATOR = new CustomWidgetsComparator(TEXT_VIEW, DIALOG_VIEW, MENU_VIEW, BUTTON);
//	public static int SLEEP_AFTER_EVENT = 2000;
//	public static int SLEEP_AFTER_RESTART = 500;

//	TomDroid
//	public static String PACKAGE_NAME = "org.tomdroid";
//	public static String CLASS_NAME = "org.tomdroid.ui.Tomdroid";
//	public static Comparator COMPARATOR = new CustomWidgetsDeepComparator(EDIT_TEXT, BUTTON, MENU_VIEW); //, DIALOG_VIEW, LIST_VIEW);
//	public static int SLEEP_AFTER_EVENT = 2000;
//	public static int SLEEP_AFTER_RESTART = 1000;
//	public static StrategyCriteria[] ADDITIONAL_CRITERIAS = new StrategyCriteria[] {
//		new AfterEventDontExplore("Service"),
//	};
//	public static InteractorAdapter[] ADDITIONAL_INPUTS = new InteractorAdapter[] { // Username and password
//		new FixedValueEditor().addIdValuePair(16908291, "\r\n"),
//	};
//	public static InteractorAdapter[] ADDITIONAL_EVENTS = new InteractorAdapter[] { // Username and password
//		new FixedValueEditor().addIdValuePair(16908291, "1", "2", "3", "http:\\"),
//	};
//	public static String[] PRECRAWLING = new String[] {
//		OPEN_MENU, null,
//		CLICK_ON_TEXT, "Settings", null
//	};


//	// Aarddict
//	public static String PACKAGE_NAME = "aarddict.android";
//	public static String CLASS_NAME = "aarddict.android.LookupActivity";
//	public static Comparator COMPARATOR = new CustomWidgetsComparator(EDIT_TEXT, BUTTON, MENU_VIEW, DIALOG_VIEW, LIST_VIEW);
//	public static int SLEEP_AFTER_EVENT = 6000;
//	public static int SLEEP_AFTER_RESTART = 6000;
//	static {
//		UserFactory.addEvent(CLICK);
//		UserFactory.forceIdsForEvent(CLICK, "2131099660");
//	}
//	public static InteractorAdapter[] ADDITIONAL_EVENTS = new InteractorAdapter[] { // Search
//		new FixedValueEditor().addIdValuePair(2131099648, "5"),
//	};
//	public static String[] PRECRAWLING = new String[] {
//		TYPE_TEXT, "2131099659", "wa", null,
//		CLICK_ON_TEXT, "Miller", null,
//	};
	
	// Book Catalog
//	public static String PACKAGE_NAME = "com.eleybourn.bookcatalogue";
//	public static String CLASS_NAME = "com.eleybourn.bookcatalogue.BookCatalogue";
//	public static Comparator COMPARATOR = new CustomWidgetsComparator(LIST_VIEW, BUTTON);
//	public static int SLEEP_AFTER_EVENT = 2000;
//	public static int SLEEP_AFTER_RESTART = 2000;
//	public static String[] PRECRAWLING = new String[] {
//		LIST_SELECT, "16908298", "2", null,
//		OPEN_MENU, null,
//		CLICK_ON_TEXT, "Duplicate Book", null,
//		TYPE_TEXT, "2131034177", "85", null,
//		CLICK, "2131034189", NULL, null,
//		CLICK, "2131034152", NULL, null,
//		CLICK, "2131034199", NULL, null,
//		CLICK_ON_TEXT, "Save Book", null,
//		LIST_SELECT, "16908298", "3", null,
//		CLICK, "2131034189", NULL, null,
//		SWAP_TAB, "16908306", "4", null,
//	};

//	public static String PACKAGE_NAME = "com.nofatclips.keyevent";
//	public static String CLASS_NAME = "com.nofatclips.keyevent.KeyEventsDumpActivity";
//	public static Comparator COMPARATOR = new CustomWidgetsDeepComparator(EDIT_TEXT);
//	public static int SLEEP_AFTER_EVENT = 4000;
//	public static int SLEEP_AFTER_RESTART = 4000;


	/*
	 * 				Default Parameters
	 */

	// Default events and inputs for the User
	static {
//		UserFactory.addEvent(CLICK, BUTTON, MENU_ITEM, LINEAR_LAYOUT, IMAGE_VIEW);
//		UserFactory.addEvent(LONG_CLICK, WEB_VIEW);
//		UserFactory.addEvent(LIST_SELECT, LIST_VIEW, SINGLE_CHOICE_LIST, PREFERENCE_LIST);
//		UserFactory.addEvent(LIST_LONG_SELECT, LIST_VIEW, SINGLE_CHOICE_LIST);
		UserFactory.addEvent(SWAP_TAB, TAB_HOST);
//		UserFactory.addInput(CLICK, CHECKBOX, RADIO, TOGGLE_BUTTON);
//		UserFactory.addInput(SET_BAR, SEEK_BAR);
//		UserFactory.addInput(WRITE_TEXT, EDIT_TEXT);
//		UserFactory.addInput(SPINNER_SELECT, SPINNER);
	}
	public static int[] KEY_EVENTS = {};
	
	// Precrawling sequence
	public static String[] PRECRAWLING = new String[] {};
	
	// Strategy Parameters
	public static int MAX_NUM_TRACES = 0; // After performing this amount of traces, the crawler exits (0 = no length limit)
	public static int PAUSE_AFTER_TRACES = 0; // After performing this amount of traces, the crawler pauses (0 = no pause)
	public static long MAX_TIME_CRAWLING = 0; // In seconds (0 = no time limit)
	public static long PAUSE_AFTER_TIME = 0; // In seconds (0 = no pause)
	public static int TRACE_MAX_DEPTH = 0; // Max number of transitions in a trace (0 = no depth limit)
	public static boolean CHECK_FOR_TRANSITION = false;
	public static boolean EXPLORE_ONLY_NEW_STATES = true;
//	public static Comparator COMPARATOR = new NullComparator();
	public static StrategyCriteria[] ADDITIONAL_CRITERIAS = new StrategyCriteria[] {};
	public static boolean COMPARE_STATE_TITLE = true;
	public static boolean COMPARE_LIST_COUNT = false;
	public static boolean COMPARE_VALUES = true; // Only for Deep comparator;
	
	// Persistence Parameters
	public static int MAX_TRACES_IN_RAM = 1; // After performing this amount of traces, the crawler saves to disk, empties the session and continues (0 = keep all in RAM)
	public static boolean ENABLE_RESUME = true;
	public static String TASK_LIST_FILE_NAME = "tasklist.xml"; // Save state for resume and optionally output
	public static String ACTIVITY_LIST_FILE_NAME = "activities.xml"; // Save state for resume
	public static String PARAMETERS_FILE_NAME = "parameters.obj"; // Save state for resume
	public static String FILE_NAME = "guitree.xml"; // Output
	public static String PREFERENCES_FILE = "preferences.xml";

	// Scheduler Parameters
	public static int MAX_TASKS_IN_SCHEDULER = 40;
	
	// Screenshot Parameters
	public static boolean SCREENSHOT_FOR_STATES = true; // Performs an image capture of the screen after processing a task
	public static boolean SCREENSHOT_ONLY_NEW_STATES = false; // Capture only if comparation was negative - Only relevant if SCREENSHOT_FOR_STATES is true
	public static boolean SCREENSHOT_FOR_EVENTS = false; // Performs an image capture of the screen before firing an event

	// User/Planner Parameters
	public static int MAX_EVENTS_PER_WIDGET = 5; // For GroupViews (0 = try all items in the group)
	public static boolean ALL_EVENTS_ON_PREFERENCES = true; // Bypass MAX_EVENTS_PER_WIDGET for PREFERENCE_LIST when true
	public static int MAX_TASKS_PER_EVENT = 1; // How many input sequences to generate for each event on a widget; 0 = no limit
	public static boolean EVENT_WHEN_NO_ID = false; // Whether to inject events on widgets without ID or not
	public static boolean BACK_BUTTON_EVENT = true;
	public static boolean MENU_EVENTS = true;
	public static boolean ORIENTATION_EVENTS = true;
	public static boolean TAB_EVENTS_START_ONLY = false; // true -> click on tabs only on the start activity
	public static boolean SCROLL_DOWN_EVENT = false;
	public static long RANDOM_SEED = 5466516511651561651L; // 0 = Random
	public static InteractorAdapter[] ADDITIONAL_EVENTS = new InteractorAdapter[] {};
	public static InteractorAdapter[] ADDITIONAL_INPUTS = new InteractorAdapter[] {};
	
	// More Parameters
	public static boolean IN_AND_OUT_FOCUS = true;
	public static boolean FORCE_RESTART = true;
	public static boolean ACTIVITY_DESCRIPTION_IN_SESSION = false;
	public static boolean RETRY_FAILED_TRACES = false; // Crashed and failed traces are retried once in case the failure had an asynchronous cause
	public static int SLEEP_ON_THROBBER = 30000; // How long to wait on spinning wheels (in ms -- 0 = don't wait)
	public static int SLEEP_AFTER_TASK = 0;
	public static String XML_BODY_BEGIN = "    <TRACE";
	public static String XML_BODY_END = "/TRACE>";
	public static CrawlerLog LOGGER = new SessionLogger();

	public static Class<?> theClass;
	static {
		try {
			theClass = Class.forName(CLASS_NAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}