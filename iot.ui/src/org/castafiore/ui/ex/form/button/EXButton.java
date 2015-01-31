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

package org.castafiore.ui.ex.form.button;

import org.castafiore.JQContants;
import org.castafiore.ui.ex.EXContainer;


/**
 * 
 * 
 * @author Kureem Rossaye<br>
 *          kureem@gmail.com
 * Oct 22, 2008
 */
public class EXButton extends EXContainer implements Button {
	
	
	public static String TYPE_DEFAULT = "btn-default";
	public static String TYPE_PRIMARY = "btn-primary";
	public static String TYPE_SUCCESS = "btn-success";
	public static String TYPE_INFO = "btn-info";
	public static String TYPE_WARNING = "btn-warning";
	public static String TYPE_DANGER = "btn-danger";
	public static String TYPE_LINK = "btn-link";
	
	
	public static String[] TYPES = new String[]{TYPE_DEFAULT,TYPE_PRIMARY,TYPE_SUCCESS,TYPE_INFO,TYPE_WARNING,TYPE_DANGER, TYPE_LINK};
	
	
	public static String SIZE_SMALL="btn-sm";
	public static String SIZE_EXTRA_SMALL="btn-xs";
	public static String SIZE_NORMAL="btn-n";
	public static String SIZE_LARGE="btn-lg";
	
	
	public static String[] SIZES = new String[]{SIZE_SMALL,SIZE_EXTRA_SMALL,SIZE_NORMAL,SIZE_LARGE};
	

	public EXButton(String name, String label) {
		super(name, "button");
		addClass("btn");
		setType(TYPE_DEFAULT);
		setText(label);
	
	}
	
	
	public EXButton setType(String type){
		for(String s : TYPES){
			removeClass(s);
		}
		
		addClass(type);
		
		return this;
	}
	
	public EXButton setSize(String size){
		for(String s : SIZES){
			removeClass(s);
		}
		
		addClass(size);
		
		return this;
	}
	
	public void setEnabled(boolean enabled){
		if(enabled){
			removeClass("disabled");
		}else{
			addClass("disabled");
		}
	}
	
}
