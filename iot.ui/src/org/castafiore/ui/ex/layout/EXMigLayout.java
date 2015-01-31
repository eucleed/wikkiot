package org.castafiore.ui.ex.layout;

import java.util.ArrayList;
import java.util.List;

import org.castafiore.InvalidLayoutDataException;
import org.castafiore.ui.Container;
import org.castafiore.ui.LayoutContainer;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.ex.layout.DroppableSection;
import org.castafiore.utils.StringUtil;
/**
 * Sample layout configuration
 * 
 * 12;4:4:4;3:3:6
 * 
 * 
 * 
 * @author acer
 *
 */
public  class EXMigLayout extends EXContainer implements LayoutContainer{
	
	

	
	
	public EXMigLayout(String name, String layout) {
		super(name, "div");
		
	//	addClass("container");
		
		setLayout(layout);
	}
	
	
	public void setWidth(int gridWidth, String layoutData){
		Container c = getContainer(layoutData);
		for(int i =1; i <=12; i++)
			c.removeClass("col-md-" + i);
		
		c.addClass("col-md-" + gridWidth);
	}
	
	
	/**
	 * Sets layout in row and column
	 * 
	 * sample 
	 *12;4:4:4;3:3:6<br>
	 *
	 *3 rows<br>
	 * 1st row-> 1 column 12 sections large
	 * 2nd row-> 3 columns 4 sections each
	 * 3rd row-> 3 columns with 1st and  2nd column 3 sections large and 3rd 6 sections large
	 * @param layout
	 */
	public void setLayout(String layout){
		String[] rows = StringUtil.split(layout, ";");
		for(String row : rows){
			Container uirow= new EXContainer("", "div").addClass("row");
			addChild(uirow);
			String[] cols = StringUtil.split(row, ":");
			for(String col : cols){
				Container uicol = new EXContainer("", "div").addClass("col-md-" + col);
				uirow.addChild(uicol);
			}
		}
	}



	

	@Override
	public void addChild(Container child, String layoutData) {
		
		 getContainer(layoutData).addChild(child);
		
	}


	@Override
	public List<Container> getChildren(String layoutData) {
		return getContainer(layoutData).getChildren();
	}


	@Override
	public Container getChild(String name, String layoutData) {
		// TODO Auto-generated method stub
		return getContainer(layoutData).getChild(name);
	}


	@Override
	public Container getDescendentOfType(Class<? extends Container> type,
			String layoutData) {
		// TODO Auto-generated method stub
		return getContainer(layoutData).getDescendentOfType(type);
	}


	@Override
	public Container getDescendentByName(String name, String layoutData) {
		// TODO Auto-generated method stub
		return getContainer(layoutData).getDescendentByName(name);
	}


	@Override
	public Container getDescendentById(String id, String layoutData) {
		return getContainer(layoutData).getDescendentById(id);
	}


	@Override
	public void validateLayoutData(String layoutData)
			throws InvalidLayoutDataException {
		
		
	}


	@Override
	public List<DroppableSection> getSections() {
		List<DroppableSection> result = new ArrayList<DroppableSection>();
		int irow = 0;
		int icol = 0;
		for(Container row : getChildren()){
			icol  =0;
			for(Container col : row.getChildren()){
				DroppableSection section = new DroppableSection(col.getId(), "Cell " + icol + ":" + irow, icol + ":" + irow);
				result.add(section);
				icol++;
			}
			
			irow++;
		}
		return result;
	}


	@Override
	public Container getContainer(String layoutData) {
		String[] coord = StringUtil.split(layoutData, ":");
		try{
		return getChildByIndex(Integer.parseInt(coord[1])).getChildByIndex(Integer.parseInt(coord[0]));
		}catch(Exception e){
			throw new InvalidLayoutDataException("invalid layout data:" + layoutData,e);
		}
	}


	@Override
	public void removeChildFromLayout(String id) {
		getDescendentById(id).remove();
		
	}



	
	
	

}
