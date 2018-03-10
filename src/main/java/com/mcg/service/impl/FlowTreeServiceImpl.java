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

package com.mcg.service.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mcg.common.Constants;
import com.mcg.entity.global.McgGlobal;
import com.mcg.entity.global.topology.Topology;
import com.mcg.plugin.flowtree.FlowTree;
import com.mcg.service.FlowTreeService;
import com.mcg.service.GlobalService;
import com.mcg.util.LevelDbUtil;

@Service
public class FlowTreeServiceImpl implements FlowTreeService {
    
    @Autowired
    private GlobalService globalService;
    
	@Override
    public FlowTree getDatas() throws ClassNotFoundException, IOException {
	    
	    FlowTree flowTree = null;
    	McgGlobal mcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
    	flowTree = new FlowTree(mcgGlobal.getSelected(), mcgGlobal.getTopologys());

        return flowTree;
    }
	
    @Override
	public boolean selected(String id) throws ClassNotFoundException, IOException {
        
        McgGlobal mcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
        FlowTree flowTree = new FlowTree(mcgGlobal.getSelected(), mcgGlobal.getTopologys());
        mcgGlobal.setSelected(flowTree.getTreeMap().get(id));
        LevelDbUtil.putObject(Constants.GLOBAL_KEY, mcgGlobal);
		return true;
	}

	@Override
    public boolean updateNode(String id, String name, String pId) throws ClassNotFoundException, IOException {
        boolean result = false;
        McgGlobal mcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
        
        FlowTree flowTree = new FlowTree(mcgGlobal.getSelected(), mcgGlobal.getTopologys());
        Topology topology = flowTree.getTreeMap().get(id);
        if(topology != null) {
            topology.setName(name);
            mcgGlobal.setSelected(topology);
            result = true;
        } else {
            Topology newNode = new Topology();
            newNode.setId(id);
            newNode.setName(name);
            newNode.setpId(pId);
            flowTree.getTreeMap().put(id, newNode);
            mcgGlobal.setSelected(newNode);
            mcgGlobal.setTopologys(flowTree.getTreeData());
            result = true;
        }
        
        globalService.updateGlobal(mcgGlobal);
        return result;
    }

    @Override
    public boolean deleteNode(List<String> ids) throws ClassNotFoundException, IOException {

        McgGlobal mcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
		
        FlowTree flowTree = new FlowTree(mcgGlobal.getSelected(), mcgGlobal.getTopologys());
        Iterator<String> iterator = ids.iterator();
        while (iterator.hasNext()) {
        	String flowId = iterator.next();
        	LevelDbUtil.delete(flowId);
        	flowTree.getTreeMap().remove(flowId);
        }
        
        mcgGlobal.setSelected(flowTree.getTreeMap().get("root"));
        mcgGlobal.setTopologys(flowTree.getTreeData());

        return globalService.updateGlobal(mcgGlobal);
    }

}