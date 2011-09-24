package com.nofatclips.crawler;

import android.view.View;
import android.widget.TabHost;

public class SimpleEventFilter extends ButtonFilter {

	@Override
	public boolean isValidItem(View v) {
		return (super.isValidItem(v) || (v instanceof TabHost));
	}

}
