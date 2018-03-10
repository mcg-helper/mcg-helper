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

package com.mcg.plugin.websocket;

import java.io.IOException;
import java.util.UUID;

import javax.websocket.Session;

import com.alibaba.fastjson.JSON;
import com.mcg.entity.auth.PermissionCollection;
import com.mcg.entity.auth.UserCacheBean;
import com.mcg.entity.message.Header;
import com.mcg.entity.message.Message;

public class MessagePlugin {

	public static boolean push(String httpSessionId, Message message) {
		boolean result = false;
    	UserCacheBean ucb = PermissionCollection.getInstance().getUserCache(httpSessionId);
    	try {
    	    Session session = ucb.getUser().getSession();
    	    if(session.isOpen())
    	        session.getBasicRemote().sendText(JSON.toJSONString(message).replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	public static Message getMessage() {
		Message message = new Message();
		message.setId(UUID.randomUUID().toString());
		Header header = new Header();
		message.setHeader(header);
		return message;
	}
	
}