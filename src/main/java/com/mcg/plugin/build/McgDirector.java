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

package com.mcg.plugin.build;

import java.util.ArrayList;

import com.mcg.entity.flow.data.FlowData;
import com.mcg.entity.flow.end.FlowEnd;
import com.mcg.entity.flow.gmybatis.FlowGmybatis;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.json.FlowJson;
import com.mcg.entity.flow.linux.FlowLinux;
import com.mcg.entity.flow.model.FlowModel;
import com.mcg.entity.flow.python.FlowPython;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.flow.sqlexecute.FlowSqlExecute;
import com.mcg.entity.flow.sqlquery.FlowSqlQuery;
import com.mcg.entity.flow.start.FlowStart;
import com.mcg.entity.flow.text.FlowText;

public class McgDirector {
    private ArrayList<String> sequence = new ArrayList<String>();
    
    public FlowStart getFlowStartProduct(FlowStart flowStart) {
        this.sequence.clear();
        return (FlowStart) new McgConcreteBuilder(flowStart, this.sequence).getMcgProduct();
    }
    
    public FlowModel getFlowModelProduct(FlowModel flowModel) {
        this.sequence.clear();
        
        return (FlowModel) new McgConcreteBuilder(flowModel, this.sequence).getMcgProduct();
    }
    
    public FlowJson getFlowJsonProduct(FlowJson flowJson) {
        this.sequence.clear();
        
        return (FlowJson) new McgConcreteBuilder(flowJson, this.sequence).getMcgProduct();
    }    
    
    public FlowSqlQuery getFlowSqlQueryProduct(FlowSqlQuery flowSqlQuery) {
        this.sequence.clear();
        
        return (FlowSqlQuery) new McgConcreteBuilder(flowSqlQuery, this.sequence).getMcgProduct();
    }
    
    public FlowSqlExecute getFlowSqlExecuteProduct(FlowSqlExecute flowSqlExecute) {
        this.sequence.clear();
        
        return (FlowSqlExecute) new McgConcreteBuilder(flowSqlExecute, this.sequence).getMcgProduct();
    }    
    
    public FlowGmybatis getFlowGmybatisProduct(FlowGmybatis flowGmybatis) {
        this.sequence.clear();
        return (FlowGmybatis) new McgConcreteBuilder(flowGmybatis, this.sequence).getMcgProduct();
    }
    
    public FlowData getFlowDataProduct(FlowData flowData) {
        this.sequence.clear();
        return (FlowData) new McgConcreteBuilder(flowData, this.sequence).getMcgProduct();
    }    
    
    public FlowText getFlowTextProduct(FlowText flowText) {
        this.sequence.clear();
        return (FlowText) new McgConcreteBuilder(flowText, this.sequence).getMcgProduct();
    }      
    
    public FlowScript getFlowScriptProduct(FlowScript flowScript) {
    	 this.sequence.clear();
         return (FlowScript) new McgConcreteBuilder(flowScript, this.sequence).getMcgProduct();    	
    }
    
    public FlowJava getFlowJavaProduct(FlowJava flowJava) {
        this.sequence.clear();
        return (FlowJava) new McgConcreteBuilder(flowJava, this.sequence).getMcgProduct();     
    }
    
    public FlowPython getFlowPythonProduct(FlowPython flowPython) {
        this.sequence.clear();
        return (FlowPython) new McgConcreteBuilder(flowPython, this.sequence).getMcgProduct();     
    }  
    
    public FlowLinux getFlowLinuxProduct(FlowLinux flowLinux) {
        this.sequence.clear();
        return (FlowLinux) new McgConcreteBuilder(flowLinux, this.sequence).getMcgProduct();     
    }    
      
    public FlowEnd getFlowEndProduct(FlowEnd flowEnd) {
        this.sequence.clear();
        return (FlowEnd) new McgConcreteBuilder(flowEnd, this.sequence).getMcgProduct();
    }     
}