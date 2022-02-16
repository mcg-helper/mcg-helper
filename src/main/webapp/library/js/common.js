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


//最大可视高度
var screenMaxHeight = $(window).height();
//最大可视宽度
var screenMaxWidth = $(window).width();
//ace编辑器的属性性，目前用于全屏功能记录其改变的高宽值
var acePropertyMap = new Map();
/*
 * 消息类，用于前端展示流程日志和通知消息
 * */
var Message = function(param){
	this.continer = "";
	this.msg = param.msg;
}

/* 前端展示日志和通知
 * 流程日志输出方法
 * 通知展示  
 * */
Message.prototype.output = function() {
	var self = this;
	var log = {
		"info" : {
			html:"暂无内容",
			value:"暂无内容",
			execute:function(){
				this.value = self.msg.body.content;
				this.html = self.build();
			}
		},
		"success" : {
			html:"暂无内容",
			value:"暂无内容",
			execute:function(){
				this.value = self.msg.body.content;
				this.html = self.build();
			}
		},
		"error" : {
			html:"暂无内容",
			value:"暂无内容",
			execute:function(){
				this.value = self.msg.body.content;
				this.html = self.build();
				Messenger().post({
					message: "发生异常",
					type: "error",
					hideAfter: 5,
				 	showCloseButton: true
				});
			}
		}
	};
	var textEditor = {
		"make" : {
			"options" : {

			},
			execute:function(container, param) {
				$("#" + container).html('<pre style="background:#fff;border-radius:0px;border:0px solid #ccc;"><code><xmp>' + param + '</xmp></code></pre>');
			}
		}
	};
	var linuxEditor = {
			"make" : {
				"options" : {

				},
				execute:function(container, param) {
					var length = param.split("\n").length;
					var screenRows = 25;
					if(length < 24) {
						screenRows = length + 1;
					}
			        var term = new Terminal({
			            cols: 120,
			            rows: screenRows
			        });
			        term.open(document.getElementById(container));
			        term.write(param);
				}
			}
	};
	var jsonEditor = {
		"make" : {
			"options" : {
				mode: 'view',
				modes: ['view','code']
			},
			execute:function(container, param) {
				if(param != null || !"" == param) {
				var result = {};
					try {
						result = JSON.parse(param);
						var editor = new JSONEditor(document.getElementById(container), this.options, result);
					//	editor.setMode("code");
					} catch (e) {
						$("#" + container).html('<pre style="background:#fff;border-radius:0px;border:0px solid #ccc;"><code><xmp>' + param + '</xmp></code></pre>');
						/*
						Messenger().post({
							message: "控制台接收到非JSON数据，异常：" + e,
							type: "error",
							hideAfter: 5,
						 	showCloseButton: true
						});
						*/
					}
				}
			}
		}
	};
	var notify = {
		execute:function(){
			Messenger().post({
				message: self.msg.body.content,
				type: self.msg.body.type,
				hideAfter: 5,
			 	showCloseButton: true
			});
		}
	};
	var wonton = {
		
	};
	
	if(self.msg.header.mesType == "FLOW") {
		/* 
		 * 将子流程中的控件日志过滤，不在当流程图中显示次序号
		 * 每个控件第一次输出日志时，因为第一次才会有次序号orderNum 
		 * */
		if(!self.msg.body.subFlag && self.msg.body.orderNum != undefined && self.msg.body.orderNum > 0) {
			if(baseMap.get("runnerFlowElementId") == null) {
				baseMap.put("runnerFlowElementId", self.msg.body.eleId);
			} else {
				$("#name_" + baseMap.get("runnerFlowElementId")).removeClass("run_flicker");
				baseMap.put("runnerFlowElementId", self.msg.body.eleId);
			}
			var elementName = elementMap.get(self.msg.body.eleId).getName();
			var nameHtml = $("#name_" + self.msg.body.eleId).html();
			var orderNumLength = $("#name_" + self.msg.body.eleId).children("span").length; //当前控件上显示次序号的个数
			// 当由于循环功能，一个控件上的次序号显示超出5个后，就显示“...”，不再追加次序号
			if(orderNumLength < 5) {
				var appendHtml = '<span class="badge" style="background-color:green">' + self.msg.body.orderNum + '</span>';
				$("#name_" + self.msg.body.eleId).html(nameHtml.substring(0, nameHtml.length - elementName.length) + appendHtml + elementName);
			} else if(orderNumLength == 5){
				var appendHtml = '<span class="badge" style="background-color:green">...</span>';
				$("#name_" + self.msg.body.eleId).html(nameHtml.substring(0, nameHtml.length - elementName.length) + appendHtml + elementName);
			}
			
			$("#name_" + self.msg.body.eleId).addClass("run_flicker");
		}

		log[self.msg.body.logType].execute();
		var html = '';
		html = log[self.msg.body.logType].html;
		$(self.continer).append(html);
		
		//控制台日志输出
		if(this.msg.body.eleType != "finish") {
			console.log(self.msg.body.eleType + "--" + self.msg.body.logOutType);
			if(self.msg.body.eleType == "linux" && self.msg.body.logOutType == "ssh") {
				linuxEditor["make"].execute(self.msg.id, log[self.msg.body.logType].value);
			} else if(self.msg.body.eleType == "text" && self.msg.body.logOutType == "text") {
				textEditor["make"].execute(self.msg.id, log[self.msg.body.logType].value);
			} else {
				jsonEditor["make"].execute(self.msg.id, log[self.msg.body.logType].value);
			}
			
		}
		if(self.msg.body.logType == "error") {
			baseMap.put("selector", self.msg.body.eleId);
			baseMap.put("highlight", "highlight" + self.msg.body.eleId); //把当前被高亮标记div的id放入缓存
			$("#highlight" + baseMap.get("selector")).addClass("highlight");
			$("#flowStopBtn span").removeClass("text-danger");   //控制台执行状态去掉红色，变为默认灰色
		    var pos = $("#log" + baseMap.get("selector")).offset().top;
		    $("#mcg_flow").animate({scrollTop: pos - 10}, 100);
		}
	} else if(self.msg.header.mesType == "NOTIFY") {
		notify.execute();
	} else if(self.msg.header.mesType == "WONTON") {
		for(var i=0;i<self.msg.body.dataList.length; i++) {
			var wontonListTable = $("#wontonListTable");
			var wontonHeart = wontonListTable.bootstrapTable('getRowByUniqueId', self.msg.body.dataList[i].host);
			if(wontonHeart == null) {
				wontonListTable.bootstrapTable('append', self.msg.body.dataList[i]);
			} else {
				wontonListTable.bootstrapTable('updateByUniqueId', {
			        id: self.msg.body.dataList[i].host,
			        row: self.msg.body.dataList[i]
			    });
			}
		}
	}
};

