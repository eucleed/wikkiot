package org.castafiore.ui.ex.layout;

import java.util.ArrayList;
import java.util.List;

import org.castafiore.InvalidLayoutDataException;
import org.castafiore.ui.Container;
import org.castafiore.ui.LayoutContainer;
import org.castafiore.ui.ex.EXContainer;

public class EXCardLayout extends EXContainer implements LayoutContainer{
	
	

	public EXCardLayout(String name, String tagName) {
		super(name, tagName);
		
	}
	
	public void show(int index){
		int count = 0;
		for(Container c : getChildren()){
			if(count == index){
				if(!c.isVisible()){
					c.setDisplay(true);
				}
			}else{
				if(c.isVisible()){
					c.setDisplay(false);
				}
			}
			count++;
		}
	}
	
	public void show(String name){
		for(Container c : getChildren()){
			if(c.getName().equals(name)){
				if(!c.isVisible()){
					c.setDisplay(true);
				}
			}else{
				if(c.isVisible()){
					c.setDisplay(false);
				}
			}
		}
	}

	@Override
	public void addChild(Container child, String layoutData) {
		addChild(child);
		
	}

	@Override
	public List<Container> getChildren(String layoutData) {
		return getChildren();
	}

	@Override
	public Container getChild(String name, String layoutData) {
		return getChild(name);
	}

	@Override
	public Container getDescendentOfType(Class<? extends Container> type,
			String layoutData) {
		return getDescendentOfType(type);
	}

	@Override
	public Container getDescendentByName(String name, String layoutData) {
		return getDescendentByName(name);
	}

	@Override
	public Container getDescendentById(String id, String layoutData) {
		return getDescendentById(id);
	}

	@Override
	public void validateLayoutData(String layoutData)
			throws InvalidLayoutDataException {
		
	}

	@Override
	public List<DroppableSection> getSections() {
		List<DroppableSection> sections = new ArrayList<DroppableSection>(1);
		sections.add(new DroppableSection(getId(), getName(), ""));
		return sections;
	}

	@Override
	public Container getContainer(String layoutData) {
		return this;
	}

	@Override
	public void removeChildFromLayout(String id) {
		getDescendentById(id).remove();
		
	}

}
