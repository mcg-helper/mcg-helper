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
	   			<h3>开源地址</h3>
	   			<p>
	   				mcg-helper为github开源项目，欢迎有需求或兴趣的朋友关注或下载，共同探讨与完善------
	   				<a href="https://github.com/mcg-helper/mcg-helper" target="_blank">下载请点击这里</a>
	   			</p>
	   		</div>
	   		<div class="col-md-4">
	   			<h3>QQ群</h3>
	   			<p>
	   				群号：619815829，学习交流沟通，分享彼此经验，这里畅所欲言，欢迎大家提出宝贵建议与意见！
	   			</p>
	   		</div>
	   		<div class="col-md-4">
	   			<h3>使用指南</h3>
	   			<p>
	   				免费视频教程，便于快速认识和掌握工具的使用，视频在百度云盘上，点击视频可在线观看------
	   				<a href="https://pan.baidu.com/s/1sliEqhJ#list/path=%2F%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97" target="_blank">观看请点击这里</a>
	   			</p>
	   		</div>	   		
		</div>
		<div class="row">
	   		<div class="col-md-4">
	   			<h3>csdn学院</h3>
	   			<p>
	   				付费视频课程，以工作中实际需求为主题，采用手把手的方式进行实战演练，逐步将日常工作进行自动化，起到简化工作量提升效率------
	   				<a href="https://edu.csdn.net/lecturer/1588" target="_blank">观看请点击这里</a>
	   			</p>
	   		</div>
	   		
	   		<div class="col-md-4">
	   			<h3>csdn博客</h3>
	   			<p>
	   				这里有最新的动态资讯、图文教程、场景案例，欢迎大家关注留言------
	   				<a href="http://blog.csdn.net/loginandpwd/article" target="_blank">访问请点击这里</a>
	   			</p>
	   		</div>

			<div class="col-md-4" >
	   			<h3>版本信息</h3>
	   			<p>
	   				浏览器：建议使用chrome、firefox、IE10（+）<br/>
	   				版本号：${version }<br/>
	   				email：mcg-helper@qq.com
	   			</p>
	   		</div>  
		</div>

   </div>
 
</body>
</html>
