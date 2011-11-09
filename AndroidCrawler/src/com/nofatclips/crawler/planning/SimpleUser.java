package com.nofatclips.crawler.planning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import android.util.Log;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.model.EventHandler;
import com.nofatclips.crawler.model.InputHandler;
import com.nofatclips.crawler.planning.interactors.Clicker;
import com.nofatclips.crawler.planning.interactors.ListLongClicker;
import com.nofatclips.crawler.planning.interactors.ListSelector;
import com.nofatclips.crawler.planning.interactors.LongClicker;

import static com.nofatclips.crawler.Resources.*;
import static com.nofatclips.androidtesting.model.InteractionType.*;
import static com.nofatclips.androidtesting.model.SimpleType.*;

public class SimpleUser implements EventHandler, InputHandler {

	public SimpleUser () {
		this.randomGenerator = new Random();
	}
	
	public SimpleUser (Abstractor a) {
		this (a, new Clicker (a, BUTTON));
	}
	
	public SimpleUser (Abstractor a, Clicker c) {
		this();
		setAbstractor(a);
		setEventClicker(c);
		setEventLongClicker (new LongClicker (a, BUTTON));
		setListSelector (new ListSelector (a, MAX_EVENTS_PER_WIDGET, LIST_VIEW));
		setListLongClicker (new ListLongClicker (a, MAX_EVENTS_PER_WIDGET, LIST_VIEW));
	}
	
	@Override
	public Collection<UserEvent> handleEvent(WidgetState w) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		
		// Code to handle ListViews
		events.addAll(getListSelector().getEvents(w));
		if (longClickListEvent()) {
			events.addAll(getListLongClicker().getEvents(w));
		}

//		if (w.getSimpleType().equals(LIST_VIEW)) {
//			int fromItem = 1; // int fromItem = Math.min(6,w.getCount());
//			int toItem = Math.min (fromItem + MAX_EVENTS_PER_WIDGET - 1, w.getCount());
//			
//			Log.d("nofatclips", "Handling events [" + fromItem + "," + toItem + "] on ListView id=" + w.getId() + " count=" + w.getCount() + " name=" + w.getName());
//			UserEvent event;
//			for (int i=fromItem; i<=toItem; i++) {
//				event = getAbstractor().createEvent(w, LIST_SELECT);
//				event.setValue(String.valueOf(i));
//				events.add(event);
//				if (longClickListEvent()) {
//					event = getAbstractor().createEvent(w, LIST_LONG_SELECT);
//					event.setValue(String.valueOf(i));
//					events.add(event);
//				}
//			}
//			return events;
//		}
		
		// Return empty if don't know how to click
//		if (!useForClick(w)) return events;
		if ( (w.getId().equals("-1"))  && (!eventWhenNoId() || (w.getName().equals("")) )) return events;

		// Plan a click on this widget
		events.addAll (getEventClicker().getEvents(w));
//		Log.d("nofatclips", "Handling event on widget id=" + w.getId() + " type=" + w.getSimpleType() + " name=" + w.getName());
//		UserEvent event = getAbstractor().createEvent(w, CLICK);
//		events.add(event);
		
		if (longClickEvent()) {
			events.addAll(getEventLongClicker().getEvents(w));
//			event = getAbstractor().createEvent(w, LONG_CLICK);
//			events.add(event);
		}
		return events;
	}
	
	public boolean eventWhenNoId () {
		return EVENT_WHEN_NO_ID;
	}
	
	public boolean longClickEvent () {
		return LONG_CLICK_EVENT;
	}

	public boolean longClickListEvent () {
		return LONG_CLICK_LIST_EVENT;
	}

	@Override
	public UserInput handleInput(WidgetState w) {
		if (!useForInput(w)) return null;
		UserInput input = null;
		if (w.getSimpleType().equals(CHECKBOX) && w.isClickable()) {
//			if (randomGenerator.nextBoolean()) return null;
			input = getAbstractor().createInput(w, "", CLICK);
		} else if (w.getSimpleType().equals(RADIO) && w.isClickable()) {
			input = getAbstractor().createInput(w, "", CLICK);
		} else if (w.getSimpleType().equals(SEEK_BAR)) {
			int randomInt = randomGenerator.nextInt(w.getCount());
			input = getAbstractor().createInput(w, String.valueOf(randomInt), SET_BAR);
		} else if (w.getSimpleType().equals(EDIT_TEXT)) {
			int randomInt = randomGenerator.nextInt(this.upperLimit-this.lowerLimit) + this.lowerLimit;  
			input = getAbstractor().createInput(w, String.valueOf(randomInt), TYPE_TEXT);
		} else {
			return null;
		}
		Log.d("nofatclips", "Handling input on widget id=" + w.getId() + " type=" + w.getSimpleType());
		return input;
	}

	protected boolean useForClick (WidgetState w) {
//		if (!(w.isAvailable() && w.isClickable())) return false;
//		return (w.getSimpleType().equals(BUTTON));
		return eventClicker.canUseWidget(w);
	}

	protected boolean useForInput (WidgetState w) {
//		if (useForClick(w)) return false;
//		if (!w.isAvailable()) return false;
//		return ( (w.getSimpleType().equals(EDIT_TEXT) || w.getSimpleType().equals(RADIO) || w.getSimpleType().equals(CHECKBOX) || w.getSimpleType().equals(SEEK_BAR)) && !w.getId().equals("-1"));
		return (!useForClick(w) && w.isAvailable() && !w.getId().equals("-1"));
	}
	
	public Abstractor getAbstractor() {
		return this.abs;
	}

	public void setAbstractor(Abstractor abs) {
		this.abs = abs;
	}

	public Random getRandomGenerator() {
		return this.randomGenerator;
	}

	public void setRandomGenerator(Random randomGenerator) {
		this.randomGenerator = randomGenerator;
	}
	
	public void setLimits (int lower, int upper) {
		this.lowerLimit = lower;
		this.upperLimit = upper;
	}
	
	public void setEventClicker (InteractorAdapter c) {
		this.eventClicker = c;
	}
	
	public InteractorAdapter getEventClicker () {
		return this.eventClicker;
	}
	
	public void setEventLongClicker (InteractorAdapter c) {
		this.eventLongClicker = c;
	}
	
	public InteractorAdapter getEventLongClicker () {
		return this.eventLongClicker;
	}
	
	public void setListSelector (InteractorAdapter c) {
		this.listSelector = c;
	}
	
	public InteractorAdapter getListSelector () {
		return this.listSelector;
	}

	public void setListLongClicker (InteractorAdapter c) {
		this.listLongClicker = c;
	}
	
	public InteractorAdapter getListLongClicker () {
		return this.listLongClicker;
	}

	private Abstractor abs;
	private Random randomGenerator;
	private int lowerLimit = 0;
	private int upperLimit = 100;
	private InteractorAdapter eventClicker;
	private InteractorAdapter eventLongClicker;
	private InteractorAdapter listSelector;
	private InteractorAdapter listLongClicker;

}
