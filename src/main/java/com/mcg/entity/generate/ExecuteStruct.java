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
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.mcg.plugin.build.McgProduct;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class ExecuteStruct implements Serializable {

    private static final long serialVersionUID = -677740433314925325L;
    @XmlElement
    private Orders orders;
    @XmlElement
    private Map<String, McgProduct> dataMap;
    @XmlElement
    private RunStatus runStatus;
    @XmlElement
    private Map<String, RunResult> runResultMap;
    
    public Orders getOrders() {
        return orders;
    }
    public void setOrders(Orders orders) {
        this.orders = orders;
    }
    public RunStatus getRunStatus() {
        return runStatus;
    }
    public void setRunStatus(RunStatus runStatus) {
        this.runStatus = runStatus;
    }
    public Map<String, RunResult> getRunResultMap() {
		return runResultMap;
	}
	public void setRunResultMap(Map<String, RunResult> runResultMap) {
		this.runResultMap = runResultMap;
	}
	public Map<String, McgProduct> getDataMap() {
        return dataMap;
    }
    public void setDataMap(Map<String, McgProduct> dataMap) {
        this.dataMap = dataMap;
    }

}