Message.prototype.build = function() {
	var html = "";
	if (this.msg.header.mesType == "FLOW") {
		//   console.log(this);  输出日志
		/*  控件初次产生的日志  */
		if($("#console").children("#log" + this.msg.body.eleId).length <= 0) {
			html += '<div id="log' + this.msg.body.eleId + '" class="alert alert-info alert-dismissible" role="alert">';
			html += '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button><br/>';
			html += '<ul style="margin-top:21px;background:#fff;" class="messenger messenger-theme-air"><li class="messenger-message-slot"><div class="messenger-message message alert alert-' + this.msg.body.logType + ' "><div id="highlight' + this.msg.body.eleId + '" class="messenger-message-inner">' + this.msg.body.logTypeDesc + '--》' + this.msg.body.eleTypeDesc + '--》' + this.msg.body.comment + '</div></div></li></ul>';
			
			// 流程或子流程正常执行完毕，控制台最后追加“文件列表”以及“控制台停止按钮”更正状态颜色
			if(this.msg.body.eleType == "finish" || this.msg.body.eleType == "subFinish") {
				if(this.msg.body.content != null && this.msg.body.content != "none") {
					html += '<div style="height:auto;background:#fff;">';
					var filesData = JSON.parse(this.msg.body.content);
					for (var key in filesData.availableFileMap) {
					    html += '&nbsp;&nbsp;' + filesData.availableFileMap[key] + '&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onClick="downFlowGenFile(this)" eleId="' + key + '" path="' + filesData.availableFileMap[key] + '">下载</a></br>';
			 		}
					html += '</div>';
					
				}
				if(this.msg.body.eleType == "finish") {
					$("#flowStopBtn span").removeClass("text-danger");
					if(!this.msg.body.subFlag) {
						$("#name_" + baseMap.get("runnerFlowElementId")).removeClass("run_flicker");
					}
				}
			} else if(this.msg.body.eleType == "interrupt") { //流程主动中断操作，且已停止后，“控制台停止按钮”更正状态颜色
				$("#flowStopBtn span").removeClass("text-danger");
			}
			
			html += '<div style="height:auto;" id="' + this.msg.id + '"></div>';
			html += '</div>';
			this.continer = "#console";
		} else {  /*  同一控件第二次及后续产生的日志  */
			html += '<ul style="background:#fff;margin-top:13px;" class="messenger messenger-theme-air"><li class="messenger-message-slot"><div class="messenger-message message alert alert-' + this.msg.body.logType + ' "><div class="messenger-message-inner">' + this.msg.body.logTypeDesc + '--》' + this.msg.body.comment + '</div></div></li></ul>';
			html += '<div style="height:auto;" id="' + this.msg.id + '"></div>';			
			this.continer = "#log" + this.msg.body.eleId;
		}
	}	
	
	return html;
}

