package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.SimpleType.AUTOC_TEXT;
import static com.nofatclips.androidtesting.model.SimpleType.EDIT_TEXT;
import static com.nofatclips.androidtesting.model.SimpleType.FOCUSABLE_EDIT_TEXT;
import static com.nofatclips.androidtesting.model.SimpleType.SEARCH_BAR;
import static com.nofatclips.androidtesting.model.InteractionType.TYPE_TEXT;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.RandomInteractorAdapter;

public class RandomEditor extends RandomInteractorAdapter {

	public RandomEditor () {
		this (EDIT_TEXT, AUTOC_TEXT, SEARCH_BAR, FOCUSABLE_EDIT_TEXT);
	}
	
	public RandomEditor (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public RandomEditor (Abstractor theAbstractor) {
		this ();
		setAbstractor(theAbstractor);
	}
	
	public RandomEditor (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public RandomEditor (Abstractor theAbstractor, int minValue) {
		this (theAbstractor);
		setMin(minValue);
	}

	public RandomEditor (Abstractor theAbstractor, int minValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
	}

	public RandomEditor (Abstractor theAbstractor, int minValue, int maxValue) {
		this (theAbstractor);
		setMin(minValue);
		setMax(maxValue);
	}

	public RandomEditor (Abstractor theAbstractor, int minValue, int maxValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
		setMax(maxValue);
	}

	public String getInteractionType() {
		return TYPE_TEXT;
	}
	
}
