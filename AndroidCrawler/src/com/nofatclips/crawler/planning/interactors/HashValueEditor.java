package com.nofatclips.crawler.planning.interactors;

import static com.nofatclips.androidtesting.model.SimpleType.EDIT_TEXT;

import java.util.List;

import android.util.Log;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.crawler.helpers.HashGenerator;
import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.planning.adapters.InteractorAdapter;
import com.nofatclips.crawler.planning.interactors.values_cache.ValuesCache;
import com.nofatclips.dictionary.TestValuesDictionary;

@SuppressWarnings("unused")
public class HashValueEditor extends InteractorAdapter {

	public static final String TAG = "HashValueEditor";
	
	public HashValueEditor () {
		this (EDIT_TEXT);
	}
	
	public HashValueEditor(Abstractor theAbstractor, String... simpleTypes) {
		super(theAbstractor, simpleTypes);
	}

	public HashValueEditor(String... simpleTypes) {
		super(simpleTypes);
	}

	public String[] getValues(WidgetState w)
	{
		String[] values = new String[1];		
		values[0] = HashGenerator.generateFromString(w.getId());
		
		Log.v(TAG, "Values :" + values[0]);
		
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
