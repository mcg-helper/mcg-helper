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

package com.mcg.entity.wssh;

public class WebSSHData {
	
    private String host;
    private Integer port = 22;
    private String username;
    private String password;
    private String operation;
    private String shell;
    private String serverSourceId;
    private int terminalHeight;
    private int terminalWidth;
    private int rows;
    private int cols;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getShell() {
		return shell;
	}

	public void setShell(String shell) {
		this.shell = shell;
	}

	public String getServerSourceId() {
		return serverSourceId;
	}

	public void setServerSourceId(String serverSourceId) {
		this.serverSourceId = serverSourceId;
	}

	public int getTerminalHeight() {
		return terminalHeight;
	}

	public void setTerminalHeight(int terminalHeight) {
		this.terminalHeight = terminalHeight;
	}

	public int getTerminalWidth() {
		return terminalWidth;
	}

	public void setTerminalWidth(int terminalWidth) {
		this.terminalWidth = terminalWidth;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
    
}
