package com.nofatclips.crawler.automation;

import com.nofatclips.androidtesting.model.WidgetState;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsSpinner;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class AbstractorUtilities {

	public static String detectName (View v) {
		String name = "";
		if (v instanceof TextView) {
			TextView t = (TextView)v;
			name = t.getText().toString();
			if (v instanceof EditText) {
				CharSequence hint = ((EditText)v).getHint();
				name = (hint==null)?"":hint.toString();
			}
		} else if (v instanceof RadioGroup) {
			RadioGroup g = (RadioGroup)v;
			int max=g.getChildCount();
			String text = "";
			for (int i=0; i<max; i++) {
				View c = g.getChildAt(i);
				text = detectName (c);
				if (!text.equals("")) {
					name = text;
					break;
				}
			}
		}
		return name;
	}
	
	@SuppressWarnings("rawtypes")
	public static void setCount (View v, WidgetState w) {
		// For lists, the count is set to the number of rows in the list (inactive rows - e.g. separators - count as well)
		if (v instanceof AdapterView) {
			w.setCount(((AdapterView)v).getCount());
			return;
		}
		
		// For Spinners, the count is set to the number of options
		if (v instanceof AbsSpinner) {
			w.setCount(((AbsSpinner)v).getCount());
			return;
		}
		
		// For the tab layout host, the count is set to the number of tabs
		if (v instanceof TabHost) {
			w.setCount(((TabHost)v).getTabWidget().getTabCount());
			return;
		}
		
		// For grids, the count is set to the number of icons, for RadioGroups it's set to the number of RadioButtons
		if (v instanceof ViewGroup) {
			w.setCount(((ViewGroup)v).getChildCount());
			return;
		}
		
		// For progress bars, seek bars and rating bars, the count is set to the maximum value allowed
		if (v instanceof ProgressBar) {
			w.setCount(((ProgressBar)v).getMax());
			return;
		}
		
	}

	public static void setValue (View v, WidgetState w) {
		
		// Checkboxes, radio buttons and toggle buttons -> the value is the checked state (true or false)
		if (v instanceof Checkable) {
			w.setValue(String.valueOf(((Checkable) v).isChecked()));
		}

		// Textview, Editview et al. -> the value is the displayed text
		if (v instanceof TextView) {
			w.setValue(((TextView) v).getText().toString());
//			Log.e("nofatclips", "Hint for " + (((TextView) v).getText().toString()) + " = " + (((TextView) v).getHint()));
			return;
		}
		
		// Progress bars, seek bars and rating bars -> the value is the current progress
		if (v instanceof ProgressBar) {
			w.setValue(String.valueOf(((ProgressBar) v).getProgress()));
		}
				
	}
	
	public static String getType (View v) {
		return v.getClass().getName();
	}

}
