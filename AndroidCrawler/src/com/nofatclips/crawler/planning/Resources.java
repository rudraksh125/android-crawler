package com.nofatclips.crawler.planning;

import static com.nofatclips.androidtesting.model.InteractionType.*;
import static com.nofatclips.androidtesting.model.SimpleType.*;

import java.util.Arrays;

import com.nofatclips.crawler.Prefs;
import com.nofatclips.crawler.model.ResourceFile;

public class Resources implements ResourceFile {

	/** @author nicola */
	//nome classe del planner (deve ereditare da SimplePlanner)
	public static String PLANNER = "DictionarySimplePlanner";
	//public static String PLANNER = "SimplePlanner";

	//comportamento di write_text/type_text normale o con valori presi dal dizionario (true)
	public static boolean TEXT_VALUES_FROM_DICTIONARY = true;
	
	//public static boolean VALID_DICTIONARY_VALUES = true;
	//public static boolean INVALID_DICTIONARY_VALUES = true;
	
	//il valore deve essere sistematico o meno
	public static boolean DICTIONARY_FIXED_VALUE = true;
	/** @author nicola */
	
	public static String EVENTS[];
	public static String INPUTS[];
	
	// Default events and inputs for the User
	static {
		UserFactory.addEvent(CLICK, BUTTON, MENU_ITEM, LINEAR_LAYOUT, IMAGE_VIEW);
//		UserFactory.addEvent(LONG_CLICK, WEB_VIEW);
//		UserFactory.addEvent(LIST_SELECT, LIST_VIEW, SINGLE_CHOICE_LIST, PREFERENCE_LIST);
//		UserFactory.addEvent(LIST_LONG_SELECT, LIST_VIEW, SINGLE_CHOICE_LIST);
//		UserFactory.addEvent(SWAP_TAB, TAB_HOST);
//		UserFactory.addInput(CLICK, CHECKBOX, TOGGLE_BUTTON);
		UserFactory.addInput(RADIO_SELECT, RADIO_GROUP);
//		UserFactory.addInput(SET_BAR, SEEK_BAR);
		UserFactory.addInput(WRITE_TEXT, EDIT_TEXT);
//		UserFactory.addInput(SPINNER_SELECT, SPINNER);
//		UserFactory.addInput(LIST_SELECT, MULTI_CHOICE_LIST);
	}

	// User/Planner Parameters
	public static int MAX_EVENTS_PER_WIDGET = 5; // For GroupViews (0 = try all items in the group)
	public static int MAX_TASKS_PER_EVENT = 1; // How many input sequences to generate for each event on a widget; 0 = no limit

	public static boolean BACK_BUTTON_EVENT = true;
	public static boolean MENU_EVENTS = false;
	public static boolean ORIENTATION_EVENTS = false;
	public static boolean SCROLL_DOWN_EVENT = false;

	public static boolean TAB_EVENTS_START_ONLY = false; // true -> click on tabs only on the start activity
	public static boolean EVENT_WHEN_NO_ID = false; // Whether to inject events on widgets without ID or not
	public static boolean ALL_EVENTS_ON_PREFERENCES = true; // Bypass MAX_EVENTS_PER_WIDGET for PREFERENCE_LIST when true
	
	public static int[] KEY_EVENTS = {};

	// Scheduler Parameters
	public static String SCHEDULER_ALGORITHM = "BREADTH_FIRST";
	public static int MAX_TASKS_IN_SCHEDULER = 40;
	
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
