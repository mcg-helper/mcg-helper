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
				<li role="presentation" class="active"><a href="#${modalId }_modelField" data-toggle="tab">字段</a></li>
			  	<li role="presentation"><a href="#${modalId }_modelProperty" data-toggle="tab">属性</a></li>
			  	<li role="presentation"><a href="#${modalId }_explain" data-toggle="tab">说明</a></li>
			</ul>
		</div>
	</div>

	<div class="row margin_top">
		<div class="col-md-12">
			<form id="${modalId }_modelForm" class="form-horizontal" role="form">
				<div class="form-body">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane fade in active" id="${modalId }_modelField">
					        <table id="${modalId }_flowModelTable"
					               data-toggle="table"
					               data-height="390"
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
					            	<th data-field="comment" data-formatter="inputFormatter">说明</th>
					                <th data-field="classField" data-formatter="inputFormatter">类字段名称</th>
					                <th data-field="dataType" data-formatter="inputFormatter">程序类型</th>
					                <th data-field="tableField" data-formatter="inputFormatter">表字段名称</th>
					                <th data-field="include" data-formatter="inputFormatter">导入类</th>
					                <th data-field="tableFieldType" data-formatter="inputFormatter">表字段类型</th>
					                <th data-field="length" data-formatter="inputFormatter">长度</th>
					                <th data-field="precision" data-formatter="inputFormatter">精度</th>
					                <th data-field="primary" data-formatter="checkboxFormatter">主键</th>
					                <th data-field="autoincrement" data-formatter="checkboxFormatter">递增</th>
					                <th data-field="mandatory" data-formatter="checkboxFormatter">非空</th>
					                <th data-field="show" data-formatter="checkboxFormatter">显示</th>
					            </tr>
					            </thead>
					        </table>								
						</div>
						<div class="tab-pane fade" id="${modalId }_modelProperty">
							<div class="form-group">
								<label class="col-sm-2 control-label">控件名称</label>
								<div class="col-sm-9">
									<div class="fg-line">
										<input type="hidden" id="${modalId }_modelId" name="modelId" value="${modalId }" />
														<input type="text" id="${modalId }_modelName" name="modelProperty[modelName]" class="form-control" placeholder="请输入控件名称" />											
													</div>
												</div>
												<div class="col-sm-1"></div>
											</div>							
											<div class="form-group">
												<label class="col-sm-2 control-label">控件KEY</label>
												<div class="col-sm-9">
													<div class="fg-line">
														<input type="text" id="${modalId }_key" name="modelProperty[key]" class="form-control" placeholder="请输入KEY" />											
													</div>
												</div>
												<div class="col-sm-1"></div>
											</div>		
											<div class="form-group">
												<label class="col-sm-2 control-label">表名</label>
												<div class="col-sm-9">
													<div class="fg-line">
														<input type="text" id="${modalId }_tableName" name="modelProperty[tableName]" class="form-control" placeholder="请输入表名称" />											
													</div>
												</div>
												<div class="col-sm-1"></div>
											</div>													
											<div class="form-group">
												<label class="col-sm-2 control-label">类名</label>
												<div class="col-sm-9">
													<div class="fg-line">
														<input type="text" id="${modalId }_className" name="modelProperty[className]" class="form-control" placeholder="请输入类名称" />											
													</div>
												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">包名</label>
												<div class="col-sm-9">
													<div class="fg-line">
														<input type="text" id="${modalId }_packageName" name="modelProperty[packageName]" class="form-control" placeholder="请输入包名称" />											
													</div>
												</div>
												<div class="col-sm-1"></div>
											</div>																
											<div class="form-group">
												<label class="col-sm-2 control-label">描述</label>
												<div class="col-sm-9">
													<div class="fg-line">
														<textarea id="${modalId }_modelDesc" name="modelProperty[modelDesc]" rows="1" cols="" class="form-control"></textarea>
													</div>
												</div>
												<div class="col-sm-1"></div>					
											</div>
										</div>	
										<div class="tab-pane fade" id="${modalId }_explain">
											<div class="form-group">
												<div class="col-sm-12">
													<div class="fg-line">
														自定义简单数据对象，便于在其它控件中使用其数据，流程中运行值如下：<br/>
														<pre>
{
  "控件key":{
    "record":[
      {
        "autoincrement":"是否自增, boolean类型",  "classField":"程序变量名, String类型",       "comment":"字段说明, String类型",
        "dataType":"程序类型, Integer类型",       "include":"导入类型, String类型",           "length":"数据库长度, Integer类型",
        "mandatory":"是否非空, boolean类型",      "precision":"字段的小数位精度, Integer类型",  "primary":"是否为主键, boolean类型",
        "show":"是否显示, boolean类型",           "tableField":"表字段名称, String类型",       "tableFieldType":"表字段类型, String类型"
      },
			......
    ],
    "property":{
      "className":"类名, String类型",      "key":"控件key, String类型",      "modelName":"控件名称, String类型",
      "packageName":"包名, String类型",    "tableName":"表名, String类型"
    }
  }
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