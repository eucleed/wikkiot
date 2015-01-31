package org.castafiore.iot.driver;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.castafiore.iot.client.Device;
import org.castafiore.iot.client.FunctionHandler;
import org.castafiore.iot.io.DefaultDisconnectedDeviceHandler;
import org.castafiore.iot.io.DisconnectedDeviceHandler;
import org.castafiore.iot.io.DownStream;
import org.castafiore.iot.io.Execute;
import org.castafiore.iot.io.OnConnectedListener;
import org.castafiore.iot.io.UpStream;
import org.castafiore.iot.io.WebSocketLayer;

import com.fasterxml.jackson.databind.ObjectMapper;

@ClientEndpoint
public class JavaWebsocketLayer implements WebSocketLayer{
	
	private List<FunctionHandler> listeners = new LinkedList<FunctionHandler>();
	
	private List<OnConnectedListener> onConnectedListeners = new LinkedList<OnConnectedListener>();
	
	private DisconnectedDeviceHandler disconnedDeviceHandler = new DefaultDisconnectedDeviceHandler();
	
	
	
	ObjectMapper mapper = new ObjectMapper();
	
	private Session session;
	
	private Device device;
	
	

	public JavaWebsocketLayer(Device device) {
		super();
		this.device = device;
	}

	@Override
	public void sendRequest(UpStream request, Object body) {
		try{
			if(isConnected()){
				String sbody = mapper.writeValueAsString(body);
				request.setBody(sbody);
				session.getAsyncRemote().sendText(mapper.writeValueAsString(request));
			}else{
				disconnedDeviceHandler.handleDisconnectedDevice(device);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public boolean isConnected() {
		return session!= null && session.isOpen();
	}

	@Override
	public void disconnect()throws IOException {
		if(session != null){
			session.close();
		}
		
		session = null;
		
	}

	

	@Override
	public void connect(String server) {
		try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(server));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		
	}

	@Override
	public void addFunctionHandler(FunctionHandler listener) {
		this.listeners.add(listener);
		
	}
	
	@OnOpen
    public void onOpen(Session userSession) {
        this.session = userSession;
       for(OnConnectedListener l : onConnectedListeners){
    	   l.onConnected(getDevice());
       }
    }
	
	@OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.session = null;
    }
	
	@OnMessage
    public void onMessage(String message) {
		try{
		DownStream response = mapper.readValue(message.getBytes(), DownStream.class);
		String type = response.getType();
		if(DownStream.TYPE_EXECUTE.equals(type)){
			Execute execute = mapper.readValue(response.getBody(), Execute.class);
			for(FunctionHandler l : listeners){
				l.execute(execute.getFunction(), execute.getParameters());
			}
		}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
    }

	@Override
	public void addOnConnectedListener(OnConnectedListener listener) {
		onConnectedListeners.add(listener);
	}

	@Override
	public Device getDevice() {
		return device;
	}

	@Override
	public void setDisconnectedDeviceHandler(DisconnectedDeviceHandler handler) {
		this.disconnedDeviceHandler = handler;
		
	}

}
