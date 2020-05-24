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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.SpringContextHelper;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.process.FlowProcess;
import com.mcg.entity.flow.web.WebStruct;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.generate.RunStatus;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.service.FlowService;
import com.mcg.util.DataConverter;

public class FlowProcessStrategy implements ProcessStrategy {

	private static Logger logger = LoggerFactory.getLogger(FlowProcessStrategy.class);
	
	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowProcess flowProcess = (FlowProcess)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowProcess.getId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		
		FlowProcess flowProcess = (FlowProcess)mcgProduct;
//		JSON parentParam = DataConverter.getParentRunResultByValue(flowProcess.getId(), executeStruct);
		JSON parentParam = DataConverter.getParentRunResult(flowProcess.getId(), executeStruct);
		
		flowProcess = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct), flowProcess);		
		RunResult runResult = new RunResult();
		
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();
        flowBody.setFlowId(flowProcess.getFlowId());
        flowBody.setSubFlag(executeStruct.getSubFlag());
        flowBody.setOrderNum(flowProcess.getOrderNum());
        flowBody.setEleType(EletypeEnum.PROCESS.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.PROCESS.getName() + "--》" + flowProcess.getProcessProperty().getName());
        flowBody.setEleId(flowProcess.getId());
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
        
        FlowService flowService = SpringContextHelper.getSpringBean(FlowService.class);
        WebStruct webStruct = new WebStruct();

        if (!StringUtils.isEmpty(flowProcess.getProcessProperty().getFlowId())) {
        	webStruct.setFlowId(flowProcess.getProcessProperty().getFlowId());
        	logger.debug("开始执行子流程，数据：{}", JSON.toJSONString(flowProcess));
        	RunStatus flowRunStatus = flowService.generate(webStruct, executeStruct.getSession(), true, executeStruct.getTopology().getId(), parentParam);
        	
        	executeStruct.getRunStatus().setCode(flowRunStatus.getCode());
    		Map<String, String> runStatusMap = new HashMap<String, String>();
    		runStatusMap.put("code", flowRunStatus.getCode());
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put(flowProcess.getProcessProperty().getKey(), runStatusMap);
    		runResult.setJsonVar(JSON.toJSONString(map, true));
		} else {
			executeStruct.getRunStatus().setCode("exception");
		}
		
		logger.debug("子流程控件：{}，执行完毕！执行状态：{}", JSON.toJSONString(flowProcess), JSON.toJSONString(executeStruct.getRunStatus()));
		runResult = new RunResult();
		
		runResult.setElementId(flowProcess.getId());
		JSONObject runResultJson = (JSONObject)parentParam;
		runResult.setJsonVar(JSON.toJSONString(runResultJson, true));
		
		executeStruct.getRunStatus().setCode("success");
		return runResult;
	}
	
}
