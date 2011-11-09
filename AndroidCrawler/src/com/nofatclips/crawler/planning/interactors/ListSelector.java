package com.nofatclips.crawler.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.*;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.crawler.model.Abstractor;
import com.nofatclips.crawler.planning.InteractorAdapter;

public class ListSelector extends InteractorAdapter {
	
	private int maxEventsPerWidget;

	public ListSelector (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public ListSelector (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public ListSelector (Abstractor theAbstractor, int maxItems, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMaxEventsPerWidget(maxItems);
	}

	@Override
	public List<UserEvent> getEvents (WidgetState w) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(w)) {
//			Log.d("nofatclips", "Handling event '" + getEventType() + "' on widget id=" + w.getId() + " type=" + w.getSimpleType() + " name=" + w.getName());
//			UserEvent event = getAbstractor().createEvent(w, getEventType());
//			events.add(event);
			final int fromItem = 1; // int fromItem = Math.min(6,w.getCount());
			final int toItem = Math.min (fromItem + getMaxEventsPerWidget() - 1, w.getCount());
			
			Log.d("nofatclips", "Handling events [" + fromItem + "," + toItem + "] on List #" + w.getId() + " count=" + w.getCount() + " name=" + w.getName());
//			UserEvent event;
			for (int i=fromItem; i<=toItem; i++) {
//				event = getAbstractor().createEvent(w, LIST_SELECT);
//				event.setValue(String.valueOf(i));
				events.add(generateEvent(w, String.valueOf(i)));
//				if (longClickListEvent()) {
//					event = getAbstractor().createEvent(w, LIST_LONG_SELECT);
//					event.setValue(String.valueOf(i));
//					events.add(event);
//				}
			}
		}
		return events;
	}

	@Override
	public String getEventType () {
		return LIST_SELECT;
	}

	public int getMaxEventsPerWidget() {
		return maxEventsPerWidget;
	}

	public void setMaxEventsPerWidget(int maxEventsPerWidget) {
		this.maxEventsPerWidget = maxEventsPerWidget;
	}

}
