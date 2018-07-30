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
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width">
		<link rel="shortcut icon" type="image/png" href="../../img/favicon.png">
        <title>mcg-helper研发助手</title>
		
		<link rel="stylesheet" href="<%=basePath %>library/css/core.css?_v=${version}">
		<link rel="stylesheet" href="<%=basePath %>library/css/drage.css?_v=${version}">
		<script type="text/javascript" src="<%=basePath %>library/js/flow/jquery.jsPlumb-1.7.5-min.js?_v=${version}"></script>
		<script type="text/javascript" src="<%=basePath %>library/js/flow/wigdet.js?_v=${version}"></script>	
		<script type="text/javascript" src="<%=basePath %>library/js/flow/mcgfn.js?_v=${version}"></script>			
		<script type="text/javascript" src="<%=basePath %>library/js/flow/htmlmodel.js?_v=${version}"></script>
	</head>

	<body>
		<!-- 主面body开始 -->
		<div id="mcg_body"  class="container-fluid autoHeight">
			<div class="row autoHeight">
				<!-- 工具区开始 -->
				<div class="col-md-2 autoHeight">
					<div id="mcg_toolbar" class="row autoHeight">
						<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="headingOne">
									<h4 class="panel-title">
										<a data-toggle="collapse" data-parent="#accordion"
											href="#collapseOne" aria-expanded="true"
											aria-controls="collapseOne">拖拽控件区</a>
									</h4>
								</div>
								<div id="collapseOne" class="panel-collapse collapse in"
									role="tabpanel" aria-labelledby="headingOne">
									<div class="panel-body">
										<div class="row">
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarStart" class="toolbar_eletype" eletype="start">开始</div>
											</div>
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarEnd" class="toolbar_eletype" eletype="end">结束</div>
											</div>
										</div>
										<div class="row">
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarModel" class="toolbar_eletype" eletype="model">model</div>
											</div>
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarJson" class="toolbar_eletype" eletype="json">json</div>
											</div>										
										</div>
										<div class="row">
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarGmybatis" class="toolbar_eletype" eletype="gmybatis">gmybatis</div>
											</div>
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarData" class="toolbar_eletype" eletype="data">data</div>
											</div>
										</div>
										<div class="row">
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarSqlQuery" class="toolbar_eletype" eletype="sqlQuery">sql查询</div>
											</div>
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarSqlExecute" class="toolbar_eletype" eletype="sqlExecute">sql执行</div>
											</div>										
										</div>										
										<div class="row">
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarScript" class="toolbar_eletype" eletype="script">JS脚本</div>
											</div>
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarJava" class="toolbar_eletype" eletype="java">JAVA</div>
											</div>										
										</div>
										<div class="row">
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarPython" class="toolbar_eletype" eletype="python">Python</div>
											</div>
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarText" class="toolbar_eletype" eletype="text">文本</div>
											</div>												
										</div>
										<div class="row">
											<div class="col-md-4 col-md-offset-1">
												<div id="toolbarLinux" class="toolbar_eletype" eletype="linux">Linux</div>
											</div>
											<div class="col-md-4 col-md-offset-1">
												
											</div>												
										</div>										
										
									</div>
								</div>
							</div>
						</div>
					</div>

				</div>
				<!-- 工具区结束 -->
				<!-- 流程工作区开始 -->
				<div class="col-md-10 autoHeight" style="border-left: 1px solid #e5e5e5;">
					<!-- 功能按钮操作区开始 -->
					<div id="mcg_func" class="row">
					     
						<div class="btn-group no_width_center">
								<button id="flowSelect" value="" flowId="" flowName="" flowPid="" class="btn btn-default" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								   我的流程&nbsp;<span class="caret"></span>
								  </button>
								<ul class="dropdown-menu">
									<ul id="flowTree" class="ztree" style="min-width: 240px;"></ul>
								</ul>
								<button id="flowClearBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-remove-sign"></span>&nbsp;清空</button>
								<button id="flowDataSourceBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-copyright-mark"></span>&nbsp;数据源</button>
								<button id="flowSaveBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-floppy-saved"></span>&nbsp;保存</button>
								<button id="flowGenBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-registration-mark"></span>&nbsp;生成</button>
								<button id="flowImpBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-upload"></span>&nbsp;导入</button>
								<button id="flowExpBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-download"></span>&nbsp;导出</button>
						</div>
					</div>
					<!-- 功能按钮操作区结束 -->
					<!-- 绘制流程区开始 -->
					<div class="row autoHeight">
		    			<div class="demo flowarea autoHeight" id="flowarea" style="overflow:scroll;"></div>
		    		</div>
		    		<!-- 绘制流程区结束 -->
		    		<!-- 控制台区开始 -->
		    		<div class="row">
						<nav class="navbar-default" >
							<div class="container-fluid">
								<div class="navbar-header">
									<span class="navbar-brand">控制台</span>
								</div>
							</div>
						</nav>
		    		</div>
		    		<div class="row">
		    			
 						<div id="console" style="width:100%;"></div>	    			
		    		</div>
		    		<!-- 控制台区结束 -->
				</div>
				<!-- 流程工作区结束 -->
			</div>
		</div>
		<!-- 主面body结束 -->
	</body>
</html>