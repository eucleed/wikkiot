package org.castafiore.web;

import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.castafiore.resource.FileData;
import org.castafiore.resource.ResourceLocator;
import org.castafiore.resource.ResourceLocatorFactory;
import org.castafiore.ui.UIException;
import org.castafiore.utils.ChannelUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/castafiore")
public class CastafioreController {
	
	
	public void doMethod(HttpServletRequest request, HttpServletResponse response){
		
	}
	
	
	@RequestMapping(value="/resource/{type}/{path}")
	public void doResource(@PathVariable(value="type")String type,@PathVariable(value="path") String path, HttpServletRequest request, HttpServletResponse response){
		String spec = null;
		try
		{
			//spec = request.getParameter("spec");
			spec = type + ":" + path;
			//logger.debug(spec);
			ResourceLocator locator = ResourceLocatorFactory.getResourceLocator(spec);
			String width = request.getParameter("width");
			FileData f = locator.getResource(spec, width);
			if(f != null)
			{
				try
				{
					OutputStream os = response.getOutputStream();
					response.setContentType(f.getMimeType());
					((HttpServletResponse)response).setHeader("Content-Disposition", "filename=" + f.getName()); 
					ChannelUtil.TransferData(f.getInputStream(), os);
					os.flush();	 
				}
				catch(ClassCastException cce)
				{
					throw new Exception("the file specified by the path " + spec + " is not a binary file");
				}
			}
			else
			{
				 throw new Exception("the file specified by the path " + spec + " cannot be found. Possibly deleted");
			}
		}
		catch(Exception e)
		{
			if(spec == null)
				throw new UIException("unable to load resource since the specification passed is null" ,e);
			else
				throw new UIException("unable to load resource with the specification " + spec ,e);
		}
	}
	
	public void doExecute(HttpServletRequest request, HttpServletResponse response){
		
	}

}
