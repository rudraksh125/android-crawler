package com.nofatclips.crawler.planning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.util.Log;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.crawler.model.Abstractor;

public abstract class InteractorAdapter {
	
	protected HashSet<String> widgetClasses = new HashSet<String>();
	private Abstractor theAbstractor;

	public InteractorAdapter (String ... simpleTypes) {
		for (String s: simpleTypes) {
			this.widgetClasses.add(s);
		}
	}
	
	public InteractorAdapter (Abstractor theAbstractor, String ... simpleTypes) {
		this (simpleTypes);
		this.theAbstractor = theAbstractor;
	}
	
	public boolean canUseWidget (WidgetState w) {
		return (w.isAvailable() && matchClass(w.getSimpleType()));
	}
	
	public List<UserEvent> getEvents (WidgetState w) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(w)) {
			Log.d("nofatclips", "Handling event '" + getEventType() + "' on widget id=" + w.getId() + " type=" + w.getSimpleType() + " name=" + w.getName());
			events.add(generateEvent(w));
		}
		return events;
	}
	
	public List<UserEvent> getEvents (WidgetState w, String value) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(w)) {
			Log.d("nofatclips", "Handling event '" + getEventType() + "' on widget id=" + w.getId() + "value=" + value + " type=" + w.getSimpleType() + " name=" + w.getName());
			events.add(generateEvent(w, value));
		}
		return events;
	}

	protected UserEvent generateEvent (WidgetState w) {
		return getAbstractor().createEvent(w, getEventType());
	}

	protected UserEvent generateEvent (WidgetState w, String value) {
		UserEvent event = generateEvent(w);
		event.setValue(value);
		return event;
	}

	public Abstractor getAbstractor() {
		return this.theAbstractor;
	}
	
	public abstract String getEventType ();
	
	protected boolean matchClass (String type) {
		for (String storedType: this.widgetClasses) {
			if (storedType.equals(type)) return true;
		}
		return false;
	}

}
