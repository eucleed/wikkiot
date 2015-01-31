package org.castafiore.ui.ex.form;

import org.castafiore.ui.engine.JQuery;
import org.castafiore.utils.StringUtil;

public abstract class AbstractEXTextInput<T> extends AbstractStatefullComponent<T>{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String mask="";
	
	public AbstractEXTextInput(String name, T value) {
		super(name, "input");
		setValue(value);
		setReadOnlyAttribute("type", "text");
		addClass("form-control");
	}
	

	public AbstractEXTextInput(String name) {
		super(name, "input");
		setReadOnlyAttribute("type", "text");
		addClass("form-control");
	}

	public void setPlaceHolder(String placeHolder) {
		setAttribute("placeholder", placeHolder);
	}

	public void setEnabled(boolean enabled) {
		if(enabled){
			setAttribute("disabled", (String)null);
		}else{
			setAttribute("disabled", "disabled");
		}
	}

	public boolean isEnabled() {
		return getAttribute("disabled").equals("disabled");
	}

	public EXInput setMaxLength(int length) {
		return (EXInput) setAttribute("maxlength", length + "");
	}

	
	public abstract T toObject(String s);
	
	public abstract String toString(T object);
	@Override
	public T getValue() {
		String val = getAttribute("value");
		return toObject(val);
	}

	@Override
	public void setValue(T value) {
		setAttribute("value", toString(value));

	}
	
	public  void applyMask( String mask)
	{	
		this.mask = mask;
		setRendered(false);	
	}
	
	public void onReady(JQuery container){
		if(StringUtil.isNotEmpty(mask)){
			container.invoke("mask", mask);
		}
	}

}
