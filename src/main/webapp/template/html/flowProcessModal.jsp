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

<div class="container-fluid" style="height: 400px;">
	<div class="row">
		<div class="col-md-12">
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#${modalId }_processProperty" data-toggle="tab">属性</a></li>
			  	<li role="presentation"><a href="#${modalId }_explain" data-toggle="tab">说明</a></li>
			</ul>
		</div>
	</div>

	<div class="row margin_top">
		<div class="col-md-12">
			<form id="${modalId }_processForm" class="form-horizontal" role="form">
				<div class="form-body">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane fade in active" id="${modalId }_processProperty">
							<div class="form-group">
								<label class="col-sm-2 control-label">控件名称</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="hidden" name="id" value="${modalId }" />
										<input type="text" name="processProperty[name]" class="form-control"  />
									</div>
								</div>
								<label class="col-sm-1 control-label">控件KEY</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" id="${modalId }_key" name="processProperty[key]" class="form-control" />
									</div>
								</div>									
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">流程选择</label>
								<div class="col-sm-9">
									<div class="fg-line">
										<button id="${modalId }_flowName" class="btn btn-default form-control" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
										   我的流程&nbsp;<span class="caret"></span>
										  </button>
										<input id="${modalId }_flowId" name="processProperty[flowId]" type="hidden"/>
										
										<ul class="dropdown-menu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
											<ul id="${modalId }_flowTree" class="ztree" style="min-width: 240px;"></ul>
										</ul>
									</div>
								</div>
																	
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">脚本描述</label>
								<div class="col-sm-9">
									<div class="fg-line">
										<textarea id="${modalId }_processDesc" name="processProperty[desc]" rows="5" cols="" class="form-control"></textarea>
									</div>
								</div>
							</div>								
						</div>
						<div class="tab-pane fade" id="${modalId }_explain">
							<div class="form-group">
								<div class="col-sm-12">
									<div class="fg-line">
										子流程控件，当前流程中实现嵌套子流程。
									</div>
								</div>
							</div>
						</div>							
					</div>
				</div>
			</form>
		</div>
	</div>
</div>	