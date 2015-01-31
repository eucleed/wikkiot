package org.castafiore.iot;

import java.util.Collection;

/**
 * An {@link IOTApplet} is a logical application that allows interaction with {@link Device}s.<br>
 * The Applet is give access to the {@link DeviceRegistry} to load {@link Device}s.<br> 
 * 
 * @author Kureem Rossaye
 *
 */
public interface IOTApplet {

	/**
	 * This is called when the devices added via {@link #addRequiredDeviceName(String)} is switched on and authenticated
	 * 
	 * @param device  The device connected
	 */
	public void onDeviceConnected(Device device);

	/**
	 * Method to return efficiently the {@link DeviceRegistry} associated to the container holding the {@link IOTApplet}
	 * @return  The device registry
	 */
	public DeviceRegistry getRegistry();

	/**
	 * Find the {@link Device} with the specified device id added via {@link #addRequiredDeviceName(String)}
	 * @param deviceId - The device id looking for
	 * @return  The device with the specified device id
	 */
	public Device findDevice(String deviceId);


	/**
	 * 
	 * @return  List of devicenames added via {@link #addRequiredDeviceName(String)}
	 */
	public Collection<String> getRequiredDevices();
	
	/**
	 * Adds a device to the applet. 
	 * By adding the device, method {@link #onDeviceConnected(Device)} will be captured and passing the device with the specified name.
	 * It is not possible to add a concrete implementation of the device since the device in question is created internally when it is connected.<br>
	 * In a sense, the device become concrete when it is ready to use.
	 * 
	 * @param deviceName  The name of the device to add
	 * @return  This iot applet
	 */ 
	public IOTApplet addRequiredDevice(String deviceId);

}
