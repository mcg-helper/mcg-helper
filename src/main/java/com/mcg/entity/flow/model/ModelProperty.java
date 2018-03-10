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

package com.mcg.entity.flow.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class ModelProperty implements Serializable {
    
    private static final long serialVersionUID = 2262884505363873156L;
    @NotBlank(message = "{flowModel.modelProperty.key.notBlank}")
    @XmlElement
    private String key;
    @XmlElement
    private String className;
    @XmlElement
    private String packageName;
    @XmlElement 
    private String tableName;
    @NotBlank(message = "{flowModel.modelProperty.modelName.notBlank}")    
    @XmlElement
    private String modelName;
    @XmlElement
    private String modelDesc;
    
    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getModelDesc() {
        return modelDesc;
    }
    public void setModelDesc(String modelDesc) {
        this.modelDesc = modelDesc;
    }
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
    
}