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
 * @ClassName:   WsshCommandEnum   
 * @Description: TODO(mcg webSocket 指令) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2020年6月14日 下午20:24:45  
 *
 */
public enum McgWsOperationEnum {

	INIT("初始化", "init");
    private String name;
    private String value;
    
    private McgWsOperationEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public static String getValueByName(String name) {
        for (McgWsOperationEnum c : McgWsOperationEnum.values()) {
            if (c.getName().equals(name)) {
                return c.value;
            }
        }
        return null;
    }
    
    public static String getNameByValue(String value) {
        for (McgWsOperationEnum c : McgWsOperationEnum.values()) {
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
