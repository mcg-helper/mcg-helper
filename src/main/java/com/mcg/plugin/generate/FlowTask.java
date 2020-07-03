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
import com.mcg.common.sysenum.LogOutTypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.FlowBase;
import com.mcg.entity.flow.end.FlowEnd;
import com.mcg.entity.flow.loop.FlowLoop;
import com.mcg.entity.flow.start.FlowStart;
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
	private String mcgWebScoketCode;
    private String httpSessionId;
    private ExecuteStruct executeStruct;
    /* 是否是子流程 */
    private Boolean subFlag;
    public FlowTask(){
    	
    }
    
    public FlowTask(String mcgWebScoketCode, String httpSessionId, ExecuteStruct executeStruct, Boolean subFlag) {
    	this.mcgWebScoketCode = mcgWebScoketCode;
        this.httpSessionId = httpSessionId;
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
		                
		                String flowInstanceId =  Tools.genFlowInstanceId(httpSessionId, executeStruct.getFlowId());
		                flowBody.setFlowInstanceId(flowInstanceId);
		                flowBody.setFlowId(executeStruct.getFlowId());
		                flowBody.setSubFlag(executeStruct.getSubFlag());
		                McgProduct mcgProduct = executeStruct.getDataMap().get(order.getElementId());
		                executeStruct.setOrderNum(orderNum);
		                
	                    McgProduct mcgProductClone = (McgProduct)mcgProduct.clone();
	                    RunResult result = director.getFlowMcgProduct(mcgProductClone).build(executeStruct);
	                    FlowBase flowBase = (FlowBase)mcgProductClone;
	                    flowBody.setLogOutType(LogOutTypeEnum.RESULT.getValue());
	                    flowBody.setEleType(flowBase.getEletypeEnum().getValue());
	                    flowBody.setEleTypeDesc(flowBase.getEletypeEnum().getName());
	                    flowBody.setEleId(order.getElementId());
	                    flowBody.setComment(LogOutTypeEnum.RESULT.getName());
	                    if(mcgProduct instanceof FlowStart || mcgProduct instanceof FlowEnd) {
	                    	flowBody.setOrderNum(orderNum);
	                    }
	                    if(mcgProduct instanceof FlowLoop) {
	                    	swicth = executeStruct.getRunStatus().getLoopStatusMap().get(order.getElementId()).getSwicth();	
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
		                
		                
		                flowBody.setLogOutType(LogOutTypeEnum.RESULT.getValue());
		                flowBody.setLogType(LogTypeEnum.INFO.getValue());
		                flowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
		                message.setBody(flowBody);
		                MessagePlugin.push(mcgWebScoketCode, httpSessionId, message);
		                
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
	            MessagePlugin.push(mcgWebScoketCode, httpSessionId, messageComplete);     
	          
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
	                MessagePlugin.push(mcgWebScoketCode, httpSessionId, fileMessage);
		            
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
	                MessagePlugin.push(mcgWebScoketCode, httpSessionId, fileMessage);
	            }
	            
	            String flowInstanceId = Tools.genFlowInstanceId(httpSessionId, executeStruct.getFlowId());
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
			ExceptionProcess.execute(mcgWebScoketCode, httpSessionId, executeStruct.getFlowId(), executeStruct.getDataMap().get(executeStruct.getRunStatus().getExecuteId()), exception);
		}
		
		return executeStruct.getRunStatus();
	}

    public String getHttpSessionId() {
        return httpSessionId;
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
