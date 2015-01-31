package org.castafiore.iot;

import java.util.List;

import org.castafiore.iot.definitions.DeviceDefinition;

public interface DeviceRegistry {


	public Device getDevice(String deviceId);

	public Device associate(String deviceId, DeviceDefinition definition);
	
	public List<Device> getDevices();

}
