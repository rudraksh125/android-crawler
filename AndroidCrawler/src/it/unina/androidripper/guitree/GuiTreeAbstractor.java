package it.unina.androidripper.guitree;

import it.unina.androidripper.helpers.ReflectionHelper;
import it.unina.androidripper.model.*;
import it.unina.androidripper.storage.PersistenceFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.nofatclips.androidtesting.guitree.*;
import com.nofatclips.androidtesting.model.*;
import com.nofatclips.dictionary.ContentTypeDetector;

import static it.unina.androidripper.Resources.TAG;
import static it.unina.androidripper.storage.Resources.*;
import static it.unina.androidripper.automation.AbstractorUtilities.*;

public class GuiTreeAbstractor implements Abstractor, FilterHandler, SaveStateListener {

	private GuiTree theSession;
	private StartActivity baseActivity;
	private HashSet<Filter> filters;
	private int eventId=0;
	private int inputId=0;
	private int activityId=0;
	private int widgetId=0;
	private TypeDetector detector;
	private List<AbstractorListener> theListeners = new ArrayList<AbstractorListener>();
	public final static String ACTOR_NAME = "GuiTreeAbstractor";
	private static final String EVENT_PARAM_NAME = "eventId";
	private static final String INPUT_PARAM_NAME = "inputId";
	private static final String ACTIVITY_PARAM_NAME = "activityId";
	private static final String WIDGET_PARAM_NAME = "widgetId";

	public GuiTreeAbstractor () throws ParserConfigurationException {
		this (new GuiTree());
	}
	
	public GuiTreeAbstractor(GuiTree s) {
		super();
		this.filters = new HashSet<Filter>();
		setTheSession(s);
		PersistenceFactory.registerForSavingState(this);
	}

	public GuiTree getTheSession() {
		return this.theSession;
	}

	public void setTheSession(GuiTree theSession) {
		this.theSession = theSession;
	}
	
	public TypeDetector getTypeDetector () {
		return this.detector;
	}
	
	public void setTypeDetector (TypeDetector t) {
		this.detector = t;
	}

	public ActivityState createActivity (ActivityDescription desc) {
		return createActivity (desc,false);
	}
	
	// If the boolean parameter is omitted, the overloading method will default to a Final Activity
	public ActivityState createActivity (ActivityDescription desc, boolean start) {
		ActivityState newActivity = (start)?StartActivity.createActivity(getTheSession()):FinalActivity.createActivity(getTheSession());
		newActivity.setName(desc.getActivityName());
		newActivity.setTitle(desc.getActivityTitle());
		newActivity.setUniqueId(getUniqueActivityId());
		newActivity.setId(newActivity.getUniqueId());
		for (Filter f: this.filters) {
			f.clear();
		}
		boolean hasDescription = updateDescription(newActivity, desc, false);
		if (!hasDescription) newActivity.markAsExit();
		for (AbstractorListener listener: this.theListeners) {
			listener.onNewActivity(newActivity);
		}
		return newActivity;
	}

	public boolean updateDescription (ActivityState newActivity, ActivityDescription desc) {
		return updateDescription  (newActivity, desc, true);
	}

	public TestCaseWidget createWidget (View v) {
		TestCaseWidget w = TestCaseWidget.createWidget(getTheSession());
		String id = String.valueOf(v.getId());
		String name = detectName(v);
		int type = 0;
		if (v instanceof TextView) {
			type = ((TextView)v).getInputType();
			if (type!=0) {
				w.setTextType("" + type);
			}
		}
		w.setIdNameType(id, name, getType(v));
		w.setUniqueId(getUniqueWidgetId());
		w.setSimpleType(getTypeDetector().getSimpleType(v));
		
		if (v instanceof AutoCompleteTextView){
			// do Nothing
		}else if (v instanceof EditText){
			
				if (	v.isFocusable()
						&&	v.isFocusableInTouchMode()
						&&	ReflectionHelper.checkIfFieldIsSet(v, "android.view.View", "mOnFocusChangeListener")
					)
				{
					w.setSimpleType(SimpleType.FOCUSABLE_EDIT_TEXT);
				}
				
		}
		
		setCount (v,w);
		setValue (v,w);
		w.setAvailable((v.isEnabled())?"true":"false");
		w.setClickable((v.isClickable())?"true":"false");
		w.setLongClickable((v.isLongClickable())?"true":"false");

		//if duplicato perche' nome e valore vengono settati dopo l'if precedente
		if (v instanceof TextView) {
			//default
			String txtId = reflectTextualIDbyNumericalID(v.getId());
			Log.v(TAG, "TextualID : " + txtId);
			w.setContentType(ContentTypeDetector.detect(w, txtId));
			Log.v(TAG, "ContentType detected : " + w.getContentType());			
		}
		
		return w;
	}

	public boolean updateDescription (ActivityState newActivity, ActivityDescription desc, boolean detectDuplicates) {
		boolean hasDescription = false;
		
		for (View v: desc) {
			hasDescription = true;
			if (!v.isShown()) continue;
			TestCaseWidget w = createWidget (v);
			w.setIndex(desc.getWidgetIndex(v));
			if (detectDuplicates && newActivity.hasWidget(w)) continue;
			newActivity.addWidget(w);

			for (Filter f: this.filters) {
				f.loadItem(v, w);
			}
			
		}
		
		return hasDescription;
	}
		
