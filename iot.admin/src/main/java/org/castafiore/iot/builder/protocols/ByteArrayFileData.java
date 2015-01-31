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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.castafiore.resource.FileData;

public class ByteArrayFileData implements FileData{
	
	private String name;
	
	private byte[] bytes;
	
	private String url;
	
	public ByteArrayFileData(byte[] bytes){
		this.bytes = bytes;
	}
	


	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}

	@Override
	public String getMimeType() {
		return "text/json";
	}

	@Override
	public InputStream getInputStream() throws Exception {
		return new ByteArrayInputStream(bytes);
	}

	@Override
	public void write(InputStream in) throws Exception {
		bytes = new byte[in.available()];
		in.read(bytes);
		
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
		
	}

}
