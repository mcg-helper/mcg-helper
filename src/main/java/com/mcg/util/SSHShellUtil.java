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

package com.mcg.util;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.mcg.common.Constants;
import com.mcg.plugin.task.McgTask;

public class SSHShellUtil {

	public static String execute(String ip, int port, String userName, String password, String shell) throws JSchException, IOException {
		String response = null;
		JSch.setLogger(new ShellLogger());
		JSch jsch = new JSch();
		Session session = jsch.getSession(userName, ip, port);
		UserInfo ui = new SSHUserInfo(password);
		session.setUserInfo(ui);
		session.connect();

		Channel channel = session.openChannel("shell");
		PipedInputStream pipedInputStream = new PipedInputStream();
		PipedOutputStream pipedOutputStream = new PipedOutputStream();
		pipedOutputStream.connect(pipedInputStream);
		
		Thread thread = new Thread(new MonitorShellUser(channel, shell, pipedOutputStream));
		thread.start();
		
		channel.setInputStream(pipedInputStream);
		
		PipedOutputStream shellPipedOutputStream = new PipedOutputStream();
		PipedInputStream receiveStream = new PipedInputStream(); 
		shellPipedOutputStream.connect(receiveStream);
		
		channel.setOutputStream(shellPipedOutputStream);
		((ChannelShell)channel).setPtyType("dumb", 160, 24, 1000, 480);
		//((ChannelShell)channel).setTerminalMode("binary".getBytes());
	//	((ChannelShell)channel).setEnv("LANG", "zh_CN.UTF-8");
		try {
			channel.connect();
			response = IOUtils.toString(receiveStream, "UTF-8");
		}finally {
//			if(channel.isClosed()) {
				pipedOutputStream.close();
				pipedInputStream.close();
				shellPipedOutputStream.close();
				receiveStream.close();
				channel.disconnect();
				session.disconnect();
			}
//		}
			
		return response;
	}
	
	public static class ShellLogger implements com.jcraft.jsch.Logger {
		private static Logger shellLogger = LoggerFactory.getLogger(ShellLogger.class);
		static ConcurrentHashMap<Integer, String> levelMap = new ConcurrentHashMap<>();
		static {
			levelMap.put(new Integer(DEBUG), "DEBUG: ");
			levelMap.put(new Integer(INFO), "INFO: ");
			levelMap.put(new Integer(WARN), "WARN: ");
			levelMap.put(new Integer(ERROR), "ERROR: ");
			levelMap.put(new Integer(FATAL), "FATAL: ");
		}
		
		@Override
		public boolean isEnabled(int level) {
			return true;
		}

		@Override
		public void log(int level, String message) {
			shellLogger.debug("level:{}, message:{}", levelMap.get(new Integer(level)), message);
		}

	}
	
	public static class SSHUserInfo implements UserInfo {
		private static Logger sshUserInfoLogger = LoggerFactory.getLogger(SSHUserInfo.class);
		String password;
		public SSHUserInfo(String password) {
			this.password = password;
		}
		public String getPassword() {
			return password;
		}

		public boolean promptYesNo(String str) {
			return true;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			sshUserInfoLogger.debug("promptPassphrase={}", message);
			return true;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		public void showMessage(String message) {
			sshUserInfoLogger.debug("showMessage={}", message);
		}

	}	
	
}

class MonitorShellUser implements Runnable {

	private static Logger monitorShellUserLogger = LoggerFactory.getLogger(McgTask.class);
	private String shell;
	private PipedOutputStream pipedOutputStream;
	private Channel channel;
	
	public MonitorShellUser(Channel channel, String shell, PipedOutputStream pipedOutputStream) {
		this.channel = channel;
		this.shell = shell;
		this.pipedOutputStream = pipedOutputStream;
	}
	
	@Override
	public void run() {
		
		try {
			String[] commands = shell.split(Constants.LINUX_ENTER);
			for(String command : commands) {
				command = command.trim();
				if(command.startsWith(Constants.LINUX_INTERACT)) {
					try {
						String sleepTime = command.substring(command.indexOf(Constants.LINUX_INTERACT) + Constants.LINUX_INTERACT.length()).trim();
						if(StringUtils.isEmpty(sleepTime)) {
							Thread.sleep(Constants.DEFAULT_TIME);
						} else {
							Thread.sleep(Integer.parseInt(sleepTime));
						}
						
						continue;
					} catch (Exception e) {
						monitorShellUserLogger.error("执行linux命令失败，命令：{}，异常信息：{}", command, e.getMessage());
						pipedOutputStream.write((command + Constants.LINUX_ENTER).getBytes());
						break;
					}
					
				}
				pipedOutputStream.write((command + Constants.LINUX_ENTER).getBytes());
			}

			while (!channel.isClosed()) {
				this.pipedOutputStream.write((Constants.LINUX_EOF + Constants.LINUX_ENTER).getBytes());
				Thread.sleep(500);
			}
			
			pipedOutputStream.flush();

		} catch (Exception e) {
			monitorShellUserLogger.error("执行shell出错，异常信息：{}", e.getMessage());
		} finally {
			try {
				pipedOutputStream.close();
			} catch (IOException e) {
				monitorShellUserLogger.error(e.getMessage());
			}
		}

		
	}
}