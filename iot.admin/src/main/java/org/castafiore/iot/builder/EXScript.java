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
import java.util.Map;

import org.castafiore.KeyValuePair;
import org.castafiore.SimpleKeyValuePair;
import org.castafiore.iot.admin.EXDevice;
import org.castafiore.iot.definitions.DeviceDefinition;
import org.castafiore.iot.definitions.EventDefinition;
import org.castafiore.ui.Container;
import org.castafiore.ui.UIException;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.ex.form.EXTextArea;
import org.castafiore.ui.ex.form.list.DefaultDataModel;
import org.castafiore.ui.ex.form.list.EXSelect;
import org.castafiore.ui.ex.layout.EXMigLayout;

public class EXScript extends EXMigLayout implements Event{

	
	private EXTextArea script = new EXTextArea("script");
	
	private EXSelect<String> events = new EXSelect<String>("event", new DefaultDataModel<String>());
	
	private EXSelect<KeyValuePair> devices = new EXSelect<KeyValuePair>("devices", new DefaultDataModel<KeyValuePair>());
	
	private EXDevice selected;
	
	public EXScript(String name) {
		super(name, "6:6;12");
		addChild(devices,"0:0");
		addChild(events, "1:0");
		addChild(script, "0:1");
		devices.addEvent(this,CHANGE);
		script.addEvent(this, BLUR);
		events.addEvent(this,CHANGE);
		
	}
	
	public void setSelected(EXDevice device){
		this.selected = device;
		
		doSelect(device);
	}
	
	
	protected void doSelect(EXDevice device){
		 DefaultDataModel<String> mm = new DefaultDataModel<String>();
		 List<EventDefinition> defns = new LinkedList<EventDefinition>();
		if(device != null){
			 DeviceDefinition definition = device.getDevice().getDefinition();
			 
			 defns = definition.getEvents();
			
			 for(EventDefinition def : defns){
				 mm.addItem(def.getName());
			 }
		}
		 
		 this.events.setModel(mm);
		 
		 
		 DefaultDataModel<KeyValuePair> devs  = new DefaultDataModel<KeyValuePair>();
		 if(getAncestorOfType(EXWorkspace.class) != null){
			 List<EXDevice> devices = getAncestorOfType(EXWorkspace.class).getDevices();
			 for(EXDevice dev : devices){
				 String key = dev.getDevice().getDeviceId();
				 String label = dev.getDevice().getDefinition().getName() + " -> " + dev.getDevice().getDefinition().getLabel();
				 devs.addItem(new SimpleKeyValuePair(key, label));
			 }
		 }
		 
		 this.devices.setModel(devs);
		 if(selected != null)
			 this.devices.setValue(new SimpleKeyValuePair(selected.getDevice().getDeviceId(), ""));
		 
		 if(defns.size() > 0){
			 events.setSelectedValue(0);
		 }
		 updateScript();
	}
	
	public void updateScript(){
		String script = "";
		if(devices.getModel().getSize() > 0){
			KeyValuePair kv = devices.getValue();
			
			String deviceId = kv.getKey();
			
			if(events.getModel().getSize() > 0){
			String event = events.getValue();
			
			 script = getAncestorOfType(EXWorkspace.class).getDescendentOfType(EXLayout.class).getScript(deviceId, event);
			}
		}
		
		this.script.setText(script);
	}

	@Override
	public void ClientAction(JQuery container) {
		container.server(this);
		
	}

	@Override
	public boolean ServerAction(Container container, Map<String, String> request)
			throws UIException {
		if(container.getName().equals("devices")){
			KeyValuePair kv = devices.getValue();
			
			String deviceId = kv.getKey();
			
			
			getAncestorOfType(EXWorkspace.class).getDescendentOfType(EXLayout.class).selectDevice(deviceId);
		}else if(container.getName().equals("event")){
			updateScript();
		}else{
			//
			KeyValuePair kv = devices.getValue();
			
			String deviceId = kv.getKey();
			
			if(events.getModel().getSize() > 0){
				String event = events.getValue();
				String script = this.script.getValue();
				getAncestorOfType(EXWorkspace.class).getDescendentOfType(EXLayout.class).setScript(deviceId, event, script);
			}
		}
		
		return true;
	}

	@Override
	public void Success(JQuery container, Map<String, String> request)
			throws UIException {
		
	}

}
