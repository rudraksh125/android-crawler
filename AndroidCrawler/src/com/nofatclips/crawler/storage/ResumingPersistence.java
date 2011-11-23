package com.nofatclips.crawler.storage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import android.content.ContextWrapper;
import android.util.Log;

import com.nofatclips.androidtesting.guitree.FinalActivity;
import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.xml.ElementWrapper;
import com.nofatclips.crawler.model.DispatchListener;
import com.nofatclips.crawler.model.SaveStateListener;
import com.nofatclips.crawler.model.SessionParams;
import com.nofatclips.crawler.model.StateDiscoveryListener;

public class ResumingPersistence extends StepDiskPersistence implements DispatchListener, StateDiscoveryListener {

	private List<Trace> taskList;
	private String activityFile;
	private String taskListFile;
	private String parametersFile;
	private FileOutputStream taskFile;
	private OutputStreamWriter taskStream;
	private FileOutputStream stateFile;
	private OutputStreamWriter stateStream;
	private Map<String, SessionParams> parameters = new Hashtable<String, SessionParams>();
	private Hashtable<String,SaveStateListener> theListeners = new Hashtable<String,SaveStateListener>();
	
	public ResumingPersistence () {
		super(1);
	}
	
	public ResumingPersistence (Session theSession) {
		super(theSession, 1);
	}

	@Override 
	public void addTrace (Trace t) {
		t.setFailed(false);
		super.addTrace(t);
	}
	
	@Override
	public void onNewTaskAdded (Trace t) { /* do nothing */ }
	
	@Override
	public void onTaskDispatched(Trace t) {
		t.setFailed(true);
		saveTaskList();
		saveParameters();
	}
	
	@Override
	public String generate () {
		return super.generate() + System.getProperty("line.separator");
	}
	
	public void saveTaskList() {
		// No tasks to save
		if (noTasks()) {
			Log.d("nofatclips", "Task list is empty. Deleting file from disk.");
			delete (getTaskListFileName());
			return;
		}
		
		// Saving tasks in XML format - the old content of tasklist.xml is deleted
		try {
			Log.d("nofatclips", "Saving task list on disk");
			openTaskFile();
			for (Trace task: this.taskList) {
				String xml = ((ElementWrapper)task).toXml() + System.getProperty("line.separator");
				writeOnTaskFile(xml);
			}
			closeTaskFile();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveParameters() {
		parameters.clear();
		FileOutputStream theFile = null;
		ObjectOutputStream theStream = null;
		
		for (Entry<String, SaveStateListener> listener: this.theListeners.entrySet()) {
			parameters.put(listener.getKey(), listener.getValue().onSavingState());
		}
		
		try {
			theFile = w.openFileOutput(getParametersFileName(), ContextWrapper.MODE_PRIVATE);
			theStream = new ObjectOutputStream(theFile);
			theStream.writeObject(this.parameters);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (theFile!=null && theStream!=null) {
				closeFile(theFile, theStream);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadParameters() {
		FileInputStream theFile = null;
		ObjectInputStream theStream = null;
		
		try {
			theFile = w.openFileInput(getParametersFileName());
			theStream = new ObjectInputStream(theFile);
			this.parameters = (Map<String, SessionParams>) theStream.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException ignore) {}
		
		for (Entry<String, SaveStateListener> listener: this.theListeners.entrySet()) {
			listener.getValue().onLoadingState(this.parameters.get(listener.getKey()));
		}
	}
	
	@Override
	public void onNewState(ActivityState newState) {
		try {
			Log.d("nofatclips", "Saving new found state '" + newState.getId() + "' on disk");
			openStateFile(newState instanceof FinalActivity); // append if final activity - write from scratch if start activity
			String xml = ((ElementWrapper)newState).toXml() + System.getProperty("line.separator");
			writeOnStateFile(xml);
			closeStateFile();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void save() {
		super.save();
		saveTaskList();
		// Delete file when crawling is over
		if (noTasks()) {
			Log.d("nofatclips", "Task list is empty: no resume needed. Deleting parameters and activity list from disk.");
			delete (getActivityFileName());
			delete (getParametersFileName());
			return;
		}
	}
	
	@Override
	public boolean isLast() {
//		Log.e ("nofatclips", "super.isLast() = " + (super.isLast()?"true":"false"));
//		Log.e ("nofatclips", "noTasks() = " + (noTasks()?"true":"false"));
		return ( (super.isLast()) && noTasks() );
	}
	
	public List<String> readTaskFile () {
		FileInputStream theFile;
		BufferedReader theStream = null;
		String line;
		List<String> output = new ArrayList<String>();
		Log.i("nofatclips", "Reading task file");
		try{
			theFile = w.openFileInput (getTaskListFileName());
			theStream = new BufferedReader (new FileReader (theFile.getFD()));
			while ( (line = theStream.readLine()) != null) {
				output.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public List<String> readStateFile () {
		FileInputStream theFile;
		BufferedReader theStream = null;
		String line;
		List<String> output = new ArrayList<String>();
		Log.i("nofatclips", "Reading state file");
		try{
			theFile = w.openFileInput (getActivityFileName());
			theStream = new BufferedReader (new FileReader (theFile.getFD()));
			while ( (line = theStream.readLine()) != null) {
				output.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public void openTaskFile () {
		try{
			this.taskFile = w.openFileOutput(getTaskListFileName(), ContextWrapper.MODE_PRIVATE);
			this.taskStream = new OutputStreamWriter(this.taskFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openStateFile () {
		openStateFile (true);
	}

	public void openStateFile (boolean append) {
		try{
			Log.v("nofatclips", "Opening state file in " + ((append)?"append":"overwrite") + " mode.");
			this.stateFile = w.openFileOutput(getActivityFileName(), (append)?ContextWrapper.MODE_APPEND:ContextWrapper.MODE_PRIVATE);
			this.stateStream = new OutputStreamWriter(this.stateFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeOnTaskFile (String graph) throws IOException {
		writeOnFile (this.taskStream, graph);
	}

	public void writeOnStateFile (String graph) throws IOException {
		writeOnFile (this.stateStream, graph);
	}

	public void closeTaskFile () {
		closeFile(this.taskFile, this.taskStream);
	}

	public void closeStateFile () {
		closeFile(this.stateFile, this.stateStream);
	}

	public boolean noTasks() {
		return (this.taskList.size()==0);
	}
	
	public void setTaskList(List<Trace> taskList) {
		this.taskList = taskList;
	}

	public String getActivityFileName() {
		return activityFile;
	}

	public void setActivityFile(String activityFile) {
		this.activityFile = activityFile;
	}

	public String getParametersFileName() {
		return parametersFile;
	}

	public void setParametersFile(String name) {
		this.parametersFile = name;
	}

	public String getTaskListFileName() {
		return taskListFile;
	}

	public void setTaskListFile(String taskListFile) {
		this.taskListFile = taskListFile;
	}
	
	@Override
	public void registerListener (SaveStateListener listener) {
		theListeners.put(listener.getListenerName(), listener);
	}

}
