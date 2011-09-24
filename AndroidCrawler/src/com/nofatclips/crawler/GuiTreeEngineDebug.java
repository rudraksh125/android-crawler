package com.nofatclips.crawler;

import javax.xml.parsers.ParserConfigurationException;

import com.nofatclips.androidtesting.guitree.GuiTree;

public class GuiTreeEngineDebug extends EngineDebug {

	public GuiTreeEngineDebug () {
		super();
	}

	
	public void constructThis () {
		setScheduler(new TraceDispatcher());
		
		this.theAutomation = new Automation();
		this.theRestarter = new BasicRestarter();
		this.theAutomation.setRestarter(theRestarter);
		setRobot (this.theAutomation);
		setExtractor (this.theAutomation);
		
		try {
			GuiTree.setValidation(false);
			this.guiAbstractor = new GuiTreeAbstractor(); 
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setAbstractor(this.guiAbstractor);

		SimplePlanner p = new SimplePlanner();

		Filter inputFilter = new FormFilter();
		p.setInputFilter (inputFilter);
		this.guiAbstractor.addFilter (inputFilter);

		Filter eventFilter = new ButtonFilter();
		p.setEventFilter (eventFilter);
		this.guiAbstractor.addFilter (eventFilter);
		
		this.user = new SimpleUser(this.guiAbstractor);
		p.setUser(user);
		p.setFormFiller(user);
		p.setAbstractor(this.guiAbstractor);
		
		setPlanner (p);
		setStrategy(new SimpleStrategy ());
		setPersistence (new DiskPersistence ());
		
	}
	
	protected void setUp () {
		constructThis();
		try {
			super.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		theRestarter.setRestartPoint(theAutomation.getActivity());
	}
	
	private Automation theAutomation;
	private GuiTreeAbstractor guiAbstractor;
	private SimpleUser user;
	private BasicRestarter theRestarter;
	
}
