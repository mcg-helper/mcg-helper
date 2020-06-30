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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;
import com.mcg.common.Constants;
import com.mcg.common.sysenum.WsshOperationEnum;
import com.mcg.entity.global.serversource.ServerSource;
import com.mcg.entity.wssh.SSHConnectInfo;
import com.mcg.entity.wssh.WebSSHData;
import com.mcg.service.FlowService;
import com.mcg.service.WebSSHService;
import com.mcg.util.SSHShellUtil.SSHGoogleAuthUserInfo;
import com.mcg.util.SSHShellUtil.SSHUserInfo;
import com.mcg.util.ThreadPoolUtils;
import com.mcg.util.Tools;

@Service
public class WebSSHServiceImpl implements WebSSHService {
	
	private static Logger logger = LoggerFactory.getLogger(WebSSHServiceImpl.class);
	
	@Autowired
	private FlowService flowService; 

    private static Map<String, SSHConnectInfo> sshMap = new ConcurrentHashMap<>();
    
	@Override
	public void initConnection(Session webSocketSession, String httpSessionId) {

        JSch jSch = new JSch();
        SSHConnectInfo sshConnectInfo = new SSHConnectInfo();
        sshConnectInfo.setjSch(jSch);
        sshConnectInfo.setWebSocketSession(webSocketSession);
        sshMap.put(Tools.genWsshConnUniqueId(httpSessionId, webSocketSession.getId()), sshConnectInfo);
	}

	@Override
	public void recvHandle(String message, Session webSocketSession, String httpSessionId) {
		WebSSHData webSSHData = null;
        try {
        	webSSHData = JSON.parseObject(message, WebSSHData.class);
        } catch (Exception e) {
        	logger.error("接收到wsshWebSocket非法指令:{}, 异常信息:", message, e);
        	return ;
		}
        
        if(WsshOperationEnum.INIT.getValue().equals(webSSHData.getOperation())) {
    		try {
    			List<ServerSource> serverSourceList = flowService.getMcgServerSources();
    			ServerSource curServerSource = null; 
    			if(serverSourceList != null) {
    				for(ServerSource serverSource : serverSourceList) {
    					if(serverSource.getId().equals(webSSHData.getServerSourceId())) {
    						curServerSource = serverSource;
    						break;
    					}
    				}
    			}
    			if(curServerSource != null) {
    				final ServerSource culServerSourceFinal = curServerSource;
    				final WebSSHData webSSHDataFinal = webSSHData;
    				ThreadPoolUtils.WSSH_WORK_EXECUTOR.execute(new Runnable() {
	    	            @Override
	    	            public void run() {
	    	                try {
	    	                    connectToSSH(sshMap.get(Tools.genWsshConnUniqueId(httpSessionId, webSocketSession.getId())), 
	    	                    		webSSHDataFinal, culServerSourceFinal, webSocketSession, httpSessionId);
	    	                } catch (Exception e) {
	    	                    logger.error("webssh连接异常，异常信息:", e);
	    	                    close(webSocketSession, httpSessionId);
	    	                }
	    	            }
	    	        });
    			}
    		} catch (Exception e) {
    			logger.error("初始化wssh失败，异常信息：", e);
    		}
        } else if(WsshOperationEnum.SHELL.getValue().equals(webSSHData.getOperation())) {
        
	        SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(Tools.genWsshConnUniqueId(httpSessionId, webSocketSession.getId()));
	        if (sshConnectInfo != null) {
	            try {
	                transToSSH(sshConnectInfo.getChannel(), webSSHData.getShell());
	            } catch (IOException e) {
	                logger.error("wssh连接出错，异常信息:", e);
	                close(webSocketSession, httpSessionId);
	            }
	        }
        }

	}

	@Override
	public void sendMessage(Session webSocketSession, byte[] buffer, String httpSessionId) throws IOException {
		webSocketSession.getBasicRemote().sendText(new String(buffer, Constants.CHARSET));
	}
	
	@Override
	public void close(Session webSocketSession, String httpSessionId) {
        String userId = httpSessionId;
        SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
        if (sshConnectInfo != null) {
            if (sshConnectInfo.getChannel() != null) sshConnectInfo.getChannel().disconnect();
            sshMap.remove(userId);
        }
	}

    private void connectToSSH(SSHConnectInfo sshConnectInfo, WebSSHData webSSHData, ServerSource serverSource, Session webSocketSession, String httpSessionId) throws JSchException, IOException {
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        com.jcraft.jsch.Session session = sshConnectInfo.getjSch().getSession(serverSource.getUserName(), serverSource.getIp(), serverSource.getPort());
        session.setConfig(config);
		UserInfo ui = null;
		if(StringUtils.isEmpty(serverSource.getSecretKey())) {
			ui = new SSHUserInfo(serverSource.getPwd());
		} else {
			ui = new SSHGoogleAuthUserInfo(serverSource.getSecretKey(), serverSource.getPwd());
		}
		session.setUserInfo(ui);
        session.connect(6000);
        
        
        Channel channel = session.openChannel("shell");
        ((ChannelShell)channel).setPtyType("vt100", webSSHData.getCols(), webSSHData.getRows(), webSSHData.getTerminalWidth(), webSSHData.getTerminalHeight());
        channel.connect(5000);
        sshConnectInfo.setChannel(channel);

        transToSSH(channel, Constants.LINUX_ENTER);

        InputStream inputStream = channel.getInputStream();
        try {
            byte[] buffer = new byte[1024];
            int i = 0;
            while ((i = inputStream.read(buffer)) != -1) {
                sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i), httpSessionId);
            }

        } finally {
            session.disconnect();
            channel.disconnect();
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }
    
    private void transToSSH(Channel channel, String command) throws IOException {
        if (channel != null) {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }
}
