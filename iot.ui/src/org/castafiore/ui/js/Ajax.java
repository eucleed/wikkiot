package org.castafiore.ui.js;

import org.castafiore.ui.engine.JQuery;

public class Ajax implements JSObject{
	
	private JMap options = new JMap();
	public Ajax(String url, JMap options){
		this.options = options;
		options.put("url", url);
	}
	
	
	public Ajax setData(JMap data){
		options.put("data", data);
		return this;
	}
	
	
	public Ajax onSuccess(JQuery function){
		options.put("success", function, "data", "textStatus", "jqXHR");
		return this;
	}
	
	public Ajax onComplete(JQuery function){
		options.put("complete", function, "data", "textStatus", "jqXHR");
		return this;
	}
	
	public Ajax onError(JQuery function){
		options.put("error", function, "data", "textStatus", "jqXHR");
		return this;
	}
	public JMap getOptions(){
		return options;
	}

	@Override
	public String getJavascript() {
		
		String ajax = "$.ajax(" + options.getJavascript() + ");";
		
		return ajax;
	}

}
