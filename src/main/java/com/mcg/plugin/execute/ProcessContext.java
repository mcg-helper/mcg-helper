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

package com.mcg.plugin.execute;

import java.util.ArrayList;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.plugin.build.McgProduct;

public class ProcessContext {

    ProcessStrategy processStrategy;
    
    public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
        processStrategy.prepare(sequence, mcgProduct, executeStruct);
    }
    
    public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
        return processStrategy.run(mcgProduct, executeStruct);
    }

    public void setProcessStrategy(ProcessStrategy processStrategy) {
        this.processStrategy = processStrategy;
    }
    
}
