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

package com.mcg.entity.flow.web;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;


public class WebStruct {

    @NotBlank(message = "{webStruct.flowId.NotBlank}")
    private String flowId;
    private List<WebElement> webElement;
    private List<WebConnector> webConnector;
    
    public List<WebElement> getWebElement() {
        return webElement;
    }
    public void setWebElement(List<WebElement> webElement) {
        this.webElement = webElement;
    }
    public List<WebConnector> getWebConnector() {
        return webConnector;
    }
    public void setWebConnector(List<WebConnector> webConnector) {
        this.webConnector = webConnector;
    }
    public String getFlowId() {
        return flowId;
    }
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }
    
}
