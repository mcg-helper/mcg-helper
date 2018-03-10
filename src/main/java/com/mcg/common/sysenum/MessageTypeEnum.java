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
 * @ClassName:   MessageTypeEnum   
 * @Description: TODO(消息类型，前端可根据类型进行展现) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午3:44:07  
 *
 */
public enum MessageTypeEnum {

    SYSTEM("系统消息", "system"),  FLOW("流程消息", "flow"), NOTIFY("通知", "notify");
    
    private String name;
    private String value;
    
    private MessageTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }    
    
    public static String getValueByName(String name) {
        for (MessageTypeEnum c : MessageTypeEnum.values()) {
            if (c.getName().equals(name)) {
                return c.value;
            }
        }
        return null;
    }
    
    public static String getNameByValue(String value) {
        for (MessageTypeEnum c : MessageTypeEnum.values()) {
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
