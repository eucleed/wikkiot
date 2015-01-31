package org.castafiore.iot.sample;

import java.util.Map;

import org.castafiore.iot.Device;
import org.castafiore.iot.EventListener;
import org.castafiore.iot.websocket.GenericIOTApplet;
 
public class RemoteControlApplet extends GenericIOTApplet {

	public RemoteControlApplet(){
		//add the required devices to be used
		addRequiredDevice("MyLamp");
		addRequiredDevice("MyRemote");
		
	}

	@Override
	public void initDevice(Device device) {
		//when device is first initialised, we add the listener for the 2 events on the remote control
		if(device.getDeviceId().equals("MyRemote")){
			device.addEvent(new OnSwitchOn(), "OnSwitchOn");
			device.addEvent(new OnSwitchOff(), "OnSwitchOff");
		}
		
	}

	
	/**
	 * Implementation of the OnSwitchOn listener
	 *
	 */
	class OnSwitchOn implements EventListener{

		@Override
		public void execute(Device source, String type,
				Map<String, String> parameters) {
			//finds the device and invoke the SwitchOn function
			findDevice("MyLamp").invoke("SwitchOn");
			
		}
		
	}
	
	class OnSwitchOff implements EventListener{

		@Override
		public void execute(Device source, String type,	Map<String, String> parameters) {
			
			//finds the Lamp device and invokes the SwitchOff event
			findDevice("MyLamp").invoke("SwitchOff");
			
		}
		
	}

}
