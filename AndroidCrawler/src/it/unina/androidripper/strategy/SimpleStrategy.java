package it.unina.androidripper.strategy;

import it.unina.androidripper.model.Comparator;
import it.unina.androidripper.model.StateDiscoveryListener;
import it.unina.androidripper.model.Strategy;
import it.unina.androidripper.model.TerminationListener;
import it.unina.androidripper.strategy.criteria.PauseCriteria;
import it.unina.androidripper.strategy.criteria.TerminationCriteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import android.util.Log;
import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;

public class SimpleStrategy implements Strategy {

	private HashSet<ActivityState> guiNodes = new HashSet<ActivityState> ();
	private Comparator c;
	protected Collection<TerminationCriteria> terminators = new ArrayList<TerminationCriteria>();
	protected Collection<PauseCriteria> pausers = new ArrayList<PauseCriteria>();
	protected boolean positiveComparation = true;
	private Trace theTask;
	private ActivityState beforeEvent;
	private ActivityState afterEvent;
	private int depth;
	private int minDepth;
	private List<StateDiscoveryListener> theListeners = new ArrayList<StateDiscoveryListener>();
	private List<TerminationListener> endListeners = new ArrayList<TerminationListener>();

	public SimpleStrategy () {
		super();
	}

	public SimpleStrategy (Comparator c) {
		super();
		setComparator(c);
	}
	
	public void addState(ActivityState newActivity) {
		for (StateDiscoveryListener listener: getListeners()) {
			listener.onNewState(newActivity);
		}
		this.guiNodes.add(newActivity);
	}

	public boolean compareState(ActivityState theActivity) {
		this.afterEvent = theActivity;
		this.positiveComparation = true;
		String name = theActivity.getName();
		if (theActivity.isExit()) {
			Log.i("androidripper", "Exit state. Not performing comparation for activity " + name);
			return false;
		}
		Log.i("androidripper", "Performing comparation for activity " + name);
		for (ActivityState stored: guiNodes) {
			Log.d("androidripper", "Comparing against activity " + stored.getName());
			if (getComparator().compare(theActivity, stored)) {
				theActivity.setId(stored.getId());
				Log.d("androidripper", "This activity state is equivalent to " + stored.getId());
				return true;
			}
		}
		Log.i("androidripper", "Registering activity " + name + " (id: " + theActivity.getId() + ") as a new found state");
		this.positiveComparation = false;
		addState (theActivity);
		return false;
	}
	
	public boolean checkForTermination () { // Logic OR of the criterias
		for (TerminationCriteria t: this.terminators) {
			if (t.termination()) {
				for (TerminationListener tl: getEndListeners()) {
					tl.onTerminate();
				}
				return true;
			}
		}
		return false;
	}

	public boolean checkForPause () { // Logic OR of the criterias
		for (PauseCriteria p: this.pausers) {
			if (p.pause()) return true;
		}
		return false;
	}

	public boolean checkForTransition () { // Assume that there is always a transition
		return true;
	}
	
	public void addTerminationCriteria (TerminationCriteria t) {
		t.setStrategy(this);
		this.terminators.add(t);
	}

	public void addPauseCriteria (PauseCriteria p) {
		p.setStrategy(this);
		this.pausers.add(p);
	}

	public Comparator getComparator() {
		return this.c;
	}

	public void setComparator(Comparator c) {
		this.c = c;
	}

	public final boolean checkForExploration() {
		if (this.depth<this.minDepth) return true;
		return explorationNeeded();
	}
	
	protected boolean explorationNeeded() {
		return !isLastComparationPositive();
	}

	public boolean isLastComparationPositive() {
		return positiveComparation;
	}
	
	public void setTask(Trace theTask) {
		this.theTask = theTask;
		this.beforeEvent = theTask.getFinalTransition().getStartActivity();
		setDepth();
	}
	
	public Trace getTask () {
		return this.theTask;
	}
	
	@SuppressWarnings("unused")
	public void setDepth() {
		int transitions = 0;
		for (Transition t: getTask()) {
			transitions++;
		}
		this.depth=transitions;
	}
	
	public int getDepth() {
		return depth;
	}

	public ActivityState getStateBeforeEvent () {
		return this.beforeEvent;
	}

	public ActivityState getStateAfterEvent () {
		return this.afterEvent;
	}

	public List<StateDiscoveryListener> getListeners() {
		return this.theListeners;
	}

	public List<TerminationListener> getEndListeners() {
		return this.endListeners;
	}

	public void registerStateListener(StateDiscoveryListener theListener) {
		this.theListeners.add(theListener);
	}

	public void registerTerminationListener(TerminationListener theListener) {
		this.endListeners.add(theListener);
	}

	public int getMinDepth() {
		return this.minDepth;
	}

	public void setMinDepth (int minDepth) {
		this.minDepth = minDepth;
	}

}
