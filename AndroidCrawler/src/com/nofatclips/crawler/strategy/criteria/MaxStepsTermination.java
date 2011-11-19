package com.nofatclips.crawler.strategy.criteria;

import com.nofatclips.crawler.model.SaveStateListener;
import com.nofatclips.crawler.model.SessionParams;
import com.nofatclips.crawler.model.Strategy;
import com.nofatclips.crawler.storage.PersistenceFactory;

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

	@Override
	public boolean termination () {
		this.current--;
		Log.i("nofatclips", "Check for termination: " + current + " steps left of " + max);
		return (this.current==0);
	}
	
	public void reset() {
		this.current=max;
	}
	
	@Override
	public void setStrategy(Strategy theStrategy) {}

	@Override
	public SessionParams onSavingState() {
		return new SessionParams(PARAM_NAME, this.current);
	}

	@Override
	public void onLoadingState(SessionParams sessionParams) {
		this.current = sessionParams.getInt(PARAM_NAME);
		Log.d("nofatclips", "Current step count restored to " + this.current);
	}

	@Override
	public String getListenerName() {
		return ACTOR_NAME;
	}
	
}