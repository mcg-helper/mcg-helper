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

package com.mcg.entity.global;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.mcg.common.Constants;
import com.mcg.common.sysenum.FlowVarTypeEnum;
import com.mcg.entity.global.datasource.McgDataSource;
import com.mcg.entity.global.serversource.ServerSource;
import com.mcg.entity.global.topology.Topology;
import com.mcg.entity.global.var.FlowVar;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class McgGlobal implements Serializable {

	private static final long serialVersionUID = -1997376482852765213L;
	//系统参数
	@XmlElement
	private List<FlowVar> sysVars;
	//流程键值对变量
	@XmlElement
	private List<FlowVar> flowVars;
	//数据库数据源组件数据
	@XmlElement
	private List<McgDataSource> flowDataSources;
	//流程下拉树数据
	@XmlElement
	private List<Topology> topologys;
	//服务器数据源组件数据
	@XmlElement
	private List<ServerSource> serverSources;
	
	public List<FlowVar> getSysVars() {
		List<FlowVar> sysVars = new ArrayList<FlowVar>();
		FlowVar flowVar = new FlowVar();
		flowVar.setId(FlowVarTypeEnum.SYSTEM.getValue() + "_" + UUID.randomUUID());
		flowVar.setType(FlowVarTypeEnum.SYSTEM.getValue());
		flowVar.setKey("mcg_helper_path");
		flowVar.setValue(Constants.WEB_PATH);
		flowVar.setNote("mcg-helper运行路径");
		
		sysVars.add(flowVar);
		return sysVars;
	}

	public List<FlowVar> getFlowVars() {
		return flowVars;
	}
	public void setFlowVars(List<FlowVar> flowVars) {
		this.flowVars = flowVars;
	}
	public List<McgDataSource> getFlowDataSources() {
		return flowDataSources;
	}
	public void setFlowDataSources(List<McgDataSource> flowDataSources) {
		this.flowDataSources = flowDataSources;
	}
	public List<Topology> getTopologys() {
		return topologys;
	}
	public void setTopologys(List<Topology> topologys) {
		this.topologys = topologys;
	}
	public List<ServerSource> getServerSources() {
		return serverSources;
	}
	public void setServerSources(List<ServerSource> serverSources) {
		this.serverSources = serverSources;
	}

}