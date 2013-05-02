package it.unina.androidripper.planning;

import static it.unina.androidripper.planning.Resources.MAX_TASKS_IN_SCHEDULER;

import it.unina.androidripper.model.DispatchListener;
import it.unina.androidripper.model.TaskScheduler;
import it.unina.androidripper.planning.TraceDispatcher.SchedulerAlgorithm;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.nofatclips.androidtesting.model.Trace;

class TrivialScheduler implements TaskScheduler {
		
	private final TraceDispatcher traceDispatcher;
	private List<Trace> tasks;
	private SchedulerAlgorithm algorithm;
	
	public TrivialScheduler (TraceDispatcher traceDispatcher, SchedulerAlgorithm algorithm) {
		this.traceDispatcher = traceDispatcher;
		setSchedulerAlgorithm (algorithm);
	}
	
	public void setSchedulerAlgorithm (SchedulerAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public Trace nextTask() {
		Log.i("androidripper", "Dispatching new task. " + tasks.size() + " more tasks remaining.");
		if (!hasMore()) return null;

		switch (algorithm) {
			case DEPTH_FIRST: return lastTask();
			case BREADTH_FIRST: 
			default: return firstTask();
		}
	}

	public void addTasks(Collection<Trace> newTasks) {
		for (Trace t: newTasks) {
			tasks.add(t);
			for (DispatchListener theListener: this.traceDispatcher.theListeners) {
				theListener.onNewTaskAdded(t);
			}
		}				
	}

	public void addPlannedTasks(List<Trace> newTasks) {
		switch (algorithm) {
			case DEPTH_FIRST:
				Collections.reverse(newTasks);
				addTasks(newTasks);
				break;
			case BREADTH_FIRST: 
			default: addTasks(newTasks);
		}
//		if (this.algorithm.equals(SchedulerAlgorithm.BREADTH_FIRST)) {
//			addTasks(newTasks);
//			return;
//		}
//		if (this.algorithm.equals(SchedulerAlgorithm.DEPTH_FIRST)) {
//			List<Trace> invert = 
//			for (Trace t: newTasks) {
//				invert.add(t);
//			}
//			addTasks(Collections.reverse(newTasks););
//		}
	}

	public void setTaskList(List<Trace> theList) {
		this.tasks = theList;
	}

	public List<Trace> getTaskList() {
		return this.tasks;
	}

	public boolean hasMore() {
		return (!tasks.isEmpty());
	}

	public void remove(Trace t) {
		tasks.remove(t);
	}

	public void addTasks(Trace t) {
		discardTasks();
		this.tasks.add(t);
	}

	private void discardTasks() {
		if (MAX_TASKS_IN_SCHEDULER==0) return;
		while (this.tasks.size()>=MAX_TASKS_IN_SCHEDULER) {
			switch (algorithm) {
				case DEPTH_FIRST: 
					remove (firstTask());
					break;
				case BREADTH_FIRST: 
				default: 
					remove (lastTask());
					break;
			}
		}
	}
	
	public Trace firstTask() {
		return this.tasks.get(0);
	}

	public Trace lastTask() {
		return this.tasks.get(this.tasks.size()-1);
	}

}