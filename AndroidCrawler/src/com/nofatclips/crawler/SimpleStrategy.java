package com.nofatclips.crawler;

import java.util.HashSet;

import android.util.Log;

import com.nofatclips.androidtesting.model.ActivityState;

public class SimpleStrategy implements Strategy {
	
	public SimpleStrategy () {
		super();
	}

	public SimpleStrategy (Comparator c) {
		super();
		setComparator(c);
	}
	
	@Override
	public void addState(ActivityState newActivity) {
		this.guiNodes.add(newActivity);
	}

	@Override
	public boolean compareState(ActivityState theActivity) {
		String name = theActivity.getName();
		Log.i("nofatclips", "Checking strategy for activity " + name);
		for (ActivityState stored: guiNodes) {
			Log.d("nofatclips", "Comparing against activity " + stored.getName());
			if (getComparator().compare(theActivity, stored)) {
				return true;
			}
		}
		Log.i("nofatclips", "Registering activity " + name + " as a new found state");
		addState (theActivity);
		return false;
	}
	
	@Override
	public Comparator getComparator() {
		return this.c;
	}

	@Override
	public void setComparator(Comparator c) {
		this.c = c;
	}

	private HashSet<ActivityState> guiNodes = new HashSet<ActivityState> ();
	private Comparator c;

}
