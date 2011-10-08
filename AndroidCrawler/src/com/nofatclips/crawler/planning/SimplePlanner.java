package com.nofatclips.crawler.planning;

import java.util.Collection;
import java.util.HashSet;

import android.util.Log;

import com.nofatclips.androidtesting.model.*;
import com.nofatclips.crawler.model.*;

import static com.nofatclips.crawler.Resources.*;

public class SimplePlanner implements Planner {

	@Override
	public Plan getPlanForActivity (ActivityState a, int numberOfTabs) {
		return getPlanForActivity(a, numberOfTabs, true);
	}

	@Override
	public Plan getPlanForBaseActivity (ActivityState a, int numberOfTabs) {
		return getPlanForActivity(a, numberOfTabs, false);
	}

	public Plan getPlanForActivity (ActivityState a, int numberOfTabs, boolean allowGoBack) {
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

		UserEvent evt;
		Transition t;
		
		// Special handling for pressing back button
		if (BACK_BUTTON_EVENT && allowGoBack) {
			evt = getAbstractor().createEvent(null, "back");
			t = getAbstractor().createStep(a, new HashSet<UserInput>(), evt);
			Log.i("nofatclips", "Created trace to press the back button");
			p.addTask(t);
		}

		// Special handling for tab switch
		if (tabs!=null && numberOfTabs>1) {
			int tabNum = 2;
			do {
				evt = getAbstractor().createEvent(tabs, "swapTab");
				evt.setValue(String.valueOf(tabNum));
				t = getAbstractor().createStep(a, new HashSet<UserInput>(), evt);
				Log.i("nofatclips", "Created trace to explore tab #" + tabNum);
				p.addTask(t);
				tabNum++;
			} while (tabNum<=numberOfTabs);
		}

		// Special handling for scrolling down
		if (SCROLL_DOWN_EVENT) {
			evt = getAbstractor().createEvent(null, "scrollDown");
			t = getAbstractor().createStep(a, new HashSet<UserInput>(), evt);
			Log.i("nofatclips", "Created trace to perform scrolling down");
			p.addTask(t);
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
