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

package com.mcg.plugin.assist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.data.FlowData;
import com.mcg.entity.flow.end.FlowEnd;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.linux.FlowLinux;
import com.mcg.entity.flow.python.FlowPython;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.flow.sqlexecute.FlowSqlExecute;
import com.mcg.entity.flow.sqlquery.FlowSqlQuery;
import com.mcg.entity.flow.start.FlowStart;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.flow.wonton.FlowWonton;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.FlowInstancesUtils;
import com.mcg.util.Tools;

public class ExceptionProcess {
	
	private static Logger logger = LoggerFactory.getLogger(ExceptionProcess.class);

	public static void execute(String httpSessionId, String flowId, McgProduct mcgProduct, String exceptionMsg) {
		String flowInstanceId = Tools.genFlowInstanceId(httpSessionId, flowId);
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();		
		if(mcgProduct instanceof FlowStart) {
			FlowStart flowStart = (FlowStart)mcgProduct;
            flowBody.setEleType(EletypeEnum.START.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.START.getName());
            flowBody.setEleId(flowStart.getStartId());
            flowBody.setFlowId(flowStart.getFlowId());
		} else if(mcgProduct instanceof FlowData) {
			FlowData flowData =(FlowData)mcgProduct;
            flowBody.setEleType(EletypeEnum.DATA.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.DATA.getName() + "--》" + flowData.getDataProperty().getName());
            flowBody.setEleId(flowData.getId());
            flowBody.setFlowId(flowData.getFlowId());
		} else if(mcgProduct instanceof FlowSqlQuery) {
			FlowSqlQuery flowSqlQuery = (FlowSqlQuery)mcgProduct;
            flowBody.setEleType(EletypeEnum.SQLQUERY.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.SQLQUERY.getName());
            flowBody.setEleId(flowSqlQuery.getId());	
            flowBody.setFlowId(flowSqlQuery.getFlowId());
		} else if(mcgProduct instanceof FlowSqlExecute) {
			FlowSqlExecute flowSqlExecute = (FlowSqlExecute)mcgProduct;
            flowBody.setEleType(EletypeEnum.SQLEXECUTE.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.SQLEXECUTE.getName());
            flowBody.setEleId(flowSqlExecute.getId());	
            flowBody.setFlowId(flowSqlExecute.getFlowId());
		} else if(mcgProduct instanceof FlowScript) {
            FlowScript flowScript = (FlowScript)mcgProduct;
            flowBody.setEleType(EletypeEnum.SCRIPT.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.SCRIPT.getName() + "--》" + flowScript.getScriptProperty().getScriptName());
            flowBody.setEleId(flowScript.getScriptId());
            flowBody.setFlowId(flowScript.getFlowId());
		} else if(mcgProduct instanceof FlowJava) {
			FlowJava flowJava =(FlowJava)mcgProduct;
            flowBody.setEleType(EletypeEnum.JAVA.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.JAVA.getName() + "--》" + flowJava.getJavaProperty().getName());
            flowBody.setEleId(flowJava.getId());
            flowBody.setFlowId(flowJava.getFlowId());
		} else if(mcgProduct instanceof FlowText) {
            FlowText flowText = (FlowText)mcgProduct;
            flowBody.setEleType(EletypeEnum.TEXT.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.TEXT.getName() + "--》" + flowText.getTextProperty().getName());
            flowBody.setEleId(flowText.getTextId());	
            flowBody.setFlowId(flowText.getFlowId());
		} else if(mcgProduct instanceof FlowPython) {
			FlowPython flowPython = (FlowPython)mcgProduct;
            flowBody.setEleType(EletypeEnum.PYTHON.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.PYTHON.getName() + "--》" + flowPython.getPythonProperty().getName());
            flowBody.setEleId(flowPython.getId());
            flowBody.setFlowId(flowPython.getFlowId());
		} else if(mcgProduct instanceof FlowLinux) {
			FlowLinux flowLinux = (FlowLinux)mcgProduct;
            flowBody.setEleType(EletypeEnum.LINUX.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.LINUX.getName() + "--》" + flowLinux.getLinuxProperty().getName());
            flowBody.setEleId(flowLinux.getId());
            flowBody.setFlowId(flowLinux.getFlowId());
		} else if(mcgProduct instanceof FlowWonton) {
			FlowWonton flowWonton = (FlowWonton)mcgProduct;
            flowBody.setEleType(EletypeEnum.WONTON.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.WONTON.getName() + "--》" + flowWonton.getWontonProperty().getName());
            flowBody.setEleId(flowWonton.getId());
            flowBody.setFlowId(flowWonton.getFlowId());
		} else if(mcgProduct instanceof FlowEnd) {
            FlowEnd flowEnd = (FlowEnd)mcgProduct;
            flowBody.setEleType(EletypeEnum.END.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.END.getName());
            flowBody.setEleId(flowEnd.getEndId());		
            flowBody.setFlowId(flowEnd.getFlowId());
		}
		
		ExecuteStruct executeStruct = FlowInstancesUtils.executeStructMap.get(flowInstanceId);
		flowBody.setSubFlag(executeStruct.getSubFlag());
		flowBody.setComment("发生异常");
		flowBody.setContent(exceptionMsg);
        flowBody.setLogType(LogTypeEnum.ERROR.getValue());
        flowBody.setLogTypeDesc(LogTypeEnum.ERROR.getName());
        message.setBody(flowBody);
        MessagePlugin.push(executeStruct.getSession().getId(), message);
        
        logger.error("流程组件运行错误，httpSessionId:{}，异常信息：{}", executeStruct.getSession().getId(), JSON.toJSONString(message));
        
        FlowInstancesUtils.executeStructMap.get(flowInstanceId).getRunStatus().setCode("exception");
	}
}
