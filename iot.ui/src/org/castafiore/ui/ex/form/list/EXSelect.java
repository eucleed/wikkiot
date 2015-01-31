/*
 * Copyright (C) 2007-2008 Castafiore
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.castafiore.ui.ex.form.list;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.castafiore.SimpleKeyValuePair;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.ex.form.AbstractStatefullComponent;
import org.castafiore.ui.js.JArray;
import org.castafiore.ui.js.JMap;
import org.castafiore.ui.mvc.CastafioreController;
import org.castafiore.utils.ResourceUtil;
import org.springframework.web.servlet.ModelAndView;


/**
 * 
 * @author Kureem Rossaye<br>
 *          kureem@gmail.com
 * June 27 2008
 */ 
public class EXSelect<T> extends AbstractStatefullComponent<T> implements CastafioreController{
	
	private DataModel<T> model_;
	public EXSelect(String name, DataModel<T> model) {
		super(name, "select");
		
		this.model_ = model;
		addClass("form-control");
		setAttribute("value", "0");
	}

	public EXSelect(String name, DataModel<T> model, T value) {
		super(name, "select");	
		setValue(value);
		addClass("form-control");
		setAttribute("value", "0");
	}
	
	

	
	public void setModel(DataModel<T> model){
		this.model_ = model;
		setRendered(false);
	}
	
	public DataModel<T> getModel(){
		return model_;
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

	@Override
	public T getValue() {
		int index = getSelectedIndex();
		return model_.getValue(index);
		
	}
	
	public void setSelectedValue(int index){
		setAttribute("value", index + "");
	}
	
	public int getSelectedIndex(){
		try{
		return Integer.parseInt(getAttribute("value"));
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public void setValue(T value) {
		int size = model_.getSize();
		if(value == null){
			setSelectedValue(0);
			return;
		}
		for(int i = 0; i < size;i++){
			
			T data = model_.getValue(i);
			if(data == null){
				return;
			}
			if(data.getClass().equals(value.getClass())){
				if(data.equals(value)){
					setAttribute("value", i + "");
					break;
				}
			}else if(value != null){
				SimpleKeyValuePair kv = new SimpleKeyValuePair(value.toString(),value.toString());
				if(data.equals(kv)){
					setAttribute("value", i + "");
					break;
				}
			}
		}		
	}

	
	

	
	public void onReady(JQuery proxy){
		proxy.invoke("select", new JMap().put("url", ResourceUtil.getMethodUrl(this)).put("selectedindex", getSelectedIndex()));
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		JArray data = new JArray();
		int size = model_.getSize();
		for(int i = 0; i < size;i++){
			JMap map = new JMap();
			map.put("key", i).put("value", model_.getValue(i).toString());
			data.add(map);
		}
		
		response.getOutputStream().write(data.getJavascript().getBytes());
		
		return null;
		
	}



	

}
