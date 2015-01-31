package org.castafiore.ui.ex.form.table;

import java.util.Map;

import org.castafiore.ui.Container;
import org.castafiore.ui.StatefullComponent;
import org.castafiore.ui.UIException;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.ex.form.AbstractStatefullComponent;
import org.castafiore.ui.ex.form.EXInput;
import org.castafiore.ui.ex.form.list.EXDropdown;
import org.castafiore.ui.js.JMap;
import org.castafiore.ui.js.Var;

public class EXEditableTable extends EXTable implements Event{

	public EXEditableTable(String name, EditableTableModel model, EditableCellRenderer renderer) {
		super(name, model, renderer);
	}

	public EXEditableTable(String name, EditableTableModel model) {
		super(name, model);
	}


	public void setCellRenderer(EditableCellRenderer celleRendere) {
		super.setCellRenderer(celleRendere);
	}

	public void changePage(int page)
	{
		if(tableModel_ == null){
			return;
		}
		this.currentPage = page;
		Container tbody =  getChild("tbody");
		int rowsPerpage = tableModel_.getRowsPerPage();
		
		int rowCount = tableModel_.getRowCount();
		
		int maxRow = (page+1)*rowsPerpage;
		
		int rowsToShow = rowsPerpage;
		
		if(maxRow > rowCount)
			rowsToShow = rowCount - ((page)*rowsPerpage);
		
		for (int row = 0; row < rowsToShow; row++)
		{
			Container tr =  tbody.getChildByIndex(row);

			for (int col = 0; col < tableModel_.getColumnCount(); col++)
			{
				Container td = null;
				if(tr.getChildren().size() <= col)
				{
					td = new EXContainer("td", "td").setAttribute("c", col + "").setAttribute("r", row + "").setAttribute("m", "r");
					td.addEvent(this, CLICK);
					if(tableModel_.isCellEditable(row, col)){
						td.addEvent(this, CLICK);
					}
					
					tr.addChild(td);
				}
				else
				{
					td = tr.getChildByIndex(col);
					if(td.getEvents().size() == 0){
						td.setAttribute("c", col + "").setAttribute("r", row + "").setAttribute("m", "r");
						if(tableModel_.isCellEditable(row, col)){
							td.addEvent(this, CLICK);
						}else{
							td.getEvents().clear();
							td.setRendered(false);
						}
					}
				}


				Container component = null;
				if (td.hasChildren())
				{
					component =  td.getChildByIndex(0);
					celleRendere_.onChangePage(component, row, col, page, tableModel_, this);
				}
				else
				{
					component = celleRendere_.getComponentAt(row, col, page, tableModel_, this);
					td.addChild(component);
					td.setRendered(false);
				}

			}
			
			if(rowDecorator_ != null){
				rowDecorator_.decorateRow(row, (EXContainer)tr, this, tableModel_);
			}
		}
		
		if(rowsToShow < rowsPerpage)
		{
			for(int i = rowsToShow; i < rowsPerpage; i ++)
			{
				if(i < tbody.getChildren().size())
				{
					Container row = tbody.getChildByIndex(i);
					
					row.getChildren().clear();
					row.setRendered(false);
				}
			}
		}
	}

	@Override
	public void ClientAction(JQuery container) {
		container.mask().server(this);
		
	}
	
	
	public void startEdit(int row, int col, int page, Container container){
		Object value = super.tableModel_.getValueAt(col, row, page);
		StatefullComponent sft = ((EditableCellRenderer)this.celleRendere_).getInputAt(row, col, page,(EditableTableModel)tableModel_ , this);
		
		sft.setValue(value);
		
		container.getChildren().clear();
		container.setText("");
		container.setStyle("padding", "0");
		if(sft instanceof EXDropdown){
			sft.getChildByIndex(0).setStyle("margin", "0").setStyle("height", "27px").addEvent(this, BLUR).setAttribute("r", row + "").setAttribute("c", col + "").setAttribute("m", "e");
			//((EXInput)sft.getChildByIndex(0)).requestFocus();
			container.addChild(sft);
		}else{
			
			container.addChild(sft.addEvent(this, BLUR).addEvent(NAVIGATE, KEY_DOWN).setAttribute("r", row + "").setStyle("width", "100%").setAttribute("c", col + "").setAttribute("m", "e").setStyle("margin", "0").setStyle("height", "27px"));
			if(sft instanceof AbstractStatefullComponent){
				((AbstractStatefullComponent)sft).requestFocus();
			}
		}
		container.getEvents().clear();
	}
	
	public Container getTd(int col, int row){
		return getChild("tbody").getChildByIndex(row).getChildByIndex(col);
	}
	
	public void cancelEdit(int row, int col, int page, Container container){	
		Container td = container.getParent();
		StatefullComponent<?> stf = container.getAncestorOfType(StatefullComponent.class);
		if(td instanceof EXDropdown){
			stf = (StatefullComponent<?>)td;
			td = td.getParent();
			
		}
		
		//stf.setStyle("margin", "0");
		td.setStyle("padding", (String)null);
		Object value = stf.getValue();
		((EditableTableModel)tableModel_).setValueAt(col, row, page, value);
		Container c = celleRendere_.getComponentAt(row, col, page, tableModel_, this);
		td.getChildren().clear();
		td.setRendered(false);
		td.addEvent(this, CLICK);
		td.addChild(c);
		container.setAttribute("m", "r");
	}
	
	
	public static Event NAVIGATE = new Event() {
		
		@Override
		public void Success(JQuery container, Map<String, String> request)
				throws UIException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean ServerAction(Container container, Map<String, String> request)
				throws UIException {
			
			EXEditableTable table = container.getAncestorOfType(EXEditableTable.class);
			
			int row = Integer.parseInt(container.getAttribute("r"));
			int col = Integer.parseInt(container.getAttribute("c"));
			int page = table.currentPage;
			
			
			Container toEdit = null;
			do{
				if(col < table.tableModel_.getColumnCount() -1){
					col=col+1;
				}else{
					row = row+1;
					col =0;
				}
				toEdit = table.getTd(col, row);
			}while(!table.getModel().isCellEditable(row, col));
			
			table.startEdit(row, col, page, toEdit);
			
			
			return true;
		}
		
		@Override
		public void ClientAction(JQuery container) {
			Var key = new Var("(event.keyCode || event.which)");
			container.IF(key.equal(new Var("9")), container.clone().server(this), container.clone());
			
		}
	};

	@Override
	public boolean ServerAction(Container container, Map<String, String> request)
			throws UIException {
		int row = Integer.parseInt(container.getAttribute("r"));
		int col = Integer.parseInt(container.getAttribute("c"));
		int page = currentPage;
		boolean editMode =  container.getAttribute("m").equalsIgnoreCase("e");
		if(this.tableModel_.isCellEditable(row, col)){
		
			if(!editMode){
				startEdit(row, col, page, container);
			}else{
				cancelEdit(row, col, page, container);
			}
		}
		
		
		
		
		return true;
	}

	@Override
	public void Success(JQuery container, Map<String, String> request)
			throws UIException {
		// TODO Auto-generated method stub
		
	}
	
}
