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

package com.mcg.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.mcg.entity.flow.FlowStruct;
import com.mcg.entity.flow.data.FlowData;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.json.FlowJson;
import com.mcg.entity.flow.linux.FlowLinux;
import com.mcg.entity.flow.model.FlowModel;
import com.mcg.entity.flow.process.FlowProcess;
import com.mcg.entity.flow.python.FlowPython;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.flow.sequence.FlowSequence;
import com.mcg.entity.flow.sqlexecute.FlowSqlExecute;
import com.mcg.entity.flow.sqlquery.FlowSqlQuery;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.flow.wonton.FlowWonton;
import com.mcg.entity.generate.Order;
import com.mcg.entity.generate.Orders;
import com.mcg.plugin.build.McgProduct;

public class FlowRunSort {

	private static Logger logger = LoggerFactory.getLogger(FlowRunSort.class);
	
	private int numOfNode;
	private List<ArrayList<Integer>> graph;
	private List<ArrayList<Integer>> result;
	private boolean[] inStack;
	private Stack<Integer> stack;
	private int[] dfn;
	private int[] low;
	private int time;
	private Map<Integer, String> nodeMap;
	private Map<String, Integer> sortMap;
	private Orders orders;

	public FlowRunSort() {
	}

	public FlowRunSort(List<ArrayList<Integer>> graph, int numOfNode) {

	}

	public List<ArrayList<Integer>> run() {
		this.inStack = new boolean[numOfNode];
		this.stack = new Stack<Integer>();
		dfn = new int[numOfNode];
		low = new int[numOfNode];
		Arrays.fill(dfn, -1);
		Arrays.fill(low, -1);
		result = new ArrayList<ArrayList<Integer>>();
		
		for (int i = 0; i < numOfNode; i++) {
			if (dfn[i] == -1) {
				tarjan(i);
			}
		}
		return result;
	}

