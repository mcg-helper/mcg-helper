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

package com.mcg.plugin.tplengine;

import com.alibaba.fastjson.JSON;

public interface TplLan {

    /**
     * 
     * @Title:       generate   
     * @Description: TODO(根据模板语言和参数生成文件)   
     * @param:       @param json   json对象参数
     * @param:       @param ftl    模板的源代码
     * @param:       @param outFileName   输出文件名
     * @param:       @param outFilePath   输出路径
     * @param:       @return      
     * @return:      String      
     * @throws
     */
	String generate(JSON json, String ftl, String outFileName, String outFilePath) throws Exception;
	
	/**
	 * 
	 * @Title:       generate   
	 * @Description: TODO(根据模板语言和参数生成字符串)   
	 * @param:       @param json
	 * @param:       @param source
	 * @param:       @return      
	 * @return:      String      
	 * @throws
	 */
	String generate(JSON json, String source) throws Exception;
}
