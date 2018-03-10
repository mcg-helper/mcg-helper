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
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.CaseFormat;
import com.mcg.common.Constants;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.common.SelectEntity;
import com.mcg.entity.flow.FlowStruct;
import com.mcg.entity.flow.data.DataRecord;
import com.mcg.entity.flow.gmybatis.Table;
import com.mcg.entity.flow.model.FlowModel;
import com.mcg.entity.flow.web.WebStruct;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.generate.RunStatus;
import com.mcg.entity.global.McgGlobal;
import com.mcg.entity.global.datasource.McgDataSource;
import com.mcg.entity.message.Message;
import com.mcg.entity.message.NotifyBody;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.dbconn.FlowDataAdapterImpl;
import com.mcg.plugin.dbconn.McgBizAdapter;
import com.mcg.plugin.ehcache.CachePlugin;
import com.mcg.plugin.generate.FlowTask;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.service.FlowService;
import com.mcg.service.GlobalService;
import com.mcg.util.DataConverter;
import com.mcg.util.LevelDbUtil;
import com.mcg.util.McgFileUtils;
import com.mcg.util.TopoSort;

@Service
public class FlowServiceImpl implements FlowService {

    @Autowired
    private GlobalService globalService;
    
    @Override
    public boolean testConnect(McgDataSource mcgDataSource) {
        McgBizAdapter mcgBizAdapter = new FlowDataAdapterImpl(mcgDataSource);
        return mcgBizAdapter.testConnect();
    }

    @Override
	public List<McgDataSource> getMcgDataSources() throws ClassNotFoundException, IOException {
		List<McgDataSource> mcgDataSourceList = null;
		McgGlobal mcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
		if(mcgGlobal != null) {
			mcgDataSourceList = mcgGlobal.getFlowDataSources();
		}
		return mcgDataSourceList;
	}
	
	@Override
	public List<DataRecord> getTableInfo(McgDataSource mcgDataSource, String tableName) {
	    
	    McgBizAdapter mcgBizAdapter = new FlowDataAdapterImpl(mcgDataSource);
		List<DataRecord> result = null;
        try {
        	result = mcgBizAdapter.getTableInfo(tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return result;
	}

	@Override
	public List<Table> getTableByDataSource(McgDataSource mcgDataSource) {
		List<Table> result = null;
		McgBizAdapter mcgBizAdapter = new FlowDataAdapterImpl(mcgDataSource);
        
        try {
            result = mcgBizAdapter.getTablesByDataSource(mcgDataSource.getDbName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		if(result != null && result.size() > 0) {
		    for(Table table : result) {
		        String entityName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table.getTableName());
		        table.setDaoName(entityName + "Dao");
		        table.setEntityName(entityName);
		        table.setXmlName(entityName + "Mapper");
		    }
		}
		return result;
	}
	
	@Override
	public McgDataSource getMcgDataSourceById(String dataSourceId) throws ClassNotFoundException, IOException {
		McgDataSource result = null;
		List<McgDataSource> mcgDataSourceList = getMcgDataSources();
		if(mcgDataSourceList == null) {
			return null;
		}
		for(McgDataSource mcgDataSource : mcgDataSourceList) {
			if(dataSourceId.equals(mcgDataSource.getDataSourceId())) {
				result = mcgDataSource;
				break;
			}
		}
		return result;
	}

	@Override
	public FlowStruct xmlToflowStruct(String flowXml) {
		FlowStruct flowStruct = null;
        try {  
            JAXBContext context = JAXBContext.newInstance(FlowStruct.class);  
            Unmarshaller unmarshaller = context.createUnmarshaller();  
            flowStruct = (FlowStruct)unmarshaller.unmarshal(new StringReader(flowXml));  
        } catch (JAXBException e) {  
            e.printStackTrace();  
        }		
		return flowStruct;
	}

    @Override
    public boolean saveFlow(WebStruct webStruct, HttpSession session) throws IOException {
        boolean result = false;
        FlowStruct flowStruct = DataConverter.webStructToflowStruct(webStruct);
        if(flowStruct == null) {
            globalService.saveFlowEmpty(webStruct.getFlowId());
        } else  {
            flowStruct.setMcgId(webStruct.getFlowId());
			LevelDbUtil.putObject(webStruct.getFlowId(), flowStruct);
        }
        result = true;
        return result;
    }

    @Override
    public boolean generate(WebStruct webStruct, HttpSession session) throws ClassNotFoundException, IOException {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY); 
        NotifyBody notifyBody = new NotifyBody();
        
        FlowStruct flowStruct = (FlowStruct)LevelDbUtil.getObject(webStruct.getFlowId(), FlowStruct.class);
        if(flowStruct != null) {

            Map<String, RunResult> runResultMap = new HashMap<String, RunResult>();
            TopoSort topoSort = new TopoSort();
            ExecuteStruct executeStruct = new ExecuteStruct();
            executeStruct.setDataMap(topoSort.init(topoSort, flowStruct));
            executeStruct.setOrders(topoSort.getFlowSort());
            executeStruct.setRunResultMap(runResultMap);
            RunStatus runStatus = new RunStatus();
            executeStruct.setRunStatus(runStatus);          
        
            notifyBody.setContent("流程执行开始！");
            notifyBody.setType(LogTypeEnum.INFO.getValue());
            message.setBody(notifyBody);
            MessagePlugin.push(session.getId(), message);
            FlowTask flowTask = new FlowTask(session.getId(), flowStruct, this, executeStruct);
            Thread thread = new Thread(flowTask, session.getId());
            thread.start();             
        } else {
            notifyBody.setContent("请先绘制流程！");
            notifyBody.setType(LogTypeEnum.ERROR.getValue());
            message.setBody(notifyBody);
            MessagePlugin.push(session.getId(), message);             
        }        
        return false;
    }

	@Override
	public boolean clearFileData(String path) {
		
		return McgFileUtils.clearFileData(path);
	}

    @Override
    public List<SelectEntity> getModelsByIds(List<String> ids) {
        List<SelectEntity> selectList = new ArrayList<SelectEntity>();
        for(String modelId : ids) {
            McgProduct mcgProduct = (McgProduct)CachePlugin.get(modelId);
            if(mcgProduct instanceof FlowModel) {
                FlowModel flowModel = (FlowModel)CachePlugin.get(modelId);
                SelectEntity select = new SelectEntity();
                select.setName(flowModel.getModelProperty().getModelName());
                select.setValue(flowModel.getModelId());
                selectList.add(select);
            }
        }
        return selectList;
    }
	
    
}