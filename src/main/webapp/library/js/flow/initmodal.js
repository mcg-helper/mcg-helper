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

var _fieldName_ = "", _tableName_="";

function checkboxFormatter(value, row, index) {
	var checkboxStr = '<input type="checkbox" onChange="checkboxChange(this, ' + index + ');"';
	if(value == true || value == 1) {
		checkboxStr += ' checked ';
	}
	checkboxStr += '/>';
	return checkboxStr;
}

function checkboxChange(obj, index) {
	
	$("#" + _tableName_).bootstrapTable("updateCell", {"index":index, "field":_fieldName_, "value":obj.checked, "reinit":false });
/*	
	var updateData =  '{"'+ _fieldName_ + '":' + $(obj).is(':checked') + '}';
	$("#" + _tableName_).bootstrapTable('updateRow', {"index":index, "row":JSON.parse(updateData) });
	*/
}

function inputFormatter(value, row, index) {
	return '<input class="form-control input-sm" type="text" value="' + value + '" onfocus="inputFocus(this)" onChange="inputBlur(this, ' + index+ ')"/>';
}

function dsCommandsFormatter(value, row, index) {
	return '<button type="button" onClick="dbTest(' + index + ');" class="btn btn-default">测试</button>';
}

function ssCommandsFormatter(value, row, index) {
	return '<button type="button" onClick="serverTest(' + index + ');" class="btn btn-default">测试</button>';
}

function dbTest(index) {
	var tableData = $("#" + baseMap.get("flowDataSourceModalId") + "_flowDataSourceTable").bootstrapTable('getData');
	var row = tableData[index];
	common.ajax({
		url : "/flow/testConnect",
		type : "POST",
		data : JSON.stringify(row),
		contentType : "application/json"
	}, function(data) {
		if(data.statusCode == 1) {
			Messenger().post({
				message: "【" + row.name + "】连接数据库成功！",
				type: "success",
				hideAfter: 5,
			 	showCloseButton: true
			});	
		} else {
			Messenger().post({
				message: "【" + row.name + "】连接数据库失败！",
				type: "error",
				hideAfter: 5,
			 	showCloseButton: true
			});	
		}
		
	});
}

function serverTest(index) {
	var tableData = $("#" + baseMap.get("flowDataSourceModalId") + "_flowServerSourceTable").bootstrapTable('getData');
	var row = tableData[index];
	common.ajax({
		url : "/global/testServerConnect",
		type : "POST",
		data : JSON.stringify(row),
		contentType : "application/json"
	}, function(data) {
		if(data.statusCode == 1) {
			Messenger().post({
				message: row.name +"【" + row.ip + ":" + row.port + "】连接成功！",
				type: "success",
				hideAfter: 5,
			 	showCloseButton: true
			});	
		} else {
			Messenger().post({
				message: row.name +"【" + row.ip + ":" + row.port + "】连接失败！",
				type: "error",
				hideAfter: 5,
			 	showCloseButton: true
			});	
		}
		
	});
}

function inputFocus(obj) {
	$(obj).click();
}

function inputBlur(obj, index) {
	$("#" + _tableName_).bootstrapTable("updateCell", {"index":index, "field":_fieldName_, "value":$(obj).val(), "reinit":false });
}

function getSelectData(cellValue, index, url, param) {
	var result = "";
	common.ajax({
		url : url,
		type : "GET",
		data : param,
		async : false,
		contentType : "application/json"
	}, function(data) {
		var selectStr = '<select onChange="selectChange(this, ' + index + ');">';
		for(var key in data){
			selectStr += '<option value="' + data[key] + '"';
			if(cellValue == data[key]) {
				selectStr += ' selected="selected" ';
			}
			selectStr += '>' + key + '</option>';
		}
		selectStr += '</select>';
		result = selectStr;
	});	
	return result;
}

