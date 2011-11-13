package com.nofatclips.crawler.planning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.model.UserAdapter;

//import static com.nofatclips.crawler.Resources.*;
import static com.nofatclips.androidtesting.model.InteractionType.*;
import static com.nofatclips.androidtesting.model.SimpleType.*;

public class SimpleUser implements UserAdapter {

	public SimpleUser () {
		this.randomGenerator = new Random();
		this.eventTypes = new ArrayList<InteractorAdapter>();
	}
	
//	public SimpleUser (Abstractor a) {
//		this (a, new Clicker (a, BUTTON));
//	}
	
	public SimpleUser (Abstractor a) {
		this();
		setAbstractor(a);
	}
	
	@Override
	public Collection<UserEvent> handleEvent(WidgetState w) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		for (InteractorAdapter eventAdapter: getEventTypes()) {
			events.addAll(eventAdapter.getEvents(w));
		}
		return events;
	}
	
	@Override
	public UserInput handleInput(WidgetState w) {
		if (!useForInput(w)) return null;
		UserInput input = null;
		if ( (w.getSimpleType().equals(CHECKBOX) || w.getSimpleType().equals(TOGGLE_BUTTON)) && w.isClickable()) {
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
	
	public void addEvent (InteractorAdapter e) {
		eventTypes.add(e);
	}
	
	public Iterable<InteractorAdapter> getEventTypes () {
		return this.eventTypes;
	}

	private Abstractor abs;
	private Random randomGenerator;
	private int lowerLimit = 0;
	private int upperLimit = 100;
	private List<InteractorAdapter> eventTypes;

}
