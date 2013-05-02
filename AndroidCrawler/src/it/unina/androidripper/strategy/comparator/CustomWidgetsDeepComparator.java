package it.unina.androidripper.strategy.comparator;

import com.nofatclips.androidtesting.model.WidgetState;

import static it.unina.androidripper.strategy.comparator.Resources.COMPARE_VALUES;

public class CustomWidgetsDeepComparator extends CustomWidgetsComparator {
	
	public CustomWidgetsDeepComparator (String... widgets) {
		super (widgets);
	}
	
	public CustomWidgetsDeepComparator (boolean ignore, String... widgets) {
		super(ignore, widgets);
	}

	@Override
	protected boolean matchWidget (WidgetState campo, WidgetState altroCampo) {
		boolean matchValue = (COMPARE_VALUES)?altroCampo.getValue().equals(campo.getValue()):true; 
		return (matchValue && super.matchWidget(campo, altroCampo) && altroCampo.getName().equals(campo.getName()));
	}
	
}