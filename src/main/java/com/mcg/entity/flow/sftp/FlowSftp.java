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

package com.mcg.entity.flow.sftp;

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
import com.mcg.plugin.execute.strategy.FlowSftpStrategy;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class FlowSftp extends FlowBase {

	private static final long serialVersionUID = 1L;
	@NotBlank(message = "{flowSftp.id.notBlank}")
	@XmlAttribute
	private String id;
	
	@Valid
	@XmlElement
	private SftpProperty sftpProperty;
	
	@XmlElement
	private SftpUpload sftpUpload;
	
	@Override
	public void prepare(ArrayList<String> sequence, ExecuteStruct executeStruct) throws Exception {
    	this.setFlowId(executeStruct.getFlowId());
    	this.setMcgWebScoketCode(executeStruct.getMcgWebScoketCode());
    	this.setOrderNum(executeStruct.getOrderNum());
    	this.setEletypeEnum(EletypeEnum.SFTP);
		ProcessContext processContext = new ProcessContext();
		processContext.setProcessStrategy(new FlowSftpStrategy());
		processContext.prepare(sequence, this, executeStruct);
	}

	@Override
	public RunResult execute(ExecuteStruct executeStruct) throws Exception {
        ProcessContext processContext = new ProcessContext();
        processContext.setProcessStrategy(new FlowSftpStrategy());
        RunResult runResult = processContext.run(this, executeStruct);
        return runResult;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SftpProperty getSftpProperty() {
		return sftpProperty;
	}

	public void setSftpProperty(SftpProperty sftpProperty) {
		this.sftpProperty = sftpProperty;
	}

	public SftpUpload getSftpUpload() {
		return sftpUpload;
	}

	public void setSftpUpload(SftpUpload sftpUpload) {
		this.sftpUpload = sftpUpload;
	}

}
