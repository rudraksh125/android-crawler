package com.nofatclips.crawler.strategy.comparator;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.crawler.model.StatelessComparator;

public class NullComparator implements StatelessComparator {

	@Override
	public boolean compare(ActivityState a, ActivityState b) {
		return false;
	}

}
