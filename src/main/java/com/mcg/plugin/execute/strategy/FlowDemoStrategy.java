package com.mcg.plugin.execute.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogOutTypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.demo.FlowDemo;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;

public class FlowDemoStrategy implements ProcessStrategy {
	
	private static Logger logger = LoggerFactory.getLogger(FlowDemoStrategy.class);
	
	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct)
			throws Exception {
		FlowDemo flowDemo = (FlowDemo)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowDemo.getId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowDemo flowDemo = (FlowDemo)mcgProduct;
		JSON parentParam = DataConverter.getParentRunResult(flowDemo.getId(), executeStruct);
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);		
        FlowBody flowBody = new FlowBody();
        flowBody.setSubFlag(executeStruct.getSubFlag());
        flowBody.setFlowId(flowDemo.getFlowId());
        flowBody.setOrderNum(flowDemo.getOrderNum());
        flowBody.setLogOutType(LogOutTypeEnum.PARAM.getValue());
        flowBody.setEleType(EletypeEnum.JSON.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.JSON.getName() + "--》" + flowDemo.getDemoProperty().getName());
        flowBody.setEleId(flowDemo.getId());
        flowBody.setComment("参数");
        if(parentParam == null) {
        	flowBody.setContent("{}");
        } else {
        	flowBody.setContent(JSON.toJSONString(parentParam, true));
        }
        flowBody.setLogType(LogTypeEnum.INFO.getValue());
        flowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
        message.setBody(flowBody);
        MessagePlugin.push(flowDemo.getMcgWebScoketCode(), executeStruct.getSession().getId(), message);		
		
		flowDemo = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct), flowDemo);		
		RunResult runResult = new RunResult();
		runResult.setElementId(flowDemo.getId());
		
		JSONObject runResultJson = (JSONObject)parentParam;
		Map<String, String> map = new HashMap<>();
		map.put("desc", flowDemo.getDemoProperty().getDesc());
		runResultJson.put(flowDemo.getDemoProperty().getKey(), map);
		runResult.setJsonVar(JSON.toJSONString(runResultJson, true));
		executeStruct.getRunStatus().setCode("success");

		logger.debug("示例控件：{}，执行完毕！执行状态：{}", JSON.toJSONString(flowDemo), JSON.toJSONString(executeStruct.getRunStatus()));
		return runResult;
	}

}
