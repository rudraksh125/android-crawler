package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.SimpleType.EDIT_TEXT;

import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.InteractorAdapter;
import it.unina.androidripper.planning.interactors.values_cache.ValuesCache;

import java.util.List;

import android.util.Log;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.dictionary.TestValuesDictionary;

public class DictionaryValueEditor extends InteractorAdapter {

	public DictionaryValueEditor () {
		this (EDIT_TEXT);
	}
	
	public DictionaryValueEditor(Abstractor theAbstractor, String... simpleTypes) {
		super(theAbstractor, simpleTypes);
	}

	public DictionaryValueEditor(String... simpleTypes) {
		super(simpleTypes);
	}

	/*
	 * NOTA: prima il valore "errato" e poi il valore corretto
	 * poiche' il comportamento normale del planner e' prendere
	 * il valore size() - 1 come input corrente
	 * 
	 * RegExSimplePlanner invece considera l'indice 0
	 * come valore errato
	 */
	public String[] getValues(WidgetState w)
	{
		String[] values = null;
		
		if (	it.unina.androidripper.planning.Resources.DICTIONARY_FIXED_VALUE
			&&	w.getId() != null
			&& 	w.getId().equals("") == false
			)
		{
			Log.i("androidripper", "DictionaryValueWriter: Using values from cache");
			ValuesCache vCache = ValuesCache.getInstance(); 
			
			if (vCache != null)
				values = vCache.get(w.getId());
			else
				throw new RuntimeException("ValuesCache not found!");
		}
		
		//anche se non e' presente nella cache sara' null
		if (values == null)
		{
			Log.i("androidripper", "DictionaryValueEditor: Generating new values");
			
			if (it.unina.androidripper.planning.Resources.DICTIONARY_IGNORE_CONTENT_TYPES)
			{
				values = TestValuesDictionary.getRandomMixedValues(w);
			}
			else
			{
				values = TestValuesDictionary.getValues(w, false);
			}
		}

		//se necessario aggiungo alla cache
		if (	it.unina.androidripper.planning.Resources.DICTIONARY_FIXED_VALUE
				&&	w.getId() != null
				&& 	w.getId().equals("") == false
				)
		{
			Log.i("androidripper", "DictionaryValueWriter: Saving values to cache");
			ValuesCache vCache = ValuesCache.getInstance(); 
			
			if (vCache != null)
				vCache.put(w.getId(), values);
			else
				throw new RuntimeException("ValuesCache not found!");
		}
		
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
		return EDIT_TEXT;
	}
}
