package org.castafiore.iot.sample;

import java.awt.BorderLayout;
import java.net.URL;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.castafiore.iot.client.Device;
import org.castafiore.iot.client.FunctionHandler;
import org.castafiore.iot.client.OnReady;
import org.castafiore.iot.definitions.DeviceDefinition;
import org.castafiore.iot.driver.JavaWebsocketLayer;

public class Lamp {

	
	
	
	
	public static void main(String[] args) {
		//1. Create an instance of device
		
		DeviceDefinition definition = Util.client.getDefinition("Lamp", "eucleed.iot", "1.0");
		final Device lamp = new Device("MyLamp",definition);
		
		
		
		//3. Set the type of communication layer. In this case it is a java implementation of websocket.
		//Depending the environment the driver is running, other implementation of the websocket layer can be injected
		lamp.setWebsocketLayer(new JavaWebsocketLayer(lamp));
		
		//a simple text label to display if lamp is on or off.
		//in a real lamp, instead displaying a label, the microcontroller will send an electric signal to a switch
		final JLabel state = new JLabel();
		
		//4. Add function handlers to execute the captured events
		//this implementation of the handler simply diplays Switched on or Switched off
		lamp.addFunctionHandler(new FunctionHandler() {
			
			@Override
			public void execute(String name, Map<String, String> input) {
				try{
				
					if(name.equals("SwitchOn")){
						state.setIcon(new ImageIcon(new URL(Util.ENDPOINT + "/img/light-on.png")));
					}else{
						state.setIcon(new ImageIcon(new URL(Util.ENDPOINT + "/img/light-off.png")));
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		});
		
		
		
		//5. This handler is executed when the device is connected and ready for use.
		lamp.onReady(new OnReady() {
			
			@Override
			public void ready() {
				JFrame frame = new JFrame("Lamp");
				frame.getContentPane().add(state, BorderLayout.NORTH);
				frame.setSize(300, 275);
				frame.setVisible(true);
				
			}
		});
		//6. connect the device to the hatzouf server
		lamp.connect(Util.WS_ENDPOINT + "/websockets/iot");
		
		
	}

}
