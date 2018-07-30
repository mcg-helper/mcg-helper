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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mcg.entity.flow.FlowStruct;
import com.mcg.entity.flow.data.FlowData;
import com.mcg.entity.flow.gmybatis.FlowGmybatis;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.json.FlowJson;
import com.mcg.entity.flow.linux.FlowLinux;
import com.mcg.entity.flow.model.FlowModel;
import com.mcg.entity.flow.python.FlowPython;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.flow.sequence.FlowSequence;
import com.mcg.entity.flow.sqlexecute.FlowSqlExecute;
import com.mcg.entity.flow.sqlquery.FlowSqlQuery;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.generate.Order;
import com.mcg.entity.generate.Orders;
import com.mcg.plugin.build.McgProduct;


/**
 * 
 * @ClassName:   TopoSort   
 * @Description: TODO(流程图排序服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:42:43  
 *
 */
public class TopoSort {

	/*
	 * max:定义顶点的最大容量为100 ver:使用内部Vertexs类创建一个数组ver,存储顶点的关键字 map:AOV网的邻接矩阵表
	 * n:顶点的数量 topoSort:存储最终得到的序列
	 */
	int max;
	Vertexs ver[];
	int map[][];
	int n;
	String topoNameSort[];
	Map<Integer, String> nodeMap;
	Map<String, Integer> sortMap;
	List<FlowSequence> flowSequenceList;
	Map<String, Order> orderMap;
	
	// 构造顶点关键字数组
	public void addVertex(String v) {
		ver[n++] = new Vertexs(v);
	}

	// 构造边的邻接矩阵
	public void addEdge(int start, int end) {
		map[start][end] = 1;
	}

	public Orders getFlowSort() {
		// num:顶点数量n要进行递减，因此起始存入num,作为输出序列遍历使用
		// isEdge:判定该边是否相连
		int num = n;
		boolean isEdge;

		while (n > 0) {
			// 选取一个入度为0的顶点
			int currVer = 0;
			// 遍历邻接矩阵
			for (int row = 0; row < n; row++) {
				isEdge = false;
				for (int col = 0; col < n; col++) {
					if (map[row][col] > 0)
						isEdge = true;
				}
				// 抽取邻接边表中没有出度的顶点(将邻接表行数赋值给当前顶点)
				if (!isEdge)
					currVer = row;
			}
			// 将没有出度的顶点存入序列结果最后的位置
			topoNameSort[n - 1] = ver[currVer].value;

			/*
			 * 删除顶点操作 当前顶点不为最后顶点时进行
			 */
			if (currVer != n - 1) {
				// 顶点关键字数组往后移动一位
				for (int i = currVer; i < n; i++)
					ver[i] = ver[i + 1];

				/*
				 * 移动邻接表行列，覆盖原值
				 */
				// 邻接表行数往后移动一位(向右移动)
				for (int row = currVer; row < n - 1; row++)
					for (int col = 0; col < n; col++)
						map[row][col] = map[row + 1][col];
				// 邻接表列数往后移动一位(向左移动)
				for (int col = currVer; col < n - 1; col++)
					for (int row = 0; row < n; row++)
						map[row][col] = map[row][col + 1];
			}
			n--; // 下一个顶点
		}
/*
        //输出矩阵
        for(int i=0; i<map.length; i++) {
            for(int j=0; j<map[i].length; j++) {
                if(j == (map[i].length-1) )
                    System.out.println(" " + map[i][j] + " ");
                else
                    System.out.print(" " + map[i][j] + " ");
            }
        }
		*/
        Orders orders = new Orders();
        List<Order> orderList = new ArrayList<Order>();
        
		// 流程图排序
		for (int k = 0; k < num; k++) {
		    Order order = new Order();
		    order.setElementId(topoNameSort[k]); //节点的id
		    List<String> pidList = new ArrayList<String>();
	        for(FlowSequence flowSequence : flowSequenceList) {
	            if(topoNameSort[k].equals(flowSequence.getTargetId())) {
	                pidList.add(flowSequence.getSourceId());
	            }
	        }
/*	        for(int i=0; i<max; i++) {
	            if(map[i][sortMap.get(topoNameSort[k])] == 1){
	                pidList.add(nodeMap.get(i));  //父级节点的id
	            }
	        }*/
	        if(pidList.size() > 0) {
	            order.setPid(pidList);
	        }
	        orderList.add(order);
		}
		orders.setOrder(orderList);
		return orders;
	}
	
