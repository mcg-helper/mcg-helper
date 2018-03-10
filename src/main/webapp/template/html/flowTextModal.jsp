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
			<ul class="nav nav-tabs" role="tablist">
			  	<li role="presentation" class="active"><a href="#${modalId }_textProperty" data-toggle="tab">属性</a></li>
			  	<li role="presentation"><a href="#${modalId }_textCore" data-toggle="tab">源代码</a></li>
			  	<li role="presentation"><a href="#${modalId }_explain" data-toggle="tab">说明</a></li>
			</ul>
		</div>
	</div>
	
	<div class="row margin_top">
		<div class="col-md-12">	
			<form id="${modalId }_textForm" class="form-horizontal" role="form">
				<div class="form-body">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane fade in active" id="${modalId }_textProperty">
							<div class="form-group">
								<label class="col-sm-2 control-label">控件名称</label>
								<div class="fg-line col-sm-10">
									<input type="text" id="${modalId }_name" name="textProperty[name]" class="form-control"  />
								</div>
							</div>		
							<div class="form-group">
								<label class="col-sm-2 control-label">控件key</label>
								<div class="fg-line col-sm-10">
									<input type="text" id="${modalId }_key" name="textProperty[key]" class="form-control"  />
								</div>
							</div>														
							<div class="form-group">
								<label class="col-sm-2 control-label">文件名称</label>
								<div class="fg-line col-sm-10">
									<input type="hidden" id="${modalId }_textId" name="textId" value="${modalId }" />
									<input type="text" id="${modalId }_fileName" name="textProperty[fileName]" class="form-control"  />
								</div>									
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">输出路径</label>
								<div class="fg-line col-sm-10">
									<input type="text" id="${modalId }_outPutPath" name="textProperty[outPutPath]" class="form-control"  />
								</div>
							</div>								
						</div>												
						<div class="tab-pane fade" id="${modalId }_textCore">
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
										文本生成控件，将有父级控件的运行值为参数，运用freemaker模板语言进行数据
										解析和处理，最终生成想要字符文件，可通过控制台的查看控件的传入参数值，
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