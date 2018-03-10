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

package com.mcg.service;

import java.io.IOException;
import java.util.List;

import com.mcg.plugin.flowtree.FlowTree;

/**
 * 
 * @ClassName:   FlowTreeService   
 * @Description: TODO(流程树的维护，如增、删、改、查) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:45:23  
 *
 */
public interface FlowTreeService {
	
    //获取流程树数据
	FlowTree getDatas() throws ClassNotFoundException, IOException;
	
	boolean selected(String id) throws ClassNotFoundException, IOException;
    
    boolean updateNode(String id, String name, String pId) throws ClassNotFoundException, IOException;
    
    boolean deleteNode(List<String> ids) throws ClassNotFoundException, IOException;
    
}