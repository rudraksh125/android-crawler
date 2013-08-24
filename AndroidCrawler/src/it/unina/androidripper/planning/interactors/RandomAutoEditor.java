package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.SimpleType.AUTOC_TEXT;
import static com.nofatclips.androidtesting.model.InteractionType.AUTO_TEXT;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.RandomInteractorAdapter;

public class RandomAutoEditor extends RandomInteractorAdapter {

	public RandomAutoEditor () {
		this (AUTOC_TEXT);
	}
	
	public RandomAutoEditor (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public RandomAutoEditor (Abstractor theAbstractor) {
		this ();
		setAbstractor(theAbstractor);
	}
	
	public RandomAutoEditor (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public RandomAutoEditor (Abstractor theAbstractor, int minValue) {
		this (theAbstractor);
		setMin(minValue);
	}

	public RandomAutoEditor (Abstractor theAbstractor, int minValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
	}

	public RandomAutoEditor (Abstractor theAbstractor, int minValue, int maxValue) {
		this (theAbstractor);
		setMin(minValue);
		setMax(maxValue);
	}

	public RandomAutoEditor (Abstractor theAbstractor, int minValue, int maxValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
		setMax(maxValue);
	}

	public String getInteractionType() {
		return AUTO_TEXT;
	}
	
}
