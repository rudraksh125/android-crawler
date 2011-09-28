package com.nofatclips.crawler;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.WidgetState;
import android.util.Log;

public class EditTextComparator extends NameComparator {
	
	public boolean compare(ActivityState current, ActivityState stored) {
		if (!super.compare(current,stored)) return false; // Different name, different activity
		for (WidgetState campo: current) {
			int id = Integer.valueOf(campo.getId());
			Log.d("nofatclips","Found widget " + id + " (type = " + campo.getSimpleType() + ")");
			if (campo.getSimpleType().equals("editText") && (id>0) ) {
				Log.v("nofatclips","Comparing editText " + id);
				boolean trovato = false;
				for (WidgetState altroCampo: stored) {
					if (altroCampo.getId().equals(campo.getId())) {
						trovato = true;
						break;
					}
				}
				if (!trovato) return false;
			}
		}
		return true;
	}

}
