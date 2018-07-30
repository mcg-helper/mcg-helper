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
<div class="container-fluid" >
	<div class="row">
		<div class="col-md-12">
			<ul class="nav nav-tabs" role="tablist" id="${modalId }_tab">
				<li id="dbType" role="presentation" class="active"><a href="#${modalId }_property" data-toggle="tab">数据库</a></li>
				<li id="serverType" role="presentation"><a href="#${modalId }_serverProperty" data-toggle="tab">服务器</a></li>
			  	<li role="presentation"><a href="#${modalId }_explain" data-toggle="tab">说明</a></li>
			</ul>
		</div>
	</div>
	
	<div class="row margin_top">
		<div class="col-md-12">	
			<form id="${modalId }_dataSourceForm" class="form-horizontal" role="form">
				<br/>
				<input type="hidden" id="${modalId }_dataSourceId" name="dataSourceId" value="${modalId }" />
				<div class="form-body">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane fade in active" id="${modalId }_property">
						<!-- data-height="390" -->
					        <table id="${modalId }_flowDataSourceTable"
					               data-toggle="table"
					               data-show-refresh="true"
					               data-show-toggle="true"
					               data-show-columns="true"
					               data-search="true" 
					               data-show-pagination-switch="true" 
						           data-pagination="true"
						           data-page-size="5"
						           data-page-list="[5,10,20,30]"
						           data-pagination-first-text="上一页"
						           data-pagination-pre-text="上一页"
						           data-pagination-next-text="下一页"
						           data-pagination-last-text="尾页">
					            <thead>
					            <tr>
					            	<th data-field="state" data-checkbox="true"></th>
					                <th data-field="name" data-formatter="inputFormatter" >数据源名称</th>
					                <th data-field="dbType" data-formatter="dbTypeSelectFormatter">数据库类型</th>
					                <th data-field="dbServer" data-formatter="inputFormatter">数据库IP</th>
					                <th data-field="dbPort" data-formatter="inputFormatter">端口</th>
					                <th data-field="dbName" data-formatter="inputFormatter">数据库名称</th>
					                <th data-field="dbUserName" data-formatter="inputFormatter">用户名</th>
					                <th data-field="dbPwd" data-formatter="inputFormatter">密码</th>
					                <th data-field="note" data-formatter="inputFormatter">说明</th>
					                <th data-field="commands" data-formatter="dsCommandsFormatter">操作</th>
					            </tr>
					            </thead>
					        </table>
						</div>
						<div class="tab-pane fade" id="${modalId }_serverProperty">
						<!-- data-height="390" -->
					        <table id="${modalId }_flowServerSourceTable"
					               data-toggle="table"
					               data-show-refresh="true"
					               data-show-toggle="true"
					               data-show-columns="true"
					               data-search="true" 
					               data-show-pagination-switch="true" 
						           data-pagination="true"
						           data-page-size="5"
						           data-page-list="[5,10,20,30]"
						           data-pagination-first-text="上一页"
						           data-pagination-pre-text="上一页"
						           data-pagination-next-text="下一页"
						           data-pagination-last-text="尾页">
					            <thead>
					            <tr>
					            	<th data-field="state" data-checkbox="true"></th>
					                <th data-field="name" data-formatter="inputFormatter" >服务器名称</th>
					                <th data-field="type" data-formatter="serverTypeSelectFormatter">服务器类型</th>
					                <th data-field="ip" data-formatter="inputFormatter">服务器IP</th>
					                <th data-field="port" data-formatter="inputFormatter">端口</th>
					                <th data-field="userName" data-formatter="inputFormatter">用户名</th>
					                <th data-field="pwd" data-formatter="inputFormatter">密码</th>
					                <th data-field="note" data-formatter="inputFormatter">说明</th>
					                <th data-field="commands" data-formatter="ssCommandsFormatter">操作</th>
					            </tr>
					            </thead>
					        </table>
						</div>
						<div class="tab-pane fade" id="${modalId }_explain">
							绑定数据库连接信息，便于其它控件引用数据源，实现与数据库的通信，支持主流数据库mysql、oracle、sqlserver、postgresql。
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>