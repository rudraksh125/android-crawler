package com.nofatclips.crawler.storage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

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
			Log.d("nofatclips", "Task list is empty: no resume needed. Deleting activity list from disk.");
			delete (getActivityFileName());
			return;
		}
	}
	
	@Override
	public boolean isLast() {
		return ( (super.isLast()) && noTasks() );
	}
	
	public List<String> readTaskFile () {
		FileInputStream theFile;
		BufferedReader theStream = null;
		String line;
		List<String> output = new ArrayList<String>();
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder;
//		Document temp;
//		ElementWrapper trace; = new Trace();
		Log.i("nofatclips", "Reading task file");
		try{
//			builder = factory.newDocumentBuilder();
			theFile = w.openFileInput (getTaskListFileName());
//			Log.e("nofatclips", "Opening task file");
			theStream = new BufferedReader (new FileReader (theFile.getFD()));
//			Log.e("nofatclips", "Browsing task file");
			while ( (line = theStream.readLine()) != null) {
//				line = line.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE SESSION PUBLIC \"SESSION\" \"guitree.dtd\"><SESSION>");
//				line = line + "</SESSION>";
				//getSession().parse(line);
				output.add(line);
//				trace.setElement(((Element)temp.getDocumentElement().getChildNodes().item(0)));	
//				this.taskList.add((trace);
				
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

	public String getTaskListFileName() {
		return taskListFile;
	}

	public void setTaskListFile(String taskListFile) {
		this.taskListFile = taskListFile;
	}

}