	public Map<String, McgProduct> init(TopoSort topoSort, FlowStruct flowStruct) {
	    Map<String, McgProduct> dataMap = new HashMap<String, McgProduct>();
	    max = flowStruct.getTotalSize();
        ver = new Vertexs[max];
        map = new int[max][max];
        n = 0;
        topoNameSort = new String[max];
        // 初始化邻接矩阵
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                map[i][j] = 0;
            }
        }	    
	    
	    int num = 0;
	    sortMap = new HashMap<String, Integer>();
	    if(flowStruct.getFlowStart() != null) {
    	    topoSort.addVertex(flowStruct.getFlowStart().getStartId());
    	    dataMap.put(flowStruct.getFlowStart().getStartId(), flowStruct.getFlowStart());
            sortMap.put(flowStruct.getFlowStart().getStartId(), num++);
	    }
	    if(flowStruct.getFlowModels() != null && flowStruct.getFlowModels().getFlowModel() != null && flowStruct.getFlowModels().getFlowModel().size() >0) {
    	    for(FlowModel flowModel : flowStruct.getFlowModels().getFlowModel()) {
    	        topoSort.addVertex(flowModel.getModelId());
    	        dataMap.put(flowModel.getModelId(), flowModel);
    	        sortMap.put(flowModel.getModelId(), num++);
    	    }
	    }
        if(flowStruct.getFlowSqlQuerys() != null && flowStruct.getFlowSqlQuerys().getFlowSqlQuery() != null && flowStruct.getFlowSqlQuerys().getFlowSqlQuery().size() >0) {
            for(FlowSqlQuery sqlQuery : flowStruct.getFlowSqlQuerys().getFlowSqlQuery()) {
                topoSort.addVertex(sqlQuery.getId());
                dataMap.put(sqlQuery.getId(), sqlQuery);
                sortMap.put(sqlQuery.getId(), num++);
            }
        }   
        if(flowStruct.getFlowSqlExecutes() != null && flowStruct.getFlowSqlExecutes().getFlowSqlExecute() != null && flowStruct.getFlowSqlExecutes().getFlowSqlExecute().size() >0) {
            for(FlowSqlExecute flowSqlExecute : flowStruct.getFlowSqlExecutes().getFlowSqlExecute()) {
                topoSort.addVertex(flowSqlExecute.getId());
                dataMap.put(flowSqlExecute.getId(), flowSqlExecute);
                sortMap.put(flowSqlExecute.getId(), num++);
            }
        }         
	    if(flowStruct.getFlowJsons() != null && flowStruct.getFlowJsons().getFlowJson() != null && flowStruct.getFlowJsons().getFlowJson().size() >0) {
    	    for(FlowJson flowJson : flowStruct.getFlowJsons().getFlowJson()) {
    	        topoSort.addVertex(flowJson.getId());
    	        dataMap.put(flowJson.getId(), flowJson);
    	        sortMap.put(flowJson.getId(), num++);
    	    }
	    }	    
	    if(flowStruct.getFlowGmybatises() != null && flowStruct.getFlowGmybatises().getFlowGmybatis() != null && flowStruct.getFlowGmybatises().getFlowGmybatis().size() > 0) {
            for(FlowGmybatis flowGmybatis : flowStruct.getFlowGmybatises().getFlowGmybatis()) {
                topoSort.addVertex(flowGmybatis.getGmybatisId());
                dataMap.put(flowGmybatis.getGmybatisId(), flowGmybatis);
                sortMap.put(flowGmybatis.getGmybatisId(), num++);
            }
	    }
	    if(flowStruct.getFlowDatas() != null && flowStruct.getFlowDatas().getFlowData() != null && flowStruct.getFlowDatas().getFlowData().size() > 0) {
            for(FlowData flowData : flowStruct.getFlowDatas().getFlowData()) {
                topoSort.addVertex(flowData.getId());
                dataMap.put(flowData.getId(), flowData);
                sortMap.put(flowData.getId(), num++);
            }
	    }	    
	    if(flowStruct.getFlowScripts() != null && flowStruct.getFlowScripts().getFlowScript() != null && flowStruct.getFlowScripts().getFlowScript().size() > 0) {
            for(FlowScript flowScript : flowStruct.getFlowScripts().getFlowScript()) {
                topoSort.addVertex(flowScript.getScriptId());
                dataMap.put(flowScript.getScriptId(), flowScript);
                sortMap.put(flowScript.getScriptId(), num++);
            }
	    }	   
	    if(flowStruct.getFlowJavas() != null && flowStruct.getFlowJavas().getFlowJava() != null && flowStruct.getFlowJavas().getFlowJava().size() > 0) {
            for(FlowJava flowJava : flowStruct.getFlowJavas().getFlowJava()) {
                topoSort.addVertex(flowJava.getId());
                dataMap.put(flowJava.getId(), flowJava);
                sortMap.put(flowJava.getId(), num++);
            }
	    }
	    if(flowStruct.getFlowPythons() != null && flowStruct.getFlowPythons().getFlowPython() != null && flowStruct.getFlowPythons().getFlowPython().size() > 0) {
            for(FlowPython flowPython : flowStruct.getFlowPythons().getFlowPython()) {
                topoSort.addVertex(flowPython.getId());
                dataMap.put(flowPython.getId(), flowPython);
                sortMap.put(flowPython.getId(), num++);
            }
	    }	  
	    if(flowStruct.getFlowLinuxs() != null && flowStruct.getFlowLinuxs().getFlowLinux() != null && flowStruct.getFlowLinuxs().getFlowLinux().size() > 0) {
            for(FlowLinux flowLinux : flowStruct.getFlowLinuxs().getFlowLinux()) {
                topoSort.addVertex(flowLinux.getId());
                dataMap.put(flowLinux.getId(), flowLinux);
                sortMap.put(flowLinux.getId(), num++);
            }
	    }	    
	    if(flowStruct.getFlowTexts() != null && flowStruct.getFlowTexts().getFlowText() != null && flowStruct.getFlowTexts().getFlowText().size() > 0) {
            for(FlowText flowText : flowStruct.getFlowTexts().getFlowText()) {
                topoSort.addVertex(flowText.getTextId());
                dataMap.put(flowText.getTextId(), flowText);
                sortMap.put(flowText.getTextId(), num++);
            }
	    }
        if(flowStruct.getFlowEnd() != null) {
            topoSort.addVertex(flowStruct.getFlowEnd().getEndId());
    	    dataMap.put(flowStruct.getFlowEnd().getEndId(), flowStruct.getFlowEnd());
            sortMap.put(flowStruct.getFlowEnd().getEndId(), num++);
        }
        
        nodeMap = new LinkedHashMap<Integer, String>();
        for (String key : sortMap.keySet()) {
            nodeMap.put(sortMap.get(key), key);
        }
	    
        flowSequenceList = flowStruct.getFlowSequences().getFlowSequences();
        for(FlowSequence flowSequence : flowStruct.getFlowSequences().getFlowSequences()) {
            topoSort.addEdge(sortMap.get(flowSequence.getSourceId()), sortMap.get(flowSequence.getTargetId()));
        }
        return dataMap;
	}
}

class Vertexs {
	public String value;

	public Vertexs(String v) {
		value = v;
	}
}