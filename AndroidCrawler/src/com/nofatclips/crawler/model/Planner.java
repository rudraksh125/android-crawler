package com.nofatclips.crawler.model;

import com.nofatclips.androidtesting.model.ActivityState;

public interface Planner {
	
	public Plan getPlanForActivity (ActivityState a);
	public Plan getPlanForActivity (ActivityState a, int numberOfTabs);
	public Plan getPlanForBaseActivity (ActivityState a, int numberOfTabs);

}
