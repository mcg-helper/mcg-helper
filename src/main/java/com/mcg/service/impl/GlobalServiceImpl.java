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
import java.util.List;

import org.springframework.stereotype.Service;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mcg.common.Constants;
import com.mcg.entity.global.McgGlobal;
import com.mcg.entity.global.serversource.ServerSource;
import com.mcg.service.GlobalService;
import com.mcg.util.LevelDbUtil;

@Service
public class GlobalServiceImpl implements GlobalService {

    @Override
	public McgGlobal getMcgGlobal() throws ClassNotFoundException, IOException {
    	
    	return (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
	}

	@Override
    public boolean updateGlobal(McgGlobal mcgGlobal) throws IOException {
    	boolean result = false;

		LevelDbUtil.putObject(Constants.GLOBAL_KEY, mcgGlobal);
		result = true;
        return result;
    }
    
    @Override
	public boolean saveFlowEmpty(String flowId) {
    	
    	LevelDbUtil.delete(flowId);
		return true;
	}

	@Override
	public ServerSource getServerSourceById(String id) throws ClassNotFoundException, IOException {
		ServerSource result = null;
		List<ServerSource> serverSourceList = getServerSources();
		if(serverSourceList == null) {
			return null;
		}
		for(ServerSource serverSource : serverSourceList) {
			if(id.equals(serverSource.getId())) {
				result = serverSource;
				break;
			}
		}
		return result;
	}

	@Override
	public List<ServerSource> getServerSources() throws ClassNotFoundException, IOException {
		List<ServerSource> serverSourceList = null;
		McgGlobal mcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
		if(mcgGlobal != null) {
			serverSourceList = mcgGlobal.getServerSources();
		}
		return serverSourceList;
	}

	@Override
	public boolean isConnected(ServerSource serverSource) {
		boolean result = false;
        JSch jsch = new JSch();
        Session session = null;		
		try {
			session = jsch.getSession(serverSource.getUserName(), serverSource.getIp(), serverSource.getPort());
	        session.setConfig("StrictHostKeyChecking", "no");
	        session.setPassword(serverSource.getPwd());
	        session.connect();
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			result = session.isConnected();
			session.disconnect();
		}
		return result;		
	}
	
}