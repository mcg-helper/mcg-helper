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
		if($("#"+id).attr("eletype") == "json") {
			url = "/html/flowJsonModal";
			option["title"] = "json控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "sqlQuery") {
			url = "/html/flowSqlQueryModal";
			option["title"] = "sql查询";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "sqlExecute") {
			url = "/html/flowSqlExecuteModal";
			option["title"] = "sql执行";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "data") {
			url = "/html/flowDataModal";
			option["title"] = "data控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "start") {
			url = "/html/flowStartModal";
			option["title"] = "开始控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "text"){
			url = "/html/flowTextModal";
			option["title"] = "文本控件";
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
		} else if($("#"+id).attr("eletype") == "wonton") {
			url = "/html/flowWontonModal";
			option["title"] = "混沌控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "process") {
			url = "/html/flowProcessModal";
			option["title"] = "子流程控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "loop") {
			url = "/html/flowLoopModal";
			option["title"] = "循环控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "git") {
			url = "/html/flowGitModal";
			option["title"] = "GIT控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "sftp") {
			url = "/html/flowSftpModal";
			option["title"] = "SFTP控件";
			option["width"] = 1100;
		} else if($("#"+id).attr("eletype") == "end") {
			url = "/html/flowEndModal";
			option["title"] = "结束控件";
			option["width"] = 1100;
		}
		
		param["modalId"] = modalId.replace(/_Modal/g, "");
		param["eletype"] = $("#"+id).attr("eletype");
		param["option"] = option;
		if(url != null)
			common.showAjaxDialog(url, setDialogBtns(param), createModalCallBack, null, param);
	}
}

/**
 * 拖拽出的控件保存时，
 * 1、将name(控件名称),sign(控件是否保存)字段，同步到elementMap中对应的控件基本数据
 * 2、将拖拽控件下面的显示名称同步
 * @param elementId
 * @param name
 * @returns
 */