/*  下载流程中“文本控件”生成的文件   */
function downFlowGenFile(obj) {
	var eleId = $(obj).attr("eleId");
	var path = $(obj).attr("path");
	if(eleId != null && eleId != "" && path != null && path != "") {
		var form = $("<form>");
		form.attr("style", "display:none");
		form.attr("target", "");
		form.attr("method", "post");
		form.attr("action", baseUrl + "/tool/downFlowGenFile");
		var eleIdInput = $("<input>");
		eleIdInput.attr("type","hidden");
		eleIdInput.attr("name","eleId");
		eleIdInput.attr("value", eleId);
		form.append(eleIdInput);	
		var pathInput = $("<input>");
		pathInput.attr("type","hidden");
		pathInput.attr("name","path");
		pathInput.attr("value", path);
		form.append(pathInput);
		$("body").append(form);
		
		form.submit();
		form.remove();
		webSocket = new WebSocket(websocketUrl);
	    webSocket.onerror = function(event) { 
	    	
	    };
		 
	    webSocket.onopen = function(event) { 
	    	
	    };
	 
	    webSocket.onmessage = function(event) {
				var message = new Message({
				msg : JSON.parse(event.data)
			});
			message.output();
	    };
	    
	    webSocket.onclose = function(event) { 
	    	
	    };	
	}
}

/*
 * websocket
 * param : {"operation":"init", "mcgWebScoketCode":"xxxx-xxxx-xxxx-xxxx"}
 */
var WSSHClient = function(param) {

}

WSSHClient.prototype._generateEndpoint = function () {
    var endPoint = "ws:" + baseUrl.replace("http:", "") + "/wssh";
    return endPoint;
};

WSSHClient.prototype.connect = function (options) {
    var endpoint = this._generateEndpoint();

    this._connection = new WebSocket(endpoint);
    
    this._connection.onopen = function () {
        options.onConnect();
    };

    this._connection.onmessage = function (evt) {
        var data = evt.data.toString();
        options.onData(data);
    };


    this._connection.onclose = function (evt) {
        options.onClose();
    };
};

WSSHClient.prototype.send = function (data) {
    this._connection.send(JSON.stringify(data));
};

WSSHClient.prototype.sendInitData = function (options) {
    this._connection.send(JSON.stringify(options));
}

WSSHClient.prototype.sendClientData = function (data) {
    this._connection.send(JSON.stringify({"operation": "shell", "shell":data}))
}

//消息通知提示框插件
Messenger.options = {
	    extraClasses: 'messenger-fixed messenger-on-bottom messenger-on-right',
	    theme: 'air'
}

Date.prototype.format = function (format) {  
    var o = {  
        "M+": this.getMonth() + 1,  
        "d+": this.getDate(),  
        "h+": this.getHours(),  
        "m+": this.getMinutes(),  
        "s+": this.getSeconds(),  
        "q+": Math.floor((this.getMonth() + 3) / 3),  
        "S": this.getMilliseconds()  
    }  
    if (/(y+)/.test(format)) {  
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
    }  
    for (var k in o) {  
        if (new RegExp("(" + k + ")").test(format)) {  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
        }  
    }  
    return format;  
}

