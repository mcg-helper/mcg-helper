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

package com.mcg.entity.mybatisconfig;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class Context implements Serializable {

    private static final long serialVersionUID = -667720820781474683L;
    @XmlAttribute
    private String id;
    @XmlAttribute
    private String targetRuntime;
    @XmlElement
    private List<Property> property;
    @XmlElement
    private List<Plugin> plugin;
    @XmlElement
    private CommentGenerator commentGenerator;
    @XmlElement
    private JdbcConnection jdbcConnection;
    @XmlElement
    private JavaModelGenerator javaModelGenerator;
    @XmlElement
    private SqlMapGenerator sqlMapGenerator;
    @XmlElement
    private JavaClientGenerator javaClientGenerator;
    @XmlElement
    private List<Table> table;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTargetRuntime() {
        return targetRuntime;
    }
    public void setTargetRuntime(String targetRuntime) {
        this.targetRuntime = targetRuntime;
    }
    public List<Property> getProperty() {
        return property;
    }
    public void setProperty(List<Property> property) {
        this.property = property;
    }
    public List<Plugin> getPlugin() {
        return plugin;
    }
    public void setPlugin(List<Plugin> plugin) {
        this.plugin = plugin;
    }
    public CommentGenerator getCommentGenerator() {
        return commentGenerator;
    }
    public void setCommentGenerator(CommentGenerator commentGenerator) {
        this.commentGenerator = commentGenerator;
    }
    public JdbcConnection getJdbcConnection() {
        return jdbcConnection;
    }
    public void setJdbcConnection(JdbcConnection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }
    public JavaModelGenerator getJavaModelGenerator() {
        return javaModelGenerator;
    }
    public void setJavaModelGenerator(JavaModelGenerator javaModelGenerator) {
        this.javaModelGenerator = javaModelGenerator;
    }
    public SqlMapGenerator getSqlMapGenerator() {
        return sqlMapGenerator;
    }
    public void setSqlMapGenerator(SqlMapGenerator sqlMapGenerator) {
        this.sqlMapGenerator = sqlMapGenerator;
    }
    public JavaClientGenerator getJavaClientGenerator() {
        return javaClientGenerator;
    }
    public void setJavaClientGenerator(JavaClientGenerator javaClientGenerator) {
        this.javaClientGenerator = javaClientGenerator;
    }
    public List<Table> getTable() {
        return table;
    }
    public void setTable(List<Table> table) {
        this.table = table;
    }
    
}
