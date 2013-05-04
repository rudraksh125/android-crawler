package it.unina.androidripper.strategy.comparator;

import static com.nofatclips.androidtesting.model.SimpleType.BUTTON;
import static com.nofatclips.androidtesting.model.SimpleType.EDIT_TEXT;
import static com.nofatclips.androidtesting.model.SimpleType.IMAGE_VIEW;
import static com.nofatclips.androidtesting.model.SimpleType.LIST_VIEW;
import static com.nofatclips.androidtesting.model.SimpleType.MENU_VIEW;
import it.unina.androidripper.Prefs;
import it.unina.androidripper.model.Comparator;
import it.unina.androidripper.model.ResourceFile;


@SuppressWarnings("unused")
public class Resources implements ResourceFile {

//	public static Comparator COMPARATOR = new CustomWidgetsComparator(CustomWidgetsComparator.IGNORE_ACTIVITY_NAME, EDIT_TEXT, BUTTON, LIST_VIEW, MENU_VIEW, IMAGE_VIEW);
	public static String COMPARATOR_TYPE = "NullComparator";
	public static String[] WIDGET_TYPES = {};
	public static boolean COMPARE_ACTIVITY_NAME = true;
	public static boolean COMPARE_STATE_TITLE = false;
	public static boolean COMPARE_LIST_COUNT = false;
	public static boolean COMPARE_MENU_COUNT = true;
	public static boolean COMPARE_VALUES = false;
	
	public static void getComparator() {		
		if (COMPARATOR_TYPE.equals("") || (COMPARATOR_TYPE == null)) return;

		if (COMPARATOR_TYPE.equals("CustomWidgetsComparator")) {
			COMPARATOR = new CustomWidgetsComparator (WIDGET_TYPES);
		} else if (COMPARATOR_TYPE.equals("CustomWidgetsDeepComparator")) {
			COMPARATOR = new CustomWidgetsDeepComparator (WIDGET_TYPES);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
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
