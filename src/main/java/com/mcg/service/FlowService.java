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

import javax.servlet.http.HttpSession;

import com.mcg.entity.common.SelectEntity;
import com.mcg.entity.flow.FlowStruct;
import com.mcg.entity.flow.data.DataRecord;
import com.mcg.entity.flow.gmybatis.Table;
import com.mcg.entity.flow.web.WebStruct;
import com.mcg.entity.global.datasource.McgDataSource;

/**
 * 
 * @ClassName:   CommonService   
 * @Description: TODO(流程控件的服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:46:01  
 *
 */
public interface FlowService {
	
    boolean testConnect(McgDataSource mcgDataSource);
    
	McgDataSource getMcgDataSourceById(String dataSourceId) throws ClassNotFoundException, IOException;
	
	List<McgDataSource> getMcgDataSources() throws ClassNotFoundException, IOException;
	
	List<DataRecord> getTableInfo(McgDataSource mcgDataSource, String tableName);
	
	List<Table> getTableByDataSource(McgDataSource mcgDataSource);
	
	FlowStruct xmlToflowStruct(String flowXml);
	
	/**
	 * 
	 * @Title:       saveFlow   
	 * @Description: TODO(保存流程)   
	 * @param:       @param webStruct 前端流程对象
	 * @param:       @return      
	 * @return:      boolean      
	 * @throws
	 */
	boolean saveFlow(WebStruct webStruct, HttpSession session) throws IOException;
	
	boolean generate(WebStruct webStruct, HttpSession session) throws ClassNotFoundException, IOException;
	
    /**
     * 清空文件的数据，但不删除文件
     * @param path 文件的绝对路径
     * @return
     */
	boolean clearFileData(String path);
	
	List<SelectEntity> getModelsByIds(List<String> ids);
}
