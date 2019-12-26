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
<%@ page language="java" import="java.util.*"  pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
	<link rel="stylesheet" href="<%=basePath %>/library/css/font-awesome/css/font-awesome.min.css?_v=${version}">
	<link rel="stylesheet" href="<%=basePath %>/library/js/ztree/css/awesomeStyle/awesome.css?_v=${version}" type="text/css">
	<link rel="stylesheet" href="<%=basePath %>/library/js/bootstrap/css/bootstrap.min.css?_v=${version}">
	<link rel="stylesheet" href="<%=basePath %>/library/js/bootstrap/select-1.12.1/css/bootstrap-select.min.css?_v=${version}">
	<link rel="stylesheet" href="<%=basePath %>/library/js/bootstraptable/css/bootstrap-table.min.css?_v=${version}">
	<link rel="stylesheet" href="<%=basePath %>/library/js/jquery-ui-1.12.1/jquery-ui-1.10.0.custom.css?_v=${version}">
	<link rel="stylesheet" href="<%=basePath %>/library/js/bootstrap-switch/css/bootstrap-switch.min.css?_v=${version}">
	<link rel="stylesheet" href="<%=basePath %>/library/js/messenger/css/messenger.css?_v=${version}">
    <link rel="stylesheet" href="<%=basePath %>/library/js/messenger/css/messenger-theme-air.css?_v=${version}">
    <link rel="stylesheet" href="<%=basePath %>/library/js/jsoneditor/jsoneditor.css?_v=${version}">
	<link rel="stylesheet" href="<%=basePath %>/library/css/flow/core.css?_v=${version}">
	<link rel="stylesheet" href="<%=basePath %>/library/css/flow/drage.css?_v=${version}">
	<link rel="stylesheet" href="<%=basePath %>/library/css/addition.css?_v=${version}">
    
	<script type="text/javascript" src="<%=basePath %>/library/js/jquery-1.12.2.min.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/jquery-ui-1.12.1/jquery-ui.min.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/jquery.goup.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/bootstrap/js/bootstrap.min.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/ztree/js/jquery.ztree.core.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/ztree/js/jquery.ztree.excheck.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/ztree/js/jquery.ztree.exedit.js?_v=${version}"></script>
	
	<script type="text/javascript" src="<%=basePath %>/library/js/bootstrap-switch/js/bootstrap-switch.min.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/bootstraptable/js/bootstrap-table.min.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/bootstraptable/locale/bootstrap-table-zh-CN.min.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/messenger/js/messenger.min.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/ace/ace.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/ace/ext-language_tools.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/ace/source-code-pro/source-code-pro.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/bootstrap/select-1.12.1/js/bootstrap-select.min.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/jsoneditor/jsoneditor-minimalist.min.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/jquery.serialize-object.min.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/jquery.form.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/reconnecting-websocket.min.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/common.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/jquery.jsPlumb-1.7.5-min.js?_v=${version}"></script>

	<script type="text/javascript" src="<%=basePath %>/library/js/flow/initmodal.js?_v=${version}"></script>
	<script type="text/javascript" src="<%=basePath %>/library/js/flow/wigdet.js?_v=${version}"></script>	
	<script type="text/javascript" src="<%=basePath %>/library/js/flow/htmlmodel.js?_v=${version}"></script>

	<script type="text/javascript">
		var baseUrl = '<%=basePath %>';
		var websocketUrl = "ws:" + baseUrl.replace("http:", "") + "/message";
		
		var mcgWebSocket = {
			open : function () {
				ws = new ReconnectingWebSocket(websocketUrl);

				ws.onmessage = function (e) {
		  			var message = new Message({
						msg : JSON.parse(event.data)
					});
					message.output();
				};

				ws.onopen = function (e) {
					
				};

				ws.onclose = function (e) {
					
				};

				ws.onerror = function (e) {
					
				};
			},
			send : function (x) {
				ws.send(x);
			}
		}
		mcgWebSocket.open();
		 
	</script>