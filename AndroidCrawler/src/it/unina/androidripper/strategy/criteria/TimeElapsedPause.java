package it.unina.androidripper.strategy.criteria;

import it.unina.androidripper.model.Strategy;
import android.os.SystemClock;
import android.util.Log;


public class TimeElapsedPause implements PauseCriteria {
	
	private long max;
	private long start;
	
	public TimeElapsedPause () {
		this(3600);
	}
	
	public TimeElapsedPause (long maxTime) {
		this.max = maxTime;
		this.start = SystemClock.uptimeMillis();
	}
	
	public boolean pause () {
		long current = (SystemClock.uptimeMillis()-this.start)/1000;
		Log.i ("androidripper", "Check for pause. Time elapsed: " + current + "s; time limit: " + this.max + "s");
		return (current>=max);
	}
	
	public void setStrategy(Strategy theStrategy) {}

}