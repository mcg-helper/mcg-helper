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

package com.mcg.entity.flow.gmybatis;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class GmybatisProperty implements Serializable {
 
	private static final long serialVersionUID = -8098204720747092242L;
	@NotBlank(message = "{flowGmybatis.gmybatisProperty.key.notBlank}")
	@XmlElement
	private String key;
	@NotBlank(message = "{flowGmybatis.gmybatisProperty.name.notBlank}")
	@XmlElement
	private String name;	
	@NotBlank(message = "{flowGmybatis.gmybatisProperty.pojoProjectPath.notBlank}")
	@XmlElement
	private String pojoProjectPath;
	@NotBlank(message = "{flowGmybatis.gmybatisProperty.pojoOutPath.notBlank}")
	@XmlElement
	private String pojoOutPath;
	@NotBlank(message = "{flowGmybatis.gmybatisProperty.xmlProjectPath.notBlank}")
	@XmlElement
	private String xmlProjectPath;	
	@NotBlank(message = "{flowGmybatis.gmybatisProperty.xmlOutPath.notBlank}")
	@XmlElement
	private String xmlOutPath;
	@NotBlank(message = "{flowGmybatis.gmybatisProperty.daoProjectPath.notBlank}")
	@XmlElement
	private String daoProjectPath;	
	@NotBlank(message = "{flowGmybatis.gmybatisProperty.daoOutPath.notBlank}")
	@XmlElement
	private String daoOutPath;

	public String getPojoOutPath() {
		return pojoOutPath;
	}

	public void setPojoOutPath(String pojoOutPath) {
		this.pojoOutPath = pojoOutPath;
	}

	public String getXmlOutPath() {
		return xmlOutPath;
	}

	public void setXmlOutPath(String xmlOutPath) {
		this.xmlOutPath = xmlOutPath;
	}

	public String getDaoOutPath() {
		return daoOutPath;
	}

	public void setDaoOutPath(String daoOutPath) {
		this.daoOutPath = daoOutPath;
	}

	public String getPojoProjectPath() {
		return pojoProjectPath;
	}

	public void setPojoProjectPath(String pojoProjectPath) {
		this.pojoProjectPath = pojoProjectPath;
	}

	public String getXmlProjectPath() {
		return xmlProjectPath;
	}

	public void setXmlProjectPath(String xmlProjectPath) {
		this.xmlProjectPath = xmlProjectPath;
	}

	public String getDaoProjectPath() {
		return daoProjectPath;
	}

	public void setDaoProjectPath(String daoProjectPath) {
		this.daoProjectPath = daoProjectPath;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}