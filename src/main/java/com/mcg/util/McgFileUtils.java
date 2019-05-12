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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName:   McgFileUtils   
 * @Description: TODO(文件操作服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:40:40  
 *
 */
public class McgFileUtils {

	private static Logger logger = LoggerFactory.getLogger(McgFileUtils.class);
    /**
     * 
     * @Title:       createDir   
     * @Description: TODO(根据文件路径，创建目录)
     * @param:       @param filePath
     * @param:       @return      
     * @return:      boolean      
     * @throws
     */
    public static boolean createDir(String filePath) {
        boolean result = false;
        File file = new File(filePath);
        if(!file.exists()) {
            result = file.mkdirs();
        } else {
            result = true;
        }
        return result;
    }
    
    public static boolean deleteFile(String path) {
        boolean result = false;
        try {
        	File file = new File(path);
        	if(file.exists())
        		FileUtils.forceDelete(file);
            result = true;
        } catch (IOException e) {
            logger.error("删除文件出错，路径：{}，异常信息：{}", path, e.getMessage());
        }
        return result;
    }	
    
	public static String readFile(String path, String encoding) {
        File file = new File(path);  
        String fileContent = null;  
        try {  
        	if(file.exists()) {
        		fileContent = FileUtils.readFileToString(file, encoding);
        	}
        } catch (IOException e) {  
        	logger.error("读文件出错，路径：{}，encoding：{}，异常信息：{}", path, encoding, e.getMessage());  
        } 		
        return fileContent;
	}
	
    /**
     * 
     * @Title:       copyInputStreamToFile   
     * @Description: TODO(将流写指定文件)   
     * @param:       @param source
     * @param:       @param target
     * @param:       @param fileName
     * @param:       @return      
     * @return:      boolean      
     * @throws
     */
    public static boolean copyInputStreamToFile(InputStream source, String target, String fileName) {
        boolean result = false;
        try {
            FileUtils.copyInputStreamToFile(source, new File(target + File.separator + fileName));
            result = true;
        } catch (IOException e) {
            logger.error("将流写指定文件出错，路径：{}， 异常信息：{}", (target + File.separator + fileName), e.getMessage());
        }
        return result;
    }
    
    /**
     * 
     * @Title:       writeByteArrayToFile   
     * @Description: TODO(将字符串写入指定文件)   
     * @param:       @param target
     * @param:       @param fileName
     * @param:       @param content
     * @param:       @return      
     * @return:      boolean      
     * @throws
     */
    public static boolean writeByteArrayToFile(String target, String fileName, String content, String encoding) {
        boolean result = false;
        try {  
            FileUtils.writeStringToFile(new File(target + fileName), content, encoding);
            result = true;
        } catch (IOException e) {  
            logger.error("将字符串写入指定文件出错，路径：{}，encoding：{}， 数据：{}，异常信息：{}", (target + fileName), encoding, content, e.getMessage());
        }  
        
        return result;
    }
    
    /**
     * 
     * @Title:       cleanDirectory   
     * @Description: TODO(清除目录下的所有文件及目录，但不删除目录)   
     * @param:       @param path
     * @param:       @return      
     * @return:      boolean      
     * @throws
     */
    public static boolean cleanDirectory(String path) {
        boolean result = false;
        try {
            FileUtils.cleanDirectory(new File(path));
            result = true;
        } catch (IOException e) {
            logger.error("清除目录下的所有文件及目录，但不删除目录出错，异常信息：{}", e.getMessage());
        }
        return result;
    }

    /**
     * 
     * @Title:       copyDirectory   
     * @Description: TODO(拷贝目录内容到另一个目录中)   
     * @param:       @param source
     * @param:       @param target
     * @param:       @return      
     * @return:      boolean      
     * @throws
     */
    public static boolean copyDirectory(String source, String target) {
        boolean result = false;
        try {
            FileUtils.copyDirectory(new File(source), new File(target));
            result = true;
        } catch (IOException e) {
            logger.error("拷贝目录内容到另一个目录中出错，源路径：{}，目标路径：{}，异常信息：{}", source, target, e.getMessage());
        }
        return result;
    }
    
    /**
     * 
     * @Title:       downFile   
     * @Description: TODO(下载文件，默认以源文件名作为新文件名)   
     * @param:       @param url   文件的地址
     * @param:       @param target  保存目录地址
     * @param:       @return      
     * @return:      boolean      
     * @throws
     */
    public static boolean downFile(String url, String target) {
        boolean result = false;
        String path = null;
        try {
            InputStream in = new URL(url).openStream();
            byte[] fileData = IOUtils.toByteArray(in);
            path = target + "/" + getFileNameByUrl(url);
            FileUtils.writeByteArrayToFile(new File(path), fileData);
            IOUtils.closeQuietly(in);  
            result = true;
        } catch (IOException e) {
            logger.error("下载文件出错，路径：{}， 异常信息：{}", path, e.getMessage());
        }
        
        return result;
    }

    /**
     * 
     * @Title:       getFileNameByUrl   
     * @Description: TODO(根据下载url地址截取文件名)   
     * @param:       @param url
     * @param:       @return    
     * @return:      String      
     * @throws
     */
    public static String getFileNameByUrl(String url) {
        String fileName = null;
        if(url.lastIndexOf("\\") != -1) {
            url = url.replaceAll("\\\\", "/");
        }
        fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        
        return fileName;
    }
    
    public static String getHtml(String url) {
        String result = null;
        InputStream in = null;
        try {
            in = new URL(url).openStream();
            result = IOUtils.toString( in ); 
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {  
            IOUtils.closeQuietly(in);  
        }  
        return result;
    }
    
    /**
     * 清空文件的数据，但不删除文件
     * @param path
     * @return
     */
    public static boolean clearFileData(String path) {
    	boolean result = false;
    	try {
    		File file = new File(path);
    		if(file.exists()) {
    			FileUtils.write(new File(path), null);
    		}
			result = true;
		} catch (IOException e) {
			logger.error("清空文件的数据，但不删除文件出错，路径：{}，异常信息：{}", path, e.getMessage());
		}
    	return result;
    }
}
