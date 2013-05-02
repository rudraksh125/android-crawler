package it.unina.androidripper.model;

import com.nofatclips.androidtesting.model.*;

public interface AbstractorListener {
	
	public void onNewActivity (ActivityState a);
	public void onNewEvent (UserEvent e);
	public void onNewInput (UserInput i);

}
