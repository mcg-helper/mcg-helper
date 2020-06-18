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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

import javax.servlet.http.HttpSession;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.JSON;
import com.mcg.entity.global.topology.Topology;
import com.mcg.plugin.build.McgProduct;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class ExecuteStruct implements Serializable {

    private static final long serialVersionUID = -677740433314925325L;
    @XmlElement
    private String mcgWebScoketCode;
    @XmlElement
    private HttpSession session;
    /* 流程id */
    @XmlElement
    private String flowId;
    /* 流程实例id */
    @XmlElement
    private String flowInstanceId;
    /* 当前执行流程是否为子流程 */
    @XmlElement
    private Boolean subFlag;
    @XmlElement
    private JSON parentParam;
    /* 正在执行的流程实例 */
    @XmlElement
    private Topology topology;
    @XmlElement
    private Orders orders;
    @XmlElement
    private ConcurrentHashMap<String, McgProduct> dataMap;
    @XmlElement
    private RunStatus runStatus;
    /* 正在执行流程的future列表，包含当前流程与所有嵌套子流程的future */
    @XmlElement
    private CopyOnWriteArrayList<Future<RunStatus>> flowTaskFutureList = new CopyOnWriteArrayList<>();
    @XmlElement
    private ConcurrentHashMap<String, RunResult> runResultMap;
    /* 正在执行的子流程的运行数据 */
    @XmlElement
    private ExecuteStruct childExecuteStruct;
    
    
    public String getMcgWebScoketCode() {
		return mcgWebScoketCode;
	}
	public void setMcgWebScoketCode(String mcgWebScoketCode) {
		this.mcgWebScoketCode = mcgWebScoketCode;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getFlowInstanceId() {
		return flowInstanceId;
	}
	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}
	public Boolean getSubFlag() {
		return subFlag;
	}
	public void setSubFlag(Boolean subFlag) {
		this.subFlag = subFlag;
	}
	public Orders getOrders() {
        return orders;
    }
    public void setOrders(Orders orders) {
        this.orders = orders;
    }
    public RunStatus getRunStatus() {
        return runStatus;
    }
    public void setRunStatus(RunStatus runStatus) {
        this.runStatus = runStatus;
    }
	public ConcurrentHashMap<String, McgProduct> getDataMap() {
		return dataMap;
	}
	public void setDataMap(ConcurrentHashMap<String, McgProduct> dataMap) {
		this.dataMap = dataMap;
	}
	public ConcurrentHashMap<String, RunResult> getRunResultMap() {
		return runResultMap;
	}
	public void setRunResultMap(ConcurrentHashMap<String, RunResult> runResultMap) {
		this.runResultMap = runResultMap;
	}
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	public Topology getTopology() {
		return topology;
	}
	public void setTopology(Topology topology) {
		this.topology = topology;
	}
	public ExecuteStruct getChildExecuteStruct() {
		return childExecuteStruct;
	}
	public void setChildExecuteStruct(ExecuteStruct childExecuteStruct) {
		this.childExecuteStruct = childExecuteStruct;
	}
	public CopyOnWriteArrayList<Future<RunStatus>> getFlowTaskFutureList() {
		return flowTaskFutureList;
	}
	public void setFlowTaskFutureList(CopyOnWriteArrayList<Future<RunStatus>> flowTaskFutureList) {
		this.flowTaskFutureList = flowTaskFutureList;
	}
	public JSON getParentParam() {
		return parentParam;
	}
	public void setParentParam(JSON parentParam) {
		this.parentParam = parentParam;
	}

}
