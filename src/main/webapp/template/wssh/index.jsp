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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width">
		<link rel="shortcut icon" type="image/png" href="../../img/favicon.png">
        <title>mcg-helper研发助手</title>
		
	</head>
	
	<body>
		<div id="mcg_body" class="container-fluid autoHeight">
			<div class="row" style="height:40px; margin-top: 3px; margin-left: 3px;">
 				<div class="form-group">
				    <div class="input-group col-xs-3">
				        <div class="input-group-btn">
							<select id="mcg_wssh_serverSourceId" class="form-control selectpicker">
								<c:forEach items="${mcgServerSources}" var="item">
									<option value="${item.id }">${item.name }: ${item.ip }</option>
								</c:forEach>
							</select>
				        </div>
				        <span class="input-group-btn">
				            <button id="mcg_wwsh_connect" class="btn btn-default" id="search_submit" type="submit">连接</button>
				        </span>
				    </div>
				</div> 

			</div>
		
			<div class="row autoHeight">
				<div id="wssh_terminal" class="col-md-12 autoHeight" style="padding:0px 0px 0px 0px;">
			        
				</div>
			</div>
	 	</div>
	</body>
</html>
<script type="text/javascript" src="<%=basePath %>/library/js/wssh/index.js?_v=${version}"></script>
