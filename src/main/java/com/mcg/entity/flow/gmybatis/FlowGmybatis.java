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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

import com.mcg.entity.flow.FlowBase;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.plugin.assist.ExceptionProcess;
import com.mcg.plugin.execute.ProcessContext;
import com.mcg.plugin.execute.strategy.FlowGmybatisStrategy;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class FlowGmybatis extends FlowBase {

	private static final long serialVersionUID = -1137835781411392025L;
	@NotBlank(message = "{flowGmybatis.gmybatisId.notBlank}")
	@XmlAttribute
	private String gmybatisId;
	@Valid
	@XmlElement
	private Relation relation;
	@Valid
	@XmlElement
	private GmybatisProperty gmybatisProperty;
	
	@Override
	public void prepare(ArrayList<String> sequence, ExecuteStruct executeStruct) {
		ProcessContext processContext = new ProcessContext();
		processContext.setProcessStrategy(new FlowGmybatisStrategy());
		try {
            processContext.prepare(sequence, this, executeStruct);
        } catch (Exception e) {
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	e.printStackTrace(new PrintStream(baos));  
        	String exception = baos.toString();  
        	ExceptionProcess.execute(this, exception);
        }
	}

	@Override
	public RunResult execute(ExecuteStruct executeStruct) {
        ProcessContext processContext = new ProcessContext();
        processContext.setProcessStrategy(new FlowGmybatisStrategy());
        RunResult runResult = null;
        try {
            runResult = processContext.run(this, executeStruct);
        } catch (Exception e) {
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	e.printStackTrace(new PrintStream(baos));  
        	String exception = baos.toString();  
        	ExceptionProcess.execute(this, exception);
        }
        return runResult;
	}

	public String getGmybatisId() {
		return gmybatisId;
	}

	public void setGmybatisId(String gmybatisId) {
		this.gmybatisId = gmybatisId;
	}

	public GmybatisProperty getGmybatisProperty() {
		return gmybatisProperty;
	}

	public void setGmybatisProperty(GmybatisProperty gmybatisProperty) {
		this.gmybatisProperty = gmybatisProperty;
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

}