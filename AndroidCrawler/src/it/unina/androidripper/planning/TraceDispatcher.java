package it.unina.androidripper.planning;

import it.unina.androidripper.model.DispatchListener;
import it.unina.androidripper.model.TaskScheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import com.nofatclips.androidtesting.model.Trace;

import static it.unina.androidripper.planning.Resources.SCHEDULER_ALGORITHM;

public class TraceDispatcher implements Iterable<Trace> {

	private TaskScheduler scheduler;
	List<DispatchListener> theListeners = new ArrayList<DispatchListener>();
	public static enum SchedulerAlgorithm {
		BREADTH_FIRST, DEPTH_FIRST
	}
	
	public TraceDispatcher () {
		this (SCHEDULER_ALGORITHM);
	}

	public TraceDispatcher (String algorithm) {
		this (SchedulerAlgorithm.valueOf(algorithm));
	}
	
	public TraceDispatcher(SchedulerAlgorithm algorithm) {
		setScheduler(getTrivialScheduler(algorithm));
	}

	public TraceDispatcher(TaskScheduler taskScheduler) {
		setScheduler(taskScheduler);
	}

	public void setScheduler (TaskScheduler ts) {
		this.scheduler = ts;
	}

	public void addPlannedTasks (List<Trace> t) {
		getScheduler().addPlannedTasks(t);
	}
	
	public void addTasks (Collection<Trace> t) {
		getScheduler().addTasks(t);
	}
	
	public void addTasks (Trace t) {
		getScheduler().addTasks(t);
	}
	
	public TaskScheduler getTrivialScheduler(SchedulerAlgorithm a) {
		TaskScheduler s = new TrivialScheduler(this, a);
		s.setTaskList(new ArrayList<Trace>());
		return s;
	}

	public TaskScheduler getScheduler() {
		return this.scheduler;
	}

	public void registerListener(DispatchListener theListener) {
		this.theListeners.add(theListener);
	}

	public Iterator<Trace> iterator() {
		return new Iterator<Trace> () {
			
			Trace lastTask;

			public boolean hasNext() {
				return scheduler.hasMore();
			}

			public Trace next() {
				this.lastTask = scheduler.nextTask();
				for (DispatchListener theListener: theListeners) {
					theListener.onTaskDispatched(this.lastTask);
				}
				remove();
				return this.lastTask;
			}

			public void remove() {
				scheduler.remove(this.lastTask);
			}
			
		};
	}

}
