package it.unina.androidripper.strategy.comparator;

import it.unina.androidripper.model.StatelessComparator;

import com.nofatclips.androidtesting.model.ActivityState;
import static com.nofatclips.androidtesting.model.SimpleType.NULL;

public class NullComparator implements StatelessComparator {

	@Override
	public boolean compare(ActivityState a, ActivityState b) {
		return false;
	}
	
	public String describe() {
		return NULL;
	}

}
