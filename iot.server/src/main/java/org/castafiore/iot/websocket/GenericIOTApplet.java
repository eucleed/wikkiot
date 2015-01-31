package org.castafiore.iot.websocket;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.castafiore.iot.Device;
import org.castafiore.iot.DeviceRegistry;
import org.castafiore.iot.IOTApplet;

public abstract  class GenericIOTApplet implements IOTApplet {

	private DeviceRegistry registry;

	private List<String> devices = new LinkedList<String>();

	private List<String> connectedDevices = new LinkedList<String>();

	@Override
	public DeviceRegistry getRegistry() {
		return registry;
	}

	public void setRegistry(DeviceRegistry registry) {
		this.registry = registry;
	}

	public IOTApplet addRequiredDevice(String deviceId) {

		if (!devices.contains(deviceId)) {
			devices.add(deviceId);
		}
		return this;
	}

	public Device findDevice(String deviceId) {
		for (String d : connectedDevices) {
			if (d.equals(deviceId)) {
				return getRegistry().getDevice(deviceId);
			}
		}
		return null;
	}

//	public Device findDeviceByName(String deviceName) {
//		for (String d : connectedDevices) {
//			Device device = getRegistry().getDevice(d);
//			if (device.getName().equals(deviceName)) {
//				return device;
//			}
//		}
//		
//		throw new RuntimeException("The device " + deviceName + " does not seem to be registered. Please switch on the device. And check if it working properly");
//	}

	@Override
	public void onDeviceConnected(Device device) {
		
		connectedDevices.remove(device.getDeviceId());
		connectedDevices.add(device.getDeviceId());
		initDevice(device);

	}
	
	public abstract void initDevice(Device device);

	@Override
	public Collection<String> getRequiredDevices() {
		return devices;
	}

}
