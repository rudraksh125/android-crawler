package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.SEARCH_TEXT;

import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.InteractorAdapter;
import it.unina.androidripper.planning.interactors.values_cache.ValuesCache;

import java.util.List;

import android.util.Log;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.dictionary.TestValuesDictionary;

public class DictionaryValueSearchWriter extends InteractorAdapter {

	public DictionaryValueSearchWriter () {
		this (SEARCH_TEXT);
	}
	
	public DictionaryValueSearchWriter(Abstractor theAbstractor, String... simpleTypes) {
		super(theAbstractor, simpleTypes);
	}

	public DictionaryValueSearchWriter(String... simpleTypes) {
		super(simpleTypes);
	}

	public String[] getValues(WidgetState w)
	{
		String[] values = null;
		
		if (	it.unina.androidripper.planning.Resources.DICTIONARY_FIXED_VALUE
			&&	w.getId() != null
			&& 	w.getId().equals("") == false
			)
		{
			Log.i("androidripper", "DictionaryValueSearchWriter: Using values from cache");
			ValuesCache vCache = ValuesCache.getInstance(); 
			
			if (vCache != null)
			{
				values = vCache.get(w.getId());
			}
			else
			{
				throw new RuntimeException("ValuesCache error!");
			}
		}
		
		if (values == null)
		{
			Log.i("androidripper", "DictionaryValueSearchWriter: Generating new values");
			
			if (it.unina.androidripper.planning.Resources.DICTIONARY_IGNORE_CONTENT_TYPES)
			{
				values = TestValuesDictionary.getRandomMixedValues(w);
			}
			else
			{
				values = TestValuesDictionary.getValues(w, false);
			}
		}
		
		if (	it.unina.androidripper.planning.Resources.DICTIONARY_FIXED_VALUE
				&&	w.getId() != null
				&& 	w.getId().equals("") == false
				)
		{			
			Log.i("androidripper", "DictionaryValueSearchWriter: Saving values to cache");
			ValuesCache vCache = ValuesCache.getInstance(); 
			
			if (vCache != null)
				vCache.put(w.getId(), values);
			else
				throw new RuntimeException("ValuesCache error!");
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
		return SEARCH_TEXT;
	}
}
