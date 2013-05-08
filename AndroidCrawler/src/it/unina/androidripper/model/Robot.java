package it.unina.androidripper.model;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;

public interface Robot {

	@SuppressWarnings("rawtypes")
	public void bind (ActivityInstrumentationTestCase2 engine);
	public void bindInstrumentationTestCase (InstrumentationTestCase engine, Activity activity);
	public void fireEvent (UserEvent e);
	public void setInput (UserInput i);
	public void process (Trace t);
	public void process (Transition tr);
	public void finalize();
	public void swapTab(String tab);
	public void wait (int milli);
	
}
