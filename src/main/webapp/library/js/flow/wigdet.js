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

$.WebElement = function (args) {
	this.id = args.id; 
	this.name = args.name;
	this.label = args.label;
	this.width = args.width;
	this.height = args.height;
	this.classname = args.classname;
	this.eletype = args.eletype;
	this.clone = args.clone;
	this.left = args.left;
	this.top = args.top;
	this.sign = args.sign;
};

$.WebConnector = function (args) {
	this.connectorId = args.connectorId; 
	this.sourceId = args.sourceId;
	this.targetId = args.targetId;
};

$.WebStruct = function (args) {
	this.flowId = args.flowId;
	this.webElement = args.webElement; 
	this.webConnector = args.webConnector;
};

/*=================连接线============== 
 * 提供所有节点的父级与子级节点id
 * 向afterArray插入  connectors.insertAfter(elementId);
 * 获取afterArray  connectors.getAfterArray()
 */
$.Connector = function (args) {
	var connectorId = args.connectorId; //连接线的id
	var sourceId = args.sourceId; //源节点id
	var targetId = args.targetId; //目标节点id
 
    this.getConnectorId = function () {     
        return connectorId;
    };	
    this.getSourceId = function () {     
        return sourceId;
    };	
    this.getTargetId = function () {     
        return targetId;
    };	        
};

/*=================节点类DragWidget
     用法：          var pnode = new $.DragWidget({ name: "",width:100,height:30,classname:"pnode",eletype:"pnode" });
            pnode.getName();
 ==================*/
$.DragWidget = function (option) {
	var id = option.id; //唯一id，字符串类型
	var label = option.label; //组件的标签
	var name = option.name; //名称，字符串类型
	var width = option.width; //节点的宽度，整型
	var height = option.height; //节点的高度，整型
	var classname = option.classname; //节点的样式类，字符串类型
	var eletype = option.eletype; //节点类型，字符串类型
	var clone = option.clone; //是否在流程区存在，布尔类型
	var left = option.left; // 相对横坐标，整型
	var top = option.top; //相对纵坐标，整型
	var sign = option.sign; //"true"：已保存到后台缓存中 "false"：未保存
	var connectorMap = new Map(); //当前节点的连接线集合,用于存储$.Connector
	
	/* 将连接线保存流程节点变量中 */
    this.insertConnector = function (connector) {   
        connectorMap.put(connector.getConnectorId(), connector);
    };
    /* 将连接线在流程节点变量中移除 */
    this.removeConnector = function (connectorId) {    
        connectorMap.remove(connectorId);
    };
    
    this.getConnectorMap = function () {     
        return connectorMap;
    };    
    this.getId = function () {     
        return id;
    };
    this.getLabel = function () {     
        return label;
    };    
    this.getName = function () {     
        return name;
    };
    this.getX = function () {     
        return x;
    };
    this.getY = function () {     
        return y;
    };
    this.getWidth = function () {     
        return width;
    };
    this.getHeight = function () {     
        return height;
    };
    this.getClassname = function () {     
        return classname;
    };
    this.getEletype = function () {     
        return eletype;
    };
    this.getClone = function () {     
        return clone;
    };	        
    this.getLeft = function () {     
        return left;
    };	 
    this.getTop = function () {     
        return top;
    };
    this.setLabel = function(lable) {
    	this.lable = lable;
    };
    this.setTop = function (y) {     
        top = y;
    };
    this.setLeft = function (x) {     
        left = x;
    };     
    this.getSign = function() {
    	return sign;
    };
    this.setSign = function (signa) {
    	sign = signa;
    };
};	

$.fn.serializeObject = function(suffix) {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (suffix) {
			this.name = this.name.replace(suffix, '');
		}
		if (o[this.name]) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value == 'null'? null: this.value || null);
		} else {
			o[this.name] = this.value == 'null'? null: this.value || null;
		}
	});
	return o;
};

/*
serializedParams格式为"key1=value1&key2=value2". 
也支持'key.sonkey=value' 
 */
$.fn.paramString2obj = function(serializedParams) {    
    var obj={};
    function evalThem (str) {
        var strAry = new Array();
        strAry = str.split("=");
        //使用decodeURIComponent解析uri 组件编码
        for(var i = 0; i < strAry.length; i++){
            strAry[i] = decodeURIComponent(strAry[i]);
        }
        var attributeName = strAry[0];
        var attributeValue = strAry[1].trim();
        //如果值中包含"="符号，需要合并值
        if(strAry.length > 2){
            for(var i = 2;i<strAry.length;i++){
                attributeValue += "="+strAry[i].trim();
            }
        }
        if(!attributeValue){
            return ;
        }
        
        var attriNames = attributeName.split("."),
            curObj = obj;
        for(var i = 0; i < (attriNames.length - 1); i++){
            curObj[attriNames[i]]?"":(curObj[attriNames[i]] = {});
            curObj = curObj[attriNames[i]];
        }
        
        //使用赋值方式obj[attributeName] = attributeValue.trim();替换
        //eval("obj."+attributeName+"=\""+attributeValue.trim()+"\";");
        //解决值attributeValue中包含单引号、双引号时无法处理的问题
        //这里可能存在一个种情况：多个checkbox同一个name的时候需要使用","来分割 
        curObj[attriNames[i]] = curObj[attriNames[i]]? 
        (curObj[attriNames[i]] + "," + attributeValue.trim()): 
        attributeValue.trim();
    };
    
    var properties = serializedParams.split("&");
    for (var i = 0; i < properties.length; i++) {
        //处理每一个键值对
        evalThem(properties[i]);
    };
    return obj;
}
