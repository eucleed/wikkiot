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
package org.castafiore.iot.sample;

import org.castafiore.iot.definitions.DefinitionBuilder;
import org.castafiore.iot.definitions.DeviceDefinition;
import org.castafiore.iot.driver.DefinitionRegistryClient;

public class DefinitionPublishers {
	
	
	
	public static void main(String[] args) {
		
		
		DeviceDefinition lamp = DefinitionBuilder.create("Lamp", "eucleed.iot", "1.0")
				.addFunction("SwitchOn", "Switch on lamp")
				.addFunction("SwitchOff", "Switch off lamp")
			.build();
		Util.client.publishDefinition(lamp);
		
		DeviceDefinition remote = DefinitionBuilder.create("Remote", "eucleed.iot", "1.0")
				.addEvent("OnSwitchOn", "When switch on is pressed on remote")
				.addEvent("OnSwitchOff", "When Switch off is pressed on remote")
			.build();
		
		Util.client.publishDefinition(remote);
		
		
		
		
	}

}
