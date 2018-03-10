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

import java.io.Serializable;

import javax.websocket.Session;

public class McgUser implements Serializable {

    private static final long serialVersionUID = -8405945101445634563L;
    
    private String userKey;
	/** 用户的websocket会话 */
	private Session session;
    
    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
    
}
