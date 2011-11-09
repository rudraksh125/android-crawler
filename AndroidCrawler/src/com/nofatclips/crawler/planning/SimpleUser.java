package com.nofatclips.crawler.planning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import android.util.Log;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.model.UserAdapter;

import static com.nofatclips.crawler.Resources.*;
import static com.nofatclips.androidtesting.model.InteractionType.*;
import static com.nofatclips.androidtesting.model.SimpleType.*;

public class SimpleUser implements UserAdapter {

	public SimpleUser () {
		this.randomGenerator = new Random();
	}
	
//	public SimpleUser (Abstractor a) {
//		this (a, new Clicker (a, BUTTON));
//	}
	
	public SimpleUser (Abstractor a) {
		this();
		setAbstractor(a);
//		setEventClicker(c);
//		setEventLongClicker (new LongClicker (a, BUTTON));
//		setListSelector (new ListSelector (a, MAX_EVENTS_PER_WIDGET));
//		setListLongClicker (new ListLongClicker (a, MAX_EVENTS_PER_WIDGET));
	}
	
	@Override
	public Collection<UserEvent> handleEvent(WidgetState w) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		
		// Code to handle ListViews
		events.addAll(getListSelector().getEvents(w));
		if (getListLongClicker() != null) {
			events.addAll(getListLongClicker().getEvents(w));
		}

		// Return empty if don't know how to click
//		if (!useForClick(w)) return events;
		if ( (w.getId().equals("-1"))  && (!eventWhenNoId() || (w.getName().equals("")) )) return events;

		// Plan a click on this widget
		events.addAll (getEventClicker().getEvents(w));
//		UserEvent event = getAbstractor().createEvent(w, CLICK);
//		events.add(event);
		
		if (getEventLongClicker() != null) {
			events.addAll(getEventLongClicker().getEvents(w));
//			event = getAbstractor().createEvent(w, LONG_CLICK);
//			events.add(event);
		}
		return events;
	}
	
	public boolean eventWhenNoId () {
		return EVENT_WHEN_NO_ID;
	}
	
//	public boolean longClickEvent () {
//		return LONG_CLICK_EVENT;
//	}
//
//	public boolean longClickListEvent () {
//		return LONG_CLICK_LIST_EVENT;
//	}

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

//	protected boolean useForClick (WidgetState w) {
//		return eventClicker.canUseWidget(w);
//	}

	protected boolean useForInput (WidgetState w) {
		return (w.isAvailable() && !w.getId().equals("-1"));
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
	private InteractorAdapter eventClicker = null;
	private InteractorAdapter eventLongClicker = null;
	private InteractorAdapter listSelector = null;
	private InteractorAdapter listLongClicker = null;

}
