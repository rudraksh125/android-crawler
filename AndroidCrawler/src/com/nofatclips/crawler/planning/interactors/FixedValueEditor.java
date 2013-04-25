//package com.nofatclips.crawler.planning.interactors;
//
//import static com.nofatclips.androidtesting.model.InteractionType.WRITE_TEXT;
//import static com.nofatclips.androidtesting.model.SimpleType.EDIT_TEXT;
//
//import java.util.Hashtable;
//import java.util.Map;
//
//import com.nofatclips.androidtesting.model.UserEvent;
//import com.nofatclips.androidtesting.model.UserInput;
//import com.nofatclips.androidtesting.model.WidgetState;
//import com.nofatclips.crawler.planning.adapters.InteractorAdapter;
//
//public class FixedValueEditor extends InteractorAdapter {
//
//	private Map<String,String> idValuePairs = new Hashtable<String,String>();
//	
//	public FixedValueEditor () {
//		this (EDIT_TEXT);
//	}
//	
//	public FixedValueEditor (String ... simpleTypes) {
//		super (simpleTypes);
//	}
//	
//	@Override
//	public boolean canUseWidget(WidgetState w) {
//		if (super.canUseWidget(w)) {
//			for (Map.Entry<String,String> c: this.idValuePairs.entrySet()) {
//				if (w.getId().equals(c.getKey())) return true;
//			}
//		}
//		return false;
//	}
//
//	@Override
//	protected UserEvent generateEvent(WidgetState w) {
//		return super.generateEvent(w, this.idValuePairs.get(w.getId()));
//	}
//
//	@Override
//	protected UserInput generateInput(WidgetState w) {
//		return super.generateInput(w, this.idValuePairs.get(w.getId()));
//	}
//
//	public String getInteractionType() {
//		return WRITE_TEXT;
//	}
//	
//	public void setIdValuePairs (Map<String,String> pairs) {
//		this.idValuePairs = pairs;
//	}
//	
//	public FixedValueEditor addIdValuePair (String id, String value) {
//		this.idValuePairs.put(id, value);
//		return this;
//	}
//
//	public FixedValueEditor addIdValuePair (int id, String value) {
//		return addIdValuePair(String.valueOf(id), value);
//	}
//
//}


package com.nofatclips.crawler.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.WRITE_TEXT;
import static com.nofatclips.androidtesting.model.SimpleType.EDIT_TEXT;
import static com.nofatclips.androidtesting.model.SimpleType.SEARCH_BAR;
import static com.nofatclips.androidtesting.model.SimpleType.TEXT_VIEW;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.crawler.planning.adapters.InteractorAdapter;

public class FixedValueEditor extends InteractorAdapter {

	private Map<String,ArrayList<String>> idValuePairs = new Hashtable<String,ArrayList<String>>();
	
	public FixedValueEditor () {
		this (EDIT_TEXT, SEARCH_BAR, TEXT_VIEW);
	}
	
	public FixedValueEditor (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	@Override
	public boolean canUseWidget(WidgetState w) {
//		if (super.canUseWidget(w)) {
//			for (Map.Entry<String,ArrayList<String>> c: this.idValuePairs.entrySet()) {
//				if (w.getId().equals(c.getKey())) return true;
//			}
//		}
//		return false;
		return (super.canUseWidget(w) && hasId(w.getId()));
	}
	
	public boolean hasId (String id) {
		for (Map.Entry<String,ArrayList<String>> c: this.idValuePairs.entrySet()) {
			if (id.equals(c.getKey())) return true;
		}
		return false;
	}

	@Override
	public List<UserEvent> getEvents (WidgetState w) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(w)) {
			ArrayList<String> values = this.idValuePairs.get(w.getId());
			for (String value: values) {
				Log.d("nofatclips", "Handling event '" + getInteractionType() + "' on widget id=" + w.getId() + " value=" + value );
				events.add(generateEvent(w, value));
			}
		}
		return events;
	}

	@Override
	public List<UserInput> getInputs (WidgetState w) {
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		if (canUseWidget(w)) {
			ArrayList<String> values = this.idValuePairs.get(w.getId());
			for (String value: values) {
				Log.d("nofatclips", "Handling input '" + getInteractionType() + "' on widget id=" + w.getId() + " value=" + value );
				inputs.add(generateInput(w, value));
			}
		}
		return inputs;
	}

//	@Override
//	protected UserEvent generateEvent(WidgetState w) {
//		return super.generateEvent(w, this.idValuePairs.get(w.getId()));
//	}
//
//	@Override
//	protected UserInput generateInput(WidgetState w) {
//		return super.generateInput(w, this.idValuePairs.get(w.getId()));
//	}

	public String getInteractionType() {
		return WRITE_TEXT;
	}
	
	public void setIdValuePairs (Map<String,ArrayList<String>> pairs) {
		this.idValuePairs = pairs;
	}
	
	public FixedValueEditor addIdValuePair (String id, String ... values) {
		ArrayList<String> valuesForId;
		if (!hasId(id)) {
			valuesForId = new ArrayList<String>();
			this.idValuePairs.put (id, valuesForId);
		} else {
			valuesForId = this.idValuePairs.get (id);
		}
		for (String value: values) {
			valuesForId.add(value);			
		}
//		this.idValuePairs.put(id, value);
		return this;
	}

	public FixedValueEditor addIdValuePair (int id, String ... value) {
		return addIdValuePair(String.valueOf(id), value);
	}

}
