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

package com.mcg.common;

/**
 * 
 * @ClassName:   JsonpResult   
 * @Description: TODO() 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午3:51:31  
 *
 */
public class JsonpResult {
	
	/**
	 * 回调JS function 名称
	 */
	private String jsonpFunction;
	
	/**
	 * 返回值
	 */
	private Object value;

	public String getJsonpFunction() {
		return jsonpFunction;
	}

	public void setJsonpFunction(String jsonpFunction) {
		this.jsonpFunction = jsonpFunction;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	

}
