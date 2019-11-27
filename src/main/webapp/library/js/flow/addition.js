
/---------------test-------------------/


function showAllDiv(){
    $("#flowarea").children("div[data-toggle='popover']").each(function (){
        $(this).css("display","inline");
    })
}

function removeConnectDiv(){
    $("#flowarea").children("div[data-toggle='popover']").each(function(){
        $(this).remove();
    })
}

function hideAllDiv(){
    $("#flowarea").children("div[data-toggle='popover']").each(function (){
        $(this).css("display","none");
    })
}

var tempId ;

function setTempId(){
    var keys = elementMap.keySet();
    for(var i = 0 ; i < keys.length ; i++){
        tempId = elementMap.get(keys[i]).getId();
    }
}

function showPopover(id){
    $("#"+id).popover("show");
}


function suspendConnector(operate) {
    var id = baseMap.get("selector");
    if (operate == "output") {
        createTextOutputModal(id, null);
    }else if(operate == "input"){
        createConnectorHtmlModal(id, null);
    }else if (operate == "delete") {
        baseMap.get("instance").detach(instanceMap.get(id)); //remove connector
        removePopover();
        removeElement(id) //remove binding popover
        removeConnector(id.substr(0,36),id); // remove connector cache data
    }
}

/* 在body中创建Modal的html元素
 * id 流程节点id
 * param json对象参数
 * */
function createTextOutputModal(id, param) {
    removePopover();
    if(param == null)
        param = {};
    var option = {};
    var url = "/html/TextOutput";
    option["title"] = "输出数据";
    option["width"] = 1100;

    param["modalId"] = id;
    param["eletype"] ="output";
    param["option"] = option;
    common.showAjaxDialog(url, setDialogBtns(param), createModalCallBack, null, param);
}

function createConnectorHtmlModal(id, param) {
    removePopover();
    if(param == null)
        param = {};
    var option = {};
    var url = "/html/Connector";
    option["title"] = "输入数据";
    option["width"] = 1100;

    param["modalId"] = id;
    param["eletype"] ="connector";
    param["option"] = option;
    common.showAjaxDialog(url, setDialogBtns(param), createModalCallBack, null, param);
}


/-----------finished----------/

/**
 * DESC : send program input data
 * DATE : 2019/11/27 14:20
 * AUTHOR : UDEAN
 */
function acquireInput(){
    var keys = InputMap.keySet();
    if(keys.length != 0) {
        var source = "";
        var target = "";
        var input = "";
        for (var i = 0; i < keys.length; i++) {
            var data = [];
            source += keys[i].substr(0, 18) + ",";
            target += keys[i].substr(18, 18) + ",";
            data = InputMap.get(keys[i]);
            var dto ="";
            for(var j = 0 ; j < data.length-1; j++){
                dto += data[j]+"|";
            }
            if(data.length != 0){
                dto += data[data.length-1];
            }
            input += dto +",";
        }
        source = source.substr(0,source.length-1);
        target = target.substr(0 , target.length-1);
        input = input.substr(0,input.length-1);
        common.ajax(  {
                "url" : "/connect/input",
                "type" : "post",
                "data" : "source="+source+" &target="+target + "&input="+input
            },function (data) {
                if(data.statusCode == 1){
                    Messenger().post({
                        message: "saving input data",
                        type: "success",
                        hideAfter: 5,
                        showCloseButton: true
                    });
                }else{
                    Messenger().post({
                        message: "save input data failed",
                        type: "error",
                        hideAfter: 5,
                        showCloseButton: true
                    });
                }
            }
        )
    }
}

//Input Output edit function
var InputMap = new Map();
var OutputMap = new Map();
var InputCount = new Map();

//Output data construction
function InputData (){
    this.nList = [];
    this.tList = [];
}

InputData.prototype.getName = function(){
    return this.nList;
}

InputData.prototype.getType = function(){
    return this.tList;
}

InputData.prototype.add = function (list1,list2){
    if(list1.indexOf(undefined) != -1 ||
        list2.indexOf(undefined) != -1 ||
        list1.indexOf("") != -1 ||
        list2.indexOf("") != -1 ){
        return 1;
    }
    this.nList = [];
    this.tList = [];
    for(var i = 0 ; i < list1.length ; i++){
        var t = list1[i];
        list1[i] = undefined;
        if(list1.indexOf(t) != -1){
            return 2;
        }
        this.nList.push(t);
        this.tList.push(list2[i]);
    }
    return 0;
}