function dbTypeSelectFormatter(value, row, index) {
	var selectStr = getSelectData(value, index, "/common/getDatabaseTypes", null);
	return selectStr;	
}

function serverTypeSelectFormatter(value, row, index) {
	var selectStr = getSelectData(value, index, "/common/getServerTypes", null);
	return selectStr;	
}

function selectChange(obj, index) {
	var rowData =  '{"'+ _fieldName_ + '":"' + $(obj).val() + '"}';
	$("#" + _tableName_).bootstrapTable('updateRow', {"index":index, "row":JSON.parse(rowData)  });
}

function getElementDataById(id, func) {
	common.ajax({
		url : "/common/getMcgProductById",
		type : "POST",
		data : "id="+id
	}, func);
}

function initFlowDataSourceModal(modalId) {
	common.ajax({
		url : "/global/getMcgGlobal",
		type : "POST",
		data : null
	}, function(data){
		if(data != null && data != undefined && data.flowDataSources != null && data.flowDataSources.length > 0) {
			var rows = [];
			for(var i=0; i<data.flowDataSources.length; i++){
				var dataSource = data.flowDataSources[i];
				rows.push({
						"id":dataSource.dataSourceId, "_bTableName_":modalId + "_flowDataSourceTable", "name":dataSource.name, "dbType":dataSource.dbType,  
						"dbServer":dataSource.dbServer, "dbPort":dataSource.dbPort, "dbName":dataSource.dbName, "dbUserName":dataSource.dbUserName, 
						"dbPwd":dataSource.dbPwd, "note":dataSource.note 
					});
			}
			$("#" + modalId + "_flowDataSourceTable").bootstrapTable({"data":rows});
		} else {
			$("#" + modalId + "_flowDataSourceTable").bootstrapTable({data: [
				{
					"id":Math.uuid(), "_bTableName_":modalId + "_flowDataSourceTable", "name":"", "dbType":"MYSQL",  
					"dbServer":"", "dbPort":"", "dbName":"", "dbUserName":"", 
					"dbPwd":"", "note":""
				}    
			]});
		}
		
		$("#" + modalId + "_flowDataSourceTable").on('click-cell.bs.table', function ($element, field, value, row) {
			_fieldName_ = field;
			_tableName_ = modalId + "_flowDataSourceTable";
		});		
		
		if(data != null && data != undefined && data.serverSources != null && data.serverSources.length > 0) {
			var rows = [];
			for(var i=0; i<data.serverSources.length; i++){
				var serverSource = data.serverSources[i];
				rows.push({
						"id":serverSource.id, "_bTableName_":modalId + "_flowServerSourceTable", "name":serverSource.name, "type":serverSource.type,  
						"ip":serverSource.ip, "port":serverSource.port, "userName":serverSource.userName, 
						"pwd":serverSource.pwd, "note":serverSource.note 
					});
			}
			$("#" + modalId + "_flowServerSourceTable").bootstrapTable({"data":rows});
		} else {

			$("#" + modalId + "_flowServerSourceTable").bootstrapTable({data: [
       			{
       				"id":Math.uuid(), "_bTableName_":modalId + "_flowServerSourceTable", "name":"", "type":"LINUX",  
       				"ip":"", "port":"", "userName":"", "pwd":"", "note":""
       			}
       		]});			
		}		
		
		$("#" + modalId + "_flowServerSourceTable").on('click-cell.bs.table', function ($element, field, value, row) {
			_fieldName_ = field;
			_tableName_ = modalId + "_flowServerSourceTable";
		});
		return ;
	});		
}

function initFlowStartModal(id) {
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.startProperty!= undefined && data.startProperty.var.length > 0) {
			var rows = [];
			for(var i=0; i<data.startProperty.var.length; i++){
				rows.push({"id":Math.uuid() , "key":data.startProperty.var[i].key, "value":data.startProperty.var[i].value, "desc":data.startProperty.var[i].desc });
			}
			$("#" + id + "_flowStartTable").bootstrapTable({"data":rows});
		} else {
			$("#" + id + "_flowStartTable").bootstrapTable({data: [{ "id": Math.uuid(), "key": "", "value": "", "desc": "" }]});
		}		
		
	});
}

