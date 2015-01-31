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
 package org.castafiore.ui.navigation;

import org.castafiore.ui.Container;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.tabbedpane.ChangeTabListener;
import org.castafiore.ui.tabbedpane.TabModel;
import org.castafiore.ui.tabbedpane.TabPanel;
import org.castafiore.ui.tabbedpane.TabRenderer;

public class EXAccordeon extends EXContainer implements  TabPanel {
	
	private TabModel model;

	public EXAccordeon(String name) {
		super(name, "div");
		addClass("panel-group");
	}

	public TabModel getModel() {
		return model;
	}

	public void setModel(TabModel model) {
		this.model = model;
		refresh();
	}
	
	public void refresh(){
		
		this.getChildren().clear();
		this.setRendered(false);
		
		
		int size = model.size();
		for(int i =0; i < size;i++){
			Container item = getPanel(model.getTabLabelAt(this, i, false), model.getTabContentAt(this, i));
			addChild(item);
		}
	}
	
	
	private Container getPanel(String label, Container content){
		Container panel = new EXContainer("", "panel").addClass("panel").addClass("panel-default");
		
		Container panelHeading  = new EXContainer("panelHeading", "div").addClass("panel-heading");
		panel.addChild(panelHeading);
		
		Container panelTitle = new EXContainer("panelTitle", "div").addClass("panel-title");
		panelHeading.addChild(panelTitle);
		
		Container a = new EXContainer("a", "a").setAttribute("data-toggle", "collapse").setAttribute("data-parent", "#" + getId()).setAttribute("href", "#");
		panelTitle.addChild(a);
				
		Container colapse = new EXContainer("colapse", "div").addClass("panel-collapse collapse in");
		panel.addChild(colapse);
		
		Container panelBody = new EXContainer("panelBody", "div").addClass("panel-body");
		colapse.addChild(panelBody);
		
		panelBody.addChild(content);
		
		a.setAttribute("href", "#" + colapse.getId());
		a.setText(label);
		return panel;
		
				
	}

	@Override
	public TabRenderer getTabRenderer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSelectTabListener(ChangeTabListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fireOnSelectTabListeners(Container tab, Integer tabIndex,
			Container tabContent) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
