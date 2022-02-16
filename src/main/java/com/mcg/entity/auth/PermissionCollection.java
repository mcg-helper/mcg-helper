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

package com.mcg.entity.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PermissionCollection {
	private static PermissionCollection instance = new PermissionCollection();
	private static Logger logger = LoggerFactory.getLogger(PermissionCollection.class);

	public static PermissionCollection getInstance() {
		return instance;
	}

	private PermissionCollection() {}

	/** 登录Key和用户缓存信息的集合 */
	private Map<String, UserCacheBean> mapSU = new ConcurrentHashMap<String, UserCacheBean>();

	public void addSessionUserCache(String sessionID, UserCacheBean uc) {
		mapSU.put(sessionID, uc);
	}

	public void removeSessionUserCache(String sessionID) {
		try {
			UserCacheBean uc = mapSU.get(sessionID);
			if(uc != null) {
				McgUser mcgUser = uc.getUser();
				if(mcgUser != null) {
					Map<String, Session> webSocketSessionMap = mcgUser.getWebSocketMap();
					if(webSocketSessionMap != null && webSocketSessionMap.size() > 0) {
						for(String webSocketSessionId : webSocketSessionMap.keySet() ) {
							Session webSocketSession = webSocketSessionMap.get(webSocketSessionId);
							if(webSocketSession != null && webSocketSession.isOpen()) {
								webSocketSession.close();
							}
						}
					}
				}
			}
			mapSU.remove(sessionID);
		}catch (Exception e) {
			logger.error("用户session关闭出错：", e);
		}
	}

	public UserCacheBean getUserCache(String sessionID) {
		return mapSU.get(sessionID);
	}

	public Map<String, UserCacheBean> getMapSU() {
		return mapSU;
	}

}
