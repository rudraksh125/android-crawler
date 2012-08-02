package com.nofatclips.crawler.planning;

import java.util.*;

import static com.nofatclips.androidtesting.model.InteractionType.*;
import static com.nofatclips.crawler.Resources.*;

import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.model.UserAdapter;
import com.nofatclips.crawler.planning.adapters.InteractorAdapter;
import com.nofatclips.crawler.planning.interactors.*;

public class UserFactory {

	public static InteractionMap eventToTypeMap = new InteractionMap();
	public static InteractionMap inputToTypeMap = new InteractionMap();
	public static Map<String,List<String>> vetoesMap = new Hashtable<String,List<String>>();
	public static Map<String,List<String>> overridesMap = new Hashtable<String,List<String>>();
	
	public final static String ALL = "ALL";

	public enum Category {
		EVENT, INPUT, SPECIAL
	}
	
	public static void addEvent (String eventType, String ... widgetTypes) {
		eventToTypeMap.put(eventType, widgetTypes);
	}

	public static void addInput (String inputType, String ... widgetTypes) {
		inputToTypeMap.put(inputType, widgetTypes);
	}
	
	public static void resetEvents () {
		eventToTypeMap.clear();
	}

	public static void resetInputs () {
		inputToTypeMap.clear();
	}

	public static void denyIds (String ... ids) {
		denyIdsForEvent(ALL, ids);
	}

	public static void denyIdsForEvent (String event, String ... ids) {
		if (!vetoesMap.containsKey(event)) {
			vetoesMap.put(event, new ArrayList<String>());
		}
		for (String id: ids) {
			vetoesMap.get(event).add(id);
		}
	}

	public static void denyIds (int ... ids) {
		for (Integer id: ids) {
			denyIdsForEvent(ALL, String.valueOf(id));
		}
	}

	public static void forceIds (String ... ids) {
		forceIdsForEvent(ALL, ids);
	}

	public static void forceIdsForEvent (String event, String ... ids) {
		if (!overridesMap.containsKey(event)) {
			overridesMap.put(event, new ArrayList<String>());
		}
		for (String id: ids) {
			overridesMap.get(event).add(id);
		}
	}

	public static void forceIds (int ... ids) {
		for (Integer id: ids) {
			forceIdsForEvent(ALL, String.valueOf(id));
		}
	}

	public static void denyInteractionOnIds (String event, int ... ids) {
		for (Integer id: ids) {
			denyIdsForEvent(event, String.valueOf(id));
		}
	}

	public static InteractorAdapter addDosAndDonts (InteractorAdapter i) {
		addVetoes (i, ALL);
		addVetoes (i, i.getInteractionType());
		addOverrides (i, ALL);
		addOverrides (i, i.getInteractionType());
		return i;
	}
	
	public static void addVetoes (InteractorAdapter i, String event) {
		if (vetoesMap.containsKey(event)) {
			i.denyIds(vetoesMap.get(event));
		}		
	}

	public static void addOverrides (InteractorAdapter i, String event) {
		if (overridesMap.containsKey(event)) {
			i.forceIds(overridesMap.get(event));
		}		
	}

	public static boolean isRequiredEvent (String interaction) {
		return isRequired (Category.EVENT, interaction);
	}

	public static boolean isRequiredInput (String interaction) {
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
		return eventToTypeMap.get(interaction);
	}

	public static String[] typesForInput (String interaction) {
		return inputToTypeMap.get(interaction);
	}

	public static boolean doForceSeed () {
		return (RANDOM_SEED==0);
	}
	
	public static boolean isUserSimple () {
		int m = Resources.MAX_TASKS_PER_EVENT;
		return (m == 1);
	}
	
