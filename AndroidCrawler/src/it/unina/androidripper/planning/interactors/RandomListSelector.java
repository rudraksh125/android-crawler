package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.LIST_SELECT;
import static com.nofatclips.androidtesting.model.SimpleType.LIST_VIEW;

public class RandomListSelector extends RandomSpinnerSelector {

	public RandomListSelector () {
		super (LIST_VIEW);
	}

	public RandomListSelector (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	@Override
	public String getInteractionType() {
		return LIST_SELECT;
	}

}
