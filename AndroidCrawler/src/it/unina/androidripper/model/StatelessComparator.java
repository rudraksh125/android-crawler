package it.unina.androidripper.model;

public interface StatelessComparator extends Comparator {

	/*
	 * Just a tagging interface. Comparator that implements this interface are declaring that they don't need the
	 * activity state list to function properly. Example: the NullComparator.
	 */
	
}
