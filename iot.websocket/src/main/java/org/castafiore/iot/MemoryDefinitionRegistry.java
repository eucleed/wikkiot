package org.castafiore.iot;

import java.util.LinkedHashMap;
import java.util.Map;

import org.castafiore.iot.definitions.DefinitionRegistry;
import org.castafiore.iot.definitions.DeviceDefinition;

public class MemoryDefinitionRegistry implements DefinitionRegistry{
	
	private Map<String, DeviceDefinition> db = new LinkedHashMap<String, DeviceDefinition>();


	@Override
	public DeviceDefinition getDefinition(String definitionId, String groupId,
			String versionId) {
		return db.get(getKey(definitionId, groupId, versionId));
	}
	
	protected String getKey(String definitionId, String groupId,
			String versionId){
		return definitionId + "#" + groupId + "#" + versionId;
	}

	@Override
	public DeviceDefinition publishDefinition(DeviceDefinition definition) {
		
		db.put(getKey(definition.getDefinitionId(), definition.getGroupId(), definition.getVersionId()), definition);
		return definition;
		
	}

}
