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

import java.util.HashMap;
import java.util.Map;

public class PermissionCollection {
	private static PermissionCollection instance = new PermissionCollection();

	public static PermissionCollection getInstance() {
		return instance;
	}

	private PermissionCollection() {
	}

	/** 登录Key和用户缓存信息的集合 */
	private Map<String, UserCacheBean> mapSU = new HashMap<String, UserCacheBean>();

	public void addSessionUserCache(String sessionID, UserCacheBean uc) {
		mapSU.put(sessionID, uc);
	}

	public void removeSessionUserCache(String sessionID) {
		mapSU.remove(sessionID);
	}

	public UserCacheBean getUserCache(String sessionID) {
		return mapSU.get(sessionID);
	}
	
}
