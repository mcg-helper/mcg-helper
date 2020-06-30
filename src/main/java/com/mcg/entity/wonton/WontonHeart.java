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

package com.mcg.entity.wonton;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class WontonHeart {

	/**
	 * 主机
	 */
	@NotBlank(message = "{wonton.heart.host.NotBlank}")
    @XmlElement	
	private String host;
	/**
	 * 进程pid
	 */
	@NotNull(message = "{wonton.heart.pid.NotNull}")
	@XmlElement
	private Integer pid;
	/**
	 * 启动时间
	 */
	@NotNull(message = "{wonton.heart.startTime.NotNull}")
	@XmlElement
	private Long startTime;
	/**
	 * 客户端心跳时间
	 */
	@NotNull(message = "{wonton.heart.time.NotNull}")
	@XmlElement
	private Long time;
	/**
	 * 实例编码
	 */
	@NotBlank(message = "{wonton.heart.instancecode.NotBlank}")
	@XmlElement
	private String instancecode;
	/**
	 * 版本号
	 */
	@NotBlank(message = "{wonton.heart.version.NotBlank}")
	@XmlElement
	private String version;
	
	private String state;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getInstancecode() {
		return instancecode;
	}

	public void setInstancecode(String instancecode) {
		this.instancecode = instancecode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
