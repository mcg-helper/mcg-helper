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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import com.alibaba.fastjson.JSON;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.FlowStruct;
import com.mcg.entity.flow.data.FlowData;
import com.mcg.entity.flow.end.FlowEnd;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.json.FlowJson;
import com.mcg.entity.flow.linux.FlowLinux;
import com.mcg.entity.flow.loop.FlowLoop;
import com.mcg.entity.flow.model.FlowModel;
import com.mcg.entity.flow.process.FlowProcess;
import com.mcg.entity.flow.python.FlowPython;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.flow.sqlexecute.FlowSqlExecute;
import com.mcg.entity.flow.sqlquery.FlowSqlQuery;
import com.mcg.entity.flow.start.FlowStart;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.flow.wonton.FlowWonton;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.Order;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.generate.RunStatus;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.entity.message.NotifyBody;
import com.mcg.plugin.assist.ExceptionProcess;
import com.mcg.plugin.build.McgDirector;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.FlowInstancesUtils;

public class FlowTask implements Callable<RunStatus> {
	public static ThreadLocal<FlowTask> executeLocal = new ThreadLocal<FlowTask>();
    private String httpSessionId;
    private FlowStruct flowStruct;
    private ExecuteStruct executeStruct;
    private Boolean subFlag;
    public FlowTask(){
    	
    }
    
    public FlowTask(String httpSessionId, FlowStruct flowStruct, ExecuteStruct executeStruct, Boolean subFlag) {
        this.httpSessionId = httpSessionId;
        this.flowStruct = flowStruct;
        this.executeStruct = executeStruct;
        this.subFlag = subFlag;
    }
    
