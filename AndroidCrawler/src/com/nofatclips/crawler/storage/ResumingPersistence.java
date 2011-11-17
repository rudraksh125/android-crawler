package com.nofatclips.crawler.storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import android.content.ContextWrapper;
import android.util.Log;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.xml.ElementWrapper;
import com.nofatclips.crawler.model.DispatchListener;
import com.nofatclips.crawler.model.StateDiscoveryListener;

public class ResumingPersistence extends StepDiskPersistence implements DispatchListener, StateDiscoveryListener {

	private List<Trace> taskList;
	private String activityFile;
	private String taskListFile;
	private FileOutputStream taskFile;
	private OutputStreamWriter taskStream;
	private FileOutputStream stateFile;
	private OutputStreamWriter stateStream;
	
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
	public void onNewTaskDispatched(Trace t) {
		t.setFailed(true);
		saveTaskList();
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
			for (Trace task: taskList) {
				String xml = ((ElementWrapper)task).toXml();
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
	
	@Override
	public void onNewState(ActivityState newState) {
		try {
			Log.d("nofatclips", "Saving new found state '" + newState.getId() + "' on disk");
			openStateFile();
			String xml = ((ElementWrapper)newState).toXml();
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
			Log.e("nofatclips", "Task list is empty: no resume needed. Deleting activity list from disk.");
			delete (getActivityFileName());
			return;
		}
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
		try{
			this.stateFile = w.openFileOutput(getActivityFileName(), ContextWrapper.MODE_APPEND);
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
		return (taskList.size()==0);
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

	public String getTaskListFileName() {
		return taskListFile;
	}

	public void setTaskListFile(String taskListFile) {
		this.taskListFile = taskListFile;
	}

}
