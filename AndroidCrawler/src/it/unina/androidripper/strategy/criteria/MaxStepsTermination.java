package it.unina.androidripper.strategy.criteria;

import static it.unina.androidripper.Resources.TAG;
import it.unina.androidripper.model.SaveStateListener;
import it.unina.androidripper.model.SessionParams;
import it.unina.androidripper.model.Strategy;
import it.unina.androidripper.storage.PersistenceFactory;

import android.util.Log;

public class MaxStepsTermination implements TerminationCriteria, SaveStateListener {
	
	private static final String ACTOR_NAME = "MaxStepsTermination";
	private static final String PARAM_NAME = "current";
	private int max;
	private int current;

	public MaxStepsTermination () {
		this(1000);
	}

	public MaxStepsTermination (int maxSteps) {
		this.max=maxSteps;
		reset();
		PersistenceFactory.registerForSavingState(this);
	}

	public boolean termination () {
		this.current--;
		Log.i(TAG, "Check for termination: " + current + " steps left of " + max);
		return (this.current==0);
	}
	
	public void reset() {
		this.current=max;
	}
	
	public void setStrategy(Strategy theStrategy) {}

	public SessionParams onSavingState() {
		return new SessionParams(PARAM_NAME, this.current);
	}

	public void onLoadingState(SessionParams sessionParams) {
		this.current = sessionParams.getInt(PARAM_NAME);
		Log.d(TAG, "Current step countdown restored to " + this.current);
	}

	public String getListenerName() {
		return ACTOR_NAME;
	}
	
}