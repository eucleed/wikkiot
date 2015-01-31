package org.castafiore.ui.ex.form.list;

import org.castafiore.ui.Container;

public interface DropDownFactory<T> {
	
	public Container createDropDown(EXDropdown<T> input);

}
