package org.castafiore.iot.definitions;

/**
 * 
 * Model to hold the event definition of a device.<br>
 * The event definition tells the server what events are expected to be propagated by the device.
 * 
 * @see FunctionDefinition
 * @author Kureem Rossaye
 *
 */
public class EventDefinition {
	
	/**
	 * The name of the event. (e.g OnStart, OnStop)
	 */
	private String name;
	
	/**
	 * A human readable description of the event definition
	 */
	private String description;

	/**
	 * 
	 * @return The name of the definition
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the definition
	 * @param name - The name of the definition
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The description of the definition
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the definition
	 * @param description - The description of the definition
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
