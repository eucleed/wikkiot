package org.castafiore.iot;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

import org.castafiore.iot.definitions.DeviceDefinition;
import org.castafiore.iot.io.Execute;
import org.castafiore.iot.io.DownStream;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A server side representation of a device.<br>
 * 
 * When a device is connected, it propagates it {@link DeviceDefinition} using
 * the iot protocol.<br>
 * This class holds the device definition and the Websocket {@link Session}<br>
 * 
 * It also hold the {@link EventListener}s to execute application scenario when
 * event is propagated.<br>
 * 
 * It also encapsulates the complexities of the iot protocol for invoking a
 * function of the physical device.<br>
 * 
 * 
 * @author Kureem Rossaye
 * 
 * 
 * 
 */
public class Device {

	private DeviceDefinition definition;

	protected static ObjectMapper mapper = new ObjectMapper();
	
	private Session session;
	
	private Map<String, List<EventListener>> listeners = new LinkedHashMap<String, List<EventListener>>();
	
	private String deviceId;

	/**
	 * Constructs a device using the {@link DeviceDefinition} propagated by a
	 * physical device
	 * 
	 * @param definition
	 *            - The device definition propagated by the physical device
	 *            using the iot protocol over a websocket layer
	 */
	public Device(String deviceId,DeviceDefinition definition) {
		super();
		this.definition = definition;
		this.deviceId = deviceId;
	}

	/**
	 * Returns the name of the device.<br>
	 * Note that although the name is not universally unique, it should be
	 * unique in the context it is being used<br>
	 * The name is actually a human readable identifier of the the device
	 * 
	 * @return - The name of the device
	 */
	public String getName() {
		return definition.getName();
	}

	/**
	 * Connects the remote device to this representation of the device.
	 * 
	 * @param session
	 *            - The websocket session created by the physical device
	 */
	public void connect(Session session) {
		this.session = session;
	}

	/**
	 * Manually disconnect the device from the server.
	 * 
	 * @throws IOException
	 *             - IO Exception if the remote device is not accessible.
	 */
	public void disconnect() throws IOException {
		if (isConnected()) {
			session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE,
					"Closed normally"));

		}
	}

	/**
	 * Checks if the device is live.
	 * 
	 * @return - Whether the device is connected or not
	 */
	public boolean isConnected() {
		if (session != null && session.isOpen()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Adds a listener for the specified event type
	 * 
	 * @param listener
	 *            - The listener invoked when the specified event is propagated
	 *            by the physical device.
	 * @param type
	 *            - The type of event. The type of event should be one of the
	 *            events published by the device upon registration
	 * @return - This device
	 */
	public Device addEvent(EventListener listener, String type) {
		if (listeners.containsKey(type)) {
			listeners.get(type).add(listener);
		} else {
			listeners.put(type, new LinkedList<EventListener>());
			listeners.get(type).add(listener);
		}
		return this;
	}

	/**
	 * Propagates the specified event by executing the listener.<br>
	 * This method is to be called by the IOT Container<br>
	 * Developers do not generally execute this method in order to avoid
	 * inconsistent states.
	 * 
	 * @param type
	 *            - The type of event to propagate
	 * @param parameters
	 *            - The parameters passed when device propagates its event.
	 */
	public void propageEvents(String type, Map<String, String> parameters) {

		if (listeners.containsKey(type)) {
			for (EventListener listener : listeners.get(type)) {
				listener.execute(this, type, parameters);
			}
		}
	}

	/**
	 * @return The universal unique id of the device
	 * 
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Invokes a function defined in the {@link DeviceDefinition}
	 * 
	 * @param method
	 *            - The method to invoke
	 */
	public void invoke(String method) {
		invoke(method, new LinkedHashMap<String, String>());
	}

	/**
	 * Invokes a function defined in the {@link DeviceDefinition} with specified
	 * parameters
	 * 
	 * @param method
	 *            - The method to invoke
	 * @param parameters
	 *            - The parameters to send downstream
	 */
	public synchronized void invoke(String method,
			Map<String, String> parameters) {
		try {
			DownStream response = new DownStream();
			Execute execute = new Execute();
			execute.setFunction(method);
			execute.setParameters(parameters);
			response.setBody(mapper.writeValueAsString(execute));
			response.setType(DownStream.TYPE_EXECUTE);
			session.getAsyncRemote().sendText(
					mapper.writeValueAsString(response));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 
	 * @return The device definition of this device published by remote device
	 *         wrapper
	 */
	public DeviceDefinition getDefinition() {
		return definition;
	}

}
