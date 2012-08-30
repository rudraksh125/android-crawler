package com.nofatclips.crawler.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.WRITE_TEXT;
import static com.nofatclips.androidtesting.model.SimpleType.EDIT_TEXT;

import java.util.List;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.planning.adapters.InteractorAdapter;
import com.nofatclips.dictionary.TestValuesDictionary;

public class DictionaryValueWriter extends InteractorAdapter {

	public DictionaryValueWriter () {
		this (EDIT_TEXT);
	}
	
	public DictionaryValueWriter(Abstractor theAbstractor, String... simpleTypes) {
		super(theAbstractor, simpleTypes);
	}

	public DictionaryValueWriter(String... simpleTypes) {
		super(simpleTypes);
	}

	/*
	 * NOTA: prima il valore "errato" e poi il valore corretto
	 * poiché il comportamento normale del planner è prendere
	 * il valore size() - 1 come input corrente
	 * 
	 * RegExSimplePlanner invece considera l'indice 0
	 * come valore errato
	 */
	public String[] getValues(WidgetState w)
	{
		return TestValuesDictionary.getValues(w, com.nofatclips.crawler.planning.Resources.DICTIONARY_FIXED_VALUE);				
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
		return WRITE_TEXT;
	}
}
