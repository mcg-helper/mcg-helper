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

package com.mcg.controller.flow;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mcg.controller.base.BaseController;
import com.mcg.plugin.flowtree.FlowTree;
import com.mcg.service.FlowTreeService;

/**
 * 
 * @ClassName:   FlowTreeController   
 * @Description: TODO(工作台中工具栏里面“流程树”的功能服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午3:59:16  
 *
 */
@Controller
@RequestMapping(value="/flowTree")
public class FlowTreeController extends BaseController {

    @Autowired
    private FlowTreeService flowTreeService;
    
    @RequestMapping(value="/getDatas")
    @ResponseBody
    public FlowTree getDatas() throws ClassNotFoundException, IOException {
        
        return flowTreeService.getDatas();
    }
    
    @RequestMapping(value="/selected")
    @ResponseBody
    public void selected(String id) throws ClassNotFoundException, IOException {
        flowTreeService.selected(id);
    }    
    
    @RequestMapping(value="/addOrUpdateNode")
    @ResponseBody
    public void updateNode(String id, String name, String pId) throws ClassNotFoundException, IOException {
        flowTreeService.updateNode(id, name, pId);
    }
    
    @RequestMapping(value="/deleteNode")
    @ResponseBody
    public void deleteNode(String ids) throws ClassNotFoundException, IOException {
    	List<String> idList = Arrays.asList(ids.split(","));
        flowTreeService.deleteNode(idList);
    }
    
}