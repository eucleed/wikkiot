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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.castafiore.ui.Container;
import org.castafiore.ui.UIException;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.js.JArray;
import org.castafiore.ui.js.JMap;
import org.castafiore.utils.ResourceUtil;
/**
 * 
 * 
 * @author Kureem Rossaye<br>
 *          kureem@gmail.com
 * Oct 22, 2008
 */
public class EXAutoComplete extends EXInput implements Event{
	
	private AutoCompleteSource source_ = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> dictionary = new ArrayList<String>();
	
	
	
	
	public EXAutoComplete(String name, String value, List<String> dictionary) {
		super(name, value);
		if(dictionary != null)
			this.dictionary = dictionary;
		
		addEvent(this, Event.READY);
	}
	
	public EXAutoComplete setSource(AutoCompleteSource source){
		this.source_ = source;
		setRendered(false);
		return this;
	}
	
	public EXAutoComplete addInDictionary(String... value){
		if(value != null){
			for(String val : value){
				dictionary.add(val);
			}
		}
		setRendered(false);
		return this;
	}

	public EXAutoComplete(String name, String value) {
		this(name, value, null);
		
	}


	public void setDictionary(List<String> dictionary) {
		this.dictionary = dictionary;
		setRendered(false);
	}

	 public String readSource(String param){
		 if(source_ != null){
			 return source_.getSource(param).getJavascript();
		 }else{
			 return "{}";
		 }
	}
	
	public void ClientAction(JQuery container) {
		JMap opt = new JMap();
		if(source_ == null){
			JArray array = new JArray();
			for(String s : dictionary){
				array.add(s);
			}
			opt.put("source", array);
		}
		else
			opt.put("source", ResourceUtil.getMethodUrl(this, "readSource", "term"));
		container.invoke("autocomplete", opt);
		
	}


	public boolean ServerAction(Container container, Map<String, String> request)
			throws UIException {
		// TODO Auto-generated method stub
		return false;
	}


	public void Success(JQuery container, Map<String, String> request)
			throws UIException {
		// TODO Auto-generated method stub
		
	}
	
	
}
