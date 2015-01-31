package org.castafiore.ui.ex.toolbar;

import org.castafiore.ui.Container;
import org.castafiore.ui.ex.EXContainer;

public class EXBreadcrumb extends EXContainer{

	public EXBreadcrumb(String name) {
		super(name, "ol");
	}
	
	public Container addItem(String label){
		Container li = new EXContainer("", "li");
		addChild(li);
		Container item = new EXContainer("", "a").setAttribute("href", "#").setText(label);
		li.addChild(item);
		return item;
	}

}
