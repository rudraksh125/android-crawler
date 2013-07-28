package it.unina.androidripper.guitree;

import it.unina.androidripper.model.Plan;
import it.unina.androidripper.model.SaveStateListener;
import it.unina.androidripper.model.SessionParams;
import it.unina.androidripper.planning.TraceDispatcher;
import it.unina.androidripper.storage.PersistenceFactory;
import it.unina.androidripper.strategy.Resources;
import it.unina.androidripper.strategy.criteria.MaxDepthTermination;
import it.unina.androidripper.strategy.criteria.OnExitPause;

import java.util.Random;

import android.util.Log;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;

import static com.nofatclips.androidtesting.model.InteractionType.*;
import static it.unina.androidripper.Resources.*;
import static it.unina.androidripper.planning.TraceDispatcher.SchedulerAlgorithm.DEPTH_FIRST;

public class RandomEngine extends GuiTreeEngine {

	Random taskLottery;
	boolean first;
	
	public RandomEngine () {
		super();
		this.theStrategyFactory.setExploreNewOnly(false);
		Log.d("androidripper", "Starting random testing");
		this.taskLottery = new SaveStateRandom(RANDOM_SEED);
		this.theStrategyFactory.setMoreCriterias(new OnExitPause());
		this.theStrategyFactory.setPauseTraces(0);
		this.theStrategyFactory.setExploreNewOnly(false);
		addMoreCriteria();
		this.first = true;
	}

	// When Max Depth is reached, exit
	public void addMoreCriteria() {
		if (Resources.TRACE_MAX_DEPTH > 0) {
			this.theStrategyFactory.setMoreCriterias(new MaxDepthTermination(Resources.TRACE_MAX_DEPTH));
		}
	}
	
	@Override
	protected void setUp () {
		super.setUp();
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
			if (!(isBase() && type.equals(BACK))) {
				getScheduler().addTasks(getNewTask(theTask, t));
			}
			thePlan.removeTask(n);
		}
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
			Log.i ("androidripper", "Incrementally Playing Trace " + theTask.getId());
			getRobot().process(theTask.getFinalTransition());
		}
		this.first=false;
	}

	public boolean isBase () {
		return isBase (getStrategy().getStateAfterEvent());
	}
	
	public boolean isBase (ActivityState s) {
		if (s==null) return true;
		return s.getName().equals(getAbstractor().getBaseActivity().getName());
	}
	
	public int getRandom (int max) {
		int n = taskLottery.nextInt(max);
		return n;
	}
	
	@SuppressWarnings("serial")
	class SaveStateRandom extends Random implements SaveStateListener {
		
		public final static String ACTOR_NAME = "RandomEngine";
		private final static String PARAM_NAME = "randomState";
		int count;
		
		public SaveStateRandom (long seed) {
			super (seed);
			count = 0;
			PersistenceFactory.registerForSavingState(this);
		}

		public String getListenerName() {
			return ACTOR_NAME;
		}

		public SessionParams onSavingState() {
			return new SessionParams(PARAM_NAME, this.count);
		}
		
		public void onLoadingState(SessionParams sessionParams) {
			this.count = sessionParams.getInt(PARAM_NAME);
			Log.d("androidripper", "Restored random counter to: " + this.count);
			for (int i=0; i<this.count; i++) {
				nextInt();
			}
		}
		
		public int nextInt (int max) {
			count++;
			return super.nextInt(max);
		}
		
	}

}
