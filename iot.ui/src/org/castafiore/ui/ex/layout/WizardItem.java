package org.castafiore.ui.ex.layout;

import org.castafiore.ui.Container;

public interface WizardItem<T> extends Container{
	
	public boolean validate();
	
	public void collect(T value);

}
