package org.castafiore.ui.tabbedpane;

import org.castafiore.ui.Container;

public interface ChangeTabListener {
	
	
	public void onSelect(TabPanel pane, TabModel model,Container tabContent, int index, Container tab);

}
