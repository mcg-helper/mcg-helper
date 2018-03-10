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

package com.mcg.controller.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.mcg.util.PageData;

/**
 * 
 * @ClassName:   BaseController   
 * @Description: TODO(业类Controller继承该类) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午3:54:57  
 *
 */
public class BaseController {
    
	protected Logger logger = Logger.getLogger(this.getClass());
	
	public PageData getPageData(){
		PageData pd = new PageData(this.getRequest());
		return pd;
	}

	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}

	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		return request;
	}
	
}
