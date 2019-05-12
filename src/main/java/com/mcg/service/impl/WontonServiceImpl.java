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
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mcg.common.Constants;
import com.mcg.entity.global.wonton.WontonData;
import com.mcg.entity.wonton.WontonHeart;
import com.mcg.service.DbService;
import com.mcg.service.WontonService;

@Service
public class WontonServiceImpl implements WontonService {

    @Autowired
    private DbService dbService;
	
	@Override
	public List<WontonHeart> getAll() throws ClassNotFoundException, IOException{
		WontonData wontonData = (WontonData)dbService.query(Constants.WONTON_KEY, WontonData.class);
		if(wontonData != null && wontonData.getWontonHeartMap().size() > 0) {
			return new ArrayList<WontonHeart>(wontonData.getWontonHeartMap().values());
		} 
		return null;
	}

}
