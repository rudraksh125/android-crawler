package com.nofatclips.crawler.storage;

import static com.nofatclips.crawler.Resources.*;

import android.util.Log;

import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.crawler.model.Persistence;
import com.nofatclips.crawler.model.Strategy;
import com.nofatclips.crawler.planning.TraceDispatcher;
import com.nofatclips.crawler.strategy.SimpleStrategy;

public class PersistenceFactory {

	Session theSession;
	TraceDispatcher scheduler;
	Strategy theStrategy;

	public PersistenceFactory() {
		super();
	}
	
	public PersistenceFactory(Session theSession) {
		setTheSession(theSession);
	}

	public PersistenceFactory(Session theSession, TraceDispatcher scheduler) {
		this (theSession);
		setDispatcher(scheduler);
	}

	public PersistenceFactory(Session theSession, TraceDispatcher scheduler, Strategy theStrategy) {
		this (theSession, scheduler);
		setStrategy(theStrategy);
	}

	public Persistence getPersistence () {
		Persistence thePersistence;
		if (resumingPersistence()) {
			Log.d("nofatclips", "Generated Resuming Persistence");
			ResumingPersistence rp = new ResumingPersistence();
			thePersistence = rp;
			
			rp.setTaskList(getDispatcher().getScheduler().getTaskList());
			rp.setTaskListFile(TASK_LIST_FILE_NAME);
			rp.setActivityFile(ACTIVITY_LIST_FILE_NAME);

			getDispatcher().registerListener(rp);
			if (getStrategy() instanceof SimpleStrategy) {
				((SimpleStrategy)getStrategy()).registerListener(rp);				
			}
		} else if (stepPersistence()) {
			Log.d("nofatclips", "Generated Step Persistence with step = " + MAX_TRACES_IN_RAM);
			thePersistence = new StepDiskPersistence (MAX_TRACES_IN_RAM);
		} else {
			Log.d("nofatclips", "Generated Default Persistence");
			thePersistence = new DiskPersistence();
		}
		
		thePersistence.setSession(getTheSession());
		return thePersistence;
	}

	public boolean stepPersistence () {
		return (MAX_TRACES_IN_RAM>0);
	}
	
	public boolean resumingPersistence () {
		return ENABLE_RESUME;
	}

	public Session getTheSession() {
		return this.theSession;
	}

	public void setTheSession(Session theSession) {
		this.theSession = theSession;
	}
	
	public TraceDispatcher getDispatcher() {
		return this.scheduler;
	}

	public void setDispatcher(TraceDispatcher scheduler) {
		this.scheduler = scheduler;
	}

	public Strategy getStrategy() {
		return this.theStrategy;
	}

	public void setStrategy(Strategy theStrategy) {
		this.theStrategy = theStrategy;
	}

}
