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




/* 在body中创建Modal的html元素 
 * id 流程节点id
 * param json对象参数
 * */
function createHtmlModal(id, param) {
	removePopover();
	var modalId = createModalId(id);
	var url = null;
	if(param == null)
		param = {};
	var option = {};
	if($("#"+id).attr("eletype") != null && $("#"+id).attr("eletype") != undefined) {
		if($("#"+id).attr("eletype") == "model") {
			url = "/html/flowModelModal";
			option["title"] = "model控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "json") {
			url = "/html/flowJsonModal";
			option["title"] = "json控件";
			option["width"] = 1000;
		} else if($("#"+id).attr("eletype") == "sqlQuery") {
			url = "/html/flowSqlQueryModal";
			option["title"] = "sql查询";
			option["width"] = 1000;
		} else if($("#"+id).attr("eletype") == "sqlExecute") {
			url = "/html/flowSqlExecuteModal";
			option["title"] = "sql执行";
			option["width"] = 1000;
		} else if($("#"+id).attr("eletype") == "data") {
			url = "/html/flowDataModal";
			option["title"] = "data控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "start") {
			url = "/html/flowStartModal";
			option["title"] = "开始控件";
			option["width"] = 800;
		} else if($("#"+id).attr("eletype") == "text"){
			url = "/html/flowTextModal";
			option["title"] = "文本控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "gmybatis") {
			url = "/html/flowGmybatisModal";
			option["title"] = "mybatis控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "script") {
			url = "/html/flowScriptModal";
			option["title"] = "js控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "java") {
			url = "/html/flowJavaModal";
			option["title"] = "java控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "python") {
			url = "/html/flowPythonModal";
			option["title"] = "python控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "linux") {
			url = "/html/flowLinuxModal";
			option["title"] = "linux控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "end") {
			url = "/html/flowEndModal";
			option["title"] = "结束控件";
			option["width"] = 800;
		}

		param["modalId"] = modalId.replace(/_Modal/g, "");
		param["eletype"] = $("#"+id).attr("eletype");
		param["option"] = option;
		if(url != null)
			common.showAjaxDialog(url, null, createModalCallBack, null, param);
//			common.showAjaxModal(url, null, createModalCallBack, null, param);
	}
}

/**
 * 控件div的sign设置为"true"，代码该控件已保存且成功
 */
function changeElementSign(id, name) {
	elementMap.get(id).setSign("true");
	$("#name_" + id).html(name);
}

/**
 * 设置控件弹出框按钮及功能绑定
 * @param param
 * @returns 按钮集合
 */
