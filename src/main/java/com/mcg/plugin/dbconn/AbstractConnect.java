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

package com.mcg.plugin.dbconn;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;

public abstract class AbstractConnect {
	
	public DbConnect dbConnect;

	public AbstractConnect(DataSource dataSource) {
		super();
		initDbConnect(dataSource);
	}

	/**
	 * 初始化
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void initDbConnect(DataSource dataSource) {
		if (dbConnect == null) {
			dbConnect = new DbConnect(dataSource);
		}
	}

	/**
	 * 带可变参数查询,返回执行结果
	 * 
	 * @param sql
	 *            查询sql
	 * @param para
	 *            可变参数
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> querySql(String sql, Object... para) throws SQLException {
		return dbConnect.querySql(sql, para);
	}

	/**
	 * 带可变参数查询,返回执行结果
	 * 
	 * @param <T>
	 * 
	 * @param sql
	 *            查询sql
	 * @param para
	 *            可变参数
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> executeQuery(T clazz, String sql, Object... para) throws SQLException {
		return dbConnect.querySql(clazz, sql, para);
	}


	/**
	 * 带可变参数查询，返回long类型数据
	 * 
	 * @param countSql
	 *            查询记录条数的sql
	 * @param para
	 *            可变参数
	 * @return
	 * @throws SQLException
	 * @throws SQLException
	 */
	public Long queryForLong(String countSql, Object... para) throws SQLException {
		return dbConnect.queryForLong(countSql, para);
	}

	/**
	 * 带可变参数, 执行sql插入，返回新增记录的自增主键<BR>
	 * 注意： 若插入的表无自增主键则返回 0，异常的话则返回 null
	 * 
	 * @param sql
	 * @param para
	 * @return
	 * @throws SQLException
	 * @see [类、类#方法、类#成员]
	 */
	public Long executeInsert(String sql, Object... para) throws SQLException {
		return dbConnect.insertSql(sql, para);
	}

	/**
	 * 带可变参数, 执行sql，返回执行影响的记录条数
	 * 
	 * @param sql
	 *            执行的sql 语句
	 * @param para
	 *            可变参数
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(String sql, Object... para) throws SQLException {
		return dbConnect.executeUpdate(sql, para);
	}
	
	public boolean testConnect() {
	    boolean result = false;
        try {
            Connection connection = dbConnect.getDataSource().getConnection();
            if (connection != null) {
                DbUtils.closeQuietly(connection);
                result = true;
            }            
        } catch (SQLException e) {
            return result;
        }	    
	    
	    return result;
	}
}