package com.nofatclips.crawler.guitree;

import com.nofatclips.crawler.model.MemorylessEngine;

// A Random Engine that doesn't even restart from the breaking point when crashing or exiting the application

public class NomadEngine extends RandomEngine implements MemorylessEngine {

	@Override
	protected void setupAfterResume() {
		planFirstTests(getAbstractor().getBaseActivity());
	}
	
	@Override
	public void addMoreCriteria() { /* do nothing */ }

}
