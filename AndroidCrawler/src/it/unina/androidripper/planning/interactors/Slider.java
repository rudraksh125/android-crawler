package it.unina.androidripper.planning.interactors;

import static com.nofatclips.androidtesting.model.InteractionType.SET_BAR;
import static com.nofatclips.androidtesting.model.SimpleType.SEEK_BAR;
import static com.nofatclips.androidtesting.model.SimpleType.RATING_BAR;
import it.unina.androidripper.model.Abstractor;
import it.unina.androidripper.planning.adapters.RandomInteractorAdapter;

import com.nofatclips.androidtesting.model.WidgetState;

public class Slider extends RandomInteractorAdapter {

	public Slider () {
		this (SEEK_BAR, RATING_BAR);
	}
	
	public Slider (String ... simpleTypes) {
		super (simpleTypes);
	}
	
	public Slider (Abstractor theAbstractor) {
		this();
		setAbstractor(theAbstractor);
	}
	
	public Slider (Abstractor theAbstractor, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
	}

	public Slider (Abstractor theAbstractor, int minValue) {
		this (theAbstractor);
		setMin(minValue);
	}

	public Slider (Abstractor theAbstractor, int minValue, String ... simpleTypes) {
		super (theAbstractor, simpleTypes);
		setMin(minValue);
	}

	public String getInteractionType() {
		return SET_BAR;
	}
	
	@Override
	public int getMin () {
		return 0;
	}
	
	@Override
	public int getMax(WidgetState w) {
		return w.getCount();
	}

}
