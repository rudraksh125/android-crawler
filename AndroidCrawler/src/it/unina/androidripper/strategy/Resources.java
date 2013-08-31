package it.unina.androidripper.strategy;

import it.unina.androidripper.Prefs;
import it.unina.androidripper.model.ResourceFile;
import it.unina.androidripper.model.StrategyCriteria;
import it.unina.androidripper.strategy.criteria.*;

@SuppressWarnings("unused")
public class Resources implements ResourceFile {

	// Strategy Parameters
	public static int MAX_NUM_TRACES = 0; // After performing this amount of traces, the crawler exits (0 = no length limit)
	public static int PAUSE_AFTER_TRACES = 1; // After performing this amount of traces, the crawler pauses (0 = no pause)
	public static long MAX_TIME_CRAWLING = 0; // In seconds (0 = no time limit)
	public static long PAUSE_AFTER_TIME = 0; // In seconds (0 = no pause)
	public static int TRACE_MAX_DEPTH = 0; // Max number of transitions in a trace (0 = no depth limit)
	public static int TRACE_MIN_DEPTH = 0; // Ignore the exploration criteria until this depth is reached
	
	public static boolean CHECK_FOR_TRANSITION = false;
	public static boolean EXPLORE_ONLY_NEW_STATES = true;
	
	// Additional Criteria
	public static StrategyCriteria[] ADDITIONAL_CRITERIA = new StrategyCriteria[] {};
	
	public static String[] AFTER_EVENT_DONT_EXPLORE = {};
	public static int[] AFTER_WIDGET_DONT_EXPLORE = {};

	static {
		Prefs.updateNode("strategy", Resources.class);
	}
}
