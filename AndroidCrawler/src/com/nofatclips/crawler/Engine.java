package com.nofatclips.crawler;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.xml.XmlGraph;
import com.nofatclips.crawler.model.*;
import com.nofatclips.crawler.planning.TraceDispatcher;
import com.nofatclips.crawler.storage.ResumingPersistence;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import static com.nofatclips.crawler.Resources.*;

@SuppressWarnings("rawtypes")
public abstract class Engine extends ActivityInstrumentationTestCase2 {
	
	@SuppressWarnings("unchecked")
	public Engine() {
		super(PACKAGE_NAME,theClass);
	}
	
	public abstract Session getNewSession();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		getRobot().bind(this);
		getExtractor().extractState();
		Activity a = getExtractor().getActivity();
		getPersistence().setFileName(FILE_NAME);
		getPersistence().setContext(a);
		ActivityDescription d = getExtractor().describeActivity();
		getAbstractor().setBaseActivity(d);
		if (!resume()) {
			getStrategy().addState(getAbstractor().getBaseActivity());
			planFirstTests(getAbstractor().getBaseActivity());
		}
	}
	
	public void testAndCrawl() {
		for (Trace theTask: getScheduler()) {
			getStrategy().setTask(theTask);
			getRobot().process(theTask);
			ActivityDescription d = getExtractor().describeActivity();
			ActivityState theActivity = getAbstractor().createActivity(d);
			getStrategy().compareState(theActivity);
			if (!getStrategy().checkForTransition()) continue;
			theTask.setFinalActivity (theActivity);
			getPersistence().addTrace(theTask);
			if (theActivity.getId() != "exit") {
				if (getStrategy().checkForExploration()) {
					planTests(theTask, theActivity);
				}
			}
			if (getStrategy().checkForTermination(theActivity)) break;
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
		if (getStrategy().getTask().isFailed()) {
			getSession().addFailedTrace(getStrategy().getTask());
		}
		getPersistence().save();
		getRobot().finalize();
		super.tearDown();
	}
	
	public boolean resume() {
		Log.e("nofatclips", "Checking for resume");
		boolean flag = ENABLE_RESUME;
		if (!flag) return false;
		if (!(getPersistence() instanceof ResumingPersistence)) return false;
		if (!getPersistence().exists(TASK_LIST_FILE_NAME)) return false;
		if (!getPersistence().exists(FILE_NAME)) return false;
		if (!getPersistence().exists(ACTIVITY_LIST_FILE_NAME)) throw new Error("Cannot resume previous session: state list not found.");
		
		ResumingPersistence r = (ResumingPersistence)getPersistence();
		List<String> entries;
		Session sandboxSession = getNewSession();
		Element e;
		
		// Importing task list
		entries = r.readTaskFile();
		List<Trace> taskList = new ArrayList<Trace>();
		Trace t;
		for (String trace: entries) {
			sandboxSession.parse(trace);
			e = ((XmlGraph)sandboxSession).getDom().getDocumentElement();
			t = getAbstractor().importTask (e);
			if (t.isFailed()) {
				Log.e("nofatclips", "Importing crashed trace #" + t.getId() + " from disk");
				getSession().addCrashedTrace(t);
			} else {
				Log.e("nofatclips", "Importing trace #" + t.getId() + " from disk");
				taskList.add(t);
			}
		}
		getScheduler().addTasks(taskList);
		
		// Importing activity list
		entries = r.readStateFile();
		List<ActivityState> stateList = new ArrayList<ActivityState>();
		ActivityState s;
		for (String state: entries) {
			sandboxSession.parse(state);
			e = ((XmlGraph)sandboxSession).getDom().getDocumentElement();
			s = getAbstractor().importState (e);
			stateList.add(s);
			Log.e("nofatclips", "Imported activity state " + s.getId() + " from disk");
		}
		for (ActivityState state: stateList) {
			getStrategy().addState(state);
		}

		r.setNotFirst();

		return true;
	}
	
	private void planFirstTests (ActivityState theActivity) {
		Plan thePlan = getPlanner().getPlanForBaseActivity(theActivity);
		planTests (null, thePlan);
	}
	
	private void planTests (Trace theTask, ActivityState theActivity) {
		Plan thePlan = getPlanner().getPlanForActivity(theActivity);
		planTests (theTask, thePlan);
	}
	
	private void planTests (Trace theTask, Plan thePlan) {
		for (Transition t: thePlan) {
			Trace newTrace = getAbstractor().createTrace(theTask, t);
			newTrace.setId(nextId());
			getScheduler().addTasks(newTrace);
		}		
	}
	
	public Robot getRobot() {
		return this.theRobot;
	}

	public void setRobot(Robot theRobot) {
		this.theRobot = theRobot;
	}

	public Extractor getExtractor() {
		return this.theExtractor;
	}

	public void setExtractor(Extractor theExtractor) {
		this.theExtractor = theExtractor;
	}
	
	public Abstractor getAbstractor() {
		return this.theAbstractor;
	}

	public void setAbstractor(Abstractor theAbstractor) {
		this.theAbstractor = theAbstractor;
	}

	public Planner getPlanner() {
		return this.thePlanner;
	}

	public void setPlanner(Planner thePlanner) {
		this.thePlanner = thePlanner;
	}
	
	public TraceDispatcher getScheduler () {
		return this.theScheduler;
	}
	
	public void setScheduler (TraceDispatcher theScheduler) {
		this.theScheduler = theScheduler;
	}

	public Strategy getStrategy() {
		return this.theStrategy;
	}

	public void setStrategy(Strategy theStrategy) {
		this.theStrategy = theStrategy;
	}
	
	public Persistence getPersistence() {
		return this.thePersistence;
	}

	public void setPersistence(Persistence thePersistence) {
		this.thePersistence = thePersistence;
	}
	
	public Session getSession() {
		return this.theSession;
	}

	public void setSession(Session theSession) {
		this.theSession = theSession;
	}
	
	protected String nextId () {
		int num = id;
		id++;
		return String.valueOf(num);
	}

	private Robot theRobot;
	private Extractor theExtractor;
	private Abstractor theAbstractor;
	private Planner thePlanner;
	private TraceDispatcher theScheduler;
	private Strategy theStrategy;
	private Persistence thePersistence;
	private Session theSession;
	private int id = 0;

}
