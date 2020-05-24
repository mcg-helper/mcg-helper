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
import java.util.List;
import java.util.Map;

import com.mcg.entity.flow.sequence.FlowSequence;
import com.mcg.entity.generate.Order;


/**
 * 
 * @ClassName:   TopoSort   
 * @Description: TODO(流程图排序服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:42:43  
 *
 */
public class TopoSort {

	int max;
	Vertexs ver[];
	int map[][];
	int n;
	String topoNameSort[];
	Map<String, Integer> sortMap;
	List<FlowSequence> loopFlowSequenceList;
	List<FlowSequence> allFlowSequenceList;
	Map<String, Order> orderMap;
	
	public void addVertex(String v) {
		ver[n++] = new Vertexs(v);
	}

	public void addEdge(int start, int end) {
		map[start][end] = 1;
	}

	public List<Order> getFlowSort() {
		int num = n;
		boolean isEdge;

		while (n > 0) {
			int currVer = 0;
			for (int row = 0; row < n; row++) {
				isEdge = false;
				for (int col = 0; col < n; col++) {
					if (map[row][col] > 0)
						isEdge = true;
				}
				if (!isEdge)
					currVer = row;
			}
			topoNameSort[n - 1] = ver[currVer].value;

			if (currVer != n - 1) {
				for (int i = currVer; i < n; i++)
					ver[i] = ver[i + 1];

				for (int row = currVer; row < n - 1; row++)
					for (int col = 0; col < n; col++)
						map[row][col] = map[row + 1][col];
				for (int col = currVer; col < n - 1; col++)
					for (int row = 0; row < n; row++)
						map[row][col] = map[row][col + 1];
			}
			n--; 
		}
		
		List<Order> loopList = new ArrayList<>();
        
		for (int k = 0; k < num; k++) {
		    Order order = new Order();
		    order.setElementId(topoNameSort[k]); //节点的id
		    List<String> pidList = new ArrayList<String>();
	        for(FlowSequence flowSequence : allFlowSequenceList) {
	            if(topoNameSort[k].equals(flowSequence.getTargetId())) {
	                pidList.add(flowSequence.getSourceId());
	            }
	        }

	        if(pidList.size() > 0) {
	            order.setPid(pidList);
	        }
	        loopList.add(order);
		}
		return loopList;
	}
	
	public List<Order> init(TopoSort topoSort, List<Order> loopList, List<FlowSequence> loopFlowSequenceList, List<FlowSequence> allFlowSequenceList) {
	    max = loopList.size();
        ver = new Vertexs[max];
        map = new int[max][max];
        n = 0;
        topoNameSort = new String[max];
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                map[i][j] = 0;
            }
        }	    
	    
        int num = 0;
        sortMap = new HashMap<String, Integer>();
	    for(Order order : loopList) {
	    	topoSort.addVertex(order.getElementId());
	    	sortMap.put(order.getElementId(), num++);
	    }

	    this.loopFlowSequenceList = loopFlowSequenceList;
	    this.allFlowSequenceList = allFlowSequenceList;
        for(FlowSequence flowSequence : loopFlowSequenceList) {
            topoSort.addEdge(sortMap.get(flowSequence.getSourceId()), sortMap.get(flowSequence.getTargetId()));
        }
        return getFlowSort();
	}
}

class Vertexs {
	public String value;

	public Vertexs(String v) {
		value = v;
	}
}