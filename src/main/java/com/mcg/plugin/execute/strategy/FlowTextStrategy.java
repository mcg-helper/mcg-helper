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

package com.mcg.plugin.execute.strategy;

import java.io.File;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.FlowTextOutModeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.tplengine.FreeMakerTpLan;
import com.mcg.plugin.tplengine.TplEngine;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;

public class FlowTextStrategy implements ProcessStrategy {

	private static Logger logger = LoggerFactory.getLogger(FlowTextStrategy.class);
	
	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowText flowText = (FlowText)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowText.getTextId());
	}
	
	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowText flowText = (FlowText)mcgProduct;
		RunResult result = new RunResult();
		result.setElementId(flowText.getTextId());
		
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();
        flowBody.setFlowId(flowText.getFlowId());
        flowBody.setSubFlag(executeStruct.getSubFlag());
        flowBody.setOrderNum(flowText.getOrderNum());
        flowBody.setEleType(EletypeEnum.TEXT.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.TEXT.getName() + "--》" + flowText.getTextProperty().getName());
        flowBody.setEleId(flowText.getTextId());
        flowBody.setComment("参数");
        JSON parentParam = DataConverter.getParentRunResult(flowText.getTextId(), executeStruct); 
        JSON allParam = DataConverter.addFlowStartRunResult(parentParam, executeStruct);
        flowText = DataConverter.flowOjbectRepalceGlobal(allParam, flowText); 
        if(parentParam == null ) {
        	flowBody.setContent("{}");
        } else {
        	flowBody.setContent(JSON.toJSONString(parentParam, true));
        }
        flowBody.setLogType(LogTypeEnum.INFO.getValue());
        flowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
        message.setBody(flowBody);
        MessagePlugin.push(executeStruct.getSession().getId(), message);
		
		TplEngine tplEngine = new TplEngine(new FreeMakerTpLan());
		String outPath = flowText.getTextProperty().getOutPutPath();
		if(!outPath.endsWith("\\") || !outPath.endsWith("/")) {
			outPath += File.separator;
		}
		String text = tplEngine.generate(allParam, flowText.getTextCore().getSource(), 
				flowText.getTextProperty().getFileName(), outPath, flowText.getTextProperty().getOutMode());
		
        Message contentMessage = MessagePlugin.getMessage();
        contentMessage.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody contentFlowBody = new FlowBody();
        contentFlowBody.setFlowId(flowText.getFlowId());
        contentFlowBody.setSubFlag(executeStruct.getSubFlag());
        contentFlowBody.setEleType(EletypeEnum.TEXT.getValue());
        contentFlowBody.setEleTypeDesc(EletypeEnum.TEXT.getName() + "--》" + flowText.getTextProperty().getName());
        contentFlowBody.setEleId(flowText.getTextId());
        if(FlowTextOutModeEnum.OVERRIDE.getValue().equals(flowText.getTextProperty().getOutMode())) {
        	contentFlowBody.setComment("创建文件内容");
        } else if(FlowTextOutModeEnum.APPEND.getValue().equals(flowText.getTextProperty().getOutMode())) {
        	contentFlowBody.setComment("追加文件内容");
        }
        contentFlowBody.setContent(text);
        contentFlowBody.setLogType(LogTypeEnum.INFO.getValue());
        contentFlowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
        contentMessage.setBody(contentFlowBody);
        MessagePlugin.push(executeStruct.getSession().getId(), contentMessage);

        JSONObject runResultJson = (JSONObject)parentParam;
		result.setJsonVar(JSON.toJSONString(runResultJson, true));
		
		executeStruct.getRunStatus().getAvailableFileMap().put(flowText.getTextId(), (outPath + flowText.getTextProperty().getFileName()));
		executeStruct.getRunStatus().setCode("success");
		
		logger.debug("文本控件：{}，执行完毕！执行状态：{}", JSON.toJSONString(flowText), JSON.toJSONString(executeStruct.getRunStatus()));
		return result;
	}
	
}