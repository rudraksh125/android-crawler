package com.nofatclips.crawler;

public class Resources {

//	public final static String PACKAGE_NAME = "com.softmimo.android.mileagetracker";
//	public final static String CLASS_NAME = "com.softmimo.android.mileagetracker.ListView";
//	public final static String FILE_NAME = "mileage_tracker.xml";
//	public final static Comparator COMPARATOR = new NameComparator();
//	public final static int SLEEP_AFTER_EVENT = 2000;

//	public final static String PACKAGE_NAME = "com.blazing_skies.caloriecalculator";
//	public final static String CLASS_NAME = "com.blazing_skies.caloriecalculator.PreMainActivity";
//	public final static String FILE_NAME = "calorie.xml";
//	public final static Comparator COMPARATOR = new NullComparator();
//	public final static int SLEEP_AFTER_EVENT = 0;

	public final static String PACKAGE_NAME = "com.evancharlton.mileage";
	public final static String CLASS_NAME = "com.evancharlton.mileage.Mileage";
	public final static String FILE_NAME = "mileage.xml";
	public final static Comparator COMPARATOR = new NameComparator();
	public final static int SLEEP_AFTER_EVENT = 2000;

	public final static CrawlerLog LOGGER = new SessionLogger();
	
	public static Class<?> theClass;
	static {
		try {
			theClass = Class.forName(CLASS_NAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