function saveElementUpdateCache(elementId, name) {
	var element = elementMap.get(elementId);
	element.setName(name);
	element.setSign("true");
	$("#name_" + elementId).html(name);
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
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveStart",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, "流程开始");
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
					var result = JSON.parse(data);
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveFlowEnd",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, "流程结束");
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
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveJson",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, result.jsonProperty.name);
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
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveSqlQuery",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, result.sqlQueryProperty.name);
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
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveSqlExecute",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, result.sqlExecuteProperty.name);
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
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveData",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, result.dataProperty.name);
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
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveScript",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, result.scriptProperty.scriptName);
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
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveJava",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, result.javaProperty.name);
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
							result["flowId"] = getCurrentFlowId();
							common.ajax({
								url : "/flow/savePython",
								type : "POST",
								data : JSON.stringify(result),
								contentType : "application/json"
							}, function(data) {
								if(data.statusCode == 1) {
									saveElementUpdateCache(param.modalId, result.pythonProperty.name);
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
							result.linuxCore["source"] = param.editor.getValue();
							result.linuxCore["connMode"] = $("#" + param.modalId + "_connMode").val();
							result.linuxCore["serverSourceId"] = $("#" + param.modalId + "_serverSourceId").val();
							result["flowId"] = getCurrentFlowId();
							common.ajax({
								url : "/flow/saveLinux",
								type : "POST",
								data : JSON.stringify(result),
								contentType : "application/json"
							}, function(data) {
								if(data.statusCode == 1) {
									saveElementUpdateCache(param.modalId, result.linuxProperty.name);
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
	} else if(param.eletype == "wonton") {
		buttons = [
			{
				class: "btn btn-primary",
				text: "保存",
				click: function() {
					var _this = this;
					var result = JSON.parse($("#" + param.modalId + "_wontonForm").serializeJSON());
					result.wontonNetRule["targetIps"] = result.wontonNetRule.TargetIps.split(",");
					result.wontonNetRule.TargetIps = result.wontonNetRule.TargetIps.split(",");
					result.wontonNetRule["targetPorts"] = result.wontonNetRule.TargetPorts.split(",");
					result.wontonNetRule.TargetPorts = result.wontonNetRule.TargetPorts.split(",");
					result.wontonNetRule["targetProtos"] = result.wontonNetRule.TargetProtos.split(",");
					result.wontonNetRule.TargetProtos = result.wontonNetRule.TargetProtos.split(",");
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveWonton",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, result.wontonProperty.name);
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
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveText",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, result.textProperty.name);
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
						if($(this).hasClass("active") == true) {
							if($(this).attr("id") == "varType") {
					 			$("#" + param.modalId + "_flowVarTable").bootstrapTable('append',
				 					{ "id":Math.uuid(), "key":"", "value":"", "note":"", "type":"flow" });	
							} else if($(this).attr("id") == "dbType") {
					 			$("#" + param.modalId + "_flowDataSourceTable").bootstrapTable('append',
				 					{ "id":Math.uuid(), "name":"", "dbType":"MYSQL", "dbServer":"", 
				 				    "dbPort":"", "dbName":"", "dbUserName":"",  "dbPwd":"", "note":"" });								
							} else if($(this).attr("id") == "serverType") {
					 			$("#" + param.modalId + "_flowServerSourceTable").bootstrapTable('append',
	                          			{"id":Math.uuid(), "_bTableName_":param.modalId  + "_flowServerSourceTable", "name":"", "type":"LINUX",  
                      				"ip":"", "port":"", "userName":"", "pwd":"", "secretKey":"", "note":""});
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
							
							if($(this).attr("id") == "varType") {
					            var ids = $.map($("#" + param.modalId + "_flowVarTable").bootstrapTable('getSelections'), function (row) {
					                return row.id;
					            });
					            $("#" + param.modalId + "_flowVarTable").bootstrapTable('remove', {
					                field: 'id',
					                values: ids
					            });									
							} else if($(this).attr("id") == "dbType") {
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
							if($(this).attr("id") == "varType") {
								$("#" + param.modalId + "_flowVarTable").bootstrapTable('removeAll');
							} else if($(this).attr("id") == "dbType") {
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
					var global = {"flowVars":[], "flowDataSources":[], "serverSources":[]};
					
					var tableData = $("#" + param.modalId + "_flowVarTable").bootstrapTable('getData');
					for(var i=0; i<tableData.length; i++) {
						if(tableData[i].type != "system") {
							var row={};
							row["type"] = tableData[i].type;
							row["id"] = tableData[i].id;
							row["key"] =tableData[i].key;
							row["value"] = tableData[i].value;
							row["note"] = tableData[i].note;
							global.flowVars.push(row);
						}
					}
					
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
						row["secretKey"] = flowServerSourceTableData[i].secretKey;
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
	} else if(param.eletype == "process") {
		buttons = [
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var data = $("#" + param.modalId + "_processForm").serializeJSON();
					var result = JSON.parse(data);
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveProcess",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, result.processProperty.name);
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
	} else if(param.eletype == "loop") {
		buttons = [
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var data = $("#" + param.modalId + "_loopForm").serializeJSON();
					var result = JSON.parse(data);
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveLoop",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, result.loopProperty.name);
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
	} else if(param.eletype == "git") {
		buttons = [
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var data = $("#" + param.modalId + "_gitForm").serializeJSON();
					var result = JSON.parse(data);
					var gitCore = {};
					result["flowId"] = getCurrentFlowId();
					common.ajax({
						url : "/flow/saveGit",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, result.gitProperty.name);
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
	} else if(param.eletype == "sftp") {
		buttons = [
			{
				class: "btn btn-default",			
				text: "增加",
				click: function() {
					var tableData = $("#" + param.modalId + "_flowSftpUploadTable").bootstrapTable('getData');
					for(var i=0; i<tableData.length; i++) {
						var row={};
						row["filePath"] = tableData[i].filePath;
						row["uploadPath"] = tableData[i].uploadPath;
						row["note"] = tableData[i].note;
						$("#" + param.modalId + "_flowSftpUploadTable").bootstrapTable('updateRow', {"index":i, "row":row  });
					} 			
		 			
		 			$("#" + param.modalId + "_flowSftpUploadTable").bootstrapTable('append',
		 					{ "id":Math.uuid(), "filePath":"", "uploadPath":"", "tableField":"", "note":"" });					
				}
			},
			{
				class: "btn btn-default",			
				text: "删除",
				click: function() {
		            var ids = $.map($("#" + param.modalId + "_flowSftpUploadTable").bootstrapTable('getSelections'), function (row) {
		                return row.id;
		            });
		            $("#" + param.modalId + "_flowSftpUploadTable").bootstrapTable('remove', {
		                field: 'id',
		                values: ids
		            });					
				}
			},
			{
				class: "btn btn-default",			
				text: "清空",
				click: function() {
					$("#" + param.modalId + "_flowSftpUploadTable").bootstrapTable('removeAll');
				}
			},
			{
				class: "btn btn-primary",			
				text: "保存",
				click: function() {
					var _this = this;
					var data = $("#" + param.modalId + "_sftpForm").serializeJSON();
					var result = JSON.parse(data);
					result.sftpProperty["connMode"] = $("#" + param.modalId + "_connMode").val();
					result.sftpProperty["serverSourceId"] = $("#" + param.modalId + "_serverSourceId").val();
					result["flowId"] = getCurrentFlowId();
					result["sftpUpload"] = {"sftpUploadRecords":[]};
					
					var rowsData = {var:[]};
					var flowSftpUploadTableData = $("#" + param.modalId + "_flowSftpUploadTable").bootstrapTable('getData');
					for(var i=0; i<flowSftpUploadTableData.length; i++) {
						var row={};
						row["id"] =flowSftpUploadTableData[i].id;
						row["filePath"] =flowSftpUploadTableData[i].filePath;
						row["uploadPath"] =flowSftpUploadTableData[i].uploadPath;
						row["note"] = flowSftpUploadTableData[i].note;
						result.sftpUpload.sftpUploadRecords.push(row);
					}
					
					common.ajax({
						url : "/flow/saveSftp",
						type : "POST",
						data : JSON.stringify(result),
						contentType : "application/json"
					}, function(data) {
						if(data.statusCode == 1) {
							saveElementUpdateCache(param.modalId, result.sftpProperty.name);
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
	
	if(param.eletype == "json") {
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
		$("#" + param.modalId + "_outMode").selectpicker({
			noneSelectedText: "请选择",
			width:"100%"
		});
		
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
		$("#" + param.modalId + "_connMode").selectpicker({
			noneSelectedText: "请选择",
			width:"100%"
		});
		$("#" + param.modalId + "_serverSourceId").selectpicker({
			noneSelectedText: "请选择",
			width:"100%"
		});
		
		$("#" + param.modalId + "_connMode").change(function(){
			if($(this).val() == "dependency") {
				$("#" + param.modalId + "_serverSourceId").prop("disabled", false);
				$("#" + param.modalId + "_serverSourceId").selectpicker('refresh');
				$("#" + param.modalId + "_ip_port").css("display", "none");
				$("#" + param.modalId + "_user_pwd").css("display", "none");
			} else if($(this).val() == "assign") {
				$("#" + param.modalId + "_serverSourceId").prop("disabled", true);
				$("#" + param.modalId + "_serverSourceId").selectpicker('refresh');
				$("#" + param.modalId + "_ip_port").css("display", "block");
				$("#" + param.modalId + "_user_pwd").css("display", "block");
			}
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
		
		$("#" + param.modalId + "_connMode").change(function(){
			
		});
		initLinuxModal(param.modalId, editor);
	} else if(param.eletype == "wonton") {

	 	$("#"+ param.modalId + "_tab a").each(function(){
			$(this).on("shown.bs.tab", function (e) {
		    	var href = $(this).attr("href");
		    	var id = href.substring(href.indexOf("#") + 1);
		    	$('#'+id + ' input[type=checkbox]').bootstrapSwitch({
		    		animate:true
		    	}).on('switchChange.bootstrapSwitch', function(event, state) {
		    		$(this).val(state);
		        });
		    	if($('#'+id + ' input[type=checkbox]').val() == "true") {
		    		$('#'+id + ' input[type=checkbox]').bootstrapSwitch('state', true);
		    	}
		     });
		});		
		
		$(".selectpicker").selectpicker({
			noneSelectedText: "请选择",
			liveSearch: true,
			width:"100%"
		});		
		
		initWontonModal(param.modalId);
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
	} else if(param.eletype == "process") {
		var setting = {
		        view: {
		            selectedMulti: false
		        },
		    	callback: {
		    		beforeClick: zTreeBeforeClick,
		    		onClick: flowSelected
		    	},
		        check: {
		            enable: false
		        },
		        data: {
		            simpleData: {
		                enable: true
		            }
		        }
		};

		common.ajax({
			url : "/flowTree/getDatas",
			type : "GET",
			data : null,
			contentType : "application/json"
		}, function(data) {
			var flowTreeName = param.modalId + "_flowTree";
	    	$.fn.zTree.init($("#" + flowTreeName), setting, data.topologys);
	    	var treeObj = $.fn.zTree.getZTreeObj(flowTreeName);
	    	treeObj.expandAll(true);
	    	
	    	initProcessModal(param.modalId, treeObj);
	    	
		});
		
	} else if(param.eletype == "loop") {
		$("#" + param.modalId + "_type").selectpicker({
			noneSelectedText: "请选择",
			width:"100%"
		});
	    initLoopModal(param.modalId);
	} else if(param.eletype == "git") {
		$("#" + param.modalId + "_mode").selectpicker({
			noneSelectedText: "请选择",
			width:"100%"
		});
	    initGitModal(param.modalId);
	} else if(param.eletype == "sftp") {
		$("#" + param.modalId + "_connMode").selectpicker({
			noneSelectedText: "请选择",
			width:"100%"
		});
		$("#" + param.modalId + "_serverSourceId").selectpicker({
			noneSelectedText: "请选择",
			width:"100%"
		});
		
		$("#" + param.modalId + "_connMode").change(function(){
			if($(this).val() == "dependency") {
				$("#" + param.modalId + "_serverSourceId").prop("disabled", false);
				$("#" + param.modalId + "_serverSourceId").selectpicker('refresh');
				$("#" + param.modalId + "_ip_port").css("display", "none");
				$("#" + param.modalId + "_user_pwd").css("display", "none");
			} else if($(this).val() == "assign") {
				$("#" + param.modalId + "_serverSourceId").prop("disabled", true);
				$("#" + param.modalId + "_serverSourceId").selectpicker('refresh');
				$("#" + param.modalId + "_ip_port").css("display", "block");
				$("#" + param.modalId + "_user_pwd").css("display", "block");
			}
		});
		
		$("#" + param.modalId + "_connMode").change(function(){
			
		});
		
		$("#" + param.modalId + "_flowSftpUploadTable").on('click-cell.bs.table', function ($element, field, value, row) {
			_fieldName_ = field;
			_tableName_ = param.modalId + "_flowSftpUploadTable";
		});
		
		initSftpModal(param.modalId, editor);
	} else if(param.eletype == "end") {
		
		initFlowEndModal(param.modalId);
	}
}

function zTreeBeforeClick(treeId, treeNode, clickFlag) {
	var result = false;
	if(treeNode.id !== $("#flowSelect").attr("flowId")) {
		result = true;
	} else {
		Messenger().post({
			message: "不能将当前编辑中的流程作为子流程！",
			type: "error",
			hideAfter: 5,
		 	showCloseButton: true
		});
	}
    return result;
}

function flowSelected(event, treeId, treeNode) {
	var modalFlowTreeId = treeNode.tId.substring(0, treeNode.tId.indexOf("_flowTree"));
	$("#" + modalFlowTreeId + "_flowName").html(treeNode.name+"&nbsp;<span class='caret'></span>");
	$("#" + modalFlowTreeId + "_flowId").val(treeNode.id);
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