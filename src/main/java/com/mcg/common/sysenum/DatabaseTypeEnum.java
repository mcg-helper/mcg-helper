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
 * @ClassName:   DatabaseTypeEnum   
 * @Description: TODO(支持的数据库) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午2:55:27  
 *
 */
public enum DatabaseTypeEnum {

    MYSQL("MYSQL", "MYSQL", "com.mysql.jdbc.Driver"), ORACLE("ORACLE", "ORACLE", "oracle.jdbc.driver.OralceDriver"), MSSQL("MSSQL", "MSSQL", "com.microsoft.sqlserver.jdbc.SQLServerDriver"), POSTGRESQL("POSTGRESQL", "POSTGRESQL", "org.postgresql.Driver");
    
    private String name;
    private String value;
    private String driverClass;
    
    private DatabaseTypeEnum(String name, String value, String driverClass) {
        this.name = name;
        this.value = value;
        this.driverClass = driverClass;
    }    
    
    public static DatabaseTypeEnum getDatabaseByName(String name) {
        for (DatabaseTypeEnum c : DatabaseTypeEnum.values()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }
    
    public static String getValueByName(String name) {
        for (DatabaseTypeEnum c : DatabaseTypeEnum.values()) {
            if (c.getName().equals(name)) {
                return c.value;
            }
        }
        return null;
    }
    
    public static String getNameByValue(String value) {
        for (DatabaseTypeEnum c : DatabaseTypeEnum.values()) {
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

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }
    
}