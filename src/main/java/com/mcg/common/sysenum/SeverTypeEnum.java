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
 * @ClassName:   SeverTypeEnum   
 * @Description: TODO(支持的服务器操作系统) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年7月29日 下午12:52:20  
 *
 */
public enum SeverTypeEnum {

    LINUX("LINUX", "LINUX", "linux系统"), UNIX("UNIX", "UNIX", "unxi系统");
    
    private String name;
    private String value;
    private String desc;
    
    private SeverTypeEnum(String name, String value, String desc) {
        this.name = name;
        this.value = value;
        this.desc = desc;
    }    
    
    public static SeverTypeEnum getSeverTypeByName(String name) {
        for (SeverTypeEnum c : SeverTypeEnum.values()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }
    
    public static String getValueByName(String name) {
        for (SeverTypeEnum c : SeverTypeEnum.values()) {
            if (c.getName().equals(name)) {
                return c.value;
            }
        }
        return null;
    }
    
    public static String getNameByValue(String value) {
        for (SeverTypeEnum c : SeverTypeEnum.values()) {
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}