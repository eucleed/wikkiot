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

package org.castafiore.ui.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.castafiore.Constant;
import org.castafiore.KeyValuePair;
import org.castafiore.SimpleKeyValuePair;
import org.castafiore.ui.Application;
import org.castafiore.ui.Container;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.js.Ajax;
import org.castafiore.ui.js.Expression;
import org.castafiore.ui.js.Function;
import org.castafiore.ui.js.JArray;
import org.castafiore.ui.js.JMap;
import org.castafiore.ui.js.JSObject;
import org.castafiore.ui.js.Var;
import org.castafiore.ui.mvc.CastafioreController;
import org.castafiore.ui.scripting.TemplateComponent;
import org.castafiore.utils.JavascriptUtil;
import org.castafiore.utils.StringUtil;
import org.springframework.util.Assert;
/**
 * This class is probably the most important class in the framework<br>
 * 
 * As its name implies , it is a proxy to the browser. <br>
 * 
 * This class basically is responsible for the management of client/Browser actions<br>
 * 
 * It contains a large number of methods that allows to make almost everything possible with an html tag, 
 * ranging from changing styles, attributes, making animations<br>
 * 
 * This class is also responsible to initiating an ajax request<br>
 * 
 * 
 * 
 * Author<br>
 * Kureem Rossaye<br>
 * kureem@gmail.com<br>
 * June 27 2008
 */
public final class JQuery implements Constant {
	
	private Container container_;
	
	private List<KeyValuePair> commands;
	private ListOrderedMap buffer = null;
	
	
	
	private final static String NL = "\n";
	
	private String selector;
	
	private Var vSelector;
	
	
	/**
	 * creates an instance of clientProxy using a selector.
	 * Creating a clientProxy using this constructor will not reflect any component in the Java DOM
	 * @param selector
	 */
	public JQuery(String selector, ListOrderedMap buffer)
	{
		
		this.selector = selector;
		String idref = getIdRef();
		this.commands = (List<KeyValuePair>)buffer.get(idref);
		if(commands == null)
		{
			commands = new LinkedList<KeyValuePair>();
			buffer.put(idref, commands);
		}
		this.buffer = buffer;
	}
	public JQuery(String selector)
	{
		
		this.selector = selector;
		
		this.buffer = new ListOrderedMap();
		
		commands = new LinkedList<KeyValuePair>();
		buffer.put(getIdRef(), commands);
		
	}
	
	
	public JQuery ajax(JMap options){
		StringBuilder b = new StringBuilder();
		b.append("$.ajax(").append(options.getJavascript()).append(")");
		eval(b.toString());
		return this;
	}
	
	public JQuery(Var selector)
	{
		
		this.vSelector = selector;
		
		this.buffer = new ListOrderedMap();
		
		commands = new LinkedList<KeyValuePair>();
		buffer.put(getIdRef(), commands);
		
	}
	
	public JQuery getCSS(String url){
		return call(Constant.NO_CONFLICT + ".getCSS",url);
	}
	
	public  JQuery alert(String msg){
		eval("alert('" + msg + "')");
		return this;
	}
	
	public  JQuery alert(Var var){
		eval("alert(" + var.getJavascript()  + ")");
		return this;
	}
	/**
	 * Creates an instance of ClientProxy to mirror a Component
	 * @param container
	 */
	@SuppressWarnings("unchecked")
	public JQuery(Container container, ListOrderedMap  buffer)
	{
		if(buffer == null){
			buffer= new ListOrderedMap();
		}
		this.container_ = container;
		this.commands = (List<KeyValuePair>) buffer.get(getIdRef());
		if(commands == null)
		{
			commands = new ArrayList<KeyValuePair>(10);
			buffer.put(getIdRef(), commands);
		}
		this.buffer = buffer;
	}
	
	/**
	 * This method provides possibility to make a conditional statement
	 * @param expression 		- an expression  that should return a boolean
	 * @param toExecuteIfTrue	- The piece of javascript that will be executed if true
	 * @param executeifFalse	- The piece of javascript that will be executed if false
	 * @return 
	 */ 
	public JQuery IF(Expression expression, JQuery toExecuteIfTrue, JQuery executeifFalse)
	{		
		StringBuilder builder = new StringBuilder();
		builder.append("if(").append(expression.getExpression()).append("){").append(toExecuteIfTrue.getCompleteJQuery()).append("}");
		
		if(executeifFalse != null)
		{
			builder.append("else{").append(executeifFalse.getCompleteJQuery()).append("}");
		}
		
		this.eval(builder.toString());
		
		return this;
		
	}
	
	
	
	
	/**
	 * return the Root of the hierarchy
	 * @return
	 */
	public JQuery getRoot()
	{
		Container root = container_.getRoot();
		if(root !=  null)
		{
			return new JQuery(root, buffer);
		}
		return null;
	}
	