var common = function(){
	
	var doResult = function (data, url, failureCallBack) {
        if (failureCallBack) 
        	failureCallBack(data); 
        return data;
    };
	
	return {
		ajax : function(option, ajaxCallBack){
			$.ajax({
	            url: baseUrl + option.url + (option.url.indexOf("?") == -1 ? "?" : "&") + "_R=" + Math.random(),
	            type: option.type,
	            data: option.data,
	            contentType: option.contentType,
	            async: option.async == undefined ? true : option.async,
	    		dataType : option.dataType, 
	            jsonp : option.jsonp, 
	            success: function (data) {
	                var result = doResult(data, option.url, option.failureCallBack || function () { });
	                if(ajaxCallBack)
	                	ajaxCallBack(result); 
	            }, 
	            error: function (a, b, c) {
					Messenger().post({
						message: "非常抱歉, 发生异常!",
						type: "error",
						hideAfter: 5,
					 	showCloseButton: true
					});	            	
	            },
	            beforeSend: function() {
	            	if(option.isLoading) {
	            		$.bootstrapLoading.end();
	            	}
	            }
	        });
		},
		ajax : function(option, ajaxCallBack, errorCallBack){
			$.ajax({
	            url: baseUrl + option.url + (option.url.indexOf("?") == -1 ? "?" : "&") + "_R=" + Math.random(),
	            type: option.type,
	            data: option.data,
	            contentType: option.contentType,
	            async: option.async == undefined ? true : option.async,
	    		dataType : option.dataType, 
	            jsonp : option.jsonp, 
	            success: function (data) {
	                var result = doResult(data, option.url, option.failureCallBack || function () { });
	                if(ajaxCallBack)
	                	ajaxCallBack(result); 
	            }, 
	            beforeSend: function() {
	            	if(option.isLoading) {
	            		$.bootstrapLoading.end();
	            	}
	            },
	            error: errorCallBack
	        });
		},		
		ready : function(){},
		showAjaxModal : function(url, data, callback, closeCallback, param){
			var me = this;
			this.ajax({
	            url: url,
	            type: 'GET',
	            "dataType":"html",
	            "async":false,
	            "data":param
			}, function(html) {

           	 var m = $(html).modal({ backdrop: "static", show: false });
           	 m.returnValue = "";
           	 var clientHeight = $(window).height();
                m.find(".modal-body").css("max-height", clientHeight - 100);
                m.find(".modal-body").css("overflow-y", 'auto');
                m.modal("show").css({
/*	             		"margin-top": function () {
           			return "5%"; //($(window).height() / 2)
           		}*/
                });
                
                m.on('shown.bs.modal', function () {
/*		                 m.draggable({
	                	 cursor: "move",
	                	 handle: '.modal-header'
		 			 });	         
	                 $(".modal-backdrop").remove();
	                 */
	                 
               	 if(callback) {
               		 callback(param);
               	 }
                    me.ready.call(m, data);
                    
                });
                m.on('hidden.bs.modal', function () {
                    if (m.returnValue) {
                       if (closeCallback)
                       	closeCallback(m.returnValue);
                    }
                    m.remove();
                });
			});
			
		}, 
		showAjaxDialog : function(url, btns, callback, closeCallback, param){
			if($("#dialog"+param.modalId).length > 0) {
				return ;
			}
			var me = this;
			this.ajax({
	            url: url,
	            type: 'GET',
	            "dataType":"html",
	            "async":false,
	            "data":param
			}, function(html) {

                var parentdiv=$('<div></div>');       
                parentdiv.attr('id', "dialog"+param.modalId);
                $(parentdiv).html(html);
            	
            	$(parentdiv).dialog({
            		title: param.option.title,
            		autoOpen: false,
            		closeOnEscape: false,
            		width: param.option.width,
            		height: "auto",
            		buttons: btns,
			        　　　position: {
			      　　　　  	my: "center top",
			      　　　　　　	at: "center top",
			      　　　　　　	of: window,
			      　　　　　　	collision: "flip"/*,
			      　　　　　　	using: function( pos ) {
				      　　          	$(this).css({
			      　　　　　　　　          	"position":"fix", //absolute
			      　　　　　　　　          	"margin":"auto",
					      　　　　　　　 "left": 0,
					            "right": 0,
			      　　　　　　　　		"top":"20px"
			      　　　　　　		});
			      　　　　　　	}*/
		      　　　　	}
            	}).on( "dialogclose", function( event, ui ) {
	            	$(this).dialog( "destroy" );
            	});	            	
            	$(parentdiv).dialog("open");
            	if(callback) {
            		param["dialog"] = this;
            		callback(param);
            	}
			});
			
		},
		formUtils : {
			setValues : function(formId, data) {
				var form = $("#" + formId)[0];
			    if (form != null){
			    	
			        for(var i = 0;i < form.elements.length;i++){
			            var oField =  form.elements[i];
			            if (oField.type != "button"){
			            	if(oField.name.indexOf("[") > 0) {
			            		var key = oField.name.substring(0, oField.name.indexOf("["));
			            		var obj = data[key];
			            		var valueName = oField.name.substring(oField.name.indexOf("[") + 1, oField.name.indexOf("]"));
			            		var value = obj[valueName];
			            	//	console.log("oField.name="+ oField.name + "---key =" +key + "---valueName=" + valueName + "---value=" + value);
				                if(typeof (value) == "undefined") {
				                    continue ;
				                }
				                
				                if(oField.type == "checkbox" || oField.type == "radio"){
				                	
				                    if (value == null || value == "" || value == "0" || value == "false"){
				                        oField.checked = false;
				                    }else{
				                        oField.checked = true;
				                    }

				                } else if(oField.type == "text"
				                    || oField.type == "number"
				                    || oField.type == "password"
				                    || oField.type == "hidden"
				                    || oField.type == "textarea"){
				                    oField.value = value;
				                } else if(oField.type == "select" || oField.type == "select-one") {
				                	$(oField).val(value);
				                	$(oField).selectpicker('refresh');
				                	$(oField).selectpicker('val', value + "");
				                    
				                } else{
				                    //其它的表单就不用设置了,比如image,reset,submit
				                    oField.value = value;
				                }
				                
			            		oField.value = obj[valueName];
			            	}
			            	
			            } else {
			            	//暂不处理button
			            }
			        }

			    } else{
					Messenger().post({
						message: "表单不存在, 初始化值失败!",
						type: "error",
						hideAfter: 5,
					 	showCloseButton: true
					});				    	
			    }				
				
			}
		},
		dateUtils : {
			/**   
			 *转换日期对象为日期字符串   
			 * @param date 日期对象   
			 * @param isFull 是否为完整的日期数据,   
			 *               为true时, 格式如"2000-03-05 01:05:04"   
			 *               为false时, 格式如 "2000-03-05"   
			 * @return 符合要求的日期字符串   
			 */
			getSmpFormatDate : function(date, isFull) {  
			     var pattern = "";  
			     if (isFull == true || isFull == undefined) {  
			         pattern = "yyyy-MM-dd hh:mm:ss";  
			     } else {  
			         pattern = "yyyy-MM-dd";  
			     }  
			     return this.getFormatDate(date, pattern);  
			 }, 
			 /**   
			 *转换当前日期对象为日期字符串   
			 * @param date 日期对象   
			 * @param isFull 是否为完整的日期数据,   
			 *               为true时, 格式如"2000-03-05 01:05:04"   
			 *               为false时, 格式如 "2000-03-05"   
			 * @return 符合要求的日期字符串   
			 */		
			getSmpFormatNowDate : function(isFull) {
				return this.getSmpFormatDate(new Date(), isFull); 
			},
			 /**   
			 *转换long值为日期字符串   
			 * @param l long值   
			 * @param isFull 是否为完整的日期数据,   
			 *               为true时, 格式如"2000-03-05 01:05:04"   
			 *               为false时, 格式如 "2000-03-05"   
			 * @return 符合要求的日期字符串   
			 */ 	
			getSmpFormatDateByLong : function(l, isFull) {
				return this.getSmpFormatDate(new Date(l), isFull);  
			},
			 /**   
			 *转换long值为日期字符串   
			 * @param l long值   
			 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss   
			 * @return 符合要求的日期字符串   
			 */ 	
			getFormatDateByLong : function(l, pattern) {  
				return this.getFormatDate(new Date(l), pattern);  
			},
			 /**   
			 *转换日期对象为日期字符串   
			 * @param date    
			 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss   
			 * @return 符合要求的日期字符串   
			 */	
			getFormatDate : function(date, pattern) {
				if (date == undefined) {  
					date = new Date();  
				}  
				if (pattern == undefined) {  
					pattern = "yyyy-MM-dd hh:mm:ss";  
			    }  
			    return date.format(pattern); 		
			}
				 
		}		
	};
	
	
}();

