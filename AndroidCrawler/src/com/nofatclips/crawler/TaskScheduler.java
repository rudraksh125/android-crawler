package com.nofatclips.crawler;

import java.util.Collection;
import java.util.List;

import com.nofatclips.androidtesting.model.Trace;

public interface TaskScheduler {
	
	public Trace nextTask();
	public void addTasks (Collection<Trace> newTasks);
	public void setTaskList (List<Trace> theList);
	public boolean hasMore();
	public void remove (Trace t);
	public void addTasks(Trace t);

}
