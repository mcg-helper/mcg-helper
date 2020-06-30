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
	/* 流程id */
	private String flowId;
	/* 流程实例id */
	private String flowInstanceId;
	/* 日志类型  */
	private String logType;
	/* 日志输出类型   param、normal、result、ssh 对应EletypeEnum */
	private String logOutType;
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
	/* 流程执行次序号 */
	private Integer orderNum;
	/* 是否是子流程 */
	private Boolean subFlag;
	private String comment;
	
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	
	public String getFlowInstanceId() {
		return flowInstanceId;
	}
	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}
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
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Boolean getSubFlag() {
		return subFlag;
	}
	public void setSubFlag(Boolean subFlag) {
		this.subFlag = subFlag;
	}
	public String getLogOutType() {
		return logOutType;
	}
	public void setLogOutType(String logOutType) {
		this.logOutType = logOutType;
	}

}