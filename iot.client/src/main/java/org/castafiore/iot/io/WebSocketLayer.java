package org.castafiore.iot.io;

import java.io.IOException;

import org.castafiore.iot.client.Device;
import org.castafiore.iot.client.FunctionHandler;

public interface WebSocketLayer {

	public void sendRequest(UpStream request, Object body);
	
	public void setDisconnectedDeviceHandler(DisconnectedDeviceHandler handler);
	
	public void addFunctionHandler(FunctionHandler listener);
	
	public void addOnConnectedListener(OnConnectedListener listener);
	
	public boolean isConnected();
	
	public void disconnect()throws IOException;
	
	public void connect(String server);
	
	public Device getDevice();
	
	
}
