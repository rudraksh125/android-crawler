package com.nofatclips.crawler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import android.util.Log;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;

public class SimpleUser implements EventHandler, InputHandler {

	public SimpleUser () {
		super();
		this.randomGenerator = new Random();
	}
	
	public SimpleUser (Abstractor a) {
		this();
		setAbstractor(a);
	}
	
	@Override
	public Collection<UserEvent> handleEvent(WidgetState w) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (!useForEvent(w)) return events;
		if (w.getId().equals("-1")) return events;
		Log.d("nofatclips", "Handling event on widget id=" + w.getId() + " type=" + w.getSimpleType());
		UserEvent event = getAbstractor().createEvent(w, "click");
		events.add(event);
		return events;
	}
	
	@Override
	public UserInput handleInput(WidgetState w) {
		if (!useForInput(w)) return null;
		Log.d("nofatclips", "Handling input on widget id=" + w.getId() + " type=" + w.getSimpleType());
		if (w.getSimpleType().equals("check")) {
//			if (randomGenerator.nextBoolean()) return null;
			UserInput input = getAbstractor().createInput(w, "", "click");
			return input;			
		} else if (w.getSimpleType().equals("editText")) {
			int randomInt = randomGenerator.nextInt(this.upperLimit-this.lowerLimit) + this.lowerLimit;  
			UserInput input = getAbstractor().createInput(w, String.valueOf(randomInt));
			return input;
		}
		return null;
	}

	protected boolean useForEvent (WidgetState w) {
		return (w.getSimpleType().equals("button"));
	}

	protected boolean useForInput (WidgetState w) {
		if (useForEvent(w)) return false;
		return ( (w.getSimpleType().equals("editText") || w.getSimpleType().equals("radio") || w.getSimpleType().equals("check")) && !w.getId().equals("-1"));
	}
	
//	protected boolean useForSwapTabs (WidgetState w) {
//		return w.getSimpleType().equals("tabHost");
//	}

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
	
	private Abstractor abs;
	private Random randomGenerator;
	private int lowerLimit = 0;
	private int upperLimit = 100;

}
