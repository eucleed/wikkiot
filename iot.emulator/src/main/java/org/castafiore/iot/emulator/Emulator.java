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
package org.castafiore.iot.emulator;

import java.util.Map;

import org.castafiore.iot.client.Device;
import org.castafiore.iot.client.FunctionHandler;
import org.castafiore.iot.client.OnReady;
import org.castafiore.iot.definitions.DeviceDefinition;
import org.castafiore.iot.definitions.EventDefinition;
import org.castafiore.iot.driver.DefinitionRegistryClient;
import org.castafiore.iot.driver.JavaWebsocketLayer;
import org.castafiore.ui.Container;
import org.castafiore.ui.UIException;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.ex.form.EXInput;
import org.castafiore.ui.ex.form.button.EXButton;
import org.castafiore.ui.ex.form.button.EXButtonSet;
import org.castafiore.ui.ex.layout.EXMigLayout;
import org.castafiore.ui.ex.toolbar.EXToolBar;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Emulator extends EXMigLayout implements Event{

	private Device device;

	private EXInput deviceId = new EXInput("deviceId");
	
	private EXInput definitionId = new EXInput("definitionId");
	
	private EXInput groupId = new EXInput("groupId");
	
	private EXInput versionId = new EXInput("versionId");

	private EXInput server = new EXInput("server");

	private EXInput deviceStore = new EXInput("deviceStore");

	private EXButton connect = new EXButton("connect", "Connect");

	private EXButton disconnect = new EXButton("disconnect", "Disconnect");
	
	private EXToolBar buttons = new EXToolBar("toolbar");

	private Container invokedList = new EXContainer("invokedList", "ul")
			.addClass("list-group").setStyle("margin", "12px");
	
	Container inputs = new EXContainer("inputs", "div");
	
	EXMigLayout form = new EXMigLayout("form", "12;12");
	
	private Container title = new EXContainer("head", "div").addClass("panel-heading").setText("New Device");
			
			


	private static ObjectMapper mapper = new ObjectMapper();

	public Emulator() {
		super("emulator", "12;12;12");
		addClass("panel panel-default");
		
		buttons.setStyle("margin", "12px");

		form.addChild(inputs.setStyle("padding", "12px"), "0:0");

		addField(inputs, "Device Store:", deviceStore);
		addField(inputs, "WikkIOT server", server);
		addField(inputs, "Device Id:", deviceId);
		addField(inputs, "Devinition Id:", deviceId);
		addField(inputs, "Group Id:", deviceId);
		addField(inputs, "Version Id:", deviceId);

		//EXMigLayout buttons = new EXMigLayout("buttons", "6:6");
		buttons.addClass("container");

		buttons.addChild(connect);
		buttons.addChild(disconnect);
		connect.addEvent(this, CLICK);
		form.addChild(buttons, "0:1");
		disconnect.setDisplay(false);
		addChild(form, "0:0");
		
		//addChild(eventList, "0:1");
		addChild(invokedList, "0:1");
		String text = "<span style=\"cursor: pointer\" title=\"Logs Arbitrary invocation from iot server\" class=\"badge\">?</span>Invocation logs";
		invokedList.addChild(new EXContainer("head", "li").addClass("list-group-item active").setText(text));
		
		addChildAt(title, 0);

	}

	private void addField(Container parent, String label, EXInput input) {
		Container group = new EXContainer("g", "div");
		group.setStyle("margin", "12px 0px");

		group.addChild(input.setStyleClass("form-control").setAttribute("placeholder", label));
		parent.addChild(group);

	}

	public void connect() {
		try {
			String store = deviceStore.getValue();
			String defid = definitionId.getValue();
			String grpId = groupId.getValue();
			String vid = versionId.getValue();
			
			DeviceDefinition definition = new DefinitionRegistryClient(store).getDefinition(defid, grpId, vid);
			//String json = ResourceUtil.readUrl(store + "/castafiore/resource?spec=iot:get#" + defid + "/" + grpId + "/" + vid);

			//DeviceDefinition definition = mapper.readValue(json.getBytes(), DeviceDefinition.class);
			
			setDefinition(definition);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	
	public void disconnect(){
		try{
		device.disconnect();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public void setDefinition(final DeviceDefinition definition) {
		String did = deviceId.getId();
		device = new Device(did,definition);
		
		
		device.setWebsocketLayer(new JavaWebsocketLayer(device));
		device.onReady(new OnReady() {
			
			@Override
			public void ready() {
				disconnect.setStyle("display", "inline").addClass("btn-danger").setStyle("float", "left");
				connect.setStyle("display", "none");
				buttons.getDescendentByName("events").setDisplay(true);
				inputs.setDisplay(false);
				title.setText("<img src="+definition.getIcon()+" width=16px>    " + definition.getLabel());
				System.out.println("connected");
			}
		});

		device.addFunctionHandler(new FunctionHandler() {

			@Override
			public void execute(String name, Map<String, String> input) {

				Container li = new EXContainer(name, "li")
						.addClass("list-group-item list-group-item-info");
				String text = "<span class=\"badge\">" + input.toString()
						+ "</span> Invoked :" + name;
				li.setText(text);
				invokedList.addChildAt(li, 1);

			}
		});

		EXButtonSet set = new EXButtonSet("events");
		set.setDisplay(false);
		buttons.addItem(set);
		for (EventDefinition defn : definition.getEvents()) {
			EXButton btn = new EXButton(defn.getName(), defn.getName());
			btn.addEvent(this, CLICK);
			set.addItem(btn);
		}
		
		device.connect(this.server.getValue());

	}

	@Override
	public void ClientAction(JQuery container) {
		container.server(this);
		
	}

	@Override
	public boolean ServerAction(Container container, Map<String, String> request)
			throws UIException {
		if(container.getName().equals("connect")){
			connect();
		}else if(container.getName().equals("disconnect")){
			disconnect();
		}else{
			device.propagateEvent(container.getName());
		}
		
		return true;
	}

	@Override
	public void Success(JQuery container, Map<String, String> request)
			throws UIException {
		// TODO Auto-generated method stub
		
	}

}