function initFlowModelModal(id) {
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.modelField != undefined && data.modelField.modelRecord.length > 0) {
			var rows = [];
			for(var i=0; i<data.modelField.modelRecord.length; i++){
				rows.push({
					"id":Math.uuid(), "classField":data.modelField.modelRecord[i].classField, "tableField":data.modelField.modelRecord[i].tableField, "comment":data.modelField.modelRecord[i].comment, "tableFieldType":data.modelField.modelRecord[i].tableFieldType, 
					"dataType":data.modelField.modelRecord[i].dataType, "length":data.modelField.modelRecord[i].length, "precision":data.modelField.modelRecord[i].precision, "primary":data.modelField.modelRecord[i].primary, 
					"autoincrement":data.modelField.modelRecord[i].autoincrement, "mandatory":data.modelField.modelRecord[i].mandatory, "show":data.modelField.modelRecord[i].show, "include":data.modelField.modelRecord[i].include 
				});
			}
			$("#" + id + "_flowModelTable").bootstrapTable({"data":rows});
			$("#" + id + "_key").val(data.modelProperty.key);
			$("#" + id + "_modelName").val(data.modelProperty.modelName);
			$("#" + id + "_className").val(data.modelProperty.className);
			$("#" + id + "_tableName").val(data.modelProperty.tableName);
			$("#" + id + "_packageName").val(data.modelProperty.packageName);
			$("#" + id + "_modelDesc").val(data.modelProperty.modelDesc);
		} else {
			$("#" + id + "_flowModelTable").bootstrapTable({data: [{
				"id":Math.uuid(), "classField":"", "tableField":"", "comment":"","tableFieldType":"",
				"dataType":"", "length":0, "precision":0, "primary":false, "autoincrement":false, "mandatory":false, "show":false, "include":"" 
			}]});
		}		
		
	});	
	
}

function initGmybatisModal(id) {
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.relation != undefined) {
			if(data.relation.tables != undefined && data.relation.tables.length > 0) {
				var rows = [];
				for(var i=0; i<data.relation.tables.length; i++){
					rows.push({"id":Math.uuid(), "tableName":data.relation.tables[i].tableName, "entityName":data.relation.tables[i].entityName,
						"daoName":data.relation.tables[i].daoName, "xmlName":data.relation.tables[i].xmlName, "selected":true });
				}
				$("#" + id + "_flowGmybatisTable").bootstrapTable({"data":rows});
			} else {
				$("#" + id + "_flowGmybatisTable").bootstrapTable();
			}
			$("#" + id + "_name").val(data.gmybatisProperty.name);
			$("#" + id + "_key").val(data.gmybatisProperty.key);
			$("#" + id + "_pojoProjectPath").val(data.gmybatisProperty.pojoProjectPath);
			$("#" + id + "_xmlProjectPath").val(data.gmybatisProperty.xmlProjectPath);
			$("#" + id + "_daoProjectPath").val(data.gmybatisProperty.daoProjectPath);			
			$("#" + id + "_pojoOutPath").val(data.gmybatisProperty.pojoOutPath);
			$("#" + id + "_xmlOutPath").val(data.gmybatisProperty.xmlOutPath);
			$("#" + id + "_daoOutPath").val(data.gmybatisProperty.daoOutPath);
			$("#" + id + "_dataSourceId").selectpicker('val', data.relation.dataSourceId);
		} else {
			$("#" + id + "_flowGmybatisTable").bootstrapTable();
		}
		
	});
}

