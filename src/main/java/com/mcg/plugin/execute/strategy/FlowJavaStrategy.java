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
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.generate.FlowTask;
import com.mcg.plugin.javacompiler.DynamicEngine;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;

public class FlowJavaStrategy implements ProcessStrategy {

	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowJava flowJava = (FlowJava)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowJava.getId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		
		FlowJava flowJava = (FlowJava)mcgProduct;
		JSON parentParam = DataConverter.getParentRunResult(flowJava.getId(), executeStruct);
		flowJava = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct), flowJava);		
		RunResult runResult = new RunResult();
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();
        flowBody.setEleType(EletypeEnum.JAVA.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.JAVA.getName() + "--》" + flowJava.getJavaProperty().getName());
        flowBody.setEleId(flowJava.getId());
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
		
		String dataJson = resolve(flowJava.getJavaCore().getSource(), parentParam);
		runResult.setElementId(flowJava.getId());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(flowJava.getJavaProperty().getKey(), JSON.parse(dataJson));
		runResult.setJsonVar(JSON.toJSONString(map, true));
		
		executeStruct.getRunStatus().setCode("success");
		return runResult;
	}
	
	public String resolve(String source, JSON param) throws Exception {
		String dataJson = null;
        DynamicEngine de = DynamicEngine.getInstance();
        Object instance =  de.execute("Controller", source, "execute",  new Class[]{JSON.class}, new Object[]{param});
        dataJson = JSON.toJSONString(instance);
	    return dataJson;
	}
	
}
