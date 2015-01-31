/*
 * Copyright (C) 2007-2010 Castafiore
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
 package org.castafiore.ui.ex.layout;

import org.castafiore.ui.Container;
import org.castafiore.ui.LayoutContainer;
import org.castafiore.ui.PopupContainer;

public class EXBorderLayoutContainer extends EXMigLayout implements LayoutContainer , PopupContainer {
	
	public final static String TOP = "0:0";
	
	public final static String LEFT = "0:1";
	
	public final static String CENTER  = "1:1";
	
	public final static String RIGHT = "2:1";
	
	public final static String BOTTOM  = "0:2";
	

	
	
	public EXBorderLayoutContainer() {
		this("Border Layout");
		
		
	}

	public EXBorderLayoutContainer(String name) {
		
		super(name, "12;3:6:3;12");

	}

	@Override
	public void addPopup(Container popup) {
		addChild(popup);
		
	}
	


	
	
	
	
	
}
