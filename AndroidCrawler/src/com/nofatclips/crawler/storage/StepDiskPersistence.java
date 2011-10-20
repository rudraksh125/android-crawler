package com.nofatclips.crawler.storage;

import android.content.ContextWrapper;

import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.androidtesting.model.Trace;

import static com.nofatclips.crawler.Resources.*;

public class StepDiskPersistence extends DiskPersistence {

	public StepDiskPersistence () {
		super();
	}
	
	public StepDiskPersistence (Session theSession) {
		super(theSession);
	}

	public StepDiskPersistence (int theStep) {
		this();
		setStep(theStep);
	}
	
	public StepDiskPersistence (Session theSession, int theStep) {
		this (theSession);
		setStep (theStep);
	}
	
	public void setStep (int theStep) {
		this.step = theStep;		
	}
	
	@Override
	public void addTrace (Trace t) {
		super.addTrace (t);
		this.count++;
		if (this.count == this.step) {
			saveStep();
			this.count=0;
		}
	}
	
	@Override
	public String generate () {
		String graph = super.generate();
		int bodyBegin = graph.indexOf(XML_BODY_BEGIN);
		int bodyEnd = graph.lastIndexOf(XML_BODY_END) + XML_BODY_END.length();
		if (isFirst()) {
			this.footer = graph.substring(bodyEnd);
			return graph.substring(0,bodyEnd);
		}
		if (isLast()) {
			return (bodyBegin == -1)?(this.footer):graph.substring(bodyBegin);
		}
		if ( (bodyBegin == -1) || (bodyEnd == -1) ) {
			return "";
		}
		return graph.substring(bodyBegin,bodyEnd);
	}
	
	public void saveStep () {
		if (!isFirst()) {
			this.mode = ContextWrapper.MODE_APPEND;
		}
		save(isLast());
		setNotFirst();
	}
	
	@Override
	public void save () {
		save (true);
	}
	
	public void save (boolean last) {
		if (last) {
			setLast();
		}
		super.save();
		for (Trace t: getSession()) {
			getSession().removeTrace(t);
		}
	}
	
	public boolean isFirst () {
		return this.first;
	}

	public boolean isLast () {
		return this.last;
	}
	
	public void setNotFirst () {
		this.first = false;
	}
	
	public void setLast () {
		this.last = true;
	}
	
	private int step = 1;
	private int count = 0;
	private String footer = "";
	private boolean first = true;
	private boolean last = false;

}