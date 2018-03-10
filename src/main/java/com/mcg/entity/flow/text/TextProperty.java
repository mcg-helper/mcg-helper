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

package com.mcg.entity.flow.text;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class TextProperty implements Serializable {

	private static final long serialVersionUID = -9222927738223425357L;
	@NotBlank(message = "{flowText.textProperty.name.notBlank}")
	@XmlElement
	private String name;
    @NotBlank(message = "{flowText.textProperty.key.notBlank}")
    @XmlElement
    private String key;	
	@NotBlank(message = "{flowText.textProperty.fileName.notBlank}")
	@XmlElement
	private String fileName;
	@NotBlank(message = "{flowText.textProperty.outPutPath.notBlank}")
	@XmlElement
	private String outPutPath;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOutPutPath() {
		return outPutPath;
	}
	public void setOutPutPath(String outPutPath) {
		this.outPutPath = outPutPath;
	}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
	
}
