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

package com.mcg.entity.wonton;

import com.mcg.entity.flow.wonton.WontonCpuRule;
import com.mcg.entity.flow.wonton.WontonIoRule;
import com.mcg.entity.flow.wonton.WontonMemRule;
import com.mcg.entity.flow.wonton.WontonNetRule;

public class WontonPublish {

	private WontonHeart wontonHeart;
	private WontonNetRule netRule;
	private WontonCpuRule cpuRule;
	private WontonMemRule memRule;
	private WontonIoRule ioRule;
	
	public WontonHeart getWontonHeart() {
		return wontonHeart;
	}

	public void setWontonHeart(WontonHeart wontonHeart) {
		this.wontonHeart = wontonHeart;
	}

	public WontonNetRule getNetRule() {
		return netRule;
	}

	public void setNetRule(WontonNetRule netRule) {
		this.netRule = netRule;
	}

	public WontonCpuRule getCpuRule() {
		return cpuRule;
	}

	public void setCpuRule(WontonCpuRule cpuRule) {
		this.cpuRule = cpuRule;
	}

	public WontonMemRule getMemRule() {
		return memRule;
	}

	public void setMemRule(WontonMemRule memRule) {
		this.memRule = memRule;
	}

	public WontonIoRule getIoRule() {
		return ioRule;
	}

	public void setIoRule(WontonIoRule ioRule) {
		this.ioRule = ioRule;
	}

	
	
}
