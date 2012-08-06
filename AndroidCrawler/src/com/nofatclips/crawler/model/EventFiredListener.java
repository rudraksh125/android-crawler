package com.nofatclips.crawler.model;

import android.view.View;

public interface EventFiredListener {
	
	public void onClickEventFired (View v);
	public void onLongClickEventFired (View v);
	public void onKeyEventFired (int key);

}
