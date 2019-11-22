
/---------------test-------------------/
function  initConnectPopover(id) {
    $("#"+id).popover({
        visibility:"visible",
        trigger:"manual",
        placement:"bottom",
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

function name(id){
    alert(id);
}

function showPopover(id){
    $("#"+id).popover("show");
}


/-----------finished----------/
function suspendConnector(instance ,operate, s, t) {
    if (operate == "edit") {
        createHtmlModal(baseMap.get("selector"), null);
    } else if (operate == "delete") {
        instance.detach(s+t);
        removeConnector(s,s+t);
    } else if (operate == "logOut") {
        removePopover();
        alert(CANNOTUSE);
    }
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
function senSimpledConnector(method , sourceId, targetId){
    var url = "/connect";
    if(method == "add"){
        url +="/add";
    }else if (method = "del"){
        url += "/del";
    }
    common.ajax(
        {
            "url" : url,
            "type" : "get",
            "sourceId" : sourceId,
            "targetId" : targetId
        }
    )
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


