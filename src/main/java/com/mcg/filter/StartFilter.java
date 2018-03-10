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

package com.mcg.filter;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcg.common.Constants;
import com.mcg.util.LevelDbUtil;

/**
 * 
 * @ClassName:   StartFilter   
 * @Description: TODO(第一启动过滤器) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:27:29  
 *
 */
public class StartFilter implements Filter {
	protected final Logger log = LoggerFactory.getLogger(getClass());
    private FilterConfig   filterConfig;

    /**
     * 初始化filter。
     * @param filterConfig filter的配置信息
     * @throws ServletException 如果初始化失败
     */
    public final void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;

		Constants.WEB_PATH = filterConfig.getServletContext().getRealPath("/");
	    StringBuilder sb = new StringBuilder();
	    sb.append(filterConfig.getServletContext().getRealPath("/")).append("library")
	    .append(File.separator).append("data").append(File.separator);
	    Constants.DATA_PATH =  sb.toString();
	    
        try {
            LevelDbUtil.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化filter。
     * @throws ServletException 如果初始化失败
     */
    public void init() throws ServletException {

    }

    public void destroy() {
        filterConfig = null;
    }

    /**
     * 取得filter的配置信息。
     * @return <code>FilterConfig</code>对象
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * 取得servlet容器的上下文信息。
     * @return <code>ServletContext</code>对象
     */
    public ServletContext getServletContext() {
        return getFilterConfig().getServletContext();
    }

    /**
     * 执行filter.
     *
     * @param request HTTP请求
     * @param response HTTP响应
     * @param chain filter链
     *
     * @throws IOException 处理filter链时发生输入输出错误
     * @throws ServletException 处理filter链时发生的一般错误
     */    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // TODO Auto-generated method stub
        
    }

}