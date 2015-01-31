package org.castafiore.ui.interceptors;

import java.util.Map;

import org.castafiore.ui.Container;
import org.castafiore.ui.Scrollable;
import org.castafiore.ui.UIException;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.events.Event;

public class ScrollableInterceptor implements Interceptor, Event{

	@Override
	public Interceptor next() {
		return null;
	}

	@Override
	public boolean onRender(Container container) {
		int eventType = Event.SCROLL;
		if(container instanceof Scrollable){
			Scrollable sc = (Scrollable)container;
			
			//container.setAttribute("appid", container.getRoot().getName()).setAttribute("eventid", this.hashCode() + "");
			//container.setAttribute("scrolheight", sc.getHeightBeforeScroll() + "");
			if(!container.hasEvent(ScrollableInterceptor.class, eventType) && sc.isScrollable()){
				container.addEvent(this, eventType);
			}else if(container.hasEvent(ScrollableInterceptor.class, eventType) && !sc.isScrollable()){
				container.removeEvent(ScrollableInterceptor.class, eventType);
			}
		}
		
		
		return true;
	}

	@Override
	public void ClientAction(JQuery container) {
		container.eval("var me = $(this);var scrlTop = me[0].scrollTop; if(scrlTop < me[0].scrollHeight - me.height())return;");
		
		container.mask(container.getAncestorOfType(Scrollable.class), "Loading...").append(container.clone().server(this));
		
		container.append(container.clone().eval("me.scrollTop(me[0].scrollTop);"));
		//container.addMethod("scrollable", new JMap());
		
	}

	@Override
	public boolean ServerAction(Container container, Map<String, String> request)
			throws UIException {
		if(container instanceof Scrollable){
			((Scrollable)container).addPage();
		}
		
		return true;
	}

	@Override
	public void Success(JQuery container, Map<String, String> request)
			throws UIException {
		// TODO Auto-generated method stub
		
	}

}
