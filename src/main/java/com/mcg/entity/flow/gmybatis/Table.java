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
public class Table implements Serializable {

	private static final long serialVersionUID = -4145473191759006328L;
	@NotBlank(message = "{flowGmybatis.relation.table.tableName.notBlank}")
	@XmlElement
	private String tableName;
	@NotBlank(message = "{flowGmybatis.relation.table.entityName.notBlank}")
	@XmlElement
	private String entityName;
	@NotBlank(message = "{flowGmybatis.relation.table.daoName.notBlank}")
	@XmlElement
	private String daoName;
	@NotBlank(message = "{flowGmybatis.relation.table.xmlName.notBlank}")
	@XmlElement
	private String xmlName;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getDaoName() {
		return daoName;
	}
	public void setDaoName(String daoName) {
		this.daoName = daoName;
	}
	public String getXmlName() {
		return xmlName;
	}
	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}
	
}