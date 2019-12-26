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
<!DOCTYPE html>
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>data_flow</title>
	<%@include file="/template/common.jsp"%>
	<link rel="shortcut icon" type="image/x-icon" href="<%=basePath %>/favicon.ico" >
	<link rel="stylesheet" href="<%=basePath %>/library/css/index.css?_v=${version}">
	<script src="<%=basePath %>/library/js/index.js?_v=${version}"></script>
</head>
<body>
	<!-- head开始 -->
	<div id="mcg_header" class="container-fluid">
		<div class="row ">
		    <div class="mcg_header">
		    </div>
	    </div>
	</div>
	<!-- head结束 -->
	
	<!-- tips开始 -->
	<div id="mcg_tips" class="container-fluid">

	</div>
	
	<!-- tips结束 -->
	
	<!-- main开始 -->
	<div id="mcg_main" class="container-fluid autoHeight">
		<div class="row">
			<div class="col-md-12">
				<div id="mcg_nav" class="mcg_main_nav_header">
					<div class="navbar-header pull-right">
						 <a class="navbar-brand" href="https://github.com/mcg-helper/mcg-helper" target="_blank">Data-Flow</a>
					</div>
						<ul class="nav nav-pills" role="tablist" id="mcgTab">
							<li role="presentation" class="active"><a href="#mcg_flow" data-toggle="tab">工作台</a></li>
							<li role="presentation" ><a href="#mcg_home" data-toggle="tab">帮助</a></li>
<%--							<li role="presentation"><a href="#mcg_wonton" data-toggle="tab">混沌工程</a></li>--%>
						</ul>

				</div>
			</div>
		</div>
		<div class="row autoHeight">
			<div class="col-md-12 autoHeight">
			
				<div class="tab-content autoHeight">
					<div id="mcg_flow" class="tab-pane active mcg_main_highlight autoHeight" style="overflow-x: auto;overflow-y: auto;"></div>
					<div id="mcg_home" class="tab-pane mcg_main_highlight autoHeight" style="overflow-x: auto;overflow-y: auto;"></div>
<%--					<div id="mcg_wonton" class="tab-pane mcg_main_highlight autoHeight"  style="overflow-x: auto;overflow-y: auto;"></div>--%>
				</div>
			</div>

		</div>

	</div>
	<!-- main结束 -->
	
	<!-- footer开始  navbar-fixed-bottom 
	<div id="mcg_footer" class="container-fluid mcg_footer">
		<div class="row">
			如有问题或建议请发送邮件到mcg-helper@qq.com进行反馈与交流，感谢您的支持！
		</div>
		<div class="row">
		   	Copyright © 2018, mcg-helper@qq.com, All Rights Reserved
		</div>
	</div>
	-->
	<!-- footer结束 -->
</body>
</html>
${js}