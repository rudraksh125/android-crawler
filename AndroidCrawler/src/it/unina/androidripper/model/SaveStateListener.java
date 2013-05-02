package it.unina.androidripper.model;

public interface SaveStateListener {
	
	public String getListenerName();
	public SessionParams onSavingState();
	public void onLoadingState(SessionParams sessionParams);

}
