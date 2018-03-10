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

package com.mcg.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mcg.entity.auth.PermissionCollection;
import com.mcg.entity.auth.UserCacheBean;

/**
 * 
 * @ClassName:   LoginHandlerInterceptor   
 * @Description: TODO(登录拦截器) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:26:41  
 *
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		UserCacheBean ucb = PermissionCollection.getInstance().getUserCache(request.getSession().getId());
        if(request.getRequestURI().contains(".css") || request.getRequestURI().contains(".js") || request.getRequestURI().contains(".ico") ){
            return true;
        }

		if(ucb == null) {
		    request.getRequestDispatcher("/login.jsp").forward(request, response);    
			return false;
		}
		
		return true;
	}
	
}
