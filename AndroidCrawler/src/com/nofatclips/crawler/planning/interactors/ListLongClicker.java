package com.nofatclips.crawler.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.*;

import com.nofatclips.crawler.model.Abstractor;

public class ListLongClicker extends ListSelector {
	
	public ListLongClicker (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public ListLongClicker (Abstractor theAbstractor) {
		super (theAbstractor);
	}
	
	public ListLongClicker (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}
	
	public ListLongClicker (Abstractor theAbstractor, int maxItems) {
		super (theAbstractor, maxItems);
	}
	
	public ListLongClicker (Abstractor theAbstractor, int maxItems, String ... simpleTypes) {
		super (theAbstractor, maxItems, simpleTypes);
	}

	@Override
	public String getEventType () {
		return LIST_LONG_SELECT;
	}

}
