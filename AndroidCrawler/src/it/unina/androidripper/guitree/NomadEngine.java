package it.unina.androidripper.guitree;

import it.unina.androidripper.model.MemorylessEngine;
import it.unina.androidripper.strategy.Resources;
import it.unina.androidripper.strategy.criteria.MaxDepthPause;

// A Random Engine that doesn't even restart from the breaking point when crashing or exiting the application

public class NomadEngine extends RandomEngine implements MemorylessEngine {

	@Override
	protected void setupAfterResume() {
		planFirstTests(getAbstractor().getBaseActivity());
	}
	
	@Override
	// When Max Depth is reached, pause
	public void addMoreCriteria() {
		if (Resources.TRACE_MAX_DEPTH > 0) {
			this.theStrategyFactory.setMoreCriterias(new MaxDepthPause(Resources.TRACE_MAX_DEPTH));
		}
	}

}
