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
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.CaseFormat;
import com.mcg.common.Constants;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.common.Table;
import com.mcg.entity.flow.FlowStruct;
import com.mcg.entity.flow.data.DataRecord;
import com.mcg.entity.flow.web.WebStruct;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.generate.RunStatus;
import com.mcg.entity.global.McgGlobal;
import com.mcg.entity.global.datasource.McgDataSource;
import com.mcg.entity.global.serversource.ServerSource;
import com.mcg.entity.global.topology.Topology;
import com.mcg.entity.message.Message;
import com.mcg.entity.message.NotifyBody;
import com.mcg.plugin.dbconn.FlowDataAdapterImpl;
import com.mcg.plugin.dbconn.McgBizAdapter;
import com.mcg.plugin.generate.FlowTask;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.service.DbService;
import com.mcg.service.FlowService;
import com.mcg.service.GlobalService;
import com.mcg.util.DataConverter;
import com.mcg.util.FlowInstancesUtils;
import com.mcg.util.FlowRunSort;
import com.mcg.util.McgFileUtils;
import com.mcg.util.ThreadPoolUtils;
import com.mcg.util.Tools;

@Service
public class FlowServiceImpl implements FlowService {

	private static Logger logger = LoggerFactory.getLogger(FlowServiceImpl.class);
	
    @Autowired
    private GlobalService globalService;
    @Autowired
    private DbService dbService;
    
    @Override
    public boolean testConnect(McgDataSource mcgDataSource) {
        McgBizAdapter mcgBizAdapter = new FlowDataAdapterImpl(mcgDataSource);
        return mcgBizAdapter.testConnect();
    }

    @Override
	public List<McgDataSource> getMcgDataSources() throws ClassNotFoundException, IOException {
		List<McgDataSource> mcgDataSourceList = null;
		McgGlobal mcgGlobal = (McgGlobal)dbService.query(Constants.GLOBAL_KEY, McgGlobal.class);
		if(mcgGlobal != null) {
			mcgDataSourceList = mcgGlobal.getFlowDataSources();
		}
		return mcgDataSourceList;
	}
	

	@Override
	public List<ServerSource> getMcgServerSources() throws ClassNotFoundException, IOException {
		List<ServerSource> serverSourceList = null;
		McgGlobal mcgGlobal = (McgGlobal)dbService.query(Constants.GLOBAL_KEY, McgGlobal.class);
		if(mcgGlobal != null) {
			serverSourceList = mcgGlobal.getServerSources();
		}
		return serverSourceList;
	}

	@Override
	public List<DataRecord> getTableInfo(McgDataSource mcgDataSource, String tableName) {
	    
	    McgBizAdapter mcgBizAdapter = new FlowDataAdapterImpl(mcgDataSource);
		List<DataRecord> result = null;
        try {
        	result = mcgBizAdapter.getTableInfo(tableName);
        } catch (Exception e) {
        	logger.error("获取表结构信息出错，异常信息：{}", e.getMessage());
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
        	logger.error("获取所有表名出错，异常信息：{}", e.getMessage());
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
        	logger.error("获取流程xml数据转换FlowStruct对象出错，异常信息：{}", e.getMessage());
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
            dbService.save(webStruct.getFlowId(), flowStruct);
        }
        result = true;
        return result;
    }

    @Override
    public RunStatus generate(WebStruct webStruct, HttpSession session, boolean subFlag, String parentFlowId, JSON parentParam) throws ClassNotFoundException, IOException, ExecutionException {
    	RunStatus runStatus = new RunStatus();
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY); 
        NotifyBody notifyBody = new NotifyBody();
        
        FlowStruct flowStruct = (FlowStruct)dbService.query(webStruct.getFlowId(), FlowStruct.class);
        if(flowStruct != null) {

        	Topology curTopology = null; //当前执行的流程
        	McgGlobal mcgGlobal = (McgGlobal)dbService.query(Constants.GLOBAL_KEY, McgGlobal.class);
        	if(mcgGlobal != null) {
        		for(Topology topology : mcgGlobal.getTopologys()) {
        			if(topology.getId().equals(flowStruct.getMcgId())) {
        				curTopology = topology;
        				break;
        			}
        		}
        	}
        	
        	String flowInstanceId = Tools.genFlowInstanceId(session.getId(), flowStruct.getMcgId());
        	
        	ConcurrentHashMap<String, RunResult> runResultMap = new ConcurrentHashMap<String, RunResult>();
            FlowRunSort flowRunSort = new FlowRunSort();
            ExecuteStruct executeStruct = new ExecuteStruct();
            executeStruct.setMcgWebScoketCode(webStruct.getMcgWebScoketCode());
            executeStruct.setFlowId(flowStruct.getMcgId());
            executeStruct.setFlowInstanceId(flowInstanceId);
            executeStruct.setDataMap(flowRunSort.init(flowStruct));
            executeStruct.setOrders(flowRunSort.getFlowSort());
            executeStruct.setRunResultMap(runResultMap);
            executeStruct.setSession(session);
            executeStruct.setTopology(curTopology);
            executeStruct.setSubFlag(subFlag);
            executeStruct.setRunStatus(runStatus);
        
            notifyBody.setContent("【" + curTopology.getName() + "】流程执行开始！");
            notifyBody.setType(LogTypeEnum.INFO.getValue());
            message.setBody(notifyBody);
            MessagePlugin.push(webStruct.getMcgWebScoketCode(), session.getId(), message);
            
            try {
	            FlowTask flowTask = new FlowTask(webStruct.getMcgWebScoketCode(), session.getId(), flowStruct, executeStruct, subFlag);
	            
	            FlowInstancesUtils.executeStructMap.put(flowInstanceId, executeStruct);
	            Future<RunStatus> future = ThreadPoolUtils.FLOW_WORK_EXECUTOR.submit(flowTask);
	            
	            FlowInstancesUtils.executeStructMap.get(flowInstanceId).getFlowTaskFutureList().add(future);
	            if(subFlag) {
	            	
	            	String subFlowInstanceId = Tools.genFlowInstanceId(session.getId(), parentFlowId);
	            	ExecuteStruct parentExecuteStruct = FlowInstancesUtils.executeStructMap.get(subFlowInstanceId);
	            	executeStruct.setParentParam(parentParam);
	            	parentExecuteStruct.setChildExecuteStruct(executeStruct);
	            }
	            runStatus = future.get();
            	logger.debug("流程：{}， 执行结果：{}", JSON.toJSONString(curTopology), JSON.toJSONString(runStatus));
            	return runStatus;
            	
            } catch(CancellationException e) {
    			executeStruct.getRunStatus().setInterrupt(true);
            	logger.error("流程执行中断，抛出CancellationException，异常信息：", e);
            } catch (InterruptedException e) {
    			executeStruct.getRunStatus().setInterrupt(true);
            	logger.error("流程执行中断，抛出InterruptedException，异常信息：", e);
			}
            
        } else {
            notifyBody.setContent("请先绘制流程！");
            notifyBody.setType(LogTypeEnum.ERROR.getValue());
            message.setBody(notifyBody);
            MessagePlugin.push(webStruct.getMcgWebScoketCode(), session.getId(), message);
        }
        return runStatus;
    }

	@Override
	public boolean clearFileData(String path) {
		
		return McgFileUtils.clearFileData(path);
	}
    
}