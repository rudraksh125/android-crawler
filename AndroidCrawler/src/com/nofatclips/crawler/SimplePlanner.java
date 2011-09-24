package com.nofatclips.crawler;

import java.util.Collection;
import java.util.HashSet;

import android.util.Log;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;

public class SimplePlanner implements Planner {

	@Override
	public Plan getPlanForActivity (ActivityState a, int numberOfTabs) {
		Plan p = new Plan();
		Log.i("nofatclips", "Planning for new Activity " + a.getName());
		WidgetState tabs = null;
		for (WidgetState w: getEventFilter()) {
			if (w.getSimpleType().equals("tabHost"))
				tabs = w;
			Collection<UserEvent> events = getUser().handleEvent(w);
			for (UserEvent evt: events) {
				if (evt == null) continue;
				Collection<UserInput> inputs = new HashSet<UserInput>();
				for (WidgetState formWidget: getInputFilter()) {
					UserInput inp = getFormFiller().handleInput(formWidget);
					if (inp != null) {
						inputs.add(inp);
					}
				}
				Transition t = getAbstractor().createStep(a, inputs, evt);
				p.addTask(t);
			}
		}
		if (tabs!=null && numberOfTabs>1) {
			int tabNum = 2;
			do {
				UserEvent evt = getAbstractor().createEvent(tabs, "swapTab");
				evt.setValue(String.valueOf(tabNum));
				Transition t = getAbstractor().createStep(a, new HashSet<UserInput>(), evt);
				Log.i("nofatclips", "Creating trace to explore tab #" + tabNum);
				p.addTask(t);
				tabNum++;
			} while (tabNum<=numberOfTabs);
		}
		return p;
	}
	
	@Override
	public Plan getPlanForActivity(ActivityState a) {
		return getPlanForActivity (a,1);
	}
	
	public Filter getEventFilter() {
		return this.eventFilter;
	}

	public void setEventFilter(Filter eventFilter) {
		this.eventFilter = eventFilter;
	}
	
	public Filter getInputFilter() {
		return this.inputFilter;
	}
	
	public void setInputFilter(Filter inputFilter) {
		this.inputFilter = inputFilter;
	}

	public EventHandler getUser() {
		return this.user;
	}

	public void setUser(EventHandler user) {
		this.user = user;
	}

	public InputHandler getFormFiller() {
		return this.formFiller;
	}

	public void setFormFiller(InputHandler formFiller) {
		this.formFiller = formFiller;
	}

	public Abstractor getAbstractor() {
		return this.abstractor;
	}

	public void setAbstractor(Abstractor abstractor) {
		this.abstractor = abstractor;
	}

	private Filter eventFilter;
	private Filter inputFilter;
	private EventHandler user;
	private InputHandler formFiller;
	private Abstractor abstractor;

}
