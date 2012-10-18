package com.nofatclips.crawler.automation;

import com.nofatclips.crawler.Prefs;
import com.nofatclips.crawler.model.ResourceFile;

//import static com.nofatclips.androidtesting.model.InteractionType.*;
//import static com.nofatclips.androidtesting.model.SimpleType.*;

public class Resources implements ResourceFile {

	// Precrawling sequence
	public static int SLEEP_AFTER_EVENT = 1000;
	public static int SLEEP_AFTER_RESTART = 1000;
	public static int SLEEP_ON_THROBBER = 1000; // How long to wait on spinning wheels (in ms -- 0 = don't wait)
	public static int SLEEP_AFTER_TASK = 1000;

	public static boolean FORCE_RESTART = false;
	public static boolean IN_AND_OUT_FOCUS = true;

	public static String[] PRECRAWLING = new String[] {};

	// Screenshot Parameters
	public static boolean SCREENSHOT_FOR_STATES = true; // Performs an image capture of the screen after processing a task
	public static boolean SCREENSHOT_FOR_EVENTS = true; // Performs an image capture of the screen before firing an event
	public static boolean SCREENSHOT_ONLY_NEW_STATES = false; // Capture only if comparation was negative - Only relevant if SCREENSHOT_FOR_STATES is true

	static {
		Prefs.updateNode("automation", Resources.class);
		Prefs.updateNode("screenshot", Resources.class);
	}

}
