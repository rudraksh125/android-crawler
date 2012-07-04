package com.nofatclips.crawler.guitree;

import java.util.Random;

import android.util.Log;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.crawler.model.Plan;
import com.nofatclips.crawler.planning.TraceDispatcher;
import com.nofatclips.crawler.strategy.criteria.MaxDepthTermination;
import com.nofatclips.crawler.strategy.criteria.OnExitPause;

import static com.nofatclips.crawler.Resources.*;
import static com.nofatclips.androidtesting.model.InteractionType.*;
import static com.nofatclips.crawler.planning.TraceDispatcher.SchedulerAlgorithm.DEPTH_FIRST;

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
	protected void setUp () {
		super.setUp();
		this.theStrategyFactory.setMoreCriterias(new OnExitPause(), new MaxDepthTermination(TRACE_MAX_DEPTH));
	}
	
	@Override
	protected void planTests (Trace theTask, Plan thePlan) {
		int n;
		int max;
		Transition t;
		String type;
		while (!thePlan.isEmpty()) {
			max = thePlan.size();
			n = getRandom(max);
			t = thePlan.getTask(n);
			type = t.getEvent().getType();
			Log.v("nofatclips","Estratto: " + (n+1) + " su " + max + " (di tipo " + type + ")");
			if (!(isBase() && type.equals(BACK))) {
				getScheduler().addTasks(getNewTask(theTask, t));
			}
			thePlan.removeTask(n);
		}
		
//		getScheduler().addTasks(getNewTask(theTask, t));		
	}

	@Override
	protected void doNotPlanTests() { 
		this.first = true; 
	}

	
	@Override
	public TraceDispatcher getNewScheduler() {
		return new TraceDispatcher(DEPTH_FIRST);
	}

	@Override
	protected void process(Trace theTask) {
		if (this.first) {
			super.process(theTask);
		} else {
			getRobot().process(theTask.getFinalTransition());
		}
		this.first=false;
	}

	public boolean isBase () {
		return isBase (getStrategy().getStateAfterEvent());
	}
	
	public boolean isBase (ActivityState s) {
		if (s==null) return true;
		return s.getId().equals(getAbstractor().getBaseActivity().getId());
	}
	
	public int getRandom (int max) {
//		if (this.first) {
//			for (int i = 0; i<getLastId(); i++) {
//				taskLottery.nextInt(max);
//			}
//		}
//		this.first = false;
		int n = taskLottery.nextInt(max);
		return n;
	}

}
