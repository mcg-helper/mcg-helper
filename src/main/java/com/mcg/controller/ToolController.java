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

package com.mcg.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import Utils.SpendTime;
import com.mcg.entity.flow.connector.ConnectorData;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.flow.text.TextCore;
import com.mcg.entity.flow.text.TextProperty;
import com.mcg.plugin.ehcache.CachePlugin;
import com.mcg.plugin.ehcache.ConnectorCache;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mcg.common.Constants;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.controller.base.BaseController;
import com.mcg.entity.common.McgResult;
import com.mcg.entity.message.Message;
import com.mcg.entity.message.NotifyBody;
import com.mcg.plugin.endesign.Des;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.LevelDbUtil;
import com.mcg.util.PageData;

/**
 * DESC : for upload and down
 * DATA : 2019/11/6 16:17
 * AUTHOR : UDEAN
 */
@Controller
@RequestMapping(value="/tool")
public class ToolController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(ToolController.class);
	
    @RequestMapping(value="down", method=RequestMethod.POST)
    public ModelAndView down() {
        ModelAndView modeAndView = new ModelAndView("redirect:/downloadFlow");
    	PageData pd = this.getPageData();
    	String id = null ;
        if(pd != null && (id = pd.getString("flowId")) != null && pd.get("flowName") != null) {
            modeAndView.addObject("flowId", id);
            modeAndView.addObject("fileName", pd.getString("flowName") + Constants.EXTENSION);
        }
        int len =Integer.valueOf( (String)pd.get("length"));
        int i = 1;
        Set<FlowText> set = new HashSet<>();
        while( i <= len ){
        	if(pd.get(Constants.ID_PREX+i) != null) {
				modeAndView.addObject(Constants.ID_PREX + i, pd.getString(Constants.ID_PREX + i));
				FlowText object= CachePlugin.getFlowEntity(id,pd.getString(Constants.ID_PREX+i));
				if(object != null) {
					set.add(object);
				}
        	}
        	i++;
		}
        FileWriter fw;
        BufferedWriter bw;
		SpendTime time = new SpendTime();
		time.clockUp();
		try {
			fw = new FileWriter(new File("E://recovery//persistent.txt"));
			bw = new BufferedWriter(fw);
			if (set.size() != 0) {
				for (FlowText temp : set) {
					TextProperty property = temp.getTextProperty();
					TextCore core = temp.getTextCore();
					bw.write(temp.getTextId());
					bw.newLine();
					bw.write(property.getName() + " " + property.getKey());
					bw.newLine();
					bw.write(core.getSource());
					bw.newLine();
				}
			}
			ConnectController.cachePersistent(bw);
			bw.flush();

			bw.close();
			fw.close();
			time.clockOver();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
        return modeAndView;
    }

	public static void cachePersistent(int a[], BufferedWriter bw) throws Exception {
		boolean failed = false;
		if(a.length == 0)
			return;
		try {
			for (int i : a) {
				ConnectorData data = ConnectorCache.get(i);
				bw.write(data.getSourceId() + " " + data.getTargetId());
				bw.newLine();
				if (!ConnectorCache.removr(i) && !failed) {
					failed = true;
					logger.error("remove connector cache failed");
				}
			}
		} finally {
			if (failed) {
				logger.debug("removing all connector cache");
				ConnectorCache.removeAll();
			}
		}
	}
    
    @RequestMapping(value="downFlowGenFile", method=RequestMethod.POST)
    public ModelAndView downFlowGenFile() {
        ModelAndView modeAndView = new ModelAndView("redirect:/download");
    	PageData pd = this.getPageData();
    	String path = (String)pd.get("path");
    	
        if(StringUtils.isNotEmpty(path)) {
            modeAndView.addObject("filePath", path);
            int pos = path.lastIndexOf(File.separator);
        	String fileName = path.substring(pos+1);
            modeAndView.addObject("fileName", fileName);
            
        }

    	return modeAndView;
    }
    
    @RequestMapping(value="upload", method=RequestMethod.POST)
    @ResponseBody
    public McgResult upload(@RequestParam(value = "flowFile", required = false) MultipartFile file, @RequestParam String flowId, HttpSession session) {
        Message messageComplete = MessagePlugin.getMessage();
        messageComplete.getHeader().setMesType(MessageTypeEnum.NOTIFY);
        NotifyBody notifyBody = new NotifyBody();
    	McgResult result = new McgResult();
    	
    	if(file == null) {
    		logger.error("导入文件丢失");
            result.setStatusCode(0);
            result.setStatusMes("导入文件丢失");
            notifyBody.setContent("导入文件丢失，请刷新页面后重试！");
            notifyBody.setType(LogTypeEnum.ERROR.getValue());    
            messageComplete.setBody(notifyBody);
            MessagePlugin.push(session.getId(), messageComplete);          
            return result;    	    
    	}
    	
    	if(file.getOriginalFilename().endsWith(Constants.EXTENSION)){
    		
	    	File targetFile = new File(Constants.DATA_PATH + flowId + Constants.EXTENSION);
	        try {
	        	if (!targetFile.getParentFile().exists()) {
	        		targetFile.getParentFile().mkdirs();
	        	}  

	        	file.transferTo(targetFile);
	        	byte[] bytes = FileUtils.readFileToByteArray(targetFile);
	        	Des des = new Des(Constants.DES_KEY);
	        	LevelDbUtil.put(flowId.getBytes(Constants.CHARSET), des.decrypt(bytes));
	        	FileUtils.forceDelete(targetFile);
	        	
		        notifyBody.setContent("导入流程文件成功！");
		        notifyBody.setType(LogTypeEnum.SUCCESS.getValue());				
			} catch (Exception e) {
				logger.error("导入流程失败，异常信息：{}", e.getMessage());
				result.setStatusCode(0);
				result.setStatusMes("导入失败");
		        notifyBody.setContent("导入流程文件异常！");
		        notifyBody.setType(LogTypeEnum.ERROR.getValue());				
			}
    	} else {
    		logger.error("流程文件格式无效！文件名：{}", file.getOriginalFilename());
	        notifyBody.setContent("流程文件格式无效！");
	        notifyBody.setType(LogTypeEnum.ERROR.getValue());	    		
    	}

        messageComplete.setBody(notifyBody);
        MessagePlugin.push(session.getId(), messageComplete);          
        return result;
    }
}