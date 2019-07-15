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
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width">
		<link rel="shortcut icon" type="image/png" href="../../img/favicon.png">
        <title>mcg-helper研发助手</title>
		
	</head>
	
	<body>
		<div id="mcg_body" class="container-fluid autoHeight">
			<div class="row autoHeight">
				<div class="col-md-12">
			        <table id="wontonListTable"
			               data-toggle="table"
			               data-height="460"
			               data-show-columns="true"
			               data-search="true" 
			               data-unique-id="host"      
				           data-pagination="true"
				           data-page-size="5"
				           data-page-list="[5,10,20,30]"
				           data-pagination-first-text="上一页"
				           data-pagination-pre-text="上一页"
				           data-pagination-next-text="下一页"
				           data-pagination-last-text="尾页">
			            <thead>
			            <tr>
			            	<th data-field="host">HOST</th>
			            	<th data-field="pid">PID</th>
			            	<th data-field="instancecode">实例编码</th>
			            	<th data-field="startTime" data-formatter="dateFormat">启动时间</th>
			            	<th data-field="time" data-formatter="dateFormat">心跳检测时间</th>
			            	<th data-field="version">版本号</th>
			            	<th data-field="state" data-formatter="wontonStateFormat">状态</th>
			            	<th data-field="commands" data-formatter="wontonCommandsFormatter">操作</th>
			            </tr>
			            </thead>
			        </table>
				</div>
			</div>
	 	</div>
	</body>
</html>
<script type="text/javascript">

$(function () {
	var wontonListTable = $("#wontonListTable").bootstrapTable({});
 		
	common.ajax({
		url : "/wonton/getAll",
		type : "POST",
		data : null,
		contentType : "application/json"
	}, function(data) {
		if(data != null && data.statusCode == 1) {
			if(data.resultMap != null && data.resultMap.dataList != null) {
				for(var i=0; i<data.resultMap.dataList.length; i++) {
					wontonListTable.bootstrapTable('append', data.resultMap.dataList[i]);
				}
			}
		} else {
			Messenger().post({
				message: data.statusMes,
				type: "error",
				hideAfter: 5,
			 	showCloseButton: true
			});
		}
		
	});  	
});

function wontonCommandsFormatter(value, row, index) {
	  	var btns = '<div class="btn-group">'
	  			+ '<button type="button" onClick="wontonPublish(\'' + row.host + '\', \'' + row.state + '\');" class="btn btn-default">控制中心</button>'
				+ '<button type="button" onClick="wontonRule(\'' + row.host + '\', \'' + row.state + '\');" class="btn btn-default">当前规则</button>'
				+ '<button type="button" onClick="wontonRecoveryNet(\'' + row.instancecode + '\', \'' + row.state + '\');" class="btn btn-default">网络恢复</button>'
				+ '<button type="button" onClick="wontonRecoveryHardware(\'' + row.instancecode + '\', \'' + row.state + '\');" class="btn btn-default">硬件恢复</button>'
				+ '<button type="button" onClick="deleteWontonInstance(\'' + row.host + '\',\'' + row.instancecode + '\');" class="btn btn-default">删除</button>'
				+ '</div>';

	return btns;
}

function wontonRecoveryNet(instanceCode, state) {
	
 	if(state != "normal") {
		Messenger().post({
			message: "客户端" + instanceCode + "实例不存活，不能发送指令！",
			type: "error",
			hideAfter: 5,
		 	showCloseButton: true
		});
		return;
	}
	
	common.ajax({
		url : "/wonton/recoveryNet",
		type : "POST",
		data : "instanceCode=" + instanceCode
	}, function(data) {
		if(data != null && data.statusCode == 1) {
			Messenger().post({
				message: data.statusMes,
				type: "success",
				hideAfter: 5,
			 	showCloseButton: true
			});
		} else {
			Messenger().post({
				message: data.statusMes,
				type: "error",
				hideAfter: 5,
			 	showCloseButton: true
			});
		}
		
	});
}

function wontonRecoveryHardware(instanceCode, state) {
	
 	if(state != "normal") {
		Messenger().post({
			message: "客户端" + instanceCode + "实例不存活，不能发送指令！",
			type: "error",
			hideAfter: 5,
		 	showCloseButton: true
		});
		return;
	}
	common.ajax({
		url : "/wonton/recoveryHardware",
		type : "POST",
		data : "instanceCode=" + instanceCode
	}, function(data) {
		if(data != null && data.statusCode == 1) {
			Messenger().post({
				message: data.statusMes,
				type: "success",
				hideAfter: 5,
			 	showCloseButton: true
			});
		} else {
			Messenger().post({
				message: data.statusMes,
				type: "error",
				hideAfter: 5,
			 	showCloseButton: true
			});
		}
		
	});
}

function deleteWontonInstance(host, instanceCode) {
	common.ajax({
		url : "/wonton/deleteInstance",
		type : "POST",
		data : "instanceCode=" + instanceCode
	}, function(data) {
		if(data != null && data.statusCode == 1) {
			$("#wontonListTable").bootstrapTable('remove', {
                field: 'host',
                values: [host]
            });
			Messenger().post({
				message: data.statusMes,
				type: "success",
				hideAfter: 5,
			 	showCloseButton: true
			});
		} else {
			Messenger().post({
				message: data.statusMes,
				type: "error",
				hideAfter: 5,
			 	showCloseButton: true
			});
		}
		
	});
}

