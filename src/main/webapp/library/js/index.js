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
	
	var tabState = {
			"mcg_home":true,
			"mcg_flow":true,
			"mcg_wonton":true
	};
	loadNavBody("mcg_flow_first");
    $("#mcgTab a[href='#mcg_home']").on("shown.bs.tab", function (e) {
    	if(tabState.mcg_home) {
    		loadNavBody("mcg_home");
    		tabState.mcg_home = false;
    	}
    	if(!tabState.mcg_flow) {
    		removePopover()
    	}
    });
    $("#mcgTab a[href='#mcg_flow']").on("shown.bs.tab", function (e) {
    	if(tabState.mcg_flow) {
    		setAutoHeight($("body"),500);
    		loadNavBody("mcg_flow");
    		tabState.mcg_flow = false;
    	}
    	
    });	
    $("#mcgTab a[href='#mcg_wonton']").on("shown.bs.tab", function (e) {
    	if(tabState.mcg_wonton) {
    		setAutoHeight($("body"),500);
			loadNavBody("mcg_wonton");
			tabState.mcg_wonton = false;
    	}
    	if(!tabState.mcg_flow) {
    		removePopover()
    	}
    });	
	
});

function loadNavBody(id) {
	var url = "";
	if(id == "mcg_home") {
		url = "/home/index";
	} else if( id == "mcg_flow_first"){
		url = "/flow/index";
	}else if(id == "mcg_flow") {
		//进行流程区时，清除存在的属性弹出层
		$("div.popover").each(function() {
			$(this).remove();
		});		
		url = "/flow/index";
	} else if(id == "mcg_wonton") {
		url = "/wonton/index";
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
			$("#"+ id).html(data);
		}
	);	
}