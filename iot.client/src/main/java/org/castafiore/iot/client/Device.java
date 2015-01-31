package org.castafiore.iot.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.castafiore.iot.definitions.DefinitionRegistry;
import org.castafiore.iot.definitions.DeviceDefinition;
import org.castafiore.iot.definitions.EventDefinition;
import org.castafiore.iot.definitions.FunctionDefinition;
import org.castafiore.iot.io.DisconnectedDeviceHandler;
import org.castafiore.iot.io.OnConnectedListener;
import org.castafiore.iot.io.OnEvent;
import org.castafiore.iot.io.UpStream;
import org.castafiore.iot.io.WebSocketLayer;

/**
 * 
 *	This class is a generic wrapper for a device.<br> 
 *	This implementation is in java and intended to run on any device that can executes a jvm.<br> 
 * 	See other projects for implementation of a device wrapper in other<br>
 * 	languages. <br>The device is associated with an iot server.<br> 
 * This class helps for the following:<br>
 * 	<ul>
 * 		<li>Create a device definition dynamically</li>
 * 		<li>Manage websocket connection with server</li>
 * 		<li>Encapsulates the complexities of constructing websocket messages
 * 			according to the iot protocol</li>
 * 		<li>Manage life cycle of device</li>
 * 	</ul>
 * <pre>
 * 
 *  {@code
 *  final Device mydevice = new Device("BasicSwitch", "Switch","Switch" ,"switch.png");
 * 
 * mydevice.registerFunction("SwitchOn", "Function exposed to server");
 * 		
 * mydevice.registerFunction("SwitchOff", "Function exposed to server for switching off the device");
 * 
 * mydevice.setWebsocketLayer(new JavaWebsocketLayer(mydevice));
 * 		
 * final JLabel state = new JLabel();
 * 		
 * mydevice.addFunctionHandler(new FunctionHandler() {
 * 			
 * 		public void execute(String name, Map<String, String> input) {
 *  			if(name.equals("SwitchOn")){
 * 				state.setText("Switched on");
 * 			}else{
 * 				state.setText("Switched off");
 * 			}			
 * 		}
 * 	}
 * );
 * 		
 * mydevice.onReady(new OnReady() {
 * 	
 * 	public void ready() {
 * 		JFrame frame = new JFrame("My switch");
 * 		
 * 		frame.getContentPane().add(state, BorderLayout.NORTH);
 * 		
 * 		frame.setSize(200, 200);
 * 		
 * 		frame.setVisible(true);
 * 			
 * 	}
 * });
 * 		
 * mydevice.connect("ws://localhost:8080/iot/websockets/iot");
 * 
 * }
 * </pre>
 * @author Kureem Rossaye
 * 
 */
public class Device implements OnConnectedListener {

	private DeviceDefinition definition;

	private String server;

	private WebSocketLayer websocket;

	private OnReady onready = null;

	private boolean initialised = false;
	
	private String deviceId;

	public Device(String deviceId,String definitionId, String groupId, String versionId, DefinitionRegistry registry) {
		definition = registry.getDefinition(definitionId,groupId,versionId);
		this.deviceId = deviceId;
	}
	
	public Device(String deviceId,DeviceDefinition definition) {
		
		this.definition = definition;
		this.deviceId = deviceId;

	}

	public void setWebsocketLayer(WebSocketLayer layer) {
		this.websocket = layer;
		websocket.addOnConnectedListener(this);
	}

	public Device setSpec(String key, String value) {
		definition.getSpecs().put(key, value);
		return this;
	}

	public Device registerEvent(String name, String description) {
		EventDefinition event = new EventDefinition();
		event.setName(name);
		event.setDescription(description);
		definition.getEvents().add(event);
		return this;
	}

	public void handshake() {
		UpStream up = new UpStream();
		up.setDeviceId(deviceId);
		up.setType(UpStream.HANDSHAKE);
		websocket.sendRequest(up, definition);
	}

	public void propagateEvent(String event, Map<String, String> parameters) {
		UpStream request = new UpStream();
		request.setType(UpStream.IO);
		request.setDeviceId(deviceId);
		OnEvent e = new OnEvent();
		e.setName(event);
		e.setParameters(parameters);
		websocket.sendRequest(request, e);

	}
	
	public void propagateEvent(String event) {
		propagateEvent(event, new HashMap<String, String>());

	}


	private Device registerFunction(String name, String description) {
		FunctionDefinition function = new FunctionDefinition();
		function.setName(name);
		function.setDescription(description);
		definition.getFunctions().add(function);
		return this;

	}

	public Device addFunctionHandler(FunctionHandler handler) {
		websocket.addFunctionHandler(handler);
		return this;
	}

	public Device setDisconnectedDeviceHandler(DisconnectedDeviceHandler handler) {
		websocket.setDisconnectedDeviceHandler(handler);
		return this;
	}

	public Device connect(String server) {
		this.server = server;
		websocket.connect(server);
		return this;
	}

	public Device reconnect() {
		connect(server);
		return this;
	}

	public boolean isConnected() {
		return websocket.isConnected();
	}

	public Device disconnect() throws IOException {
		websocket.disconnect();
		return this;
	}

	@Override
	public void onConnected(Device device) {
		UpStream stream = new UpStream();
		stream.setDeviceId(deviceId);
		stream.setType(UpStream.HANDSHAKE);
		websocket.sendRequest(stream, definition);
		if (!initialised) {
			initialised = true;
			if (onready != null) {
				onready.ready();
			}
		} else {

		}

	}

	public Device onReady(OnReady function) {
		this.onready = function;
		return this;
	}

}
