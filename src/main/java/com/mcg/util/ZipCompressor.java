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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcg.controller.ToolController;

public class ZipCompressor {
	
	private static Logger logger = LoggerFactory.getLogger(ToolController.class);
	
	private static final int BUFFER = 1024;

	private File zipFile;

	public ZipCompressor(String path, String fileName) {
		try {
			zipFile = new File(path + File.separator + fileName);
			if(!zipFile.getParentFile().exists()) {
				zipFile.getParentFile().mkdirs();
			}
			if(!zipFile.exists()) {
				zipFile.createNewFile();
			}
		} catch(Exception e) {
			logger.error("压缩目录生成出错：", e);
		}
	}
	
	public void compress(List<String> filePathList) {
		ZipOutputStream out = null;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
			out = new ZipOutputStream(cos);
			for (int i = 0; i < filePathList.size(); i++) {
				int endPos = Math.max(filePathList.get(i).lastIndexOf("/") + 1, filePathList.get(i).lastIndexOf("\\") + 1);
				String basedir = filePathList.get(i).substring(0, endPos);
				compress(new File(filePathList.get(i)), out, basedir);
			}
			
		} catch (Exception e) {
			logger.error("压缩文件出错：", e);
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("关闭输出流出错：", e);
				}
			}
		}
	}

	public void compress(String srcPathName) {
		File file = new File(srcPathName);
		if (!file.exists()) {
			logger.error("压缩文件不存在：{}", srcPathName);
		}
		CheckedOutputStream cos = null;
		ZipOutputStream out = null;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			cos = new CheckedOutputStream(fileOutputStream, new CRC32());
			out = new ZipOutputStream(cos);
			String basedir = "";
			compress(file, out, basedir);
			
		} catch (Exception e) {
			logger.error("压缩文件出错：", e);
		} finally {
			try {
				if(out != null) {
					out.close();
				}
				if(cos != null) {
					cos.close();
				}
			} catch (IOException e) {
				logger.error("关闭流出错：", e);
			}
		}
	}

	private void compress(File file, ZipOutputStream out, String basedir) {
		if (file.isDirectory()) {
			this.compressDirectory(file, out, basedir);
		} else {
			this.compressFile(file, out, basedir);
		}
	}

	private void compressDirectory(File dir, ZipOutputStream out, String basedir) {
		if (!dir.exists())
			return;

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			compress(files[i], out, basedir + dir.getName() + "/");
		}
	}

	private void compressFile(File file, ZipOutputStream out, String basedir) {
		if (!file.exists()) {
			return;
		}
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			
		} catch (Exception e) {
			logger.error("压缩文件出错：", e);
		} finally {
			if(bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					logger.error("关闭流出错：", e);
				}
			}
		}
	}
	
}