//connector tool bar
function  initConnectPopover(id) {
    $("#"+id).popover({
        visibility:"visible",
        trigger:"manual",
        placement:"bottom",
        html:true,
        container: $("#flowarea"),
        animation: false,
        content:baseMap.get("connectorPopover")
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



//send connector message while saving file
function acquireConnectors() {
    var array = getConnectors() ; var len = 0 ;   while(len < array.length){
        var s = array[len];
        common.ajax(
            {
                "url": "/connect/add",
                "type": "get",
                "data": "sourceId="+s.getSourceId()+"&targetId="+ s.getTargetId()
            },function (data){
                if(data.statusCode == 1){
                    Messenger().post({
                        message: "saving connector",
                        type: "success",
                        hideAfter: 5,
                        showCloseButton: true
                    });
                }else{
                    Messenger().post({
                        message: "save connector failed",
                        type: "error",
                        hideAfter: 5,
                        showCloseButton: true
                    });
                }
            });
        len ++;
    }
}
/**
 * DESC : check is flow runnable
 * DATE : 2019/11/11 9:49
 * AUTHOR : UDEAN
 */
function flowIsLegal() {
    common.ajax(
        {
            "url" : "/connect/legal",
            "type" : "get"
        },
        function (data) {
            if(data.statusCode == 1 ){
                Messenger().post({
                    message: "flow legal",
                    type: "success",
                    hideAfter: 5,
                    showCloseButton: true
                })
                return true;
            }else{
                Messenger().post({
                    message: "flow illegal",
                    type: "error",
                    hideAfter: 5,
                    showCloseButton: false
                })
                return false;
            }
        }
    )
}

/**
 * DESC : delete out time connector cache
 * DATE : 2019/11/12 9:40
 * AUTHOR : UDEAN
 */
function delCache() {
    var array = getConnectors();
    var index = 0 ;
    for(;index<array.length;index++){
        var s = array[index];
        common.ajax({
                "url":"/connect/del",
                "type":"get",
                "data": "sourceId="+s.getSourceId()+"&targetId="+ s.getTargetId()
            }, function (data) {
                if(data.statusCode == 1){
                    Messenger().post({
                        message: "delete success",
                        type: "success",
                        hideAfter: 5,
                        showCloseButton: true
                    })
                }else{
                    Messenger().post({
                        message: "delete failed",
                        type: "success",
                        hideAfter: 5,
                        showCloseButton: true
                    })
                }
            }
        )
    }
}
/**
 * DESC : delete all connector cache without data transform
 * DATE : 2019/11/12 9:49
 * AUTHOR : UDEAN
 */
function delAll() {
    common.ajax({
            "url":"/connect/delAll",
            "type":"get"
        },function (data) {
            if(data.statusCode == 0){
                Messenger().post({
                    message: "delete cache failed",
                    type: "error",
                    hideAfter: 5,
                    showCloseButton: true
                })
            }
        }
    )
}


//reload text component position
var element;
function reloadText() {
    var eles = elementMap.keySet();
    for(var index = 0 ; index < eles.length ; index ++){
        var key = eles[index];
        element = elementMap.get(key);
        alert(element);
    }
}

/**
 * DESC : load import file text position
 * DATE : 2019/11/8 18:14
 * AUTHOR : UDEAN
 */
function reloadPosition(Id , xy) {
    var element = elementMap.get(Id);
    element.setLeft(xy.x);
    element.setTop(xy.y);
}

/**
 * DESC : acquire element xy value for test
 * DATE : 2019/11/8 18:33
 * AUTHOR : UDEAN
 * PARM : element Id
 */

function acquireXY(Id) {
    var element = elementMap.get(Id);
    alert(element.getLeft() + " " + element.getTop());
}

/**
 * DESC : transform element to xy
 * DATE : 2019/11/8 18:35
 * AUTHOR : UDEAN
 */
function  transXY(element) {
    var xy = {x:element.getLeft(),y:element.getTop()};
    return xy;
}

/**
 * DESC : get XY int test
 * DATE : 2019/11/8 18:37
 * AUTHOR : UDEAN
 */

function XY(x , y) {
    return {x:x,y:y};
}

function sendAllConnecotors(){
    var array = getConnectors() ;
    var len = 0 ;
    var slist = [];
    var tlist = [];
    while(len < array.length){
        slist.push(array[len].getSourceId());
        tlist.push(array[len].getTargetId());
        len++;
    }
    alert(slist);
    common.ajax(
        {
            "url": "/connect/addArray",
            "type": "POST",
            "data": "sourceId=" +JSON.stringify(slist)+"&targetId=" + JSON.stringify(tlist)
        },function (data){
            if(data.statusCode == 1){
                Messenger().post({
                    message: "saving connector",
                    type: "success",
                    hideAfter: 5,
                    showCloseButton: true
                });
            }else{
                Messenger().post({
                    message: "save connector failed",
                    type: "error",
                    hideAfter: 5,
                    showCloseButton: true
                });
            }
        });
}


