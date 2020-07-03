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

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.mcg.common.sysenum.McgWsOperationEnum;
import com.mcg.entity.auth.PermissionCollection;
import com.mcg.entity.auth.UserCacheBean;
import com.mcg.entity.ws.McgWsInfo;
import com.mcg.util.Tools;


@ServerEndpoint(value = "/message", configurator = GetHttpSessionConfigurator.class)
public class McgWebSocket {
	
	private static Logger logger = LoggerFactory.getLogger(McgWebSocket.class);
	private String httpSessionId;
	
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {

    	if(httpSessionId != null) {
	    	logger.debug("接收数据：{}, sessionId:{}", message, session.getId());
	    	McgWsInfo mcgWsInfo = null;
	    	try {
	    		mcgWsInfo = JSON.parseObject(message, McgWsInfo.class);
	    	} catch (Exception e) {
	    		logger.error("mcgWebSocket接收到非法指令:{}, 异常信息:", message, e);
	    		return ;
			}
	    	if(McgWsOperationEnum.INIT.getValue().equals(mcgWsInfo.getOperation())) {
	        	UserCacheBean ucb = PermissionCollection.getInstance().getUserCache(httpSessionId);
	        	if(ucb != null && ucb.getUser() != null) {
	        		ucb.getUser().getWebSocketMap().put(Tools.genMcgWsConnUniqueId(mcgWsInfo.getMcgWebScoketCode() , httpSessionId), session);
	        	}
	    	}
    	}
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
    	HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
    	if(httpSession != null) {
    		httpSessionId = httpSession.getId();
    	}
    }

    @OnClose
    public void onClose() {
    	logger.debug("关闭一个websocket通道成功");
    }
    
    @OnError 
    public void onerror(Session session, Throwable throwable) { 
    	logger.error("非法关闭一个websocket通道成功, httpSessionId:{}, webSocketSessionId:{}, 异常信息:", httpSessionId, session.getId() , throwable);
    }
}