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

<div class="container-fluid" >
	<div class="row">
		<div class="col-md-12">
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#${modalId }_linuxProperty" data-toggle="tab">属性</a></li>
			  	<li role="presentation"><a href="#${modalId }_coreLinux" data-toggle="tab">源代码</a></li>
			  	<li role="presentation"><a href="#${modalId }_explain" data-toggle="tab">说明</a></li>
			</ul>
		</div>
	</div>

	<div class="row margin_top">
		<div class="col-md-12">
			<form id="${modalId }_linuxForm" class="form-horizontal" role="form">
				<div class="form-body">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane fade in active" id="${modalId }_linuxProperty">
							<div class="form-group">
								<label class="col-sm-2 control-label">控件名称</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="hidden" id="${modalId }_id" name="id" value="${modalId }" />
										<input type="hidden" id="${modalId }_modalId" name="id" value="${modalId }" />
										<input type="text" id="${modalId }_name" name="linuxProperty[name]" class="form-control"  />
									</div>
								</div>
								<label class="col-sm-1 control-label">控件KEY</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" id="${modalId }_key" name="linuxProperty[key]" class="form-control"  />
									</div>
								</div>									
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">连接方式</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select id="${modalId }_connMode" name="linuxCore[connMode]" class="selectpicker">
											<c:forEach items="${connMode}" var="item" varStatus="status">
												<c:choose>
													<c:when test="${status.count == 1}">
														<option value="${item.value }" selected="selected">${item.name }</option>
													</c:when>
													<c:otherwise>
														<option value="${item.value }">${item.name }</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</div>
								</div>
								<label class="col-sm-1 control-label">数据源</label>
								<div class="col-sm-4">
									<div class="fg-line">
							          	<select id="${modalId }_serverSourceId" name="linuxCore[serverSourceId]" class="selectpicker">
						                	<c:forEach items="${serverSources}" var="item">
							              		<option value="${item.id }">${item.name } : ${item.ip }</option>
							              	</c:forEach>
							          	</select>										
									</div>
								</div>
							</div>		
							<div class="form-group" id="${modalId }_ip_port" style="display: none;">
								<label class="col-sm-2 control-label">IP</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" id="${modalId }_sshIp" name="linuxCore[ip]" class="form-control" />
									</div>
								</div>
								<label class="col-sm-1 control-label">端口</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" id="${modalId }_sshPort" name="linuxCore[port]" class="form-control" />
									</div>
								</div>
							</div>
							<div class="form-group" id="${modalId }_user_pwd" style="display: none;">
								<label class="col-sm-2 control-label">用户名</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" id="${modalId }_sshUser" name="linuxCore[user]" class="form-control" />
									</div>
								</div>
								<label class="col-sm-1 control-label">密码</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" id="${modalId }_sshPwd" name="linuxCore[pwd]" class="form-control" />
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">描述</label>
								<div class="col-sm-9">
									<div class="fg-line">
										<textarea id="${modalId }_desc" name="linuxProperty[desc]" rows="5" cols="" class="form-control"></textarea>
									</div>
								</div>
							</div>								
						</div>
						<div class="tab-pane fade" id="${modalId }_coreLinux">
							<div class="form-group">
								<div class="col-sm-12">
									<pre id="${modalId }_editor" style="min-height:300px"></pre>
								</div>
							</div>
						</div>
						<div class="tab-pane fade" id="${modalId }_explain">
							<div class="form-group">
								<div class="col-sm-12">
									<div class="fg-line">
										Linux SSH连接，可执行Linux Shell命令，亮点在于实现交互等待命令（interact 时间毫秒），<br/>
										比如：执行下一句命令前等待2秒钟：interact 2000，适用于交互性场景，比如：用户切换时要求密码，软件安装再次确认等场景<br/>
										可在源代码编辑器中按下ctrl+enter键切换全屏。
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