package org.castafiore.iot;

import java.util.Map;

/**
 * 
 * Interface listening to events propagated by a remote device.
 * 
 * 
 * 
 * @author Kureem Rossaye
 *
 */
public interface EventListener {
	
	/**
	 * Executed the device holding the {@link EventListener} propagates an event of the specified type.
	 * 
	 * @param source  The device propagating the event
	 * @param type  The type of even propagated
	 * @param parameters  The parameters passed while propagating the event.
	 */
	public void execute(Device source, String type,Map<String, String> parameters);

}
