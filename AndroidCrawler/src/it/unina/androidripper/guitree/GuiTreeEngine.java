package it.unina.androidripper.guitree;

import it.unina.androidripper.Engine;
import it.unina.androidripper.automation.*;
import it.unina.androidripper.filters.*;
import it.unina.androidripper.model.*;
import it.unina.androidripper.planning.*;
import it.unina.androidripper.planning.interactors.values_cache.ValuesCache;
import it.unina.androidripper.storage.PersistenceFactory;
import it.unina.androidripper.strategy.*;
import it.unina.androidripper.strategy.comparator.Resources;

import java.util.GregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;

import android.content.Context;
import android.util.Log;

import com.nofatclips.androidtesting.guitree.GuiTree;
import com.nofatclips.androidtesting.model.Session;

import static it.unina.androidripper.Resources.*;
import static it.unina.androidripper.automation.Resources.*;
import static it.unina.androidripper.storage.Resources.*;
import static it.unina.androidripper.strategy.Resources.*;

public class GuiTreeEngine extends Engine {

	public GuiTreeEngine () {
		super ();
		
		setScheduler(getNewScheduler());
		
		this.theAutomation = getNewAutomation();
		this.theRestarter = new BasicRestarter();
		this.theAutomation.setRestarter(theRestarter);
		setRobot (this.theAutomation);
		setExtractor (this.theAutomation);
		setImageCaptor(this.theAutomation);
		
		try {
			GuiTree.setValidation(false);
			this.guiAbstractor = new GuiTreeAbstractor();
			this.theGuiTree = this.guiAbstractor.getTheSession();
			GregorianCalendar c=new GregorianCalendar();
			theGuiTree.setDateTime(c.getTime().toString());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setAbstractor(this.guiAbstractor);
		setSession (this.theGuiTree);
		
		String className = "it.unina.androidripper.planning." + it.unina.androidripper.planning.Resources.PLANNER;
		SimplePlanner p;
		try {
			p = (SimplePlanner)Class.forName(className).newInstance();
		} catch (Exception e) {		
			//e.printStackTrace();
			Log.e(TAG, "Error during planner instantiation: " + e.toString());
			throw new RuntimeException(e);
		}		


		Filter inputFilter = new FormFilter();
		p.setInputFilter (inputFilter);
		this.guiAbstractor.addFilter (inputFilter);

		Filter eventFilter = new AllPassFilter(); //SimpleEventFilter();
		p.setEventFilter (eventFilter);
		this.guiAbstractor.addFilter (eventFilter);
		this.guiAbstractor.setTypeDetector(new SimpleTypeDetector());
		
		this.user = UserFactory.getUser(this.guiAbstractor);
		p.setUser(user);
		p.setFormFiller(user);
		p.setAbstractor(this.guiAbstractor);
		setPlanner (p);
		
		StrategyFactory sf = new StrategyFactory(Resources.COMPARATOR, ADDITIONAL_CRITERIA);
		sf.setDepth(TRACE_MAX_DEPTH);
		sf.setMaxTraces(MAX_NUM_TRACES);
		sf.setMaxSeconds(MAX_TIME_CRAWLING);
		sf.setPauseSeconds(PAUSE_AFTER_TIME);
		sf.setCheckTransitions(CHECK_FOR_TRANSITION);
		sf.setPauseTraces(PAUSE_AFTER_TRACES);
		sf.setExploreNewOnly(EXPLORE_ONLY_NEW_STATES);
		sf.setMinDepth(TRACE_MIN_DEPTH);
		sf.setStopEvents(AFTER_EVENT_DONT_EXPLORE);
		sf.setStopWidgets(AFTER_WIDGET_DONT_EXPLORE);
		this.theStrategyFactory = sf; // Save in a field so that subclasses can modify the parameters of the strategy

		// Last object to instantiate: the other components register as listeners on the factory class
		this.thePersistenceFactory = new PersistenceFactory (this.theGuiTree, getScheduler());
		
	}
	
	protected void setUp (){		
		
		Strategy s = this.theStrategyFactory.getStrategy();
		setStrategy (s);
		this.thePersistenceFactory.setStrategy(s);
		setPersistence (this.thePersistenceFactory.getPersistence());
		try {
			super.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		theRestarter.setRestartPoint(theAutomation.getActivity());
		theGuiTree.setAppName(theAutomation.getAppName());
		theGuiTree.setSleepAfterEvent(SLEEP_AFTER_EVENT);
		theGuiTree.setSleepAfterRestart(SLEEP_AFTER_RESTART);
		theGuiTree.setSleepOnThrobber(SLEEP_ON_THROBBER);
		theGuiTree.setClassName(CLASS_NAME);
		theGuiTree.setPackageName(PACKAGE_NAME);
		theGuiTree.setComparationWidgets(Resources.COMPARATOR.describe());
		theGuiTree.setInAndOutFocus(IN_AND_OUT_FOCUS);
		theGuiTree.setSleepAfterTask(SLEEP_AFTER_TASK);
		theGuiTree.setRandomSeed(RANDOM_SEED);
		theGuiTree.setMaxDepth(TRACE_MAX_DEPTH);
		if (!ACTIVITY_DESCRIPTION_IN_SESSION) {
			theGuiTree.setStateFileName(ACTIVITY_LIST_FILE_NAME);
		}
		
		if (it.unina.androidripper.planning.Resources.DICTIONARY_FIXED_VALUE){
			Context ctx = this.getActivity().getApplicationContext();
			
			if (ctx == null)
				ctx = this.getInstrumentation().getTargetContext().getApplicationContext();
			
			ValuesCache.init(ctx);
		}
		
	}
	
	@Override
	protected void tearDown() throws Exception{
		super.tearDown();
	}		
	
	public Session getNewSession() {
		try {
			return new GuiTree();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public TraceDispatcher getNewScheduler() {
		return new TraceDispatcher();
	}

	public Automation getNewAutomation() {
		return new Automation();
	}

	public boolean stepPersistence () {
		return (MAX_TRACES_IN_RAM>0);
	}
	
	private Automation theAutomation;
	private GuiTreeAbstractor guiAbstractor;
	private UserAdapter user;
	private BasicRestarter theRestarter;
	private GuiTree theGuiTree;
	protected StrategyFactory theStrategyFactory;
	protected PersistenceFactory thePersistenceFactory;
	
}
