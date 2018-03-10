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

package com.mcg.plugin.execute.strategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.alibaba.fastjson.JSON;
import com.mcg.common.Constants;
import com.mcg.common.sysenum.DatabaseTypeEnum;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.data.DataRecord;
import com.mcg.entity.flow.gmybatis.FlowGmybatis;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.global.McgGlobal;
import com.mcg.entity.global.datasource.McgDataSource;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.entity.mybatisconfig.ClassPathEntry;
import com.mcg.entity.mybatisconfig.CommentGenerator;
import com.mcg.entity.mybatisconfig.Context;
import com.mcg.entity.mybatisconfig.GeneratorConfiguration;
import com.mcg.entity.mybatisconfig.JavaClientGenerator;
import com.mcg.entity.mybatisconfig.JavaModelGenerator;
import com.mcg.entity.mybatisconfig.JdbcConnection;
import com.mcg.entity.mybatisconfig.Plugin;
import com.mcg.entity.mybatisconfig.Property;
import com.mcg.entity.mybatisconfig.SqlMapGenerator;
import com.mcg.entity.mybatisconfig.Table;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.dbconn.FlowDataAdapterImpl;
import com.mcg.plugin.dbconn.McgBizAdapter;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.generate.FlowTask;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;
import com.mcg.util.LevelDbUtil;
import com.mcg.util.McgFileUtils;
import com.mcg.util.Tools;

public class FlowGmybatisStrategy implements ProcessStrategy {

