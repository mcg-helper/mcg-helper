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

package com.mcg.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mcg.common.Constants;
import com.mcg.controller.base.BaseController;

/**
 * 
 * @ClassName:   HomeController   
 * @Description: TODO(系统主页功能服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午4:03:06  
 *
 */
@Controller
@RequestMapping(value="/home")
public class HomeController extends BaseController {
	
	@RequestMapping(value="/index")
	public ModelAndView index() throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.addObject("version", Constants.VERSION);
		mv.setViewName("home/index");
		return mv;
	}
	
}