function setDialogBtns(param) {
    var buttons = [];
	if(param.eletype == "start") {
		buttons = [
			{
				class: "btn btn-default",
				text: "增加",
				click: function() {
					var tableData = $("#" + param.modalId + "_flowStartTable").bootstrapTable('getData');
					for(var i=0; i<tableData.length; i++) {
						var row={};
						row["key"] = tableData[i].key;
						row["value"] = tableData[i].value;
						row["desc"] = tableData[i].desc;
						$("#" + param.modalId + "_flowStartTable").bootstrapTable('updateRow', {"index":i, "row":row  });
					} 			
		 			$("#" + param.modalId + "_flowStartTable").bootstrapTable('append', { "id": Math.uuid(), "key": "", "value": "", "desc": "" });
				}
			},
			{
				class: "btn btn-default",			
				text: "删除",
				click: function() {
		            var ids = $.map($("#" + param.modalId + "_flowStartTable").bootstrapTable('getSelections'), function (row) {
		                return row.id;
		            });
		            $("#" + param.modalId + "_flowStartTable").bootstrapTable('remove', {
		                field: 'id',
		                values: ids
		            });
				}
			},
			{
				class: "btn btn-default",			
				text: "清空",
				click: function() {
		 			$("#" + param.modalId + "_flowStartTable").bootstrapTable('removeAll');
				}
			},
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var tableData = $("#" + param.modalId + "_flowStartTable").bootstrapTable('getData');
					var rowsData = {var:[]};
					for(var i=0; i<tableData.length; i++) {
						var row={};
						row["key"] = tableData[i].key;
						row["value"] = tableData[i].value;
						row["desc"] = tableData[i].desc;
						rowsData.var.push(row);
					}			
					var data = $("#" + param.modalId + "_startForm").serializeJSON();
					var result = JSON.parse(data);
					result["startProperty"] = rowsData;
					common.ajax({
						url : "/flow/saveStart",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							changeElementSign(param.modalId, "流程开始");
							$( _this ).dialog( "destroy" );
						}
					});
				}
			},
			{
				class: "btn btn-default",			
				text: "关闭",
				click: function() {
					$( this ).dialog( "destroy" );
				}
			}
		];
	} else if(param.eletype == "end") {
		buttons = [
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var data = $("#" + param.modalId　+ "_endForm").serializeJSON();
					common.ajax({
						url : "/flow/saveFlowEnd",
						type : "POST",
						data : data,
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							changeElementSign(param.modalId, "流程结束");
							$( _this ).dialog( "destroy" );
						}
					});					
				}
			},
			{
				class: "btn btn-default",			
				text: "关闭",
				click: function() {
					$( this ).dialog( "destroy" );
				}
			}			
		];
	} else if(param.eletype == "model") {
		buttons = [
			{
				class: "btn btn-default",			
				text: "增加",
				click: function() {
					var tableData = $("#" + param.modalId + "_flowModelTable").bootstrapTable('getData');
					for(var i=0; i<tableData.length; i++) {
						var row={};
						row["classField"] = tableData[i].classField;
						row["tableField"] = tableData[i].tableField;
						row["comment"] = tableData[i].comment;
						row["tableFieldType"] = tableData[i].tableFieldType;
						row["dataType"] = tableData[i].dataType;
						row["length"] = tableData[i].length;
						row["precision"] = tableData[i].precision;
						row["primary"] = tableData[i].primary;
						row["autoincrement"] = tableData[i].autoincrement;
						row["mandatory"] = tableData[i].mandatory;
						row["show"] = tableData[i].show;
						row["include"] = tableData[i].include;
						$("#" + param.modalId + "_flowModelTable").bootstrapTable('updateRow', {"index":i, "row":row  });
					} 			
		 			
		 			$("#" + param.modalId + "_flowModelTable").bootstrapTable('append',
		 					{ "id":Math.uuid(), "name":"", "classField":"", "tableField":"", "comment":"","tableFieldType":"", "dataType":"", "length":0, 
		 				    "precision":0, "primary":false, "autoincrement":false, "mandatory":false, "show":false, "include":"" });					
				}
			},
			{
				class: "btn btn-default",			
				text: "删除",
				click: function() {
		            var ids = $.map($("#" + param.modalId + "_flowModelTable").bootstrapTable('getSelections'), function (row) {
		                return row.id;
		            });
		            $("#" + param.modalId + "_flowModelTable").bootstrapTable('remove', {
		                field: 'id',
		                values: ids
		            });					
				}
			},
			{
				class: "btn btn-default",			
				text: "清空",
				click: function() {
					$("#" + param.modalId + "_flowModelTable").bootstrapTable('removeAll');		 			
				}
			},
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var tableData = $("#" + param.modalId + "_flowModelTable").bootstrapTable('getData');
					var rowsData = {modelRecord:[]};
					for(var i=0; i<tableData.length; i++) {
						var row={};
						row["classField"] = tableData[i].classField;
						row["tableField"] = tableData[i].tableField;
						row["comment"] = tableData[i].comment;
						row["tableFieldType"] = tableData[i].tableFieldType;
						row["dataType"] = tableData[i].dataType;
						row["length"] = tableData[i].length;
						row["precision"] = tableData[i].precision;
						row["primary"] = tableData[i].primary;
						row["autoincrement"] = tableData[i].autoincrement;
						row["mandatory"] = tableData[i].mandatory;
						row["show"] = tableData[i].show;
						row["include"] = tableData[i].include;
						rowsData.modelRecord.push(row);
					}
					var data = $("#" + param.modalId + "_modelForm").serializeJSON();
					var result = JSON.parse(data);
					result["modelField"] = rowsData;
					
					common.ajax({
						url : "/flow/saveModel",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							changeElementSign(param.modalId, result.modelProperty.modelName);
							$( _this ).dialog( "destroy" );
						}
					});					
					
				}
			},
			{
				class: "btn btn-default",			
				text: "关闭",
				click: function() {
					$( this ).dialog( "destroy" );
				}
			}
		];		
	} else if(param.eletype == "json") {
		buttons = [
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var data = $("#" + param.modalId + "_jsonForm").serializeJSON();
					var result = JSON.parse(data);
					var jsonCore = {};
					jsonCore["source"] = param.editor.getValue();
					result["jsonCore"] = jsonCore; 
					common.ajax({
						url : "/flow/saveJson",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							changeElementSign(param.modalId, result.jsonProperty.name);
							$( _this ).dialog( "destroy" );
						}
					});					
				}
			},
			{
				class: "btn btn-default",			
				text: "关闭",
				click: function() {
					$( this ).dialog( "destroy" );
				}
			}
		];			
	} else if(param.eletype == "sqlQuery") {
		buttons = [
			{
				class: "btn btn-primary",
				text: "保存",
				click: function() {
					var _this = this;
					var data = $("#" + param.modalId + "_sqlQueryForm").serializeJSON();
					var result = JSON.parse(data);
					result.sqlQueryCore["source"] = param.editor.getValue(); 
					common.ajax({
						url : "/flow/saveSqlQuery",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							changeElementSign(param.modalId, result.sqlQueryProperty.name);
							$( _this ).dialog( "destroy" );
						}
					});					
				}
			},
			{
				class: "btn btn-default",			
				text: "关闭",
				click: function() {
					$( this ).dialog( "destroy" );
				}
			}
		];			
	} else if(param.eletype == "sqlExecute") {
		buttons = [
			{
				class: "btn btn-primary",
				text: "保存",
				click: function() {
					var _this = this;
					var data = $("#" + param.modalId + "_sqlExecuteForm").serializeJSON();
					var result = JSON.parse(data);
					result.sqlExecuteCore["source"] = param.editor.getValue(); 
					common.ajax({
						url : "/flow/saveSqlExecute",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							changeElementSign(param.modalId, result.sqlExecuteProperty.name);
							$( _this ).dialog( "destroy" );
						}
					});					
				}
			},
			{
				class: "btn btn-default",			
				text: "关闭",
				click: function() {
					$( this ).dialog( "destroy" );
				}
			}
		];			
	} else if(param.eletype == "data") {
		buttons = [
			{
				class: "btn btn-default",			
				text: "增加",
				click: function() {
					var tableData = $("#" + param.modalId + "_flowDataTable").bootstrapTable('getData');
					for(var i=0; i<tableData.length; i++) {
						var row={};
						row["classField"] = tableData[i].classField;
						row["tableField"] = tableData[i].tableField;
						row["comment"] = tableData[i].comment;
						row["tableFieldType"] = tableData[i].tableFieldType;
						row["dataType"] = tableData[i].dataType;
						row["length"] = tableData[i].length;
						row["precision"] = tableData[i].precision;
						row["primary"] = tableData[i].primary;
						row["autoincrement"] = tableData[i].autoincrement;
						row["mandatory"] = tableData[i].mandatory;
						row["include"] = tableData[i].include;
						$("#" + param.modalId + "_flowDataTable").bootstrapTable('updateRow', {"index":i, "row":row  });
					} 			
		 			
		 			$("#" + param.modalId + "_flowDataTable").bootstrapTable('append',
		 					{ "id":Math.uuid(), "name":"", "classField":"", "tableField":"", "comment":"","tableFieldType":"", "dataType":"", "length":0, 
		 				    "precision":0, "primary":false, "autoincrement":false, "mandatory":false, "include":"" });					
				}
			},
			{
				class: "btn btn-default",			
				text: "删除",
				click: function() {
		            var ids = $.map($("#" + param.modalId + "_flowDataTable").bootstrapTable('getSelections'), function (row) {
		                return row.id;
		            });
		            $("#" + param.modalId + "_flowDataTable").bootstrapTable('remove', {
		                field: 'id',
		                values: ids
		            });					
				}
			},
			{
				class: "btn btn-default",			
				text: "清空",
				click: function() {
					$("#" + param.modalId + "_flowDataTable").bootstrapTable('removeAll');
				}
			},
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var tableData = $("#" + param.modalId + "_flowDataTable").bootstrapTable('getData');
					var rowsData = [];
					for(var i=0; i<tableData.length; i++) {
						var row={};
						row["classField"] = tableData[i].classField;
						row["tableField"] = tableData[i].tableField;
						row["comment"] = tableData[i].comment;
						row["tableFieldType"] = tableData[i].tableFieldType;
						row["dataType"] = tableData[i].dataType;
						row["length"] = tableData[i].length;
						row["precision"] = tableData[i].precision;
						row["primary"] = tableData[i].primary;
						row["autoincrement"] = tableData[i].autoincrement;
						row["mandatory"] = tableData[i].mandatory;
						row["show"] = tableData[i].show;
						row["include"] = tableData[i].include;
						rowsData.push(row);
					}
					var data = $("#" + param.modalId + "_dataForm").serializeJSON();
					var result = JSON.parse(data);
					result.dataField["dataRecord"] = rowsData;			
					common.ajax({
						url : "/flow/saveData",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							changeElementSign(param.modalId, result.dataProperty.name);
							$( _this ).dialog( "destroy" );
						}
					});					
					
				}
			},
			{
				class: "btn btn-default",			
				text: "关闭",
				click: function() {
					$( this ).dialog( "destroy" );
				}
			}
		];			
	} else if(param.eletype == "gmybatis") {
		buttons = [
			{
				class: "btn btn-default",			
				text: "删除",
				click: function() {
		            var ids = $.map($("#" + param.modalId + "_flowGmybatisTable").bootstrapTable('getSelections'), function (row) {
		                return row.id;
		            });
		            $("#" + param.modalId + "_flowGmybatisTable").bootstrapTable('remove', {
		                field: 'id',
		                values: ids
		            });					
				}
			},
			{
				class: "btn btn-default",			
				text: "清空",
				click: function() {
					$("#" + param.modalId + "_flowGmybatisTable").bootstrapTable('removeAll');
				}
			},
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var data = $("#" + param.modalId + "_gmybatisForm").serializeJSON();
					var result = JSON.parse(data);
					var tableData = $("#" + param.modalId + "_flowGmybatisTable").bootstrapTable('getData');
					var tables = [];
					for(var i=0; i<tableData.length; i++) {
						if(tableData[i].selected == true) {
							var row={};
							row["tableName"] = tableData[i].tableName;
							row["entityName"] = tableData[i].entityName;
							row["daoName"] = tableData[i].daoName;
							row["xmlName"] = tableData[i].xmlName;
							tables.push(row);
						}
					}
					result["relation"].dataSourceId = $("#" + param.modalId + "_dataSourceId").val();
					result["relation"].tables = tables;
					common.ajax({
						url : "/flow/saveGmybatis",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							changeElementSign(param.modalId, result.gmybatisProperty.name);
							$( _this ).dialog( "destroy" );
						}
					});					
					
				}
			},
			{
				class: "btn btn-default",
				text: "关闭",
				click: function() {
					$( this ).dialog( "destroy" );
				}
			}
		];			
	} else if(param.eletype == "script") {
		buttons = [
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var data = $("#" + param.modalId + "_scriptForm").serializeJSON();
					var result = JSON.parse(data);
					var scriptCore = {};
					scriptCore["source"] = param.editor.getValue();
					result["scriptCore"] = scriptCore; 
					common.ajax({
						url : "/flow/saveScript",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							changeElementSign(param.modalId, result.scriptProperty.scriptName);
							$( _this ).dialog( "destroy" );
						}
					});					
					
					
				}
			},
			{
				class: "btn btn-default",			
				text: "关闭",
				click: function() {
					$( this ).dialog( "destroy" );
				}
			}
		];			
	} else if(param.eletype == "java") {
		buttons = [
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var data = $("#" + param.modalId + "_javaForm").serializeJSON();
					var result = JSON.parse(data);
					var javaCore = {};
					javaCore["source"] = param.editor.getValue();
					result["javaCore"] = javaCore; 
					common.ajax({
						url : "/flow/saveJava",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							changeElementSign(param.modalId, result.javaProperty.name);
							$( _this ).dialog( "destroy" );
						}
					});
					
					
				}
			},
			{
				class: "btn btn-default",			
				text: "关闭",
				click: function() {
					$( this ).dialog( "destroy" );
				}
			}
		];			
	} else if(param.eletype == "python") {
		buttons = [
					{
						class: "btn btn-primary",			
						text: "保存",
						click: function() {
							var _this = this;
							var data = $("#" + param.modalId + "_pythonForm").serializeJSON();
							var result = JSON.parse(data);
							var pythonCore = {};
							pythonCore["source"] = param.editor.getValue();
							result["pythonCore"] = pythonCore; 
							common.ajax({
								url : "/flow/savePython",
								type : "POST",
								data : JSON.stringify(result),
								contentType : "application/json"
							}, function(data) {
								if(data.statusCode == 1) {
									changeElementSign(param.modalId, result.pythonProperty.name);
									$( _this ).dialog( "destroy" );
								}
							});
							
						}
					},
					{
						class: "btn btn-default",			
						text: "关闭",
						click: function() {
							$( this ).dialog( "destroy" );
						}
					}
				];			
	} else if(param.eletype == "linux") {
		buttons = [
					{
						class: "btn btn-primary",			
						text: "保存",
						click: function() {
							var _this = this;
							var data = $("#" + param.modalId + "_linuxForm").serializeJSON();
							var result = JSON.parse(data);
							var linuxCore = {};
							linuxCore["source"] = param.editor.getValue();
							linuxCore["serverSourceId"] = $("#" + param.modalId + "_serverSourceId").val();
							result["linuxCore"] = linuxCore; 
							common.ajax({
								url : "/flow/saveLinux",
								type : "POST",
								data : JSON.stringify(result),
								contentType : "application/json"
							}, function(data) {
								if(data.statusCode == 1) {
									changeElementSign(param.modalId, result.linuxProperty.name);
									$( _this ).dialog( "destroy" );
								}
							});
							
						}
					},
					{
						class: "btn btn-default",			
						text: "关闭",
						click: function() {
							$( this ).dialog( "destroy" );
						}
					}
				];			
	} else if(param.eletype == "text") {
		buttons = [
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var data = $("#" + param.modalId + "_textForm").serializeJSON();
					var result = JSON.parse(data);
					var textCore = {};
					textCore["source"] = param.editor.getValue();
					result["textCore"] = textCore; 
					common.ajax({
						url : "/flow/saveText",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							changeElementSign(param.modalId, result.textProperty.name);
							$( _this ).dialog( "destroy" );
						}
					});					
					
				}
			},
			{
				class: "btn btn-default",			
				text: "关闭",
				click: function() {
					$( this ).dialog( "destroy" );
				}
			}
		];			
	} else if(param.eletype == "dataSource") {
		buttons = [
			{
				class: "btn btn-default",
				text: "增加",
				click: function() {
					
					$("#"+ param.modalId + "_tab").children("li").each(function() {
						if($(this).hasClass("active") == true){
							if($(this).attr("id") == "dbType") {
					 			$("#" + param.modalId + "_flowDataSourceTable").bootstrapTable('append',
				 					{ "id":Math.uuid(), "name":"", "dbType":"MYSQL", "dbServer":"", 
				 				    "dbPort":"", "dbName":"", "dbUserName":"",  "dbPwd":"", "note":"" });								
							} else if($(this).attr("id") == "serverType") {
					 			$("#" + param.modalId + "_flowServerSourceTable").bootstrapTable('append',
	                          			{"id":Math.uuid(), "_bTableName_":param.modalId  + "_flowServerSourceTable", "name":"", "type":"LINUX",  
                      				"ip":"", "port":"", "userName":"", "pwd":"", "note":""});
					 			
							}
						}
					});
					
				}
			},
			{
				class: "btn btn-default",			
				text: "删除",
				click: function() {
					$("#"+ param.modalId + "_tab").children("li").each(function() {
						if($(this).hasClass("active") == true){
							if($(this).attr("id") == "dbType") {
					            var ids = $.map($("#" + param.modalId + "_flowDataSourceTable").bootstrapTable('getSelections'), function (row) {
					                return row.id;
					            });
					            $("#" + param.modalId + "_flowDataSourceTable").bootstrapTable('remove', {
					                field: 'id',
					                values: ids
					            });									
							} else if($(this).attr("id") == "serverType") {
					            var ids = $.map($("#" + param.modalId + "_flowServerSourceTable").bootstrapTable('getSelections'), function (row) {
					                return row.id;
					            });
					            $("#" + param.modalId + "_flowServerSourceTable").bootstrapTable('remove', {
					                field: 'id',
					                values: ids
					            });									
							}
						}
					});
					
				}
			},
			{
				class: "btn btn-default",			
				text: "清空",
				click: function() {
					$("#"+ param.modalId + "_tab").children("li").each(function() {
						if($(this).hasClass("active") == true){
							if($(this).attr("id") == "dbType") {
								$("#" + param.modalId + "_flowDataSourceTable").bootstrapTable('removeAll');
							} else if($(this).attr("id") == "serverType") {
								$("#" + param.modalId + "_flowServerSourceTable").bootstrapTable('removeAll');
							}
						}
					});
					
				}
			},
			{
				class: "btn btn-primary",
				text: "保存",
				click: function() {
					var _this = this;
					var global = {"flowDataSources":[], "serverSources":[]};
					var tableData = $("#" + param.modalId + "_flowDataSourceTable").bootstrapTable('getData');
					for(var i=0; i<tableData.length; i++) {
						var row={};
						row["dataSourceId"] =tableData[i].id;
						row["name"] =tableData[i].name;
						row["dbType"] = tableData[i].dbType;
						row["dbServer"] = tableData[i].dbServer;
						row["dbPort"] = tableData[i].dbPort;
						row["dbName"] = tableData[i].dbName;
						row["dbUserName"] = tableData[i].dbUserName;
						row["dbPwd"] = tableData[i].dbPwd;
						row["note"] = tableData[i].note;
						global.flowDataSources.push(row);
					}
					
					var flowServerSourceTableData = $("#" + param.modalId + "_flowServerSourceTable").bootstrapTable('getData');
					for(var i=0; i<flowServerSourceTableData.length; i++) {
						var row={};
						row["id"] =flowServerSourceTableData[i].id;
						row["name"] =flowServerSourceTableData[i].name;
						row["type"] = flowServerSourceTableData[i].type;
						row["ip"] = flowServerSourceTableData[i].ip;
						row["port"] = flowServerSourceTableData[i].port;
						row["userName"] = flowServerSourceTableData[i].userName;
						row["pwd"] = flowServerSourceTableData[i].pwd;
						row["note"] = flowServerSourceTableData[i].note;
						global.serverSources.push(row);
					}
					
					common.ajax({
						url : "/global/saveDataSource",
						type : "POST",
						data : JSON.stringify(global),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							$( _this ).dialog( "destroy" );
						}
						
					});					
					
				}
			},
			{
				class: "btn btn-default",			
				text: "关闭",
				click: function() {
					$( this ).dialog( "destroy" );
				}
			}
		];	
	}
	
	return buttons;
}

