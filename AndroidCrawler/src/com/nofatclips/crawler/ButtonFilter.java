package com.nofatclips.crawler;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

public class ButtonFilter extends ListFilter {

	@Override
	public boolean isValidItem(View v) {
		return ((v instanceof Button) && (!(v instanceof CompoundButton)));
	}

}
