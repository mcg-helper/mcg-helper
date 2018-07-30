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

package com.mcg.entity.message;

public class FlowBody extends Body {

	private static final long serialVersionUID = -8342298153553459856L;
	/* 日志类型  */
	private String logType;
	/* 日志类型说明  */
	private String logTypeDesc;
	/* 控件类型  */
	private String eleType;
	/* 控件类型说明  */
	private String eleTypeDesc;
	/* 信息  */
	private String content;
	/* 控件的id  */
	private String eleId;
	private String comment;
	
	public String getLogType() {
        return logType;
    }
    public void setLogType(String logType) {
        this.logType = logType;
    }
    public String getLogTypeDesc() {
        return logTypeDesc;
    }
    public void setLogTypeDesc(String logTypeDesc) {
        this.logTypeDesc = logTypeDesc;
    }
    public String getEleType() {
        return eleType;
    }
    public void setEleType(String eleType) {
        this.eleType = eleType;
    }
    public String getEleTypeDesc() {
        return eleTypeDesc;
    }
    public void setEleTypeDesc(String eleTypeDesc) {
        this.eleTypeDesc = eleTypeDesc;
    }
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEleId() {
		return eleId;
	}
	public void setEleId(String eleId) {
		this.eleId = eleId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

}