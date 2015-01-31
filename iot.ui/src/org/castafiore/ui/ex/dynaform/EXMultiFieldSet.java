package org.castafiore.ui.ex.dynaform;

import java.util.ArrayList;
import java.util.List;

import org.castafiore.ui.Container;
import org.castafiore.ui.StatefullComponent;
import org.castafiore.ui.tabbedpane.EXTabPanel;
import org.castafiore.ui.tabbedpane.TabModel;
import org.castafiore.ui.tabbedpane.TabPanel;

public class EXMultiFieldSet extends EXTabPanel implements TabModel{
	
	private List<String> labels = new ArrayList<String>();
	
	private List<EXFieldSet> fieldsets = new ArrayList<EXFieldSet>();

	public EXMultiFieldSet(String name) {
		super(name);
	}

	@Override
	public String getTabLabelAt(TabPanel pane, int index, boolean selected) {
		return labels.get(index);
	}

	@Override
	public Container getTabContentAt(TabPanel pane, int index) {
		return fieldsets.get(index);
	}

	@Override
	public int getSelectedTab() {
		return 0;
	}

	@Override
	public int size() {
		return labels.size();
	}
	
	public EXMultiFieldSet addTab(String name,String label, boolean doublecol){
		labels.add(label);
		fieldsets.add(new EXFieldSet(name, label,doublecol));
		return this;
	}
	
	
	public void refresh(){
		super.refresh();
		setModel(this);
		
		setShowTabs(labels.size() >1);
		
		for(EXFieldSet fs : fieldsets){
			fs.setShowTitle(labels.size() ==1);
		}
	}
	
	public void addField(String name, String label,StatefullComponent field){
		for(EXFieldSet f : fieldsets){
			if(f.getName().equals(name)){
				f.addField(label, field);
			}
		}
	}

}
