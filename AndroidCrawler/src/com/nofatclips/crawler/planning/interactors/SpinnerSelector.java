package com.nofatclips.crawler.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.SPINNER_SELECT;
import static com.nofatclips.androidtesting.model.SimpleType.SPINNER;

import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.planning.adapters.IterativeInteractorAdapter;

public class SpinnerSelector extends IterativeInteractorAdapter {
	
	public SpinnerSelector () {
		this (SPINNER);
	}
	
	public SpinnerSelector (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public SpinnerSelector (int maxItems) {
		this(maxItems, SPINNER);
	}
	
	public SpinnerSelector (Abstractor theAbstractor) {
		this (theAbstractor, SPINNER);
	}

	public SpinnerSelector (int maxItems, String ... simpleTypes) {
		super (maxItems, simpleTypes);
	}

	public SpinnerSelector (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public SpinnerSelector (Abstractor theAbstractor, int maxItems) {
		this (theAbstractor, maxItems, SPINNER);
	}

	public SpinnerSelector (Abstractor theAbstractor, int maxItems, String ... simpleTypes) {
		super (theAbstractor, maxItems, simpleTypes);
	}

	public String getInteractionType () {
		return SPINNER_SELECT;
	}

}