/* 在body中创建Modal后的回调函数
 * 由于多层使用modal后，无法在页面中执行js，所以可在回调中执行js
 *  */
function createModalCallBack(param) {
	if(param.eletype == "model") {

		$("#" + param.modalId + "_flowModelTable").on('click-cell.bs.table', function ($element, field, value, row) {
			_fieldName_ = field;
			_tableName_ = param.modalId + "_flowModelTable";
		});
		
		initFlowModelModal(param.modalId);
	} else if(param.eletype == "json") {
		var editor = ace.edit(param.modalId + "_editor");
		editor.setTheme("ace/theme/eclipse");
		editor.session.setMode("ace/mode/json");		 
		editor.setFontSize(18);
		editor.setReadOnly(false); 
		editor.setOption("wrap", "off");
		ace.require("ace/ext/language_tools");
		editor.setOptions({
			enableBasicAutocompletion: true,
			enableSnippets: true,
		    enableLiveAutocompletion: true
		});
		
		editorScreen($("#" + param.modalId + "_modalId").val(), editor);
		
		param["editor"] = editor;
		
		initJsonModal(param.modalId, editor);
	} else if(param.eletype == "sqlQuery") {
		$(".selectpicker").selectpicker({
			noneSelectedText: "请选择",
			liveSearch: true,
			width:"100%"
		});		
		
		var editor = ace.edit(param.modalId + "_editor");
		editor.setTheme("ace/theme/eclipse");
		editor.session.setMode("ace/mode/sql");		 
		editor.setFontSize(18);
		editor.setReadOnly(false); 
		editor.setOption("wrap", "off");
		ace.require("ace/ext/language_tools");
		editor.setOptions({
			enableBasicAutocompletion: true,
			enableSnippets: true,
		    enableLiveAutocompletion: true
		});
		
		editorScreen($("#" + param.modalId + "_modalId").val(), editor);
		
		param["editor"] = editor;	
		
		initSqlQueryModal(param.modalId, editor);
	} else if(param.eletype == "sqlExecute") {
		$(".selectpicker").selectpicker({
			noneSelectedText: "请选择",
			liveSearch: true,
			width:"100%"
		});		
		
		var editor = ace.edit(param.modalId + "_editor");
		editor.setTheme("ace/theme/eclipse");
		editor.session.setMode("ace/mode/sql");		 
		editor.setFontSize(18);
		editor.setReadOnly(false); 
		editor.setOption("wrap", "off");
		ace.require("ace/ext/language_tools");
		editor.setOptions({
			enableBasicAutocompletion: true,
			enableSnippets: true,
		    enableLiveAutocompletion: true
		});
		
		editorScreen($("#" + param.modalId + "_modalId").val(), editor);
		
		param["editor"] = editor;	
		
		initSqlExecuteModal(param.modalId, editor);
	} else if(param.eletype == "start") {
		$("#" + param.modalId + "_flowStartTable").on('click-cell.bs.table', function ($element, field, value, row) {
			_fieldName_ = field;
			_tableName_ = param.modalId + "_flowStartTable";
		});
 		initFlowStartModal(param.modalId);
 		
	} else if(param.eletype == "text") {
		var editor = ace.edit(param.modalId + "_editor");
		editor.setTheme("ace/theme/eclipse");
		editor.session.setMode("ace/mode/java");		 
		editor.setFontSize(18);
		//设置只读（true时只读，用于展示代码）
		editor.setReadOnly(false); 
		//自动换行,设置为off关闭
		editor.setOption("wrap", "off");
		//启用提示菜单
		ace.require("ace/ext/language_tools");
		editor.setOptions({
			enableBasicAutocompletion: true,
			enableSnippets: true,
		    enableLiveAutocompletion: true
		});
		editorScreen($("#" + param.modalId + "_textId").val(), editor);
		param["editor"] = editor;
		
		initTextModal(param.modalId, editor);
	} else if(param.eletype == "script") {
		var editor = ace.edit(param.modalId + "_editor");
		editor.setTheme("ace/theme/eclipse");
		editor.session.setMode("ace/mode/javascript");		 
		editor.setFontSize(18);
		editor.setReadOnly(false); 
		editor.setOption("wrap", "off");
		ace.require("ace/ext/language_tools");
		editor.setOptions({
			enableBasicAutocompletion: true,
			enableSnippets: true,
		    enableLiveAutocompletion: true
		});		
		editorScreen($("#" + param.modalId + "_scriptId").val(), editor);
		param["editor"] = editor;
		
		initScriptModal(param.modalId, editor);
	} else if(param.eletype == "java") {
		var editor = ace.edit(param.modalId + "_editor");
		editor.setTheme("ace/theme/eclipse");
		editor.session.setMode("ace/mode/java");		 
		editor.setFontSize(18);
		editor.setReadOnly(false); 
		editor.setOption("wrap", "off");
		ace.require("ace/ext/language_tools");
		editor.setOptions({
			enableBasicAutocompletion: true,
			enableSnippets: true,
		    enableLiveAutocompletion: true
		});		
		editorScreen($("#" + param.modalId + "_modalId").val(), editor);
		param["editor"] = editor;
		
		initJavaModal(param.modalId, editor);
	} else if(param.eletype == "python") {
		var editor = ace.edit(param.modalId + "_editor");
		editor.setTheme("ace/theme/eclipse");
		editor.session.setMode("ace/mode/python");		 
		editor.setFontSize(18);
		editor.setReadOnly(false); 
		editor.setOption("wrap", "off");
		ace.require("ace/ext/language_tools");
		editor.setOptions({
			enableBasicAutocompletion: true,
			enableSnippets: true,
		    enableLiveAutocompletion: true
		});		
		editorScreen($("#" + param.modalId + "_modalId").val(), editor);
		param["editor"] = editor;
		
		initPythonModal(param.modalId, editor);
	} else if(param.eletype == "linux") {
		$(".selectpicker").selectpicker({
			noneSelectedText: "请选择",
			liveSearch: true,
			width:"100%"
		});
		var editor = ace.edit(param.modalId + "_editor");
		editor.setTheme("ace/theme/eclipse");
		editor.session.setMode("ace/mode/sh");		 
		editor.setFontSize(18);
		editor.setReadOnly(false); 
		editor.setOption("wrap", "off");
		ace.require("ace/ext/language_tools");
		editor.setOptions({
			enableBasicAutocompletion: true,
			enableSnippets: true,
		    enableLiveAutocompletion: true
		});		
		editorScreen($("#" + param.modalId + "_modalId").val(), editor);
		param["editor"] = editor;
		initLinuxModal(param.modalId, editor);
	} else if(param.eletype == "data") {
		$(".selectpicker").selectpicker({
			noneSelectedText: "请选择",
			liveSearch: true,
			width:"100%"
		});
		
		$("#" + param.modalId + "_dataSourceId").change(function () {
			if($("#" + param.modalId + "_dataSourceId").val() != null && $("#" + param.modalId + "_dataSourceId").val() != "" ) {
				common.ajax({
					url : "/common/getTableByDataSourceId",
					type : "POST",
					data : "dataSourceId=" + $("#" + param.modalId + "_dataSourceId").val(),
				}, function(data) {
					$("#" + param.modalId + "_tableNameSelect").find('option').remove();
					$("#" + param.modalId + "_tableNameSelect").append($('<option value="">请选择</option>'));
					for(var i=0; i<data.length; i++) {
			 			$("#" + param.modalId + "_tableNameSelect").append($('<option value="' + data[i].tableName + '">' + data[i].tableName + '</option>'));
					}
					$("#" + param.modalId + "_tableNameSelect").selectpicker('refresh');
				});				
			}
			
		});
		$("#" + param.modalId + "_tableNameSelect").change(function () {
			if($("#" + param.modalId + "_dataSourceId").val() != null && $("#" + param.modalId + "_dataSourceId").val() != "" && $("#" + param.modalId + "_tableNameSelect").val() != null && $("#" + param.modalId + "_tableNameSelect").val() != "" ) {
				common.ajax({
					url : "/common/getTableInfo",
					type : "POST",
					data : "tableName=" + $("#" + param.modalId + "_tableNameSelect").val() + "&dataSourceId=" + $("#" + param.modalId + "_dataSourceId").val(),
				}, function(data) {
					$("#" + param.modalId + "_flowDataTable").bootstrapTable('removeAll');
					for(var i=0; i<data.length; i++){
			 			$("#" + param.modalId + "_flowDataTable").bootstrapTable('append', {"id":Math.uuid(), "classField":data[i].classField, "tableField":data[i].tableField, "comment": typeof(data[i].comment) == "undefined" ? "":data[i].comment , "tableFieldType":data[i].tableFieldType, 
								"dataType":data[i].dataType, "length":data[i].length, "precision":data[i].precision, "primary":data[i].primary, 
								"autoincrement":data[i].autoincrement, "mandatory":data[i].mandatory, "show":data[i].show, "include":data[i].include});						
					}
					var tableName = $("#" + param.modalId + "_tableNameSelect").find("option:selected").text();
					$("#" + param.modalId + "_tableName").val(tableName);
					$("#" + param.modalId + "_className").val(tools.convertClassName(tableName));					
				});
			}
			
		});
		
		$("#" + param.modalId + "_flowDataTable").on('click-cell.bs.table', function ($element, field, value, row) {
			_fieldName_ = field;
			_tableName_ = param.modalId + "_flowDataTable";
		}); 	
		
		initDataModal(param.modalId);
	} else if(param.eletype == "gmybatis") {
		$(".selectpicker").selectpicker({
			noneSelectedText: "请选择",
			width:"100%"
		});
		$("#" + param.modalId + "_dataSourceId").change(function () {
			if($("#" + param.modalId + "_dataSourceId").val() != null && $("#" + param.modalId + "_dataSourceId").val() != "" ) {
				common.ajax({
					url : "/common/getTableByDataSourceId",
					type : "POST",
					data : "dataSourceId=" + $("#" + param.modalId + "_dataSourceId").val(),
				}, function(data) {
					$("#" + param.modalId + "_flowGmybatisTable").bootstrapTable('removeAll');
					for(var i=0; i<data.length; i++) {
			 			$("#" + param.modalId + "_flowGmybatisTable").bootstrapTable('append', { "id": Math.uuid(), "tableName": data[i].tableName, "entityName": data[i].entityName,
			 				"daoName": data[i].daoName, "xmlName": data[i].xmlName, "selected": "" });					
					}
				});				
			}
		});

/* 		$("#" + param.modalId + "_addGmybatisBtn").click(function(){
 			$("#" + param.modalId + "_flowGmybatisTable").bootstrapTable('append', { "id": Math.uuid(), "tableName": "我的tableName", "entityName": "我的entityName",
 				"daoName": "我的daoName", "xmlName": "我的xmlName", "selected": "我的selected" });
 			
 		});*/
		$("#" + param.modalId + "_flowGmybatisTable").on('click-cell.bs.table', function ($element, field, value, row) {
			_fieldName_ = field;
			_tableName_ = param.modalId + "_flowGmybatisTable";
		}); 		
	
		initGmybatisModal(param.modalId);
	} else if(param.eletype == "end") {
		initFlowEndModal(param.modalId);
	}
}

