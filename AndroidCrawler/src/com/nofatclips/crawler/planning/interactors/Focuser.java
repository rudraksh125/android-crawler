package com.nofatclips.crawler.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.FOCUS;

import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.planning.adapters.InteractorAdapter;

public class Focuser extends InteractorAdapter {
	public Focuser (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public Focuser (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}
	
	public boolean canUseWidget (WidgetState w) {
		return (w.isClickable() && super.canUseWidget(w));
	}
	
	@Override
	public String getInteractionType () {
		return FOCUS;
	}
}
