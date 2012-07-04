package com.nofatclips.crawler.planning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.crawler.model.DispatchListener;
import com.nofatclips.crawler.model.TaskScheduler;

import static com.nofatclips.crawler.Resources.MAX_TASKS_IN_SCHEDULER;

public class TraceDispatcher implements Iterable<Trace> {

	private TaskScheduler scheduler;
	private List<DispatchListener> theListeners = new ArrayList<DispatchListener>();
	public static enum SchedulerAlgorithm {
		BREADTH_FIRST, DEPTH_FIRST
	}
	
	public TraceDispatcher () {
		this (SchedulerAlgorithm.BREADTH_FIRST);
//		setScheduler(getTrivialScheduler());
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

	public void addTasks (Collection<Trace> t) {
		getScheduler().addTasks(t);
	}
	
	public void addTasks (Trace t) {
		getScheduler().addTasks(t);
	}
	
//	public TaskScheduler getTrivialScheduler() {
//		return getTrivialScheduler(SchedulerAlgorithm.BREADTH_FIRST);
//	}

	public TaskScheduler getTrivialScheduler(SchedulerAlgorithm a) {
		TaskScheduler s = new TrivialScheduler(a);
		s.setTaskList(new ArrayList<Trace>());
		return s;
	}

	public TaskScheduler getScheduler() {
		return this.scheduler;
	}

	public void registerListener(DispatchListener theListener) {
//		throw new Error();
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
	
	private class TrivialScheduler implements TaskScheduler {
		
		private List<Trace> tasks;
		private SchedulerAlgorithm algorithm;
		
//		public TrivialScheduler () {
//		}

		public TrivialScheduler (SchedulerAlgorithm algorithm) {
			setSchedulerAlgorithm (algorithm);
		}
		
		public void setSchedulerAlgorithm (SchedulerAlgorithm algorithm) {
			this.algorithm = algorithm;
		}

		public Trace nextTask() {
			Log.i("nofatclips", "Dispatching new task. " + tasks.size() + " more tasks remaining.");
			if (!hasMore()) return null;

			switch (algorithm) {
				case DEPTH_FIRST: return lastTask();
				case BREADTH_FIRST: 
				default: return firstTask();
			}
//			Trace t = (hasMore())?tasks.get(0):null;
		}

		public void addTasks(Collection<Trace> newTasks) {
			for (Trace t: newTasks) {
				tasks.add(t);
				for (DispatchListener theListener: theListeners) {
					theListener.onNewTaskAdded(t);
				}
			}				
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
					case DEPTH_FIRST: remove (firstTask());
					case BREADTH_FIRST: 
					default: remove(lastTask());
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

}
