<!-- 
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
 -->
 
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>mcg-helper研发助手</title>
</head>
<body>
    <div class="container">
    	<div class="row">
    		<div class="col-md-12">
    			<br/><br/><br/>
    		</div>
    	</div>
       	<div class="row">
	   		<div class="col-md-4">
	   			<h3>QQ交流群</h3>
	   			<p>
	   				群号：619815829，学习交流沟通，分享彼此经验，这里畅所欲言，欢迎大家提出宝贵建议与意见！<br/>
	   				反馈邮箱：mcg-helper@qq.com
	   			</p>
	   		</div>
	   		
	   		<div class="col-md-4">
	   			<h3>csdn博客</h3>
	   			<p>
	   				这里有最新的动态资讯、图文教程、场景案例，欢迎大家关注留言------
	   				<a href="http://blog.csdn.net/loginandpwd/article" target="_blank">访问请点击这里</a>
	   			</p>
	   		</div>
	   		
	   		<div class="col-md-4">
	   			<h3>视频教程</h3>
	   			<p>
	   				可快速学习与掌握工具的使用，教程以工作中实际需求为主题进行实战演练，逐步将日常工作进行流程自动化，达到减少简化工作量，显著性提升效率！------
	   				<a href="https://edu.csdn.net/course/play/5954" target="_blank">观看请点击这里</a>
	   			</p>
	   		</div>   		
		</div>
		<div class="row">
	   		<div class="col-md-4">
	   			<h3>开源地址</h3>
	   			<p>
	   				mcg-helper为github开源项目，需要二次开发和自定义控件的朋友，赶快加星与关注吧，欢迎共同探讨与完善！------
	   				<a href="https://github.com/mcg-helper/mcg-helper" target="_blank">下载请点击这里</a>
	   			</p>
	   		</div>
	   		
			<div class="col-md-4" >
	   			<h3>版本信息</h3>
	   			<p>
	   				浏览器：建议使用chrome、firefox、IE10（+）<br/>
	   				运行环境：jdk1.8（+）、tomcat8（+）<br/>
	   				版本号：${version }
	   				
	   			</p>
	   		</div>
	   		  
		</div>

   	</div>
 
	<div class="mcg_footer navbar-fixed-bottom">
		<div class="row">
		   	Copyright © 2018, mcg-helper@qq.com, All Rights Reserved
		</div>
	</div>
</body>
</html>
