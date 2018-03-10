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

import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.data.FlowData;
import com.mcg.entity.flow.end.FlowEnd;
import com.mcg.entity.flow.gmybatis.FlowGmybatis;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.json.FlowJson;
import com.mcg.entity.flow.model.FlowModel;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.flow.start.FlowStart;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.generate.FlowTask;
import com.mcg.plugin.websocket.MessagePlugin;

public class ExceptionProcess {

	public static void execute(McgProduct mcgProduct, String exceptionMsg) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();		
		if(mcgProduct instanceof FlowStart) {
			FlowStart flowStart = (FlowStart)mcgProduct;
            flowBody.setEleType(EletypeEnum.START.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.START.getName());
            flowBody.setEleId(flowStart.getStartId());
            			
		} else if(mcgProduct instanceof FlowModel) {
            FlowModel flowModel =(FlowModel)mcgProduct;
            flowBody.setEleType(EletypeEnum.MODEL.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.MODEL.getName() + "--》" + flowModel.getModelProperty().getModelName());
            flowBody.setEleId(flowModel.getModelId());
		} else if(mcgProduct instanceof FlowJson) {
			FlowJson flowJson =(FlowJson)mcgProduct;
            flowBody.setEleType(EletypeEnum.JSON.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.JSON.getName() + "--》" + flowJson.getJsonProperty().getName());
            flowBody.setEleId(flowJson.getId());
		} else if(mcgProduct instanceof FlowData) {
			FlowData flowData =(FlowData)mcgProduct;
            flowBody.setEleType(EletypeEnum.DATA.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.DATA.getName() + "--》" + flowData.getDataProperty().getName());
            flowBody.setEleId(flowData.getId());
		} else if(mcgProduct instanceof FlowGmybatis) {
            FlowGmybatis flowGmybatis = (FlowGmybatis)mcgProduct;
            flowBody.setEleType(EletypeEnum.GMYBATIS.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.GMYBATIS.getName());
            flowBody.setEleId(flowGmybatis.getGmybatisId());			
		} else if(mcgProduct instanceof FlowScript) {
            FlowScript flowScript = (FlowScript)mcgProduct;
            flowBody.setEleType(EletypeEnum.SCRIPT.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.SCRIPT.getName() + "--》" + flowScript.getScriptProperty().getScriptName());
            flowBody.setEleId(flowScript.getScriptId());			
		} else if(mcgProduct instanceof FlowJava) {
			FlowJava flowJava =(FlowJava)mcgProduct;
            flowBody.setEleType(EletypeEnum.JAVA.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.JAVA.getName() + "--》" + flowJava.getJavaProperty().getName());
            flowBody.setEleId(flowJava.getId());
		} else if(mcgProduct instanceof FlowText) {
            FlowText flowText = (FlowText)mcgProduct;
            flowBody.setEleType(EletypeEnum.TEXT.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.TEXT.getName() + "--》" + flowText.getTextProperty().getName());
            flowBody.setEleId(flowText.getTextId());			
		} else if(mcgProduct instanceof FlowEnd) {
            FlowEnd flowEnd = (FlowEnd)mcgProduct;
            flowBody.setEleType(EletypeEnum.END.getValue());
            flowBody.setEleTypeDesc(EletypeEnum.END.getName());
            flowBody.setEleId(flowEnd.getEndId());			
		}
		
		flowBody.setComment("发生异常");
		flowBody.setContent(exceptionMsg);
        flowBody.setLogType(LogTypeEnum.ERROR.getValue());
        flowBody.setLogTypeDesc(LogTypeEnum.ERROR.getName());
        message.setBody(flowBody);
        FlowTask flowTask = FlowTask.executeLocal.get();
        MessagePlugin.push(flowTask.getHttpSessionId(), message);
        
        flowTask.getExecuteStruct().getRunStatus().setCode("exception");
	}
}
