package com.nofatclips.crawler;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;

import android.test.ActivityInstrumentationTestCase2;

import static com.nofatclips.crawler.Resources.*;

@SuppressWarnings("rawtypes")
public abstract class EngineDebug extends ActivityInstrumentationTestCase2 {
	
	@SuppressWarnings("unchecked")
	public EngineDebug() {
		super(PACKAGE_NAME,theClass);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		getRobot().bind(this);
		getExtractor().extractState();
		ActivityDescription d = getExtractor().describeActivity();
		getAbstractor().setBaseActivity(d);
		Plan thePlan = getPlanner().getPlanForActivity(getAbstractor().getBaseActivity());
		
		for (Transition t: thePlan) {
			Trace newTrace = getAbstractor().createTrace(null, t);
			getScheduler().addTasks(newTrace);
		}
		getStrategy().addState(getAbstractor().getBaseActivity());
	}
	
	public void testAndCrawl() {
		for (Trace theTask: getScheduler()) {
			getRobot().process(theTask);
			ActivityDescription d = getExtractor().describeActivity();
			ActivityState theActivity = getAbstractor().createActivity(d);
			theTask.setFinalActivity (theActivity);
			if (getStrategy().compareState(theActivity)) {
				Plan thePlan = getPlanner().getPlanForActivity(theActivity);
				for (Transition t: thePlan) {
					Trace newTrace = getAbstractor().createTrace(theTask, t);
					getScheduler().addTasks(newTrace);
				}				
			}
			getPersistence().addTrace(theTask);
		}
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
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

	public Session getTheSession() {
		return this.theSession;
	}

	public void setTheSession(Session theSession) {
		this.theSession = theSession;
	}

	private Robot theRobot;
	private Extractor theExtractor;
	private Abstractor theAbstractor;
	private Planner thePlanner;
	private TraceDispatcher theScheduler;
	private Strategy theStrategy;
	private Persistence thePersistence;
	private Session theSession;

}
