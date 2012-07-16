package com.nofatclips.crawler.guitree;

import java.util.GregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;

import com.nofatclips.androidtesting.guitree.GuiTree;
import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.crawler.Engine;
import com.nofatclips.crawler.Prefs;
import com.nofatclips.crawler.automation.Automation;
import com.nofatclips.crawler.automation.BasicRestarter;
import com.nofatclips.crawler.automation.SimpleTypeDetector;
import com.nofatclips.crawler.filters.AllPassFilter;
import com.nofatclips.crawler.filters.FormFilter;
import com.nofatclips.crawler.model.Filter;
import com.nofatclips.crawler.model.Strategy;
import com.nofatclips.crawler.model.UserAdapter;
import com.nofatclips.crawler.planning.SimplePlanner;
import com.nofatclips.crawler.planning.TraceDispatcher;
import com.nofatclips.crawler.planning.UserFactory;
import com.nofatclips.crawler.storage.PersistenceFactory;
import com.nofatclips.crawler.strategy.*;
import com.nofatclips.crawler.strategy.comparator.Resources;

import static com.nofatclips.crawler.Resources.*;

public class GuiTreeEngine extends Engine {

	public GuiTreeEngine () {
		super ();
		
		setScheduler(getNewScheduler());
		
		// BEGIN - Reading preferences from XML file
		Prefs.setMainNode("com.nofatclips.crawler");
		Prefs.updateMainNode();
//		
//		// Create an input stream on a file
//		InputStream is = null;
//		try {
//			is = new BufferedInputStream(new FileInputStream("/data/data/" + PACKAGE_NAME + "/files/"+ PREFERENCES_FILE));
//			Preferences.importPreferences(is);
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidPreferencesFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		Preferences prefs = Preferences.userRoot().node("com.nofatclips.crawler");
//		RANDOM_SEED = prefs.getLong ("RANDOM_SEED", RANDOM_SEED);
//		
//		String fieldValue = "";
//		for (Field f: Resources.class.getFields()) {
//			
//		    if (Modifier.isFinal(f.getModifiers())) continue;
//
//			try {
//				fieldValue = "\"" + f.get("").toString() + "\"";
//			} catch (IllegalArgumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			Log.e ("nofatclips", "public static " + f.getType() + " " + f.getName() + " = " + fieldValue + ";");
//		}

		// END - Reading preferences from XML file
		
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

		SimplePlanner p = new SimplePlanner();

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
		
		StrategyFactory sf = new StrategyFactory(Resources.COMPARATOR, ADDITIONAL_CRITERIAS);
		sf.setDepth(com.nofatclips.crawler.strategy.Resources.TRACE_MAX_DEPTH);
		sf.setMaxTraces(com.nofatclips.crawler.strategy.Resources.MAX_NUM_TRACES);
		sf.setMaxSeconds(com.nofatclips.crawler.strategy.Resources.MAX_TIME_CRAWLING);
		sf.setPauseSeconds(com.nofatclips.crawler.strategy.Resources.PAUSE_AFTER_TIME);
		sf.setCheckTransitions(com.nofatclips.crawler.strategy.Resources.CHECK_FOR_TRANSITION);
		sf.setPauseTraces(com.nofatclips.crawler.strategy.Resources.PAUSE_AFTER_TRACES);
		sf.setExploreNewOnly(com.nofatclips.crawler.strategy.Resources.EXPLORE_ONLY_NEW_STATES);
		this.theStrategyFactory = sf; // Save in a field so that subclasses can modify the parameters of the strategy

		// Last object to instantiate: the other components register as listeners on the factory class
		this.thePersistenceFactory = new PersistenceFactory (this.theGuiTree, getScheduler());
		
	}
	
	protected void setUp () {
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
		theGuiTree.setSleepAfterEvent(com.nofatclips.crawler.automation.Resources.SLEEP_AFTER_EVENT);
		theGuiTree.setSleepAfterRestart(com.nofatclips.crawler.automation.Resources.SLEEP_AFTER_RESTART);
		theGuiTree.setSleepOnThrobber(com.nofatclips.crawler.automation.Resources.SLEEP_ON_THROBBER);
		theGuiTree.setClassName(CLASS_NAME);
		theGuiTree.setPackageName(PACKAGE_NAME);
		theGuiTree.setComparationWidgets(Resources.COMPARATOR.describe());
		theGuiTree.setInAndOutFocus(com.nofatclips.crawler.automation.Resources.IN_AND_OUT_FOCUS);
		theGuiTree.setSleepAfterTask(com.nofatclips.crawler.automation.Resources.SLEEP_AFTER_TASK);
		theGuiTree.setRandomSeed(RANDOM_SEED);
		theGuiTree.setMaxDepth(com.nofatclips.crawler.strategy.Resources.TRACE_MAX_DEPTH);
		if (!ACTIVITY_DESCRIPTION_IN_SESSION) {
			theGuiTree.setStateFileName(com.nofatclips.crawler.storage.Resources.ACTIVITY_LIST_FILE_NAME);
		}
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
		return (com.nofatclips.crawler.storage.Resources.MAX_TRACES_IN_RAM>0);
	}
	
	private Automation theAutomation;
	private GuiTreeAbstractor guiAbstractor;
	private UserAdapter user;
	private BasicRestarter theRestarter;
	private GuiTree theGuiTree;
	protected StrategyFactory theStrategyFactory;
	protected PersistenceFactory thePersistenceFactory;
	
}
