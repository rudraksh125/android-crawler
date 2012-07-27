package com.nofatclips.crawler.model;

public interface UserAdapter extends EventHandler, InputHandler {
	
	public void addInput (Interactor ... inputs);
	public void addEvent (Interactor ... events);
	
}