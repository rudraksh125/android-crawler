package it.unina.androidripper.automation;

import static it.unina.androidripper.Resources.TAG;
import it.unina.androidripper.model.ImageCaptor;
import it.unina.androidripper.model.ImageStorage;

import java.io.IOException;

import android.graphics.Bitmap;
import android.util.Log;


public class ScreenshotFactory {
	
	private static ImageCaptor theImageCaptor = null;
	private static ImageStorage theImageStorage = null;

	public static ImageStorage getTheImageStorage() {
		return theImageStorage;
	}

	public static void setTheImageStorage(ImageStorage imageStorage) {
		theImageStorage = imageStorage;
	}

	public static ImageCaptor getImageCaptor() {
		return theImageCaptor;
	}

	public static void setImageCaptor(ImageCaptor theCaptor) {
		theImageCaptor = theCaptor;
	}

	public static boolean saveScreenshot(String id) {
		Bitmap b = theImageCaptor.captureImage();
		
		if (b == null)
			return false;
		
		try {
			theImageStorage.saveImage(b, id);
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
		return true;		
	}
	
	public static String getFileExtension() {
		return theImageStorage.imageFormat();
	}
	
}