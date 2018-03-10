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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mcg.controller.base.BaseController;
import com.mcg.entity.auth.McgUser;
import com.mcg.entity.auth.PermissionCollection;
import com.mcg.entity.auth.UserCacheBean;

/**
 * 
 * @ClassName:   LoginController   
 * @Description: TODO(负责登录和登出) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:24:29  
 *
 */
@Controller
public class LoginController extends BaseController {
	
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView index(HttpServletRequest reuqest, HttpServletResponse response) {
        ModelAndView mv = this.getModelAndView();

        if(reuqest.getHeader("x-requested-with")!=null && reuqest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
            //不记录上次登录的url
            mv.setViewName("timeout");
        } else {
        	UserCacheBean ucb = PermissionCollection.getInstance().getUserCache(reuqest.getSession().getId());
        	if(ucb == null ) {
        		if(reuqest.getParameter("userKey") != null && !"".equals(reuqest.getParameter("userKey"))) {
 		            ucb = new UserCacheBean();
 		            ucb.setSessionID(reuqest.getSession().getId());
 		            McgUser mu = new McgUser();
 		            mu.setUserKey(reuqest.getParameter("userKey"));
 		            ucb.setUser(mu);
 		            PermissionCollection.getInstance().addSessionUserCache(reuqest.getSession().getId(), ucb);
 		            mv.setViewName("redirect:/index");
 	        	} else {
 	        		mv.setViewName("redirect:/login.jsp");
 	        	}
         	} else {
         		mv.setViewName("redirect:/index");
         	}
        
        }

        return mv;
    }

}
