package it.unina.androidripper.strategy.comparator;

import it.unina.androidripper.model.Comparator;
import android.util.Log;

import com.nofatclips.androidtesting.model.ActivityState;

import static it.unina.androidripper.Resources.TAG;
import static com.nofatclips.androidtesting.model.SimpleType.NULL;
import static it.unina.androidripper.strategy.comparator.Resources.COMPARE_ACTIVITY_NAME;
import static it.unina.androidripper.strategy.comparator.Resources.COMPARE_STATE_TITLE;

public class NameComparator implements Comparator {

	public NameComparator () {
		this.byName  = COMPARE_ACTIVITY_NAME;
		this.byTitle = COMPARE_STATE_TITLE;
		Log.d(TAG, "Comparation by name is "  + ((this.byName)?"enabled":"disabled"));
		Log.d(TAG, "Comparation by title is " + ((this.byTitle)?"enabled":"disabled"));
	};

	@Override
	public boolean compare(ActivityState a, ActivityState b) {
		if (!(this.byTitle || this.byName)) return true;
		if ((this.byName) && !(a.getName().equals(b.getName()))) return false;
		if ((this.byTitle) && !(a.getTitle().equals(b.getTitle()))) return false;
		return true;
	}
	
	public String describe() {
		return NULL;
	}
	
	protected boolean byName;
	protected boolean byTitle;

}
