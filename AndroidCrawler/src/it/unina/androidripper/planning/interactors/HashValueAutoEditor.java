package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.AUTO_TEXT;

import it.unina.androidripper.helpers.HashGenerator;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.InteractorAdapter;

import java.util.List;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;

public class HashValueAutoEditor extends InteractorAdapter {

	public HashValueAutoEditor () {
		this (AUTO_TEXT);
	}
	
	public HashValueAutoEditor(Abstractor theAbstractor, String... simpleTypes) {
		super(theAbstractor, simpleTypes);
	}

	public HashValueAutoEditor(String... simpleTypes) {
		super(simpleTypes);
	}

	public String[] getValues(WidgetState w)
	{
		String[] values = new String[1];		
		values[0] = HashGenerator.generateFromString(w.getId());
		return values;
	}
	
	@Override
	public List<UserEvent> getEvents (WidgetState w) {
		return getEvents (w, getValues(w));
	}

	@Override
	public List<UserInput> getInputs (WidgetState w) {
		return getInputs (w, getValues(w));
	}
	
	@Override
	public String getInteractionType() {
		return AUTO_TEXT;
	}
	
}
