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
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.mcg.entity.auth.PermissionCollection;
import com.mcg.entity.auth.UserCacheBean;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Header;
import com.mcg.entity.message.Message;
import com.mcg.util.DateUtils;
import com.mcg.util.FlowInstancesUtils;
import com.mcg.util.Tools;

public class MessagePlugin {

	private static Logger logger = LoggerFactory.getLogger(MessagePlugin.class);
	
	public static boolean push(String mcgWebScoketCode, String httpSessionId, Message message) {
		boolean result = false;
    	UserCacheBean ucb = PermissionCollection.getInstance().getUserCache(httpSessionId);
    	if(ucb == null) {
    		logger.error("websocket推送客户端时，获取用户缓存为null，无法推送消息，推送时间：{}，httpSessionId：{}，推送消息：{}", DateUtils.format(new Date()), httpSessionId, JSON.toJSONString(message));
    	} else {
	    	try {
	    	    Session session = ucb.getUser().getWebSocketMap().get(Tools.genMcgWsConnUniqueId(mcgWebScoketCode, httpSessionId));
	    	    if(session != null && session.isOpen()) {
	    	        session.getBasicRemote().sendText(JSON.toJSONString(message), true);
	    	    }
			} catch (IOException e) {
				if(message != null && message.getBody() != null && message.getBody() instanceof FlowBody) {
					FlowBody flowBody = (FlowBody)message.getBody();
					FlowInstancesUtils.executeStructMap.get(flowBody.getFlowId()).getRunStatus().setInterrupt(true);
				}
				logger.error("webscoket推送客户端消息出错，推送时间：{}，推送消息：{}，异常信息：", DateUtils.format(new Date()), JSON.toJSONString(message), e);
			}
    	}
		return result;
	}
	
	public static boolean pushAll(Message message) {
		boolean result = false;
		try {
			Map<String, UserCacheBean> mapSU = PermissionCollection.getInstance().getMapSU();
			for (UserCacheBean ucb : mapSU.values()) { 
				if(ucb != null) {
					for(String mcgWebScoketCode : ucb.getUser().getWebSocketMap().keySet()) {
						if(!StringUtils.isEmpty(mcgWebScoketCode)) {
							push(mcgWebScoketCode , ucb.getSessionID(), message);
						} else {
							logger.error("webscoket推送消息给所有客户端时，mcgWebScoketCode为空，推送时间：{}，消息数据：{}", DateUtils.format(new Date()), JSON.toJSONString(message));
						}
					}
				}
			}
			result = true;
		} catch (Exception e) {
			logger.error("webscoket推送消息给所有客户端出错，推送时间：{}，消息数据：{}，异常信息：{}", DateUtils.format(new Date()), JSON.toJSONString(message), e);
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