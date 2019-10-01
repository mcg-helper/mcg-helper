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

package com.mcg.entity.flow.linux;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class LinuxCore implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "{flowLinux.linuxCore.severSourceId.notBlank}")
    @XmlElement
    private String serverSourceId;
	
	@NotBlank(message = "{flowLinux.linuxCore.connMode.notBlank}")
    @XmlElement
	private String connMode;
	
//	@NotBlank(message = "{flowLinux.linuxCore.source.notBlank}")
	@XmlElement
	private String source;
	@XmlElement
	private String ip;
	@XmlElement
	private String port;
	@XmlElement
	private String user;
	@XmlElement
	private String pwd;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

}
