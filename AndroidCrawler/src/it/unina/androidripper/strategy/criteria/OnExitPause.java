package it.unina.androidripper.strategy.criteria;

import it.unina.androidripper.model.Strategy;

public class OnExitPause implements PauseCriteria {

	private Strategy theStrategy;

	public void setStrategy(Strategy theStrategy) {
		this.theStrategy = theStrategy;	
	}

	public boolean pause() {
		return this.theStrategy.getStateAfterEvent().isExit();
	}

}
