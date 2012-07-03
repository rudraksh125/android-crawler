package com.nofatclips.crawler.guitree;

import java.util.Random;

import android.util.Log;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.crawler.model.Plan;

import static com.nofatclips.crawler.Resources.*;
import static com.nofatclips.androidtesting.model.InteractionType.*;

public class RandomEngine extends GuiTreeEngine {

	Random taskLottery;
	boolean first;
	
	public RandomEngine () {
		super();
		this.theStrategyFactory.setExploreNewOnly(false);
		Log.d("nofatclips", "Starting random testing");
		this.taskLottery = new Random(RANDOM_SEED);
		this.first = true;
	}
	
	@Override
	protected void planTests (Trace theTask, Plan thePlan) {
		int max = thePlan.size();
		if (max==0) return;
		
		int n;
		Transition t;
		do {
			n = getRandom(max);
			Log.e("nofatclips","Estratto: " + n + " su " + (max-1));
			t = thePlan.getTask(n);
		} while (isBase() && t.getEvent().getType().equals(BACK));
		
		getScheduler().addTasks(getTask(theTask, t));		
	}
	
	public boolean isBase () {
		return isBase (getStrategy().getStateAfterEvent());
	}
	
	public boolean isBase (ActivityState s) {
		if (s==null) return true;
		return s.getId().equals(getAbstractor().getBaseActivity().getId());
	}
	
	public int getRandom (int max) {
		if (this.first) {
			for (int i = 0; i<getLastId(); i++) {
				taskLottery.nextInt(max);
			}
		}
		this.first = false;
		int n = taskLottery.nextInt(max);
		return n;
	}

}
