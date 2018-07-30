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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mcg.common.sysenum.DatabaseTypeEnum;
import com.mcg.common.sysenum.SeverTypeEnum;
import com.mcg.controller.base.BaseController;
import com.mcg.entity.flow.data.DataRecord;
import com.mcg.entity.flow.gmybatis.Table;
import com.mcg.entity.global.datasource.McgDataSource;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.ehcache.CachePlugin;
import com.mcg.service.FlowService;
import com.mcg.util.PageData;

/**
 * 
 * @ClassName:   CommonController   
 * @Description: TODO(前端常用基本服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午3:56:12  
 *
 */
@Controller
@RequestMapping(value="/common")
public class CommonController extends BaseController {

    @Autowired
    private FlowService flowService;
    
    @RequestMapping(value="/getMcgDataSources")
    @ResponseBody
    public List<McgDataSource> getMcgDataSources() throws ClassNotFoundException, IOException {
    	return flowService.getMcgDataSources();
    }
    
    @RequestMapping(value="getTableByDataSourceId", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody    
    public List<Table> getTableByDataSourceId() throws ClassNotFoundException, IOException {
        PageData pd = this.getPageData();
        if(pd.get("dataSourceId") == null)
        	return null;
    	return flowService.getTableByDataSource(flowService.getMcgDataSourceById(pd.getString("dataSourceId")));
    }
    
    @RequestMapping(value="getTableInfo", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody    
    public List<DataRecord> getTableInfo() throws ClassNotFoundException, IOException {
        PageData pd = this.getPageData();
        if(pd.get("dataSourceId") == null || pd.get("tableName") == null)
            return null;
        return flowService.getTableInfo(flowService.getMcgDataSourceById(pd.getString("dataSourceId")), pd.getString("tableName"));
    }    
    
    @RequestMapping(value="getMcgProductById", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody    
    public McgProduct getMcgProductById() {
    	PageData pd = this.getPageData();
        if(pd.get("id") == null)
        	return null;    	
    	McgProduct mcgProduct = (McgProduct)CachePlugin.get(pd.getString("id"));
    	return mcgProduct;
    }
    
    @RequestMapping(value="/getDatabaseTypes")
    @ResponseBody   
    public Map<String, String> getDatabaseType() throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        for (DatabaseTypeEnum dt : DatabaseTypeEnum.values()) {
            map.put(dt.getName(), dt.getValue());
        }       
        return map;
    }    
    
    @RequestMapping(value="/getServerTypes")
    @ResponseBody   
    public Map<String, String> getServerTypes() throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        for (SeverTypeEnum dt : SeverTypeEnum.values()) {
            map.put(dt.getName(), dt.getValue());
        }       
        return map;
    }    
    
}