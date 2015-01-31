package org.castafiore.iot.definitions;

/**
 * 
 * Class model to hold the function definition of a device.<br>
 * The function definition tells the server what functions are exposed to be executed.
 * 
 * @author Kureem Rossaye
 *
 */
public class FunctionDefinition {
	
	/**
	 * The name of the function. (e.g SwitchOn, SwitchOff)
	 */
	private String name;
	
	/**
	 * A human readable description of the function definition
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
