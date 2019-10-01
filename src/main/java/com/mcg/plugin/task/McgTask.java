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

package com.mcg.plugin.task;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.mcg.common.Constants;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.common.sysenum.WontonStateEnum;
import com.mcg.entity.auth.PermissionCollection;
import com.mcg.entity.auth.UserCacheBean;
import com.mcg.entity.global.wonton.WontonData;
import com.mcg.entity.message.Message;
import com.mcg.entity.message.WontonHeartBody;
import com.mcg.entity.wonton.WontonHeart;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DateUtils;
import com.mcg.util.LevelDbUtil;

public class McgTask {
	
	private static Logger logger = LoggerFactory.getLogger(McgTask.class);
	public final static BlockingQueue<WontonHeart> wontonHeartCacheData = new LinkedBlockingQueue<>(500);
	private final static ScheduledExecutorService MCG_POOL = createScheduledExecutor();
	public static ConcurrentHashMap<String, WontonHeart> wontonInstanceMap = new ConcurrentHashMap<>();
	
    public final static ScheduledExecutorService getScheduledExecutor() {
        return MCG_POOL;
    }	
	
    public static synchronized void init() {
    	try {
			WontonData wontonData = (WontonData)LevelDbUtil.getObject(Constants.WONTON_KEY, WontonData.class);
			if(wontonData != null) {
				wontonInstanceMap = wontonData.getWontonHeartMap();
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
    	
    	wontonHeartMonitor();
    	checkWebSession();
    }
    
    /**
     * 定时检查websocket session活跃状态，非活跃状态时清除用户缓存
     */
    private static void checkWebSession() {
		MCG_POOL.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
            	Map<String, UserCacheBean> mapSU = PermissionCollection.getInstance().getMapSU();
            	if(mapSU != null && mapSU.size() > 0) {
                	Set<String> keys = mapSU.keySet();
                	for(String key : keys) {
                		UserCacheBean ucb = mapSU.get(key);
                		Session session = ucb.getUser().getSession();
                		if(!session.isOpen()) {
                			mapSU.remove(ucb.getSessionID());
                		}
                	}
            	}
            }
		}, 20L, 5L, TimeUnit.MINUTES);
    }
    
    private static void wontonHeartMonitor() {
    	/* 混沌客户端心跳处理和存活检测 */
		MCG_POOL.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                	
                	if (wontonHeartCacheData.size() > 0) {
                		int size = 1000;
                		LinkedBlockingQueue<WontonHeart> list = new LinkedBlockingQueue<>(size);
                        wontonHeartCacheData.drainTo(list, size);    
                        if (list.size() > 0){
                        	for(WontonHeart wontonHeart : list) {
                        		wontonInstanceMap.put(wontonHeart.getInstancecode(), wontonHeart);
                        	}
                        }
                	}
                	
                	if(wontonInstanceMap.size() > 0) {
	                	for(WontonHeart wontonHeart : wontonInstanceMap.values()) {
	                		Socket socket = new Socket();
	                		try {
	                			String[] ipAndPort = wontonHeart.getInstancecode().split(":");
	                			InetAddress address = InetAddress.getByName(ipAndPort[0]);
	                			int timeOut = 2000;
	                			
	                	        SocketAddress socketAddress = new InetSocketAddress(address, Integer.valueOf(ipAndPort[1]));
	                	        socket.connect(socketAddress, timeOut);
	                	        wontonHeart.setState(WontonStateEnum.NORMAL.getValue());
	                		} catch (Exception e) {
	                			logger.debug("混沌客户端存活连接检测失败，检测时间：{}，心跳数据：{}，异常信息：{}", DateUtils.format(new Date()), JSON.toJSONString(wontonHeart), e.getMessage());
	                			wontonHeart.setState(WontonStateEnum.LOSED.getValue());
	                		} finally {
	                			socket.close();
							}
	                		
	                	}
		                	
	                	WontonHeartBody wontonHeartBody = new WontonHeartBody();
	                	wontonHeartBody.setDataList(new ArrayList<WontonHeart>(wontonInstanceMap.values()));
	                    Message message = MessagePlugin.getMessage();
	                    message.getHeader().setMesType(MessageTypeEnum.WONTON);
	                    
	                    message.setBody(wontonHeartBody);
	                    MessagePlugin.pushAll(message);	                	
                	}

                } catch (Throwable e) {
                	logger.error("混沌客户端心跳处理和存活检测任务失败，检测时间：{}，异常信息：{}", DateUtils.format(new Date()), e.getMessage());
                }
            }
        }, 30L, 20L, TimeUnit.SECONDS);
		
		//定时将wonton客户端数据落盘
		MCG_POOL.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
            	saveWontonInstance();
            }
		}, 20L, 60L, TimeUnit.SECONDS);
    }	
	
    private static void saveWontonInstance() {
        try {
        	WontonData wontonData = (WontonData)LevelDbUtil.getObject(Constants.WONTON_KEY, WontonData.class);
        	if(wontonData == null) {
        		wontonData = new WontonData();
        	}
        	wontonData.setWontonHeartMap(wontonInstanceMap);
        	LevelDbUtil.putObject(Constants.WONTON_KEY, wontonData);
        } catch (Exception e) {
			logger.error("定时保存混沌客户端实例数据出错：{}", e.getMessage());
		}
    }
    
    private final static ScheduledExecutorService createScheduledExecutor() {

        final ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(4, new WontonThreadFactory("wonton-monitor", true));

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                pool.shutdown();
            }
        }, "mcg-task-ShutdownHook"));
        return pool;

    }	
}
