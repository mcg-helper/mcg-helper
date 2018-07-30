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
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.mcg.entity.global.datasource.McgDataSource;
import com.mcg.entity.global.serversource.ServerSource;
import com.mcg.entity.global.topology.Topology;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class McgGlobal implements Serializable {

	private static final long serialVersionUID = -1997376482852765213L;
	//数据库数据源组件数据
	@XmlElement
	private List<McgDataSource> flowDataSources;
	//流程下拉树选中节点数据
	@XmlElement
	private Topology selected;
	//流程下拉树数据
	@XmlElement
	private List<Topology> topologys;
	
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
	public Topology getSelected() {
		return selected;
	}
	public void setSelected(Topology selected) {
		this.selected = selected;
	}
	public List<ServerSource> getServerSources() {
		return serverSources;
	}
	public void setServerSources(List<ServerSource> serverSources) {
		this.serverSources = serverSources;
	}

	//服务器数据源组件数据
	@XmlElement
	private List<ServerSource> serverSources;
}