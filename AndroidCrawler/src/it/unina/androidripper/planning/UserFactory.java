package it.unina.androidripper.planning;

import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.model.UserAdapter;
import it.unina.androidripper.planning.adapters.InteractorAdapter;
import it.unina.androidripper.planning.interactors.*;

import java.util.*;

import static com.nofatclips.androidtesting.model.InteractionType.*;
import static it.unina.androidripper.Resources.*;


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

		if (isRequiredEvent(FOCUS)) {
			
			if (Resources.TEXT_VALUES_FROM_DICTIONARY)
			{
				DictionaryFocusWriter f = new DictionaryFocusWriter(typesForEvent(FOCUS));
				f.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(f));
			}
			else if(Resources.TEXT_VALUES_ID_HASH)
			{
				HashFocusWriter f = new HashFocusWriter(typesForEvent(FOCUS));
				f.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(f));
			}			
			else
			{
				RandomFocusWriter f = new RandomFocusWriter(typesForEvent(FOCUS));				
				f.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(f));
			}
			
		}
		
		if (isRequiredEvent(DRAG)) {
			Drager d = new Drager(typesForEvent(DRAG));
			d.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
			u.addEvent(addDosAndDonts(d));
		}
		
		if (isRequiredEvent(TYPE_TEXT)) {
			if (Resources.TEXT_VALUES_FROM_DICTIONARY)
			{
				DictionaryValueEditor te = new DictionaryValueEditor(typesForEvent(TYPE_TEXT));
				te.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(te));	
			}
			else if(Resources.TEXT_VALUES_ID_HASH)
			{
				HashValueEditor te = new HashValueEditor(typesForEvent(TYPE_TEXT));
				te.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(te));
			}
			else
			{
				RandomEditor te = new RandomEditor(typesForEvent(TYPE_TEXT));
				te.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(te));			
			}
		}

		if (isRequiredEvent(WRITE_TEXT)) {
			if (Resources.TEXT_VALUES_FROM_DICTIONARY)
			{
				DictionaryValueWriter we = new DictionaryValueWriter(typesForEvent(WRITE_TEXT));
				we.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(we));
			}
			else if(Resources.TEXT_VALUES_ID_HASH)
			{
				HashValueWriter we = new HashValueWriter(typesForEvent(WRITE_TEXT));
				we.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(we));
			}			
			else
			{
				RandomWriter we = new RandomWriter(typesForEvent(WRITE_TEXT));				
				we.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(we));
			}
		}
		
		if (isRequiredEvent(SEARCH_TEXT)) {
			if (Resources.TEXT_VALUES_FROM_DICTIONARY)
			{
				DictionaryValueSearchWriter ste = new DictionaryValueSearchWriter(typesForEvent(SEARCH_TEXT));
				ste.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(ste));
			}
			else if(Resources.TEXT_VALUES_ID_HASH)
			{
				HashValueSearchWriter ste = new HashValueSearchWriter(typesForEvent(SEARCH_TEXT));
				ste.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(ste));
			}			
			else
			{
				RandomSearchWriter ste = new RandomSearchWriter(typesForEvent(SEARCH_TEXT));				
				ste.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(ste));
			}
		}
		
		if (isRequiredEvent(AUTO_TEXT)) {
			if (Resources.TEXT_VALUES_FROM_DICTIONARY)
			{
				DictionaryValueAutoEditor ate = new DictionaryValueAutoEditor(typesForEvent(AUTO_TEXT));
				ate.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(ate));	
			}
			else if(Resources.TEXT_VALUES_ID_HASH)
			{
				HashValueAutoEditor ate = new HashValueAutoEditor(typesForEvent(AUTO_TEXT));
				ate.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(ate));
			}
			else
			{
				RandomAutoEditor ate = new RandomAutoEditor(typesForEvent(AUTO_TEXT));
				ate.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addEvent (addDosAndDonts(ate));			
			}
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
			rs.setEventWhenNoId(true);
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

		if (isRequiredInput(FOCUS)) {
			if (Resources.TEXT_VALUES_FROM_DICTIONARY)
			{
				DictionaryFocusWriter f = new DictionaryFocusWriter(typesForInput(FOCUS));
				f.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addInput (addDosAndDonts(f));
			}
			else if(Resources.TEXT_VALUES_ID_HASH)
			{
				HashFocusWriter f = new HashFocusWriter(typesForInput(FOCUS));
				f.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addInput (addDosAndDonts(f));
			}			
			else
			{
				RandomFocusWriter f = new RandomFocusWriter(typesForInput(FOCUS));				
				f.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addInput (addDosAndDonts(f));
			}
		}
		
		if (isRequiredInput(SET_BAR)) {
			Slider sl = new Slider (typesForInput(SET_BAR));
			sl.setEventWhenNoId(false);
			u.addInput (addDosAndDonts(sl));
		}

		if (isRequiredInput(TYPE_TEXT)) {
			if (Resources.TEXT_VALUES_FROM_DICTIONARY)
			{
				DictionaryValueEditor re = new DictionaryValueEditor(typesForInput(TYPE_TEXT));
				re.setEventWhenNoId(false);
				u.addInput (addDosAndDonts(re));	
			}
			else if(Resources.TEXT_VALUES_ID_HASH)
			{
				HashValueEditor re = new HashValueEditor(typesForInput(TYPE_TEXT));
				re.setEventWhenNoId(false);
				u.addInput (addDosAndDonts(re));
			}
			else
			{
				RandomEditor re = new RandomEditor(typesForInput(TYPE_TEXT));
				re.setEventWhenNoId(false);
				u.addInput (addDosAndDonts(re));			
			}
		}

		if (isRequiredInput(WRITE_TEXT)) {
			if (Resources.TEXT_VALUES_FROM_DICTIONARY)
			{
				DictionaryValueWriter re = new DictionaryValueWriter(typesForInput(WRITE_TEXT));
				re.setEventWhenNoId(false);
				u.addInput (addDosAndDonts(re));
			}
			else if(Resources.TEXT_VALUES_ID_HASH)
			{
				HashValueWriter re = new HashValueWriter(typesForInput(WRITE_TEXT));
				re.setEventWhenNoId(false);
				u.addInput (addDosAndDonts(re));
			}			
			else
			{
				RandomWriter re = new RandomWriter(typesForInput(WRITE_TEXT));				
				re.setEventWhenNoId(false);
				u.addInput (addDosAndDonts(re));
			}
		}
		
		if (isRequiredInput(SEARCH_TEXT)) {
			if (Resources.TEXT_VALUES_FROM_DICTIONARY)
			{
				DictionaryValueSearchWriter sti = new DictionaryValueSearchWriter(typesForInput(SEARCH_TEXT));
				sti.setEventWhenNoId(false);
				u.addInput (addDosAndDonts(sti));
			}
			else if(Resources.TEXT_VALUES_ID_HASH)
			{
				HashValueSearchWriter sti = new HashValueSearchWriter(typesForInput(SEARCH_TEXT));
				sti.setEventWhenNoId(false);
				u.addInput (addDosAndDonts(sti));
			}			
			else
			{
				RandomSearchWriter sti = new RandomSearchWriter(typesForInput(SEARCH_TEXT));				
				sti.setEventWhenNoId(false);
				u.addInput (addDosAndDonts(sti));
			}
		}
		
		if (isRequiredInput(AUTO_TEXT)) {
			if (Resources.TEXT_VALUES_FROM_DICTIONARY)
			{
				DictionaryValueAutoEditor ate = new DictionaryValueAutoEditor(typesForInput(AUTO_TEXT));
				ate.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addInput (addDosAndDonts(ate));	
			}
			else if(Resources.TEXT_VALUES_ID_HASH)
			{
				HashValueAutoEditor ate = new HashValueAutoEditor(typesForInput(AUTO_TEXT));
				ate.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addInput (addDosAndDonts(ate));
			}
			else
			{
				RandomAutoEditor ate = new RandomAutoEditor(typesForInput(AUTO_TEXT));
				ate.setEventWhenNoId(Resources.EVENT_WHEN_NO_ID);
				u.addInput (addDosAndDonts(ate));			
			}
		}
		
		
		if (isRequiredInput(SPINNER_SELECT)) {
			RandomSpinnerSelector rss = new RandomSpinnerSelector(typesForInput(SPINNER_SELECT));
			rss.setEventWhenNoId(false);
			u.addInput(addDosAndDonts(rss));
		}

		if (isRequiredInput(RADIO_SELECT)) {
			RandomRadioSelector rrs = new RandomRadioSelector(typesForInput(RADIO_SELECT));
			rrs.setEventWhenNoId(true);
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