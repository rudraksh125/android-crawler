package com.nofatclips.crawler.strategy.comparator;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.crawler.model.Comparator;
import static com.nofatclips.androidtesting.model.SimpleType.NULL;

public class NameComparator implements Comparator {

	@Override
	public boolean compare(ActivityState a, ActivityState b) {
		return a.getName().equals(b.getName());
	}
	
	public String describe() {
		return NULL;
	}

}
