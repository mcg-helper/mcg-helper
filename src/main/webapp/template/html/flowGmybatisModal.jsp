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
				<li role="presentation" class="active"><a href="#${modalId }_relation" data-toggle="tab">关联表</a></li>				
			    <li role="presentation"><a href="#${modalId }_gmybatisProperty" data-toggle="tab">属性</a></li>
			  	<li role="presentation"><a href="#${modalId }_gmybatisExplain" data-toggle="tab">说明</a></li>
			</ul>
		</div>
	</div>

	<div class="row margin_top">
		<div class="col-md-12">
			<form id="${modalId }_gmybatisForm" class="form-horizontal" role="form">
				<input type="hidden" id="${modalId }_gmybatisId" name="gmybatisId" value="${modalId }" />
				<div class="form-body">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane fade in active" id="${modalId }_relation">
							<div class="form-group">
								<label class="col-sm-3 control-label">选择数据源</label>
								<div class="col-sm-9">
									<div class="fg-line">
							          	<select id="${modalId }_dataSourceId" name="relation[flowDataSourceId]" class="selectpicker">
							          		<option value="">请选择</option>
						                	<c:forEach items="${mcgDataSources}" var="item">
							              		<option value="${item.dataSourceId }">${item.name }</option>
							              	</c:forEach>
										          	</select>											
												</div>
											</div>
										</div>	
									        <table id="${modalId }_flowGmybatisTable"
									               data-toggle="table"
									               data-height="350"
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
									                <th data-field="tableName">表名</th>
									                <th data-field="entityName" data-formatter="inputFormatter">实体类名</th>
									                <th data-field="daoName" data-formatter="inputFormatter">DAO名称</th>
									                <th data-field="xmlName" data-formatter="inputFormatter">XML名称</th>
									                <th data-field="selected" data-formatter="checkboxFormatter" data-align="center">是否生成</th>
									            </tr>
									            </thead>
									        </table>
									</div>						
									<div class="tab-pane fade" id="${modalId }_gmybatisProperty">
										<div class="form-group">
											<label class="col-sm-3 control-label">控件名称</label>
											<div class="col-sm-8">
												<div class="fg-line">
													<input type="text" id="${modalId }_name" name="gmybatisProperty[name]" class="form-control"  />
												</div>
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group">
											<label class="col-sm-3 control-label">控件Key</label>
											<div class="col-sm-8">
												<div class="fg-line">
													<input type="text" id="${modalId }_key" name="gmybatisProperty[key]" class="form-control"  />
												</div>
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group">
											<label class="col-sm-3 control-label">POJO工程路径</label>
											<div class="col-sm-8">
												<div class="fg-line">
													<input type="text" id="${modalId }_pojoProjectPath" name="gmybatisProperty[pojoProjectPath]" class="form-control"  />
												</div>
											</div>
											<div class="col-sm-1"></div>
										</div>							
										<div class="form-group">
											<label class="col-sm-3 control-label">POJO包路径</label>
											<div class="col-sm-8">
												<div class="fg-line">
													<input type="text" id="${modalId }_pojoOutPath" name="gmybatisProperty[pojoOutPath]" class="form-control"  />
												</div>
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group">
											<label class="col-sm-3 control-label">xml工程路径</label>
											<div class="col-sm-8">
												<div class="fg-line">
													<input type="text" id="${modalId }_xmlProjectPath" name="gmybatisProperty[xmlProjectPath]" class="form-control"  />
												</div>
											</div>
											<div class="col-sm-1"></div>
										</div>								
										<div class="form-group">
											<label class="col-sm-3 control-label">xml包路径</label>
											<div class="col-sm-8">
												<div class="fg-line">
													<input type="text" id="${modalId }_xmlOutPath" name="gmybatisProperty[xmlOutPath]" class="form-control"  />
												</div>
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group">
											<label class="col-sm-3 control-label">DAO工程路径</label>
											<div class="col-sm-8">
												<div class="fg-line">
													<input type="text" id="${modalId }_daoProjectPath" name="gmybatisProperty[daoProjectPath]" class="form-control"  />
												</div>
											</div>
											<div class="col-sm-1"></div>
										</div>								
										<div class="form-group">
											<label class="col-sm-3 control-label">DAO包路径</label>
											<div class="col-sm-8">
												<div class="fg-line">
													<input type="text" id="${modalId }_daoOutPath" name="gmybatisProperty[daoOutPath]" class="form-control"  />
												</div>
											</div>
											<div class="col-sm-1"></div>
										</div>																
									</div>
									<div class="tab-pane fade" id="${modalId }_gmybatisExplain">
										<div class="form-group">
											<div class="col-sm-12">
												<div class="fg-line">
													由数据库表反向生成mybaits相关文件（model类、example类、xml映射文件、dao文件），且流程执行的运行值表信息对象数据如下：
													<pre>
{	"控件key":										
	{
		"类名":{
			"record":[
				{
					"autoincrement":"是否自增, boolean类型",     "classField":"程序变量名, String类型", 
					"comment":"字段说明, String类型",            "dataType":"程序类型, Integer类型",
					"include":"导入类型, String类型",            "length":"数据库长度, Integer类型",
					"mandatory":"是否非空, boolean类型",         "precision":"字段的小数位精度, Integer类型",
					"primary":"是否为主键, boolean类型",          "show":"是否显示, boolean类型",
					"tableField":"表字段名称, String类型",        "tableFieldType":"表字段类型,  String类型"
				},
				......
			]
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