/* 设置ace编辑器全屏功能   */
function editorScreen(id, editor) {
	editor.commands.addCommand({
		name: "fullscreen", bindKey: {
			win: "Ctrl-Enter", 
			mac: "Command-Enter"
		},
		exec: function(editor) {
			if(editor.isFullScreen == undefined) {
				editor.isFullScreen = false;
			}
			if(editor.isFullScreen) {

				$( "#dialog" + id ).dialog( "option", "resizable", true);
				$("#" + id + "_editor").height(acePropertyMap.get(id+"_height"));
				$( "#dialog" + id ).dialog( "option", "width", acePropertyMap.get(id+"_width") );
				$( "#dialog" + id ).dialog( "option", "height", acePropertyMap.get("#dialog" + id+"_width") );
				$( "#dialog" + id ).dialog( "option", "position", acePropertyMap.get(id+"_position") );
				editor.resize();
				editor.isFullScreen = false;
			} else {
				$( "#dialog" + id ).dialog( "option", "resizable", false);
				var oldWidth = $( "#dialog" + id ).dialog("option", "width");
				var oldHeight = $( "#dialog" + id ).dialog("option", "height");
				var position = $( "#dialog" + id ).dialog("option", "position");
				acePropertyMap.put(id+"_position", position);
				
				
				$( "#dialog" + id ).dialog( "option", "width", (screenMaxWidth - 50) );
				
				$( "#dialog" + id ).dialog( "option", "position", {
		      　　　　  	my: "center",
		      　　　　　　	at: "center",
		      　　　　　　	of: window,
		      　　　　　　	using: function( pos ) {
			      　　          	$(this).css({
		      　　　　　　　　          	"position":"fix",
		      　　　　　　　　		"top":"5px",
		      　　　　　　　　		"left": "10px"
		      　　　　　　		});
		      　　　　　　	}
	      　　　　		} );
				
				acePropertyMap.put(id+"_height", $("#" + id + "_editor").height());
				acePropertyMap.put(id+"_width", oldWidth);
				acePropertyMap.put("#dialog" + id+"_width", oldHeight);
				
				$( "#dialog" + id ).dialog( "option", "height", (screenMaxHeight - 15) );
				$("#" + id + "_editor").height(screenMaxHeight - 260);
				
				editor.resize();
				editor.isFullScreen = true;
				
				
			}
			
		}
	});
}