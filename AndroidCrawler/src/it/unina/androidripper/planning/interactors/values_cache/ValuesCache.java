package it.unina.androidripper.planning.interactors.values_cache;

import it.unina.androidripper.model.SaveStateListener;
import it.unina.androidripper.model.SessionParams;
import it.unina.androidripper.storage.PersistenceFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;


import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;


//NOTA: utilizzo il listener solo per capire quando memorizzare i dati
public class ValuesCache extends HashMap<String,String[]> implements SaveStateListener, Serializable
{
	private static final long serialVersionUID = 123456L;
	public final static String ACTOR_NAME = "ValuesCache";
	
	public static String FILE_NAME = "valuesCache.obj";
	
	private static Context context = null; 
	private static ValuesCache singletonInstance = null;
	
	public static ValuesCache getInstance()
	{
		return singletonInstance;
	}
	
	public static ValuesCache init(Context ctx)
	{
		if (singletonInstance == null && ctx != null)
		{
			context = ctx;
			singletonInstance = new ValuesCache();
		}
		
		return singletonInstance;
	}
	
	public String[] put(int key, String value)
	{
		return this.put(Integer.toString(key), value);
	}
	
	public String[] put(String key, String value)
	{
		String[] strings = new String[2];
		strings[0] = new String(value);
		return this.put(key, strings);
	}

	public String[] put(int key, String incorrectValue, String correctValue)
	{
		return this.put(Integer.toString(key), incorrectValue, correctValue);
	}
	
	public String[] put(String key, String incorrectValue, String correctValue)
	{
		String[] strings = new String[2];		
		strings[0] = new String(incorrectValue);
		strings[1] = new String(correctValue);
		return this.put(key, strings);
	}
	
	private ValuesCache()
	{
		super();
		PersistenceFactory.registerForSavingState(this);
	}
	
	@Override
	public String getListenerName() {
		return ACTOR_NAME;
	}

	@Override
	public SessionParams onSavingState()
	{
		try {
			saveCache(FILE_NAME);
		}
		catch(Exception ex) {
			
		}
		return new SessionParams();
	}

	@Override
	public void onLoadingState(SessionParams sessionParams) {
		try {
			loadCache(FILE_NAME);
		}
		catch(Exception ex) {}
	}
	
	@SuppressWarnings("unused")
	public void loadCache(String name) throws IOException
	{
		if (context == null)
			throw new RuntimeException("ValuesCache : initialization needed!");
		
		FileInputStream fileIn = null;
		ObjectInputStream in = null;
		 try
         {
            fileIn = context.openFileInput(name);
            in = new ObjectInputStream(fileIn);
            ValuesCache vCache = (ValuesCache) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i)
        {
            i.printStackTrace();            
        }
		catch(ClassNotFoundException c)
        {
            c.printStackTrace();
        }
		finally
		{
			try {if (in != null) in.close(); } catch (Exception ex) {}
			try {if (fileIn != null) fileIn.close(); } catch (Exception ex) {}
		}
	}
	
	public void saveCache(String name) throws IOException
	{
		if (context == null)
			throw new RuntimeException("ValuesCache : initialization needed!");
		
		FileOutputStream fileOutput = null;
		ObjectOutputStream streamWriter = null;
		try {
			fileOutput = context.openFileOutput(name,ContextWrapper.MODE_WORLD_READABLE);
			streamWriter = new ObjectOutputStream(fileOutput);
			if (fileOutput != null) {
				streamWriter.writeObject(this);
				Log.i("androidripper","Saved values cache on disk: " + name);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {if (streamWriter != null) streamWriter.close(); } catch (Exception ex) {}
			try {if (fileOutput != null) fileOutput.close(); } catch (Exception ex) {}
		}
	}
}
