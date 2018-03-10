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

package com.mcg.entity.generate;

import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.mcg.plugin.build.McgProduct;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class RunResult extends McgProduct implements Serializable {

    private static final long serialVersionUID = -1321328867544959522L;
    @XmlAttribute
    private String elementId;
    @XmlElement
    private String jsonVar;
    @XmlElement
    private String sourceCode;
    
    @Override
    public void prepare(ArrayList<String> sequence, ExecuteStruct executeStruct) {

    }
    @Override
    public RunResult execute(ExecuteStruct executeStruct) {
        RunResult result = new RunResult();
        return result;
    }    
    
    public String getElementId() {
        return elementId;
    }
    public void setElementId(String elementId) {
        this.elementId = elementId;
    }
    public String getJsonVar() {
        return jsonVar;
    }
    public void setJsonVar(String jsonVar) {
        this.jsonVar = jsonVar;
    }
    public String getSourceCode() {
        return sourceCode;
    }
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

}
