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
package org.castafiore.iot.builder;

import java.util.List;

import org.castafiore.iot.Device;
import org.castafiore.iot.DeviceRegistry;
import org.castafiore.iot.admin.EXDevices;
import org.castafiore.utils.BaseSpringUtil;

public class EXDevicePanel extends EXDevices{
	
	
	
	public EXDevicePanel(){
		super();
		setTemplateLocation("templates/EXDevicePanel.xhtml");
	}
	
	
	public void refreshDevices(){
		
		List<Device> dds = BaseSpringUtil.getBeanOfType(DeviceRegistry.class).getDevices();
		
		devices.getChildren().clear();
		devices.setRendered(false);
		
		for(Device device : dds){
			devices.addChild(new EXDesignableDevice(device));
		}
	}
}
