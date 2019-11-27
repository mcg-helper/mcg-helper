<%--
  Created by IntelliJ IDEA.
  User: UDean
  Date: 2019/11/12
  Time: 16:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script language="javascript">
    $(document).ready(function () {
        var cid = $("#id").attr("name").substr(0,18) + $("#id ").attr("name").substr(36,18);
        var id = $("#id").attr("name").substr(0,18);
        var cons = OutputMap.get(id);
        if(cons != undefined && cons.nList.length != 0){
            var checked = InputMap.get(cid);
            var nList = cons.getName();
            var tList = cons.getType();
            if(checked == undefined) {
                for (var i = 0; i < nList.length; i++) {
                    var selector = tList[i] + "_" + nList[i];
                    var divNode = "<input type = 'checkbox' name = 'input' checked = 'checked' value='"+ selector+"'> " + selector + "<br>";
                    $("#selectForm").append(divNode);
                }
            }else{
                for (var i = 0; i < nList.length; i++) {
                    var selector = tList[i] + "_" + nList[i];
                    if(checked.indexOf(selector) != -1) {
                        var divNode = "<input type = 'checkbox' name = 'input' checked = 'checked' value='"+ selector+"'> " + selector + "<br>";
                    }else{
                        var divNode =  "<input type = 'checkbox' name = 'input' value='"+ selector+"'> " + selector + "<br>";
                    }
                    $("#selectForm").append(divNode);
                }
            }
        }
        $("#save").bind("click",function () {
            checked = [];
            $("input[name = 'input']:checked").each(function () {
                checked.push($(this).val());
            });
            InputMap.put(cid,checked);
            alert("success!!!");
        })
        }
    );

</script>
<div id = "id" name="${modalId}" style="display: none"></div>
<div class="container-fluid" >
    <div class="row">
        <div class="col-md-12">
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#${modalId }_textProperty" data-toggle="tab">输入</a></li>
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
                                        <input type="text" id="${modalId }_name"  class="form-control"  />
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
<%--                                <div class="fg-line col-sm-4">--%>
<%--                                    <input type="hidden" id="${modalId }_textId" name="textId" value="${modalId }" />--%>
<%--                                    <input type="text" id="${modalId }_fileName" name="textProperty[fileName]" class="form-control"  />--%>
<%--                                </div>--%>

                                <label name = "dataName" class="col-sm-2 control-label" style="text-align: center">data name</label>
<%--                                <div class="col-sm-4">--%>
<%--                                    <div class="fg-line">--%>
<%--                                        <select id="${modalId }_outMode" name="textProperty[outMode]" class="selectpicker">--%>
<%--                                            <c:forEach items="${outModes}" var="item">--%>
<%--                                                <option value="${item.value }">${item.name }</option>--%>
<%--                                            </c:forEach>--%>
<%--                                        </select>--%>
<%--                                    </div>--%>
<%--                                </div>--%>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div id ="selectDiv">
        <form id = "selectForm">
        </form>
    </div>
    <div>
        <button id = "save">save</button>
    </div>
</div>