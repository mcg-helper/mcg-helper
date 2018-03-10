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

package com.mcg.entity.flow.script;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class ScriptProperty implements Serializable {

	private static final long serialVersionUID = -7531917158740164055L;
	@NotBlank(message = "{flowScript.scriptProperty.key.notBlank}")
	@XmlElement
	private String key;
	@NotBlank(message = "{flowScript.scriptProperty.scriptName.notBlank}")
	@XmlElement
	private String scriptName;
	@XmlElement
	private String scriptDesc;
	
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public String getScriptDesc() {
		return scriptDesc;
	}
	public void setScriptDesc(String scriptDesc) {
		this.scriptDesc = scriptDesc;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
}
