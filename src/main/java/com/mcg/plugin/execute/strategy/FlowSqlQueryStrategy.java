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
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.sqlquery.FlowSqlQuery;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.dbconn.FlowDataAdapterImpl;
import com.mcg.plugin.dbconn.McgBizAdapter;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.generate.FlowTask;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.service.FlowService;
import com.mcg.service.impl.FlowServiceImpl;
import com.mcg.util.DataConverter;

public class FlowSqlQueryStrategy implements ProcessStrategy {

	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
	    FlowSqlQuery flowSqlQuery = (FlowSqlQuery)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowSqlQuery.getId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		
	    FlowSqlQuery flowSqlQuery = (FlowSqlQuery)mcgProduct;
		JSON parentParam = DataConverter.getParentRunResult(flowSqlQuery.getId(), executeStruct);
		
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);		
        FlowBody flowBody = new FlowBody();
        flowBody.setEleType(EletypeEnum.SQLQUERY.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.SQLQUERY.getName() + "--》" + flowSqlQuery.getSqlQueryProperty().getName());
        flowBody.setEleId(flowSqlQuery.getId());
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
		
        flowSqlQuery = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct), flowSqlQuery);		
		RunResult runResult = new RunResult();
		runResult.setElementId(flowSqlQuery.getId());
		
        FlowService flowService = new FlowServiceImpl();
        McgBizAdapter mcgBizAdapter = new FlowDataAdapterImpl(flowService.getMcgDataSourceById(flowSqlQuery.getSqlQueryCore().getDataSourceId()));
        
        List<Map<String, Object>> result = null;
        result = mcgBizAdapter.tableQuery(flowSqlQuery.getSqlQueryCore().getSource(), null);
     
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		map.put(flowSqlQuery.getSqlQueryProperty().getKey(), result);
		runResult.setJsonVar(JSON.toJSONString(map, SerializerFeature.WriteDateUseDateFormat));
		executeStruct.getRunStatus().setCode("success");
		return runResult;
	}
	
}