/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castafiore.iot.admin;

import org.castafiore.iot.Device;
import org.castafiore.ui.Container;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.scripting.EXXHTMLFragment;

public class EXDevice extends EXXHTMLFragment{

	
	private Device device ;
	
	private Container deviceId = new EXContainer("deviceId", "label");
	private Container status = new EXContainer("status", "div");
	private Container label = new EXContainer("label", "label");
	private Container name = new EXContainer("name", "label");
	private Container icon = new EXContainer("icon", "img");
	
	
	
	public EXDevice(Device device) {
		super(device.getDeviceId(), "templates/EXDevice.xhtml");
		setDevice(device);
		addChild(deviceId);
		addChild(status);
		addChild(label);
		addChild(icon);
		addChild(name);
	}
	
	public void setDevice(Device device){
		this.device = device;
		deviceId.setText(device.getDeviceId());
		name.setText(device.getName());
		label.setText(device.getDefinition().getLabel());
		icon.setAttribute("src", device.getDefinition().getIcon());
		if(device.isConnected()){
			status.setText("<span class=\"badge\">Connected</span>");
		}else{
			status.setText("<span class=\"badge off\">Disconnected</span>");
		}
	}
	
	public Device getDevice(){
		return device;
	}
	

}
