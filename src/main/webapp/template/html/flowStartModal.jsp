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
			    <li role="presentation" class="active"><a href="#${modalId }_startProperty" data-toggle="tab">属性</a></li>
			  	<li role="presentation"><a href="#${modalId }_startExplain" data-toggle="tab">说明</a></li>
			</ul>
		</div>
	</div>
	
	<div class="row margin_top">
		<div class="col-md-12">	
			<form id="${modalId }_startForm" class="form-horizontal" role="form">
				<input type="hidden" id="${modalId }_startId" name="startId" value="${modalId }" />
				<div class="form-body">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane fade in active" id="${modalId }_startProperty">
					        <table id="${modalId }_flowStartTable"
					               data-toggle="table"
					               data-height="390"
					               data-show-refresh="true"
					               data-show-toggle="true"
					               data-show-columns="true"               
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
					                <th data-field="key" data-formatter="inputFormatter">KEY</th>
					                <th data-field="value" data-formatter="inputFormatter">值</th>
					                <th data-field="desc" data-formatter="inputFormatter">说明</th>
					            </tr>
					            </thead>
					        </table>							
						</div>
						<div class="tab-pane fade" id="${modalId }_startExplain">
							<div class="form-group">
								<div class="col-sm-12">
									<div class="fg-line">
										作为流程的开始标志外，拥有自定义流程中的全局变量作用，可在任何流程控件中采用${key}替换成value，其运行值如下：
														<pre>
{
	key:value,
	key:value,
	key:value, 
	......
}											
										</pre>
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