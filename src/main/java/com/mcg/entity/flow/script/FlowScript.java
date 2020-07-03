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

package com.mcg.entity.flow.script;

import java.util.ArrayList;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.entity.flow.FlowBase;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.plugin.execute.ProcessContext;
import com.mcg.plugin.execute.strategy.FlowScriptStrategy;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class FlowScript extends FlowBase {

	private static final long serialVersionUID = 1319153774376874270L;
	@NotBlank(message = "{flowScript.scriptId.notBlank}")
	@XmlAttribute
	private String scriptId;
	@Valid
	@XmlElement
	private ScriptCore scriptCore;
	@Valid
	@XmlElement
	private ScriptProperty scriptProperty;
	
	@Override
	public void prepare(ArrayList<String> sequence, ExecuteStruct executeStruct) throws Exception {
    	this.setFlowId(executeStruct.getFlowId());
    	this.setMcgWebScoketCode(executeStruct.getMcgWebScoketCode());
    	this.setOrderNum(executeStruct.getOrderNum());
    	this.setEletypeEnum(EletypeEnum.SCRIPT);
        ProcessContext processContext = new ProcessContext();
        processContext.setProcessStrategy(new FlowScriptStrategy());
        processContext.prepare(sequence, this, executeStruct);
	}

	@Override
	public RunResult execute(ExecuteStruct executeStruct) throws Exception {
        ProcessContext processContext = new ProcessContext();
        processContext.setProcessStrategy(new FlowScriptStrategy());
        RunResult runResult = processContext.run(this, executeStruct);
        return runResult;
	}

	public String getScriptId() {
		return scriptId;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}

	public ScriptCore getScriptCore() {
		return scriptCore;
	}

	public void setScriptCore(ScriptCore scriptCore) {
		this.scriptCore = scriptCore;
	}

	public ScriptProperty getScriptProperty() {
		return scriptProperty;
	}

	public void setScriptProperty(ScriptProperty scriptProperty) {
		this.scriptProperty = scriptProperty;
	}
	
}