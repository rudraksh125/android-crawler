package com.nofatclips.crawler.planning;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nofatclips.androidtesting.model.*;
import com.nofatclips.crawler.model.*;
import com.nofatclips.crawler.planning.adapters.RandomInteractor;

public class SimpleUser implements UserAdapter {

	public SimpleUser () {
		this(new Random());
	}
	
	public SimpleUser (Abstractor a) {
		this();
		setAbstractor(a);
	}
	
	public SimpleUser (Random r) {
		this.eventTypes = new ArrayList<Interactor>();
		this.inputTypes = new ArrayList<Interactor>();		
		setRandomGenerator(r);		
	}
	
	public SimpleUser (Abstractor a, Random r) {
		this (r);
		setAbstractor(a);
	}
	
	public List<UserEvent> handleEvent(WidgetState w) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		for (Interactor eventAdapter: getEventTypes()) {
			events.addAll(eventAdapter.getEvents(w));
		}
		return events;
	}
	
	public List<UserInput> handleInput(WidgetState w) {
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		for (Interactor inputAdapter: getInputTypes()) {
			inputs.addAll(inputAdapter.getInputs(w));
		}
		return inputs;
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
	
	public void addEvent (Interactor ... events) {
		for (Interactor e: events) {
			e.setAbstractor(getAbstractor());
			eventTypes.add(e);
		}
	}
	
	public Iterable<Interactor> getEventTypes () {
		return this.eventTypes;
	}

	public void addInput (Interactor ... inputs) {
		for (Interactor i: inputs) {
			i.setAbstractor(getAbstractor());
			if (i instanceof RandomInteractor) {
				((RandomInteractor) i).setRandomGenerator(getRandomGenerator());
			}
			inputTypes.add(i);
		}
	}
	
	public Iterable<Interactor> getInputTypes () {
		return this.inputTypes;
	}

	private Abstractor abs;
	private Random randomGenerator;
	private List<Interactor> eventTypes;
	private List<Interactor> inputTypes;

}
