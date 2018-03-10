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
 * @ClassName:   JsonProcessInterceptor   
 * @Description: TODO(JSON处理后拦截器) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午3:52:16  
 *
 */
public interface JsonProcessInterceptor {
	
	/**
	 * 读入JSON前,执行操作
	 * @param jsonBytes
	 */
	void readBefore(byte[] jsonBytes);
	
	/**
	 * 读入JSON后,执行操作
	 * @param object
	 */
	void readAfter(Object object);
	
	/**
	 * 写入JSON前,执行操作
	 * @param object
	 */
	void writeBefore(Object object);
	/**
	 * 写入JSON后,执行操作
	 * @param jsonStr
	 */
	void writeAfter(String jsonStr);

}
