package com.nofatclips.crawler.automation;

import android.view.View;

import com.nofatclips.crawler.model.TypeDetector;

// Detect the simple type of the widget based on the name of the class. Faster but won't work always.
// A more reliable implementation should use instanceof.

public class SimpleTypeDetector implements TypeDetector {

	@Override
	public String getSimpleType(View v) {
		String type = v.getClass().getName(); 
		if (type.endsWith("null"))
			return "null";
		if (type.endsWith("RadioButton"))
			return "radio";
		if (type.endsWith("CheckBox"))
			return "check";
		if (type.endsWith("Button"))
			return "button";
		if (type.endsWith("EditText"))
			return "editText";
		if (type.endsWith("TabHost"))
			return "tabHost";
		return "";
	}

}
