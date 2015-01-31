package org.castafiore.ui.ex.dynaform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.castafiore.ui.Container;
import org.castafiore.ui.StatefullComponent;
import org.castafiore.ui.engine.JQuery;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.ex.form.button.Button;
import org.castafiore.utils.ComponentUtil;
import org.castafiore.utils.ComponentVisitor;

public class EXFieldSet extends EXContainer implements StatefullComponent<Map<String,?>>{

	private int columns = 1;
	
	private Container title = new EXContainer("title", "h6").addClass("widget-head");
	
	
	
	
	private Container form = new EXContainer("formContainer", "div");
	
	private Container buttonsContainer = new EXContainer("buttonsContainer", "div").addClass("col-lg-offset-1 col-lg-9");
	
	private Container body = new EXContainer("form", "div").addClass("form-horizontal").setAttribute("role", "form");
	
	
	
	public EXFieldSet(String name, String title, boolean doubleCols){
		this(name,title,doubleCols?2:1);
	}
	
	public EXFieldSet(String name, String title){
		this(name,title,1);
	}
	
	public EXFieldSet(String name, String title, int cols) {
		super(name, "fieldset");
		
		
		
		addChild(this.title.setText(title));
		//addChild(new EXContainer("", "hr"));
		
		 
		 addChild(body);
		 
		 body.addChild(form);
		 body.addChild(new EXContainer("", "hr"));
		 Container buttons = new EXContainer("buttons", "div").addClass("form-group").addChild(buttonsContainer);
		 body.addChild(buttons);
		 
		 columns = cols;
		 
	}
	
	public void setShowTitle(boolean b){
		title.setDisplay(b);
	}
	
	public EXFieldSet hideField(String name){
		getField(name).getParent().setStyle("display", "none");
		Container uiLabel = getDescendentByName("label_" + name);
		if(uiLabel != null){
			uiLabel.setDisplay(false);
		}
		return this;
	}
	
	public EXFieldSet showField(String name){
		getField(name).getParent().setStyle("display", "block");
		Container uiLabel = getDescendentByName("label_" + name);
		if(uiLabel != null){
			uiLabel.setDisplay(true);
		}
		return this;
	}
	
	public void setLabelFor(String label, StatefullComponent<?> input){
		Container uiLabel = getDescendentByName("label_" + input.getName());
		if(uiLabel != null){
			uiLabel.setText(label);
		}
		
	}
	
	
	public EXFieldSet displayBody(boolean display){
		body.setDisplay(display);
		return this;
	}
	
	
	public void setTitle(String title){
		this.title.setText(title);
		
	}
	
	public void addButton(Button btn){
		
		buttonsContainer.setDisplay(true);
		buttonsContainer.addChild(btn);
		
	}
	
	protected Container addRow(boolean odd){
		
		Container row = new EXContainer("row", "div").addClass("form-group");
		form.addChild(row);
		
		
		return row;
	}
	
	
	
	
	public void addField(String label, StatefullComponent<?> input){
		addField(label, input, 1);
	}
	
	
	public void addField(Container label, StatefullComponent<?> input){
		addField(label, input, 1);
	}
	
	
	public void addField(StatefullComponent<?> input){
		addField(input, 1);
	}
	
	public void addField(Container label, StatefullComponent<?> input, int spancolumn){
		addField(label,(Container)input,spancolumn);
	}
	
	
	public void addField(Container label, Container input, int spancolumn){
		int rows = form.getChildren().size();
		Container row = null;
		if(rows == 0){
			row  = addRow(false);
		}else{
			int total =0;
			
			row = form.getChildren().get(rows-1);
			for(Container c : row.getChildren()){
				total = Integer.parseInt(c.getAttribute("cols")) + total;
			}
			
			if(total >= columns){
				row = addRow(false);
			}
			
		}
		
		label.addClass("col-lg-2 control-label").setAttribute("cols", "0");
		row.addChild(label);
		
		Container inputcell = new EXContainer("cell__", "div").addClass("col-lg-" + getInputSize(spancolumn)).setAttribute("cols", spancolumn + "");
		inputcell.addChild(input);
		row.addChild(inputcell);
	}
	
	private String getInputSize(int spans){
		if(columns == 1){
			return "8";
		}else if(columns == 2){
			if(spans > 1){
				return "8";
			}
			return "4";
		}else if(columns == 3){
			if(spans == 2){
				return "5";
			}else if(spans == 3){
				return "8";
			}else{
				return "3";
			}
		}else if(columns == 4){
			
			if(spans == 2){
				return "4";
			}else if(spans == 3){
				return "6";
			}else if(spans == 4){
				return "8";
			}else{
				return "2";
			}
			
		}
		return "4";
	}
	

	
	public void addField(String label, StatefullComponent<?> input, int spancolumn){
		Container eXLabel = new EXContainer(input.getName() + "_label", "label");
		eXLabel.setText(label);
		addField(eXLabel, input, spancolumn);
	}
	
	public void addField(StatefullComponent<?> input, int spancolumn){
		addField(input.getName(), input, spancolumn);
	}
	
	public StatefullComponent getField(String name){
		return (StatefullComponent)getDescendentByName(name);
	}
	
	public List<StatefullComponent<?>> getFields(){
		//Container tBody = getChild("tbody");
		
		final List result = new ArrayList<Container>();
		
		ComponentUtil.iterateOverDescendentsOfType(form, Container.class, new ComponentVisitor() {
			
			@Override
			public void doVisit(Container c) {
				
				if(c.getName().equals("cell__")){
					result.add(c.getChildByIndex(0));
				}
				
			}
		});
		//ComponentUtil.getDescendentsOfType(form, result, StatefullComponent.class);
		
		return result;
	}


	

	public Map<String,?> getValue() {
		
		Map<String, Object> result = new HashMap<String,Object>();
		for(StatefullComponent<?> st : getFields()){
			result.put(st.getName(), st.getValue());
		}
		
		return result;
	}

	public void setValue(Map<String,?> value) {
		for(String key : value.keySet()){
			Object val = value.get(key);
			getField(key).setValue(val);
		}
	}
	
	
	public EXFieldSet setError(String fieldName, String errorMsg){
		getField(fieldName).getParent().getParent().addClass("has-error");
		getField(fieldName).setAttribute("title", errorMsg);
		return this;
	}
	
	
	public EXFieldSet clearError(String fieldName){
		getField(fieldName).getParent().getParent().removeClass("has-error");
		return this;
	}
	
	public EXFieldSet clearErrors(){
		for(StatefullComponent<?> s : getFields()){
			s.getParent().getParent().removeClass("has-error");
		}
		return this;
	}


	public void setEnabled(boolean enabled) {
		if(enabled){
			removeClass("disabled");
		}else{
			addClass("disabled");
		}
		
	}
	
	
	public void clientProxy(JQuery proxy){
		
	}
}
