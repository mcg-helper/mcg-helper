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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @ClassName:   SpringContextHelper   
 * @Description: TODO() 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午3:52:37  
 *
 */
public class SpringContextHelper implements ApplicationContextAware, DisposableBean {
	
	private static ApplicationContext applicationContext = null;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
	    this.applicationContext = applicationContext;
	    
	}

	public static ApplicationContext getAppContext() {
		return applicationContext;
	}

	public static final Object getSpringBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
	
	public static final <T> T getSpringBean(Class<T> beanType) {
		return applicationContext.getBean(beanType);
	}
	
    @Override
    public void destroy() throws Exception {
        SpringContextHelper.clearHolder();
    }
    
    public static void clearHolder() {
        applicationContext = null;
    }    
}
