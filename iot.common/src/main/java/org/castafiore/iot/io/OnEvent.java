package org.castafiore.iot.io;

import java.util.LinkedHashMap;
import java.util.Map;


public class OnEvent  {
	
	private String name;
	
	private Map<String, String> parameters = new LinkedHashMap<String, String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	
	

}
