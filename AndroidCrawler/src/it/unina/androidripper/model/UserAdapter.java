package it.unina.androidripper.model;

public interface UserAdapter extends EventHandler, InputHandler {
	
	public void addInput (Interactor ... inputs);
	public void addEvent (Interactor ... events);
	
}