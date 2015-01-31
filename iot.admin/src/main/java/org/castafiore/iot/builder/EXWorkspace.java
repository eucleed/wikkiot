/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castafiore.iot.builder;

import java.util.List;

import org.castafiore.iot.admin.EXDevice;
import org.castafiore.ui.Container;
import org.castafiore.ui.tabbedpane.ChangeTabListener;
import org.castafiore.ui.tabbedpane.EXTabPanel;
import org.castafiore.ui.tabbedpane.TabModel;
import org.castafiore.ui.tabbedpane.TabPanel;

public class EXWorkspace extends EXTabPanel implements TabModel,ChangeTabListener{
	
	
	private final static String[] labels = new String[]{"Design", "Script", "Source"};
	
	
	private EXLayout layout = new EXLayout("layout");
	
	private EXScript script = new EXScript("script");
	
	private EXSource source = new EXSource();
	
	
	
	private EXDevice selected;

	public EXWorkspace(String name) {
		super(name);
		setType(EXTabPanel.TYPE_PILLS);
		setModel(this);
		addSelectTabListener(this);
		
	}
	
	public void setSelected(EXDevice device){
		this.selected = device;
		
		script.setSelected(device);
	}
	
	public List<EXDevice> getDevices(){
		return layout.getDevices();
	}

	@Override
	public String getTabLabelAt(TabPanel pane, int index, boolean selected) {
		return labels[index];
	}

	@Override
	public Container getTabContentAt(TabPanel pane, int index) {
		if(index == 0){
			return layout;
		}else if (index == 1){
			return script;
		}else{
			return source;
		}
	}

	@Override
	public int getSelectedTab() {
		return 0;
	}

	@Override
	public int size() {
		return labels.length;
	}

	@Override
	public void onSelect(TabPanel pane, TabModel model, Container tabContent,
			int index, Container tab) {
		if(index == 1){
			script.setSelected(selected);
		}
		if(index == 2){
			source.refreshContent();
		}
		// TODO Auto-generated method stub
		
	}

}
