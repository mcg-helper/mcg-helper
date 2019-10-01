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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.FlowLoopTypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.common.LoopStatus;
import com.mcg.entity.flow.loop.FlowLoop;
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

public class FlowLoopStrategy implements ProcessStrategy {

	private static Logger logger = LoggerFactory.getLogger(FlowLoopStrategy.class);
	
	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowLoop flowLoop = (FlowLoop)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowLoop.getId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		RunResult runResult = new RunResult();
		FlowLoop flowLoop = (FlowLoop)mcgProduct;
	
		JSON parentParam = DataConverter.getParentRunResult(flowLoop.getId(), executeStruct);
		flowLoop = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct), flowLoop);		
		
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();
        flowBody.setFlowId(flowLoop.getFlowId());
        flowBody.setSubFlag(executeStruct.getSubFlag());
        flowBody.setOrderNum(flowLoop.getOrderNum());
        flowBody.setEleType(EletypeEnum.LOOP.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.LOOP.getName() + "--》" + flowLoop.getLoopProperty().getName());
        flowBody.setEleId(flowLoop.getId());
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
        
        JSONObject jsonObject = (JSONObject)parentParam;
        LoopStatus loopStatusLast = executeStruct.getRunStatus().getLoopStatusMap().get(flowLoop.getId());
        
        if(FlowLoopTypeEnum.COUNT.getValue().equals(flowLoop.getLoopProperty().getType()) ) {
	        if(loopStatusLast == null) {
	        	int count = Integer.valueOf(flowLoop.getLoopProperty().getValue());
	        	LoopStatus loopStatus = new LoopStatus();
	        	loopStatus.setSwicth(--count <= 0 ? false : true);
	        	loopStatus.setCount(count);
	        	executeStruct.getRunStatus().getLoopStatusMap().put(flowLoop.getId(), loopStatus);
	        	
	        	jsonObject.put("count", count + 1);
	        } else {
	        	LoopStatus loopStatus = executeStruct.getRunStatus().getLoopStatusMap().get(flowLoop.getId());
	        	loopStatus.setCount(loopStatus.getCount() - 1); 
	        	loopStatus.setSwicth(loopStatus.getCount() <= 0 ? false : true);
	        	executeStruct.getRunStatus().getLoopStatusMap().put(flowLoop.getId(), loopStatus);
	        	
	        	jsonObject.put("count", loopStatus.getCount() + 1);
	        }
        } else if(FlowLoopTypeEnum.EXPRES.getValue().equals(flowLoop.getLoopProperty().getType()) ) {
        	jsonObject.put("expres", flowLoop.getLoopProperty().getValue());
        	TplEngine tplEngine = new TplEngine(new FreeMakerTpLan());
        	String flag = tplEngine.generate(null, "${(" + flowLoop.getLoopProperty().getValue() + ")?c}");
        	LoopStatus loopStatus = new LoopStatus();
        	loopStatus.setSwicth(Boolean.valueOf(flag));
        	executeStruct.getRunStatus().getLoopStatusMap().put(flowLoop.getId(), loopStatus);
        	
        } else {
        	logger.error("循环规则非法！");
        }
        
        runResult.setElementId(flowLoop.getId());
        
		JSONObject runResultJson = new JSONObject();
		runResultJson.put(flowLoop.getLoopProperty().getKey(), jsonObject);
        runResult.setJsonVar(JSON.toJSONString(runResultJson));
        
        executeStruct.getRunStatus().setCode("success");
		
        logger.debug("循环控件：{}，执行完毕！执行状态：{}", JSON.toJSONString(flowLoop), JSON.toJSONString(executeStruct.getRunStatus()));
        
		return runResult;
	}
	
	
}
