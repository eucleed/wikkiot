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
package org.castafiore.ui.tabbedpane;

import java.util.LinkedList;
import java.util.List;

import org.castafiore.JQContants;
import org.castafiore.ui.Container;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.utils.ComponentUtil;

public class EXTabPanel extends EXContainer implements JQContants, TabPanel {

	protected TabModel model;

	private TabRenderer tabRenderer = new BTTabRenderer();
	
	protected Container tabs = new EXContainer("tabs", "ul").addClass("nav").addClass("nav-tabs");
	
	public final static String TYPE_PILLS = "nav-pills";
	
	public final static String TYPE_BREADCRUMBS = "nav-breadcrumbs";
	
	public final static String TYPE_TABS = "nav-tabs";
	
	private List<ChangeTabListener> selectTabListeners = new LinkedList<ChangeTabListener>();

	public EXTabPanel(String name) {
		super(name, "div");
	}
	
	

	public EXTabPanel(String name, TabModel model) {
		this(name);
		setModel(model);

	}
	
	public EXTabPanel setType(String type){
		tabs.removeClass(TYPE_PILLS).removeClass(TYPE_BREADCRUMBS).removeClass(TYPE_TABS).addClass(type);
		return this;
	}
	
	public void addSelectTabListener(ChangeTabListener listener){
		selectTabListeners.add(listener);
	}
	
	public void fireOnSelectTabListeners(Container tab, Integer tabIndex, Container tabContent){
		for(ChangeTabListener l : selectTabListeners){
			l.onSelect(this, getModel(), tabContent, tabIndex, tab);
		}
	}

	public TabModel getModel() {
		return model;
	}
	
	public void setShowTabs(boolean show){
		getChild("tabs").setDisplay(show);
	}
	
	public EXTabPanel setEnableTab(int index,boolean enable){
		if(!enable){
			tabs.getChildByIndex(index).addClass("disabled");
		}else{
			tabs.getChildByIndex(index).removeClass("disabled");
		}
		return this;
	}

	public void setModel(TabModel model) {

		this.model = model;

		this.getChildren().clear();
		this.setRendered(false);
		if (model == null) {
			return;
		}

		//Container tabs = ComponentUtil.getContainer("tabs", "ul", null, null);
		//tabs.addClass("nav").addClass("nav-tabs");
		addChild(tabs);

		int selectedTab = model.getSelectedTab();
		for (int i = 0; i < model.size(); i++) {

			Container tab = tabRenderer.getComponentAt(this, model, i);
			tabs.addChild(tab);
			tab.setAttribute("t", i + "");
			tab.setAttribute("init", "false");
			tab.addEvent(EVT_SHOW_TAB, Event.CLICK);

			Container content = ComponentUtil.getContainer("c-" + i, "div", "",
					null).addClass("tab-pane");
			content.setAttribute("init", "false");
			content.setDisplay(false);
			addChild(content);

			// initialise default selected tab
			if (i == selectedTab) {
				tabRenderer.onSelect(this, model, i, tab);
				// content.setAttribute("class",
				// TABS_SELECTED_TAB_CONTENT_STYLE);
				content.setDisplay(true);
				content.addChild(model.getTabContentAt(this, i));
				content.setAttribute("init", "true");
				tab.setAttribute("init", "true");
				content.addClass("active");
			}

		}

	}
	
	
	

	public TabRenderer getTabRenderer() {
		return tabRenderer;
	}

	public void setTabRenderer(TabRenderer tabRenderer) {
		this.tabRenderer = tabRenderer;
		setModel(model);
	}

	public static class BTTabRenderer implements TabRenderer {

		public Container getComponentAt(TabPanel pane, TabModel model, int index) {
			Container tab = ComponentUtil.getContainer("tt", "a", "", null);
			tab.setAttribute("href", "#tabs-" + index);
			tab.setText(model.getTabLabelAt(pane, index, false));
			EXContainer tTab = new EXContainer("", "li");
			tab.setAttribute("data-toggle", "tab");
			tTab.addChild(tab);
			return tTab;
		}

		public void onSelect(TabPanel pane, TabModel model, int index,
				Container tab) {
			tab.addClass("active");
		}

		public void onDeselect(TabPanel pane, TabModel model, int index,
				Container tab) {
			tab.removeClass("active");
		}

	}

}
