package it.unina.androidripper.model;

import java.util.List;
import com.nofatclips.androidtesting.model.*;

public interface Interactor {

	public boolean cannotIdentifyWidget (WidgetState w);
	public boolean isVetoedWidget (WidgetState w);
	public boolean isForcedWidget (WidgetState w);
	public boolean canUseWidget (WidgetState w);
	public List<UserEvent> getEvents (WidgetState w);
	public List<UserInput> getInputs (WidgetState w);
	public List<UserEvent> getEvents (WidgetState w, String ... values);
	public List<UserInput> getInputs (WidgetState w, String ... values);

	public Abstractor getAbstractor();
	public void setAbstractor (Abstractor a);
	
	public String getInteractionType ();
	
}
