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

/**
 * 
 * @ClassName:   EletypeEnum   
 * @Description: TODO(所有流程控件) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午3:35:56  
 *
 */
public enum EletypeEnum {

    START("开始控件", "start"), DATA("data控件", "data"), JSON("json控件", "json"), 
    TEXT("文本控件", "text"), SCRIPT("js脚本控件", "script"), JAVA("java控件", "java"), PYTHON("python控件", "python"), 
    LINUX("linux控件", "linux"), WONTON("混沌控件", "wonton"), SQLQUERY("sql查询控件", "sqlQuery"), 
    SQLEXECUTE("sql执行控件", "sqlExecute"), LOOP("循环", "loop"), PROCESS("子流程", "process"), END("结束控件", "end");
    
    private String name;
    private String value;
    
    private EletypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
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
}