	public void setBaseActivity (ActivityDescription desc) {
		this.baseActivity = (StartActivity) createActivity(desc,true);
	}
	
	public ActivityState getBaseActivity () {
		return this.baseActivity;
	}

	public void setStartActivity (Transition theStep, ActivityState theActivity) {
		theStep.setStartActivity ((ACTIVITY_DESCRIPTION_IN_SESSION)?theActivity:stubActivity(theActivity));
	}

	public void setFinalActivity (Trace theTask, ActivityState theActivity) {
		theTask.setFinalActivity ((ACTIVITY_DESCRIPTION_IN_SESSION)?theActivity:stubActivity(theActivity));
	}
	
	private TestCaseActivity stubActivity (ActivityState theActivity) {
		TestCaseActivity theStub = ((TestCaseActivity)theActivity).clone();
		theStub.resetDescription();
		theStub.setDescriptionId(theActivity.getId());
		return theStub;
	}

	public Iterator<Filter> iterator() {
		return this.filters.iterator();
	}

	public void addFilter(Filter f) {
		this.filters.add(f);
	}

	public UserEvent createEvent (String type) {
		return createEvent (null, type);
	}

	public UserEvent createEvent (WidgetState target, String type) {
		TestCaseEvent newEvent = TestCaseEvent.createEvent(getTheSession());
		if (target == null) {
			target = TestCaseWidget.createWidget(getTheSession());
			target.setType("null");
			target.setId("-1");
			target.setSimpleType("null");
			target.setUniqueId("w0");
			newEvent.setWidget (target);
		} else {
			newEvent.setWidget (target.clone());
		}
		newEvent.setType(type);
		newEvent.setId(getUniqueEventId());
		for (AbstractorListener listener: this.theListeners) {
			listener.onNewEvent(newEvent);
		}
		return newEvent;
	}

	public UserInput createInput(WidgetState target, String value, String type) {
		TestCaseInput newInput = TestCaseInput.createInput(getTheSession());
		newInput.setWidget (target.clone());
		newInput.setValue(value);
		newInput.setType(type);
		newInput.setId(getUniqueInputId());
		for (AbstractorListener listener: this.theListeners) {
			listener.onNewInput(newInput);
		}
		return newInput;
	}

	public Trace createTrace(Trace head, Transition tail) {
		TestCaseTrace t;
		if (head!= null) {
			t = ((TestCaseTrace)head).clone();
		} else {
			t = new TestCaseTrace (getTheSession());
		}
		t.addTransition(tail);
		return t;
	}
	
	public Trace importTask (Element fromXml) {
		TestCaseTrace imported = new TestCaseTrace (getTheSession());
		Element task = (Element)getTheSession().getDom().adoptNode(fromXml);
		imported.setElement(task);
		return imported;
	}

	public ActivityState importState (Element fromXml) {
		return getTheSession().importState(fromXml);
	}

	public Transition createStep (ActivityState start, Collection<UserInput> inputs, UserEvent event) {
		Transition t = TestCaseTransition.createTransition(start.getElement().getOwnerDocument());
		try {
			setStartActivity(t, StartActivity.createActivity(start));
			for (UserInput inPut: inputs) {
				t.addInput(inPut);
			}
			t.setEvent (event);
		}
		catch (DOMException e) {
			Log.i(TAG, "Abstractor->createStep(activity): " + ((e.code==DOMException.HIERARCHY_REQUEST_ERR)?"HIERARCHY_REQUEST_ERR":String.valueOf(e.code)));
		}
		return t;
	}
	
	public void registerListener(AbstractorListener theListener) {
		this.theListeners.add(theListener);
	}
	
	public String getUniqueEventId () {
		int ret = this.eventId;
		this.eventId++;
		return "e" + ret;
	}

	public String getUniqueActivityId () {
		this.activityId++;
		return "a" + this.activityId;
	}

	public String getUniqueInputId () {
		int ret = this.inputId;
		this.inputId++;
		return "i" + ret;
	}

	public String getUniqueWidgetId () {
		int ret = this.widgetId;
		this.widgetId++;
		return "w" + ret;
	}

	public SessionParams onSavingState() {
		SessionParams state = new SessionParams();
		state.store(EVENT_PARAM_NAME, this.eventId);
		state.store(INPUT_PARAM_NAME, this.inputId);
		state.store(ACTIVITY_PARAM_NAME, this.activityId);
		state.store(WIDGET_PARAM_NAME, this.widgetId);
		return state;
	}
	
	public void onLoadingState(SessionParams sessionParams) {
		this.eventId = sessionParams.getInt(EVENT_PARAM_NAME);
		this.inputId = sessionParams.getInt(INPUT_PARAM_NAME);
		this.activityId = sessionParams.getInt(ACTIVITY_PARAM_NAME);
		this.widgetId = sessionParams.getInt(WIDGET_PARAM_NAME);
		Log.d(TAG, "Restored abstractor counters to: event = " + eventId + " - input = " + inputId + " - activity = " + activityId);
	}

	public String getListenerName() {
		return ACTOR_NAME;
	}

}