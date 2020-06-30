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
	setAutoHeight($("body"), 500);
	
	$("#mcg_wssh_serverSourceId").selectpicker({
		noneSelectedText: "请选择",
		width:"100%"
	});
	
	var defaultTerminal, defaultWSSHClient;
	$("#mcg_wwsh_connect").click(function(){
		if(defaultTerminal != null && defaultTerminal != '') {
			try {
				defaultWSSHClient.sendClientData("exit\r");
			} catch(err) {
				
			}
			$("#wssh_terminal").empty();
		}
		
		var terminalHeight = Math.round($("#wssh_terminal").height());
		if(terminalHeight <= 0) {
			terminalHeight = 480;
		}
		var rows = Math.round(terminalHeight / 20);
		if(rows <= 0) {
			rows = 24;
		}
		
		var terminalWidth = Math.round($("#wssh_terminal").width());
		if(terminalWidth <= 0) {
			terminalWidth = 1000;
		}
		var cols = Math.round(terminalWidth / 9);
		if(cols <= 0) {
			cols <= 100;
		}
		
		initTerminal({
			"terminalWidth":terminalWidth,
			"terminalHeight":terminalHeight,
			"rows":rows,
			"cols":cols,
			"operation":"init",
			"serverSourceId":$("#mcg_wssh_serverSourceId").val()
		});
	});
	
    function initTerminal(options) {
    	defaultWSSHClient = new WSSHClient(options);
        defaultTerminal = new Terminal({
            cols: options.cols,
            rows: options.rows,
            cursorBlink: true, 
            cursorStyle: "block", 
            scrollback: 800, 
            tabStopWidth: 8, 
            screenKeys: true
        });

        defaultTerminal.on('data', function (data) {
        	defaultWSSHClient.sendClientData(data);
        });
        defaultTerminal.open(document.getElementById('wssh_terminal'));
        defaultWSSHClient.connect({
            onError: function (error) {
            	defaultTerminal.write('Error: ' + error + '\r\n');
            },
            onConnect: function () {
            	defaultTerminal.write('请等待，正在连接中...\r\n');
            	defaultWSSHClient.sendInitData(options);
            },
            onClose: function () {
            	defaultTerminal.write("\r终端连接关闭。");
            },
            onData: function (data) {
            	defaultTerminal.write(data);
            }
        });
    }
});