	public JQuery setTimeout(JQuery function, int milliseconds){
		this.eval("setTimeout('" + JavascriptUtil.javaScriptEscape(function.getCompleteJQuery()) + "', "+ milliseconds +")");
		return this;
	}
	public JQuery setTimeout(Function function, int milliseconds){
		this.eval("setTimeout('" + JavascriptUtil.javaScriptEscape(function.getJavascript()) + "', "+ milliseconds +")");
		return this;
	}
	/**
	 * generates the complet javascript starting from the root
	 * @return
	 */
	public String getCompleteJQuery()
	{
		StringBuilder builder = new StringBuilder();
		
		Iterator selectors = buffer.keyList().iterator();
		while(selectors.hasNext())
		{
			String selector = selectors.next().toString();
			List<KeyValuePair> cmds = (List<KeyValuePair>)buffer.get(selector);
			
			builder.append(JQuery.getCurrentJQuery_(cmds, selector));
		}
		return builder.toString();
	}
	
	/**
	 * calls a javascript method with the specified name, and passing the specified parameters into it
	 */
	public JQuery invoke(String name, JMap parameter)
	{
		commands.add(makeKeyValuePair(name, parameter));
		return this;
	}
	
	/**
	 * calls a javascript method with the specified name, and passing the specified parameters to it
	 * @param name		-the name of the javascript method to call
	 * @param objects	-the parameter to pass
	 * @return
	 */
	public JQuery invoke(String name, Object ...objects)
	{
		commands.add(makeKeyValuePair(name, objects));
		return this;
	}
	
	
	/**
	 * return the width
	 * @return
	 */
	public Var getWidth()
	{
		return getAttribute("width");
	}
	
	/**
	 * appends a javascript fragment to the Proxy
	 * @param fragment
	 * @return
	 */
	public JQuery eval(String fragment)
	{
		SimpleKeyValuePair kv = new SimpleKeyValuePair();
		kv.setKey("js");
		kv.setValue(fragment);
		commands.add(kv);
		return this;
	}
	
	
	public JQuery call(String functionName,Object...params){
	
			StringBuilder p = new StringBuilder();
			int count = 0;
			for(Object o : params){
				if(count > 0)
					p.append(",");
				if(o instanceof JSObject){
					p.append( ((JSObject)o).getJavascript());
					
				}else if (o instanceof Boolean){
					p.append(o);
				}else if(o instanceof Number){
					p.append(o);
				}else{
					p.append("'").append(o).append("'");
				}
				count++;
			}
			
			return eval(functionName + "("+p.toString()+");");	
		
	}
	
	public JQuery executeFunction(String functionName,JMap options){
		return call(functionName, new Object[]{options});
	}
	
	
	public JQuery getScript(String scriptUrl, JQuery callBack){
		return call(Constant.NO_CONFLICT + ".getScript",scriptUrl, new Function(callBack,new Var("data"), new Var("textStatus"), new Var("jqxhr")));
	}
	
	public JQuery getScript(JArray sciprts, JQuery callBack){
		return call(Constant.NO_CONFLICT + ".getScript",sciprts, new Function(callBack,new Var("data"), new Var("textStatus"), new Var("jqxhr")));
	}
	
	public JQuery redirectTo(String url){
		return 	eval("window.location = '"+url+ "'");
	}
	
	
	public JQuery append(JQuery clientProxy)
	{
		eval(clientProxy.getCompleteJQuery());
		return this;
	}
	
	
	/**
	 * returns the name of the component this ClientProxy mirrors
	 * If the client proxy was created using a selector, this method returns empty string
	 * @return
	 */
	public String getName()
	{
		if(container_!= null)
			return container_.getName();
		return "";
	}
	
	
	/**
	 * returns the id of the component this ClientProxy mirrors
	 * If the client proxy was created using a selector, this method returns the selector
	 * @return
	 */
	public String getId()
	{
		if(container_ != null)
			return container_.getId();
		else if(selector != null)
			return selector;
		else
			return vSelector.getJavascript();
	}
	
