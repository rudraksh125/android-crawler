package com.nofatclips.crawler.planning;

import static com.nofatclips.androidtesting.model.InteractionType.*;
import static com.nofatclips.androidtesting.model.SimpleType.*;

import java.util.Arrays;

import android.location.LocationManager;

import com.nofatclips.crawler.Prefs;
import com.nofatclips.crawler.model.ResourceFile;

@SuppressWarnings("unused")
public class Resources implements ResourceFile {

	/** @author nicola */
	//nome classe del planner (deve ereditare da SimplePlanner)
	//public static String PLANNER = "DictionarySimplePlanner";
	//public static String PLANNER = "SimpleReflectionPlanner";
	public static String PLANNER = "SimplePlanner";

	//comportamento di write_text/type_text normale o con valori presi dal dizionario (true)
	public static boolean TEXT_VALUES_FROM_DICTIONARY = false;
	
	//public static boolean VALID_DICTIONARY_VALUES = true;
	//public static boolean INVALID_DICTIONARY_VALUES = true;
	
	//il valore deve essere sistematico o meno (attiva il caching dei valori)
	public static boolean DICTIONARY_FIXED_VALUE = false;
	
	//prende un valore casuale di qualsiasi contentType dal dizionario
	public static boolean DICTIONARY_IGNORE_CONTENT_TYPES = false;
	/** @author nicola */
	
	/** @author Nicola */
	//utilzza gli hash degli id delle EditText per riempire i campi
	//(e' escluso se si utilizza il dizionario)
	public static boolean TEXT_VALUES_ID_HASH = false;
	/** @author Nicola */
	
	public static String EVENTS[];
	public static String INPUTS[];
	
//	// Default events and inputs for the User
//	static {
//		UserFactory.addEvent(CLICK, BUTTON, MENU_ITEM, LINEAR_LAYOUT, IMAGE_VIEW);
//		UserFactory.addEvent(FOCUS, FOCUSABLE_EDIT_TEXT);
//		UserFactory.addEvent(LONG_CLICK, WEB_VIEW);
//		UserFactory.addEvent(LIST_SELECT, LIST_VIEW, SINGLE_CHOICE_LIST, PREFERENCE_LIST);
//		UserFactory.addEvent(LIST_LONG_SELECT, LIST_VIEW, SINGLE_CHOICE_LIST);
//		UserFactory.addEvent(SWAP_TAB, TAB_HOST);
//		UserFactory.addInput(CLICK, CHECKBOX, TOGGLE_BUTTON);
//		UserFactory.addInput(RADIO_SELECT, RADIO_GROUP);
//		UserFactory.addInput(SET_BAR, SEEK_BAR);
//		UserFactory.addInput(TYPE_TEXT, EDIT_TEXT);
//		UserFactory.addInput(SPINNER_SELECT, SPINNER);
//		UserFactory.addInput(LIST_SELECT, MULTI_CHOICE_LIST);
//	}

	// User/Planner Parameters
	public static int MAX_EVENTS_PER_WIDGET = 0; // For GroupViews (0 = try all items in the group)
	public static int MAX_TASKS_PER_EVENT = 1; // How many input sequences to generate for each event on a widget; 0 = no limit

	public static boolean BACK_BUTTON_EVENT = true;
	public static boolean MENU_EVENTS = true;
	public static boolean ORIENTATION_EVENTS = true;
	public static boolean SCROLL_DOWN_EVENT = false;

	public static boolean TAB_EVENTS_START_ONLY = false; // true -> click on tabs only on the start activity
	public static boolean EVENT_WHEN_NO_ID = true; // Whether to inject events on widgets without ID or not
	public static boolean ALL_EVENTS_ON_PREFERENCES = true; // Bypass MAX_EVENTS_PER_WIDGET for PREFERENCE_LIST when true
	
	public static int[] KEY_EVENTS = {};

	// Scheduler Parameters
	public static String SCHEDULER_ALGORITHM = "BREADTH_FIRST";
	public static int MAX_TASKS_IN_SCHEDULER = 0;
	
	/** @author nicola amatucci - sensori/reflection */
	public static boolean REFLECT_WIDGETS = false;
	public static boolean REFLECT_ACTIVITY_LISTENERS = false;
	
	//Sensor events on/off
	public static boolean USE_SENSORS = false;
	public static boolean EXCLUDE_WIDGETS_INPUTS_IN_SENSORS_EVENTS = true; // aggiunge input prima di scatenare l'evento "sensore"
	
	//Sensors used
	public static Integer[] SENSOR_TYPES = new Integer[] {
		//android.hardware.Sensor.TYPE_ACCELEROMETER,
		//android.hardware.Sensor.TYPE_ORIENTATION // Inclinazione del dispositivo + bussola e accelerometro 
		//,
		//android.hardware.Sensor.TYPE_MAGNETIC_FIELD,
		//android.hardware.Sensor.TYPE_TEMPERATURE
		//android.hardware.Sensor.TYPE_GRAVITY
		//android.hardware.Sensor.TYPE_GYROSCOPE
		//android.hardware.Sensor.TYPE_LIGHT
		//android.hardware.Sensor.TYPE_LINEAR_ACCELERATION
		//android.hardware.Sensor.TYPE_PRESSURE
		//android.hardware.Sensor.TYPE_PROXIMITY
		//android.hardware.Sensor.TYPE_ROTATION_VECTOR
	};
	
	//location provider
	public static boolean USE_GPS = false;
	public static boolean EXCLUDE_WIDGETS_INPUTS_IN_GPS_EVENTS = true; // aggiunge input prima di scatenare l'evento "gps"	
	public static String TEST_LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
	
	//sms, call
	public static boolean SIMULATE_INCOMING_CALL = false;
	public static boolean SIMULATE_INCOMING_SMS = false;
	
	//for ddms socket connection
	public static int EMULATOR_PORT = 5554;
	/** @author nicola amatucci - sensori/reflection */
	
	
	
	
	
	static {
		Prefs.updateNode("scheduler", Resources.class);
		Prefs.updateNode("planner", Resources.class);
		Prefs.updateNode("interactions", Resources.class);
		if (EVENTS != null) {
			UserFactory.resetEvents();
			for (String s: EVENTS) {
				String[] widgets = s.split("( )?,( )?");
				UserFactory.addEvent(widgets[0], Arrays.copyOfRange(widgets, 1, widgets.length));
			}
		}
		if (INPUTS != null) {
			UserFactory.resetInputs();
			for (String s: INPUTS) {
				String[] widgets = s.split("( )?,( )?");
				UserFactory.addInput(widgets[0], Arrays.copyOfRange(widgets, 1, widgets.length));
			}
		}
	}

}
