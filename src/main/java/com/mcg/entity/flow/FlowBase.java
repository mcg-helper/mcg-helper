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

package com.mcg.entity.flow;

import java.util.Date;

import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.plugin.build.McgProduct;
public abstract class FlowBase extends McgProduct {
    
    private static final long serialVersionUID = -7660980819340679924L;
    private String mcgWebScoketCode;
    /* 流程实例ID */
    private String flowId;
    /* 执行的次序号 */
    private Integer orderNum;
    private String id;
    /* 控件名称 */
    private String name;
    private String label;
    private String width;
    private String height;
    private String classname;
    private String eletype;
    private String clone;
    private String left;
    private String top;
    private String sign;
    private EletypeEnum eletypeEnum;
    private Date startTime;
    private Date endTime;
    
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getWidth() {
        return width;
    }
    public void setWidth(String width) {
        this.width = width;
    }
    public String getHeight() {
        return height;
    }
    public void setHeight(String height) {
        this.height = height;
    }
    public String getClassname() {
        return classname;
    }
    public void setClassname(String classname) {
        this.classname = classname;
    }
    public String getEletype() {
        return eletype;
    }
    public void setEletype(String eletype) {
        this.eletype = eletype;
    }
    public String getClone() {
        return clone;
    }
    public void setClone(String clone) {
        this.clone = clone;
    }
    public String getLeft() {
        return left;
    }
    public void setLeft(String left) {
        this.left = left;
    }
    public String getTop() {
        return top;
    }
    public void setTop(String top) {
        this.top = top;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getMcgWebScoketCode() {
		return mcgWebScoketCode;
	}
	public void setMcgWebScoketCode(String mcgWebScoketCode) {
		this.mcgWebScoketCode = mcgWebScoketCode;
	}
	public EletypeEnum getEletypeEnum() {
		return eletypeEnum;
	}
	public void setEletypeEnum(EletypeEnum eletypeEnum) {
		this.eletypeEnum = eletypeEnum;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
    
}
