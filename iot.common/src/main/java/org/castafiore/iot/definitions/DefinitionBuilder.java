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
package org.castafiore.iot.definitions;

public class DefinitionBuilder {
	
	
	private DeviceDefinition def;
	private DefinitionBuilder(DeviceDefinition definition){
		this.def = definition;
	}
	public static DefinitionBuilder create(String definitionId, String groupId, String versionId){
		DeviceDefinition def = new DeviceDefinition();
		def.setDefinitionId(definitionId);
		def.setGroupId(groupId);
		def.setVersionId(versionId);
		return new DefinitionBuilder(def);
	}
	
	public DefinitionBuilder addEvent(String name, String description){
		EventDefinition evt = new EventDefinition();
		evt.setDescription(description);
		evt.setName(name);
		def.getEvents().add(evt);
		return this;
	}
	
	public DefinitionBuilder addFunction(String name, String description){
		FunctionDefinition func = new FunctionDefinition();
		func.setDescription(description);
		func.setName(name);
		def.getFunctions().add(func);
		return this;
	}
	
	public DeviceDefinition build(){
		return def;
	}

}
