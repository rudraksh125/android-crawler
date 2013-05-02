package it.unina.androidripper.storage;

import static it.unina.androidripper.Resources.*;

import it.unina.androidripper.automation.ScreenshotFactory;
import it.unina.androidripper.model.ImageStorage;
import it.unina.androidripper.model.Persistence;
import it.unina.androidripper.model.SaveStateListener;
import it.unina.androidripper.model.Strategy;
import it.unina.androidripper.planning.TraceDispatcher;
import it.unina.androidripper.strategy.SimpleStrategy;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.nofatclips.androidtesting.model.Session;

public class PersistenceFactory {

	private Session theSession;
	private TraceDispatcher scheduler;
	private Strategy theStrategy;
	static List<SaveStateListener> stateSavers = new ArrayList<SaveStateListener>();

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
			Log.d("androidripper", "Generated Resuming Persistence");
			ResumingPersistence resumer = new ResumingPersistence();
			thePersistence = resumer;
			
			resumer.setTaskList(getDispatcher().getScheduler().getTaskList());
			resumer.setTaskListFile(Resources.TASK_LIST_FILE_NAME);
			resumer.setActivityFile(Resources.ACTIVITY_LIST_FILE_NAME);
			resumer.setParametersFile(Resources.PARAMETERS_FILE_NAME);
			getStrategy().registerTerminationListener(resumer);
			
			for (SaveStateListener saver: stateSavers) {
				resumer.registerListener(saver);
			}

			getDispatcher().registerListener(resumer);
			if (getStrategy() instanceof SimpleStrategy) {
				((SimpleStrategy)getStrategy()).registerStateListener(resumer);				
			}
		} else if (stepPersistence()) {
			Log.d("androidripper", "Generated Step Persistence with step = " + Resources.MAX_TRACES_IN_RAM);
			thePersistence = new StepDiskPersistence (Resources.MAX_TRACES_IN_RAM);
		} else {
			Log.d("androidripper", "Generated Default Persistence");
			thePersistence = new DiskPersistence();
		}
		
		thePersistence.setSession(getTheSession());
		if (thePersistence instanceof ImageStorage) {
			ScreenshotFactory.setTheImageStorage((ImageStorage)thePersistence);
		}
		
		return thePersistence;
	}

	public boolean stepPersistence () {
		return (Resources.MAX_TRACES_IN_RAM>0);
	}
	
	public boolean resumingPersistence () {
		return ENABLE_RESUME || (!ACTIVITY_DESCRIPTION_IN_SESSION);
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
	
	public static void registerForSavingState (SaveStateListener s) {
		stateSavers.add(s);
	}

}
