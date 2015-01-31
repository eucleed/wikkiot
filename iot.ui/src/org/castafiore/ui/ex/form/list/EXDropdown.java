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

import java.util.ArrayList;
import java.util.Map;

import org.castafiore.ui.Container;
import org.castafiore.ui.StatefullComponent;
import org.castafiore.ui.UIException;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.ex.form.EXInput;
import org.castafiore.ui.ex.form.button.EXButton;
import org.castafiore.ui.js.Var;




/**
 * 
 * 
 * @author Kureem Rossaye<br>
 *          kureem@gmail.com
 * Oct 22, 2008
 */
public class EXDropdown<T>  extends EXContainer implements Event, StatefullComponent<T>{
	
	
	private EXInput input;
	
	private EXButton addOn = (EXButton)new EXButton("addOn", "<span class=\"caret\"></span>").addEvent(this, Event.CLICK).setAttribute("data-toggle", "dropdown");
	
	private EXButton addOnRefresh = (EXButton)new EXButton("addOnRefresh", "<span class=\"icon icon-refresh\"></span>").addEvent(this, CLICK);
	
	private Container dropDownCtn = new EXContainer("", "li");
	
	private Container defaultCtn = new EXContainer("defaultCtn","div").addClass("default-ctn");
	
	private DropDownFactory<T> factory_;
	
	private java.util.List<OnChangeListener<T>> changeListeners = new ArrayList<OnChangeListener<T>>(1);
	
	private T value;
	
	public EXDropdown(String name, DropDownFactory<T> factory) {
		
		super(name,"div");
		addClass("input-group");
		this.factory_ = factory;
		input = new EXInput(name + "_input");
		input.addClass("form-control");
		addChild(input);
		
		addOn.setType(EXButton.TYPE_DEFAULT);
		addOn.addClass("dropdown-toggle");
		addOnRefresh.addClass("dropdown-toggle");
		addOnRefresh.setType(EXButton.TYPE_DEFAULT);
		//addOnRefresh.addEvent(this, CLICK);
		
		Container inputGroupBtn = new EXContainer("", "div").addClass("input-group-btn");
		addChild(inputGroupBtn.addChild(addOn).addChild(addOnRefresh));
		
		
		
		Container menu = new EXContainer("menu", "ul").addClass("dropdown-menu pull-right default").setAttribute("role", "menu").addChild(dropDownCtn);
		inputGroupBtn.addChild(menu);
		
		
		defaultCtn.setStyle("background", "url(img/loading.gif) no-repeat scroll 50% center rgba(0, 0, 0, 0)");
		defaultCtn.addChild(new EXContainer("loading", "h3").setText("Loading..."));
		
		
		dropDownCtn.addChild(defaultCtn);
		
	}
	
	
	public void addOnChangeListener(OnChangeListener<T> listener){
		this.changeListeners.add(listener);
	}


	public Container getDropDownHolder(){
		return dropDownCtn;
	}

	public void setDropdownFactory(DropDownFactory<T> factory){
		this.factory_ = factory;
	}

	@Override
	public void ClientAction(JQuery container) {
	//	container.server(this);
		container.IF(container.getAttribute("init").equal(new Var("true")), container.clone(), container.clone().setTimeout(container.clone().server(this), 1));
		
	}


	@Override
	public boolean ServerAction(Container container, Map<String, String> request)
			throws UIException {
		
		
		if(container.getName().equalsIgnoreCase("addOnRefresh")){
			addOn.setAttribute("init", "false");
			this.dropDownCtn.getChildren().clear();
			this.dropDownCtn.addChild(this.defaultCtn.setRendered(false));
			this.dropDownCtn.setRendered(false);
			return true;
		}
		
		Container first = dropDownCtn.getChildByIndex(0);
		if(first.getName().equals("defaultCtn") || "always".equals(first.getAttribute("reload"))){
			
			request.put("toremove", first.getId());
			
			dropDownCtn.getChildren().remove(0);
			Container dp = this.factory_.createDropDown(this).setDisplay(false);
			request.put("toshow", dp.getId());
			dropDownCtn.addChild(dp);
			container.setAttribute("init", "true");
			return true;
			
		}else{
			return true;
		}
	}


	@Override
	public void Success(JQuery container, Map<String, String> request)
			throws UIException {
		if(request.containsKey("toremove")){
			String toremove = request.get("toremove");
			
			
			if(request.containsKey("toshow")){
				container.append(new JQuery("#" + toremove).fadeOut(500,new JQuery("#" + request.get("toshow")).fadeIn(1000)));
				//container.append(new JQuery("#" + request.get("toshow")).fadeIn(1000));
			}else{
				container.append(new JQuery("#" + toremove).remove());
			}
		}else{
			if(request.containsKey("toshow")){
				//container.append(new JQuery("#" + toremove).fadeOut(500,new JQuery("#" + request.get("toshow")).fadeIn(1000)));
				container.append(new JQuery("#" + request.get("toshow")).fadeIn(1000));
			}
		}
		
		
		
	}


	@Override
	public T getValue() {
		return value;
	}


	@Override
	public void setValue(T value) {
		
		this.value = value;
		if(value != null)
			input.setValue(value.toString());
		else
			input.setValue("");
		
	}

	
	public void changeValue(T value){
		T oldValue = getValue();
		setValue(value);
		
		for(OnChangeListener<T> l : changeListeners){
			l.onChange(this,oldValue, value);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		
	}
	
	
	public interface OnChangeListener<T> {
		
		public void onChange(EXDropdown<T>  field,T oldValue, T newValue);
		
	}
	
	
	
	
	
	
	

}
