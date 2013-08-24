package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.SimpleType.FOCUSABLE_EDIT_TEXT;
import static com.nofatclips.androidtesting.model.InteractionType.FOCUS;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.RandomInteractorAdapter;

public class RandomFocusWriter extends RandomInteractorAdapter {
	
	public RandomFocusWriter () {
		this (FOCUSABLE_EDIT_TEXT);
	}
	
	public RandomFocusWriter (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public RandomFocusWriter (Abstractor theAbstractor) {
		this ();
		setAbstractor(theAbstractor);
	}
	
	public RandomFocusWriter (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public RandomFocusWriter (Abstractor theAbstractor, int minValue) {
		this (theAbstractor);
		setMin(minValue);
	}

	public RandomFocusWriter (Abstractor theAbstractor, int minValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
	}

	public RandomFocusWriter (Abstractor theAbstractor, int minValue, int maxValue) {
		this (theAbstractor);
		setMin(minValue);
		setMax(maxValue);
	}

	public RandomFocusWriter (Abstractor theAbstractor, int minValue, int maxValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
		setMax(maxValue);
	}

	@Override
	public String getInteractionType() {
		return FOCUS;
	}

}
