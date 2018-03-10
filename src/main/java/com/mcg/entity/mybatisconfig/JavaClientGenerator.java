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
public class JavaClientGenerator implements Serializable {

    private static final long serialVersionUID = 3295427401370011191L;
    @XmlAttribute
    private String targetPackage;
    @XmlAttribute
    private String targetProject;
    @XmlAttribute
    private String type;
    
    public String getTargetPackage() {
        return targetPackage;
    }
    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }
    public String getTargetProject() {
        return targetProject;
    }
    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    
    
}
