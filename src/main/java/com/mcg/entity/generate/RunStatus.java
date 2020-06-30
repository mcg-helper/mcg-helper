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

package com.mcg.entity.generate;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.mcg.entity.common.LoopStatus;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class RunStatus implements Serializable {

    private static final long serialVersionUID = -7093061150060549223L;

    /* success:成功  exception:失败*/
    @XmlElement
    private String code;
    /* 当前执行控件的ID */
    @XmlElement
    private String executeId;
    /* 中断流程执行信号  true:是  false：否 */
    @XmlElement
    private boolean isInterrupt;
    /* 当前流程实例的文本控件输出的文件路径，key为控件ID，value为文件路径 */
    @XmlElement
    private ConcurrentHashMap<String, String> availableFileMap = new ConcurrentHashMap<String, String>();
    /* 当前执行流程实例的所有循环控件执行的剩余次数，key为控件ID，value为循环控件执行的状态 */
    @XmlElement
    private ConcurrentHashMap<String, LoopStatus> loopStatusMap = new ConcurrentHashMap<String, LoopStatus>();
    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getExecuteId() {
        return executeId;
    }
    public void setExecuteId(String executeId) {
        this.executeId = executeId;
    }
	public ConcurrentHashMap<String, String> getAvailableFileMap() {
		return availableFileMap;
	}
	public void setAvailableFileMap(ConcurrentHashMap<String, String> availableFileMap) {
		this.availableFileMap = availableFileMap;
	}
	public ConcurrentHashMap<String, LoopStatus> getLoopStatusMap() {
		return loopStatusMap;
	}
	public void setLoopStatusMap(ConcurrentHashMap<String, LoopStatus> loopStatusMap) {
		this.loopStatusMap = loopStatusMap;
	}
	public boolean isInterrupt() {
		return isInterrupt;
	}
	public void setInterrupt(boolean isInterrupt) {
		this.isInterrupt = isInterrupt;
	}

    
}
