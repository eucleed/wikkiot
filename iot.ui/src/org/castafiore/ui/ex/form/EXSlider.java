package org.castafiore.ui.ex.form;

import java.util.Map;

import org.castafiore.ui.Container;
import org.castafiore.ui.StatefullComponent;
import org.castafiore.ui.UIException;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.js.JMap;
import org.castafiore.ui.js.Var;

public class EXSlider extends EXContainer implements StatefullComponent<Integer>, Event{
	
	private String orientation = "horizontal";
	
	private int min =0;;
	
	private int max = 100;
	
	private boolean animate = false;
	
	private int step = 1;
	
	

	@Override
	public void onReady(JQuery proxy) {
		super.onReady(proxy);
		
		String s = "$( '"+proxy.getIdRef()+"' ).slider( 'option', 'value' )";
		JQuery p = proxy.clone().makeServerRequest( new JMap().put("val", new Var(s)),this);
		
		JMap par = new JMap().put("origntation", orientation).put("min", min).put("max", max).put("animate", animate).put("step", step).put("value", getAttribute("value"));
		
		par.put("stop",p, "event", "ui");
		proxy.invoke("slider", par);
	}

	public EXSlider(String name) {
		super(name, "div");
		setAttribute("value", "0");
		addEvent(this, Event.MISC);
		
	}

	

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
		setRendered(false);
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
		setRendered(false);
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
		setRendered(false);
	}

	public boolean isAnimate() {
		return animate;
	}

	public void setAnimate(boolean animate) {
		this.animate = animate;
		setRendered(false);
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
		setRendered(false);
	}

	@Override
	public void ClientAction(JQuery container) {
		String s = "$( '"+container.getIdRef()+"' ).slider( 'option', 'value' )";
		container.makeServerRequest( new JMap().put("val", new Var(s)),this);
		
	}

	@Override
	public boolean ServerAction(Container container, Map<String, String> request)
			throws UIException {
		String val = request.get("val");
		setAttribute("value", val);
		SliderHandler p = getAncestorOfType(SliderHandler.class);
		if(p != null)
			p.propagate();
		return true;
	}

	@Override
	public void Success(JQuery container, Map<String, String> request)
			throws UIException {
		
	}

	@Override
	public void setEnabled(boolean enabled) {
		
	}

	@Override
	public Integer getValue() {
		try{
			return Integer.parseInt(getAttribute("value"));
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public void setValue(Integer value) {
		setAttribute("value", value.toString());
	}

	
}
