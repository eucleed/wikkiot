package org.castafiore.iot.sample;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.castafiore.iot.client.Device;
import org.castafiore.iot.client.OnReady;
import org.castafiore.iot.definitions.DeviceDefinition;
import org.castafiore.iot.driver.JavaWebsocketLayer;

public class RemoteControl  {


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		DeviceDefinition definition = Util.client.getDefinition("Remote", "eucleed.iot", "1.0");
		//create an instance of the device
		final Device remotecontrol = new Device("MyRemote", definition);
		//register the 2 events
		remotecontrol.registerEvent("OnSwitchOn", "Switch on");
		remotecontrol.registerEvent("OnSwitchOff", "Switch off");
		
		
		remotecontrol.onReady(new OnReady() {

			@Override
			public void ready() {

				String title = "Remote control";
				JFrame frame = new JFrame(title);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JButton switchon = new JButton("Switch on");
				switchon.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						remotecontrol.propagateEvent("OnSwitchOn");
					}
				});
				
				JButton switchoff = new JButton("Switch off");
				switchoff.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						remotecontrol.propagateEvent("OnSwitchOff");
					}
				});
				
				Container contentPane = frame.getContentPane();
				contentPane.add(switchon, BorderLayout.NORTH);
				contentPane.add(switchoff, BorderLayout.SOUTH);

				frame.setSize(150, 100);
				frame.setVisible(true);

			}
		});
		//set WebsocketLayer implementation
		remotecontrol.setWebsocketLayer(new JavaWebsocketLayer(remotecontrol));
		//connect to server
		remotecontrol.connect("ws://localhost:8081/iot/websockets/iot");
		

	}

}
