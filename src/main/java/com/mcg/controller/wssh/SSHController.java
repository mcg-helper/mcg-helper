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

package com.mcg.controller.wssh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mcg.common.Constants;
import com.mcg.controller.base.BaseController;
import com.mcg.service.FlowService;

/**
 * 
 * @ClassName:   SSHController   
 * @Description: TODO(SSH连接功能服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2020年6月6日 下午14:45:06  
 *
 */

@Controller
@RequestMapping(value="/wssh")
public class SSHController extends BaseController {

    @Autowired
    private FlowService flowService;
	
	@RequestMapping(value="/index")
	public ModelAndView toIndex() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("wssh/index");
		mv.addObject("mcgServerSources", flowService.getMcgServerSources());
		mv.addObject("version", Constants.VERSION);
		return mv;
	}
	
}
