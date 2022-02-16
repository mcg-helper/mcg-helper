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
import com.mcg.entity.flow.FlowBase;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.FlowInstancesUtils;
import com.mcg.util.Tools;

public class ExceptionProcess {
	
	private static Logger logger = LoggerFactory.getLogger(ExceptionProcess.class);

	public static void execute(String mcgWebScoketCode, String httpSessionId, String flowId, McgProduct mcgProduct, String exceptionMsg) {
		String flowInstanceId = Tools.genFlowInstanceId(httpSessionId, flowId);
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();		
        FlowBase flowBase = (FlowBase)mcgProduct;
        for(EletypeEnum eletypeEnum : EletypeEnum.values()) {
        	if(eletypeEnum.getValue().equals(flowBase.getEletype())) {
                flowBody.setEleType(eletypeEnum.getValue());
                flowBody.setEleTypeDesc(eletypeEnum.getName() + "--》" + flowBase.getName());
                flowBody.setEleId(flowBase.getId());
                flowBody.setFlowId(flowBase.getFlowId());
        	}
        }
        
		ExecuteStruct executeStruct = FlowInstancesUtils.executeStructMap.get(flowInstanceId);
		flowBody.setSubFlag(executeStruct.getSubFlag());
		flowBody.setComment("发生异常");
		flowBody.setContent(exceptionMsg);
        flowBody.setLogType(LogTypeEnum.ERROR.getValue());
        flowBody.setLogTypeDesc(LogTypeEnum.ERROR.getName());
        message.setBody(flowBody);
        MessagePlugin.push(mcgWebScoketCode, executeStruct.getSession().getId(), message);
        
        logger.error("流程组件运行错误，httpSessionId:{}，异常信息：{}", executeStruct.getSession().getId(), JSON.toJSONString(message));
        
        FlowInstancesUtils.executeStructMap.get(flowInstanceId).getRunStatus().setCode("exception");
	}
}
