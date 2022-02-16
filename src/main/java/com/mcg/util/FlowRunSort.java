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
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.entity.flow.FlowBase;
import com.mcg.entity.flow.FlowStruct;
import com.mcg.entity.flow.sequence.FlowSequence;
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

	public ConcurrentHashMap<String, McgProduct> init(FlowStruct flowStruct) {
		ConcurrentHashMap<String, McgProduct> dataMap = new ConcurrentHashMap<String, McgProduct>();
		this.numOfNode = flowStruct.getTotalSize();
		this.graph = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < numOfNode; i++) {
			graph.add(new ArrayList<Integer>());
		}

		int num = 0;
		sortMap = new HashMap<String, Integer>();
		for(String classes : flowStruct.getFlowElementMap().keySet()) {
			List<?> mcgProductList = flowStruct.getFlowElementMap().get(classes);
			if(mcgProductList != null && mcgProductList.size() > 0) {
				for(Object obj : mcgProductList) {
					JSONObject mcgProductJsonObject = (JSONObject)obj;
					for(EletypeEnum eletypeEnum : EletypeEnum.values()) {
	        			if(eletypeEnum.getValue().equals(mcgProductJsonObject.getString("eletype"))) {
							FlowBase flowBase = (FlowBase)JSON.toJavaObject((JSONObject)obj, eletypeEnum.getClasses());
							dataMap.put(flowBase.getId(), (McgProduct)flowBase);
							sortMap.put(flowBase.getId(), num++);
							break;
	        			}
					}

				}
			}
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
		
		for (int i = (result.size() -1); i >= 0; i--) {
			List<Order> orderLoopList = new ArrayList<Order>();
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

			}
			orderList.add(orderLoopList);
		}
		
		List<List<Order>> mcgFlowOrderList = new ArrayList<>();
		for(List<Order> loopList : orderList) {
			if(loopList.size() == 1) {
				mcgFlowOrderList.add(loopList);
			} else {
				List<FlowSequence> loopFlowSequenceList = new ArrayList<>();
				List<Order> topoOrderList = new ArrayList<>();
				Order loopOrder = new Order(); 
				for(int i=0; i<loopList.size(); i++) {
					Order order  = loopList.get(i);
					if(order.getElementId().contains("toolbarLoop")) {
						loopOrder = order;
					} else {
						topoOrderList.add(order);
					}
					for (FlowSequence flowSequence : flowStruct.getFlowSequences().getFlowSequences()) {					
						if(flowSequence.getSourceId().contains("toolbarLoop")) {
							continue ;
						}
						if(order.getElementId().equals(flowSequence.getSourceId())) {
							loopFlowSequenceList.add(flowSequence);
						}
						
					}
				}
				topoOrderList.add(loopOrder);
				TopoSort topoSort = new TopoSort();
				
				List<Order> topoList = topoSort.init(topoSort, topoOrderList, loopFlowSequenceList, flowStruct.getFlowSequences().getFlowSequences());
				mcgFlowOrderList.add(topoList);
			}
			
		}
		
		logger.debug("正在执行流程：{}，排序完毕，执行顺序如下：{}", JSON.toJSONString(flowStruct), JSON.toJSONString(mcgFlowOrderList));
		tempOrders.setOrder(mcgFlowOrderList);
		this.orders = tempOrders;
		return dataMap;
	}

	public Orders getFlowSort() {
		return this.orders;
	}

}
