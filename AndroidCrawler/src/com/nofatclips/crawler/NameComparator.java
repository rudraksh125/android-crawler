package com.nofatclips.crawler;

import com.nofatclips.androidtesting.model.ActivityState;

public class NameComparator implements Comparator {

	@Override
	public boolean compare(ActivityState a, ActivityState b) {
		return a.getName().equals(b.getName());
	}

}
