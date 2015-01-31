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

import org.castafiore.ui.ex.EXApplication;
import org.castafiore.ui.ex.panel.EXPanel;

public class Builder extends EXApplication{

	public Builder() {
		super("builder");
		EXPanel panel = new EXPanel("panel");
		panel.setTitle("Give it a try");
		panel.setShowCloseButton(false);
		panel.setDraggable(false);
		panel.setBody(new IOTBuilder("dd"));
		addChild(panel);
		
		panel.getDescendentByName("labelContainer").setStyleClass("panel-title");
		panel.getDescendentByName("widget-head").setStyleClass("panel-heading");
		panel.setStyleClass("panel panel-default container");
		panel.setStyle("margin", "12px");
		
	}

}
