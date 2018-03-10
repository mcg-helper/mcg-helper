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
<div id="flowElementSuspension" class="btn-group">
	<button id="elementEditBtn" onclick="suspend('edit')"  type="button" class="btn btn-default"><span class="glyphicon glyphicon-edit"></span></button>
	<button id="elementDeleteBtn" onclick="suspend('delete')" type="button" class="btn btn-default"><span class="glyphicon glyphicon-remove"></span></button>
	<div class="btn-group">
		<button id="logOutBtn" onclick="suspend('logOut')" type="button" class="btn btn-default"><span class="glyphicon glyphicon-log-out"></span></button>
<!--  			
		<button id="elementSet" onclick="suspend('set')" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
			<span class="glyphicon glyphicon-cog"></span>
		</button>
		<ul class="dropdown-menu" role="menu">
			<li><a id="nono" href="#" onclick="suspend('no')">设置</a></li>
			<li><a id="yesyes" href="#" onclick="suspend('yes')">功能</a></li>
		</ul>
		  -->
	</div>
</div>