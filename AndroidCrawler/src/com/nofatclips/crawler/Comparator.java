package com.nofatclips.crawler;

import com.nofatclips.androidtesting.model.ActivityState;

public interface Comparator {

	public boolean compare (ActivityState a, ActivityState b);
	
}
