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

import org.python.core.PyDictionary;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogOutTypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.python.FlowPython;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;
import com.mcg.util.JythonEnvironment;

public class FlowPythonStrategy implements ProcessStrategy {

	private static Logger logger = LoggerFactory.getLogger(FlowPythonStrategy.class);
	
	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowPython flowPython = (FlowPython)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowPython.getId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		
		FlowPython flowPython = (FlowPython)mcgProduct;
		JSON parentParam = DataConverter.getParentRunResult(flowPython.getId(), executeStruct);
		flowPython = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct), flowPython);		
		RunResult runResult = new RunResult();
		
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();
        flowBody.setFlowId(flowPython.getFlowId());
        flowBody.setSubFlag(executeStruct.getSubFlag());
        flowBody.setOrderNum(flowPython.getOrderNum());
        flowBody.setLogOutType(LogOutTypeEnum.PARAM.getValue());
        flowBody.setEleType(EletypeEnum.PYTHON.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.PYTHON.getName() + "--》" + flowPython.getPythonProperty().getName());
        flowBody.setEleId(flowPython.getId());
        flowBody.setComment("参数");
        if(parentParam == null) {
            flowBody.setContent("{}");
        } else {
            flowBody.setContent(JSON.toJSONString(parentParam, true));
        }
        flowBody.setLogType(LogTypeEnum.INFO.getValue());
        flowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
        message.setBody(flowBody);
        MessagePlugin.push(flowPython.getMcgWebScoketCode(), executeStruct.getSession().getId(), message); 		
		
		String dataJson = resolve(flowPython.getPythonCore().getSource(), parentParam);
		runResult.setElementId(flowPython.getId());
		
		JSONObject runResultJson = (JSONObject)parentParam;
		runResultJson.put(flowPython.getPythonProperty().getKey(), JSON.parse(dataJson));
		runResult.setJsonVar(JSON.toJSONString(runResultJson, true));
		
		executeStruct.getRunStatus().setCode("success");
		
		logger.debug("python控件：{}，执行完毕！执行状态：{}", JSON.toJSONString(flowPython), JSON.toJSONString(executeStruct.getRunStatus()));
		return runResult;
	}
	
	public String resolve(String source, JSON param) throws Exception {
		String dataJson = null;
        
		final PythonInterpreter interpreter = JythonEnvironment.getInstance().getPythonInterpreter();
	    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	    ScriptEngine engine = scriptEngineManager.getEngineByName("python");
		engine.eval(source);
	    Invocable invocable = (Invocable) engine;
	    PyDictionary pyDictionary = JSON.parseObject(JSON.toJSONString(param), PyDictionary.class);
	    Object result = invocable.invokeFunction("main", pyDictionary);
	    dataJson = JSON.toJSONString(result);
/*	    
		interpreter.exec(source);
		PyFunction function = (PyFunction) interpreter.get("main", PyFunction.class);
		PyObject pyobject = function.__call__(new PyString(JSON.toJSONString(param)));
		Object jot = (Object)pyobject.__tojava__(Object.class);
		dataJson = JSON.toJSONString(jot); 
        */
		interpreter.close();
	    return dataJson;
	}
	
}
