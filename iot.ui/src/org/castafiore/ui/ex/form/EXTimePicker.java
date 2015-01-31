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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.castafiore.utils.StringUtil;
/**
 * 
 * 
 * @author Kureem Rossaye<br>
 *          kureem@gmail.com
 * Oct 22, 2008
 */
public class EXTimePicker extends AbstractEXTextInput<Date> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	
	
	public EXTimePicker(String name, Date value) {
		super(name, value);
		applyMask("99-99-9999");
	}

	public EXTimePicker(String name) {
		super(name);
		applyMask("99-99-9999");
	}

	@Override
	public Date toObject(String s) {
		try{
			if(StringUtil.isNotEmpty(s)){
				return format.parse(s);
			}else{
				return null;
			}
		}
		catch(Exception e){
			throw new ValidationException(e);
		}
	}

	@Override
	public String toString(Date object) {
		
		if(object == null)
			return "";
		return format.format(object);
	}
	
	

	
	
	


}