/*===========定义生成uuid函数开始
用法：Math.uuid()
==============*/
(function() {
	var CHARS = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'
			.split('');
	Math.uuid = function(len, radix) {
		var chars = CHARS, uuid = [], i;
		radix = radix || chars.length;
		if (len) {
			for (i = 0; i < len; i++)
				uuid[i] = chars[0 | Math.random() * radix];
		} else {
			var r;
			uuid[8] = uuid[13] = uuid[18] = uuid[23] = '_';
			uuid[14] = '4';
			for (i = 0; i < 36; i++) {
				if (!uuid[i]) {
					r = 0 | Math.random() * 16;
					uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
				}
			}
		}
		return uuid.join('');
	};

	Math.uuidFast = function() {
		var chars = CHARS, uuid = new Array(36), rnd = 0, r;
		for (var i = 0; i < 36; i++) {
			if (i == 8 || i == 13 || i == 18 || i == 23) {
				uuid[i] = '_';
			} else if (i == 14) {
				uuid[i] = '4';
			} else {
				if (rnd <= 0x02)
					rnd = 0x2000000 + (Math.random() * 0x1000000) | 0;
				r = rnd & 0xf;
				rnd = rnd >> 4;
				uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
			}
		}
		return uuid.join('');
	};
	
	Math.uuidCompact = function() {
		return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
				function(c) {
					var r = Math.random() * 16 | 0, v = c == 'x' ? r
							: (r & 0x3 | 0x8);
					return v.toString(16);
				});
	};
})(); 	
/*===========定义生成uuid函数结束==============*/	    

