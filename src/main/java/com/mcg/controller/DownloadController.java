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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mcg.common.Constants;
import com.mcg.plugin.endesign.Des;
import com.mcg.util.LevelDbUtil;

/**
 * 
 * @ClassName:   DownloadController   
 * @Description: TODO(下载功能) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:02:39  
 *
 */
@Controller
public class DownloadController {

	private static Logger logger = LoggerFactory.getLogger(DownloadController.class);
	
	@RequestMapping("download")
	public String download(String filePath, String fileName, HttpServletRequest request, HttpServletResponse response) {
	    
		response.setCharacterEncoding(Constants.CHARSET.toString());
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + convertCharacterEncoding(request, fileName));
		OutputStream os = null;
		InputStream inputStream = null;
        try {
            os = response.getOutputStream();
            inputStream = new FileInputStream(new File(filePath));            
  
            byte[] b = new byte[1024];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
                        
        }catch (IOException e) {
        	logger.error("文件下载出错，异常信息：{}", e.getMessage());
        }finally {
            try {
            	if(os != null) {
            		os.close();
            	}
            	if(inputStream != null) {
            		inputStream.close();
            	}
            } catch (IOException e) {
            	logger.error("文件下载后关闭流出错，异常信息：{}", e.getMessage());
            }
        }
        
		return null;
	}
	
    @RequestMapping("downloadFlow")
    public String downloadFlow(String flowId, String fileName, HttpServletRequest request, HttpServletResponse response) {
        
        response.setCharacterEncoding(Constants.CHARSET.toString());
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + convertCharacterEncoding(request, fileName));
        OutputStream os = null;
        
        try {
            os = response.getOutputStream();
            Des des = new Des(Constants.DES_KEY);
            byte[] bytes = des.encrypt(LevelDbUtil.get(flowId.getBytes(Constants.CHARSET)));
            os.write(bytes);
            os.close();
        }catch (Exception e) {
        	logger.error("导入流程文件出错，异常信息：{}", e.getMessage());
        }finally {
            try {
                if(os != null) {
                    os.close();
                }
            } catch (IOException e) {
            	logger.error("导入流程文件后关闭流出错，异常信息：{}", e.getMessage());
            }
        }
        
        return null;
    }	
	
	public String convertCharacterEncoding(HttpServletRequest request, String fileName) {
	     String agent = request.getHeader("USER-AGENT").toLowerCase();
	     //根据浏览器类型处理文件名称
	     if(agent.indexOf("msie")>-1){
	         try {
				fileName = java.net.URLEncoder.encode(fileName, Constants.CHARSET.toString());
			} catch (UnsupportedEncodingException e) {
				logger.error("设置浏览器下载文件名的编码为utf-8，异常信息：{}", e.getMessage());
			}
	     }else{
	         try {
				fileName = new String(fileName.getBytes(Constants.CHARSET), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				logger.error("设置浏览器下载文件名的编码为ISO8859-1，异常信息：{}", e.getMessage());
			}
	     }	
	     return fileName;
	}
	
}