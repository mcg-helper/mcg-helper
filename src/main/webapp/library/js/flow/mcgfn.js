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



/* 基本数据Map
 * selector: 当前拖拽控件id
 * runnerFlowId: 当前执行的流程id
 * runnerFlowElementId: 当前执行流程正在执行控件id
 * status: 0:流程区正常操作  1:重绘流程区时
 * highlight: 当前控制台日志高亮显示的id
 * flowDataSourceModalId 数据源弹出框的id，点击数据源时触发保存id
 */
var CANNOTUSE="developing, please waiting";
var baseMap = new Map();
/* 流程节点Map */
var elementMap = new Map();
/* html工具Map*/
var htmlToolsMap = new Map();
var sourceMap = new Map();
var targetMap = new Map();
var setting = {
        view: {
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,
            selectedMulti: false
        },
    	callback: {
    		beforeRename: beforeRename,
//    		beforeEditName: beforeEditName,
    		beforeRemove: beforeRemove,
//    		onRemove: onRemove,
    		onClick: onClick
    	},
        check: {
            enable: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        edit: {
            enable: true,
            showRemoveBtn: setRemoveBtn
        }
    };

$(function() {
	setAutoHeight($("body"), 500);
/*
	$('#mcg_toolbar').affix({
	   offset: {
		   bottom: 0
	  },
	  target: $("#flowarea")
	});

	$('#console_header').affix({
		offset: {
			bottom: 0
		},
		target: $("#flowarea")
	});
*/
	common.ajax({
		url : "/flowTree/getDatas",
		type : "GET",
		data : null,
		contentType : "application/json"
	}, function(data) {
    	$.fn.zTree.init($("#flowTree"), setting, data.topologys);
    	var treeObj = $.fn.zTree.getZTreeObj("flowTree");
    	treeObj.expandAll(true);

    	var rootNode = treeObj.getNodeByParam("id", data.selected.id, null);
    	nodeSelected(rootNode);
    	//  系统初始化
    	initFlowSystem();
	});

});

/*
 * 初始化流程控制台，主要以下：
 * 绑定按钮功能
 */
function initFlowConsole() {

	$("#flowStopBtn").click(function(){

		if($("#flowStopBtn span").is(".text-danger")) {
			common.ajax({
				url : "/flow/stop",
				type : "POST",
				data : "flowId=" + $("#flowSelect").attr("flowId")
			}, function(data) {
				if(data.statusCode == 1) {
					$("#flowStopBtn span").removeClass("text-danger");
				} else {
					Messenger().post({
						message: "停止流程失败！",
						type: "error",
						hideAfter: 5,
					 	showCloseButton: true
					});
				}
			});
		} else {
			Messenger().post({
				message: "流程未执行，无法停止！",
				type: "error",
				hideAfter: 5,
			 	showCloseButton: true
			});
		}
	});

	$("#flowLogCleanBtn").click(function() {
		$("#console").html("");
	});
}

/*
 *  绑定流程控件拖拽事件
 */
function flowDropBind() {
	baseMap.put("selector", "");
	baseMap.put("status", 0);
	$("#flowarea").droppable({
		drop: function( event, ui ) {
			//排除流程区中，非流程控件的html对象拖拽事件
			if(ui.draggable.attr("eletype") == undefined || ui.draggable.attr("eletype") == "undefined" ) {
				return ;
			} else if( ui.draggable.attr("eletype") != "text"){
				if(ui.draggable.attr("eletype" ) != "info") {
					alert(CANNOTUSE);
					return;
				}
			}

			// 从工具栏拖拽到流程区时
			if(ui.draggable.attr("clone") != "true" && ui.draggable.attr("eletype") != undefined) {
		    	var element = new $.DragWidget({ id:Math.uuid(), label:$("#"+ui.draggable.attr("id")).text() ,name:$("#"+ui.draggable.attr("id")).text(), classname:"w eletype",eletype:ui.draggable.attr("eletype"), clone:'true',left:ui.offset.left,top:ui.offset.top,sign:"false" });
		    	var divNode = $("<div data-toggle='popover' id=" + element.getId() + " name='' eletype=" + element.getEletype() + " class='" + element.getClassname() +"' clone='" + element.getClone() + "'>工具<div class='ep'></div></div>");
		 		divNode.css("left", element.getLeft()+"px");
		 		divNode.css("top", element.getTop()+"px");
		 		$(this).append(divNode);
		 		var xy = getXY(element.getId(),"false");
		 		element.setLeft(xy.x);
		 		element.setTop(xy.y);
		 		elementMap.put(element.getId(), element);
		 		setXY(element.getId(), xy);

		        //重绘流程区节点和连接线
				repaint(this);
		 		//初始化流程节点工具层
				initPopover();
		 		//初始化流程节点拖拽功能和连接线连接功能
				initConnectLine();
			} else { //在流程区中移动节点时，重新记录坐标
				var xy = getXY(ui.draggable.attr("id"),ui.draggable.attr("clone"));
				var element = elementMap.get(ui.draggable.attr("id"));
				element.setLeft(xy.x);
				element.setTop(xy.y);
				saveXY(ui.draggable.attr("id"), xy);
				elementMap.put(ui.draggable.attr("id"), element);

				tempId = element.getId();
				let tlist = sourceMap.get(element.getId());
				if(tlist != undefined) {
                    for (let i = 0; i < tlist.length; i++) {
                        let left = element.getLeft() + tlist[i].getLeft();
                        let top = element.getTop() + tlist[i].getTop();
                        alert("left: "+left + "\n top:"+ top );
                        $("#" + element.getId() + tlist[i].getId()).css("left", left / 2);
                        $("#" + element.getId() + tlist[i].getId()).css("top", top / 2);
                    }
                }


				let slist = targetMap.get(element.getId());
				if(slist != undefined) {
                    for (let i = 0; i < slist.length; i++) {
                        let left = element.getLeft() + slist[i].getLeft();
                        let top = element.getTop() + slist[i].getTop();
                        $("#" + slist[i].getId() + element.getId() ).css("left", left / 2);
                        $("#" + slist[i].getId() + element.getId() ).css("top", top / 2);
                    }
                }
			}
			$(".draggable").draggable({containment: "parent", zIndex: 100 }); //grid: [ 50, 20 ]
		}

	});
}

/* 计算流程节点坐标
 * (flag="true") 第一次生成节点时
 *  (flag="false") 在流程区中拖拽已有节点时
 */
function getXY(id, flag) {
	if(id == undefined || flag == undefined) {
		return false;
	}

	var xy = {x:0, y:0};
	if(flag != "true") {
//		xy.x = $("#"+id).position().left - $("#flowarea").offset().left;
//		xy.y = $("#"+id).position().top - $("#flowarea").offset().top;		
		xy.x = document.getElementById(id).offsetLeft + document.getElementById("flowarea").scrollLeft - $("#flowarea").offset().left;
		xy.y = document.getElementById(id).offsetTop + document.getElementById("flowarea").scrollTop - $("#flowarea").offset().top;
	} else {
		xy.x = document.getElementById(id).offsetLeft;
		xy.y = document.getElementById(id).offsetTop;
	}
	return xy;
}

//给流程节点绑定事件
function bindEvent(id) {
	$("#"+id).dblclick(function(){
		createHtmlModal(id, null);
	});
	$("#"+id).click(function(){
		baseMap.put("selector", id);
		removePopover(); //删除所有流程节点悬浮工具层
		$("#"+id).popover('show'); //显示流程节点悬浮工具层
	});

}
//删除所有流程节点县浮工具层
function removePopover() {
	$("#flowarea").children("div[data-toggle='popover']").each(function() {
		$(this).popover('hide');
	});
}
//初始化流程节点工具层
function initPopover() {
	$("#flowarea").children("[data-toggle='popover']").popover({
//		delay:{ show: 0, hide: 1000 },
		trigger:"manual",//manual,focus,click
		placement:"bottom",
//		title:"流程节点工具层",
		html:true,
		container: $("#flowarea"),
		animation: false,
		content:baseMap.get("popoverContent")
	}).on("mouseenter", function () {
		removePopover();
        var _this = this;
        $(this).popover("show");
        $(this).siblings(".popover").on("mouseleave", function () {
            $(_this).popover('hide');
        });
    }).on("mouseleave", function () {
        var _this = this;
        setTimeout(function () {
            if (!$(".popover:hover").length) {
                $(_this).popover("hide")
            }
        }, 100);
    }).on("show.bs.popover", function() {
    	var _this = this;
    	baseMap.put("selector", $(_this).attr("id"));
    });
}

/**
 * 控件悬浮工具功能（修改、删除、日志定位）
 * @param operate (edit:修改  delete:删除  logOut:日志定位)
 */
function suspend(operate) {
	if(operate == "edit") {
		createHtmlModal(baseMap.get("selector"), null);
	} else if(operate == "delete") {
		removePopover();
		removeElement(baseMap.get("selector"));
	} else if(operate == "logOut") {
		removePopover();
		alert(CANNOTUSE);
		return ;
		if(baseMap.get("selector") != null && baseMap.get("selector") != "" && baseMap.get("selector") != undefined) {
			if($("#log" + baseMap.get("selector")).length > 0) {
				if($("#" + baseMap.get("highlight")).length > 0) {
					$("#" + baseMap.get("highlight")).removeClass("highlight"); //把上次被高亮标记div去除高亮样式
				}
			    $("#highlight" + baseMap.get("selector")).addClass("highlight");
			    baseMap.put("highlight", "highlight" + baseMap.get("selector")); //把当前被高亮标记div的id放入缓存
			    var pos = $("#log" + baseMap.get("selector")).offset().top;
			//    $("html,body").animate({scrollTop: pos}, 100);
			    $("#mcg_flow").animate({scrollTop: (pos - 40)}, 100);
			//    return false;
			}
		}
	}/*	else if(operate == "set") {
		$(".dropdown-toggle").show();
	} else if(operate == "no") {
		removePopover();
		alert("nonono");
	} else if(operate == "yes") {
		removePopover();
		alert("yesyes");
	}*/
}

function clearAll() {
	//删除所有连接线
	if(baseMap.get("instance") != undefined)
		baseMap.get("instance").deleteEveryEndpoint();
	var array = elementMap.keySet();
	//将当前所有节点删除
	if(array.length > 0) {
		for(var i=0; i<array.length; i++) {
			$("#"+elementMap.get(array[i]).getId()).remove();
		}
	}
	elementMap = null;
	elementMap = new Map();

}
// use when import mcg data file
// 重绘流程区节点和连接线
function repaint(object) {
	//删除所有连接线
	if(baseMap.get("instance") != undefined)
		baseMap.get("instance").deleteEveryEndpoint();
	var array = elementMap.keySet();
	//将当前所有节点删除
	if(array.length > 0) {
		for(var i=0; i<array.length; i++) {
			$("#"+elementMap.get(array[i]).getId()).remove();
		}
	}
	removePopover();
	//重新创建当前所有节点
 	for(var i=0; i<array.length; i++) {
 		var name = elementMap.get(array[i]).getSign() == "true" || elementMap.get(array[i]).getSign() == undefined ? elementMap.get(array[i]).getName() : "";
 		var divNode = $("<div data-toggle='popover' id=" + elementMap.get(array[i]).getId() + " name='' eletype=" + elementMap.get(array[i]).getEletype() + " class='" + elementMap.get(array[i]).getClassname() +"' clone='" + elementMap.get(array[i]).getClone() + "'>" + elementMap.get(array[i]).getLabel() + "<div class='ep'></div><div id='name_" + elementMap.get(array[i]).getId() + "' style='position:absolute;top:55px;left:0px;width:135px;'>" + name + "</div></div>");
 		divNode.css("left", elementMap.get(array[i]).getLeft()+"px");
 		divNode.css("top", elementMap.get(array[i]).getTop()+"px");
 		$(object).append(divNode);
 		eventInterceptor("name_"　+ elementMap.get(array[i]).getId());
 	//	setXY(elementMap.get(array[i]).getId(), getXY(elementMap.get(array[i]).getId(), "false"));
 		bindEvent(elementMap.get(array[i]).getId());
	}
}

/* 通过流程节点id, 连接线id，删除指定的连接线缓存 */
function removeConnectorsById(elementId, connectorId) {
	var connectorMap = elementMap.get(elementId).getConnectorMap();
	elementMap.get(elementId).getConnectorMap().remove(connectorId);
/*	var connectorArray  = connectorMap.keySet();
	for(var i in connectorArray) {
		var connector = connectorMap.get(connectorArray[i]);
		elementMap.get(elementId).getConnectorMap().remove(connector.getConnectorId());
	}*/
}

/* 删除指定的流程节点中，包含的向下、向后连接线的缓存 */
function removeConnectorById(elementId) {
	var connectors = new Array();
	var array = elementMap.keySet();
	if(array.length > 0) {
		for(var i=0; i<array.length; i++) {
			var connectorMap = elementMap.get(array[i]).getConnectorMap();
			var connectorArray  = connectorMap.keySet();
			for(var k=0; k<connectorArray.length; k++) {
				var connector = connectorMap.get(connectorArray[k]);
				if(connector.getSourceId() == elementId)
					elementMap.get(elementId).getConnectorMap().remove(connector.getConnectorId());
				if(connector.getTargetId() == elementId)
					removeConnectorsById(connector.getSourceId(), connector.getConnectorId());
			}
		}
	}
	return connectors;
}

/* 获取所有连接线集合 
 * return connectors;
 * */
function getConnectors() {
	var connectors = new Array();
	var array = elementMap.keySet();
	if(array.length > 0) {
		for(var i=0; i<array.length; i++) {
			var connectorMap = elementMap.get(array[i]).getConnectorMap();
			var connectorArray  = connectorMap.keySet();
			for(var k=0; k<connectorArray.length; k++) {
				connectors.push(connectorMap.get(connectorArray[k]));
			}
		}
	}
	return connectors;
}

/* 根据当前流程节点，获取父级们的流程节点 
 * param elementId 当前流程节点id
 * return elements
 * */
function getParents(elementId) {
	var elements = new Array();
	var connectors = getConnectors(elementMap);
	for(var i=0; i<connectors.length; i++) {
		if(connectors[i].getTargetId() == elementId) {
			elements.push(elementMap.get(connectors[i].getSourceId()));
		}
	}
	return elements;
}

/* 根据流程节点类型，返回流程节点集合
 * param elements 流程节点集合
 * param eletype 流程节点类型
 * return elements 返回过滤后的流程节点集合
 * */
function getElementsByEletype(elements, eletype) {
	var result = new Array();
	for(var i=0; i<elements.length; i++) {
		if(elements[i].getEletype() == eletype) {
			result.push(elements[i]);
		}
	}
	return result;
}

/* 删除连接线时清除流程节点中connectorMap缓存数据 */
function removeConnector(id, connectorId) {
	if(elementMap.get(id) != undefined && elementMap.get(id) != null && elementMap.get(id) != "")
	elementMap.get(id).removeConnector(connectorId);
}
/* 删除流程节点在elementMap中的缓存 
 * elementId 流程节点的id
 * */
function reomveElementCache(elementId) {
	removeConnectorById(elementId)
	elementMap.remove(elementId);
}

/* 删除连接线html元素   */
function removeConnectorElement(instance, info) {
	var connectors = getConnectors(elementMap);
	for(var i=0; i<connectors.length; i++) {
		if(connectors[i].getConnectorId() == (info.sourceId+info.targetId)) {
			instance.detach(info);
			return ;
		}
	}
}
/* 流程节点A已连接流程节点B, 当B去连接A时, 删除该连接线 , 当B为循环控件时除外*/
function removeReverse(instance, info) {
	var connectors = getConnectors(elementMap);
	for(var i=0; i<connectors.length; i++) {
		if(connectors[i].getConnectorId() == (info.targetId+info.sourceId)) {
			if("loop" == $("#" + info.sourceId).attr("eletype")) {
				return true;
			} else {
				instance.detach(info);
			}
			return false;
		}
	}
	return true;
}


/* 将elementMap中的连接线，重绘到流程区 */
function repaintConnector(instance) {
	var tempValue = baseMap.get("status");
	var connectors = getConnectors();
	baseMap.put("status", 1);
	for(var i=0; i<connectors.length; i++) {
		instance.connect({ source: connectors[i].getSourceId(), target: connectors[i].getTargetId() });
		// if finish the import function, below code can be used for repaint connectors' properties. Maybe
		// var divNode = $("<div data-toggle='popover' id=" + connectors[i].getSourceId()+connectors[i].getTargetId() + " name='' eletype=" + element.getEletype() + " class='" + element.getClassname() +"' clone='" + element.getClone() + "'>工具<div class='ep'></div></div>");
		// divNode.css("left", element.getLeft()+"px");
		// divNode.css("top", element.getTop()+"px");
		// $(this).append(divNode);
	}
	baseMap.put("status",tempValue);
}

/* 重置流程节点坐标 */
function setXY(id, xy) {
	$("#"+id).css("left",xy.x + "px");
	$("#"+id).css("top",xy.y + "px");
}

/* 将流程节点的坐标保存elementMap中 */
function saveXY(id, xy) {
	var element = elementMap.get(id);
	element.setLeft(xy.x);
	element.setTop(xy.y);
	elementMap.put(id, element);
}

/* 删除流程节点(包含html元素和elementMap中的缓存) 
 * id 流程节点id
 * */
function removeElement(id) {
	if(baseMap.get("selector") != null && baseMap.get("selector") != "" && baseMap.get("selector") != undefined)
		$("#"+baseMap.get("selector")).remove();
	reomveElementCache(baseMap.get("selector"));
}

/* 初始化流程系统 */
function initFlowSystem() {
	//初始化返回顶部按钮
    $.goup({
        trigger: 100,
        bottomOffset: 150,
        locationOffset: 100,
        title: '返回顶部',
        titleAsText: true
    });

	/* 初始化下拉框控件 */
	$(".selectpicker").selectpicker({
		noneSelectedText: "请选择",
		liveSearch: true,
		width:"100%"
	});
	initToolbarDrag();
	initFunc();
	initHtmlTools();
	flowDropBind();
	initFlowConsole();
}

/* 初始化流程数据,从后台读取数据装配到elementMap中 */
function initFlowData(flowId) {
	initPopover();
	common.ajax({
		url : "/flow/getFlowData",
		type : "POST",
		data : "flowId=" + flowId
	}, function(data) {
		if(data != null && data != undefined && data.webElement != undefined && data.webElement.length > 0) {
			for(var i=0; i<data.webElement.length; i++) {
				var element = new $.DragWidget({
					id:data.webElement[i].id, label:data.webElement[i].label,
					name:data.webElement[i].name, classname:data.webElement[i].classname,eletype:data.webElement[i].eletype,
					clone:data.webElement[i].clone,left:data.webElement[i].left,top:data.webElement[i].top,sign:data.webElement[i].sign });
				elementMap.put(element.getId(), element);
			}
			for(var i=0; i<data.webConnector.length; i++) {
		    	var connector = new $.Connector({
		    		connectorId:data.webConnector[i].connectorId,
		    		sourceId:data.webConnector[i].sourceId,
		    		targetId:data.webConnector[i].targetId
		    	});
		    	elementMap.get(data.webConnector[i].sourceId).insertConnector(connector);
			}
			repaint($("#flowarea"));
			initConnectLine();
			initPopover();
		}
	});
}

/* 初始化拖拽控件区流程节点的拖拽功能 */
function initToolbarDrag() {
	$("#mcg_toolbar").find("div[eletype]").each(function(){
		$(this).draggable( {helper:"clone",revert: "invalid", cursor: 'pointer'});
	});
}

/* 初始化功能区按钮 */
function initFunc() {
	$('#flowSaveBtn').click(function(){
		alert (CANNOTUSE);
		// var webStruct = convertFlowObject();
		// common.ajax({
		// 	url : "/flow/saveWebStruct",
		// 	type : "POST",
		// 	data : JSON.stringify(webStruct),
		// 	contentType : "application/json"
		// }, function(data) {
		// 	if(data.statusCode != 1) {
		// 		Messenger().post({
		// 			message: "保存流程失败！",
		// 			type: "error",
		// 			hideAfter: 5,
		// 		 	showCloseButton: true
		// 		});
		// 	}
		// });
	});
	$('#flowGenBtn').click(function(){
		//清空控制台
		alert(CANNOTUSE);
		// $("#console").html("");
		// var array = elementMap.keySet();
		// for(var i in array) {
		// 	$("#name_" + array[i]).children("span").remove();
		// 	$("#name_" + array[i]).removeClass("run_state");
		// }
		// var webStruct = convertFlowObject();
		// $("#flowStopBtn span").addClass("text-danger");
		// common.ajax({
		// 	isLoading : true,
		// 	url : "/flow/generate",
		// 	type : "POST",
		// 	data : JSON.stringify(webStruct),
		// 	contentType : "application/json"
		// }, function(data) {
		// 	if(data.statusCode != 1) {
		// 		Messenger().post({
		// 			message: "执行流程失败！",
		// 			type: "error",
		// 			hideAfter: 5,
		// 		 	showCloseButton: true
		// 		});
		// 	}
		// });
	});
	$('#flowImpBtn').click(function(){
		alert(CANNOTUSE);
		return;
		var form = $("<form id='flowUploadForm' />");
		form.attr("style", "display:none");
		form.attr("method", "post");
//		form.attr("target", "");
		form.attr("enctype", "multipart/form-data");
		form.attr("action", baseUrl + "/tool/upload");
		var input = $("<input id='flowFile' name='flowFile'  type='file' onchange='uploadFlow()' style='display: none;'/>");
		var flowIdInput = $("<input name='flowId'  type='hidden' value='" + $("#flowSelect").attr("flowId") + "' />");
		form.append(input);
		form.append(flowIdInput);
		form.append($("#flowFile"));
		$("body").append(form);

		$("#flowFile").click();
	});
	$('#flowExpBtn').click(function(){
		delAll();
		//export data here
		acquireConnectors();
		if(!flowIsLegal()){
			alert("check is there a loop exist which would occurs error");
			return;
		}
		var form = $("<form>");
		form.attr("style", "display:none");
		form.attr("target", "");
		form.attr("method", "post");
		form.attr("action", baseUrl + "/tool/down");
		var  mapArray = elementMap.keySet();
		if(mapArray.length > 0) {
			var index = 0 ;
			while(index < mapArray.length) {
				var value = elementMap.get(mapArray[index++]);
				var flowIdInput = $("<input>");
				flowIdInput.attr("type", "hidden");
				flowIdInput.attr("name", "flowId_" + index);
				flowIdInput.attr("value", value.getId());
				form.append(flowIdInput);
			}
		}
		var flowIdInput = $("<input>");
		flowIdInput.attr("type","hidden");
		flowIdInput.attr("name","length");
		flowIdInput.attr("value",mapArray.length);
		form.append(flowIdInput);

		var flowIdInput = $("<input>");
		flowIdInput.attr("type","hidden");
		flowIdInput.attr("name","flowId");
		flowIdInput.attr("value",$("#flowSelect").attr("flowId"));
		form.append(flowIdInput);
		var flowNameInput = $("<input>");
		flowNameInput.attr("type","hidden");
		flowNameInput.attr("name","flowName");
		flowNameInput.attr("value",$("#flowSelect").attr("flowName"));
		form.append(flowNameInput);
		$("body").append(form);

		form.submit();
		form.remove();

	});
	$('#flowClearBtn').click(function(){
        var parentdiv=$('<div></div>');
        parentdiv.attr('id', "flowClear");
        $(parentdiv).html("<div style='height:50px;line-height:50px;'>清空数据不可恢复，您确定清空当前流程数据吗？</div>");
    	$(parentdiv).dialog({
    		title: "清空当前流程数据？",
    		resizable: false,
    		autoOpen: false,
    		closeOnEscape: false,
    		modal: true,
    		width: 350,
    		height: "auto",
    		position: { my: "center", at: "center", of: window  },
    		buttons: [
				{
					class: "btn btn-primary",
					text: "确定",
					click: function() {
						removeConnectDiv();
						var _this = this;
			    		common.ajax({
			    			url : "/flow/clearFlowData",
			    			type : "POST",
			    			data : "flowId=" + $("#flowSelect").attr("flowId")
			    		}, function(data) {
			    			if(data.statusCode == 1) {
			    				clearAll($("#flowarea"));
			    				$( _this ).dialog( "destroy" );
			    			} else {
			    				Messenger().post({
			    					message: "清空流程失败！",
			    					type: "error",
			    					hideAfter: 5,
			    				 	showCloseButton: true
			    				});
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
			]
    	}).on( "dialogclose", function( event, ui ) {
        	$(this).dialog( "destroy" );
    	});
    	$(parentdiv).dialog("open");

	});
	$('#flowDataSourceBtn').click(function(data){
		alert(CANNOTUSE);
		return;
		var modalId = createModalId("dataSource");
		var param = {};
		var option = {};
		option["title"] = "数据源管理";
		option["width"] = 1100;
		param["modalId"] = modalId.replace(/_Modal/g, "");
		param["eletype"] = "dataSource";
		param["option"] = option;
		common.showAjaxDialog("/html/flowDataSourceModal", setDialogBtns(param),
			function (data) {
				baseMap.put("flowDataSourceModalId", param.modalId);
				initFlowDataSourceModal(param.modalId);
			},
		null, param);

	});
}

/* 将elementMap中的缓存数据转换成WebStruct*/
function convertFlowObject() {
	var webStruct = "";
	var webElementArray = new Array();
	var webConnectorArray = new Array();
	var array = elementMap.keySet();
 	for(var i=0; i<array.length; i++) {
 		var data = elementMap.get(array[i]);
 		webElementArray.push(new $.WebElement({
 			id:data.getId(),
 			label:data.getLabel(),
 			name:data.getName(),
 			width:data.getWidth(),
 			height:data.getHeight(),
 			classname:data.getClassname(),
 			eletype:data.getEletype(),
 			clone:data.getClone(),
 			left:data.getLeft(),
 			top:data.getTop(),
 			sign:data.getSign()
 		}));

		var connectorMap = data.getConnectorMap();
		var connectorArray  = connectorMap.keySet();
		for(var k=0; k<connectorArray.length; k++) {
	 		webConnectorArray.push(new $.WebConnector({
	 			connectorId:connectorMap.get(connectorArray[k]).getConnectorId(),
	 			sourceId:connectorMap.get(connectorArray[k]).getSourceId(),
	 			targetId:connectorMap.get(connectorArray[k]).getTargetId()
	 		}));
		}
	}
 	webStruct = new $.WebStruct({flowId:$("#flowSelect").attr("flowId"), webElement:webElementArray, webConnector:webConnectorArray});
	return webStruct;
}

/* 初始化html工具 */
function initHtmlTools() {
	/* 设置流程节点悬浮层html */
	var dataHtml = setHtmlTool("/html/flowSuspension", null);
	baseMap.put("popoverContent", dataHtml);
}

/* 生成Modal的id */
function createModalId(elementId) {
//	return elementId + "_" + $("#"+elementId).attr("eletype");
	return elementId + "_Modal";
}

/*
 * 设置工具的的html url 请求地址 param json对象参数
 */
function setHtmlTool(url, param) {
	if(url == null || url == "") {
		return ;
	}
	var result = "";
	common.ajax(
		{
			"url":url,
			"type":"get",
			"dataType":"html",
			"data":param,
			"async":false
		}, function(data){
			result = data;
		}
	);
	return result;
}

/* 流程区控件的名称过滤掉父级绑定的事件(点击、双击、鼠标按下、mouseenter) */
function eventInterceptor(id) {
	$("#" + id).click(function(e){
		if (e && e.stopPropagation) {//非IE浏览器
			e.stopPropagation();
		} else {//IE浏览器
			window.event.cancelBubble = true;
		}
	});
	$("#" + id).dblclick(function(e){
		if (e && e.stopPropagation) {
			e.stopPropagation();
		} else {
			window.event.cancelBubble = true;
		}
	});
	$("#" + id).mousedown(function(e){
		if (e && e.stopPropagation) {
			e.stopPropagation();
		} else {
			window.event.cancelBubble = true;
		}
	});
	$("#" + id).mouseenter(function(e){
		if (e && e.stopPropagation) {
			e.stopPropagation();
		} else {
			window.event.cancelBubble = true;
		}
	});
}
// TODO: 2019/11/12 14:38  connector
var tempConnectId ;
function initConnectLine() {
    // setup some defaults for jsPlumb.
    var instance = jsPlumb.getInstance({
    	Endpoint: ["Blank", {radius: 2}], //"Dot":设置连接点的形状为圆形   "Blank":不需要连接点
        HoverPaintStyle: {strokeStyle: "#1e8151", lineWidth: 2 }, //元素经过的默认颜色
			ConnectionOverlays: [     //连接线的箭头
              [ "Arrow", {
                  location: 1,
                  id: "arrow",
                  length: 14,
                  foldback: 0.8     //三角形的厚度  0.618： 普通箭头，1：平底箭头，2：钻石箭头
              } ],
              [ "Label", { id: "label" }]
//            [ "Label", { label: "请拖至需要选择连接的节点", id: "label", cssClass: "aLabel" }]
        ],
        Container: "flowarea"
    });

    window.jsp = instance;
    var windows = jsPlumb.getSelector(".flowarea .w");

    instance.draggable(windows,{
    	containment: "parent",
//    	grid: [ 5, 5 ],
//    	distance: 5,
    	"start": function( event, ui ) {
    		removePopover();
//    		$("#name_" + ui.helper.context.id).html("");
    	},
    	"stop": function( event, ui ) {
//    		$("#name_" + ui.helper.context.id).html(elementMap.get(ui.helper.context.id).getName());
    		eventInterceptor("name_"　+ ui.helper.context.id);
    	},
    });

    instance.bind("click",function (c) {
    	var connectorId = c.sourceId + c.targetId;
		tempConnectId = connectorId;
    	removePopover();
    	$("#"+connectorId).popover('show');
		// connectorId = sourceId + targetId
		// TODO: 2019/11/12 15:24 add connector info modify here , needing info constructor and connector code reference
	});

    //当连接线取消时
    instance.bind("connectionDetached", function (c) {
    });

    instance.bind("connection", function (info) {
//    	info.connection.getOverlay("label").setLabel("删除");
    	//流程节点连接自己时删除该连接线
        if(info.sourceId == info.targetId) {
        	instance.detach(info);
        } else {
        	/* 流程节点连接节点只能有一条，否则删除第二条连接线
        	 * baseMap.get("status")=1 属于流程区重绘时,不用删除连接线
        	 * */
        	if(baseMap.get("status") != 1) {
        		removeConnectorElement(instance, info);
        		if(!removeReverse(instance, info))
        			return ;
        	}

        	var connector = new $.Connector({
        		connectorId:info.sourceId+info.targetId,
				sourceId:info.sourceId,
				targetId:info.targetId
        	});
			elementMap.get(info.sourceId).insertConnector(connector);
        	var s = elementMap.get(info.sourceId);
        	var t = elementMap.get(info.targetId);
        	var left = s.getLeft() + t.getLeft();
        	var top = s.getTop() + t.getTop();
			var divNode = $("<div data-toggle='popover' id=" + connector.getConnectorId()+ " name='' eletype='text' class='w eletype'></div>");
			divNode.css("visibility","hidden");
			// divNode.css("display","none");
			divNode.css("left", left/2 +"px");
			divNode.css("top", top/2 +"px");
			$("#flowarea").append(divNode);
			initConnectPopover(connector.getConnectorId());
			var sid = s.getId();
			var tid = t.getId();
			var tlist = sourceMap.get(sid);
			if(tlist == undefined) {
				tlist = []
			}
			tlist.push(t);
			sourceMap.put(sid,tlist);
			var slist = targetMap.get(tid);
			if(slist == undefined) {
				slist = [];
			}
			slist.push(s);
			targetMap.put(tid,slist);
        }
    });

    instance.batch(function () {
        instance.makeSource(windows, {
            filter: ".ep",
            anchor: "Continuous",
            connector: [ "StateMachine", { curviness: 20 } ],//轻微弯曲的线 连接线离节点有间隙
   //       connector: [ "Bezier", { curviness: 50 } ], // 连接线离节点无间隙
            connectorStyle: { strokeStyle: "#5c96bc", lineWidth: 2, outlineColor: "transparent", outlineWidth: 4 },
            maxConnections: 5,
            onMaxConnections: function (info, e) {
    			Messenger().post({
    				message: "达到连接数上限" + info.maxConnections + "！",
    				type: "error",
    				hideAfter: 5,
    			 	showCloseButton: true
    			});
                console.log("Maximum connections (" + info.maxConnections + ") reached");
            }
        });

        instance.makeTarget(windows, {
            dropOptions: {
            	hoverClass: "dragHover" //释放时指定鼠标停留在该元素上使用的css class  , activeClass:"dragActive"//可拖动到的元素使用的css class
            },
            anchor: "Continuous",
            allowLoopback: true
        });

        repaintConnector(instance);
    });

    jsPlumb.fire("jsPlumbDemoLoaded", instance);
    baseMap.put("instance", instance);
}

function uploadFlow() {
	var form = $("#flowUploadForm");
	form.ajaxSubmit({
    	url : baseUrl + "/tool/upload",
    	type : "post",
    	dataType : 'json',
    	success : function(data) {
    		if(data.statusCode == 1) {
    			clearAll($("#flowarea"));
    			initFlowData($("#flowSelect").attr("flowId"));
    			form.remove();
    		}
    	},
    	error : function(data) {
			Messenger().post({
				message: "上传流程失败！",
				type: "error",
				hideAfter: 5,
			 	showCloseButton: true
			});

    	}
	});
	return false;
}

/* 获取当前流程树选中流程id */
function getCurrentFlowId() {
	return $("#flowSelect").attr("flowId");
}

/*------------------流程树方法开始 ---------------------*/
/* 用于当鼠标移动到节点上时，显示用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮 */
function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span");
    if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) {
    	return;
    }
    var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
        + "' title='add node' onfocus='this.blur();'></span>";
    sObj.after(addStr);
    var btn = $("#addBtn_"+treeNode.tId);
    if (btn) {
        btn.bind("click", function(){
            var zTree = $.fn.zTree.getZTreeObj("flowTree");
            var newNode = zTree.addNodes(treeNode, {id:Math.uuid(), pId:treeNode.id, name:"请输入名称"});
            zTree.editName(newNode[0]);
            return false;
        });
    }
};
/* 用于当鼠标移出节点时，隐藏用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮 */
function removeHoverDom(treeId, treeNode) {
    $("#addBtn_"+treeNode.tId).unbind().remove();
};
/* 用于捕获节点编辑名称结束（Input 失去焦点 或 按下 Enter 键）之后，更新节点名称数据之前的事件回调函数，并且根据返回值确定是否允许更改名称的操作 */
function beforeRename(treeId, treeNode, newName, isCancel) {
	common.ajax({
		url : "/flowTree/addOrUpdateNode",
		type : "GET",
		data : "id=" + treeNode.id + "&name=" + newName + "&pId=" + treeNode.pId,
		contentType : "application/json"
	}, function(data) {
		nodeSelected(treeNode);
	});
}
/*
// 用于捕获删除节点之后的事件回调函数 
function onRemove(event, treeId, treeNode) {}

//用于捕获节点编辑按钮的 click 事件，并且根据返回值确定是否允许进入名称编辑状态
function beforeEditName(treeId, treeNode) {
	return true;
}
*/
// 设置是否显示删除按钮。过滤根元素不显示删除按钮 
function setRemoveBtn(treeId, treeNode) {
	return treeNode.id == "root" ? false:true;
}

//用于捕获节点被删除之前的事件回调函数，并且根据返回值确定是否允许删除操作 
function beforeRemove(treeId, treeNode) {
	var isParent = treeNode.isParent;
	var ids = new Array();
	ids = getChildren(ids,treeNode);

    var parentdiv=$('<div></div>');
    parentdiv.attr('id', "flowDelete_" + treeId);
    $(parentdiv).html("<div style='height:50px;line-height:50px;'>删除当前流程以及所有子流程，您确定需要删除吗？</div>");
	$(parentdiv).dialog({
		title: "删除当前流程？",
		resizable: false,
		autoOpen: false,
		closeOnEscape: false,
		modal: true,
		width: 350,
		height: "auto",
		position: { my: "center", at: "center", of: window  },
		buttons: [
			{
				class: "btn btn-primary",
				text: "确定",
				click: function() {
					var _this = this;
					common.ajax({
						url : "/flowTree/deleteNode",
						type : "GET",
						data : "ids=" + ids,
						async: false,
						contentType : "application/json"
					}, function(data) {
		    			if(data.statusCode == 1) {
					     	var treeObj = $.fn.zTree.getZTreeObj("flowTree");
					     	treeObj.removeNode(treeNode, false);
					    	var rootNode = treeObj.getNodeByParam("id", "root", null);
					    	nodeSelected(rootNode);
					    	$( _this ).dialog( "destroy" );

		    				Messenger().post({
		    					message: "删除流程成功！",
		    					type: "success",
		    					hideAfter: 5,
		    				 	showCloseButton: true
		    				});

		    			} else {
		    				Messenger().post({
		    					message: "清空流程失败！",
		    					type: "error",
		    					hideAfter: 5,
		    				 	showCloseButton: true
		    				});
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
		]
	}).on( "dialogclose", function( event, ui ) {
    	$(this).dialog( "destroy" );
	});
	$(parentdiv).dialog("open");

	return false;
}

/* 获取流程树选中的节点id及递归包含的所有子节点id */
function getChildren(ids,treeNode){
	ids.push(treeNode.id);
	 if (treeNode.isParent){
			for(var node in treeNode.children){
				getChildren(ids,treeNode.children[node]);
			}
	    }
	 return ids;
}

/* 用于捕获节点被点击的事件回调函数  */
function onClick(event, treeId, treeNode) {
	nodeSelected(treeNode);
};

/* 节点被选中时，更新下拉按钮的值 */
function nodeSelected(treeNode) {
 	var treeObj = $.fn.zTree.getZTreeObj("flowTree");
	treeObj.selectNode(treeNode);
	$("#flowSelect").html(treeNode.name+"&nbsp;<span class='caret'></span>");
	$("#flowSelect").attr("flowId", treeNode.id);
	$("#flowSelect").attr("flowName", treeNode.name);
	$("#flowSelect").attr("flowPid", treeNode.pId);
	clearAll($("#flowarea"));
	initFlowData($("#flowSelect").attr("flowId"));
}
/*------------------流程树方法结束 ---------------------*/