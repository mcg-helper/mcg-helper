package com.mcg.util;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.mcg.common.Constants;

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
		
		Thread thread = new Thread(new MonitorShellUser(shell, pipedOutputStream));
		thread.start();
		
		channel.setInputStream(pipedInputStream);
		
		PipedOutputStream shellPipedOutputStream = new PipedOutputStream();
		PipedInputStream receiveStream = new PipedInputStream(); 
		shellPipedOutputStream.connect(receiveStream);
		
		channel.setOutputStream(shellPipedOutputStream);
		((ChannelShell)channel).setEnv("LANG", "zh_CN.UTF-8");
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
			System.out.print(levelMap.get(new Integer(level)));
			System.out.println(message);
		}

	}
	
	public static class SSHUserInfo implements UserInfo {
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
			System.out.println(message);
			return true;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		public void showMessage(String message) {
			System.out.println("showMessage=" + message);
		}

	}	
	
}

class MonitorShellUser implements Runnable {

	private String shell;
	private PipedOutputStream pipedOutputStream;
	
	public MonitorShellUser(String shell, PipedOutputStream pipedOutputStream) {
		this.shell = shell;
		this.pipedOutputStream = pipedOutputStream;
	}
	
	@Override
	public void run() {
		
		try {
			String[] commands = shell.split(Constants.LINUX_ENTER);
			for(String command : commands) {
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
						pipedOutputStream.write((command + Constants.LINUX_ENTER).getBytes());
						break;
					}
					
				}
				pipedOutputStream.write((command + Constants.LINUX_ENTER).getBytes());
			}
			
			for(int i=0; i<5; i++) {
				pipedOutputStream.write((Constants.LINUX_EOF + Constants.LINUX_ENTER).getBytes());
			}
			pipedOutputStream.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				pipedOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		
	}
}