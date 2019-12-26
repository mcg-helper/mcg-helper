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

import java.nio.charset.Charset;

/**
 * 
 * @ClassName:   Constants   
 * @Description: TODO(定义系统需要的常量) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午3:47:31  
 *
 */
public class Constants {

    /*   字符编码        */
    public static final Charset CHARSET = Charset.forName("utf-8");
    /*   ID前缀     */
	public static  final String ID_PREX = "flowId_";
	/*   name前缀     */
	public static  final String NAME_PREX = "flowName_";
	/*   版本号        */
    public static final String VERSION = "mcg-helper_V1.6.0-Release";
    /*   全局变量key   */
    public static final String GLOBAL_KEY = "mcgGlobal";
    /*   流程文件扩展名        */
    public static final String EXTENSION = ".mcg";
    /*   字符加密key   */
    public final static String DES_KEY = "mcg-helper_2017_mc"; 
    /*   数据持久化路径        */
	public static String DATA_PATH;
    /*   项目的绝对路径        */
    public static String WEB_PATH;
	/*   缓存名称        */
	public final static String CACHE_NAME = "varCache";
	/*   连接缓存        */
	public final static String CONNECTOR_CACHE = "conCache";
	/*   SSH Linux控件 交互符        */
	public final static String LINUX_INTERACT = "interact";
	/*   SSH Linux控件 默认交互等待时间       */
	public final static long DEFAULT_TIME = 2000L;	
	/*   SSH Linux控件 结束符        */
	public final static String LINUX_EOF = "exit";
	/*   SSH Linux控件 换行符        */
	public final static String LINUX_ENTER = "\n";
	/*   混沌客户端数据key   */
	public final static String WONTON_KEY = "mcgWontonData";
	/* 请求混沌客户端超时设置 */
	public final static int REQUEST_WONTON_TIME_OUT = 5000;
	/*   统计代码        */
	public final static String js = "<script>var _hmt = _hmt || [];(function() {var hm = document.createElement(\"script\");hm.src = \"https://hm.baidu.com/hm.js?221fd6bfa9a0ff2f4a99e9f4ddb0075a\";var s = document.getElementsByTagName(\"script\")[0]; s.parentNode.insertBefore(hm, s);})();</script>";
    	
}
