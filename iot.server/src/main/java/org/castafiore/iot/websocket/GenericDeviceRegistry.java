package org.castafiore.iot.websocket;

import java.util.LinkedList;
import java.util.List;

import org.castafiore.iot.Device;
import org.castafiore.iot.DeviceRegistry;
import org.castafiore.iot.definitions.DeviceDefinition;

public class GenericDeviceRegistry implements DeviceRegistry {


	private List<Device> devices = new LinkedList<Device>();

	@Override
	public Device associate(String deviceId,DeviceDefinition definition) {

		//definitionRegistry.publishDefinition(definition);
		Device device = getDevice(deviceId);
		if (device != null) {
			devices.remove(device);
		}
		device = new Device(deviceId,definition);
		devices.add(device);
		return device;

	}

	@Override
	public Device getDevice(String deviceId) {
		for (Device d : devices) {
			if (d.getDeviceId().equals(deviceId)) {
				return d;
			}
		}
		return null;
	}

	@Override
	public List<Device> getDevices() {
		return devices;
	}

}