	public void tarjan(int current) {
		dfn[current] = low[current] = time++;
		inStack[current] = true;
		stack.push(current);

		for (int i = 0; i < graph.get(current).size(); i++) {
			int next = graph.get(current).get(i);
			if (dfn[next] == -1) {
				tarjan(next);
				low[current] = Math.min(low[current], low[next]);
			} else if (inStack[next]) {
				low[current] = Math.min(low[current], dfn[next]);
			}
		}

		if (low[current] == dfn[current]) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			int j = -1;
			while (current != j) {
				j = stack.pop();
				inStack[j] = false;
				temp.add(j);
			}
			result.add(temp);
		}
	}

	public Map<String, McgProduct> init(FlowStruct flowStruct) {
		Map<String, McgProduct> dataMap = new HashMap<String, McgProduct>();
		this.numOfNode = flowStruct.getTotalSize();
		this.graph = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < numOfNode; i++) {
			graph.add(new ArrayList<Integer>());
		}

		int num = 0;
		sortMap = new HashMap<String, Integer>();
		if (flowStruct.getFlowStart() != null) {
			dataMap.put(flowStruct.getFlowStart().getStartId(), flowStruct.getFlowStart());
			sortMap.put(flowStruct.getFlowStart().getStartId(), num++);
		}
		if (flowStruct.getFlowModels() != null && flowStruct.getFlowModels().getFlowModel() != null
				&& flowStruct.getFlowModels().getFlowModel().size() > 0) {
			for (FlowModel flowModel : flowStruct.getFlowModels().getFlowModel()) {
				dataMap.put(flowModel.getModelId(), flowModel);
				sortMap.put(flowModel.getModelId(), num++);
			}
		}
		if (flowStruct.getFlowSqlQuerys() != null && flowStruct.getFlowSqlQuerys().getFlowSqlQuery() != null
				&& flowStruct.getFlowSqlQuerys().getFlowSqlQuery().size() > 0) {
			for (FlowSqlQuery sqlQuery : flowStruct.getFlowSqlQuerys().getFlowSqlQuery()) {
				dataMap.put(sqlQuery.getId(), sqlQuery);
				sortMap.put(sqlQuery.getId(), num++);
			}
		}
		if (flowStruct.getFlowSqlExecutes() != null && flowStruct.getFlowSqlExecutes().getFlowSqlExecute() != null
				&& flowStruct.getFlowSqlExecutes().getFlowSqlExecute().size() > 0) {
			for (FlowSqlExecute flowSqlExecute : flowStruct.getFlowSqlExecutes().getFlowSqlExecute()) {
				dataMap.put(flowSqlExecute.getId(), flowSqlExecute);
				sortMap.put(flowSqlExecute.getId(), num++);
			}
		}
		if (flowStruct.getFlowJsons() != null && flowStruct.getFlowJsons().getFlowJson() != null
				&& flowStruct.getFlowJsons().getFlowJson().size() > 0) {
			for (FlowJson flowJson : flowStruct.getFlowJsons().getFlowJson()) {
				dataMap.put(flowJson.getId(), flowJson);
				sortMap.put(flowJson.getId(), num++);
			}
		}
		if (flowStruct.getFlowDatas() != null && flowStruct.getFlowDatas().getFlowData() != null
				&& flowStruct.getFlowDatas().getFlowData().size() > 0) {
			for (FlowData flowData : flowStruct.getFlowDatas().getFlowData()) {
				dataMap.put(flowData.getId(), flowData);
				sortMap.put(flowData.getId(), num++);
			}
		}
		if (flowStruct.getFlowScripts() != null && flowStruct.getFlowScripts().getFlowScript() != null
				&& flowStruct.getFlowScripts().getFlowScript().size() > 0) {
			for (FlowScript flowScript : flowStruct.getFlowScripts().getFlowScript()) {
				dataMap.put(flowScript.getScriptId(), flowScript);
				sortMap.put(flowScript.getScriptId(), num++);
			}
		}
		if (flowStruct.getFlowJavas() != null && flowStruct.getFlowJavas().getFlowJava() != null
				&& flowStruct.getFlowJavas().getFlowJava().size() > 0) {
			for (FlowJava flowJava : flowStruct.getFlowJavas().getFlowJava()) {
				dataMap.put(flowJava.getId(), flowJava);
				sortMap.put(flowJava.getId(), num++);
			}
		}
		if (flowStruct.getFlowPythons() != null && flowStruct.getFlowPythons().getFlowPython() != null
				&& flowStruct.getFlowPythons().getFlowPython().size() > 0) {
			for (FlowPython flowPython : flowStruct.getFlowPythons().getFlowPython()) {
				dataMap.put(flowPython.getId(), flowPython);
				sortMap.put(flowPython.getId(), num++);
			}
		}
		if (flowStruct.getFlowLinuxs() != null && flowStruct.getFlowLinuxs().getFlowLinux() != null
				&& flowStruct.getFlowLinuxs().getFlowLinux().size() > 0) {
			for (FlowLinux flowLinux : flowStruct.getFlowLinuxs().getFlowLinux()) {
				dataMap.put(flowLinux.getId(), flowLinux);
				sortMap.put(flowLinux.getId(), num++);
			}
		}
		if (flowStruct.getFlowWontons() != null && flowStruct.getFlowWontons().getFlowWonton() != null
				&& flowStruct.getFlowWontons().getFlowWonton().size() > 0) {
			for (FlowWonton flowWonton : flowStruct.getFlowWontons().getFlowWonton()) {
				dataMap.put(flowWonton.getId(), flowWonton);
				sortMap.put(flowWonton.getId(), num++);
			}
		}
		if (flowStruct.getFlowProcesses() != null && flowStruct.getFlowProcesses().getFlowProcess() != null
				&& flowStruct.getFlowProcesses().getFlowProcess().size() > 0) {
			for (FlowProcess flowProcess : flowStruct.getFlowProcesses().getFlowProcess()) {
				dataMap.put(flowProcess.getId(), flowProcess);
				sortMap.put(flowProcess.getId(), num++);
			}
		}

		if (flowStruct.getFlowTexts() != null && flowStruct.getFlowTexts().getFlowText() != null
				&& flowStruct.getFlowTexts().getFlowText().size() > 0) {
			for (FlowText flowText : flowStruct.getFlowTexts().getFlowText()) {
				dataMap.put(flowText.getTextId(), flowText);
				sortMap.put(flowText.getTextId(), num++);
			}
		}
		if (flowStruct.getFlowEnd() != null) {
			dataMap.put(flowStruct.getFlowEnd().getEndId(), flowStruct.getFlowEnd());
			sortMap.put(flowStruct.getFlowEnd().getEndId(), num++);
		}

		nodeMap = new LinkedHashMap<Integer, String>();
		for (String key : sortMap.keySet()) {
			nodeMap.put(sortMap.get(key), key);
		}

		for (FlowSequence flowSequence : flowStruct.getFlowSequences().getFlowSequences()) {
			graph.get(sortMap.get(flowSequence.getSourceId())).add(sortMap.get(flowSequence.getTargetId()));
		}

		result = run();
		Orders tempOrders = new Orders();
		List<List<Order>> orderList = new ArrayList<List<Order>>();
		
		logger.debug("正在执行流程：{}，排序完毕，执行顺序如下", JSON.toJSONString(flowStruct));
		for (int i = (result.size() -1); i >= 0; i--) {
			List<Order> orderLoopList = new ArrayList<Order>(); //list.size()大于1 则代表是流程图中的环，以及节点执行的顺序
			for (int j = (result.get(i).size() -1); j >= 0 ; j--) {
				Order order = new Order();
				order.setElementId(nodeMap.get(result.get(i).get(j))); // 节点的id
				List<String> pidList = new ArrayList<String>();
				for (FlowSequence flowSequence : flowStruct.getFlowSequences().getFlowSequences()) {
					if (nodeMap.get(result.get(i).get(j)).equals(flowSequence.getTargetId())) {
						pidList.add(flowSequence.getSourceId());
					}
				}
				if (pidList.size() > 0) {
					order.setPid(pidList);
				}
				orderLoopList.add(order);
				logger.debug("控件id：{}, ", nodeMap.get(result.get(i).get(j)));
			}
			orderList.add(orderLoopList);
		}

		tempOrders.setOrder(orderList);
		this.orders = tempOrders;
		return dataMap;
	}

	public Orders getFlowSort() {
		return this.orders;
	}

}
