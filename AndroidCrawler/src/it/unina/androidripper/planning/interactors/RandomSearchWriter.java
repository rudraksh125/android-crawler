package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.SimpleType.SEARCH_BAR;
import static com.nofatclips.androidtesting.model.InteractionType.SEARCH_TEXT;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.RandomInteractorAdapter;


public class RandomSearchWriter extends RandomInteractorAdapter {
	
	public RandomSearchWriter () {
		this (SEARCH_BAR);
	}
	
	public RandomSearchWriter (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public RandomSearchWriter (Abstractor theAbstractor) {
		this ();
		setAbstractor(theAbstractor);
	}
	
	public RandomSearchWriter (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public RandomSearchWriter (Abstractor theAbstractor, int minValue) {
		this (theAbstractor);
		setMin(minValue);
	}

	public RandomSearchWriter (Abstractor theAbstractor, int minValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
	}

	public RandomSearchWriter (Abstractor theAbstractor, int minValue, int maxValue) {
		this (theAbstractor);
		setMin(minValue);
		setMax(maxValue);
	}

	public RandomSearchWriter (Abstractor theAbstractor, int minValue, int maxValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
		setMax(maxValue);
	}

	@Override
	public String getInteractionType() {
		return SEARCH_TEXT;
	}

}
