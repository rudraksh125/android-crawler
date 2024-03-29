package it.unina.androidripper.strategy;

import it.unina.androidripper.model.Comparator;
import it.unina.androidripper.model.Strategy;
import it.unina.androidripper.model.StrategyCriteria;
import it.unina.androidripper.strategy.criteria.AfterEventDontExplore;
import it.unina.androidripper.strategy.criteria.AfterWidgetDontExplore;
import it.unina.androidripper.strategy.criteria.MaxDepthExplore;
import it.unina.androidripper.strategy.criteria.MaxStepsPause;
import it.unina.androidripper.strategy.criteria.MaxStepsTermination;
import it.unina.androidripper.strategy.criteria.NewActivityExplore;
import it.unina.androidripper.strategy.criteria.NewActivityTransition;
import it.unina.androidripper.strategy.criteria.TimeElapsedPause;
import it.unina.androidripper.strategy.criteria.TimeElapsedTermination;

import java.util.ArrayList;

public class StrategyFactory {

	private Comparator comparator;
	private int maxTraces = 0;
	private int maxTransitions = 0;
	private int minTransitions = 0;
	private long maxSeconds = 0;
	private long pauseSeconds = 0;
	private boolean checkTransistions = false;
	private boolean exploreNewStatesOnly = true;
	private ArrayList<StrategyCriteria> otherCriterias = new ArrayList<StrategyCriteria>();
	private int pauseTraces;
	private String[] stopEvents;
	private int[] stopWidgets;
	
	public StrategyFactory () {}
	
	public StrategyFactory (Comparator c) {
		setComparator (c);
	}

	public StrategyFactory (Comparator c, StrategyCriteria ... criterias) {
		this (c);
		setMoreCriterias(criterias);
	}

	public void setComparator(Comparator c) {
		this.comparator = c;
	}

	public Strategy getStrategy () {
		if (useCustomStrategy ()) {
			
			StrategyCriteria[] c = new StrategyCriteria[this.otherCriterias.size()];
			CustomStrategy s = new CustomStrategy(this.comparator, this.otherCriterias.toArray(c));
			if (exploreNewStatesOnly()) {
				s.addCriteria (new NewActivityExplore());
			}
			if (checkMaxTraces()) {
				s.addCriteria(new MaxStepsTermination(this.maxTraces));
			}
			if (checkForDepth()) {
				s.addCriteria(new MaxDepthExplore(this.maxTransitions));
			}
			if (checkTransition()) {
				s.addCriteria(new NewActivityTransition());
			}
			if (checkSessionTime()) {
				s.addCriteria(new TimeElapsedTermination(this.maxSeconds));
			}
			if (checkSessionTimeForPause()) {
				s.addCriteria(new TimeElapsedPause(this.pauseSeconds));
			}
			if (checkTracesForPause()) {
				s.addCriteria(new MaxStepsPause(this.pauseTraces));
			}
			if (checkForEvents()) {
				s.addCriteria(new AfterEventDontExplore(getStopEvents()));
			}
			if (checkForWidgets()) {
				s.addCriteria(new AfterWidgetDontExplore(getStopWidgets()));
			}
			s.setMinDepth(minTransitions);
			return s;
		}
		SimpleStrategy s = new SimpleStrategy (this.comparator);
		if (checkMaxTraces()) {
			s.addTerminationCriteria(new MaxStepsTermination(this.maxTraces));
		}
		s.setMinDepth(minTransitions);
		return s;

	}
	
	public boolean useCustomStrategy () {
		return true;
	}
	
	public boolean checkForDepth() {
		return (this.maxTransitions>0);
	}
	
	public boolean checkMaxTraces() {
		return (this.maxTraces>0);
	}
	
	public boolean checkTracesForPause() {
		return (this.pauseTraces>0);
	}
	
	public boolean checkTransition() {
		return this.checkTransistions;
	}

	public boolean exploreNewStatesOnly() {
		return this.exploreNewStatesOnly;
	}

	public boolean checkSessionTime() {
		return (this.maxSeconds>0);
	}

	public boolean checkSessionTimeForPause() {
		return (this.pauseSeconds>0);
	}
	
	public boolean checkForEvents() {
		return (getStopEvents().length>0);
	}

	public boolean checkForWidgets() {
		return (getStopWidgets().length>0);
	}

	public boolean hasMoreCriterias() {
		return (this.otherCriterias.size()>0);
	}

	public void setDepth(int depth) {
		this.maxTransitions = depth;
	}

	public void setMinDepth(int depth) {
		this.minTransitions = depth;
	}

	public void setMaxTraces(int length) {
		this.maxTraces = length;
	}
	
	public void setCheckTransitions (boolean check) {
		this.checkTransistions = check;
	}

	public void setExploreNewOnly (boolean newOnly) {
		this.exploreNewStatesOnly = newOnly;
	}

	public void setMaxSeconds (long max) {
		this.maxSeconds = max;
	}

	public void setPauseSeconds (long span) {
		this.pauseSeconds = span;
	}

	public void setMoreCriterias (StrategyCriteria ... s) {
		for (StrategyCriteria sc: s) {
			this.otherCriterias.add(sc);
		}
	}

	public void setPauseTraces(int pauseAfterTraces) {
		this.pauseTraces = pauseAfterTraces;		
	}

	public String[] getStopEvents() {
		return stopEvents;
	}

	public void setStopEvents(String ... stopEvents) {
		this.stopEvents = stopEvents;
	}

	public int[] getStopWidgets() {
		return stopWidgets;
	}

	public void setStopWidgets(int ... stopWidgets) {
		this.stopWidgets = stopWidgets;
	}
	
}
