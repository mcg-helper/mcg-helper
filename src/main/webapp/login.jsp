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
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="shortcut icon"  href="<%=basePath %>/favicon.ico" >
	<link rel="stylesheet" href="<%=basePath %>/library/js/bootstrap/css/bootstrap.min.css">
	<script src="<%=basePath %>/library/js/jquery-1.12.2.min.js"></script>
	<script src="<%=basePath %>/library/js/bootstrap/js/bootstrap.min.js"></script>
	<title>mcg-helper研发助手</title>
	<script type="text/javascript">
		$(function(){
			$('#loginBtn').on('click', function () {
 				var $btn = $(this).button('loading');
				$("#loginForm").submit(); 
			//	$btn.button('reset');
			});
		});
		
	</script>
</head>
<body>
	<div class="container">
		<form id="loginForm" class="form-signin" action="<%=basePath %>/login" method="post">
	        <div class="modal-dialog" style="margin-top: 15%;">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <h4 class="modal-title">欢迎使用mcg-helper研发助手</h4>
	                </div>
 	                
	                <div class="modal-body">
	                    
	                	<input type="hidden" name="userKey" value="test" placeholder="" autocomplete="off" class="form-control placeholder-no-fix">
	                	<div class="form-body">
	                	
	                		<div class="form-group">
	                    		<button id="loginBtn" class="btn btn-primary pull-right" type="button">进入系统</button>
	                    		<br/>
	                    	</div>
	                    </div>
	                    	
	                </div>
<!--  	                 
	                <div class="modal-footer">

	                </div>
	                 -->
	            </div>
	        </div>
        </form>
	</div>            
</body>
</html>