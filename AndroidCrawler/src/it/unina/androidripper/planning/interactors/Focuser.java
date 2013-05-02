package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.FOCUS;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.InteractorAdapter;

import com.nofatclips.androidtesting.model.WidgetState;

public class Focuser extends InteractorAdapter {
	public Focuser (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public Focuser (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}
	
	public boolean canUseWidget (WidgetState w) {
		return (w.isClickable() && super.canUseWidget(w));
	}
	
	@Override
	public String getInteractionType () {
		return FOCUS;
	}
}
