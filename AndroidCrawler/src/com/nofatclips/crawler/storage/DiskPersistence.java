package com.nofatclips.crawler.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import android.app.Activity;
import android.content.ContextWrapper;

import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.xml.XmlGraph;
import com.nofatclips.crawler.model.Persistence;

public class DiskPersistence implements Persistence {
	
	public DiskPersistence () {
	}
	
	public DiskPersistence (Session theSession) {
		this();
		setSession(theSession);
	}

	@Override
	public void setFileName(String name) {
		this.fileName = name;
	}

	@Override
	public void setSession(Session s) {
		this.theSession = s;
	}
	
	public Session getSession() {
		return this.theSession;
	}

	public String getFileName () {
		return this.fileName;
	}
	
	public void setContext(Activity a) {
		this.w = new ContextWrapper(a);
	}

	@Override
	public void addTrace(Trace t) {
		this.theSession.addTrace(t);
	}

	@Override
	public void save() {
		save (this.fileName);
	}
	
	protected String generate () {
		// Generate text
		String graph = "";
		try {
			if (this.theSession instanceof XmlGraph) {
				graph = ((XmlGraph)this.theSession).toXml();
			} else {
				graph = this.theSession.toString();
			}
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return graph;
	}
	
	protected void save (String fileName) {
		
		// Generate text
		String graph = generate();

		// Write text to disk
		openFile(fileName);
		try {
			writeOnFile (graph);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeFile();
		}
	}
	
	public void writeOnFile (String graph) throws IOException {
		writeOnFile (this.osw, graph);
	}

	public void writeOnFile (OutputStreamWriter output, String graph) throws IOException {
		output.write(graph);
	}

	public void openFile (String fileName) {
		try{
			this.fOut = w.openFileOutput(fileName, this.mode);
			this.osw = new OutputStreamWriter(fOut);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean delete (String fileName) {
		return w.deleteFile(fileName);
	}
	
	public boolean exists (String filename) {
		File file = w.getFileStreamPath(filename);
		return file.exists();
	}

	public void closeFile () {
		closeFile (this.fOut, this.osw);
	}
	
	public void closeFile (FileOutputStream theFile, OutputStreamWriter theStream) {
		try {
			theStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				theStream.close();
				theFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	FileOutputStream fOut = null; 
    OutputStreamWriter osw = null;
    ContextWrapper w = null;
	private String fileName;
	private Session theSession;
	protected int mode = ContextWrapper.MODE_PRIVATE;

}