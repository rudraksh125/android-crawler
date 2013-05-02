package it.unina.androidripper.strategy.criteria;

import it.unina.androidripper.model.Strategy;

public class NewActivityExplore implements ExplorationCriteria {

	private Strategy theStrategy;

	public void setStrategy(Strategy theStrategy) {
		this.theStrategy = theStrategy;
	}

	public boolean exploration() {
		return !theStrategy.isLastComparationPositive();
	}

}
