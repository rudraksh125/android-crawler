package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.SEARCH_TEXT;
import static com.nofatclips.androidtesting.model.SimpleType.SEARCH_BAR;
import static com.nofatclips.androidtesting.model.SimpleType.FOCUSABLE_EDIT_TEXT;

import it.unina.androidripper.planning.adapters.InteractorAdapter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;

public class FixedValueSearchEditor extends InteractorAdapter {

	private Map<String,ArrayList<String>> idValuePairs = new Hashtable<String,ArrayList<String>>();
	
	public FixedValueSearchEditor () {
		this (SEARCH_BAR, FOCUSABLE_EDIT_TEXT);
	}
	
	public FixedValueSearchEditor (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	@Override
	public boolean canUseWidget(WidgetState w) {
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
				Log.d("androidripper", "Handling event '" + getInteractionType() + "' on widget id=" + w.getId() + " value=" + value );
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
				Log.d("androidripper", "Handling input '" + getInteractionType() + "' on widget id=" + w.getId() + " value=" + value );
				inputs.add(generateInput(w, value));
			}
		}
		return inputs;
	}

	public String getInteractionType() {
		return SEARCH_TEXT;
	}
	
	public void setIdValuePairs (Map<String,ArrayList<String>> pairs) {
		this.idValuePairs = pairs;
	}
	
	public FixedValueSearchEditor addIdValuePair (String id, String ... values) {
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
		return this;
	}

	public FixedValueSearchEditor addIdValuePair (int id, String ... value) {
		return addIdValuePair(String.valueOf(id), value);
	}

}