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

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogOutTypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;

public class FlowScriptStrategy implements ProcessStrategy {

	private static Logger logger = LoggerFactory.getLogger(FlowScriptStrategy.class);
	
	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowScript flowScript = (FlowScript)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowScript.getScriptId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		
		FlowScript flowScript = (FlowScript)mcgProduct;
		JSON parentParam = DataConverter.getParentRunResult(flowScript.getScriptId(), executeStruct);
		flowScript = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct) ,flowScript);		
		RunResult runResult = new RunResult();
		
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();
        flowBody.setFlowId(flowScript.getFlowId());
        flowBody.setSubFlag(executeStruct.getSubFlag());
        flowBody.setOrderNum(flowScript.getOrderNum());
        flowBody.setLogOutType(LogOutTypeEnum.PARAM.getValue());
        flowBody.setEleType(EletypeEnum.SCRIPT.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.SCRIPT.getName() + "--》" + flowScript.getScriptProperty().getScriptName());
        flowBody.setEleId(flowScript.getScriptId());
        flowBody.setComment("参数");
        if(parentParam == null) {
        	flowBody.setContent("{}");
        } else {
        	flowBody.setContent(JSON.toJSONString(parentParam, true));
        }
        flowBody.setLogType(LogTypeEnum.INFO.getValue());
        flowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
        message.setBody(flowBody);
        MessagePlugin.push(flowScript.getMcgWebScoketCode(), executeStruct.getSession().getId(), message); 		
		
		String dataJson = resolve(executeStruct.getMcgWebScoketCode(), executeStruct.getSession().getId(), flowScript.getFlowId(), flowScript.getScriptCore().getSource(), parentParam);
		runResult.setElementId(flowScript.getScriptId());
		
		JSONObject runResultJson = (JSONObject)parentParam;
		runResultJson.put(flowScript.getScriptProperty().getKey(), JSON.parse(dataJson));
		runResult.setJsonVar(JSON.toJSONString(runResultJson, true));
		executeStruct.getRunStatus().setCode("success");
		
		logger.debug("js脚本控件：{}，执行完毕！执行状态：{}", JSON.toJSONString(flowScript), JSON.toJSONString(executeStruct.getRunStatus()));
		return runResult;
	}
	
	public String resolve(String mcgWebScoketCode, String httpSessionId, String flowId, String script, JSON param) throws ScriptException, NoSuchMethodException {
		if(StringUtils.isNotEmpty(script)) {
			script = script.replace("console.info(", "console.info(\"" + mcgWebScoketCode + "\", \"" + httpSessionId + "\", \"" + flowId + "\", ")
					.replace("console.success(", "console.success(\"" + mcgWebScoketCode + "\", \"" + httpSessionId + "\", \"" + flowId + "\", ")
					.replace("console.error(", "console.error(\"" + mcgWebScoketCode + "\", \"" + httpSessionId + "\", \"" + flowId + "\", ");
		}
		String dataJson = null;
	    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();  
	    ScriptEngine engine = scriptEngineManager.getEngineByName("nashorn");
		engine.eval(script);
	    Invocable invocable = (Invocable) engine;
	    Object result = invocable.invokeFunction("main", param);
	    
	    dataJson = JSON.toJSONString(result);
	    return dataJson;
	}
	
}
