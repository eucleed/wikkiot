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

import org.castafiore.ui.Container;
import org.castafiore.ui.UIException;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.ex.EXApplication;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.ex.form.button.EXButton;
import org.castafiore.ui.ex.layout.EXMigLayout;
import org.castafiore.ui.ex.panel.EXPanel;
import org.castafiore.ui.ex.toolbar.EXToolBar;
import org.castafiore.utils.JavascriptUtil;

public class EmulatorApp extends EXApplication implements Event {

	private EXButton addNew = new EXButton("addNew", "Add new device");

	private EXMigLayout main = new EXMigLayout("", "12;12");

	private Container devices = new EXContainer("devices", "div");

	public EmulatorApp() {
		super("emulator");
		addClass("container");
		EXToolBar tb = new EXToolBar("sda");
		tb.setStyle("margin", "12px");
		tb.addItem(addNew);
		addNew.addEvent(this, CLICK);
		main.addChild(tb, "0:0");

		Container devicec = new EXContainer("sda", "div").addClass("container");
		devicec.addChild(devices);
		main.addChild(devices, "0:1");

		// addChild(new Emulator());
		addDevice();

		addEvent(new Event() {

			@Override
			public void Success(JQuery container, Map<String, String> request)
					throws UIException {
			}

			@Override
			public boolean ServerAction(Container container,
					Map<String, String> request) throws UIException {
				return true;
			}

			@Override
			public void ClientAction(JQuery container) {
				container.eval("setInterval('"
						+ JavascriptUtil.javaScriptEscape(container.clone()
								.server(this).getCompleteJQuery()) + "', "
						+ 2000 + ")");

			}
		}, Event.READY);
		
		
		EXPanel panel = new EXPanel("panel");
		panel.setTitle("Device emulator");
		panel.setShowCloseButton(false);
		panel.setDraggable(false);
		//panel.setBody(new IOTBuilder("dd"));
		addChild(panel);
		
		panel.getDescendentByName("labelContainer").setStyleClass("panel-title");
		panel.getDescendentByName("widget-head").setStyleClass("panel-heading");
		panel.setStyleClass("panel panel-default container");
		panel.setStyle("margin", "12px");
		panel.getDescendentByName("bodyContainer").setStyleClass("panel-body");
		//EXPanel panel = new EXPanel("dd", "Device emulator");
		panel.setBody(main);
		
		//addChild(panel);
	}

	public void addDevice() {

			Emulator e = new Emulator();
			devices.addChild(new EXContainer("", "div").addClass("col-md-6").addChild(e));

	}

	private void setMd(int md) {
		for (Container c : devices.getChildren()) {
			c.setStyleClass("thumbnail col-md-" + md);
		}
	}

	@Override
	public void ClientAction(JQuery container) {
		container.server(this);

	}

	@Override
	public boolean ServerAction(Container container, Map<String, String> request)
			throws UIException {

		addDevice();
		return true;
	}

	@Override
	public void Success(JQuery container, Map<String, String> request)
			throws UIException {
		// TODO Auto-generated method stub

	}

}