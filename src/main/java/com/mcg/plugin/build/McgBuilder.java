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

public abstract class McgBuilder {

    /**
     * 
     * @Title:       setSequence   
     * @Description: TODO(构建处理流程集合)   
     * @param:       @param sequence      
     * @return:      void      
     * @throws
     */
    public abstract void setSequence(ArrayList<String> sequence);

    /**
     * 
     * @Title:       getMcgProduct   
     * @Description: TODO(获取模型)   
     * @param:       @return      
     * @return:      McgProduct      
     * @throws
     */
    public abstract McgProduct getMcgProduct();
    
}