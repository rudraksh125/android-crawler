package com.nofatclips.crawler.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.LIST_SELECT;
import static com.nofatclips.androidtesting.model.SimpleType.LIST_VIEW;

import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.planning.adapters.IterativeInteractorAdapter;

public class ListSelector extends IterativeInteractorAdapter {
	
	public ListSelector () {
		this (LIST_VIEW);
	}
	
	public ListSelector (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public ListSelector (int maxItems) {
		this(maxItems, LIST_VIEW);
	}
	
	public ListSelector (Abstractor theAbstractor) {
		this (theAbstractor, LIST_VIEW);
	}

	public ListSelector (int maxItems, String ... simpleTypes) {
		super (maxItems, simpleTypes);
	}

	public ListSelector (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public ListSelector (Abstractor theAbstractor, int maxItems) {
		this (theAbstractor, maxItems, LIST_VIEW);
	}

	public ListSelector (Abstractor theAbstractor, int maxItems, String ... simpleTypes) {
		super (theAbstractor, maxItems, simpleTypes);
	}

//	@Override
//	public List<UserEvent> getEvents (WidgetState w) {
//		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
//		if (canUseWidget(w)) {
//			final int fromItem = 1; // int fromItem = Math.min(6,w.getCount());
//			final int toItem = getMax(fromItem, w.getCount()); //Math.min (fromItem + getMaxEventsPerWidget() - 1, w.getCount());
//			if (toItem<fromItem) return events;
//			Log.d("nofatclips", "Handling events [" + fromItem + "," + toItem + "] on List #" + w.getId() + " count=" + w.getCount() + " name=" + w.getName());
//			for (int i=fromItem; i<=toItem; i++) {
//				events.add(generateEvent(w, String.valueOf(i)));
//			}
//		}
//		return events;
//	}

	@Override
	public String getEventType () {
		return LIST_SELECT;
	}

//	public int getMaxEventsPerWidget() {
//		return maxEventsPerWidget;
//	}
//	
//	public int getMax(int min, int max) {
//		return (getMaxEventsPerWidget()>0)?Math.min (min + getMaxEventsPerWidget() - 1, max):max;
//	}
//
//	public void setMaxEventsPerWidget(int maxEventsPerWidget) {
//		this.maxEventsPerWidget = maxEventsPerWidget;
//	}

}
