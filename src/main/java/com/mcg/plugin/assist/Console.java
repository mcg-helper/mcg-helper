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
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.generate.FlowTask;
import com.mcg.plugin.websocket.MessagePlugin;

public class Console {

    public String info(Object content) {
        pushMessage(content, LogTypeEnum.INFO);
        return "";
    }
    
    public String success(Object content) {
        pushMessage(content, LogTypeEnum.SUCCESS);
        return "";
    }
    
    public String error(Object content) {
        pushMessage(content, LogTypeEnum.ERROR);
        return "";
    }
    
    public static void pushMessage(Object content, LogTypeEnum logType) {
        FlowTask flowTask = FlowTask.executeLocal.get();
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW); 
        FlowBody flowBody = new FlowBody();
        flowBody.setEleType(EletypeEnum.SCRIPT.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.SCRIPT.getName());        
        flowBody.setLogType(logType.getValue());
        flowBody.setLogTypeDesc(logType.getName());
        flowBody.setComment("自定义输出");
        if(content != null) {
        	flowBody.setContent(JSON.toJSONString(content, true));
        } else {
        	flowBody.setContent("");
        }    
        flowBody.setEleId(flowTask.getExecuteStruct().getRunStatus().getExecuteId());
        message.setBody(flowBody);
        MessagePlugin.push(flowTask.getHttpSessionId(), message);
    }
}
