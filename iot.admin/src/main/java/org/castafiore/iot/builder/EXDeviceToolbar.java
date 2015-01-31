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

import java.util.Map;

import org.castafiore.ui.Container;
import org.castafiore.ui.UIException;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.ex.form.button.EXButtonSet;
import org.castafiore.ui.ex.toolbar.EXToolBar;

public class EXDeviceToolbar extends EXToolBar implements Event{
	
	private Container save = new EXContainer("save", "img").setAttribute("src", "http://icons.iconarchive.com/icons/oxygen-icons.org/oxygen/24/Actions-document-save-icon.png");
	
	private Container create = new EXContainer("create", "img").setAttribute("src", "http://icons.iconarchive.com/icons/gakuseisean/ivista-2/24/Files-New-File-icon.png");
	
	private Container refresh = new EXContainer("refresh", "img").setAttribute("src", "http://icons.iconarchive.com/icons/oxygen-icons.org/oxygen/24/Actions-view-refresh-icon.png");
	//
	private Container exit = new EXContainer("exit", "img").setAttribute("src", "http://icons.iconarchive.com/icons/hopstarter/sleek-xp-basic/24/Close-2-icon.png");
	

	public EXDeviceToolbar() {
		super("toolbar");
		
		EXButtonSet set = new EXButtonSet("set");
		addItem(set);
		save.addEvent(this, CLICK);
		set.addChild(create.addEvent(this, CLICK).addClass("btn btn-default"));
		set.addChild(save.addClass("btn btn-default"));
		
		set.addChild(refresh.addEvent(this, CLICK).addClass("btn btn-default"));
		set.addChild(exit.addEvent(this, CLICK).addClass("btn btn-default"));
		setStyle("margin", "12px 0px");
	}

	@Override
	public void ClientAction(JQuery container) {
		container.server(this);
		
	}

	@Override
	public boolean ServerAction(Container container, Map<String, String> request)
			throws UIException {
		
		if(container.getName().equals("save")){
			
		}else{
			
		}
		
		return true;
	}

	@Override
	public void Success(JQuery container, Map<String, String> request)
			throws UIException {
		// TODO Auto-generated method stub
		
	}
	
	

}
