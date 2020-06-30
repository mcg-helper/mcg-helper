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

package com.mcg.entity.common;

import java.util.HashMap;
import java.util.Map;


public class McgResult {

    //状态码     1：成功          0：失败
    private Integer statusCode = 1;
    //执行结果消息
    private String statusMes = "执行成功";
    //自定义属性
    private Map<String, Object> resultMap = new HashMap<String, Object>();
    
	public McgResult addAttribute(String attributeName, Object attributeValue) {
		resultMap.put(attributeName, attributeValue);
		return this;
	}
    
    public Integer getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
    public String getStatusMes() {
        return statusMes;
    }
    public void setStatusMes(String statusMes) {
        this.statusMes = statusMes;
    }

	public Map<String, Object> getResultMap() {
		return resultMap;
	}
    
}
