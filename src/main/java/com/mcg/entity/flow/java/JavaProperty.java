/*
 * @Copyright (c) 2018 缪聪(mcg-helper@qq.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");  
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at  
 *     
 *     http://www.apache.org/licenses/LICENSE-2.0  
 *     
 * Unless required by applicable law or agreed to in writing, software  
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
 * See the License for the specific language governing permissions and  
 * limitations under the License.
 */

package com.mcg.entity.flow.java;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class JavaProperty implements Serializable {

	private static final long serialVersionUID = -7531917158740164055L;
	@NotBlank(message = "{flowJava.javaProperty.key.notBlank}")
	@XmlElement
	private String key;
	@NotBlank(message = "{flowJava.javaProperty.name.notBlank}")
	@XmlElement
	private String name;
	@XmlElement
	private String javaDesc;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJavaDesc() {
		return javaDesc;
	}
	public void setJavaDesc(String javaDesc) {
		this.javaDesc = javaDesc;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
}
