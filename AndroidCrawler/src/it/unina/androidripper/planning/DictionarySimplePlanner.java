package it.unina.androidripper.planning;

import static it.unina.androidripper.Resources.TAG;
import it.unina.androidripper.model.Plan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.util.Log;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.ContentType;
import com.nofatclips.androidtesting.model.SimpleType;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;

public class DictionarySimplePlanner extends SimplePlanner {

	public void addPlanForActivityWidgets (Plan p, ActivityState a, boolean allowSwapTabs, boolean allowGoBack)
	{
		Log.i(TAG, "Planning with RegExPlanner for new Activity " + a.getName());
		
		//conta i widget EditText con contentType != DEFAULT
		int nEditText = 0;
		for (WidgetState w : a) //a.getNotDefaultEditTextCount()
		{
			if (	w.getSimpleType().equals(SimpleType.EDIT_TEXT) //w.isNotDefaultEditText()
				&& 	w.getContentType().equals(ContentType.DEFAULT) == false)
				nEditText++;
		}
		
		Log.i(TAG, "nEditText=" + nEditText);
		
		//Crea gli eventi
		//0000
		//1000
		//0100
		//0010
		//0001
		for (int currentEditTextWrongRegExIndex = 0; currentEditTextWrongRegExIndex <= nEditText; currentEditTextWrongRegExIndex++)
		{
			Log.i(TAG, "currentEditTextWrongRegExIndex=" + currentEditTextWrongRegExIndex);
			
			for (WidgetState w: getEventFilter())
			{
				int currentEditTextIndex  = 0;
				Collection<UserEvent> events = getUser().handleEvent(w);			
				for (UserEvent evt: events) {
					if (evt == null) continue;
					Collection<UserInput> inputs = new ArrayList<UserInput>();
					
					for (WidgetState formWidget: getInputFilter()) {
						
						List<UserInput> alternatives = getFormFiller().handleInput(formWidget);
						if (alternatives.size()==0) continue;
						UserInput inp = null;
						
						boolean isNotDefaultEditText = 	
													formWidget.getSimpleType().equals(SimpleType.EDIT_TEXT)
												&& 	formWidget.getContentType().equals(ContentType.DEFAULT) == false;
						
						//aumenta numero di editText con contentType != DEFAULT
						if (isNotDefaultEditText)							
							currentEditTextIndex++;
						
						//setta valore
						if (	isNotDefaultEditText
							&&	currentEditTextWrongRegExIndex == currentEditTextIndex) //e' l'edittext che deve essere riempita col valore sbagliato
						{
							inp = alternatives.get(0);
							Log.i(TAG, "edittext " + currentEditTextIndex + ", content=" + formWidget.getContentType() + ", using input=" + inp.getValue());
						}
						else
						{
							//comportamento normale							
							inp = alternatives.get(alternatives.size()-1);							
						}
						
						if ( (inp != null) && !((inp.getWidget().getUniqueId().equals(evt.getWidget().getUniqueId())) && (inp.getType().equals(evt.getType()))) ) {							
							inputs.add(inp);
						}
					}
					
					Transition t = getAbstractor().createStep(a, inputs, evt);
					p.addTask(t);
				}
			}
		}
	}
	
}
