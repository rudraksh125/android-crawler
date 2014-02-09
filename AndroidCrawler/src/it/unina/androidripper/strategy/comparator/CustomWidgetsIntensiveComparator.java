package it.unina.androidripper.strategy.comparator;

import com.nofatclips.androidtesting.model.WidgetState;

import static it.unina.androidripper.strategy.comparator.Resources.*;

public class CustomWidgetsIntensiveComparator extends CustomWidgetsSimpleComparator {
	
	public CustomWidgetsIntensiveComparator (String... widgets) {
		super (widgets);
	}

	@Override
	protected boolean matchWidget (WidgetState campo, WidgetState altroCampo) {
		boolean matchValue = (COMPARE_VALUES)?altroCampo.getValue().equals(campo.getValue()):true; 
		return (matchValue && super.matchWidget(campo, altroCampo) && altroCampo.getName().equals(campo.getName()));
	}
	
}