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

import java.io.Serializable;
import java.util.ArrayList;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;

public abstract class McgProduct implements Serializable{

    private static final long serialVersionUID = 8626194290037166978L;
    // 各个基本方法执行的顺序
    private ArrayList<String> sequence = new ArrayList<String>();
    
    // 准备工作
    public abstract void prepare(ArrayList<String> sequence, ExecuteStruct executeStruct);

    // 执行组件算法
    public abstract RunResult execute(ExecuteStruct executeStruct); 
    
    final public RunResult build(ExecuteStruct executeStruct) {
        this.prepare(sequence, executeStruct);
        
        return this.execute(executeStruct);
    }    
    
    final public void setSequence(ArrayList<String> sequence) {
        this.sequence = sequence;
    }
    
    final public ArrayList<String> getSequence() {
        return this.sequence;
    }

}