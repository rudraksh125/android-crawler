package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.SimpleType.AUTOC_TEXT;
import static com.nofatclips.androidtesting.model.SimpleType.EDIT_TEXT;
import static com.nofatclips.androidtesting.model.SimpleType.FOCUSABLE_EDIT_TEXT;
import static com.nofatclips.androidtesting.model.SimpleType.SEARCH_BAR;
import static com.nofatclips.androidtesting.model.InteractionType.ENTER_TEXT;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.RandomInteractorAdapter;

public class RandomEnterWriter extends RandomInteractorAdapter {
	
	public RandomEnterWriter () {
		this (EDIT_TEXT, AUTOC_TEXT, SEARCH_BAR, FOCUSABLE_EDIT_TEXT);
	}
	
	public RandomEnterWriter (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public RandomEnterWriter (Abstractor theAbstractor) {
		this ();
		setAbstractor(theAbstractor);
	}
	
	public RandomEnterWriter (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public RandomEnterWriter (Abstractor theAbstractor, int minValue) {
		this (theAbstractor);
		setMin(minValue);
	}

	public RandomEnterWriter (Abstractor theAbstractor, int minValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
	}

	public RandomEnterWriter (Abstractor theAbstractor, int minValue, int maxValue) {
		this (theAbstractor);
		setMin(minValue);
		setMax(maxValue);
	}

	public RandomEnterWriter (Abstractor theAbstractor, int minValue, int maxValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
		setMax(maxValue);
	}

	@Override
	public String getInteractionType() {
		return ENTER_TEXT;
	}

}
