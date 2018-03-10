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

public class TplEngine {

	private TplLan tplLan;
	
	public TplEngine(TplLan tplLan) {
		this.tplLan = tplLan;
	}

	public String generate(JSON json, String ftl, String outFileName, String outFilePath) throws Exception  {
		return this.tplLan.generate(json, ftl, outFileName, outFilePath);
	}
	
	public String generate(JSON json, String source) throws Exception {
		return this.tplLan.generate(json, source);
	}

	public void setTplLan(TplLan tplLan) {
		this.tplLan = tplLan;
	}
	
}