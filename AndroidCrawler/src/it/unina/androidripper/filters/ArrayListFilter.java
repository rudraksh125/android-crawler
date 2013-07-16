package it.unina.androidripper.filters;

import it.unina.androidripper.model.Filter;

import java.util.ArrayList;
import java.util.Iterator;

import android.view.View;

import com.nofatclips.androidtesting.model.WidgetState;

public abstract class ArrayListFilter implements Filter {

	public ArrayListFilter () {
		super ();
	}
	
	@Override
	public Iterator<WidgetState> iterator() {
		return this.filteredItems.iterator();
	}
	
	@Override
	public void loadItem(View v, WidgetState w) {
		if (isValidItem(v))
			this.filteredItems.add(w);
	}
	
	@Override
	public void clear () {
		this.filteredItems.clear();
	}
	
	private ArrayList<WidgetState> filteredItems = new ArrayList<WidgetState>();

}