	/**
	 * returns the id with a "#" before the id
	 * @return
	 */
	public String getIdRef()
	{
		if(container_ != null)
			return ID_PREF + container_.getId();
		else if(selector != null)
			return selector;
		else
			return vSelector.getJavascript();
	}
	
	/**
	 * returns the text of the component this clientProxy mirrors
	 * @return
	 */
	public Var text()
	{
		String ref = Constant.NO_CONFLICT +  "(\""+getIdRef()+"\").text()";
		
		String serverData = "";
		if(container_ != null)
			serverData = this.container_.getText();
		
		return new Var(ref, serverData);
	}
	
	
	/**
	 * returns all the attributes of the component this ClientProxy mirrors
	 * @return
	 */
	public Map<String, Var> getAttributes()
	{
		Map<String, Var> result = new HashMap<String, Var>();
		if(container_ != null)
		{
			String[] attributeNames = container_.getAttributeNames();
			
			
			for(String attributeName : attributeNames)
			{
				Var attribute = this.getAttribute(attributeName);
				
				result.put(attributeName, attribute);
			}
		}
		
		return result;
	}
	
	
	/**
	 * returns all the styles of the component this ClientProxy mirrors
	 * @return
	 */
	public Map<String, Var> getStyles()
	{
		Map<String, Var> result = new HashMap<String, Var>();
		
		if(container_!= null)
		{
			String[] styleNames = container_.getStyleNames();
			
			
			for(String styleName : styleNames)
			{
				Var value = getStyle(styleName);
				
				result.put(styleName, value);
			}
		}
		return result;
	}
	
	/**
	 * creates an effect on the component
	 * @param name		- The name of the effect
	 * @param options	- The option parameters
	 * @param speed		- The speed
	 * @return
	 */
	public JQuery effect(String name, JMap options, int speed)
	{
		KeyValuePair kv = makeKeyValuePair("effect",name, options, speed);
		
		commands.add(kv);
		return this;
	}
	
	public Var getProperty(String name)
	{
		String ref = Constant.NO_CONFLICT + "(\""+this.getIdRef()+"\")."+name+"()";
		
		
		return new Var(ref,"");
	}
	
	public Var getProperty(String name, String inner)
	{
		String ref = Constant.NO_CONFLICT + "(\""+this.getIdRef()+"\")."+name+"()." + inner;
		
		
		return new Var(ref, "");
	}
	
	/**
	 * returns the specified attribute of the component Mirrored by this ClientProxy
	 * @param name
	 * @return
	 */
	public Var getAttribute(String name)
	{
		String ref = Constant.NO_CONFLICT + "(\""+this.getIdRef()+"\").attr(\""+name+"\")";
		
		String sessionData="";
		if(container_ != null){
			sessionData= container_.getAttribute(name);
		}
		return new Var(ref, sessionData);
	}
	
	
	/**
	 * returns the specified style of the component Mirrored by this ClientProxy
	 * @param name
	 * @return
	 */
	public Var getStyle(String name)
	{
		String ref = Constant.NO_CONFLICT + "(\""+this.getIdRef()+"\").css(\""+name+"\")";
		
		String serverData = "";
		if(container_ != null)
			serverData = this.container_.getStyle(name);
		
		return new Var(ref, serverData);
	}
	
	public JQuery getParent()
	{
		if(container_.getParent() != null)
			return new JQuery(container_.getParent(), buffer);
		return null;
	}
	
