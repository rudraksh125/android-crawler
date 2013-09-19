package it.unina.androidripper.strategy.criteria;

import static it.unina.androidripper.Resources.TAG;
import it.unina.androidripper.model.Strategy;
import android.util.Log;

public class NewActivityTransition implements TransitionCriteria {

	private Strategy theStrategy;

	public void setStrategy(Strategy theStrategy) {
		this.theStrategy = theStrategy;
	}

	public boolean transition() {
		Log.i (TAG, "Checking for Transition: from " + theStrategy.getStateBeforeEvent().getName() + "(" + theStrategy.getStateBeforeEvent().getId() + ") to " + theStrategy.getStateAfterEvent().getName() + "(" + theStrategy.getStateAfterEvent().getId() + ")");
		return (theStrategy.getStateBeforeEvent().getId() != theStrategy.getStateAfterEvent().getId());
	}

}
