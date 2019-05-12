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

package com.mcg.plugin.flowtree;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import com.mcg.common.Constants;
import com.mcg.entity.global.McgGlobal;
import com.mcg.entity.global.topology.Topology;
import com.mcg.util.LevelDbUtil;

public class FlowTree implements Serializable {

	private static final long serialVersionUID = 1215620860343159189L;
	//当前选中节点
    private Topology selected;
    //流程树数据
    private TreeMap<String, Topology> treeMap;
    //所有节点数据
    private List<Topology> topologys;
    
    public FlowTree(McgGlobal mcgGlobal) throws IOException{
    	
    	if(mcgGlobal == null) {
    		mcgGlobal = new McgGlobal();
    		Topology topology = new Topology();
    		topology.setId("root");
    		topology.setName("我的流程");
    		topology.setpId("1");
    		
    		mcgGlobal.setTopologys(new ArrayList<Topology>() { 
				private static final long serialVersionUID = 1L;
				{ 
					add(topology); 
				} 
			});
    		LevelDbUtil.putObject(Constants.GLOBAL_KEY, mcgGlobal);
    	}
    	
    	setTopologys(mcgGlobal.getTopologys());
    	initTreeMap(mcgGlobal.getTopologys());
    	setSelected(treeMap.get("root"));
    	
    }
    
    public void initTreeMap(List<Topology> topologys) {
    	treeMap = new TreeMap<String, Topology>();
    	Iterator<Topology> iterator = topologys.iterator();
    	while(iterator.hasNext()) {
    		Topology topology = iterator.next();
    		treeMap.put(topology.getId(), topology);
    	}
    }
    
    public List<Topology> getTreeData() {
        List<Topology> topologyList = new ArrayList<Topology>();
        Iterator<Topology> iterator = treeMap.values().iterator();
        while(iterator.hasNext()) {
            topologyList.add(iterator.next());
        }
        return topologyList;
    }

    public Topology getSelected() {
		return selected;
	}

	public void setSelected(Topology selected) {
		this.selected = selected;
	}

	public TreeMap<String, Topology> getTreeMap() {
        return treeMap;
    }

    public void setTreeMap(TreeMap<String, Topology> treeMap) {
        this.treeMap = treeMap;
    }

	public List<Topology> getTopologys() {
		return topologys;
	}

	public void setTopologys(List<Topology> topologys) {
    	this.topologys = topologys;
	}
    
}