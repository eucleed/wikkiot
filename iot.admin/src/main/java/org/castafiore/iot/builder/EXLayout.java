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

import java.util.LinkedList;
import java.util.List;

import org.castafiore.iot.admin.EXDevice;
import org.castafiore.iot.builder.json.Application;
import org.castafiore.iot.builder.json.Device;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.ex.dynaform.EXFieldSet;

public class EXLayout extends EXContainer{
	
	private Application application;
	
	private EXDevice selected;
	
	private List<EXDevice> devices = new LinkedList<EXDevice>();

	public EXLayout(String name) {
		super(name, "div");
		New();
	}
	
	
	public void addDevice(EXDevice device){ 
		
		
		for(EXDevice d : devices){
			if(device.getDevice().getDeviceId().equals(d.getDevice().getDeviceId())){
				throw new RuntimeException("Cannot add same device more than once in an application");
			}
		}
		Device d = new Device();
		d.setId(device.getDevice().getDeviceId());
	
		application.getDevices().add(d);
		EXDevice dev = new EXViewDevice(device.getDevice());
		devices.add(dev);
		addChild(dev);
		selectDevice(dev);
	}
	
	public String getScript(String deviceId, String event){
		for(Device d : application.getDevices()){
			if(d.getId().equals(deviceId)){
				if(d.getEvents().containsKey(event)){
					return d.getEvents().get(event);
				}
			}
		}
		
		return "";
	}
	
	public void setScript(String deviceId, String event, String script){
		for(Device d : application.getDevices()){
			if(d.getId().equals(deviceId)){
				d.getEvents().put(event, script);
			}
		}
		
	}
	
	public EXDevice getDevice(String deviceId){
		for(EXDevice d : devices){
			if(d.getDevice().getDeviceId().equals(deviceId)){
				return  d;
			}
		}
		
		return null;
	}
	
	
	public void removeDevice(EXDevice device){
		getChildren().remove(device);
		devices.remove(device);
		setRendered(false);
	}
	
	public void selectDevice(String deviceId){
		for(EXDevice d : devices){
			if(d.getDevice().getDeviceId().equals(deviceId)){
				selectDevice(d);
			}
		}
	}
	public void selectDevice(EXDevice device){
		if(selected != null){
			selected.removeClass("device-selected");
		}
		this.selected = device;
		device.addClass("device-selected");
		getAncestorOfType(EXWorkspace.class).setSelected(device);
		
	}
	
	public void New(){
		devices.clear();
		getChildren().clear();
		setRendered(false);
		application = new Application();
	}
	
	public void refreshLayout(){
		for(EXDevice device : devices){
			device.setDevice(device.getDevice());
		}
	}
	
	
	public void close(){
		
	}
	
	public List<EXDevice> getDevices(){
		return devices;
	}
	
	
	public Application getApplication(){
		return application;
	}
	
	public EXDevice getSelected(){
		return selected;
	}

}