	@Override
	public RunStatus call() throws Exception {
		try {
		getFlowTask();
		
		FlowInstancesUtils.executeStructMap.put(flowStruct.getMcgId(), executeStruct);
        McgDirector director = new McgDirector();
        
        if(executeStruct.getOrders() != null && executeStruct.getOrders().getOrder() != null && executeStruct.getOrders().getOrder().size() > 0) {
            for(int i=0; i<executeStruct.getOrders().getOrder().size(); i++) {
                if(executeStruct.getRunStatus().isInterrupt()) {
                	break;
                }
            	List<Order> orderLoopList = executeStruct.getOrders().getOrder().get(i);
            	
            	int loopIndex = 0;
            	/* 循环的开关 */
            	boolean swicth = true;
            	do {
	                
	                Order order = orderLoopList.get(loopIndex ++);
	                Message message = MessagePlugin.getMessage();
	                message.getHeader().setMesType(MessageTypeEnum.FLOW);
	                FlowBody flowBody = new FlowBody();
	                
	                McgProduct mcgProduct = executeStruct.getDataMap().get(order.getElementId());
	                RunResult result = null;
	                if(mcgProduct instanceof FlowStart) {
	                    FlowStart flowStart = (FlowStart)mcgProduct.clone();
	                    result = director.getFlowStartProduct(flowStart).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.START.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.START.getName());
	                    flowBody.setEleId(flowStart.getStartId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowModel) { 
	                    FlowModel flowModel =(FlowModel)mcgProduct.clone();
	                    result = director.getFlowModelProduct(flowModel).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.MODEL.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.MODEL.getName() + "--》" + flowModel.getModelProperty().getModelName());
	                    flowBody.setEleId(flowModel.getModelId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowJson) {
	                    FlowJson flowJson =(FlowJson)mcgProduct.clone();
	                    result = director.getFlowJsonProduct(flowJson).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.JSON.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.JSON.getName() + "--》" + flowJson.getJsonProperty().getName());
	                    flowBody.setEleId(flowJson.getId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowSqlQuery) {
	                    FlowSqlQuery flowSqlQuery =(FlowSqlQuery)mcgProduct.clone();
	                    result = director.getFlowSqlQueryProduct(flowSqlQuery).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.SQLQUERY.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.SQLQUERY.getName() + "--》" + flowSqlQuery.getSqlQueryProperty().getName());
	                    flowBody.setEleId(flowSqlQuery.getId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowSqlExecute) {
	                    FlowSqlExecute flowSqlExecute =(FlowSqlExecute)mcgProduct.clone();
	                    result = director.getFlowSqlExecuteProduct(flowSqlExecute).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.SQLEXECUTE.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.SQLEXECUTE.getName() + "--》" + flowSqlExecute.getSqlExecuteProperty().getName());
	                    flowBody.setEleId(flowSqlExecute.getId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowData) {
	                    FlowData flowData = (FlowData)mcgProduct.clone();
	                    result = director.getFlowDataProduct(flowData).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.DATA.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.DATA.getName() + "--》" + flowData.getDataProperty().getName());
	                    flowBody.setEleId(flowData.getId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowText) {
	                    FlowText flowText = (FlowText) mcgProduct.clone();
	                    result = director.getFlowTextProduct(flowText).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.TEXT.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.TEXT.getName() + "--》" + flowText.getTextProperty().getName());
	                    flowBody.setEleId(flowText.getTextId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowScript) {
	                    FlowScript flowScript = (FlowScript)mcgProduct.clone();
	                    result = director.getFlowScriptProduct(flowScript).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.SCRIPT.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.SCRIPT.getName() + "--》" + flowScript.getScriptProperty().getScriptName());
	                    flowBody.setEleId(flowScript.getScriptId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowJava) {
	                    FlowJava flowJava = (FlowJava)mcgProduct.clone();
	                    result = director.getFlowJavaProduct(flowJava).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.JAVA.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.JAVA.getName() + "--》" + flowJava.getJavaProperty().getName());
	                    flowBody.setEleId(flowJava.getId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowPython) {
	                    FlowPython flowPython = (FlowPython)mcgProduct.clone();
	                    result = director.getFlowPythonProduct(flowPython).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.PYTHON.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.PYTHON.getName() + "--》" + flowPython.getPythonProperty().getName());
	                    flowBody.setEleId(flowPython.getId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowLinux) {
	                	FlowLinux flowLinux = (FlowLinux)mcgProduct.clone();
	                    result = director.getFlowLinuxProduct(flowLinux).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.LINUX.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.LINUX.getName() + "--》" + flowLinux.getLinuxProperty().getName());
	                    flowBody.setEleId(flowLinux.getId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowWonton) {
	                	FlowWonton flowWonton = (FlowWonton)mcgProduct.clone();
	                    result = director.getFlowWontonProduct(flowWonton).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.WONTON.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.WONTON.getName() + "--》" + flowWonton.getWontonProperty().getName());
	                    flowBody.setEleId(flowWonton.getId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowProcess) {
	                	FlowProcess flowProcess = (FlowProcess)mcgProduct.clone();
	                    result = director.getFlowProcessProduct(flowProcess).build(executeStruct);
	                    flowBody.setEleType(EletypeEnum.PROCESS.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.PROCESS.getName() + "--》" + flowProcess.getProcessProperty().getName());
	                    flowBody.setEleId(flowProcess.getId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowLoop) {
	                	FlowLoop flowLoop = (FlowLoop)mcgProduct.clone();
	                    result = director.getFlowLoopProduct(flowLoop).build(executeStruct);
	                    swicth = executeStruct.getRunStatus().getLoopStatusMap().get(flowLoop.getId()).getSwicth();
	                    flowBody.setEleType(EletypeEnum.LOOP.getValue());
	                    flowBody.setEleTypeDesc(EletypeEnum.LOOP.getName() + "--》" + flowLoop.getLoopProperty().getName());
	                    flowBody.setEleId(flowLoop.getId());
	                    flowBody.setComment("运行值");
	                } else if(mcgProduct instanceof FlowEnd) {
	                    FlowEnd flowEnd = (FlowEnd)mcgProduct.clone();
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
	                } else if(result.getJsonVar() == null && result.getSourceCode() == null) {
	                    flowBody.setContent("");
	                }  else {
	                    flowBody.setContent("控件运行值异常");
	                }
	                
	                flowBody.setLogType(LogTypeEnum.INFO.getValue());
	                flowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
	                message.setBody(flowBody);
	                MessagePlugin.push(httpSessionId, message);
	                
	                if(!"success".equals(executeStruct.getRunStatus().getCode()) ) {
	                    break;
	                }
	                
            		// 当有流程实例中存在循环时，重置下标进行达到无限循环
            		if(orderLoopList.size()  == loopIndex) {
            			loopIndex = 0;
            		}
            		
				} while (!executeStruct.getRunStatus().isInterrupt() && orderLoopList.size() > 1 && swicth);

            }
            
            Message messageComplete = MessagePlugin.getMessage();
            messageComplete.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
            NotifyBody notifyBody = new NotifyBody();
            if(executeStruct.getRunStatus().isInterrupt()) {
            	notifyBody.setContent("【" + executeStruct.getTopology().getName() + "】流程中断完毕！");
            } else {
            	notifyBody.setContent("【" + executeStruct.getTopology().getName() + "】流程执行完毕！");
            }
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
            messageComplete.setBody(notifyBody);
            MessagePlugin.push(httpSessionId, messageComplete);     
          
            RunStatus runStatus = executeStruct.getRunStatus();
            //流程实例执行时有产生文件
            if(!executeStruct.getRunStatus().isInterrupt()) {
                Message fileMessage = MessagePlugin.getMessage();
                fileMessage.getHeader().setMesType(MessageTypeEnum.FLOW);
                FlowBody fileFlowBody = new FlowBody();
                if(subFlag) {
                	fileFlowBody.setEleType("subFinish");
                } else {
                	fileFlowBody.setEleType("finish");
                }
                fileFlowBody.setEleTypeDesc(executeStruct.getTopology().getName());
                String tempId = UUID.randomUUID().toString();
                fileFlowBody.setEleId(tempId);
                fileFlowBody.setComment("流程执行完毕");
                
                runStatus.setExecuteId(tempId);
                if(runStatus.getAvailableFileMap().size() > 0) {
                	fileFlowBody.setContent(JSON.toJSONString(runStatus));
                } else {
                	fileFlowBody.setContent("none");
                }
                fileFlowBody.setLogType(LogTypeEnum.INFO.getValue());
                fileFlowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
                fileMessage.setBody(fileFlowBody);
                MessagePlugin.push(httpSessionId, fileMessage);
	            
            } else {
                Message fileMessage = MessagePlugin.getMessage();
                fileMessage.getHeader().setMesType(MessageTypeEnum.FLOW);
                FlowBody fileFlowBody = new FlowBody();
                fileFlowBody.setEleType("interrupt");
                fileFlowBody.setEleTypeDesc(executeStruct.getTopology().getName());
                String tempId = UUID.randomUUID().toString();
                fileFlowBody.setEleId(tempId);
                fileFlowBody.setComment("流程中断完毕");
                
                runStatus.setExecuteId(tempId);

                fileFlowBody.setContent("执行引擎收到中断信号，流程已停止执行！");
                fileFlowBody.setLogType(LogTypeEnum.INFO.getValue());
                fileFlowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
                fileMessage.setBody(fileFlowBody);
                MessagePlugin.push(httpSessionId, fileMessage);
            }
            
            FlowInstancesUtils.executeStructMap.remove(flowStruct.getMcgId());

        }
		
		} catch (Exception e) {
			ExceptionProcess.execute(executeStruct.getDataMap().get(executeStruct.getRunStatus().getExecuteId()), e.getMessage());
		}
		
		return executeStruct.getRunStatus();
		
		
	}
    
   

    public FlowTask getFlowTask() {
    	FlowTask flowTask = executeLocal.get();
    	if(flowTask == null) {
    		flowTask = new FlowTask(httpSessionId, flowStruct, executeStruct, subFlag);
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
