package org.castafiore.iot.definitions;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * 
 * Object model for configuring a Device definition.<br>
 * The device definition is submitted by a device during handshaking. 
 * 
 * @author Kureem Rossaye
 *
 */
public class DeviceDefinition {

	/**
	 * A unique universal id for a definition.<br>
	 * The definitionId is set by the creator of the definition . <br>
	 * It should be unique in the context it is being used.<br>
	 */
	private String definitionId;
	
	
	private String versionId;
	
	private String groupId;
	
	/**
	 * a representative name the device.<br>
	 * While the deviceid is used internally by the system to reference a device, 
	 * the name which should be more human readable, will be used by a developer to reference and search for a device on the server side.
	 * 
	 */
	private String name;

	/**
	 * A human readable label
	 */
	private String label;

	/**
	 * An icon representing the device.<br>
	 * This should be a simple url accessible by the server
	 */
	private String icon;
	
	/**
	 * Arbitrary specifications set by the device.<br>
	 * The specifications can be used to validate conformity
	 */
	private Map<String, String> specs = new LinkedHashMap<String, String>();

	private List<EventDefinition> events = new LinkedList<EventDefinition>();

	private List<FunctionDefinition> functions = new LinkedList<FunctionDefinition>();

	public List<EventDefinition> getEvents() {
		return events;
	}

	public void setEvents(List<EventDefinition> events) {
		this.events = events;
	}

	public List<FunctionDefinition> getFunctions() {
		return functions;
	}

	public void setFunctions(List<FunctionDefinition> functions) {
		this.functions = functions;
	}
	
	

	/**
	 * 
	 * @return The name of the device
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the device
	 * @param name - The name of the device
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	/**
	 * 
	 * @return The label of the device
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label of the device
	 * @param label - The label of the device
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 
	 * @return The icon of the device 
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * sets The icon of the device
	 * @param icon - The icon of the device 
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	/**
	 * 
	 * @return The specifications of the device
	 */
	public Map<String, String> getSpecs() {
		return specs;
	}

	/**
	 * Sets the specifications of the device
	 * @param specs - The specifications of the device
	 */
	public void setSpecs(Map<String, String> specs) {
		this.specs = specs;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	

}
