package com.nofatclips.crawler.model;

import com.nofatclips.androidtesting.model.ActivityState;

public interface Strategy {
	
	public void addState (ActivityState newActivity);
	public boolean compareState (ActivityState theActivity);
	public Comparator getComparator ();
	public void setComparator (Comparator c);

}
