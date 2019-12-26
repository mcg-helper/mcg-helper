<%--
  Created by IntelliJ IDEA.
  User: UDean
  Date: 2019/11/26
  Time: 15:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script language="javascript">
    $(document).ready(function () {
            var id = $("#id").attr("name").substr(0,18);
            var cons = OutputMap.get(id);
            if(cons == undefined){
                InputCount.remove(id);
            }else {
                var nList = cons.getName();
                var tList = cons.getType();
                var count = [];
                for (var i = 1; i <= nList.length; i++) {
                    var name = id + "_Name_" + i;
                    var type = id + "_Type_" + i;
                    var divNode = "<div id = '" + id + "_" + i + "'><input type = 'text' id = '" + type + "' value ='" + tList[i-1] + "'><input type ='text' id = '" + name + "'value = '" + nList[i-1] + "'/><button onclick='del(" + i + ")'>X</button><div>";
                    $("#addItemGroup").append(divNode);
                    count.push(i);
                }
                InputCount.put(id, count);
            }
            $("#addItem").bind("click", function () {
                var count = InputCount.get(id);
                if (count == undefined) {
                    count = [];
                    count.push(1);
                }else{
                    count.push(count.length+1);
                    for(var i = 1; i< count.length;i++ )
                        if(count.indexOf(i) == -1){
                            count.splice(count.length-1,1);
                            count.push(i);
                            break;
                        }
                }
                InputCount.put(id,count);
                var index = count[count.length-1];
                var name = id + "_Name_" + index;
                var type = id + "_Type_" + index;
                var divNode = "<div id = '"+id+"_"+index +"'><input type = 'text' id = '"+ type +"'><input type ='text' id = '" + name + "'/><button onclick='del("+index +")'>X</button><div>";
                $("#addItemGroup").append(divNode);
            });
            $("#deleteItem").bind("click", function () {
                count = [];
                $("#addItemGroup").children().each(function () {
                    $(this).remove();
                });
                InputCount.put(id, count);
            });
            $("#save").bind("click",function () {
                var tList = [];
                var nList = [];
                var count = InputCount.get(id);
                if(count == undefined){
                    alert("not output data");
                    return ;
                }
                for(var i = 1 ; i <= count.length ; i++){
                    nList.push($("#"+id+"_Name_"+i).val());
                    tList.push($("#"+id+"_Type_"+i).val());
                }
                cons = new InputData();
                var result = cons.add(nList,tList);
                if(result == 1){
                    alert("can exist blank input");
                }else if (result == 2){
                    alert("name repeated");
                }else if( result == 0){
                    OutputMap.put(id,cons);
                    alert("successful!!!");
                }else{
                    alert("error");
                }

            })
        }
    );

    function del(e){
        var id = $("#id").attr("name").substr(0,18);
        var count = InputCount.get(id);
        var index = count.indexOf(e);
        if(index == -1){
            alert("don't call del function by using console !!!");
            return;
        }
        $("#"+id+"_"+e).remove();
        count.splice(index,1);
        InputCount.put(id,count);
    }
</script>
<div id = "id" name="${modalId}" style="display: none"></div>
<div class="container-fluid" >
    <div class="row">
        <div class="col-md-12">
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#${modalId }_textProperty" data-toggle="tab">输出</a></li>
            </ul>
        </div>
    </div>

    <div class="row margin_top">
        <div class="col-md-12">
            <form id="${modalId }_textForm" class="form-horizontal" role="form">
                <div class="form-body">
                    <div id="myTabContent" class="tab-content">
                        <div class="tab-pane fade in active" id="${modalId }_textProperty">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">控件名称</label>
                                <div class="col-sm-4">
                                    <div class="fg-line">
                                        <input type="text" id="${modalId }_name"  class="form-control" />
                                    </div>
                                </div>

                                <label class="col-sm-1 control-label">控件key</label>
                                <div class="col-sm-4">
                                    <div class="fg-line">
                                        <input type="text" id="${modalId }_key"  class="form-control"  />
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label name = "dataType" class="col-sm-2 control-label" style="text-align: center">data type</label>
                                <label name = "dataName" class="col-sm-2 control-label" style="text-align: center">data name</label>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div id ="addItemGroup">
    </div>
    <div>
        <button id = "addItem">Add</button>
        <button id = "deleteItem">Delete</button>
    </div>
    <div>
        <button id = "save" >确定</button>
        <button id = "cancel">取消</button>
    </div>
</div>
