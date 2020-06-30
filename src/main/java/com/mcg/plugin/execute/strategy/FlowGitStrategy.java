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
import com.mcg.common.sysenum.FlowGitModeEnum;
import com.mcg.common.sysenum.LogOutTypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.git.FlowGit;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;
import com.mcg.util.JGitUtil;

public class FlowGitStrategy implements ProcessStrategy {

	private static Logger logger = LoggerFactory.getLogger(FlowGitStrategy.class);
	
	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct)
			throws Exception {
		FlowGit flowGit = (FlowGit)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowGit.getId());
		
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		RunResult runResult = new RunResult();
		FlowGit flowGit = (FlowGit)mcgProduct;
		
		JSON parentParam = DataConverter.getParentRunResult(flowGit.getId(), executeStruct);
		flowGit = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct), flowGit);
		
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();
        flowBody.setFlowId(flowGit.getFlowId());
        flowBody.setSubFlag(executeStruct.getSubFlag());
        flowBody.setOrderNum(flowGit.getOrderNum());
        flowBody.setLogOutType(LogOutTypeEnum.PARAM.getValue());
        flowBody.setEleType(EletypeEnum.GIT.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.GIT.getName() + "--》" + flowGit.getGitProperty().getName());
        flowBody.setEleId(flowGit.getId());
        flowBody.setComment("参数");
        if(parentParam == null) {
            flowBody.setContent("{}");
        } else {
            flowBody.setContent(JSON.toJSONString(parentParam, true));
        }
        flowBody.setLogType(LogTypeEnum.INFO.getValue());
        flowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
        message.setBody(flowBody);
        MessagePlugin.push(flowGit.getMcgWebScoketCode(), executeStruct.getSession().getId(), message);
		
        if(FlowGitModeEnum.HTTP.getValue().equals(flowGit.getGitProperty().getMode())) {
			File file = new File(flowGit.getGitProperty().getProjectPath() + "/.git");
			if(file.exists()) {
				JGitUtil.pull(flowGit.getGitProperty().getProjectPath(), flowGit.getGitProperty().getBranch(), flowGit.getGitProperty().getUserName(), flowGit.getGitProperty().getUserPwd());
			} else {
				JGitUtil.cloneRepository(flowGit.getGitProperty().getRemoteUrl(), flowGit.getGitProperty().getBranch(), flowGit.getGitProperty().getProjectPath(), flowGit.getGitProperty().getUserName(), flowGit.getGitProperty().getUserPwd());
			}
        }
        
        runResult.setElementId(flowGit.getId());
        
        JSONObject runResultJson = (JSONObject)parentParam;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("url", flowGit.getGitProperty().getRemoteUrl());
		jsonObject.put("branch", flowGit.getGitProperty().getBranch());
		jsonObject.put("path", flowGit.getGitProperty().getProjectPath());
		runResultJson.put(flowGit.getGitProperty().getKey(), jsonObject);
		
        runResult.setJsonVar(JSON.toJSONString(runResultJson, true));
        
        executeStruct.getRunStatus().setCode("success");
		
        logger.debug("GIT控件：{}，执行完毕！执行状态：{}", JSON.toJSONString(flowGit), JSON.toJSONString(executeStruct.getRunStatus()));
        
		return runResult;
	}

}
