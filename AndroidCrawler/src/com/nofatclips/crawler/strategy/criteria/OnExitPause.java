package com.nofatclips.crawler.strategy.criteria;

import com.nofatclips.crawler.model.Strategy;

public class OnExitPause implements PauseCriteria {

	private Strategy theStrategy;

	public void setStrategy(Strategy theStrategy) {
		this.theStrategy = theStrategy;	
	}

	public boolean pause() {
		return this.theStrategy.getStateAfterEvent().isExit();
	}

}
