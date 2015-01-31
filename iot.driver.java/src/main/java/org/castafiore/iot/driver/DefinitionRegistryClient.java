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
package org.castafiore.iot.driver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import org.castafiore.iot.definitions.DefinitionRegistry;
import org.castafiore.iot.definitions.DeviceDefinition;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DefinitionRegistryClient implements DefinitionRegistry{
	
	private String server;
	
	private static ObjectMapper mapper = new ObjectMapper();

	public DefinitionRegistryClient(String server) {
		super();
		this.server = server;
	}

	@Override
	public DeviceDefinition getDefinition(String definitionId, String groupId,
			String versionId) {

		try{
			
			String param = URLEncoder.encode("iot:get|" + definitionId + "/" + groupId + "/" + versionId);
		String json = readUrl(server + "/castafiore/resource?spec=" + param);

		DeviceDefinition definition = mapper.readValue(json.getBytes(), DeviceDefinition.class);
		
		return definition;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public DeviceDefinition publishDefinition(DeviceDefinition definition) {
		try{
			
			String json =mapper.writeValueAsString(definition);
			String param =  URLEncoder.encode( "iot:put|" + json);
			json = readUrl(server + "/castafiore/resource?spec=" + param);

			DeviceDefinition result = mapper.readValue(json.getBytes(), DeviceDefinition.class);
			
			return result;
			}catch(Exception e){
				throw new RuntimeException(e);
			}
	}
	
	
	public static String readUrl(String url)throws Exception{
		URL yahoo = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(yahoo.openStream()));

		String inputLine;
		StringBuilder b = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			b.append(inputLine).append("\n");
		    //System.out.println(inputLine);

		in.close();
		
		return b.toString();

	}

}
