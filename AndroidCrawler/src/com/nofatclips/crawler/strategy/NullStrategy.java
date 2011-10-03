package com.nofatclips.crawler.strategy;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.crawler.model.Comparator;
import com.nofatclips.crawler.model.Strategy;
import com.nofatclips.crawler.model.TerminationCriteria;
import com.nofatclips.crawler.model.TransitionCriteria;

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

	@Override
	public boolean checkForTermination(ActivityState theActivity) {
		return false;
	}

	@Override
	public void addTerminationCriteria(TerminationCriteria t) {
		return;
	}

	@Override
	public boolean checkForTransition(ActivityState theActivity) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void addTransitionCriteria(TransitionCriteria theCriteria) {
		// TODO Auto-generated method stub
		return;
	}

}
