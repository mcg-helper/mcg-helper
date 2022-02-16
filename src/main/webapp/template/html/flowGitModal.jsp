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

<div class="container-fluid" style="height: 400px;">
	<div class="row">
		<div class="col-md-12">
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#${modalId }_gitProperty" data-toggle="tab">属性</a></li>
			  	<li role="presentation"><a href="#${modalId }_explain" data-toggle="tab">说明</a></li>
			</ul>
		</div>
	</div>

	<div class="row margin_top">
		<div class="col-md-12">
			<form id="${modalId }_gitForm" class="form-horizontal" role="form">
				<div class="form-body">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane fade in active" id="${modalId }_gitProperty">
							<div class="form-group">
								<label class="col-sm-2 control-label">控件名称</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="hidden" id="${modalId }_id" name="id" value="${modalId }" />
										<input type="hidden" name="id" value="${modalId }" />
										<input type="text" name="gitProperty[name]" class="form-control"  />
									</div>
								</div>
								<label class="col-sm-1 control-label">控件KEY</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" id="${modalId }_key" name="gitProperty[key]" class="form-control" />
									</div>
								</div>									
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">GIT地址</label>
								<div class="col-sm-9">
									<div class="fg-line">
										<input type="text" name="gitProperty[remoteUrl]" class="form-control"  />
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">项目路径</label>
								<div class="col-sm-9">
									<div class="fg-line">
										<input type="text" name="gitProperty[projectPath]" class="form-control"  />
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">分支名</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="gitProperty[branch]" class="form-control"  />
									</div>
								</div>
								<label class="col-sm-1 control-label">连接方式</label>
								<div class="col-sm-4">
									<div class="fg-line">
							          	<select id="${modalId }_mode" name="gitProperty[mode]" class="selectpicker">
						                	<c:forEach items="${gitModes}" var="item">
							              		<option value="${item.value }">${item.name }</option>
							              	</c:forEach>
							          	</select>
									</div>
								</div>								
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">用户名</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="gitProperty[userName]" class="form-control"  />
									</div>
								</div>
								<label class="col-sm-1 control-label">密码</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="password" name="gitProperty[userPwd]" class="form-control" />
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">描述</label>
								<div class="col-sm-9">
									<div class="fg-line">
										<textarea id="${modalId }_desc" name="gitProperty[desc]" rows="5" cols="" class="form-control"></textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="tab-pane fade" id="${modalId }_explain">
							<div class="form-group">
								<div class="col-sm-12">
									<div class="fg-line">
										<p>操作GIT，实现克隆，拉取功能。<p/>
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