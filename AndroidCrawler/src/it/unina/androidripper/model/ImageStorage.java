package it.unina.androidripper.model;

import java.io.IOException;

import android.graphics.Bitmap;

public interface ImageStorage {
	
	public void saveImage (Bitmap image, String fileName) throws IOException;
	public String imageFormat ();

}
