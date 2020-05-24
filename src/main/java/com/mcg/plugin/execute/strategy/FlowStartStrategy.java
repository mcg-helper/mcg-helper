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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.SpringContextHelper;
import com.mcg.entity.flow.start.FlowStart;
import com.mcg.entity.flow.start.Var;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.global.var.FlowVar;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.service.GlobalService;

public class FlowStartStrategy implements ProcessStrategy {

	private static Logger logger = LoggerFactory.getLogger(FlowStartStrategy.class);
	
	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowStart flowStart = (FlowStart)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowStart.getStartId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {

		FlowStart flowStart = (FlowStart)mcgProduct;
        
        RunResult result = new RunResult();
        result.setElementId(flowStart.getStartId());
        Map<String, String> dataMap = new HashMap<String, String>();
        
		GlobalService globalService = SpringContextHelper.getSpringBean(GlobalService.class);
		List<FlowVar> sysVarList = globalService.getMcgGlobal().getSysVars();
		if(sysVarList != null && sysVarList.size() > 0) {
	        for(FlowVar sysVar : sysVarList) {
	            dataMap.put(sysVar.getKey(), sysVar.getValue());
	        }
		}
		
		List<FlowVar> flowVarList = globalService.getMcgGlobal().getFlowVars();
		if(flowVarList != null && flowVarList.size() > 0) {
	        for(FlowVar flowVar : flowVarList) {
	            dataMap.put(flowVar.getKey(), flowVar.getValue());
	        }
		}
		
        List<Var> varList = flowStart.getStartProperty().getVar();
        for(Var var : varList) {
            dataMap.put(var.getKey(), var.getValue());
        }
        
        JSONObject param = new JSONObject();
        
        if(executeStruct.getSubFlag()) {
        	if(executeStruct.getParentParam() instanceof JSONObject) {
        		param = (JSONObject)executeStruct.getParentParam();

        		for(String key : dataMap.keySet()){
        		    if(param.get(key) == null) {
        		    	param.put(key, dataMap.get(key));
        		    }
        		}
        	} else {
        		logger.error("父级流程传入参数有误，父级流程参数：{}", executeStruct.getParentParam().toJSONString());
        		throw new Exception("当前子流程中父级流程传入参数有误，父级流程参数：" + executeStruct.getParentParam().toJSONString());
        	}
        	
        	result.setJsonVar(JSON.toJSONString(param, true));
        } else {
        	result.setJsonVar(JSON.toJSONString(dataMap, true));
        }
        
        executeStruct.getRunStatus().setCode("success");
        
        logger.debug("开始控件：{}，执行完毕！执行状态：{}", JSON.toJSONString(flowStart), JSON.toJSONString(executeStruct.getRunStatus()));
		return result;
	}
	
}