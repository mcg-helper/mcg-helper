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

package com.mcg.entity.mybatisconfig;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class Table implements Serializable {

    private static final long serialVersionUID = -5658345198832987564L;
    @XmlAttribute
    private String schema;
    @XmlAttribute
    private String tableName;
    @XmlAttribute
    private String domainObjectName;
    @XmlAttribute
    private String mapperName;
    
    public String getSchema() {
        return schema;
    }
    public void setSchema(String schema) {
        this.schema = schema;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getDomainObjectName() {
        return domainObjectName;
    }
    public void setDomainObjectName(String domainObjectName) {
        this.domainObjectName = domainObjectName;
    }
    public String getMapperName() {
        return mapperName;
    }
    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }
    
}
