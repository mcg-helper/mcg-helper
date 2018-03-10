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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.model.FlowModel;
import com.mcg.entity.flow.model.ModelRecord;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.generate.FlowTask;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;

public class FlowModelStrategy implements ProcessStrategy {

	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowModel flowModel = (FlowModel)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowModel.getModelId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowModel flowModel = (FlowModel)mcgProduct;
		JSON parentParam = DataConverter.getParentRunResult(flowModel.getModelId(), executeStruct);
		
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);		
        FlowBody flowBody = new FlowBody();
        flowBody.setEleType(EletypeEnum.MODEL.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.MODEL.getName() + "--》" + flowModel.getModelProperty().getModelName());
        flowBody.setEleId(flowModel.getModelId());
        flowBody.setComment("参数");
        if(parentParam == null) {
        	flowBody.setContent("{}");
        } else {
        	flowBody.setContent(JSON.toJSONString(parentParam, true));
        }
        flowBody.setLogType(LogTypeEnum.INFO.getValue());
        flowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
        message.setBody(flowBody);
        FlowTask flowTask = FlowTask.executeLocal.get();    
        MessagePlugin.push(flowTask.getHttpSessionId(), message);		
		
		flowModel = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct), flowModel);		
        RunResult result = new RunResult();
        result.setElementId(flowModel.getModelId());
        
        List<ModelRecord> modelRecordList = flowModel.getModelField().getModelRecord();
        Map<String, HashMap<String, Object>> value = new HashMap<String, HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("property", flowModel.getModelProperty());
        map.put("record", modelRecordList);
        value.put(flowModel.getModelProperty().getKey(), map);
        
        result.setJsonVar(JSON.toJSONString(value, true));
        executeStruct.getRunStatus().setCode("success");
		return result;
	}
	
}