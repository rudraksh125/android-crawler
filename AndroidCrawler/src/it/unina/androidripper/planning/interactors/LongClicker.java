package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.LONG_CLICK;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.InteractorAdapter;

import com.nofatclips.androidtesting.model.WidgetState;

public class LongClicker extends InteractorAdapter {
	
	public LongClicker (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public LongClicker (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public boolean canUseWidget (WidgetState w) {
		return (w.isLongClickable() && super.canUseWidget(w));
	}

	@Override
	public String getInteractionType () {
		return LONG_CLICK;
	}

}
