package org.castafiore.iot;

import java.util.Collection;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castafiore.iot.definitions.DeviceDefinition;
import org.castafiore.iot.io.DownStream;
import org.castafiore.iot.io.OnEvent;
import org.castafiore.iot.io.UpStream;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is the protocol layer. <br>
 * Its purpose it to interpret raw text message over a websocket layer according to the iot protocol specification and takes necessary action.<br>
 * Although the protocol handler could be written in any language, this implementation is solely for java web containers. It follows the websocket specification.<br>
 * 
 * 
 * @author Kureem Rossaye
 *
 */
public abstract class IOTProtocolHandler {

	protected static ObjectMapper mapper = new ObjectMapper();
	
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Main entry point behind the scene.<br>
	 * This class is used by iot server developers. They should extend this class to provide more user defined way to load {@link DeviceRegistry} and {@link IOTApplet}s<br>
	 * Subclass should include the class level annotation {@link ServerEndpoint} annotation.
	 * @param session  A websocket connection initated by a device
	 * @param msg  The text json message sent by the device according to the iot protocol.
	 * @param last  Indicates if this is a last message and the session has been closed.
	 */
	@OnMessage
	public void OnMessage(Session session, String msg, boolean last) {
		
		try {
			
			logger.debug("OnMessage:{" + session.getRequestURI() + "," + msg );
			UpStream request = mapper.readValue(msg.getBytes(), UpStream.class);

			String type = request.getType();

			DownStream response = new DownStream();
			String deviceId = request.getDeviceId();
			

			if (type.equals(UpStream.HANDSHAKE)) {

				DeviceDefinition definition = mapper.readValue(request.getBody(), DeviceDefinition.class);
				Device device= getRegistry().associate(deviceId,definition);
				logger.info("device registered:{" + device.getDeviceId() + "," + device.getName() + "}");
				device.connect(session);
				
				for(IOTApplet applet : getApplets()){
					Collection<String> requiredDevices = applet.getRequiredDevices();
					if(requiredDevices.contains(device.getDeviceId())){
						applet.onDeviceConnected(device);
						logger.info("Device connected:{" + device.getDeviceId() + "}");
					}
				}

				String result = mapper.writeValueAsString(response);
				session.getBasicRemote().sendText(result);
				logger.debug("Response sent after handshake:" + result);

			} else if (type.equals(UpStream.IO)) {
				OnEvent event = mapper.readValue(request.getBody(),OnEvent.class);
				getRegistry().getDevice(deviceId).propageEvents(event.getName(), event.getParameters());
				logger.debug("Event propagated:{" + event.getName() + "," + deviceId + "}");
			}

		} catch (Exception jme) {
			logger.error("Error while executing request:" + msg, jme);
			throw new RuntimeException(jme);	
		} 
	}
	
	

	/**
	 * Conventional onclose method of websocket.
	 * TODO  Delegate closing of session to application layer
	 * @param session  The websocket session being closed.
	 * @param reason  The reason why the websocket is being closed.
	 */
	@OnClose
	public void OnClose(Session session, CloseReason reason) {
		logger.debug("Session closed:" + session + reason.getReasonPhrase());
	}
	
	
	/**
	 * Conventional OnError event executed when any error occures.<br>
	 * TODO Delegate exception handling to application layer.
	 * @param session  The session on which the error has occured
	 * @param t The error
	 */
	@OnError
	public void onError(Session session, Throwable t){
		logger.error("Error in session:" + session, t);
	}
	
	/**
	 * The loading {@link IOTApplet} configured in the container is left over to custom implementations.<br>
	 * The {@link IOTApplet} can be configured in a spring application context, or in a persistent storage system.
	 * 
	 * @return  The list of {@link IOTApplet} configured in this container.
	 */
	public abstract Collection<IOTApplet> getApplets(); 
	

	/**
	 * The loading of the {@link DeviceRegistry} is left over to custom implementation<br>
	 * Server implementer can choose how connected devices are stored and retrieved. Either in memory, or in a persistent storage or in the cloud.<br>
	 * 
	 * @return {@link DeviceRegistry} associated to this container.
	 */
	public abstract DeviceRegistry getRegistry();


	


}
