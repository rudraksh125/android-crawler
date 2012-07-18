package com.nofatclips.crawler;

import static com.nofatclips.crawler.Resources.CRAWLER_PACKAGE;
import static com.nofatclips.crawler.Resources.PREFERENCES_FILE;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import com.nofatclips.crawler.model.ResourceFile;

import android.util.Log;

public class Prefs {
	
	private static Preferences prefs;
	private static boolean notFound = false;
	private Preferences localPrefs;
	private Class<?> resources;
	private static String mainNode = CRAWLER_PACKAGE;
	
	public Prefs (Preferences p) {
		this.localPrefs = p;
	}
	
	public Prefs (String node) {
		this (node, Resources.class);
	}	

	public Prefs (String node, ResourceFile resources) {
		this (node, resources.getClass());
	}	

	public Prefs (String node, Class<? extends ResourceFile> resources) {
		this.localPrefs = loadNode(node);
		this.resources = resources;
	}	
	
	public static void setMainNode (String node) {
		mainNode = node;
	}
	
	public static Preferences getMainNode () {
		if (notFound) return null;
		if (prefs == null) {
			loadMainNode();
		}
		return prefs;
	}

	public static void loadMainNode() {
		loadMainNode (mainNode);
	}
	
	public static void loadMainNode (String node) {
		String path = "/data/data/" + CRAWLER_PACKAGE + "/files/"+ PREFERENCES_FILE;
		InputStream is = null;

		if (!(new File(path).exists())) {
			Log.i("nofatclips", "Preferences file not found.");
			notFound = true;
			return;
		}
		
		Log.i("nofatclips", "Preferences file found.");
		try {
			is = new BufferedInputStream(new FileInputStream(path));
			Preferences.importPreferences(is);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPreferencesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		prefs = Preferences.userRoot().node(node);
	}
	
	public static Preferences loadNode (String localNode) {
		Log.d("nofatclips","Loading node " + localNode);
		if (getMainNode() == null) return null;
		return getMainNode().node(localNode);
	}
	
	public boolean hasPrefs() {
		return localPrefs!=null;
	}
	
	public void updateResources() {
		if (!hasPrefs()) return;
		for (Field f: resources.getFields()) {			
		    if (Modifier.isFinal(f.getModifiers())) continue;
		    try {
				updateValue (f);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int getInt (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		return localPrefs.getInt(parameter.getName(), parameter.getInt(parameter));
	}

	public long getLong (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		return localPrefs.getLong(parameter.getName(), parameter.getLong(parameter));
	}

	public boolean getBoolean (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		return localPrefs.getBoolean(parameter.getName(), parameter.getBoolean(parameter));
	}

	public String getString (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		return localPrefs.get(parameter.getName(), parameter.get("").toString());
	}
	
	public static String fromArray (Field parameter, int index) {
		return parameter.getName() + "[" + index + "]";
	}
		
	public String[] getStringArray (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		List<String> theList = new ArrayList<String>();
		int index = 0;
		String value;
		while ((value = localPrefs.get(fromArray(parameter, index), null)) != null) {
			theList.add(value);
			index++;
		}
		String tmp[] = new String [theList.size()];
		return theList.toArray(tmp);
	}

	public int[] getIntArray (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		String[] value = getStringArray (parameter);
		int[] ret = new int[value.length];
		for (int i=0; i<value.length; i++) {
			ret[i] = Integer.parseInt(value[i]);
		}
		return ret;
	}

	protected void updateValue (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		Log.v("nofatclips", "Updating value " + parameter.getName());
		Class<?> type = parameter.getType();
		String before = (parameter.get("") != null)?parameter.get("").toString():null;
		if (type.equals(int.class)) {
			parameter.setInt (parameter, getInt (parameter));
		} else if (type.equals(long.class)) {
			parameter.setLong (parameter, getLong (parameter));
		} else if (type.equals(String.class)) {
			parameter.set (parameter, getString (parameter));
		} else if (type.equals(boolean.class)) {
			parameter.setBoolean (parameter, getBoolean (parameter));
		} else if (type.isArray()) {
			Class<?> component = type.getComponentType();
			if (component.equals(String.class)) {
				parameter.set (parameter, getStringArray(parameter));
			} else if (component.equals(int.class)) {
				parameter.set (parameter, getIntArray(parameter));
			}
		} else {
			return;
		}
		String after = parameter.get("").toString();
		if (!after.equals(before)) {
			if (!type.isArray()) {
				Log.d("nofatclips", "Updated value of parameter " + parameter.getName() + " to " + after + " (default = " + before + ")");
			}
		}
	}
	
	public static void updateMainNode () {
		updateNode("", Resources.class);

//		Preferences comparator = p.prefs.node("comparator");
//		try {
//			for (String s: comparator.keys()) {
//				Log.e("nofatclips", s);
//			}
//		} catch (BackingStoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	public static void updateNode (String node, Class<? extends ResourceFile> resources) {
		Log.d("nofatclips", "Updating node " + node);
		Prefs p = new Prefs (node, resources);
		p.updateResources();
	}

}
