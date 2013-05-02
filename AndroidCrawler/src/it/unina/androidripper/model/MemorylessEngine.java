package it.unina.androidripper.model;

import junit.framework.Test;

public interface MemorylessEngine extends Test {
	
	/*
	 * Just a tagging interface. Engines that implement this interface are declaring that they don't need the
	 * task list to be loaded at startup to function properly. A new task list will be generated from scratch.
	 */

}
