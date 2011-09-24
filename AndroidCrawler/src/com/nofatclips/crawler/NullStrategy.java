package com.nofatclips.crawler;

import com.nofatclips.androidtesting.model.ActivityState;

public class NullStrategy implements Strategy {

	@Override
	public void addState(ActivityState newActivity) {
		return; //do nothing
	}

	@Override
	public boolean compareState(ActivityState theActivity) {
		return false; // Always return no match
	}

	@Override
	public Comparator getComparator() {
		return null;
	}

	@Override
	public void setComparator(Comparator c) {
		return;
	}

}
