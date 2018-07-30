package com.mcg.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHLinux {

	private static SSHLinux instance;

	private SSHLinux() {

	}

	public static SSHLinux getInstance() {
		if (instance == null) {
			instance = new SSHLinux();
		}
		return instance;
	}
	
	public static String exeCommand(String host, int port, String user, String password, String command) throws JSchException, IOException {

		String result = null;
		ChannelExec channelExec = null;
		Session session = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");				
			session.setPassword(password);
			session.connect();
	
			channelExec = (ChannelExec) session.openChannel("exec");
			InputStream in = channelExec.getInputStream();
			channelExec.setCommand(command);
			channelExec.setErrStream(System.err);
			channelExec.connect();
			result = IOUtils.toString(in, "UTF-8");
		} finally {
			channelExec.disconnect();
			session.disconnect();
		}

		return result;
	}
}