function initDataModal(id) {
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.dataField != undefined && data.dataField.dataRecord.length > 0) {
			
			var rows = [];
			for(var i=0; i<data.dataField.dataRecord.length; i++){
				rows.push({
					"id":Math.uuid(), "classField":data.dataField.dataRecord[i].classField, "tableField":data.dataField.dataRecord[i].tableField, "comment":data.dataField.dataRecord[i].comment, "tableFieldType":data.dataField.dataRecord[i].tableFieldType, 
					"dataType":data.dataField.dataRecord[i].dataType, "length":data.dataField.dataRecord[i].length, "precision":data.dataField.dataRecord[i].precision, "primary":data.dataField.dataRecord[i].primary, 
					"autoincrement":data.dataField.dataRecord[i].autoincrement, "mandatory":data.dataField.dataRecord[i].mandatory, "include":data.dataField.dataRecord[i].include 
				});
			}			
			$("#" + id + "_flowDataTable").bootstrapTable({"data":rows});
			$("#" + id + "_key").val(data.dataProperty.key);
			$("#" + id + "_name").val(data.dataProperty.name);
			$("#" + id + "_className").val(data.dataProperty.className);
			$("#" + id + "_tableName").val(data.dataProperty.tableName);
			$("#" + id + "_packageName").val(data.dataProperty.packageName);
			$("#" + id + "_dataDesc").val(data.dataProperty.dataDesc);		
			$("#" + id + "_dataSourceId").selectpicker('val', data.dataField.dataSourceId);
			
			if(data.dataField.dataSourceId != null && data.dataField.dataSourceId != "" && data.dataField.tableName != null && data.dataField.tableName != "" ) {
				common.ajax({
					url : "/common/getTableByDataSourceId",
					type : "POST",
					data : "dataSourceId=" + data.dataField.dataSourceId,
				}, function(result) {
					$("#" + id + "_tableNameSelect").find('option').remove();
					$("#" + id + "_tableNameSelect").append($('<option value="">请选择</option>'));
					for(var i=0; i<result.length; i++) {
			 			$("#" + id + "_tableNameSelect").append($('<option value="' + result[i].tableName + '">' + result[i].tableName + '</option>'));
					}
					$("#" + id + "_tableNameSelect").selectpicker('refresh');
					$("#" + id + "_tableNameSelect").selectpicker('val', data.dataField.tableName);
				});
			}			
		} else {
			$("#" + id + "_flowDataTable").bootstrapTable();
		}
		
	});
}

function initJsonModal(id, editor) {
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.jsonProperty != undefined) {
			$("#" + id +"_key").val(data.jsonProperty.key);
			$("#" + id +"_name").val(data.jsonProperty.name);
			$("#" + id +"_jsonDesc").val(data.jsonProperty.jsonDesc);
		} 
		if(data != "" && data != undefined && data.jsonCore != null) {
			editor.setValue(data.jsonCore.source);
		} else {
			editor.setValue("");
		}
	});
}

function initSqlQueryModal(id, editor) {
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.sqlQueryProperty != undefined) {
			$("#" + id +"_key").val(data.sqlQueryProperty.key);
			$("#" + id +"_name").val(data.sqlQueryProperty.name);
			$("#" + id +"_desc").val(data.sqlQueryProperty.desc);
		} 
		if(data != "" && data != undefined && data.sqlQueryCore != null) {
			$("#" + id + "_dataSourceId").selectpicker('val', data.sqlQueryCore.dataSourceId);
			editor.setValue(data.sqlQueryCore.source);
		} else {
			editor.setValue("");
		}
	});
}

function initSqlExecuteModal(id, editor) {
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.sqlExecuteProperty != undefined) {
			$("#" + id +"_key").val(data.sqlExecuteProperty.key);
			$("#" + id +"_name").val(data.sqlExecuteProperty.name);
			$("#" + id +"_desc").val(data.sqlExecuteProperty.desc);
		} 
		if(data != "" && data != undefined && data.sqlExecuteCore != null) {
			$("#" + id + "_dataSourceId").selectpicker('val', data.sqlExecuteCore.dataSourceId);
			editor.setValue(data.sqlExecuteCore.source);
		} else {
			editor.setValue("");
		}
	});
}

