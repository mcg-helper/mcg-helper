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

package com.mcg.entity.flow.data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class DataRecord implements Serializable {

    private static final long serialVersionUID = -1841232101261295734L;
    @XmlAttribute
    private String classField;
    @XmlAttribute
    private String tableField;
    @XmlAttribute
    private String comment;
    @XmlAttribute
    private String tableFieldType;
    @XmlAttribute
    private String dataType;
//    @Pattern(regexp="^\\d+$", message = "{flowData.dataField.dataRecord.length.int}")
    @XmlAttribute
    private int length;
//    @Pattern(regexp="^\\d+$", message = "{flowData.dataField.dataRecord.precision.notBlank}")
    @XmlAttribute
    private int precision;
    @XmlAttribute
    private boolean primary;
    @XmlAttribute
    private boolean autoincrement;
    @XmlAttribute
    private boolean mandatory;
    @XmlAttribute
    private String include;
	public String getClassField() {
		return classField;
	}
	public void setClassField(String classField) {
		this.classField = classField;
	}
	public String getTableField() {
		return tableField;
	}
	public void setTableField(String tableField) {
		this.tableField = tableField;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getTableFieldType() {
		return tableFieldType;
	}
	public void setTableFieldType(String tableFieldType) {
		this.tableFieldType = tableFieldType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public boolean isPrimary() {
		return primary;
	}
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	public boolean isAutoincrement() {
		return autoincrement;
	}
	public void setAutoincrement(boolean autoincrement) {
		this.autoincrement = autoincrement;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public String getInclude() {
		return include;
	}
	public void setInclude(String include) {
		this.include = include;
	}
    
}
