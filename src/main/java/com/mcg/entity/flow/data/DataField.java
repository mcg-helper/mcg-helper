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
import java.util.List;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class DataField implements Serializable {

    private static final long serialVersionUID = 2978897440879391218L;
    @NotBlank(message = "{flowData.dataField.dataSourceId.notBlank}")
    @XmlElement
    private String dataSourceId;
    @NotBlank(message = "{flowData.dataField.tableName.notBlank}")
    @XmlElement
    private String tableName;
    @Valid
    @XmlElement
    private List<DataRecord> dataRecord;
	public List<DataRecord> getDataRecord() {
		return dataRecord;
	}
	public void setDataRecord(List<DataRecord> dataRecord) {
		this.dataRecord = dataRecord;
	}
	public String getDataSourceId() {
		return dataSourceId;
	}
	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}