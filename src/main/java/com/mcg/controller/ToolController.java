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

import java.io.File;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
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
 * 
 * @ClassName:   ToolController   
 * @Description: TODO(上传、下载处理) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:28:54  
 *
 */
@Controller
@RequestMapping(value="/tool")
public class ToolController extends BaseController {

    @RequestMapping(value="down", method=RequestMethod.POST)
    public ModelAndView down() {
        ModelAndView modeAndView = new ModelAndView("redirect:/downloadFlow");
    	PageData pd = this.getPageData();
        if(pd != null && pd.get("flowId") != null && pd.get("flowName") != null) {
            modeAndView.addObject("flowId", pd.getString("flowId"));
            modeAndView.addObject("fileName", pd.getString("flowName") + Constants.EXTENSION);
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
				result.setStatusCode(0);
				result.setStatusMes("导入失败");
		        notifyBody.setContent("导入流程文件异常！");
		        notifyBody.setType(LogTypeEnum.ERROR.getValue());				
				e.printStackTrace();
			}
    	} else {
	        notifyBody.setContent("流程文件格式无效！");
	        notifyBody.setType(LogTypeEnum.ERROR.getValue());	    		
    	}

        messageComplete.setBody(notifyBody);
        MessagePlugin.push(session.getId(), messageComplete);          
        return result;
    }
}