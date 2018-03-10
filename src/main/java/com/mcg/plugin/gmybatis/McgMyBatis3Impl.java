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

package com.mcg.plugin.gmybatis;

import java.text.MessageFormat;

import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

public class McgMyBatis3Impl extends IntrospectedTableMyBatis3Impl {
	
	private String daoName;
	private String xmlName;
	
    @Override
    protected String calculateMyBatis3XmlMapperFileName() {
        StringBuilder sb = new StringBuilder();
        if (stringHasValue(tableConfiguration.getMapperName())) {
            String mapperName = tableConfiguration.getMapperName();
            int ind = mapperName.lastIndexOf('.');
            if (ind != -1) {
                mapperName = mapperName.substring(ind + 1);
            }
            //支持mapperName = "{0}Dao" 等用法
//            sb.append(MessageFormat.format(mapperName, fullyQualifiedTable.getDomainObjectName()));
            sb.append(xmlName);
            sb.append(".xml"); //$NON-NLS-1$
        } else {
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper.xml"); //$NON-NLS-1$
        }
        return sb.toString();
    }

    @Override
    protected void calculateJavaClientAttributes() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return;
        }
        getDaoName(tableConfiguration.getMapperName());
        StringBuilder sb = new StringBuilder();
        sb.append(calculateJavaClientImplementationPackage());
        sb.append('.');
        if(daoName != null && !"".equals(daoName)) {
        	sb.append(daoName);
        } else {
            sb.append(fullyQualifiedTable.getDomainObjectName());
        }        
        sb.append("DAOImpl"); //$NON-NLS-1$

        setDAOImplementationType(sb.toString());

        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        if(daoName != null && !"".equals(daoName)) {
        	sb.append(daoName);
        } else {
        	sb.append(fullyQualifiedTable.getDomainObjectName());
        	sb.append("DAO"); //$NON-NLS-1$
        }
        setDAOInterfaceType(sb.toString());

        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        if (stringHasValue(tableConfiguration.getMapperName())) {
            //支持mapperName = "{0}Dao" 等用法
            sb.append(MessageFormat.format(daoName, fullyQualifiedTable.getDomainObjectName()));
        } else {
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper"); //$NON-NLS-1$
        }
        setMyBatis3JavaMapperType(sb.toString());

        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        if (stringHasValue(tableConfiguration.getSqlProviderName())) {
            //支持mapperName = "{0}SqlProvider" 等用法
            sb.append(MessageFormat.format(tableConfiguration.getSqlProviderName(), fullyQualifiedTable.getDomainObjectName()));
        } else {
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("SqlProvider"); //$NON-NLS-1$
        }
        setMyBatis3SqlProviderType(sb.toString());
    }
    
    public boolean stringHasValue(String str) {
        boolean result = false;
        if(str != null && !"".equals(str))
            result = true;
        return result;
    }
    
    public void getDaoName(String str)  {
    	if(stringHasValue(str)) {
    		String[] names = str.split(",");
    		this.daoName = names[0];
    		this.xmlName = names[1];
    	}
    }
}