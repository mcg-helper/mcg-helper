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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.Constants;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.FlowLinuxConnModeEnum;
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
import com.mcg.plugin.tplengine.FreeMakerTpLan;
import com.mcg.plugin.tplengine.TplEngine;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;
import com.mcg.util.LevelDbUtil;
import com.mcg.util.SSHShellUtil;

public class FlowLinuxStrategy implements ProcessStrategy {

	private static Logger logger = LoggerFactory.getLogger(FlowLinuxStrategy.class);
	
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
		flowLinux = DataConverter.flowOjbectRepalceGlobal(allParam, flowLinux);
		RunResult runResult = new RunResult();
       
		Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();
        flowBody.setSubFlag(executeStruct.getSubFlag());
        flowBody.setFlowId(flowLinux.getFlowId());
        flowBody.setOrderNum(flowLinux.getOrderNum());
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
        MessagePlugin.push(executeStruct.getSession().getId(), message);
        
        ServerSource serverSource = null;
        if(FlowLinuxConnModeEnum.DEPENDENCY.getValue().equals(flowLinux.getLinuxCore().getConnMode())) {
            McgGlobal mcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
            
            if(mcgGlobal != null && CollectionUtils.isNotEmpty(mcgGlobal.getServerSources())) {
            	for(ServerSource source : mcgGlobal.getServerSources()) {
            		if(flowLinux.getLinuxCore().getServerSourceId().equals(source.getId())) {
            			serverSource = source;
            			break;
            		}
            	}
            }
        } else if(FlowLinuxConnModeEnum.ASSIGN.getValue().equals(flowLinux.getLinuxCore().getConnMode())) {
        	serverSource = new ServerSource();
        	TplEngine tplEngine = new TplEngine(new FreeMakerTpLan());
        	String ip = tplEngine.generate(allParam, flowLinux.getLinuxCore().getIp());
        	serverSource.setIp(ip);
        	String port = tplEngine.generate(allParam, flowLinux.getLinuxCore().getPort());
        	serverSource.setPort(Integer.valueOf(port));
        	String user = tplEngine.generate(allParam, flowLinux.getLinuxCore().getUser());
        	serverSource.setUserName(user);
        	String pwd = tplEngine.generate(allParam, flowLinux.getLinuxCore().getPwd());
        	serverSource.setPwd(pwd);
        }
        

        
        TplEngine tplEngine = new TplEngine(new FreeMakerTpLan());
        String command = tplEngine.generate(allParam, flowLinux.getLinuxCore().getSource());
        
		String text = resolve(command, serverSource);
		
        Message shellMessage = MessagePlugin.getMessage();
        shellMessage.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody shellFlowBody = new FlowBody();
        shellFlowBody.setEleType(EletypeEnum.LINUX.getValue());
        shellFlowBody.setEleTypeDesc(EletypeEnum.LINUX.getName() + "--》" + flowLinux.getLinuxProperty().getName());
        shellFlowBody.setEleId(flowLinux.getId());
        shellFlowBody.setComment("shell执行");
        shellFlowBody.setContent(text);
        shellFlowBody.setLogType(LogTypeEnum.INFO.getValue());
        shellFlowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
        shellMessage.setBody(shellFlowBody);
        MessagePlugin.push(executeStruct.getSession().getId(), shellMessage);
		
		runResult.setElementId(flowLinux.getId());
		
		JSONObject runResultJson = (JSONObject)parentParam;
		runResultJson.put(flowLinux.getLinuxProperty().getKey(), null);
		runResult.setJsonVar(JSON.toJSONString(runResultJson, true));
		
		executeStruct.getRunStatus().setCode("success");
		
		logger.debug("Linux控件：{}，执行完毕！执行状态：{}", JSON.toJSONString(flowLinux), JSON.toJSONString(executeStruct.getRunStatus()));
		return runResult;
	}
	
	public String resolve(String command, ServerSource serverSource) throws Exception {
        
		String log = SSHShellUtil.execute(serverSource.getIp(), serverSource.getPort(), 
											serverSource.getUserName(),  serverSource.getPwd(), serverSource.getSecretKey(), command);
	    return log;
	}
	
}
