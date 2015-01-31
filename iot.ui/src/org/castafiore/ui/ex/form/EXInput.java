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


/**
 * 
 * @author Kureem Rossaye<br>
 *         kureem@gmail.com June 27 2008
 */
public class EXInput extends AbstractStatefullComponent<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private boolean numeric = false;

	public EXInput(String name, String value) {
		super(name, "input");
		setValue(value);
		setReadOnlyAttribute("type", "text");
		addClass("form-control");
	}
	

	public EXInput(String name) {
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

	@Override
	public String getValue() {
		String val = getAttribute("value");
		if(val == null){
			return "";
		}
		return getAttribute("value");
	}

	@Override
	public void setValue(String value) {
		setAttribute("value", value);

	}

}
