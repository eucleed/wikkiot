package org.castafiore.iot.io;

public class UpStream {

	public final static String HANDSHAKE = "handshake";

	public final static String IO = "io";

	private String deviceId;

	private String type;

	private long timestamp = System.currentTimeMillis();

	private String body;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
