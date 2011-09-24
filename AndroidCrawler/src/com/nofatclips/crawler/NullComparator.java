package com.nofatclips.crawler;

import com.nofatclips.androidtesting.model.ActivityState;

public class NullComparator implements Comparator {

	@Override
	public boolean compare(ActivityState a, ActivityState b) {
		return false;
	}

}