	public static UserAdapter getUser (Abstractor a) {
		
		UserAdapter u;

		if (isUserSimple()) {
			u = (doForceSeed())?new SimpleUser (a):new SimpleUser(a,new Random(RANDOM_SEED));
		} else {
			u = (doForceSeed())?new AlternativeUser (a):new AlternativeUser(a,new Random(RANDOM_SEED));
		}

		if (isRequiredEvent(CLICK)) {
			Clicker c = new Clicker (typesForEvent(CLICK));
			c.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
			u.addEvent(addDosAndDonts(c));
		}

		if (isRequiredEvent(LONG_CLICK)) {
			LongClicker l = new LongClicker (typesForEvent(LONG_CLICK));
			l.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
			u.addEvent(addDosAndDonts(l));
		}
		
		if (isRequiredEvent(LIST_SELECT)) {			
			ListSelector ls = new ListSelector (Resources.MAX_EVENTS_PER_WIDGET, typesForEvent(LIST_SELECT));
			ls.setEventWhenNoId(true);
			u.addEvent(addDosAndDonts(ls));
		}
		
		if (isRequiredEvent(LIST_LONG_SELECT)) {
			ListLongClicker llc = new ListLongClicker (Resources.MAX_EVENTS_PER_WIDGET, typesForEvent(LIST_LONG_SELECT));
			llc.setEventWhenNoId(true);
			u.addEvent(addDosAndDonts(llc));			
		}
		
		if (isRequiredEvent(SPINNER_SELECT)) {
			SpinnerSelector ss = new SpinnerSelector (Resources.MAX_EVENTS_PER_WIDGET, typesForEvent(SPINNER_SELECT));
			ss.setEventWhenNoId(false);
			u.addEvent(addDosAndDonts(ss));
		}

		if (isRequiredEvent(RADIO_SELECT)) {
			RadioSelector rs = new RadioSelector (Resources.MAX_EVENTS_PER_WIDGET, typesForEvent(RADIO_SELECT));
			rs.setEventWhenNoId(false);
			u.addEvent(addDosAndDonts(rs));
		}

		if (isRequiredEvent(SWAP_TAB)) {
			TabSwapper ts = new TabSwapper (typesForEvent(SWAP_TAB));
			if (Resources.TAB_EVENTS_START_ONLY) {
				ts.setOnlyOnce(true);
			}
			u.addEvent(addDosAndDonts(ts));
		}
		
		for (InteractorAdapter i: ADDITIONAL_EVENTS) {
			i.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
			u.addEvent(addDosAndDonts(i));			
		}
		
		if (isRequiredInput(CLICK)) {
			Clicker c2 = new Clicker (typesForInput(CLICK));
			c2.setEventWhenNoId(false);
			u.addInput (addDosAndDonts(c2));
		}

		if (isRequiredInput(SET_BAR)) {
			Slider sl = new Slider (typesForInput(SET_BAR));
			sl.setEventWhenNoId(false);
			u.addInput (addDosAndDonts(sl));
		}
		
		if (isRequiredInput(TYPE_TEXT)) {
			RandomEditor re = new RandomEditor(typesForInput(TYPE_TEXT));
			re.setEventWhenNoId(false);
			u.addInput (addDosAndDonts(re));
		}

		if (isRequiredInput(WRITE_TEXT)) {
			RandomWriter re = new RandomWriter(typesForInput(WRITE_TEXT));
			re.setEventWhenNoId(false);
			u.addInput (addDosAndDonts(re));
		}

		if (isRequiredInput(SPINNER_SELECT)) {
			RandomSpinnerSelector rss = new RandomSpinnerSelector(typesForInput(SPINNER_SELECT));
			rss.setEventWhenNoId(false);
			u.addInput(addDosAndDonts(rss));
		}

		if (isRequiredInput(RADIO_SELECT)) {
			RandomRadioSelector rrs = new RandomRadioSelector(typesForInput(RADIO_SELECT));
			rrs.setEventWhenNoId(false);
			u.addInput(addDosAndDonts(rrs));
		}

		if (isRequiredInput(LIST_SELECT)) {
			RandomListSelector rls = new RandomListSelector(typesForInput(LIST_SELECT));
			rls.setEventWhenNoId(false);
			u.addInput(addDosAndDonts(rls));
		}

		for (InteractorAdapter i: ADDITIONAL_INPUTS) {
			i.setEventWhenNoId(false);
			u.addInput(addDosAndDonts(i));
		}
		
		return u;
	}
	
	@SuppressWarnings("serial")
	public static class InteractionMap extends HashMap<String,String[]> {}
	
}