package it.unina.androidripper.storage;

import it.unina.androidripper.Prefs;
import it.unina.androidripper.model.ResourceFile;

public class Resources implements ResourceFile {

	public static int MAX_TRACES_IN_RAM = 1; // After performing this amount of traces, the crawler saves to disk, empties the session and continues (0 = keep all in RAM)

	public static String TASK_LIST_FILE_NAME = "tasklist.xml"; // Save state for resume and optionally output
	public static String ACTIVITY_LIST_FILE_NAME = "activities.xml"; // Save state for resume
	public static String PARAMETERS_FILE_NAME = "parameters.obj"; // Save state for resume
	public static String FILE_NAME = "guitree.xml"; // Output
	
	public static boolean ACTIVITY_DESCRIPTION_IN_SESSION = false;
	public static boolean ENABLE_RESUME = true;
	
	public static boolean ENABLE_MODEL = true;
	public static boolean ONLY_FINALTRANSITION = false;
	
	public final static String XML_BODY_BEGIN = "    <TRACE";
	public final static String XML_BODY_END = "/TRACE>";
	
	static {
		Prefs.updateNode("storage", Resources.class);
	}

}
