package it.unina.androidripper;

import it.unina.androidripper.model.Comparator;
import it.unina.androidripper.model.ResourceFile;
import it.unina.androidripper.model.StrategyCriteria;
import it.unina.androidripper.planning.UserFactory;
import it.unina.androidripper.planning.adapters.InteractorAdapter;
import it.unina.androidripper.planning.interactors.*;
import it.unina.androidripper.storage.*;
import it.unina.androidripper.strategy.comparator.*;
import it.unina.androidripper.strategy.criteria.*;

import com.nofatclips.androidtesting.model.InteractionType;
import android.util.Log;

import static com.nofatclips.androidtesting.model.SimpleType.*;
import static com.nofatclips.androidtesting.model.InteractionType.*;
import static android.view.KeyEvent.*;

@SuppressWarnings("unused")
public class Resources implements ResourceFile {

	public final static String PREFERENCES_FILE = "preferences.xml";

	public static String PACKAGE_NAME = "app.package";
	public static String CLASS_NAME = "app.package.class";
	public static StrategyCriteria[] ADDITIONAL_CRITERIAS = new StrategyCriteria[] {};
	public static InteractorAdapter[] ADDITIONAL_EVENTS = new InteractorAdapter[] {};
	public static InteractorAdapter[] ADDITIONAL_INPUTS = new InteractorAdapter[] {};
		
	// More Parameters
	public final static String CRAWLER_PACKAGE = "it.unina.androidripper";
	public static boolean ENABLE_RESUME = true;
	public static boolean ENABLE_MODEL = true;
	public static long RANDOM_SEED = 5466516511651561651L; // 0 = Random
	public static boolean ACTIVITY_DESCRIPTION_IN_SESSION = false;
	public static boolean RETRY_FAILED_TRACES = false; // Crashed and failed traces are retried once in case the failure had an asynchronous cause
	public final static String XML_BODY_BEGIN = "    <TRACE";
	public final static String XML_BODY_END = "/TRACE>";
	
	public static Class<?> theClass;
	static {
		Prefs.setMainNode(CRAWLER_PACKAGE);
		Prefs.updateMainNode();

		try {
			theClass = Class.forName(CLASS_NAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
