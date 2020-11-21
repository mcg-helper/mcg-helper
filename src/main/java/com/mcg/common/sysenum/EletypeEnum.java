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

package com.mcg.common.sysenum;

import com.mcg.entity.flow.data.FlowData;
import com.mcg.entity.flow.demo.FlowDemo;
import com.mcg.entity.flow.end.FlowEnd;
import com.mcg.entity.flow.git.FlowGit;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.json.FlowJson;
import com.mcg.entity.flow.linux.FlowLinux;
import com.mcg.entity.flow.loop.FlowLoop;
import com.mcg.entity.flow.process.FlowProcess;
import com.mcg.entity.flow.python.FlowPython;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.flow.sftp.FlowSftp;
import com.mcg.entity.flow.sqlexecute.FlowSqlExecute;
import com.mcg.entity.flow.sqlquery.FlowSqlQuery;
import com.mcg.entity.flow.start.FlowStart;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.flow.wonton.FlowWonton;

/**
 * 
 * @ClassName:   EletypeEnum   
 * @Description: TODO(所有流程控件) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午3:35:56  
 *
 */
public enum EletypeEnum {

    START("开始控件", "start", (new FlowStart()).getClass()), DATA("data控件", "data", (new FlowData()).getClass()), 
    JSON("json控件", "json", (new FlowJson()).getClass()), GIT("git控件", "git", (new FlowGit()).getClass()), 
    SFTP("sftp控件", "sftp", (new FlowSftp()).getClass()), TEXT("文本控件", "text", (new FlowText()).getClass()), 
    SCRIPT("js脚本控件", "script", (new FlowScript()).getClass()), JAVA("java控件", "java", (new FlowJava()).getClass()), 
    PYTHON("python控件", "python", (new FlowPython()).getClass()), LINUX("linux控件", "linux", (new FlowLinux()).getClass()), 
    WONTON("混沌控件", "wonton", (new FlowWonton()).getClass()), SQLQUERY("sql查询控件", "sqlQuery", (new FlowSqlQuery()).getClass()), 
    SQLEXECUTE("sql执行控件", "sqlExecute", (new FlowSqlExecute()).getClass()), LOOP("循环", "loop", (new FlowLoop()).getClass()), 
    PROCESS("子流程", "process", (new FlowProcess()).getClass()), END("结束控件", "end", (new FlowEnd()).getClass()),
    DEMO("示例控件", "demo", (new FlowDemo()).getClass());
    
    private String name;
    private String value;
    private Class<?> classes;
    
    private EletypeEnum(String name, String value, Class<?> classes) {
        this.name = name;
        this.value = value;
        this.classes = classes;
    }    
    
    public static String getValueByName(String name) {
        for (EletypeEnum c : EletypeEnum.values()) {
            if (c.getName().equals(name)) {
                return c.value;
            }
        }
        return null;
    }
    
    public static String getNameByValue(String value) {
        for (EletypeEnum c : EletypeEnum.values()) {
            if (c.getValue().equals(value)) {
                return c.name;
            }
        }
        return null;
    }
    
    public static Class<?> getClassByValue(String value) {
        for (EletypeEnum c : EletypeEnum.values()) {
            if (c.getValue().equals(value)) {
                return c.classes;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

	public Class<?> getClasses() {
		return classes;
	}

	public void setClasses(Class<?> classes) {
		this.classes = classes;
	}
    
}