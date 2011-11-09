package com.nofatclips.crawler.planning;

import java.util.HashMap;
import java.util.Map;

import static com.nofatclips.androidtesting.model.InteractionType.*;
import static com.nofatclips.androidtesting.model.SimpleType.*;
import static com.nofatclips.crawler.Resources.*;

import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.model.UserAdapter;
import com.nofatclips.crawler.planning.interactors.*;

public class UserFactory {

	public static Map<String,String[]> typeMap = new HashMap<String,String[]>();
	
	public static void addInteraction (String eventType, String ... widgetTypes) {
		typeMap.put(eventType, widgetTypes);
	}
	
	public static boolean isDefined (String interaction) {
		return UserFactory.typeMap.containsKey(interaction);
	}
	
	public static String[] typesForEvent (String interaction) {
		return UserFactory.typeMap.get(interaction);
	}

	public static boolean doLongClick() {
		return LONG_CLICK_EVENT;
	}

	public static boolean doLongClickOnLists() {
		return LONG_CLICK_LIST_EVENT;
	}

	public static UserAdapter getUser (Abstractor a) {
		SimpleUser u = new SimpleUser (a);

		Clicker c = (isDefined(CLICK))?new Clicker (a, typesForEvent(CLICK)):new Clicker (a, BUTTON);
		u.setEventClicker(c);

		if (doLongClick()) {
			LongClicker l = (isDefined(LONG_CLICK))?new LongClicker (a, typesForEvent(LONG_CLICK)):new LongClicker (a, BUTTON);
			u.setEventLongClicker(l);
		}
		
		ListSelector ls = (isDefined(LIST_SELECT))?new ListSelector (a, MAX_EVENTS_PER_WIDGET, typesForEvent(LIST_SELECT)):new ListSelector(a, MAX_EVENTS_PER_WIDGET);
		u.setListSelector(ls);
		
		if (doLongClickOnLists()) {
			ListLongClicker llc = (isDefined(LIST_LONG_SELECT))?new ListLongClicker (a, MAX_EVENTS_PER_WIDGET, typesForEvent(LIST_LONG_SELECT)):new ListLongClicker(a, MAX_EVENTS_PER_WIDGET);
			u.setListLongClicker(llc);
		}
		
		return u;
	}
	
}
