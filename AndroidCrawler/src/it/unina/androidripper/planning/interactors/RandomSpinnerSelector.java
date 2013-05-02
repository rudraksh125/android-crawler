package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.SPINNER_SELECT;
import static com.nofatclips.androidtesting.model.SimpleType.SPINNER;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.RandomInteractorAdapter;

import com.nofatclips.androidtesting.model.WidgetState;

public class RandomSpinnerSelector extends RandomInteractorAdapter {

	public RandomSpinnerSelector () {
		this (SPINNER);
	}
	
	public RandomSpinnerSelector (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public RandomSpinnerSelector (Abstractor theAbstractor) {
		this ();
		setAbstractor(theAbstractor);
	}
	
	public RandomSpinnerSelector (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public RandomSpinnerSelector (Abstractor theAbstractor, int minValue) {
		this (theAbstractor);
		setMin(minValue);
	}

	public RandomSpinnerSelector (Abstractor theAbstractor, int minValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
	}

	public RandomSpinnerSelector (Abstractor theAbstractor, int minValue, int maxValue) {
		this (theAbstractor);
		setMin(minValue);
		setMax(maxValue);
	}

	public RandomSpinnerSelector (Abstractor theAbstractor, int minValue, int maxValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
		setMax(maxValue);
	}
	
	public String getInteractionType() {
		return SPINNER_SELECT;
	}

	@Override
	public boolean canUseWidget (WidgetState w) {
		if (getMin(w)>getMax(w)) return false;
		return super.canUseWidget(w);
	}
	
	@Override
	public int getMin () {
		return 1;
	}
	
	@Override
	public int getMax(WidgetState w) {
		return w.getCount();
	}

}
