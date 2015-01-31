package org.castafiore.iot;

import java.util.Collection;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.castafiore.utils.BaseSpringUtil;

@ServerEndpoint("/websockets/iot")
public class CastafioreIOTProtocolHandler extends IOTProtocolHandler {

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
	public DeviceRegistry getRegistry() {
		return BaseSpringUtil.getBeanOfType(DeviceRegistry.class);
	}

	@Override
	public Collection<IOTApplet> getApplets() {
		return BaseSpringUtil.getApplicationContext().getBeansOfType(IOTApplet.class).values();
	}

}