/*=================定义Map开始==================
用法 : baseMap.put("key", value);
baseMap.remove("key");
var array = map.keySet();
for(var i in array) {
	console.log("key:(" + array[i] +") value: ("+map.get(array[i])+")");
}	 
=================================================*/
function Map() {
	this.container = new Object();
}

Map.prototype.put = function(key, value) {
	this.container[key] = value;
}

Map.prototype.get = function(key) {
	return this.container[key];
}

Map.prototype.keySet = function() {
	var keyset = new Array();
	var count = 0;
	for ( var key in this.container) {
		// 跳过object的extend函数
		if (key == 'extend') {
			continue;
		}
		keyset[count] = key;
		count++;
	}
	return keyset;
}

Map.prototype.size = function() {
	var count = 0;
	for ( var key in this.container) {
		// 跳过object的extend函数
		if (key == 'extend') {
			continue;
		}
		count++;
	}
	return count;
}

Map.prototype.remove = function(key) {
	delete this.container[key];
}
/*===============定义Map结束=============*/

/*===============定义loading开始=============
 * 用法	$.bootstrapLoading.start({ loadingTips: "正在处理数据，请稍候..." });
 *	    $.bootstrapLoading.end();
 * */
jQuery.bootstrapLoading = {
	start : function(options) {
		var defaults = {
			opacity : 1,
			//loading页面透明度
			backgroundColor : "#fff",
			//loading页面背景色
			borderColor : "#bbb",
			//提示边框颜色
			borderWidth : 1,
			//提示边框宽度
			borderStyle : "solid",
			//提示边框样式
			loadingTips : "Loading, please wait...",
			//提示文本
			TipsColor : "#666",
			//提示颜色
			delayTime : 1000,
			//页面加载完成后，加载页面渐出速度
			zindex : 9999999,
			//loading页面层次
			sleep : 0
		//设置挂起,等于0时则无需挂起
		}
		var options = $.extend(defaults, options);
		//获取页面宽高
		var _PageHeight = document.documentElement.clientHeight, _PageWidth = document.documentElement.clientWidth;
		//在页面未加载完毕之前显示的loading Html自定义内容
		var _LoadingHtml = '<div id="loadingPage" style="position:fixed;left:0;top:0;_position: absolute;width:100%;height:'
				+ _PageHeight
				+ 'px;background:#cccccc;'
//				+ options.backgroundColor
				+ ';opacity:'
				+ options.opacity
				+ ';filter:alpha(opacity='
				+ options.opacity * 100
				+ ');z-index:'
				+ options.zindex
				+ ';"><div id="loadingTips" class="pacman" style="position: absolute; cursor1: wait; width: auto;border-color:'
				+ options.borderColor
				+ ';border-style:'
				+ options.borderStyle
				+ ';border-width:'
				+ options.borderWidth
				+ 'px; background: #cccccc;'
	//			+ options.backgroundColor
	//			+ ' url(<%=basePath %>/library/images/loading.gif) no-repeat 5px center; color:'
				+ options.TipsColor
				+ ';font-size:20px;">'
				+ '<div></div><div></div><div></div><div></div><div></div></div></div>';
		//呈现loading效果
		$("#mcg_header").append(_LoadingHtml);
		//获取loading提示框宽高
		var _LoadingTipsH = document.getElementById("loadingTips").clientHeight, _LoadingTipsW = document
				.getElementById("loadingTips").clientWidth;
		//计算距离，让loading提示框保持在屏幕上下左右居中
		var _LoadingTop = _PageHeight > _LoadingTipsH ? (_PageHeight - _LoadingTipsH) / 2
				: 0, _LoadingLeft = _PageWidth > _LoadingTipsW ? (_PageWidth - _LoadingTipsW) / 2
				: 0;
		$("#loadingTips").css({
			"left" : _LoadingLeft + "px",
			"top" : _LoadingTop + "px"
		});
	},
	end : function() {
		$("#loadingPage").remove();
	}
}
/*===============定义loading结束=============*/

