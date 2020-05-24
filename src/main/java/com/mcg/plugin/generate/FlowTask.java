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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.FlowStruct;
import com.mcg.entity.flow.data.FlowData;
import com.mcg.entity.flow.end.FlowEnd;
import com.mcg.entity.flow.git.FlowGit;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.json.FlowJson;
import com.mcg.entity.flow.linux.FlowLinux;
import com.mcg.entity.flow.loop.FlowLoop;
import com.mcg.entity.flow.process.FlowProcess;
import com.mcg.entity.flow.python.FlowPython;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.flow.sftp.FlowSftp;
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
import com.mcg.util.Tools;

public class FlowTask implements Callable<RunStatus> {
	
	private Logger logger = LoggerFactory.getLogger(FlowTask.class);
    private String httpSessionId;
    private FlowStruct flowStruct;
    private ExecuteStruct executeStruct;
    /* 是否是子流程 */
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
	        McgDirector director = new McgDirector();
	        
	        int orderNum = 1;
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
		                
		                String flowInstanceId =  Tools.genFlowInstanceId(httpSessionId, flowStruct.getMcgId());
		                flowBody.setFlowInstanceId(flowInstanceId);
		                flowBody.setFlowId(flowStruct.getMcgId());
		                flowBody.setSubFlag(executeStruct.getSubFlag());
		                McgProduct mcgProduct = executeStruct.getDataMap().get(order.getElementId());
		                RunResult result = null;
		                if(mcgProduct instanceof FlowStart) {
		                    FlowStart flowStart = (FlowStart)mcgProduct.clone();
		                    flowStart.setOrderNum(orderNum);
		                    flowStart.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowStartProduct(flowStart).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.START.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.START.getName());
		                    flowBody.setEleId(flowStart.getStartId());
		                    flowBody.setComment("运行值");
		                    flowBody.setOrderNum(orderNum);
		                } else if(mcgProduct instanceof FlowJson) {
		                    FlowJson flowJson =(FlowJson)mcgProduct.clone();
		                    flowJson.setOrderNum(orderNum);
		                    flowJson.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowJsonProduct(flowJson).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.JSON.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.JSON.getName() + "--》" + flowJson.getJsonProperty().getName());
		                    flowBody.setEleId(flowJson.getId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowSqlQuery) {
		                    FlowSqlQuery flowSqlQuery =(FlowSqlQuery)mcgProduct.clone();
		                    flowSqlQuery.setOrderNum(orderNum);
		                    flowSqlQuery.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowSqlQueryProduct(flowSqlQuery).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.SQLQUERY.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.SQLQUERY.getName() + "--》" + flowSqlQuery.getSqlQueryProperty().getName());
		                    flowBody.setEleId(flowSqlQuery.getId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowSqlExecute) {
		                    FlowSqlExecute flowSqlExecute =(FlowSqlExecute)mcgProduct.clone();
		                    flowSqlExecute.setOrderNum(orderNum);
		                    flowSqlExecute.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowSqlExecuteProduct(flowSqlExecute).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.SQLEXECUTE.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.SQLEXECUTE.getName() + "--》" + flowSqlExecute.getSqlExecuteProperty().getName());
		                    flowBody.setEleId(flowSqlExecute.getId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowData) {
		                    FlowData flowData = (FlowData)mcgProduct.clone();
		                    flowData.setOrderNum(orderNum);
		                    flowData.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowDataProduct(flowData).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.DATA.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.DATA.getName() + "--》" + flowData.getDataProperty().getName());
		                    flowBody.setEleId(flowData.getId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowText) {
		                    FlowText flowText = (FlowText) mcgProduct.clone();
		                    flowText.setOrderNum(orderNum);
		                    flowText.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowTextProduct(flowText).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.TEXT.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.TEXT.getName() + "--》" + flowText.getTextProperty().getName());
		                    flowBody.setEleId(flowText.getTextId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowScript) {
		                    FlowScript flowScript = (FlowScript)mcgProduct.clone();
		                    flowScript.setOrderNum(orderNum);
		                    flowScript.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowScriptProduct(flowScript).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.SCRIPT.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.SCRIPT.getName() + "--》" + flowScript.getScriptProperty().getScriptName());
		                    flowBody.setEleId(flowScript.getScriptId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowJava) {
		                    FlowJava flowJava = (FlowJava)mcgProduct.clone();
		                    flowJava.setOrderNum(orderNum);
		                    flowJava.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowJavaProduct(flowJava).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.JAVA.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.JAVA.getName() + "--》" + flowJava.getJavaProperty().getName());
		                    flowBody.setEleId(flowJava.getId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowPython) {
		                    FlowPython flowPython = (FlowPython)mcgProduct.clone();
		                    flowPython.setOrderNum(orderNum);
		                    flowPython.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowPythonProduct(flowPython).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.PYTHON.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.PYTHON.getName() + "--》" + flowPython.getPythonProperty().getName());
		                    flowBody.setEleId(flowPython.getId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowLinux) {
		                	FlowLinux flowLinux = (FlowLinux)mcgProduct.clone();
		                	flowLinux.setOrderNum(orderNum);
		                	flowLinux.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowLinuxProduct(flowLinux).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.LINUX.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.LINUX.getName() + "--》" + flowLinux.getLinuxProperty().getName());
		                    flowBody.setEleId(flowLinux.getId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowWonton) {
		                	FlowWonton flowWonton = (FlowWonton)mcgProduct.clone();
		                	flowWonton.setOrderNum(orderNum);
		                	flowWonton.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowWontonProduct(flowWonton).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.WONTON.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.WONTON.getName() + "--》" + flowWonton.getWontonProperty().getName());
		                    flowBody.setEleId(flowWonton.getId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowProcess) {
		                	FlowProcess flowProcess = (FlowProcess)mcgProduct.clone();
		                	flowProcess.setOrderNum(orderNum);
		                	flowProcess.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowProcessProduct(flowProcess).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.PROCESS.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.PROCESS.getName() + "--》" + flowProcess.getProcessProperty().getName());
		                    flowBody.setEleId(flowProcess.getId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowLoop) {
		                	FlowLoop flowLoop = (FlowLoop)mcgProduct.clone();
		                	flowLoop.setOrderNum(orderNum);
		                	flowLoop.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowLoopProduct(flowLoop).build(executeStruct);
		                    swicth = executeStruct.getRunStatus().getLoopStatusMap().get(flowLoop.getId()).getSwicth();
		                    flowBody.setEleType(EletypeEnum.LOOP.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.LOOP.getName() + "--》" + flowLoop.getLoopProperty().getName());
		                    flowBody.setEleId(flowLoop.getId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowGit) {
		                	FlowGit flowGit = (FlowGit)mcgProduct.clone();
		                	flowGit.setOrderNum(orderNum);
		                	flowGit.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowGitProduct(flowGit).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.GIT.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.GIT.getName() + "--》" + flowGit.getGitProperty().getName());
		                    flowBody.setEleId(flowGit.getId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowSftp) {
		                	FlowSftp flowSftp = (FlowSftp)mcgProduct.clone();
		                	flowSftp.setOrderNum(orderNum);
		                	flowSftp.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowSftpProduct(flowSftp).build(executeStruct);
		                    flowBody.setEleType(EletypeEnum.SFTP.getValue());
		                    flowBody.setEleTypeDesc(EletypeEnum.SFTP.getName() + "--》" + flowSftp.getSftpProperty().getName());
		                    flowBody.setEleId(flowSftp.getId());
		                    flowBody.setComment("运行值");
		                } else if(mcgProduct instanceof FlowEnd) {
		                    FlowEnd flowEnd = (FlowEnd)mcgProduct.clone();
		                    flowEnd.setOrderNum(orderNum);
		                    flowEnd.setFlowId(flowStruct.getMcgId());
		                    result = director.getFlowEndProduct(flowEnd).build(executeStruct);
		                    flowBody.setOrderNum(orderNum);
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
	            		orderNum ++;
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
	                fileFlowBody.setSubFlag(executeStruct.getSubFlag());
	                fileFlowBody.setComment("流程执行完毕");
	                
	                runStatus.setExecuteId(tempId);
	                if(runStatus.getAvailableFileMap().size() > 0) {
	                	Map<String, ConcurrentHashMap<String, String>> availableFileMap = new HashMap<>();
	                	availableFileMap.put("availableFileMap", runStatus.getAvailableFileMap());
	                	fileFlowBody.setContent(JSON.toJSONString(availableFileMap));
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
	            
	            String flowInstanceId = Tools.genFlowInstanceId(httpSessionId, flowStruct.getMcgId());
	            FlowInstancesUtils.executeStructMap.remove(flowInstanceId);
	        }
		} catch (InterruptedException e) {
			executeStruct.getRunStatus().setInterrupt(true);
        	logger.error("流程中断，抛出InterruptedException，异常信息：", e);
		} catch(CancellationException e) {
 			executeStruct.getRunStatus().setInterrupt(true);
         	logger.error("流程中断，抛出CancellationException，异常信息：", e);
        } catch (Exception e) {
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	e.printStackTrace(new PrintStream(baos));  
        	String exception = baos.toString();  
        	logger.error("流程执行发生错误，异常信息：", e);
			ExceptionProcess.execute(httpSessionId, flowStruct.getMcgId(), executeStruct.getDataMap().get(executeStruct.getRunStatus().getExecuteId()), exception);
		}
		
		return executeStruct.getRunStatus();
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

	public Boolean getSubFlag() {
		return subFlag;
	}

	public void setSubFlag(Boolean subFlag) {
		this.subFlag = subFlag;
	}

}
