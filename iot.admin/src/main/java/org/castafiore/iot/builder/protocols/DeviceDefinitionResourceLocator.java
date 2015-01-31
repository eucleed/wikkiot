/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castafiore.iot.builder.protocols;

import org.castafiore.iot.definitions.DefinitionRegistry;
import org.castafiore.iot.definitions.DeviceDefinition;
import org.castafiore.resource.FileData;
import org.castafiore.resource.ResourceLocator;
import org.castafiore.utils.BaseSpringUtil;
import org.castafiore.utils.StringUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DeviceDefinitionResourceLocator implements ResourceLocator{

	
	private ObjectMapper mapper = new ObjectMapper();
	@Override
	public FileData getResource(String spec, String width) throws Exception {
		String id = spec;
		
		 
		//put#
		//get#
		
		if(id.startsWith("iot:put|")){
			
			String json = id.replace("iot:put|", "");
			DeviceDefinition definition = mapper.readValue(json.getBytes(), DeviceDefinition.class);
			definition = BaseSpringUtil.getBeanOfType(DefinitionRegistry.class).publishDefinition(definition);
			String data = mapper.writeValueAsString(definition);
			
			ByteArrayFileData fd = new ByteArrayFileData(data.getBytes());
			fd.setName("definition-" + id + ".json");
			return fd;
			
		}else{
			String key = id.replace("iot:get|", "");
			String[] parts = StringUtil.split(key, "/");
			if(parts.length ==3){
				String data = mapper.writeValueAsString(BaseSpringUtil.getBeanOfType(DefinitionRegistry.class).getDefinition(parts[0], parts[1], parts[2]));
				
				ByteArrayFileData fd = new ByteArrayFileData(data.getBytes());
				fd.setName("definition-" + id + ".json");
				return fd;
			}else{
				throw new RuntimeException("cannot load resource with spec:" + spec);
			}
			
		}
		
		
		
	}

}
