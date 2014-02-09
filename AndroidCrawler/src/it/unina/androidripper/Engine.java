package it.unina.androidripper;

import it.unina.androidripper.automation.Resources;
import it.unina.androidripper.automation.ScreenshotFactory;
import it.unina.androidripper.model.*;
import it.unina.androidripper.planning.TraceDispatcher;
import it.unina.androidripper.storage.PersistenceFactory;
import it.unina.androidripper.storage.ResumingPersistence;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.*;
import com.nofatclips.androidtesting.xml.XmlGraph;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import static it.unina.androidripper.Resources.*;

@SuppressWarnings("rawtypes")
public abstract class Engine extends ActivityInstrumentationTestCase2 implements SaveStateListener {

	@SuppressWarnings("unchecked")
	public Engine() {
		super(theClass);
		PersistenceFactory.registerForSavingState(this);
	}
	
	public abstract Session getNewSession();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		if (getImageCaptor()!=null) {
			ScreenshotFactory.setImageCaptor(getImageCaptor());
		}
		getRobot().bind(this);
		getExtractor().extractState();
		Activity a = getExtractor().getActivity();
		getPersistence().setFileName(it.unina.androidripper.storage.Resources.FILE_NAME);
		getPersistence().setContext(a);
		ActivityDescription d = getExtractor().describeActivity();
		getAbstractor().setBaseActivity(d);
		if (resume()) {
			setupAfterResume();
		} else {
			setupFirstStart();
		}
	}

	protected void setupFirstStart() {
		Log.i(TAG, "Starting a new session");
		ActivityState baseActivity = getAbstractor().getBaseActivity(); 
		getStrategy().addState(baseActivity);
		if (screenshotEnabled()) {
			takeScreenshot (baseActivity);
		}
		planFirstTests(baseActivity);
	}

	protected void setupAfterResume() {
		// do nothing
	}

	public void testAndCrawl() {
		for (Trace theTask: getScheduler()) {
			GregorianCalendar c=new GregorianCalendar();
			theTask.setDateTime(c.getTime().toString());
			getStrategy().setTask(theTask);
			process(theTask);
			ActivityDescription d = getExtractor().describeActivity();
			ActivityState theActivity = getAbstractor().createActivity(d);
			getStrategy().compareState(theActivity);
			if (screenshotNeeded()) {
				takeScreenshot(theActivity);
			}
			getRobot().wait(Resources.SLEEP_AFTER_TASK);
			if (!getStrategy().checkForTransition()) continue;
			getAbstractor().setFinalActivity (theTask, theActivity);
			getPersistence().addTrace(theTask);
			if (canPlanTests(theActivity)) {
				planTests(theTask, theActivity);
			} else {
				doNotPlanTests();
			}
			if ( (getStrategy().checkForTermination()) || (getStrategy().checkForPause()) ) break;
		}
	}
	
	protected void process(Trace theTask) {
		getRobot().process(theTask);
	}

	protected boolean canPlanTests (ActivityState theActivity){
		return (!(theActivity.isExit()) && getStrategy().checkForExploration());
	}
	
	@Override
	protected void tearDown() throws Exception {
		if ((getStrategy().getTask() != null) && (getStrategy().getTask().isFailed())) {
			getSession().addFailedTrace(getStrategy().getTask());
		}
		getPersistence().save();
		getRobot().finalize();
		super.tearDown();
	}
	
	public boolean resume() {
		if (!(getPersistence() instanceof ResumingPersistence)) {
			Log.w(TAG, "The instance of Persistence does not implement Resuming.");
			return false;
		}
		
		ResumingPersistence r = (ResumingPersistence)getPersistence();
		if (!r.canHasResume()) return false;
		Log.i(TAG, "Attempting to resume previous session");

		importTaskList(r);
		
		importActivitiyList(r);

		r.loadParameters();
		r.setNotFirst();
		r.saveStep();

		return true;
	}

	public void importActivitiyList(ResumingPersistence r) {
		if (getStrategy().getComparator() instanceof StatelessComparator) {
			Log.i(TAG,"Stateless comparator: the state file will not be loaded.");
			return;
		}
		List<String> entries;
		Session sandboxSession = getNewSession();
		Element e;
		entries = r.readStateFile();
		List<ActivityState> stateList = new ArrayList<ActivityState>();
		ActivityState s;
		for (String state: entries) {
			sandboxSession.parse(state);
			e = ((XmlGraph)sandboxSession).getDom().getDocumentElement();
			s = getAbstractor().importState (e);
			stateList.add(s);
			Log.d(TAG, "Imported activity state " + s.getId() + " from disk");
		}
		for (ActivityState state: stateList) {
			getStrategy().addState(state);
		}
	}

	public void importTaskList(ResumingPersistence r) {
		boolean noLoadTasks = this instanceof MemorylessEngine;
		if (noLoadTasks) {
			Log.i(TAG,"Memoryless engine: the task file will not be loaded. Looking for crashed traces.");
		}
		List<String> entries;
		Session sandboxSession = getNewSession();
		Element e;
		entries = r.readTaskFile();
		List<Trace> taskList = new ArrayList<Trace>();
		Trace t;
		for (String trace: entries) {
			sandboxSession.parse(trace);
			e = ((XmlGraph)sandboxSession).getDom().getDocumentElement();
			t = getAbstractor().importTask (e);
			if (t.isFailed()) {
				Log.d(TAG, "Importing crashed trace #" + t.getId() + " from disk");
				getSession().addCrashedTrace(t);
			} else if (noLoadTasks) {
				Log.v(TAG, "Discarding trace #" + t.getId());
			} else {
				Log.d(TAG, "Importing trace #" + t.getId() + " from disk");
				taskList.add(t);
			}
		}
		getScheduler().addTasks(taskList);
	}
	
	protected void planFirstTests (ActivityState theActivity) {
		Plan thePlan = getPlanner().getPlanForBaseActivity(theActivity);
		planTests (null, thePlan);
	}
	
	protected void planTests (Trace theTask, ActivityState theActivity) {
		Plan thePlan = getPlanner().getPlanForActivity(theActivity);
		planTests (theTask, thePlan);
	}
	
	protected void planTests (Trace baseTask, Plan thePlan) {
		List<Trace> tasks = new ArrayList<Trace>();
		for (Transition t: thePlan) {
			tasks.add(getNewTask(baseTask, t));
		}
		getScheduler().addPlannedTasks(tasks);
	}
	
	protected void doNotPlanTests() { 
		// do nothing 
	}
	
	protected Trace getNewTask (Trace theTask, Transition t) {
		Trace newTrace = getAbstractor().createTrace(theTask, t);
		newTrace.setId(nextId());
		return newTrace;
	}
	
	public SessionParams onSavingState () {
		return new SessionParams (PARAM_NAME, this.id);
	}
	
	public void onLoadingState(SessionParams sessionParams) {
		this.id = sessionParams.getInt(PARAM_NAME);
		Log.d(TAG,"Restored trace count to " + this.id);
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
	
	public ImageCaptor getImageCaptor() {
		return this.theImageCaptor;
	}

	public void setImageCaptor(ImageCaptor theImageCaptor) {
		this.theImageCaptor = theImageCaptor;
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
	
	public int getLastId() {
		return this.id;
	}
	
	protected String nextId () {
		int num = id;
		id++;
		return String.valueOf(num);
	}
	
	public String getListenerName () {
		return ACTOR_NAME;
	}
	
	public boolean screenshotEnabled() {
		return it.unina.androidripper.automation.Resources.SCREENSHOT_FOR_STATES;
	}
	
	public boolean screenshotEveryTrace() {
		return !it.unina.androidripper.automation.Resources.SCREENSHOT_ONLY_NEW_STATES;
	}
	
	public boolean screenshotNeeded() {
		if (!screenshotEnabled()) return false; // Function disable, always return false
		if (screenshotEveryTrace()) return true; // Function enable for all states, always return true
		return !(getStrategy().isLastComparationPositive()); // Function enabled for new states only: return true if comparation was false
	}
	
	public String screenshotName (String stateId) {
		return stateId + "." + ScreenshotFactory.getFileExtension();
	}
	
	private void takeScreenshot(ActivityState theActivity) {
		if (theActivity.isExit()) return;
		String fileName = screenshotName(theActivity.getUniqueId());
		if (ScreenshotFactory.saveScreenshot(fileName)) {
			theActivity.setScreenshot(fileName);
		}
	}

	public final static String ACTOR_NAME = "Engine";
	
	private Robot theRobot;
	private Extractor theExtractor;
	private Abstractor theAbstractor;
	private Planner thePlanner;
	private TraceDispatcher theScheduler;
	private Strategy theStrategy;
	private Persistence thePersistence;
	private Session theSession;
	private ImageCaptor theImageCaptor;
	
	private final static String PARAM_NAME = "taskId";
	private int id = 0;

}
