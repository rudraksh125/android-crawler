package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.SimpleType.EDIT_TEXT;
import static com.nofatclips.androidtesting.model.InteractionType.WRITE_TEXT;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.RandomInteractorAdapter;


public class RandomWriter extends RandomInteractorAdapter {
	
	public RandomWriter () {
		this (EDIT_TEXT);
	}
	
	public RandomWriter (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public RandomWriter (Abstractor theAbstractor) {
		this ();
		setAbstractor(theAbstractor);
	}
	
	public RandomWriter (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public RandomWriter (Abstractor theAbstractor, int minValue) {
		this (theAbstractor);
		setMin(minValue);
	}

	public RandomWriter (Abstractor theAbstractor, int minValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
	}

	public RandomWriter (Abstractor theAbstractor, int minValue, int maxValue) {
		this (theAbstractor);
		setMin(minValue);
		setMax(maxValue);
	}

	public RandomWriter (Abstractor theAbstractor, int minValue, int maxValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
		setMax(maxValue);
	}

	@Override
	public String getInteractionType() {
		return WRITE_TEXT;
	}

}
