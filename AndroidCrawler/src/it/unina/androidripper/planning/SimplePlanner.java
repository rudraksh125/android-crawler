package it.unina.androidripper.planning;

import it.unina.androidripper.model.*;
import it.unina.androidripper.planning.sensors_utils.GpsValuesGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import android.util.Log;

import com.nofatclips.androidtesting.model.*;

import static com.nofatclips.androidtesting.model.InteractionType.*;
//import static com.nofatclips.androidtesting.model.SimpleType.*;

public class SimplePlanner implements Planner {

	public final static boolean ALLOW_SWAP_TAB = true;
	public final static boolean NO_SWAP_TAB = false;
	public final static boolean ALLOW_GO_BACK = true;
	public final static boolean NO_GO_BACK = false;
	
	public Plan getPlanForActivity (ActivityState a) {
		return getPlanForActivity(a, !Resources.TAB_EVENTS_START_ONLY, ALLOW_GO_BACK);
	}

	public Plan getPlanForBaseActivity (ActivityState a) {
		return getPlanForActivity(a, ALLOW_SWAP_TAB, NO_GO_BACK);
	}

	public void addPlanForActivityWidgets (Plan p, ActivityState a, boolean allowSwapTabs, boolean allowGoBack)
	{		
		for (WidgetState w: getEventFilter()) {
			Collection<UserEvent> events = getUser().handleEvent(w);			
			for (UserEvent evt: events) {
				if (evt == null) continue;
				Collection<UserInput> inputs = new ArrayList<UserInput>();
				for (WidgetState formWidget: getInputFilter()) {
					List<UserInput> alternatives = getFormFiller().handleInput(formWidget);
					if (alternatives.size()==0) continue;
					UserInput inp = alternatives.get(alternatives.size()-1);
					if ( (inp != null) && !((inp.getWidget().getUniqueId().equals(evt.getWidget().getUniqueId())) && (inp.getType().equals(evt.getType()))) ) {
						inputs.add(inp);
					}
				}
				Transition t = getAbstractor().createStep(a, inputs, evt);
				p.addTask(t);
			}
		}
	}
	