function initScriptModal(id, editor) {
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.scriptProperty != undefined) {
			$("#" + id + "_key").val(data.scriptProperty.key);
			$("#" + id + "_scriptName").val(data.scriptProperty.scriptName);
			$("#" + id + "_scriptDesc").val(data.scriptProperty.scriptDesc);
		} 
		if(data != "" && data != undefined && data.scriptCore != null) {
			editor.setValue(data.scriptCore.source);
		} else {
			editor.setValue("var Console = Java.type('com.mcg.plugin.assist.Console');\r\nvar console = new Console();\r\n\r\nfunction main(param) {\r\n    var result = {};\r\n    console.success({'myName':'mcg-helper', 'desc':'欢迎使用MCG小助手'});\r\n    \r\n    result = param;\r\n    return result;\r\n}");
		}
	});
}

function initJavaModal(id, editor) {
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.javaProperty != undefined) {
			$("#" + id + "_key").val(data.javaProperty.key);
			$("#" + id + "_name").val(data.javaProperty.name);
			$("#" + id + "_javaDesc").val(data.javaProperty.javaDesc);
		} 
		if(data != "" && data != undefined && data.javaCore != null) {
			editor.setValue(data.javaCore.source);
		} else {
			editor.setValue("import com.alibaba.fastjson.JSON;\r\nimport com.alibaba.fastjson.JSONArray;\r\nimport com.alibaba.fastjson.JSONObject;\r\nimport com.mcg.plugin.assist.Console;\r\n\r\npublic class Controller {\r\n    private Console console = new Console();\r\n\r\n    public JSON execute(JSON param) {\r\n        console.info(\"-----欢迎使用---------\");\r\n\r\n\r\n\r\n        return param;\r\n    }\r\n}\r\n");
		}
	});
}

function initPythonModal(id, editor) {
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.pythonProperty != undefined) {
			$("#" + id + "_key").val(data.pythonProperty.key);
			$("#" + id + "_name").val(data.pythonProperty.name);
			$("#" + id + "_pythonDesc").val(data.pythonProperty.pythonDesc);
		} 
		if(data != "" && data != undefined && data.pythonCore != null) {
			editor.setValue(data.pythonCore.source);
		} else {
			editor.setValue("#param为Dictionary类型\r\ndef main(param):\r\n\r\n    return param");
		}
	});
}

function initLinuxModal(id, editor) {
	
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.linuxProperty != undefined) {
			$("#" + id + "_key").val(data.linuxProperty.key);
			$("#" + id + "_name").val(data.linuxProperty.name);
			$("#" + id + "_desc").val(data.linuxProperty.desc);
		} 
		if(data != "" && data != undefined && data.linuxCore != null) {
			editor.setValue(data.linuxCore.source);
			$("#" + id + "_serverSourceId").selectpicker('refresh');
			$("#" + id + "_serverSourceId").selectpicker('val', data.linuxCore.serverSourceId);
		} else {
			editor.setValue("");
		}
	});
}

function initTextModal(id, editor) {
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.textProperty != undefined) {
			$("#" + id + "_name").val(data.textProperty.name);
			$("#" + id + "_key").val(data.textProperty.key);
			$("#" + id + "_fileName").val(data.textProperty.fileName);
			$("#" + id + "_outPutPath").val(data.textProperty.outPutPath);
		} 
		if(data != null && data != undefined && data.textCore != undefined) {
			editor.setValue(data.textCore.source);
		}
	});
}

function initFlowEndModal(id) {
	getElementDataById(id, function(data) {
		if(data != null && data != "" && data != undefined && data.endProperty != undefined) {
			$("#" + id + "_comment").val(data.endProperty.comment);
		}
	});
}