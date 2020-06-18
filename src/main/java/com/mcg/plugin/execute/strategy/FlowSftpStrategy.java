package com.mcg.plugin.execute.strategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;
import com.mcg.common.Constants;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.FlowLinuxConnModeEnum;
import com.mcg.common.sysenum.LogOutTypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.sftp.FlowSftp;
import com.mcg.entity.flow.sftp.SftpUploadRecord;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.global.McgGlobal;
import com.mcg.entity.global.serversource.ServerSource;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.tplengine.FreeMakerTpLan;
import com.mcg.plugin.tplengine.TplEngine;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;
import com.mcg.util.LevelDbUtil;
import com.mcg.util.SSHShellUtil.SSHGoogleAuthUserInfo;
import com.mcg.util.SSHShellUtil.SSHUserInfo;

public class FlowSftpStrategy implements ProcessStrategy {

	private static Logger logger = LoggerFactory.getLogger(FlowSftpStrategy.class);
	
	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowSftp flowSftp = (FlowSftp)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowSftp.getId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowSftp flowSftp = (FlowSftp)mcgProduct;
		JSON parentParam = DataConverter.getParentRunResult(flowSftp.getId(), executeStruct);
		JSON allParam = DataConverter.addFlowStartRunResult(parentParam, executeStruct);
		flowSftp = DataConverter.flowOjbectRepalceGlobal(allParam, flowSftp);
		RunResult runResult = new RunResult();
       
		Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();
        flowBody.setSubFlag(executeStruct.getSubFlag());
        flowBody.setFlowId(flowSftp.getFlowId());
        flowBody.setOrderNum(flowSftp.getOrderNum());
        flowBody.setLogOutType(LogOutTypeEnum.PARAM.getValue());
        flowBody.setEleType(EletypeEnum.SFTP.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.SFTP.getName() + "--》" + flowSftp.getSftpProperty().getName());
        flowBody.setEleId(flowSftp.getId());
        flowBody.setComment("参数");
        if(parentParam == null) {
            flowBody.setContent("{}");
        } else {
            flowBody.setContent(JSON.toJSONString(parentParam, true));
        }
        flowBody.setLogType(LogTypeEnum.INFO.getValue());
        flowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
        message.setBody(flowBody);
        MessagePlugin.push(flowSftp.getMcgWebScoketCode(), executeStruct.getSession().getId(), message);
        
        ServerSource serverSource = null;
        if(FlowLinuxConnModeEnum.DEPENDENCY.getValue().equals(flowSftp.getSftpProperty().getConnMode())) {
            McgGlobal mcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
            
            if(mcgGlobal != null && CollectionUtils.isNotEmpty(mcgGlobal.getServerSources())) {
            	for(ServerSource source : mcgGlobal.getServerSources()) {
            		if(flowSftp.getSftpProperty().getServerSourceId().equals(source.getId())) {
            			serverSource = source;
            			break;
            		}
            	}
            }
        } else if(FlowLinuxConnModeEnum.ASSIGN.getValue().equals(flowSftp.getSftpProperty().getConnMode())) {
        	serverSource = new ServerSource();
        	TplEngine tplEngine = new TplEngine(new FreeMakerTpLan());
        	String ip = tplEngine.generate(allParam, flowSftp.getSftpProperty().getIp());
        	serverSource.setIp(ip);
        	String port = tplEngine.generate(allParam, flowSftp.getSftpProperty().getPort());
        	serverSource.setPort(Integer.valueOf(port));
        	String user = tplEngine.generate(allParam, flowSftp.getSftpProperty().getUser());
        	serverSource.setUserName(user);
        	String pwd = tplEngine.generate(allParam, flowSftp.getSftpProperty().getPwd());
        	serverSource.setPwd(pwd);
        }
        
		Session session = null;
		Channel channel = null; 
	    
	    try {  
	        JSch jsch = new JSch();
	        
	        session = jsch.getSession(serverSource.getUserName(), serverSource.getIp(), serverSource.getPort());
			UserInfo ui = null;
			if(StringUtils.isEmpty(serverSource.getSecretKey())) {
				ui = new SSHUserInfo(serverSource.getPwd());
			} else {
				ui = new SSHGoogleAuthUserInfo(serverSource.getSecretKey(), serverSource.getPwd());
			}
			session.setUserInfo(ui);
			
	        Properties config = new Properties();  
	        config.put("StrictHostKeyChecking", "no");  
	        session.setConfig(config); 
	        session.connect(6000);
	        
	        channel = (Channel) session.openChannel("sftp");
	        channel.connect(10000);  
	        ChannelSftp sftp = (ChannelSftp) channel;  
	        if(flowSftp.getSftpUpload() != null && flowSftp.getSftpUpload().getSftpUploadRecords() != null 
	        		&& flowSftp.getSftpUpload().getSftpUploadRecords().size() > 0) {
		        for(SftpUploadRecord sftpUploadRecord : flowSftp.getSftpUpload().getSftpUploadRecords()) {
		        	
		        	uploadFile(sftp, sftpUploadRecord);
		        	
		            Message shellMessage = MessagePlugin.getMessage();
		            shellMessage.getHeader().setMesType(MessageTypeEnum.FLOW);
		            FlowBody shellFlowBody = new FlowBody();
		            shellFlowBody.setEleType(EletypeEnum.SFTP.getValue());
		            shellFlowBody.setEleTypeDesc(EletypeEnum.SFTP.getName() + "--》" + flowSftp.getSftpProperty().getName());
		            shellFlowBody.setEleId(flowSftp.getId());
		            shellFlowBody.setComment("上传文件完成");
		            shellFlowBody.setContent("上传服务器路径：" + sftpUploadRecord.getUploadPath() + "   源文件路径：" + sftpUploadRecord.getFilePath()); 
		            shellFlowBody.setLogType(LogTypeEnum.INFO.getValue());
		            shellFlowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
		            shellMessage.setBody(shellFlowBody);
		            MessagePlugin.push(flowSftp.getMcgWebScoketCode(), executeStruct.getSession().getId(), shellMessage);
		        }
	        }
	    } finally {
			if(session != null) {
	            session.disconnect();
			}
			if(channel != null) {
	            channel.disconnect();
			}
		}
        
		
		runResult.setElementId(flowSftp.getId());
		
		JSONObject runResultJson = (JSONObject)parentParam;
		runResultJson.put(flowSftp.getSftpProperty().getKey(), flowSftp.getSftpUpload().getSftpUploadRecords());
		runResult.setJsonVar(JSON.toJSONString(runResultJson, true));
		
		executeStruct.getRunStatus().setCode("success");
		
		logger.debug("Linux控件：{}，执行完毕！执行状态：{}", JSON.toJSONString(flowSftp), JSON.toJSONString(executeStruct.getRunStatus()));
		return runResult;
	}
	
	private boolean uploadFile(ChannelSftp sftp, SftpUploadRecord sftpUploadRecord) throws JSchException, SftpException, FileNotFoundException {
		boolean result = false;
		
	    InputStream instream = null;
	    try {  
	        sftp.cd(sftpUploadRecord.getUploadPath());   //进入服务器指定的文件夹
	        File uploadFile = new File(sftpUploadRecord.getFilePath());
	        instream = new FileInputStream(uploadFile);  
	        sftp.put(instream, uploadFile.getName());
	    } finally {  
	        try {
	            if(instream != null) {
				    instream.close();
	            }
			} catch (IOException e) {
				logger.error("sftp控件上传文件失败：", e);
			}  	
	    }
		return result;
	}

}
