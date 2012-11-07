package com.nofatclips.crawler.strategy.criteria;

import android.util.Log;

import com.nofatclips.crawler.model.Strategy;

public class MaxDepthPause implements PauseCriteria {
	
	private int maxDepth;
	private Strategy theStrategy;

	public MaxDepthPause () {}
	
	public MaxDepthPause (int maxDepth) {
		setMaxDepth (maxDepth);
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	
	public int getMaxDepth () {
		return this.maxDepth;
	}

	public void setStrategy(Strategy theStrategy) {
		this.theStrategy = theStrategy;
	}

	public boolean pause() {
		int transitions = theStrategy.getDepth();
		Log.i("nofatclips", "Checking for depth: this trace is " + transitions + " transitions deep (max = " + getMaxDepth() + ")");
		return (transitions>=getMaxDepth());
	}

}
