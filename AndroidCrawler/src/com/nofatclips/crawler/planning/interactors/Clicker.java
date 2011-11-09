package com.nofatclips.crawler.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.CLICK;

import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.planning.InteractorAdapter;

public class Clicker extends InteractorAdapter {

	public Clicker (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public Clicker (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}
	
	@Override
	public boolean canUseWidget (WidgetState w) {
		return (w.isClickable() && super.canUseWidget(w));
	}
	
	@Override
	public String getEventType () {
		return CLICK;
	}

}
