package com.nofatclips.crawler;

import java.util.GregorianCalendar;

import javax.xml.parsers.ParserConfigurationException;

import com.nofatclips.androidtesting.guitree.GuiTree;

import static com.nofatclips.crawler.Resources.*;

public class GuiTreeEngine extends Engine {

	public GuiTreeEngine () {
		super ();
				
		setScheduler(new TraceDispatcher());
		
		this.theAutomation = new Automation();
		this.theRestarter = new BasicRestarter();
		this.theAutomation.setRestarter(theRestarter);
		setRobot (this.theAutomation);
		setExtractor (this.theAutomation);
		
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

		Filter eventFilter = new SimpleEventFilter();
		p.setEventFilter (eventFilter);
		this.guiAbstractor.addFilter (eventFilter);
		
		this.user = new SimpleUser(this.guiAbstractor);
		p.setUser(user);
		p.setFormFiller(user);
		p.setAbstractor(this.guiAbstractor);
		
		setPlanner (p);
		setStrategy(new SimpleStrategy (COMPARATOR));
		
		d = new DiskPersistence (this.theGuiTree);
		setPersistence (d);
		
	}
	
	protected void setUp () {
		try {
			super.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		d.setContext(theAutomation.getActivity());
		theRestarter.setRestartPoint(theAutomation.getActivity());
		theGuiTree.setAppName(theAutomation.getAppName());
	}
	
	private Automation theAutomation;
	private GuiTreeAbstractor guiAbstractor;
	private SimpleUser user;
	private BasicRestarter theRestarter;
	private GuiTree theGuiTree;
	private DiskPersistence d;
	
}
