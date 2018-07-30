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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.generate.FlowTask;
import com.mcg.plugin.tplengine.FreeMakerTpLan;
import com.mcg.plugin.tplengine.TplEngine;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;

public class FlowTextStrategy implements ProcessStrategy {

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
        FlowTask flowTask = FlowTask.executeLocal.get();
        MessagePlugin.push(flowTask.getHttpSessionId(), message);
		
		TplEngine tplEngine = new TplEngine(new FreeMakerTpLan());
		String outPath = flowText.getTextProperty().getOutPutPath();
		if(!outPath.endsWith("\\") || !outPath.endsWith("/")) {
			outPath += File.separator;
		}
		String text = tplEngine.generate(allParam, flowText.getTextCore().getSource(), flowText.getTextProperty().getFileName(), outPath);
		
		result.setSourceCode(text);
		JSONObject runResultJson = new JSONObject();
		runResultJson.put(flowText.getTextProperty().getKey(), text);
		result.setJsonVar(JSON.toJSONString(runResultJson, true));
		executeStruct.getRunStatus().setCode("success");
		return result;
	}
	
}