function wontonPublish(host, state) {
	var row = $("#wontonListTable").bootstrapTable('getRowByUniqueId', host);
	if(state != "normal") {
		Messenger().post({
			message: "客户端" + row.instancecode + "实例不存活，不能发送规则！",
			type: "error",
			hideAfter: 5,
		 	showCloseButton: true
		});
		return;
	}	
	var param = {"modalId":Math.uuid() + "_wonton_publish_Modal", "wontonHeart":row, "option":{"title":("控制中心--" + row.instancecode + "--" + row.version), "width":1100} };
	common.showAjaxDialog("/wonton/publishModal", setWontonPublishDialogBtns(param), createWontonPublishModalCallBack, null, param);
}

function wontonRule(host, state) {
	var row = $("#wontonListTable").bootstrapTable('getRowByUniqueId', host);
	if(state != "normal") {
		Messenger().post({
			message: "客户端" + row.instancecode + "实例不存活，不能获取当前规则！",
			type: "error",
			hideAfter: 5,
		 	showCloseButton: true
		});
		return;
	}
	
	var param = {"modalId":Math.uuid() + "_wonton_rule_Modal", "option":{"title":("当前规则--"  + row.instancecode + "--" + row.version), "width":1100} };
	common.ajax({
		url : "/wonton/getRule",
		type : "POST",
		data : JSON.stringify(row),
		contentType : "application/json"
	}, function(data) {
		if(data != null && data.statusCode == 1) {
			param["rule_text"] = JSON.stringify(JSON.parse(data.resultMap.rule), null, 4);
			common.showAjaxDialog("/wonton/ruleModal", setWontonRuleDialogBtns(param), createWontonRuleModalCallBack, null, param);			
		} else {
			Messenger().post({
				message: data.statusMes,
				type: "error",
				hideAfter: 5,
			 	showCloseButton: true
			});
		}
		
	});
	
}

function createWontonPublishModalCallBack(param) {

	$(".selectpicker").selectpicker({
		noneSelectedText: "请选择",
		liveSearch: true,
		width:"100%"
	});

	$('#'+ param.modalId + '_wonton_publish_net_tab input[type=checkbox]').bootstrapSwitch({
		animate:true
	}).on('switchChange.bootstrapSwitch', function(event, state) {
        $(this).val(state);
    });
	
 	$("#"+ param.modalId + "_tab a").each(function(){
		$(this).on("shown.bs.tab", function (e) {
	    	var href = $(this).attr("href");
	    	var id = href.substring(href.indexOf("#") + 1);
	    	$('#'+id + ' input[type=checkbox]').bootstrapSwitch({
	    		animate:true
	    	}).on('switchChange.bootstrapSwitch', function(event, state) {
	            $(this).val(state);
	        });
	    	if("true" == $('#'+id + ' input[type=checkbox]').val() ) {
	    		$('#'+id + ' input[type=checkbox]').bootstrapSwitch('state', true);
	    	}
	     });
	});
 	
	common.ajax({
		url : "/wonton/getWontonPublish",
		type : "POST",
		data : "instancecode=" + param.wontonHeart.instancecode
	}, function(data) {
		if(data != null && data.statusCode == 1) {
			var wontonPublish = data.resultMap.wontonPublish;
			if(wontonPublish != null) {
				common.formUtils.setValues(param.modalId + "_wonton_publish_form", wontonPublish);
				if(wontonPublish.netRule.TargetIp != null) {
					$("#" + param.modalId + "_targetIps").val(wontonPublish.netRule.TargetIps.join(","));
				}
				if(wontonPublish.netRule.TargetPorts != null) {
					$("#" + param.modalId + "_targetPorts").val(wontonPublish.netRule.TargetPorts.join(","));
				}
				if(wontonPublish.netRule.TargetProtos != null) {
					$("#" + param.modalId + "_targetProtos").val(wontonPublish.netRule.TargetProtos.join(","));
				}
				if("true" == $('#'+ param.modalId + '_wonton_publish_net_tab input[type=checkbox]').val()) {
					$('#'+ param.modalId + '_wonton_publish_net_tab input[type=checkbox]').bootstrapSwitch('state', true);
				}
			}
		} else {
			Messenger().post({
				message: data.statusMes,
				type: "error",
				hideAfter: 5,
			 	showCloseButton: true
			});
		}
		
	});
}

function createWontonRuleModalCallBack(param) {
	$("#"+ param.modalId + "rule_text").val(param["rule_text"]);
}

function setWontonPublishDialogBtns(param) {
	return buttons = [
		{
			class: "btn btn-primary",
			text: "发布",
			click: function() {
				var data = {};
				
				var data = JSON.parse($("#" + param.modalId + "_wonton_publish_form").serializeJSON());
				data.netRule["targetIps"] = data.netRule.TargetIps.split(",");
				data.netRule.TargetIps = data.netRule.TargetIps.split(",");
				data.netRule["targetPorts"] = data.netRule.TargetPorts.split(",");
				data.netRule.TargetPorts = data.netRule.TargetPorts.split(",");
				data.netRule["targetProtos"] = data.netRule.TargetProtos.split(",");
				data.netRule.TargetProtos = data.netRule.TargetProtos.split(",");
				data["wontonHeart"] = param.wontonHeart;
				var _this = this;
		
				common.ajax({
					url : "/wonton/publishRule",
					type : "POST",
					data : JSON.stringify(data),
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

function setWontonRuleDialogBtns(param) {
	return buttons = [
		{
			class: "btn btn-default",			
			text: "关闭",
			click: function() {
				$( this ).dialog( "destroy" );
			}
		}
	];
	
}

function dateFormat(value) {
	return common.dateUtils.getFormatDateByLong(value, "yyyy-MM-dd hh:mm:ss");
}

function wontonStateFormat(state) {
	
	if(state == "normal") {
		return "<span class='label label-sm label-success'>存活</span>";	
	} else if(state == "losed") {
		return "<span class='label label-sm label-danger'>失联</span>";	
	} else {
		return "<span class='label label-sm label-warning'>未知</span>";
	}
}

</script>
