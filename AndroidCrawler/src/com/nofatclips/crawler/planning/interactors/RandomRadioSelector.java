package com.nofatclips.crawler.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.RADIO_SELECT;
import static com.nofatclips.androidtesting.model.SimpleType.RADIO_GROUP;

import android.util.Log;

import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.planning.adapters.RandomInteractorAdapter;

public class RandomRadioSelector extends RandomInteractorAdapter {

	public RandomRadioSelector () {
		this (RADIO_GROUP);
	}
	
	public RandomRadioSelector (String ... simpleTypes) {
		super (simpleTypes);
		Log.e("nofatclips", "Sono qui");
	}
	
	public RandomRadioSelector (Abstractor theAbstractor) {
		this ();
		setAbstractor(theAbstractor);
	}
	
	public RandomRadioSelector (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public RandomRadioSelector (Abstractor theAbstractor, int minValue) {
		this (theAbstractor);
		setMin(minValue);
	}

	public RandomRadioSelector (Abstractor theAbstractor, int minValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
	}

	public RandomRadioSelector (Abstractor theAbstractor, int minValue, int maxValue) {
		this (theAbstractor);
		setMin(minValue);
		setMax(maxValue);
	}

	public RandomRadioSelector (Abstractor theAbstractor, int minValue, int maxValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
		setMax(maxValue);
	}
	
	public String getInteractionType() {
		return RADIO_SELECT;
	}

	@Override
	public boolean canUseWidget (WidgetState w) {
//		Log.e("nofatclips","canUseWidget: #" + w.getId() + " (" + w.getSimpleType() + ")");
		if (getMin(w)>getMax(w)) return false;
		return super.canUseWidget(w);
	}
	
	@Override
	public int getMin () {
		return 1;
	}
	
	@Override
	public int getMax(WidgetState w) {
//		Log.e("nofatclips","getMax: #" + w.getId() + ": " + w.getCount());
		return w.getCount();
	}

}
