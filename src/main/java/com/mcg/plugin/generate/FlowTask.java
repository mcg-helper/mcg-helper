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

package com.mcg.plugin.generate;

import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.FlowStruct;
import com.mcg.entity.flow.data.FlowData;
import com.mcg.entity.flow.end.FlowEnd;
import com.mcg.entity.flow.gmybatis.FlowGmybatis;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.json.FlowJson;
import com.mcg.entity.flow.linux.FlowLinux;
import com.mcg.entity.flow.model.FlowModel;
import com.mcg.entity.flow.python.FlowPython;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.flow.sqlexecute.FlowSqlExecute;
import com.mcg.entity.flow.sqlquery.FlowSqlQuery;
import com.mcg.entity.flow.start.FlowStart;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.Order;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.entity.message.NotifyBody;
import com.mcg.plugin.build.McgDirector;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.service.FlowService;

public class FlowTask implements Runnable {
	public static ThreadLocal<FlowTask> executeLocal = new ThreadLocal<FlowTask>();
    private String httpSessionId;
    private FlowStruct flowStruct;
    private FlowService flowService;
    private ExecuteStruct executeStruct;
    
    public FlowTask(){
    	
    }
    
    public FlowTask(String httpSessionId, FlowStruct flowStruct, FlowService flowService, ExecuteStruct executeStruct) {
        this.httpSessionId = httpSessionId;
        this.flowStruct = flowStruct;
        this.flowService = flowService;
        this.executeStruct = executeStruct;
    }
    
