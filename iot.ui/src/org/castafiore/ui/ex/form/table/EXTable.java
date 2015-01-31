/*
 * Copyright (C) 2007-2008 Castafiore
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

package org.castafiore.ui.ex.form.table;

import java.util.Map;

import org.castafiore.ui.Container;
import org.castafiore.ui.UIException;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.ex.EXAsynchronous;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.js.Function;
import org.castafiore.ui.js.JMap;
import org.castafiore.ui.js.Var;
import org.castafiore.utils.ResourceUtil;
import org.castafiore.utils.StringUtil;
/**
 * 
 * 
 * @author Kureem Rossaye<br>
 *          kureem@gmail.com
 * Oct 22, 2008
 */
public class EXTable extends EXContainer implements Table, Event
{

	protected TableModel tableModel_;

	protected CellRenderer celleRendere_ = new DefaultCellRenderer();
	
	protected TableColumnModel columnModel_ = new DefaultTableColumnModel();
	
	protected RowDecorator rowDecorator_ = new DefaultRowDecorator();
	
	protected int currentPage = 0;
	
	protected Container paginator = null;
	
	private Container tbody = new EXAsynchronous("tbody", "tbody");
	
	private Container thead = new EXContainer("thead", "thead");
	
	private String caption ="";

	public EXTable(String name, TableModel model)
	{
		super(name, "table");
		this.tableModel_ = model;
		addClass("table");
		//addClass("table table-striped table-hover");
		this.refresh();
		addEvent(this, MISC);

	}

	
	public EXTable(String name, TableModel model, CellRenderer renderer)
	{
		super(name, "table");
		this.tableModel_ = model;
		this.celleRendere_ = renderer;
		addClass("table");
		//addClass("table table-striped table-bordered table-hover");
		this.refresh();
		addEvent(this, MISC);

	}
	
	public void refreshCell(int row, int col){
		Container cell = this.tbody.getChildByIndex(row).getChildByIndex(col).getChildByIndex(0);
		this.celleRendere_.onChangePage(cell, row, col, currentPage, tableModel_, this);
	}
	
	public void setCaption(String caption){
		if(getChildren().size() == 0){
			Container uicaption = new EXContainer("caption", "caption").setText(caption);
			addChild(uicaption);
		}else if(!getChildByIndex(0).getName().equalsIgnoreCase("caption")){
			Container uicaption = new EXContainer("caption", "caption").setText(caption);
			addChildAt(uicaption, 0);
		}else{
			getChildByIndex(0).setText(caption);
		}
		this.caption = caption;
	}
	
	public void setBordered(boolean b){
		if(b){
			addClass("table-bordered");
		}else{
			removeClass("table-bordered");
		}
	}
	
	public void setStriped(boolean b){
		if(b){
			addClass("table-striped");
		}else{
			removeClass("table-striped");
		}
	}
	
	
	public void setHover(boolean b){
		if(b){
			addClass("table-hover");
		}else{
			removeClass("table-hover");
		}
	}
	
	public void setCondensed(boolean b){
		if(b){
			addClass("table-condensed");
		}else{
			removeClass("table-condensed");
		}
	}
	
	
	public void createFooter(){
		Container tfoot = new EXContainer("footer", "tfoot");
		addChild(tfoot);
		Container tr = new EXContainer("foot", "tr");
		tfoot.addChild(tr);
		Container td = new EXContainer("td", "td");
		tr.addChild(td.setAttribute("colspan", "200"));
		
		paginator = new EXContainer("paginator", "div");
		td.addChild(paginator);
		
		boolean showPagin = false;
		
		int size = tableModel_.getRowCount();
		
		if(size > tableModel_.getRowsPerPage()){
			showPagin = true;
		}
		
		setShowFooter(showPagin);
		
	}
	
	
	public void setShowFooter(boolean b){
		getChild("footer").setStyle("display", b?"table-footer-group":"none");
		
	}
	
	public int getPages()
	{
		int rows = this.tableModel_.getRowCount();

		int rPerPage = this.tableModel_.getRowsPerPage();

		int remainder = rows % rPerPage;

		int multiple = Math.round(rows / rPerPage);

		int pages = multiple;
 
		if (remainder != 0)
		{
			pages = pages + 1;
		}

		return pages;

	}
	
	
	public void setShowHeader(boolean b){
		
		thead.setStyle("display", b? "table-header-group":"none");
	}
	
	public void addHeaderStyle(String sclass){
		thead.getChild("header").addClass(sclass);
	}
	public void selectRow(Container row){
		for(Container r : row.getParent().getChildren()){
			r.removeClass("selected");
		}
		
		row.addClass("selected");
	}
	

	public void refreshData(){
		tbody.getChildren().clear();
		tbody.setRendered(false);
		int size = tableModel_.getRowCount();
		int colcount =  tableModel_.getColumnCount();
		boolean showPagin = false;
		
		if(size > tableModel_.getRowsPerPage()){
			showPagin = true;
		}
		
		if (size == 0){
			Container tr = new EXContainer("empty", "tr");
			tbody.addChild(tr);
			Container td = new EXContainer("emtpytd", "td").setAttribute("colspan", "1000").setText("No records found").setStyle("text-align", "center");
			tr.addChild(td);
		}else{
			int rows = tableModel_.getRowsPerPage();
			if (rows > size){
				rows = size;
			}
		//	tbody = new EXAsynchronous("tbody", "tbody");
			//addChild(tbody);
			for (int i = 0; i < rows; i++)
			{
				Container row = new EXContainer("" + i, "tr");
				
				tbody.addChild(row);
				for (int j = 0; j < colcount; j++){
					
					
					Container td = new EXContainer("", "td").setStyle("display", getColumnTd(j).getStyle("display"));
					row.addChild(td);
				}
				
			}
			changePage(currentPage);
		}
		
		if(getChild("footer") != null){
			setShowFooter(showPagin);
		}
		
	}
	
