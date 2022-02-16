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

package com.mcg.entity.flow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.mcg.entity.flow.sequence.FlowSequences;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class FlowStruct implements Serializable {

    private static final long serialVersionUID = 5270098230104370957L;
    /* 流程实例中控件的数量 */
    @XmlAttribute
    private int totalSize;
    @XmlAttribute
    private String mcgId;
    @XmlElement
    private FlowSequences flowSequences;
    @XmlElement
    private HashMap<String, List<Object>> flowElementMap;
 
	public int getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	public String getMcgId() {
		return mcgId;
	}
	public void setMcgId(String mcgId) {
		this.mcgId = mcgId;
	}
	public HashMap<String, List<Object>> getFlowElementMap() {
		return flowElementMap;
	}
	public void setFlowElementMap(HashMap<String, List<Object>> flowElementMap) {
		this.flowElementMap = flowElementMap;
	}
	public FlowSequences getFlowSequences() {
		return flowSequences;
	}
	public void setFlowSequences(FlowSequences flowSequences) {
		this.flowSequences = flowSequences;
	}
 

}