	public Plan getPlanForActivity (ActivityState a, boolean allowSwapTabs, boolean allowGoBack)
	{
		Log.i("androidripper", "Planning for new Activity " + a.getName());
		Plan p = new Plan();
		addPlanForActivityWidgets(p, a, allowSwapTabs, allowGoBack);
		
		UserEvent evt;
		Transition t;
		
		// Special handling for pressing back button
		if (Resources.BACK_BUTTON_EVENT && allowGoBack) {
			evt = getAbstractor().createEvent(null, BACK);
			t = getAbstractor().createStep(a, new HashSet<UserInput>(), evt);
			Log.i("androidripper", "Created trace to press the back button");
			p.addTask(t);
		}
		
		if (Resources.MENU_EVENTS) {
			evt = getAbstractor().createEvent(null, OPEN_MENU);
			t = getAbstractor().createStep(a, new HashSet<UserInput>(), evt);
			Log.i("androidripper", "Created trace to press the menu button");
			p.addTask(t);
		}

		// Special handling for scrolling down
		if (Resources.SCROLL_DOWN_EVENT) {
			evt = getAbstractor().createEvent(null, SCROLL_DOWN);
			t = getAbstractor().createStep(a, new HashSet<UserInput>(), evt);
			Log.i("androidripper", "Created trace to perform scrolling down");
			p.addTask(t);
		}

		if (Resources.ORIENTATION_EVENTS) {
			evt = getAbstractor().createEvent(null, CHANGE_ORIENTATION);
			t = getAbstractor().createStep(a, new HashSet<UserInput>(), evt);
			Log.i("androidripper", "Created trace to change orientation");
			p.addTask(t);
		}
		
		if (Resources.KEY_EVENTS.length>0) {
			for (int keyCode: Resources.KEY_EVENTS) {
				evt = getAbstractor().createEvent(null, PRESS_KEY);
				evt.setValue(String.valueOf(keyCode));
				t = getAbstractor().createStep(a, new HashSet<UserInput>(), evt);
				Log.i("androidripper", "Created trace to perform key press (key code: " + keyCode + ")");
				p.addTask(t);
			}
		}
		
		/** @author nicola amatucci - sensori/reflection */
		//NOTA: w0 significa nessun id, quindi scateno gli eventi che non
		//		interessano nessun widget, per esempio, che interessano
		//		l'activity
		for ( SupportedEvent se : a.getSupportedEventsByWidgetUniqueId(SupportedEvent.GENERIC_ACTIVITY_UID) )
		{
			if (se.getEventType().equals(InteractionType.ACCELEROMETER_SENSOR_EVENT))
			{
				addStepsForSensor(
						android.hardware.Sensor.TYPE_ACCELEROMETER,
						InteractionType.ACCELEROMETER_SENSOR_EVENT,
						a, p);
			}
			
			else if (se.getEventType().equals(InteractionType.ORIENTATION_SENSOR_EVENT))
			{
				addStepsForSensor(
						android.hardware.Sensor.TYPE_ORIENTATION,
						InteractionType.ORIENTATION_SENSOR_EVENT,
						a, p);
			}			
			
			else if (se.getEventType().equals(InteractionType.MAGNETIC_FIELD_SENSOR_EVENT))
			{
				addStepsForSensor(
						android.hardware.Sensor.TYPE_MAGNETIC_FIELD,
						InteractionType.MAGNETIC_FIELD_SENSOR_EVENT,
						a, p);
			}
			
			//NOTA: TYPE_TEMPERATURE e' deprecato nelle api 14: diventa TYPE_AMBIENT_TEMPERATURE
			else if (se.getEventType().equals(InteractionType.TEMPERATURE_SENSOR_EVENT))
			{
				addStepsForSensor(
						android.hardware.Sensor.TYPE_TEMPERATURE,
						InteractionType.TEMPERATURE_SENSOR_EVENT,
						a, p);
			}
			
			else if (se.getEventType().equals(InteractionType.GPS_LOCATION_CHANGE_EVENT))				
			{
				addStepsForGPS(a, p);
			}
			
			else if (se.getEventType().equals(InteractionType.GPS_PROVIDER_DISABLE_EVENT))				
			{
				//TODO
				//evento "disabilita provider"
				//evt = getAbstractor().createEvent(null, GPS_PROVIDER_DISABLE_EVENT);
				//t = getAbstractor().createStep(a, inputs, evt);
				//p.addTask(t);
			}
			
			else if (se.getEventType().equals(InteractionType.INCOMING_CALL_EVENT))
			{
				evt = null;
				evt = getAbstractor().createEvent(null, INCOMING_CALL_EVENT);
				t = getAbstractor().createStep(a, new ArrayList<UserInput>(), evt);
				p.addTask(t);
			}
			
			else if (se.getEventType().equals(InteractionType.INCOMING_SMS_EVENT))
			{
				evt = null;
				evt = getAbstractor().createEvent(null, INCOMING_SMS_EVENT);
				t = getAbstractor().createStep(a, new ArrayList<UserInput>(), evt);
				p.addTask(t);
			}
		}
		/** @author nicola amatucci - sensori/reflection */		
		
		return p;
	}
	
	/** @author nicola amatucci - sensori/reflection */
	protected void addStepsForSensor(Integer SENSOR_TYPE, String eventType, ActivityState a, Plan p)
	{
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		
		if (Resources.EXCLUDE_WIDGETS_INPUTS_IN_SENSORS_EVENTS == false)
		{
			for (WidgetState formWidget: getInputFilter()) {
				List<UserInput> alternatives = getFormFiller().handleInput(formWidget); 
				UserInput inp = ((alternatives.size()>0)?alternatives.get(alternatives.size()-1):null);
				if (inp != null) {
					inputs.add(inp);
				}
			}
		}
		
		float[] randomInputValues = it.unina.androidripper.planning.sensors_utils.SensorValuesGenerator.generateSensorValues(SENSOR_TYPE);;					
		float[] positiveRandomInputValues = it.unina.androidripper.planning.sensors_utils.SensorValuesGenerator.generateSensorValues(SENSOR_TYPE);
		float[] negativeRandomInputValues = it.unina.androidripper.planning.sensors_utils.SensorValuesGenerator.generateSensorValues(SENSOR_TYPE);
		
		for (int i = 0; i < 3; i++)
			positiveRandomInputValues[i] = Math.abs(positiveRandomInputValues[0]);
		
		for (int i = 0; i < 3; i++)
			negativeRandomInputValues[i] = -1 * Math.abs(positiveRandomInputValues[0]);
		
		UserEvent evt = null;
		Transition t = null;
		
		String sensorInputValueStr = null;
		
		//random
		sensorInputValueStr = randomInputValues[0] + "|" + randomInputValues[1] + "|" + randomInputValues[2];
		evt = getAbstractor().createEvent(null, eventType);
		evt.setValue(sensorInputValueStr);
		t = getAbstractor().createStep(a, inputs, evt);
		p.addTask(t);
		
		//+ + +
		sensorInputValueStr = positiveRandomInputValues[0] + "|" + positiveRandomInputValues[1] + "|" + positiveRandomInputValues[2];
		evt = getAbstractor().createEvent(null, eventType);
		evt.setValue(sensorInputValueStr);
		t = getAbstractor().createStep(a, inputs, evt);
		p.addTask(t);
		
		//- - -
		sensorInputValueStr = negativeRandomInputValues[0] + "|" + negativeRandomInputValues[1] + "|" + negativeRandomInputValues[2];
		evt = getAbstractor().createEvent(null, eventType);
		evt.setValue(sensorInputValueStr);
		t = getAbstractor().createStep(a, inputs, evt);
		p.addTask(t);				

		//0 0 0
		sensorInputValueStr = "0|0|0";
		evt = getAbstractor().createEvent(null, eventType);
		evt.setValue(sensorInputValueStr);
		t = getAbstractor().createStep(a, inputs, evt);
		p.addTask(t);
		
		//TODO: out-of-bounds?
	}
	