    @Override
    public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
    	FlowGmybatis flowGmybatis = (FlowGmybatis)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowGmybatis.getGmybatisId());
    }

    @Override
    public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
        FlowGmybatis flowGmybatis = (FlowGmybatis)mcgProduct;
		JSON parentParam = DataConverter.getParentRunResult(flowGmybatis.getGmybatisId(), executeStruct);
		
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);		
        FlowBody flowBody = new FlowBody();
        flowBody.setEleType(EletypeEnum.GMYBATIS.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.GMYBATIS.getName() + "--》" + flowGmybatis.getGmybatisProperty().getName());
        flowBody.setEleId(flowGmybatis.getGmybatisId());
        flowBody.setComment("参数");
        if(parentParam == null) {
        	flowBody.setContent("{}");
        } else {
        	flowBody.setContent(JSON.toJSONString(parentParam, true));
        }
        flowBody.setLogType(LogTypeEnum.INFO.getValue());
        flowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
        message.setBody(flowBody);
        FlowTask flowTask = FlowTask.executeLocal.get();    
        MessagePlugin.push(flowTask.getHttpSessionId(), message);		
		
		flowGmybatis = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct), flowGmybatis);	        
        
		McgFileUtils.createDir(flowGmybatis.getGmybatisProperty().getDaoProjectPath());
		McgFileUtils.createDir(flowGmybatis.getGmybatisProperty().getPojoProjectPath());
		McgFileUtils.createDir(flowGmybatis.getGmybatisProperty().getXmlProjectPath());
		
        createTable(createMybaitsCfg(flowGmybatis));
        RunResult result = new RunResult();
        result.setElementId(flowGmybatis.getGmybatisId());
        
        Map<String, HashMap<String, Object>> value = new HashMap<String, HashMap<String, Object>>();
        McgGlobal mcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
		if(mcgGlobal != null) {
			List<McgDataSource> mcgDataSourceList = mcgGlobal.getFlowDataSources();
			if(flowGmybatis.getRelation() != null && flowGmybatis.getRelation().getTables() != null && flowGmybatis.getRelation().getTables().size() > 0 ) {
				for(McgDataSource mcgDataSource : mcgDataSourceList) {
					if(mcgDataSource.getDataSourceId().equals(flowGmybatis.getRelation().getDataSourceId())) {
						HashMap<String, Object> tableMap = new HashMap<String, Object>();
						for(com.mcg.entity.flow.gmybatis.Table table : flowGmybatis.getRelation().getTables()) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							
							McgBizAdapter mcgBizAdapter = new FlowDataAdapterImpl(mcgDataSource);
					        List<DataRecord> dataRecordList = mcgBizAdapter.getTableInfo(table.getTableName());
							
							map.put("record", dataRecordList);
							tableMap.put(Tools.convertClassName(table.getTableName()), map);
						}
						value.put(flowGmybatis.getGmybatisProperty().getKey(), tableMap);
						break;
					}
				}
			}
		}
        
        result.setJsonVar(JSON.toJSONString(value, true));
        executeStruct.getRunStatus().setCode("success");
        return result;
    }
    
    public String createMybaitsCfg(FlowGmybatis flowGmybatis) throws Exception {
        String cfgXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\n"+ "<!DOCTYPE generatorConfiguration PUBLIC \"-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN\" \"http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd\" >";
        GeneratorConfiguration generatorConfiguration = new GeneratorConfiguration();
        List<ClassPathEntry> classPathEntryList = new ArrayList<ClassPathEntry>();
        ClassPathEntry classPathEntry = new ClassPathEntry();
        classPathEntry.setLocation(Constants.WEB_PATH + "WEB-INF/lib/mysql-connector-java-5.1.34.jar");
        classPathEntryList.add(classPathEntry);
        generatorConfiguration.setClassPathEntry(classPathEntryList);
        
        List<Context> contextList = new ArrayList<Context>();
        Context context = new Context();
        context.setId("context");
        context.setTargetRuntime("com.mcg.plugin.gmybatis.McgMyBatis3Impl");
        
        List<Property> contextPropertyList = new ArrayList<Property>(); 
        // 生成java文件编码
        Property encodedProperty = new Property();
        encodedProperty.setName("javaFileEncoding");
        encodedProperty.setValue(Constants.CHARSET.toString());
        contextPropertyList.add(encodedProperty);
        
        // 格式化java代码
        Property javaFormatterproperty = new Property();
        javaFormatterproperty.setName("javaFormatter");
        javaFormatterproperty.setValue("org.mybatis.generator.api.dom.DefaultJavaFormatter");
        contextPropertyList.add(javaFormatterproperty);
        
        //格式化xml代码
        Property xmlFormatterproperty = new Property();
        xmlFormatterproperty.setName("xmlFormatter");
        xmlFormatterproperty.setValue("org.mybatis.generator.api.dom.DefaultXmlFormatter");
        contextPropertyList.add(xmlFormatterproperty);
        
        Property beginningDelimiterProperty = new Property();
        beginningDelimiterProperty.setName("beginningDelimiter");
        beginningDelimiterProperty.setValue("`");
        contextPropertyList.add(beginningDelimiterProperty);
        
        Property endingDelimiterProperty = new Property();
        endingDelimiterProperty.setName("endingDelimiter");
        endingDelimiterProperty.setValue("`");
        contextPropertyList.add(endingDelimiterProperty);
        
        context.setProperty(contextPropertyList);
        
        List<Plugin> pluginList = new ArrayList<Plugin>();
        Plugin modelPlugin = new Plugin();
        modelPlugin.setType("org.mybatis.generator.plugins.ToStringPlugin");
        pluginList.add(modelPlugin);
        Plugin modelSerialPlugin = new Plugin();
        modelSerialPlugin.setType("org.mybatis.generator.plugins.SerializablePlugin");
        pluginList.add(modelSerialPlugin);
        Plugin pagePlugin = new Plugin();
        pagePlugin.setType("com.mcg.plugin.gmybatis.MySQLPaginationPlugin");
        pluginList.add(pagePlugin);
        
        context.setPlugin(pluginList);
        
        CommentGenerator commentGenerator = new CommentGenerator();
        commentGenerator.setType("com.mcg.plugin.gmybatis.DefaultCommentGenerator");
        List<Property> commentGeneratorPropertyList = new ArrayList<Property>();
        Property suppressDateProperty = new Property();
        suppressDateProperty.setName("suppressDate");
        suppressDateProperty.setValue("true");
        commentGeneratorPropertyList.add(suppressDateProperty);
        Property suppressAllCommentsProperty = new Property();
        suppressAllCommentsProperty.setName("suppressAllComments");
        suppressAllCommentsProperty.setValue("false");
        commentGeneratorPropertyList.add(suppressAllCommentsProperty);
        commentGenerator.setProperty(commentGeneratorPropertyList);
        
        context.setCommentGenerator(commentGenerator);
        
        JavaModelGenerator javaModelGenerator = new JavaModelGenerator();
        javaModelGenerator.setTargetPackage(flowGmybatis.getGmybatisProperty().getPojoOutPath());
        javaModelGenerator.setTargetProject(flowGmybatis.getGmybatisProperty().getPojoProjectPath());
        context.setJavaModelGenerator(javaModelGenerator);
        
        SqlMapGenerator sqlMapGenerator = new SqlMapGenerator();
        sqlMapGenerator.setTargetPackage(flowGmybatis.getGmybatisProperty().getXmlOutPath());
        sqlMapGenerator.setTargetProject(flowGmybatis.getGmybatisProperty().getXmlProjectPath());
        context.setSqlMapGenerator(sqlMapGenerator);
        
        JavaClientGenerator javaClientGenerator = new JavaClientGenerator();
        javaClientGenerator.setTargetPackage(flowGmybatis.getGmybatisProperty().getDaoOutPath());
        javaClientGenerator.setTargetProject(flowGmybatis.getGmybatisProperty().getDaoProjectPath());
        javaClientGenerator.setType("XMLMAPPER");
        context.setJavaClientGenerator(javaClientGenerator);
        
        JdbcConnection jdbcConnection = new JdbcConnection();
        
        McgGlobal mcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
        for(McgDataSource mcgDataSource : mcgGlobal.getFlowDataSources()) {
            if(mcgDataSource.getDataSourceId().equals(flowGmybatis.getRelation().getDataSourceId())) {
                DatabaseTypeEnum databaseTypeEnum = DatabaseTypeEnum.getDatabaseByName(mcgDataSource.getDbType());  
                if(databaseTypeEnum != null) {
                    jdbcConnection.setDriverClass(databaseTypeEnum.getDriverClass());
                }
                jdbcConnection.setConnectionURL("jdbc:mysql://" + mcgDataSource.getDbServer() + ":" + mcgDataSource.getDbPort() + "/" + mcgDataSource.getDbName());
                jdbcConnection.setUserId(mcgDataSource.getDbUserName());
                jdbcConnection.setPassword(mcgDataSource.getDbPwd());       
                context.setJdbcConnection(jdbcConnection);  
                
                List<Table> tableList = new ArrayList<Table>();
                for(com.mcg.entity.flow.gmybatis.Table sourceTable: flowGmybatis.getRelation().getTables()) {
                    Table targetTable = new Table();
                    targetTable.setSchema(mcgDataSource.getDbName());
                    targetTable.setTableName(sourceTable.getTableName());
                    targetTable.setDomainObjectName(sourceTable.getEntityName());
                    targetTable.setMapperName(sourceTable.getDaoName() + "," + sourceTable.getXmlName());
                    
                    tableList.add(targetTable);
                }
                context.setTable(tableList);                
            }
        }        
        
        contextList.add(context);
        generatorConfiguration.setContext(contextList);
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JAXBContext jaxbContext = JAXBContext.newInstance(GeneratorConfiguration.class);
        Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);//
  //      m.setProperty(Marshaller.JAXB_ENCODING, "GBK"); //防止文件中文乱码  
        m.marshal(generatorConfiguration, os);
        cfgXml += new String(os.toByteArray(), Constants.CHARSET);
        os.close();
        
        return cfgXml;
    }

    public boolean createTable(String cfgXml) throws Exception {
        boolean result = false;
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        ConfigurationParser cp = new ConfigurationParser(warnings);
//          Configuration config = cp.parseConfiguration(new File("D:/tmp/generatorConfig.xml"));
        Configuration config = cp.parseConfiguration(new ByteArrayInputStream(cfgXml.getBytes()));
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
        result = true;
        
        return result;
    }

}
