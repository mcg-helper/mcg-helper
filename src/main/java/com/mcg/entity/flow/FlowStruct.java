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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.mcg.entity.flow.data.FlowDatas;
import com.mcg.entity.flow.end.FlowEnd;
import com.mcg.entity.flow.gmybatis.FlowGmybatises;
import com.mcg.entity.flow.java.FlowJavas;
import com.mcg.entity.flow.json.FlowJsons;
import com.mcg.entity.flow.linux.FlowLinuxs;
import com.mcg.entity.flow.model.FlowModels;
import com.mcg.entity.flow.python.FlowPythons;
import com.mcg.entity.flow.script.FlowScripts;
import com.mcg.entity.flow.sequence.FlowSequences;
import com.mcg.entity.flow.sqlexecute.FlowSqlExecutes;
import com.mcg.entity.flow.sqlquery.FlowSqlQuerys;
import com.mcg.entity.flow.start.FlowStart;
import com.mcg.entity.flow.text.FlowTexts;

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
    private FlowStart flowStart;
    @XmlElement
    private FlowModels flowModels;
    @XmlElement
    private FlowJsons flowJsons;
    @XmlElement
    private FlowSqlExecutes FlowSqlExecutes;    
    @XmlElement
    private FlowSqlQuerys flowSqlQuerys;     
    @XmlElement
    private FlowGmybatises flowGmybatises;
    @XmlElement
    private FlowDatas flowDatas;
    @XmlElement
    private FlowTexts flowTexts;
    @XmlElement
    private FlowScripts flowScripts;
    @XmlElement
    private FlowJavas flowJavas;
    @XmlElement
    private FlowEnd flowEnd;
    @XmlElement
    private FlowSequences flowSequences;
    
    public String getMcgId() {
        return mcgId;
    }
    public void setMcgId(String mcgId) {
        this.mcgId = mcgId;
    }
    public FlowModels getFlowModels() {
        return flowModels;
    }
    public void setFlowModels(FlowModels flowModels) {
        this.flowModels = flowModels;
    }
    public FlowSequences getFlowSequences() {
        return flowSequences;
    }
    public void setFlowSequences(FlowSequences flowSequences) {
        this.flowSequences = flowSequences;
    }
    public FlowStart getFlowStart() {
        return flowStart;
    }
    public void setFlowStart(FlowStart flowStart) {
        this.flowStart = flowStart;
    }
    public FlowGmybatises getFlowGmybatises() {
		return flowGmybatises;
	}
	public void setFlowGmybatises(FlowGmybatises flowGmybatises) {
		this.flowGmybatises = flowGmybatises;
	}
	public FlowTexts getFlowTexts() {
		return flowTexts;
	}
	public FlowEnd getFlowEnd() {
		return flowEnd;
	}
	public void setFlowEnd(FlowEnd flowEnd) {
		this.flowEnd = flowEnd;
	}
	public void setFlowTexts(FlowTexts flowTexts) {
		this.flowTexts = flowTexts;
	}
	public FlowScripts getFlowScripts() {
		return flowScripts;
	}
	public void setFlowScripts(FlowScripts flowScripts) {
		this.flowScripts = flowScripts;
	}
	public FlowJavas getFlowJavas() {
		return flowJavas;
	}
	public void setFlowJavas(FlowJavas flowJavas) {
		this.flowJavas = flowJavas;
	}
	public FlowJsons getFlowJsons() {
		return flowJsons;
	}
	public void setFlowJsons(FlowJsons flowJsons) {
		this.flowJsons = flowJsons;
	}
	public FlowDatas getFlowDatas() {
		return flowDatas;
	}
	public void setFlowDatas(FlowDatas flowDatas) {
		this.flowDatas = flowDatas;
	}
    public int getTotalSize() {
        return totalSize;
    }
    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
    public FlowSqlQuerys getFlowSqlQuerys() {
        return flowSqlQuerys;
    }
    public void setFlowSqlQuerys(FlowSqlQuerys flowSqlQuerys) {
        this.flowSqlQuerys = flowSqlQuerys;
    }
    public FlowSqlExecutes getFlowSqlExecutes() {
        return FlowSqlExecutes;
    }
    public void setFlowSqlExecutes(FlowSqlExecutes flowSqlExecutes) {
        FlowSqlExecutes = flowSqlExecutes;
    }
	public FlowPythons getFlowPythons() {
		return flowPythons;
	}
	public void setFlowPythons(FlowPythons flowPythons) {
		this.flowPythons = flowPythons;
	}
	
    public FlowLinuxs getFlowLinuxs() {
		return flowLinuxs;
	}
	public void setFlowLinuxs(FlowLinuxs flowLinuxs) {
		this.flowLinuxs = flowLinuxs;
	}

	@XmlElement
    private FlowPythons flowPythons;
    @XmlElement
    private FlowLinuxs flowLinuxs;
}