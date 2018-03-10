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


$(function () { 
	setAutoHeight($("body"),500);
	
	$('#mcg_nav a:first').tab('show');//初始化显示"主页" 
	loadNavBody($('#mcg_nav a:first').attr("id"));
    $('#mcg_nav a').click(function (e) { 
      e.preventDefault();//阻止a链接的跳转行为 
      $(this).tab('show');
      loadNavBody($(this).attr("id"));
    });   
});

function loadNavBody(id) {
	var url = "";
	if(id == "mcg_home") {
		url = "/home/index";
	} else if(id == "mcg_flow") {
		//进行流程区时，清除存在的属性弹出层
		$("div.popover").each(function() {
			$(this).remove();
		});		
		url = "/flow/index";
	} else {
		return ;
	}

	common.ajax(
		{
			"url":url,
			"type":"get",
			"dataType":"html"
//			"data":param,
		}, function(data){
			$('#mcg_nav_body').html(data);
		}
	);	
}