package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.CLICK;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.InteractorAdapter;

import com.nofatclips.androidtesting.model.WidgetState;

public class Clicker extends InteractorAdapter {

	public Clicker (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public Clicker (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}
	
	public boolean canUseWidget (WidgetState w) {
		return (w.isClickable() && super.canUseWidget(w));
	}
	
	@Override
	public String getInteractionType () {
		return CLICK;
	}

}
