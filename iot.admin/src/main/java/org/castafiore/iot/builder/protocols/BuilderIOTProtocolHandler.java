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
package org.castafiore.iot.builder.protocols;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.castafiore.iot.DeviceRegistry;
import org.castafiore.iot.IOTApplet;
import org.castafiore.iot.IOTProtocolHandler;
import org.castafiore.utils.BaseSpringUtil;


public class BuilderIOTProtocolHandler extends IOTProtocolHandler{
	
	private List<IOTApplet> applets = new LinkedList<IOTApplet>();
	
	@Override
	@OnMessage
	public void OnMessage(Session session, String msg, boolean last) {
		super.OnMessage(session, msg, last);
	}

	@Override
	@OnClose
	public void OnClose(Session session, CloseReason reason) {
		super.OnClose(session, reason);
	}

	@Override
	public Collection<IOTApplet> getApplets() {
		if(applets.size() == 0){
			applets.add(new BuilderAppRuntime());
		}
		return applets;
	}

	@Override
	public DeviceRegistry getRegistry() {
		return BaseSpringUtil.getBeanOfType(BuilderRegistry.class);
	}

}
