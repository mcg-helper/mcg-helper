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

package com.mcg.entity.flow.sftp;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class SftpProperty implements Serializable {

	private static final long serialVersionUID = 1L;
	@NotBlank(message = "{flowSftp.sftpProperty.key.notBlank}")
	@XmlElement
	private String key;
	@NotBlank(message = "{flowSftp.sftpProperty.name.notBlank}")
	@XmlElement
	private String name;
	
	@NotBlank(message = "{flowSftp.sftpProperty.severSourceId.notBlank}")
    @XmlElement
    private String serverSourceId;
	
	@NotBlank(message = "{flowSftp.sftpProperty.connMode.notBlank}")
    @XmlElement
	private String connMode;
	
	@XmlElement
	private String ip;
	@XmlElement
	private String port;
	@XmlElement
	private String user;
	@XmlElement
	private String pwd;
	
	@XmlElement
	private String uploadPath;
	
	@XmlElement
	private String sourceFilePath;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServerSourceId() {
		return serverSourceId;
	}

	public void setServerSourceId(String serverSourceId) {
		this.serverSourceId = serverSourceId;
	}

	public String getConnMode() {
		return connMode;
	}

	public void setConnMode(String connMode) {
		this.connMode = connMode;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}
	
}