$(document).ajaxStart(function(){
    $.bootstrapLoading.start({ "loadingTips": "正在处理数据，请稍候...", "borderWidth":0, "opacity":0.5 });
});

$(document).ajaxStop(function(){
    $.bootstrapLoading.end();
});


$(document).ajaxError(function(){
	Messenger().post({
		message: "非常抱歉，发生异常！",
		type: "error",
		hideAfter: 5,
	 	showCloseButton: true
	});
    $.bootstrapLoading.end();
});

var Tools = function(param){
}
//将带下划线的字符转换为驼峰命名
Tools.prototype.convertHumpName = function(name) {
	var reg = /_(\w)/g;
    return name.toLowerCase().replace(reg, function (a,b){
        return b.toUpperCase();
    });
}
//将带下划线的字符转换为驼峰命名，且首字符为大写
Tools.prototype.convertClassName = function(name) {
	var reg = /\b(\w)|\s(\w)/g;
    return this.convertHumpName(name).replace(reg, function (data){
        return data.toUpperCase();
    });
}
var tools = new Tools({});

/*
 * 自适应布局 计算并设置所有autoHeight元素的高度（参数为父元素）
 */
function setAutoHeight($pe,min_height){
	if ($pe != null) {
		// 获取父元素的高度
		var parent_h = 0;									
		//if ($pe.attr("id")=="body"){
		if ($pe.get(0).tagName=="BODY"){	
			parent_h = getTotalHeight();  									// body元素获取窗口的高度			
			//alert("0-"+parent_h);
			min_height = (parseInt(min_height) == "NaN") ? 400 : min_height;  	// 默认最小高度为500
			parent_h = (parent_h<min_height) ? min_height : parent_h;	
			// 动态设置body的
			$pe.css("overflow","hidden");			
		} else {
			parent_h = $pe.height(); 
		}
		// 遍历父元素子级元素中样式类名 包含"autoHeight"但不包含"gridTable"的元素，计算并设置其高度
		$pe.children(".autoHeight").each(function(i){	
			var $e1 = $(this);		
			if ($e1.hasClass("autoHeight") && (! $e1.hasClass("gridTable")) && ($e1.css("display") != "none") ) {		
				// 该元素的纵向的边线宽度及外边距（从边线（border）开始至margin-top或margin-bottom之间的距离
				//（含border线宽度和padding高度））
				var border_outer_h = $e1.outerHeight()-$e1.height();					
				//alert($e1.attr("id")+" : "+border_outer_h);	
				// 遍历该元素的所有 非autoHeight 和 非隐藏 的兄弟元素并合计实际高度（siblings()）
				var sibling_h = 0;
				$e1.siblings().each(function(j){											
					if (!($(this).hasClass("autoHeight") || ($(this).css("display") == "none"))) {																	
						// 获取本元素
						sibling_h = sibling_h+$(this).outerHeight(true);						
						//alert($(this).attr("id")+" : "+sibling_h);	
					};									
				});	
				
				// 遍历父元素的所有子元素， 排除 浮动 的子元素，处理
				// 纵向上的外边距值（maring）之间重叠互吃的问题（两个以上兄弟元素才计算）
				var margin_height_1 = 0;  // 垂直方向的理论外边距（包含margin-top和margin-bottom）
				var margin_height_2 = 0;  // 元素的margin-bottom 与相邻下一个元素间的 margin-top 之间互吃后的实际边距
				if ($pe.children().size()>1) {				
					$pe.children().each(function(k){
						$e2 = $(this);	
						if (!(($e2.css("float")=="left") || ($e2.css("float")=="right"))) {								
							// 垂直方向的理论外边距合计
							margin_height_1 = margin_height_1 + $e2.outerHeight(true) - $e2.outerHeight(false);							
							// 计算元素的margin-bottom 与相邻下一个元素间的 margin-top
							// 之间互吃被吃掉的外边距
							// 当前元素下边距值（只考虑了单位是px的情况，其他单位会出错）
							var v_m_bottom=$e2.css("margin-bottom");
							var index = v_m_bottom.indexOf("px");
							if (index>0) {
								v_m_bottom = v_m_bottom.substring(0,index);
							} else {
								alert("不合乎要求的下外边距设置，计算出的高度可能会有误差："+v_m_bottom);
								v_m_bottom = 0;
							};
							v_m_bottom = parseInt(v_m_bottom);							
							// 下一个元素上边距值（只考虑了单位是px的情况，其他单位会出错）
							var v_m_top = 0;
							if (k<$pe.children().size()-1) {
								v_m_top=$e2.next().css("margin-top");
								var index = v_m_top.indexOf("px");
								if (index>0) {
									v_m_top = v_m_top.substring(0,index);
								} else {
									alert("不合乎要求的上外边距设置，计算出的高度可能会有误差："+v_m_top);
									v_m_top = 0;
								};
								v_m_top = parseInt(v_m_top); 
							};					
							// 实际被吃掉的那一部分边距（小的那一个被吃掉了,等于的时候吃掉任意一个）
							margin_height_2 = margin_height_2 + ((v_m_bottom<v_m_top) ? v_m_bottom : v_m_top);									
							//alert($e2.attr("id")+" : "+  margin_height_1 + " | " + margin_height_2 );
						};								
					});				
				};
				// 设置该元素的高度值
				var h=parent_h-sibling_h-border_outer_h-margin_height_1+margin_height_2-1;	
				//alert($e1.attr("id") +" ::: "+$pe.attr("id")+" :  "+parent_h +"|" +sibling_h+"|" +border_outer_h+"|" +margin_height_1+ "|" +margin_height_2);				
				//alert("h-"+h);				
				$e1.height(h);
				// 递归调用，计算该元素下所有自动高度元素的高度
				setAutoHeight($e1);	
			};						
		});
	}					
}
/*
 * 计算窗口的有效高度
 */
function getTotalHeight(){	  
	//alert(document.compatMode);
	//alert($(document).height()+"   "+$("body").height());	
	//alert(document.documentElement.clientHeight+"   "+document.body.clientHeight);	
	//leadingWhitespace: 如果在使用innerHTML的时候浏览器会保持前导空白字符，则返回true，目前在IE 6-8中返回false。
    if($.support.leadingWhitespace){
    	//compatMode 文档渲染方式：CSS1Compat-标准兼容模式模式开启，BackCompat-标准兼容模式关闭（怪异模式）
    	_h = document.compatMode == "CSS1Compat"? document.documentElement.clientHeight :
            document.body.clientHeight;
    }else{
    	_h = document.compatMode == "CSS1Compat"? document.documentElement.clientHeight :
            	document.body.clientHeight;
    	//return self.innerHeight;
    }
	//alert(_h + "  |  " +self.innerHeight);
	return _h-2;
}
/*
 * 计算窗口的有效宽度
 */
function getTotalWidth(){	             
    if($.support.leadingWhitespace){
        return document.compatMode == "CSS1Compat"? document.documentElement.clientWidth :
                 document.body.clientWidth;
    }else{
        return self.innerWidth;
    }
}