	public Container getHeaderContainer(){
		return thead.getChild("header");
	}
	
	protected Container getColumnTd(int index){
		return thead.getChildByIndex(0).getChildByIndex(index);
	}
	
	public Container getTd(int col, int row){
		return tbody.getChildByIndex(row).getChildByIndex(col);
	}
	
	public Container addRow(){
		Container tr = new EXContainer("tr", "tr");
		
		int newRow = tbody.getChildren().size();
		tbody.addChild(tr);
		
		
		for(int i =0; i < this.tableModel_.getColumnCount(); i++){
			Container td = new EXContainer("td", "td");
			tr.addChild(td);
			td.addChild(this.getCellRenderer().getComponentAt(newRow, i, currentPage, this.tableModel_, this));
		}
		
		return tr;
	}
	
	public int getRowCount(){
		return tbody.getChildren().size();
	}
	
	private void createTable()
	{

		this.getChildren().clear();
		this.setRendered(false);
		
		if(tableModel_ == null){
			return;
		}
 
		 thead = new EXContainer("thead", "thead");

		 if(StringUtil.isNotEmpty(caption)){
			 setCaption(caption);
		 }
		 
		addChild(thead);
		addChild(tbody);
		Container header = new EXContainer("header", "tr");
		thead.addChild(header);

		for (int i = 0; i < this.tableModel_.getColumnCount(); i++){

			Container column = this.columnModel_.getColumnAt(i, this, this.tableModel_);
			
			header.addChild(column);
		}

		refreshData();
		
		createFooter();
		
	}
	

	public void setModel(TableModel model)
	{
		this.tableModel_ = model;
		this.refresh();
	}

	public TableModel getModel()
	{
		return this.tableModel_;
	}

	public int getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(int currentPage) {
		changePage(currentPage);
	}


	public void changePage(int page)
	{
		if(tableModel_ == null){
			return;
		}
		this.currentPage = page;
		//Container tbody =  getChild("tbody");
		int rowsPerpage = tableModel_.getRowsPerPage();
		
		int rowCount = tableModel_.getRowCount();
		
		
		if (rowCount == 0){
			return;
		}
		
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
					td = new EXContainer("", "td");
					td.setStyle("display", getColumnTd(col).getStyle("display"));
					tr.addChild(td);
				}
				else
				{
					td = tr.getChildByIndex(col);
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

	
	
	
	
	public CellRenderer getCellRenderer()
	{
		return celleRendere_;
	}

	public void setCellRenderer(CellRenderer celleRendere_)
	{
		this.celleRendere_ = celleRendere_;
		refresh();
	}

	
	
	
	public TableColumnModel getColumnModel() {
		return columnModel_;
	}


	public void setColumnModel(TableColumnModel columnModel_) {
		this.columnModel_ = columnModel_;
		this.refresh();
	}


	public RowDecorator getRowDecorator() {
		return rowDecorator_;
		
	}


	public void setRowDecorator(RowDecorator rowDecorator_) {
		this.rowDecorator_ = rowDecorator_;
		this.refresh();
	}


	@Override
	public void refresh()
	{
		this.createTable();

		this.changePage(0);
	}
	
	
	public void onReady(JQuery query){
		try{
			JMap options = new JMap(); 
			options.put("total", getPages());
			
			JQuery serverrequest =query.clone().makeServerRequest(new JMap().put("num", new Var("num")),this);
			Function function = new Function(serverrequest,new Var("event"), new Var("num"));
			
			JQuery bootpag= query.clone().getDescendentById(paginator.getId()).invoke("bootpag", options).invoke("on","page",function);
			String url = "https://raw.githubusercontent.com/botmonster/jquery-bootpag/master/lib/jquery.bootpag.min.js";
			query.getScript(url,bootpag);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void showAllFields(){
		showMaxFields(this.tableModel_.getColumnCount());
	}
	
	public void showMaxFields(int fields){
		Container trHeaderCols = getChild("thead").getChild("header");
		//Container body = getChild("tbody");
		
		for(Container trCols : tbody.getChildren()){
			for(int i = 0; i < trCols.getChildren().size(); i++){
				
				if(i >= fields){
					trHeaderCols.getChildByIndex(i).setStyle("display", "none");
					trCols.getChildByIndex(i).setStyle("display", "none");
				}else{
					trHeaderCols.getChildByIndex(i).setStyle("display", "table-cell");
					trCols.getChildByIndex(i).setStyle("display", "table-cell");
				}
			}
		}
	}
	
	public static class MakeSortable implements Event{

		public void ClientAction(JQuery container) {
			container.invoke("tablesorter");
			
		}

		public boolean ServerAction(Container container, Map<String, String> request) throws UIException {
			// TODO Auto-generated method stub
			return false;
		}

		public void Success(JQuery container, Map<String, String> request) throws UIException {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public void ClientAction(JQuery container) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean ServerAction(Container container, Map<String, String> request)
			throws UIException {
		changePage(Integer.parseInt(request.get("num"))-1);
		return true;
	}


	@Override
	public void Success(JQuery container, Map<String, String> request)
			throws UIException {
		// TODO Auto-generated method stub
		
	}

}
