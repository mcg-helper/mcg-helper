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
			    <li role="presentation" class="active"><a href="#${modalId }_endProperty" data-toggle="tab">属性</a></li>
			  	<li role="presentation"><a href="#${modalId }_endExplain" data-toggle="tab">说明</a></li>
			</ul>
		</div>
	</div>
	<div class="row margin_top">
		<div class="col-md-12">
			<form id="${modalId }_endForm" class="form-horizontal" role="form">
				<input type="hidden" id="${modalId }_endId" name="endId" value="${modalId }" />
				<div class="form-body">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane fade in active" id="${modalId }_endProperty">
							<textarea id="${modalId }_comment" name="endProperty[comment]" class="form-control" rows="6" cols=""></textarea>
						</div>						
						<div class="tab-pane fade" id="${modalId }_endExplain">
							<div class="form-group">
								<div class="col-sm-12">
									<div class="fg-line">
										该控件标志流程执行完成，暂无功能，可用于流程执行完成后控制台输出结束语。
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