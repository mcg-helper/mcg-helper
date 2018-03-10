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
	   			<h3>QQ群</h3>
	   			<p>
	   				群号：619815829，学习交流沟通，分享彼此经验，这里畅所欲言，欢迎大家提出宝贵建议与意见！
	   			</p>
	   		</div>
	   		<div class="col-md-4">
	   			<h3>使用指南</h3>
	   			<p>
	   				与csdn学院视频有所不同，这里纯粹讲解工具的使用，视频在百度云盘上，点击视频同样可在线观看------
	   				<a href="https://pan.baidu.com/s/1sliEqhJ#list/path=%2F%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97" target="_blank">观看请点击这里</a>
	   			</p>
	   		</div>	   		
	   		<div class="col-md-4">
	   			<h3>csdn博客</h3>
	   			<p>
	   				这里有最新的动态资讯、图文教程、使用指南，欢迎大家留言关注------
	   				<a href="http://blog.csdn.net/loginandpwd/article" target="_blank">访问请点击这里</a>
	   			</p>
	   		</div>
		</div>
		<div class="row">
	   		<div class="col-md-4">
	   			<h3>csdn学院</h3>
	   			<p>
	   				视频教程更为直接有效，便于认识和掌控工具，《代码生成实战初级篇》课程以实战演练方式，运用工具实现百分之百的代码生成，这里需要开通csdn学院会员------
	   				<a href="http://edu.csdn.net/course/detail/5953" target="_blank">观看请点击这里</a>
	   			</p>
	   		</div>
	   		<div class="col-md-4">
	   			<h3>示例下载</h3>
	   			<p>
	   				提供博客教程、视频教程中分享讲解的示例，下载后可导入到工具中进行学习，通过运行调试能够更好的学习使用与掌握------
	   				<a href="https://pan.baidu.com/s/1jI5h5sU#list/path=%2Fdemo" target="_blank">下载请点击这里</a>
	   			</p>
	   		</div>
	   		<div class="col-md-4">
	   			<h3>软件下载</h3>
	   			<p>
	   				本软件跨平台且无浸入性，基于流程图，采用拖拽式控件，人性化交互设计，提供便捷实用的功能服务，完全性自定义，能够实现任何语言、任何框架的代码生成------
	   				<a href="https://pan.baidu.com/s/1bOe1mY#list/path=%2Fmcg-helper" target="_blank">下载请点击这里</a>
	   			</p>
	   		</div>
		</div>
		<div class="row">
			<div class="col-md-4" >
	   			<h3>软件信息</h3>
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