	public JQuery findDescendentByName(String name)
	{
		return findDescendentByName(name, this);
	}
	
	
	
	
	private JQuery findDescendentByName(String name, JQuery container)
	{
		
		
		if(container.getName().equalsIgnoreCase(name))
		{
			return this;
		}
		else
		{
			Iterator<JQuery> children = container.getChildren().iterator();
			
			while(children.hasNext())
			{
				JQuery child = children.next();
				
				if(child.getName().equalsIgnoreCase(name))
				{
					return child;
				}
				else
				{
					findDescendentByName(name, child);
				}
			}
		}
		
		return null;
	}
	
	
	public JQuery getDescendentById(String id)
	{
		if(container_ != null)
		{
			if(id.equals( container_.getId() ))
			{
				return this;
			}
			else
			{
				for(JQuery child : this.getChildren())
				{
					JQuery c = child.getDescendentById(id);
					if(c != null)
					{
						return c;
					}
				}
			}
		}
		
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public JQuery getDescendentOfType(Class type)
	{
		if(container_ != null)
		{
			if(type.isAssignableFrom( container_.getClass() ))
			{
				return this;
			}
			else
			{
				for(JQuery child : this.getChildren())
				{
					JQuery c = child.getDescendentOfType(type);
					if(c != null)
					{
						return c;
					}
				}
			}
		}
		
		return null;
	}
	
	public JQuery getDescendentByName(String name)
	{
		if(container_ != null)
		{
			if(name.equals( container_.getName() ))
			{
				return this;
			}
			else
			{
				for(JQuery child : this.getChildren())
				{
					JQuery c = child.getDescendentByName(name);
					if(c != null)
					{
						return c;
					}
				}
			}
		}
		
		return null;
	}
	
	public List<JQuery> getChildren()
	{
		
		List<Container> containers = container_.getChildren();
		List<JQuery> result = new ArrayList<JQuery>(containers.size());
		for(Container c: containers)
		{
			result.add(new JQuery(c, buffer));
		}
		return result;
	}
	
	
	public JQuery call(Ajax ajax){
		
		eval(ajax.getJavascript());
		return this;
		
	}
	
	
	/*****************************************************************/
	
	
	public static String getCreationCommand(Container component, String parentId,String html, Container previousSibbling, Container nextSibbling)
	{
		
		
		
		String componentId = component.getId();
		StringBuilder builder = new StringBuilder();

		//next sibbling is rendered, we append before next sibbling
		if(component.getParent() instanceof TemplateComponent)
		{
			String content = Constant.NO_CONFLICT + "(\"" + html + "\").attr({\""+ID_ATTR+"\":\""+componentId+"\"})";
			builder.append(Constant.NO_CONFLICT + "(\"" +ID_PREF+parentId+"  *[name='"+component.getName()+"']\").replaceWith("+content+");");
		}
		else if(nextSibbling!= null && nextSibbling.rendered())
		{
			builder.append(Constant.NO_CONFLICT + "(\"" + html + "\").attr({\""+ID_ATTR+"\":\""+componentId+"\"}).insertBefore("+Constant.NO_CONFLICT +"(\""+ID_PREF+nextSibbling.getId()+"\"));");
		}
		//previous sibbling rendered, we append after previous sibbling
		else if(previousSibbling!= null && previousSibbling.rendered())
		{
			builder.append(Constant.NO_CONFLICT + "(\"" + html + "\").attr({\""+ID_ATTR+"\":\""+componentId+"\"}).insertAfter("+Constant.NO_CONFLICT +"(\"" +ID_PREF+previousSibbling.getId()+"\"));");
		}else if(false){
			String s = "var _"+component.getId()+" = document.createElementNS(\"http://www.w3.org/2000/svg\", \""+component.getTag()+"\");";
			s = s + "_"+component.getId()+".setAttributeNS(null,\"id\", \""+component.getId()+"\");";
			if(StringUtil.isNotEmpty(component.getName())){
				s = s + "_"+component.getId()+".setAttributeNS(null,\"name\", \""+component.getName()+"\");";
			}
			
			s = s + "$(\"#"+component.getParent().getId()+"\")[0].appendChild(_"+component.getId()+");";
			builder.append(s);
			
		}
		//append to end in root
		else
		{ 
			builder.append(Constant.NO_CONFLICT + "(\"" + html + "\").attr({\""+ID_ATTR+"\":\""+componentId+"\"}).appendTo("+Constant.NO_CONFLICT +"(\""+ID_PREF+parentId+"\"));");
		}
		
		return builder.toString();
	}
	
	
	public static String addQuote(String s) {
		if("this".equals(s)){
			return s;
		}
		return "\"" + s + "\"";
	}

	private static String combineForParams(String[] strings) {
		String result = "";
		for (String s : strings) {
			if(!s.startsWith(Constant.NO_CONFLICT ))
				result = result + "," + addQuote(s);
			else
				result = result + "," + s;
		}
		if(result.length() > 0)
			result = result.substring(1);

		return result;
	}

	private static KeyValuePair makeKeyValuePair(String methodname,
			String[] params) {
		SimpleKeyValuePair kv = new SimpleKeyValuePair();

		kv.setKey(methodname);

		kv.setValue(combineForParams(params));

		return kv;
	}

	
	private static KeyValuePair makeKeyValuePair(String methodname, Object...params ) {
		
		String result = JavascriptUtil.generateJS(params);
		

		SimpleKeyValuePair kv = new SimpleKeyValuePair();
		kv.setKey(methodname);
		kv.setValue(result);
		return kv;
	}
	
	private static KeyValuePair makeKeyValuePair(String methodname, String componentId) {
		SimpleKeyValuePair kv = new SimpleKeyValuePair();
		kv.setKey(methodname);
		kv.setValue(Constant.NO_CONFLICT + "(\"" +ID_PREF + componentId + "\")");
		return kv;
	}
	
	private static KeyValuePair makeKeyValuePair(String methodname, JQuery jquery) {
		SimpleKeyValuePair kv = new SimpleKeyValuePair();
		kv.setKey(methodname);
		kv.setValue(Constant.NO_CONFLICT + "(\"" + jquery.getIdRef() + "\")");
		return kv;
	}

	public JQuery setAttributes(JMap map) {
		if(!map.isEmpty())
			commands.add(makeKeyValuePair("attr", map));
		return this;
	}
	
	
	public JQuery setSVGAttributes(JMap map, String id){
		Map<Object,String> cpl = map.getCompiled();
		Iterator<Object> keys = cpl.keySet().iterator();
		while(keys.hasNext()){
			
			String key = keys.next().toString();
			if(!key.equalsIgnoreCase("name") && !key.equalsIgnoreCase("__path")){
				String val = cpl.get(key);
				eval("_"+id+".setAttributeNS(null,\""+key+"\", "+val+");");
			}
		}
		return this;
		
	}

	public JQuery setAttribute(String key, String value) {
		commands.add(makeKeyValuePair("attr", new String[] { key, value }));
		return this;
	}
	
	public JQuery setAttribute(String key, Var value) {
		commands.add(makeKeyValuePair("attr", value));
		return this;
	}

	public JQuery removeAttr(String name) {
		commands.add(makeKeyValuePair("removeAttr", new String[] { name }));
		return this;
	}

	public JQuery addClass(String className) {
		commands.add(makeKeyValuePair("addClass", new String[] { className }));
		return this;
	}

	public JQuery removeClass(String className) {
		commands.add(makeKeyValuePair("removeClass", new String[] { className }));
		return this;
	}

	public JQuery toggleClass(String className) {
		commands.add(makeKeyValuePair("toggleClass", new String[] { className }));
		return this;
	}

	public JQuery setText(String text) {
		commands.add(makeKeyValuePair("text", new String[] { text }));
		return this;
	}

	public JQuery val(String value) {
		commands.add(makeKeyValuePair("val", new String[] { value }));
		return this;
	}

	public JQuery removeData(String dataName) {
		commands.add(makeKeyValuePair("removeData", new String[] { dataName }));
		return this;
	}

	public JQuery html(String html) {
		commands.add(makeKeyValuePair("html", new String[] { html }));
		return this;
	}
	
	public Var html() {
		String ref = Constant.NO_CONFLICT + "(\""+this.getIdRef()+"\").html()";
		return new Var(ref, "");
	}

	public JQuery append(String html) {
		commands.add(makeKeyValuePair("append", new String[] { html }));
		return this;
	}

	public JQuery prepend(String html) {
		commands.add(makeKeyValuePair("prepend", new String[] { html }));
		return this;
	}

	public JQuery after(String html) {
		commands.add(makeKeyValuePair("after", new String[] { html }));
		return this;
	}

	public JQuery before(String html) {
		commands.add(makeKeyValuePair("before", new String[] { html }));
		return this;
	}

	public JQuery insertAfter(JQuery jquery) {
		commands.add(makeKeyValuePair("insertAfter", jquery));
		return this;
	}

	public JQuery insertBefore(JQuery jquery) {
		commands.add(makeKeyValuePair("insertBefore", jquery));
		return this;
	}

	public JQuery wrap(String html) {
		commands.add(makeKeyValuePair("wrap", new String[] { html }));
		return this;
	}

	public JQuery wrapInner(String html) {
		commands.add(makeKeyValuePair("wrapInner", new String[] { html }));
		return this;
	}

	public JQuery replaceWith(String html) {
		commands.add(makeKeyValuePair("replaceWith", new String[] { html }));
		return this;
	}
	
	public JQuery draggable()
	{
		commands.add(makeKeyValuePair("draggable", new String[]{}));
		return this;
	}
	
	
	public JQuery hover(JQuery fn1, JQuery fn2){
		invoke("hover", fn1,fn2);
		return this;
	}
	
	public JQuery droppable(String acceptClass)
	{	
		JMap jmap = new JMap();
		
		jmap.put("accept", "." + acceptClass);
		commands.add(makeKeyValuePair("droppable", jmap));
		return this;
	}
	
	
	public JQuery droppable(String acceptClass, JMap options)
	{
		options.put("accept", acceptClass);
		commands.add(makeKeyValuePair("droppable", options));
		return this;	
	}
	
	public JQuery draggable(JMap options)
	{
		commands.add(makeKeyValuePair("draggable",options));
		return this;
	}

	public JQuery empty() {
		commands.add(makeKeyValuePair("empty", new String[] {}));
		return this;
	}

	
	public JQuery remove() {
		commands.add(makeKeyValuePair("remove", new String[] {}));
		return this;
	}
	
	public JQuery setStyle(String key, String value) {
		commands.add(makeKeyValuePair("css", new String[] { key, value }));
		return this;
	}

	public JQuery setStyle(String key, Var value) {
		JMap map = new JMap().put(key, value);
		setStyles(map);
		return this;
	}
	
	public JQuery setStyle(Var key, Var value) {
		invoke("css", key, value);
		return this;
	}
	
	public JQuery setStyles(JMap properties) {
		if(!properties.isEmpty())
			commands.add(makeKeyValuePair("css", properties));
		
		return this;
	}
	
	public JQuery height(int val) {
		commands.add(makeKeyValuePair("height", new String[] { val + "px" }));
		return this;
	}

	public JQuery width(int val) {
		commands.add(makeKeyValuePair("width", new String[] { val + "px" }));
		return this;
	}

	public JQuery show(int speed) {
		commands.add(makeKeyValuePair("show", new String[] { speed + "" }));
		return this;
	}

	public JQuery hide(int speed) {
		commands.add(makeKeyValuePair("hide", new String[] { speed + "" }));
		return this;
	}
	
	public JQuery hide(String effect, JMap options, int speed) {
		commands.add(makeKeyValuePair("hide", effect, options, speed ));
		return this;
	}

	public JQuery toggle() {
		commands.add(makeKeyValuePair("toggle", new String[] {}));
		return this;
	}
	
	public JQuery slideDown(int speed) {
		commands.add(makeKeyValuePair("slideDown", new String[] { speed + "" }));
		return this;
	}

	public JQuery slideUp(int speed) {
		commands.add(makeKeyValuePair("slideUp", new String[] { speed + "" }));
		return this;
	}

	public JQuery slideToggle(int speed) {
		commands.add(makeKeyValuePair("slideToggle",
				new String[] { speed + "" }));
		return this;
	}

	public JQuery fadeIn(int speed) {
		commands.add(makeKeyValuePair("fadeIn", new String[] { speed + "" }));
		return this;
	}

	public JQuery fadeIn(int speed, JQuery callback) {
		commands.add(makeKeyValuePair("fadeIn", speed + "", callback));
		return this;
	}
	
	public JQuery fadeOut(int speed) {
		commands.add(makeKeyValuePair("fadeOut", new String[] { speed + "" }));
		return this;
	}
	
	public JQuery fadeOut(int speed, JQuery callback) {
		commands.add(makeKeyValuePair("fadeOut", speed + "", callback));
		return this;
	}

	public JQuery fadeTo(int speed, int opacity) {
		commands.add(makeKeyValuePair("fadeTo", new String[] { speed + "",	opacity + "" }));
		return this;
	}

	public JQuery data(String name, String value) {
		commands.add(makeKeyValuePair("data", new String[] { value}));
		return this;
	}

	public JQuery data(String name, String[] values) {
		commands.add(makeKeyValuePair("data", values));
		return this;
	}

	public JQuery data(String name, JArray data) {
		commands.add(makeKeyValuePair("data", data));
		return this;
	}

	public JQuery data(String name, JMap data) {
		commands.add(makeKeyValuePair("data", data));
		return this;
	}
	
	public JQuery appendTo(JQuery jquery) {
		commands.add(makeKeyValuePair("appendTo", jquery));
		return this;
	}
	
	public JQuery appendTo(String componentId) {
		commands.add(makeKeyValuePair("appendTo", componentId));
		return this;
	}
	
	public JQuery prependTo(JQuery jquery) {
		commands.add(makeKeyValuePair("prependTo", jquery));
		return this;
	}
	
	public void on(String eventName, JQuery jquery)
	{	String js = jquery.getCompleteJQuery();
	
		if(StringUtil.isNotEmpty(js)){
			SimpleKeyValuePair kv = new SimpleKeyValuePair();
			kv.setKey(eventName);	
			kv.setValue("function (event) {" + NL + jquery.getCompleteJQuery() + NL + "}");
			this.commands.add(kv);
		}
	}
	
	
	public void on(String eventName, String javascript)
	{
		SimpleKeyValuePair kv = new SimpleKeyValuePair();
		kv.setKey(eventName);	
		kv.setValue("function (event) {" + NL + javascript + NL + "}");
		this.commands.add(kv);
	}
	
	//events
	
	public JQuery click(JQuery jquery)
	{
		this.on("click", jquery);
		
		return this;
	}
	
	public JQuery blur(JQuery jquery)
	{
		this.on("blur", jquery);
		
		return this;
	}
	
	public JQuery change(JQuery jquery)
	{
		this.on("change", jquery);
		
		return this;
	}
	
	public JQuery dblclick(JQuery jquery)
	{
		this.on("dblclick", jquery);
		
		return this;
	}
	
	public JQuery error(JQuery jquery)
	{
		this.on("error", jquery);
		
		return this;
	}
	
	public JQuery focus(JQuery jquery)
	{
		this.on("focus", jquery);
		
		return this;
	}
	
	public JQuery keydown(JQuery jquery)
	{
		this.on("keydown", jquery);
		
		return this;
	}
	
	public JQuery keypress(JQuery jquery)
	{
		this.on("keypress", jquery);
		
		return this;
	}
	
	public JQuery keyup(JQuery jquery)
	{
		this.on("keyup", jquery);
		
		return this;
	}
	
	public JQuery load(JQuery jquery)
	{
		this.on("load", jquery);
		
		return this;
	}
	
	public JQuery mousedown(JQuery jquery)
	{
		this.on("mousedown", jquery);
		
		return this;
	}
	
	public JQuery mousemove(JQuery jquery)
	{
		this.on("mousemove", jquery);
		
		return this;
	}
	
	
	public JQuery mouseout(JQuery jquery)
	{
		this.on("mouseout", jquery);
		
		return this;
	}
	
	public JQuery mouseover(JQuery jquery)
	{
		this.on("mouseover", jquery);
		
		return this;
	}
	
	public JQuery mouseup(JQuery jquery)
	{
		this.on("mouseup", jquery);
		
		return this;
	}
	
	public JQuery resize(JQuery jquery)
	{
		this.on("resize", jquery);
		
		return this;
	}
	
	
	
	public JQuery scroll(JQuery jquery)
	{
		this.on("scroll", jquery);
		
		return this;
	}
	
	public JQuery select(JQuery jquery)
	{
		this.on("select", jquery);
		
		return this;
	}
	
	public JQuery submit(JQuery jquery)
	{
		this.on("submit", jquery);
		
		return this;
	}
	
	public JQuery unload(JQuery jquery)
	{
		this.on("unload", jquery);
		
		return this;
	}
	
	
	public JQuery resizeable(JMap options)
	{
		//container_.addScript(ResourceUtil.getJavascriptURL("jquery/ui.resizable.js"));
		KeyValuePair kv = makeKeyValuePair("resizable", options);
		this.commands.add(kv);
		return this;
	}
	
	public JQuery animate(JMap options, String duration, String easing)
	{
		KeyValuePair kv = null;
		if(easing != null)
			kv = makeKeyValuePair("animate", options, duration, easing);
		else
			kv = makeKeyValuePair("animate", options, duration);
		
		this.commands.add(kv);
		return this;
	}
	
	public JQuery animate(JMap options, String duration, String easing, JQuery callback)
	{
		KeyValuePair kv = null;
		if(easing != null)
			kv = makeKeyValuePair("animate", options, duration, easing, callback);
		else
			kv = makeKeyValuePair("animate", options, duration, callback);
		
		this.commands.add(kv);
		return this;
	}
	
	public String getCurrentJQuery()
	{
		return getCurrentJQuery_(commands, getIdRef());
	}
	
	public static String getCurrentJQuery_(List<KeyValuePair> commands, String idRef)
	{
		StringBuilder builder = new StringBuilder();
		
		String result = "";

		if(commands.size() > 0)
		{	
			
			
			boolean previousWasFragment = true;
			String dot = ".";
			for (KeyValuePair kv : commands) {
				
				if(kv != null)
				{
					if( kv.getKey().equalsIgnoreCase("js"))
					{
						builder.append(";" + kv.getValue()).append("");
						previousWasFragment = true;
					}
					else
					{ 
						if(result.length() == 0){
							result = result + Constant.NO_CONFLICT + "(" + JQuery.addQuote(idRef) + ")";
						}
						if(previousWasFragment)
							dot = result + ".";
						else
							dot = ".";
						
						builder.append(dot).append(kv.getKey()).append("(").append(	kv.getValue()).append(")");
						previousWasFragment = false;
					}
				}
			}
			
			
		}
		//if(result != null && result.length() > 0)
		//{
			//result =  result +builder.toString() + ";\n";
			return builder.toString() + ";";
			//return result;
		//}
		//return "";
		
	}
	
	@SuppressWarnings("unchecked")
	public JQuery getAncestorOfType(Class classType) 
	{
		
		Container c = container_.getAncestorOfType(classType);
		if(c != null)
		{
			return new JQuery(c, buffer);
		}
		return null;
	}
	
	public JQuery mask()
	{	
		return mask(null, null);
	}
	
	public JQuery mask(String message)
	{	
		return mask(null, message);
	}
	
	public JQuery mask(JQuery ancestor)
	{	
		return mask(ancestor, null);
	}
	
	public JQuery mask(JQuery ancestor, String message){
//		if(message == null){
//			message = "chargement....";
//		}
//		if(ancestor == null){
//			this.getRoot().addMethod("mask", message);
//		}else{
//			ancestor.addMethod("mask", message);
//		}
		return this;
	}
	
	public JQuery server( Event evt)
	{
		return server(null,null,evt, null);
	}
	
	public JQuery makeServerRequest( Event evt, String confirm)
	{
		return server(null,null,evt, confirm);
	}
	public JQuery makeServerRequest( JMap optionalParams, Event evt)
	{
		return server(null,optionalParams, evt, null);
	}
	public JQuery makeServerRequest( JMap optionalParams, Event evt, String confirm)
	{
		return server(null,optionalParams, evt, confirm);
	}
	
	@Deprecated
	public JQuery makeServerRequest( String ancestorId,  Event evt)
	{
		return server(ancestorId, null, evt, null);
	}
	
	@Deprecated
	public JQuery makeServerRequest( String ancestorId,  JMap optionalParams, Event evt)
	{
		return this.server(ancestorId, optionalParams, evt, null);
		
	}
	
	
	public JQuery server( String ancestorId,  JMap optionalParams, Event evt, String confirm){
		JMap params = new JMap();
		
		Application root = this.container_.getRoot();
		
		
		Object oUserId = root.getConfigContext().get("remoteUser");
		if(oUserId != null){
			String userid = oUserId.toString();
			if(userid != null && userid.trim().length() > 0 && !userid.equals("null")){
				params.put("casta_userid", userid);
			}
		}
		
		
		if(ancestorId == null)
			ancestorId = root.getId();
		
		String ancestor = ID_PREF + ancestorId;
		
		params.put("casta_applicationid", root.getName());
		
		params.put("casta_componentid", this.container_.getId());
		
		Assert.notNull(evt, "The event passed as parameter in a client proxy is null");
		if(evt != null)
		{
			params.put("casta_eventid", evt.hashCode() + "");
		}
		
		
		if(optionalParams != null)
			params.putAll(optionalParams);
		
		String fragment = "sCall("+params.getJavascript()+")";
		if(confirm != null && confirm.length() > 0){
			fragment = "if(confirm('"+ JavascriptUtil.javaScriptEscape(confirm)+"')){" + fragment + "};";
		}
		this.eval(fragment);
		
		return this;
	}
	
	public static  Var getDragSourceId(){
		return new Var("ui.draggable.attr(\"id\")", "");
	}
	
	public static  Var getDragSourcePositionX(){
		return new Var("ui.draggable.position().left", "");
	}
	
	public static  Var getDragSourcePositionY(){
		return new Var("ui.draggable.position().top", "");
	}
	
	public static  Var getSelectedItemId(){
		return new Var("ui.selected.id", "");
	}
	
	public static  Var getSelectedItem(){
		return new Var("ui.selected", "");
	}
	
	public static  Var getMouseXPosition(){
		return new Var("event.pageX", "");
	}
	
	public static  Var getMouseYPosition(){
		return new Var("event.pageY", "");
	}
	
	@Override
	public JQuery clone()
	{
		if(container_ != null)
			return new JQuery(container_, new ListOrderedMap());
		else if(selector != null){
			return new JQuery(selector);
			
		}else{
			return new JQuery(vSelector);
		}
	}
	
	
	public static Var getBrowserWidth()
	{
		return new Var("getBrowserWidth()", "");
	}
	
	public static Var getBrowserHeight()
	{
		return new Var("getBrowserHeight()", "");
	}
}