	protected void addStepsForGPS(ActivityState a, Plan p)
	{
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		
		if (Resources.EXCLUDE_WIDGETS_INPUTS_IN_GPS_EVENTS == false)
		{
			for (WidgetState formWidget: getInputFilter()) {
				List<UserInput> alternatives = getFormFiller().handleInput(formWidget); 
				UserInput inp = ((alternatives.size()>0)?alternatives.get(alternatives.size()-1):null);
				if (inp != null) {
					inputs.add(inp);
				}
			}
		}
		
		UserEvent evt = null;
		Transition t = null;
		
		//random
		String locationInputValueStr = 	GpsValuesGenerator.getRandomLatitude() + "|" +
										GpsValuesGenerator.getRandomLongitude() + "|" +
										GpsValuesGenerator.getRandomAltitude();			
		evt = getAbstractor().createEvent(null, GPS_LOCATION_CHANGE_EVENT);
		evt.setValue(locationInputValueStr);
		t = getAbstractor().createStep(a, inputs, evt);
		p.addTask(t);	
		
		//+ + +
		locationInputValueStr = GpsValuesGenerator.getRandomPositiveLatitude() + "|" +
								GpsValuesGenerator.getRandomPositiveLongitude() + "|" +
								GpsValuesGenerator.getRandomPositiveAltitude();			
		evt = getAbstractor().createEvent(null, GPS_LOCATION_CHANGE_EVENT);
		evt.setValue(locationInputValueStr);
		t = getAbstractor().createStep(a, inputs, evt);
		p.addTask(t);
		
		//- - -
		locationInputValueStr = GpsValuesGenerator.getRandomNegativeLatitude() + "|" +
								GpsValuesGenerator.getRandomNegativeLongitude() + "|" +
								GpsValuesGenerator.getRandomNegativeAltitude();			
		evt = getAbstractor().createEvent(null, GPS_LOCATION_CHANGE_EVENT);
		evt.setValue(locationInputValueStr);
		t = getAbstractor().createStep(a, inputs, evt);
		p.addTask(t);
		
		//0 0 0
		locationInputValueStr = "0|0|0";			
		evt = getAbstractor().createEvent(null, GPS_LOCATION_CHANGE_EVENT);
		evt.setValue(locationInputValueStr);
		t = getAbstractor().createStep(a, inputs, evt);
		p.addTask(t);
	}
	/** @author nicola amatucci - sensori/reflection */		
	
	public Filter getEventFilter() {
		return this.eventFilter;
	}

	public void setEventFilter(Filter eventFilter) {
		this.eventFilter = eventFilter;
	}
	
	public Filter getInputFilter() {
		return this.inputFilter;
	}
	
	public void setInputFilter(Filter inputFilter) {
		this.inputFilter = inputFilter;
	}

	public EventHandler getUser() {
		return this.user;
	}

	public void setUser(EventHandler user) {
		this.user = user;
	}

	public InputHandler getFormFiller() {
		return this.formFiller;
	}

	public void setFormFiller(InputHandler formFiller) {
		this.formFiller = formFiller;
	}

	public Abstractor getAbstractor() {
		return this.abstractor;
	}

	public void setAbstractor(Abstractor abstractor) {
		this.abstractor = abstractor;
	}

	/** @author nicola amatucci - sensori/reflection */
	//NOTA: cambiato private -> protected
	protected Filter eventFilter;
	protected Filter inputFilter;
	protected EventHandler user;
	protected InputHandler formFiller;
	protected Abstractor abstractor;
	/** @author nicola amatucci - sensori/reflection */

}
