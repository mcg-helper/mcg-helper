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

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.Constants;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.linux.FlowLinux;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.global.McgGlobal;
import com.mcg.entity.global.serversource.ServerSource;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.generate.FlowTask;
import com.mcg.plugin.tplengine.FreeMakerTpLan;
import com.mcg.plugin.tplengine.TplEngine;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;
import com.mcg.util.LevelDbUtil;
import com.mcg.util.SSHLinux;

public class FlowLinuxStrategy implements ProcessStrategy {

	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowLinux flowLinux = (FlowLinux)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowLinux.getId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		
		FlowLinux flowLinux = (FlowLinux)mcgProduct;
		JSON parentParam = DataConverter.getParentRunResult(flowLinux.getId(), executeStruct);
		JSON allParam = DataConverter.addFlowStartRunResult(parentParam, executeStruct);
		flowLinux = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct), flowLinux);		
		RunResult runResult = new RunResult();
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();
        flowBody.setEleType(EletypeEnum.LINUX.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.LINUX.getName() + "--》" + flowLinux.getLinuxProperty().getName());
        flowBody.setEleId(flowLinux.getId());
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
        
        McgGlobal mcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
        ServerSource serverSource = null;
        
        if(mcgGlobal != null && CollectionUtils.isNotEmpty(mcgGlobal.getServerSources())) {
        	for(ServerSource source : mcgGlobal.getServerSources()) {
        		if(flowLinux.getLinuxCore().getServerSourceId().equals(source.getId())) {
        			serverSource = source;
        			break;
        		}
        	}
        }
        
        TplEngine tplEngine = new TplEngine(new FreeMakerTpLan());
        String command = tplEngine.generate(allParam, flowLinux.getLinuxCore().getSource());
        
		String text = resolve(command, serverSource);
		runResult.setElementId(flowLinux.getId());
		
		runResult.setSourceCode(text);
		JSONObject runResultJson = new JSONObject();
		runResultJson.put(flowLinux.getLinuxProperty().getKey(), text);
		runResult.setJsonVar(JSON.toJSONString(runResultJson, true));
		
		executeStruct.getRunStatus().setCode("success");
		return runResult;
	}
	
	public String resolve(String command, ServerSource serverSource) throws Exception {
        
		String log =  SSHLinux.exeCommand(serverSource.getIp(), serverSource.getPort(), 
				serverSource.getUserName(), serverSource.getPwd(), command);
	    return log;
	}
	
}
