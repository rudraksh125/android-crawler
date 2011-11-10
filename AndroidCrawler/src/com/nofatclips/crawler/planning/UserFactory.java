package com.nofatclips.crawler.planning;

import java.util.HashMap;
import static com.nofatclips.androidtesting.model.InteractionType.*;
import static com.nofatclips.androidtesting.model.SimpleType.*;
import static com.nofatclips.crawler.Resources.*;

import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.model.UserAdapter;
import com.nofatclips.crawler.planning.interactors.*;

public class UserFactory {

	public static InteractionMap eventToTypeMap = new InteractionMap();
	public static InteractionMap inputToTypeMap = new InteractionMap();

	public enum Category {
		EVENT, INPUT, SPECIAL
	}
	
	public static void addEvent (String eventType, String ... widgetTypes) {
		eventToTypeMap.put(eventType, widgetTypes);
	}

	public static void addInput (String inputType, String ... widgetTypes) {
		inputToTypeMap.put(inputType, widgetTypes);
	}

	public static boolean requiredEvent (String interaction) {
		return isRequired (Category.EVENT, interaction);
	}

	public static boolean requiredInput (String interaction) {
		return isRequired (Category.INPUT, interaction);
	}

	public static boolean isRequired (Category c, String interaction) {
		switch (c) {
		case EVENT:
			return UserFactory.eventToTypeMap.containsKey(interaction);
		case INPUT:
			return UserFactory.inputToTypeMap.containsKey(interaction);
		default:
			return false;
		}
	}

	public static String[] typesForEvent (String interaction) {
		return UserFactory.eventToTypeMap.get(interaction);
	}

	public static String[] typesForInput (String interaction) {
		return UserFactory.inputToTypeMap.get(interaction);
	}

	public static boolean doLongClick() {
		return LONG_CLICK_EVENT;
	}

	public static boolean doLongClickOnLists() {
		return LONG_CLICK_LIST_EVENT;
	}

	public static UserAdapter getUser (Abstractor a) {
		SimpleUser u = new SimpleUser (a);

		Clicker c = (requiredEvent(CLICK))?new Clicker (a, typesForEvent(CLICK)):new Clicker (a, BUTTON);
		c.setEventWhenNoId(EVENT_WHEN_NO_ID);
		u.addEvent(c);

		if (doLongClick()) {
			LongClicker l = (requiredEvent(LONG_CLICK))?new LongClicker (a, typesForEvent(LONG_CLICK)):new LongClicker (a, BUTTON);
			l.setEventWhenNoId(EVENT_WHEN_NO_ID);
			u.addEvent(l);
		}
		
		ListSelector ls = (requiredEvent(LIST_SELECT))?
				new ListSelector (a, MAX_EVENTS_PER_WIDGET, typesForEvent(LIST_SELECT)):new ListSelector(a, MAX_EVENTS_PER_WIDGET);
		ls.setEventWhenNoId(true);
		u.addEvent(ls);
		
		if (doLongClickOnLists()) {
			ListLongClicker llc = (requiredEvent(LIST_LONG_SELECT))?
					new ListLongClicker (a, MAX_EVENTS_PER_WIDGET, typesForEvent(LIST_LONG_SELECT)):new ListLongClicker(a, MAX_EVENTS_PER_WIDGET);
			llc.setEventWhenNoId(true);
			u.addEvent(llc);
		}
		
		return u;
	}
	
	@SuppressWarnings("serial")
	public static class InteractionMap extends HashMap<String,String[]> {
		
	}
	
}
