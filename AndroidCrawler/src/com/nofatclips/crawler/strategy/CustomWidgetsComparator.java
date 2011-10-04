package com.nofatclips.crawler.strategy;

import android.util.Log;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.WidgetState;

// Accetta in input (nel costruttore) un numero arbitrario di tipi di widget e compara le activity
// considerandone il nome ed i widget dei tipi selezionati. Una activity A sarà diversa da B se il
// nome di A è diverso da quello di B, o se fra i widget dei tipi selezionati posseduti da A ce
// n'è almeno uno che B non possiede.
// Sono presi in considerazione solo i widget dotati di ID.

// Es: Comparator COMPARATOR = new CustomWidgetsComparator("button", "editText");

public class CustomWidgetsComparator extends NameComparator {
	
	public CustomWidgetsComparator (String... widgets) {
		super ();
		this.widgetClasses = widgets; 
	}
	
	public boolean matchClass (String type) {
		for (String storedType: this.widgetClasses) {
			if (storedType.equalsIgnoreCase(type)) return true;
		}
		return false;
	}

	public boolean compare(ActivityState current, ActivityState stored) {
		if (!super.compare(current,stored)) return false; // Different name, different activity
		for (WidgetState campo: current) {
			int id = Integer.valueOf(campo.getId());
			String type = campo.getSimpleType();
			Log.d("nofatclips","Comparator found widget " + id + " (type = " + type + ")");
			
			if (matchClass(campo.getSimpleType()) && (id>0) ) {
				Log.v("nofatclips","Comparing " + type + " #" + id);
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
	
	String[] widgetClasses;
	
}
