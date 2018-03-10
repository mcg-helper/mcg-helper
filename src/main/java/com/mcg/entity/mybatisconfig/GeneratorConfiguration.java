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
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="generatorConfiguration") 
public class GeneratorConfiguration implements Serializable {

    private static final long serialVersionUID = 7642774121797792509L;
    @XmlElement
    private List<ClassPathEntry> classPathEntry;
    @XmlElement
    private List<Context> context;
    
    public List<ClassPathEntry> getClassPathEntry() {
        return classPathEntry;
    }
    public void setClassPathEntry(List<ClassPathEntry> classPathEntry) {
        this.classPathEntry = classPathEntry;
    }
    public List<Context> getContext() {
        return context;
    }
    public void setContext(List<Context> context) {
        this.context = context;
    }

    
}
