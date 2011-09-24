package com.nofatclips.crawler;

import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.model.UserInput;

public class TraceProcessor implements TaskProcessor {
	
	private Robot robot;
	
	public TraceProcessor () {
	}
	
	public TraceProcessor (Robot r) {
		setRobot (r);
	}
	
	@Override
	public void execute (Trace trace) {
		for (Transition t: trace) {
			execute (t);
		}
	}

	private void execute(Transition t) {
		for (UserInput i: t) {
			robot.setInput(i);
		}
		robot.fireEvent(t.getEvent());
	}
	
	public void setRobot (Robot r) {
		this.robot = r;
	}

}
