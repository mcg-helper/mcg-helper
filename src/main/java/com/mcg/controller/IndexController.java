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

package com.mcg.controller;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mcg.common.Constants;
import com.mcg.controller.base.BaseController;

/**
 * 
 * @ClassName:   IndexController   
 * @Description: TODO(进入系统的第一入口) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:04:01  
 *
 */
@Controller
public class IndexController extends BaseController {
    
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView toIndex(ServletRequest reuqest, ServletResponse resp) {
        ModelAndView mv = this.getModelAndView();
        mv.addObject("js", Constants.js);
        mv.addObject("version", Constants.VERSION);
        mv.setViewName("index");
        return mv;
    }
      
}
