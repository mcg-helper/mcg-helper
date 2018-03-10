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

package com.mcg.service.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.mcg.common.Constants;
import com.mcg.entity.global.McgGlobal;
import com.mcg.service.GlobalService;
import com.mcg.util.LevelDbUtil;

@Service
public class GlobalServiceImpl implements GlobalService {

    @Override
    public boolean updateGlobal(McgGlobal mcgGlobal) throws IOException {
    	boolean result = false;

		LevelDbUtil.putObject(Constants.GLOBAL_KEY, mcgGlobal);
		result = true;
        return result;
    }
    
    @Override
	public boolean saveFlowEmpty(String flowId) {
    	
    	LevelDbUtil.delete(flowId);
		return true;
	}

}