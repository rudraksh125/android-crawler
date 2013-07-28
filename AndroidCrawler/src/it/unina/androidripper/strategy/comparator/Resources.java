package it.unina.androidripper.strategy.comparator;

import static com.nofatclips.androidtesting.model.SimpleType.*;
import it.unina.androidripper.Prefs;
import it.unina.androidripper.model.Comparator;
import it.unina.androidripper.model.ResourceFile;

@SuppressWarnings("unused")
public class Resources implements ResourceFile {

	public static String COMPARATOR_TYPE = "NullComparator";
	public static String[] WIDGET_TYPES = {};
	public static boolean COMPARE_ACTIVITY_NAME = true;
	public static boolean COMPARE_STATE_TITLE = false;
	public static boolean COMPARE_LIST_COUNT = false;
	public static boolean COMPARE_MENU_COUNT = true;
	public static boolean COMPARE_VALUES = false;
	
	public static void getComparator() {		
		if (COMPARATOR_TYPE.equals("") || (COMPARATOR_TYPE == null)) return;

		if (COMPARATOR_TYPE.equals("CustomWidgetsSimpleComparator")) {
			COMPARATOR = new CustomWidgetsSimpleComparator (WIDGET_TYPES);
		} else if (COMPARATOR_TYPE.equals("CustomWidgetsIntensiveComparator")) {
			COMPARATOR = new CustomWidgetsIntensiveComparator (WIDGET_TYPES);
		} else if (COMPARATOR_TYPE.equals("NameComparator")) {
			COMPARATOR = new NameComparator ();
		} else {
			try {
				String className = (COMPARATOR_TYPE.indexOf(".") == -1)?
						("it.unina.androidripper.strategy.comparator." + COMPARATOR_TYPE):COMPARATOR_TYPE;
				Class<?> theClass = Class.forName(className);
				COMPARATOR = (Comparator) theClass.newInstance();
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		
	}
	
	public static Comparator COMPARATOR = new NullComparator();
	static {
		Prefs.updateNode("comparator", Resources.class);
		getComparator();
	}

}