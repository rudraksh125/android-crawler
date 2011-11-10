package com.nofatclips.crawler.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.LONG_CLICK;

import com.nofatclips.crawler.model.Abstractor;

public class LongClicker extends Clicker {
	
	public LongClicker (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public LongClicker (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	@Override
	public String getEventType () {
		return LONG_CLICK;
	}

}
