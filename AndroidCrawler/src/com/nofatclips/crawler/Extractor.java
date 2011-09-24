package com.nofatclips.crawler;

import android.view.View;

public interface Extractor {
	
	public ActivityDescription describeActivity();
	public void extractState();
	public View getWidget (int key);
	public int getNumTabs();

}
