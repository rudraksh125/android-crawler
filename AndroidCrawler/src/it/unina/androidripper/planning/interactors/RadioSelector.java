package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.RADIO_SELECT;
import static com.nofatclips.androidtesting.model.SimpleType.RADIO_GROUP;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.IterativeInteractorAdapter;


public class RadioSelector extends IterativeInteractorAdapter {
	
	public RadioSelector () {
		this (RADIO_GROUP);
	}
	
	public RadioSelector (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public RadioSelector (int maxItems) {
		this(maxItems, RADIO_GROUP);
	}
	
	public RadioSelector (Abstractor theAbstractor) {
		this (theAbstractor, RADIO_GROUP);
	}

	public RadioSelector (int maxItems, String ... simpleTypes) {
		super (maxItems, simpleTypes);
	}

	public RadioSelector (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public RadioSelector (Abstractor theAbstractor, int maxItems) {
		this (theAbstractor, maxItems, RADIO_GROUP);
	}

	public RadioSelector (Abstractor theAbstractor, int maxItems, String ... simpleTypes) {
		super (theAbstractor, maxItems, simpleTypes);
	}

	public String getInteractionType () {
		return RADIO_SELECT;
	}

}