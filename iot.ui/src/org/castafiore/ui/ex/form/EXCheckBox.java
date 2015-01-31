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

package org.castafiore.ui.ex.form;

import java.util.Map;

import org.castafiore.ui.Container;
import org.castafiore.ui.UIException;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.js.Expression;

/**
 * 
 * 
 * @author Kureem Rossaye<br>
 *          kureem@gmail.com
 * Oct 22, 2008
 */
public class EXCheckBox extends AbstractStatefullComponent<Boolean>{
	
	public EXCheckBox(String name) {
		this(name, false);
	}
	
	public EXCheckBox(String name, boolean checked) {
		super(name, "input");
		setReadOnlyAttribute("type", "checkbox");
		removeClass("form-control");
		setValue(checked); 
		
	}

	
	public boolean isChecked(){
		String checked = getAttribute("checked");
		if("checked".equals(checked)){
			return true;
		}else{
			return false;
		}
	}
	
	public void setChecked(boolean checked){
		setValue(checked);
			
	}
	
	
	
	
	
	public void onReady(JQuery proxy){
		if(isChecked()){
			proxy.setAttribute("checked", "checked");
		}
	}

	@Override
	public Boolean getValue() {
		String checked = getAttribute("value");
		if("checked".equals(checked)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void setValue(Boolean value) {
		if(value!=null && value.equals(Boolean.TRUE)){
			setAttribute("checked", "checked");
		}else{
			setAttribute("checked", (String)null);
		}
		setRendered(false);
		
	}

	@Override
	public void setEnabled(boolean enabled) {
		if(enabled){
			removeClass("disabled");
		}else{
			addClass("disabled");
		}
		
	}

}
