package com.nofatclips.crawler;

import com.nofatclips.androidtesting.model.ActivityState;

public interface Planner {
	
	public Plan getPlanForActivity (ActivityState a);
	public Plan getPlanForActivity (ActivityState a, int numberOfTabs);

}
