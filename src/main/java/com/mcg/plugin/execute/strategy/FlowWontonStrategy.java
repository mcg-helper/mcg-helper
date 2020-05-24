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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;
import com.mcg.common.Constants;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.wonton.FlowWonton;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.global.wonton.WontonData;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.entity.wonton.WontonHeart;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.tplengine.FreeMakerTpLan;
import com.mcg.plugin.tplengine.TplEngine;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;
import com.mcg.util.LevelDbUtil;

public class FlowWontonStrategy implements ProcessStrategy {

	private static Logger logger = LoggerFactory.getLogger(FlowWontonStrategy.class);
	
	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowWonton flowWonton = (FlowWonton)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowWonton.getId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		
		FlowWonton flowWonton = (FlowWonton)mcgProduct;
		JSON parentParam = DataConverter.getParentRunResult(flowWonton.getId(), executeStruct);
		JSON allParam = DataConverter.addFlowStartRunResult(parentParam, executeStruct);
		flowWonton = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct), flowWonton);		
		RunResult runResult = new RunResult();
		
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();
        flowBody.setFlowId(flowWonton.getFlowId());
        flowBody.setSubFlag(executeStruct.getSubFlag());
        flowBody.setOrderNum(flowWonton.getOrderNum());
        flowBody.setEleType(EletypeEnum.WONTON.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.WONTON.getName() + "--》" + flowWonton.getWontonProperty().getName());
        flowBody.setEleId(flowWonton.getId());
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
        
        WontonData wontonData = (WontonData)LevelDbUtil.getObject(Constants.WONTON_KEY, WontonData.class);
        if(wontonData != null && wontonData.getWontonHeartMap() != null) {
        	runResult.setElementId(flowWonton.getId());
        	WontonHeart wontonHeart = wontonData.getWontonHeartMap().get(flowWonton.getWontonProperty().getInstancecode());
            try {
                boolean flag = false;
                StringBuilder sucessBuilder = new StringBuilder();
            	StringBuilder faultBuilder = new StringBuilder();
            	
            	String resetUrl = "http://" + wontonHeart.getInstancecode() + "/muxy/networkshape/_reset";
            	String resourcesUrl = "http://" + wontonHeart.getInstancecode() + "/stressng/stressor";
                TplEngine tplEngine = new TplEngine(new FreeMakerTpLan());
                
            	if(flowWonton.getWontonNetRule() != null && flowWonton.getWontonNetRule() != null && flowWonton.getWontonNetRule().isSwitchState()) {
            		HttpRequest httpRequest = HttpRequest.put(resetUrl);
            		
            		String command = tplEngine.generate(allParam, JSON.toJSONString(flowWonton.getWontonNetRule()));
            		String result = httpRequest.send(command.getBytes()).connectTimeout(Constants.REQUEST_WONTON_TIME_OUT).body();
            		if(result.endsWith("successfully.")) {
    	        		flag = true;
    	        		sucessBuilder.append("网络规则，");
            		} else {
            			flag = false;
            			faultBuilder.append("网络规则，");
            		}
            	}
            	
            	if(flowWonton.getWontonCpuRule() != null && flowWonton.getWontonCpuRule() != null && flowWonton.getWontonCpuRule().isSwitchState()) {
            		HttpRequest httpRequest = HttpRequest.post(resourcesUrl);
            		String command = tplEngine.generate(allParam, JSON.toJSONString(flowWonton.getWontonCpuRule()));
            		String result = httpRequest.send(command.getBytes()).connectTimeout(Constants.REQUEST_WONTON_TIME_OUT).body();
            		if(200 == JSON.parseObject(result).getIntValue("State")) {
            			flag = true;
            			sucessBuilder.append("cpu规则，");
            		} else {
            			flag = false;
            			faultBuilder.append("cpu规则，");
            		}
            	}
            	if(flowWonton.getWontonMemRule() != null && flowWonton.getWontonMemRule() != null && flowWonton.getWontonMemRule().isSwitchState()) {
            		HttpRequest httpRequest = HttpRequest.post(resourcesUrl);
            		String command = tplEngine.generate(allParam, JSON.toJSONString(flowWonton.getWontonMemRule()));
            		String result = httpRequest.send(command.getBytes()).connectTimeout(Constants.REQUEST_WONTON_TIME_OUT).body();
            		if(200 == JSON.parseObject(result).getIntValue("State")) {
            			flag = true;
            			sucessBuilder.append("内存规则，");
            		} else {
            			flag = false;
            			faultBuilder.append("内存规则，");
            		}
            	}
            	if(flowWonton.getWontonIoRule() != null && flowWonton.getWontonIoRule() != null && flowWonton.getWontonIoRule().isSwitchState()) {
            		HttpRequest httpRequest = HttpRequest.post(resourcesUrl);
            		String command = tplEngine.generate(allParam, JSON.toJSONString(flowWonton.getWontonIoRule()));
            		String result = httpRequest.send(command.getBytes()).connectTimeout(Constants.REQUEST_WONTON_TIME_OUT).body();
            		if(200 == JSON.parseObject(result).getIntValue("State")) {
            			flag = true;
            			sucessBuilder.append("io规则，");
            		} else {
            			flag = false;
            			faultBuilder.append("io规则，");
            		}
            	}
            	
            	if(StringUtils.isEmpty(sucessBuilder.toString()) && StringUtils.isEmpty(faultBuilder.toString())) {
            		JSONObject runResultJson = new JSONObject();
            		runResultJson.put(flowWonton.getWontonProperty().getKey(), "没有规则可发布！");
            		runResult.setJsonVar(JSON.toJSONString(runResultJson, true));
            	} else {
            		JSONObject runResultJson = new JSONObject();
        	    	if(flag) {
        	    		sucessBuilder.append("发布成功！");
        	    		runResult.setSourceCode(sucessBuilder.toString());
        	    		runResultJson.put(flowWonton.getWontonProperty().getKey(), sucessBuilder.toString());
        	    	} else {
        	    		faultBuilder.append("发布失败！");
        	    		runResult.setSourceCode(sucessBuilder.toString() + faultBuilder.toString());
        	    		runResultJson.put(flowWonton.getWontonProperty().getKey(), sucessBuilder.toString() + faultBuilder.toString());
        	    	}
        	    	runResult.setJsonVar(JSON.toJSONString(runResultJson, true));
        	    	
            	}
            } catch (Exception e) {
            	String reason = "请求混沌客户端" + wontonHeart.getInstancecode() + "异常，规则发布失败！";
        		runResult.setJsonVar(reason);
        		logger.error(reason + "异常信息：{}", e.getMessage());
    		}
        }
		
        JSONObject runResultJson = (JSONObject)parentParam;
		runResult.setJsonVar(JSON.toJSONString(runResultJson, true));
		executeStruct.getRunStatus().setCode("success");
		
		logger.debug("混沌控件：{}，执行完毕！执行状态：{}", JSON.toJSONString(flowWonton), JSON.toJSONString(executeStruct.getRunStatus()));
		return runResult;
	}
	
	
}
