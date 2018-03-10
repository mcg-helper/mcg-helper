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

import com.alibaba.fastjson.JSON;
import com.mcg.entity.flow.start.FlowStart;
import com.mcg.entity.flow.start.Var;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;

public class FlowStartStrategy implements ProcessStrategy {

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
        List<Var> varList = flowStart.getStartProperty().getVar();
        for(Var var : varList) {
            dataMap.put(var.getKey(), var.getValue());
        }
        result.setJsonVar(JSON.toJSONString(dataMap, true));		
        executeStruct.getRunStatus().setCode("success");
		return result;
	}
	
}