    @Override
    public void run() {
    	getFlowTask();
        McgDirector director = new McgDirector();
        
        if(executeStruct.getOrders() != null && executeStruct.getOrders().getOrder() != null && executeStruct.getOrders().getOrder().size() > 0) {
            for(int i=0; i<executeStruct.getOrders().getOrder().size(); i++) {
                
                Order order = executeStruct.getOrders().getOrder().get(i);
                Message message = MessagePlugin.getMessage();
                message.getHeader().setMesType(MessageTypeEnum.FLOW);
                FlowBody flowBody = new FlowBody();
                
                McgProduct mcgProduct = executeStruct.getDataMap().get(order.getElementId());
                RunResult result = null;
                if(mcgProduct instanceof FlowStart) {
                    FlowStart flowStart = (FlowStart)mcgProduct;
                    result = director.getFlowStartProduct(flowStart).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.START.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.START.getName());
                    flowBody.setEleId(flowStart.getStartId());
                    flowBody.setComment("运行值");
                } else if(mcgProduct instanceof FlowModel) { 
                    FlowModel flowModel =(FlowModel)mcgProduct;
                    result = director.getFlowModelProduct(flowModel).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.MODEL.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.MODEL.getName() + "--》" + flowModel.getModelProperty().getModelName());
                    flowBody.setEleId(flowModel.getModelId());
                    flowBody.setComment("运行值");
                } else if(mcgProduct instanceof FlowJson) {
                    FlowJson flowJson =(FlowJson)mcgProduct;
                    result = director.getFlowJsonProduct(flowJson).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.JSON.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.JSON.getName() + "--》" + flowJson.getJsonProperty().getName());
                    flowBody.setEleId(flowJson.getId());
                    flowBody.setComment("运行值");
                } else if(mcgProduct instanceof FlowSqlQuery) {
                    FlowSqlQuery flowSqlQuery =(FlowSqlQuery)mcgProduct;
                    result = director.getFlowSqlQueryProduct(flowSqlQuery).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.SQLQUERY.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.SQLQUERY.getName() + "--》" + flowSqlQuery.getSqlQueryProperty().getName());
                    flowBody.setEleId(flowSqlQuery.getId());
                    flowBody.setComment("运行值");
                } else if(mcgProduct instanceof FlowSqlExecute) {
                    FlowSqlExecute flowSqlExecute =(FlowSqlExecute)mcgProduct;
                    result = director.getFlowSqlExecuteProduct(flowSqlExecute).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.SQLEXECUTE.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.SQLEXECUTE.getName() + "--》" + flowSqlExecute.getSqlExecuteProperty().getName());
                    flowBody.setEleId(flowSqlExecute.getId());
                    flowBody.setComment("运行值");
                } else if(mcgProduct instanceof FlowGmybatis) {
                    FlowGmybatis flowGmybatis = (FlowGmybatis)mcgProduct;
                    result = director.getFlowGmybatisProduct(flowGmybatis).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.GMYBATIS.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.GMYBATIS.getName() + "--》" + flowGmybatis.getGmybatisProperty().getName());
                    flowBody.setEleId(flowGmybatis.getGmybatisId());
                    flowBody.setComment("运行值");
                } else if(mcgProduct instanceof FlowData) {
                    FlowData flowData = (FlowData)mcgProduct;
                    result = director.getFlowDataProduct(flowData).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.DATA.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.DATA.getName() + "--》" + flowData.getDataProperty().getName());
                    flowBody.setEleId(flowData.getId());
                    flowBody.setComment("运行值");
                } else if(mcgProduct instanceof FlowText) {
                    FlowText flowText = (FlowText)mcgProduct;
                    result = director.getFlowTextProduct(flowText).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.TEXT.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.TEXT.getName() + "--》" + flowText.getTextProperty().getName());
                    flowBody.setEleId(flowText.getTextId());
                    flowBody.setComment("运行值");
                } else if(mcgProduct instanceof FlowScript) {
                    FlowScript flowScript = (FlowScript)mcgProduct;
                    result = director.getFlowScriptProduct(flowScript).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.SCRIPT.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.SCRIPT.getName() + "--》" + flowScript.getScriptProperty().getScriptName());
                    flowBody.setEleId(flowScript.getScriptId());
                    flowBody.setComment("运行值");
                } else if(mcgProduct instanceof FlowJava) {
                    FlowJava flowJava = (FlowJava)mcgProduct;
                    result = director.getFlowJavaProduct(flowJava).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.JAVA.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.JAVA.getName() + "--》" + flowJava.getJavaProperty().getName());
                    flowBody.setEleId(flowJava.getId());
                    flowBody.setComment("运行值");
                } else if(mcgProduct instanceof FlowPython) {
                    FlowPython flowPython = (FlowPython)mcgProduct;
                    result = director.getFlowPythonProduct(flowPython).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.PYTHON.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.PYTHON.getName() + "--》" + flowPython.getPythonProperty().getName());
                    flowBody.setEleId(flowPython.getId());
                    flowBody.setComment("运行值");
                } else if(mcgProduct instanceof FlowLinux) {
                	FlowLinux flowLinux = (FlowLinux)mcgProduct;
                    result = director.getFlowLinuxProduct(flowLinux).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.LINUX.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.LINUX.getName() + "--》" + flowLinux.getLinuxProperty().getName());
                    flowBody.setEleId(flowLinux.getId());
                    flowBody.setComment("运行值");
                } else if(mcgProduct instanceof FlowEnd) {
                    FlowEnd flowEnd = (FlowEnd)mcgProduct;
                    result = director.getFlowEndProduct(flowEnd).build(executeStruct);
                    flowBody.setEleType(EletypeEnum.END.getValue());
                    flowBody.setEleTypeDesc(EletypeEnum.END.getName());
                    flowBody.setEleId(flowEnd.getEndId());
                    flowBody.setComment("运行值");
                }
                
                executeStruct.getRunResultMap().put(order.getElementId(), result);
                
                if(result == null) {
                    flowBody.setContent("");
                } else if(result.getSourceCode() != null && !"".equals(result.getSourceCode())) {
                    flowBody.setContent(result.getSourceCode());
                } else if(result.getJsonVar() != null && !"".equals(result.getJsonVar())) {
                    flowBody.setContent(result.getJsonVar());
                } else {
                    flowBody.setContent("系统异常");
                }
                
                flowBody.setLogType(LogTypeEnum.INFO.getValue());
                flowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
                message.setBody(flowBody);
                MessagePlugin.push(httpSessionId, message);     
                
                if(!"success".equals(executeStruct.getRunStatus().getCode()) ) {
                    break;
                }
                
                if(i == (executeStruct.getOrders().getOrder().size() -1)) {
                    Message messageComplete = MessagePlugin.getMessage();
                    messageComplete.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
                    NotifyBody notifyBody = new NotifyBody();
                    notifyBody.setContent("流程执行完毕！");
                    notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
                    messageComplete.setBody(notifyBody);
                    MessagePlugin.push(httpSessionId, messageComplete);                      
                }
            }
        }
        
    }

    public FlowTask getFlowTask() {
    	FlowTask flowTask = executeLocal.get();
    	if(flowTask == null) {
    		flowTask = new FlowTask(httpSessionId, flowStruct, flowService, executeStruct);
    		executeLocal.set(flowTask);
    	}
    	return flowTask;
    }     
    
    public String getHttpSessionId() {
        return httpSessionId;
    }

	public FlowStruct getFlowStruct() {
		return flowStruct;
	}

    public ExecuteStruct getExecuteStruct() {
        return executeStruct;
    }

    public void setExecuteStruct(ExecuteStruct executeStruct) {
        this.executeStruct = executeStruct;
    }

}
