package org.castafiore.iot.io;


public class DownStream  {


	public final static String TYPE_EXECUTE = "exec";
	
	private String body;

	private String type;


	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
