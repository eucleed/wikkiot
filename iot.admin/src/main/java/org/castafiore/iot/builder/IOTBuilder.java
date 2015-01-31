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

import org.castafiore.ui.ex.layout.EXMigLayout;

public class IOTBuilder extends EXMigLayout{

	
	private EXDevicePanel devices = new EXDevicePanel();
	
	private EXWorkspace workspace = new EXWorkspace("workspace");
	
	private EXDeviceInfo info = new EXDeviceInfo();
	
	private EXDeviceToolbar toolbar = new EXDeviceToolbar();
	
	public IOTBuilder(String name) {
		super(name, "12;1:9:3");
		addChild(toolbar, "0:0");
		addChild(devices, "0:1");
		addChild(workspace, "1:1");
		addChild(info, "2:1");
		devices.getParent().setStyle("padding-right", "0px").setStyle("margin-left", "5px");
		
		
	}

}
