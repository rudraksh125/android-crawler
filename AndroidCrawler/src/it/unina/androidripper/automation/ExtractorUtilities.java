package it.unina.androidripper.automation;

import android.app.Activity;
import android.view.View;

public class ExtractorUtilities {

	private static Activity theActivity; // Current Activity
	
	public static Activity getActivity() {
		return theActivity;
	}
	
	public static void setActivity(Activity a) {
		theActivity = a;
	}
	
	public static View findViewById (int id) {
		return getActivity().findViewById(id);
	}

}
