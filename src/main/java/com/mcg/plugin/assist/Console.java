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

import com.alibaba.fastjson.JSON;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.FlowInstancesUtils;
import com.mcg.util.Tools;

public class Console {

    public String info(String httpSessionId, String flowId, Object content) {
        pushMessage(httpSessionId, flowId, content, LogTypeEnum.INFO);
        return "";
    }
    
    public String success(String httpSessionId, String flowId, Object content) {
        pushMessage(httpSessionId, flowId, content, LogTypeEnum.SUCCESS);
        return "";
    }
    
    public String error(String httpSessionId, String flowId, Object content) {
        pushMessage(httpSessionId, flowId, content, LogTypeEnum.ERROR);
        return "";
    }
    
    public static void pushMessage(String httpSessionId, String flowId, Object content, LogTypeEnum logType) {
    	String flowInstanceId = Tools.genFlowInstanceId(httpSessionId, flowId);
    	ExecuteStruct executeStruct = FlowInstancesUtils.executeStructMap.get(flowInstanceId);
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW); 
        FlowBody flowBody = new FlowBody();
        String executeId = executeStruct.getRunStatus().getExecuteId();
        McgProduct mcgProduct = executeStruct.getDataMap().get(executeId);
        if(mcgProduct instanceof FlowScript) {
            flowBody.setEleType(EletypeEnum.SCRIPT.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.SCRIPT.getName());
        } else if(mcgProduct instanceof FlowJava) {
            flowBody.setEleType(EletypeEnum.JAVA.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.JAVA.getName());
        }
        
        flowBody.setFlowId(flowId);
        flowBody.setSubFlag(executeStruct.getSubFlag());
        flowBody.setLogType(logType.getValue());
        flowBody.setLogTypeDesc(logType.getName());
        flowBody.setComment("自定义输出");
        if(content != null) {
        	flowBody.setContent(JSON.toJSONString(content, true));
        } else {
        	flowBody.setContent("");
        }    
        flowBody.setEleId(executeId);
        message.setBody(flowBody);
        MessagePlugin.push(executeStruct.getSession().getId(), message);
    }
}
