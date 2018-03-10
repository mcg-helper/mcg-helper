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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 带事务支持的数据库底层操作类
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DbConnect {
    
	Logger log = LoggerFactory.getLogger(getClass());
	private DataSource dataSource;
	private ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

	public DbConnect(DataSource dataSource) {
       this.dataSource = dataSource;

	}

	/**
	 * 创建数据库连接
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Connection getConnection() {
		Connection connection = threadLocal.get();
		try {
			if (connection == null) {
				connection = dataSource.getConnection();
				threadLocal.set(connection);
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new RuntimeException("获取数据库连接失败.....");
		}
		return connection;
	}
    
	/**
	 * 带可变参数查询,返回执行结果
	 * 
	 * @param sql 查询sql
	 * @param para 可变参数
	 * @return
	 */
	public List<Map<String, Object>> querySql(String sql, Object... para) throws SQLException {
		log.info("querySql: {}, para: {}", sql, ToStringBuilder.reflectionToString(para));
		QueryRunner runner = new QueryRunner();
		Connection conn = null;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			conn = getConnection();
			result = runner.query(conn, sql, new MapListHandler(), para);
		} catch (SQLException e) {
			log.error("------querySql error: {}------", e.getMessage());
			throw e;
		} finally {
			if (conn != null && conn.getAutoCommit() == true) {
				freeConnection();
			}
		}
		return result;
	}

	/**
	 * 
	 * @Title:       querySql   
	 * @Description: TODO(带可变参数查询,返回执行结果)   
	 * @param:       @param clazz 转换的对象实例
	 * @param:       @param sql 查询sql语句
	 * @param:       @param para 查询参数
	 * @param:       @return
	 * @param:       @throws SQLException      
	 * @return:      List      
	 * @throws
	 */
	public <T> List querySql(T clazz, String sql, Object... para) throws SQLException {
		log.info("querySql: {}, para: {}", sql, ToStringBuilder.reflectionToString(para));
		QueryRunner runner = new QueryRunner();
		Connection conn = null;
		List<T> result = new ArrayList<T>();
		try {
			conn = getConnection();
			// 下划线分隔的表字段名转换为实体bean驼峰命名属性
			BeanProcessor bean = new GenerousBeanProcessor();
			RowProcessor processor = new BasicRowProcessor(bean);
			result = (List<T>) runner.query(conn, sql, new BeanListHandler((Class) clazz, processor), para);
		} catch (SQLException e) {
			log.error("------querySql error: {}------", e.getMessage());
			throw e;
		} finally {
			if (conn != null && conn.getAutoCommit() == true) {
				freeConnection();
			}
		}
		return result;
	}

	/**
	 * 
	 * @Title:       queryForLong   
	 * @Description: TODO(带可变参数查询，返回long类型数据)   
	 * @param:       @param countSql 查询记录总条数
	 * @param:       @param para 参数
	 * @param:       @return
	 * @param:       @throws SQLException      
	 * @return:      Long      
	 * @throws
	 */
	public Long queryForLong(String countSql, Object... para) throws SQLException {
		log.info("queryForLong: {}, para: {}", countSql, ToStringBuilder.reflectionToString(para));
		QueryRunner runner = new QueryRunner();
		Long number = null;
		Connection conn = null;
		try {
			conn = getConnection();
			number = runner.query(conn, countSql, new ScalarHandler<Long>(), para);
		} catch (SQLException e) {
			log.error("------queryForLong error: {}------", e.getMessage());
			throw e;
		} finally {
			if (conn != null && conn.getAutoCommit() == true) {
				freeConnection();
			}
			log.info("DB queryForLong end ");
		}
		return number;
	}

	/**
	 * 
	 * @Title:       executeUpdate   
	 * @Description: TODO(带可变参数, 执行sql，返回执行影响的记录条数)   
	 * @param:       @param sql 执行的sql语句
	 * @param:       @param para 参数
	 * @param:       @return
	 * @param:       @throws SQLException      
	 * @return:      int      
	 * @throws
	 */
	public int executeUpdate(String sql, Object... para) throws SQLException {
		log.info("executeUpdate: {}, para: {}", sql, ToStringBuilder.reflectionToString(para));
		QueryRunner runner = new QueryRunner();
		Connection conn = null;
		int count = 0;
		try {
			conn = getConnection();
			count = runner.update(conn, sql, para);
		} catch (SQLException e) {
			log.error("------executeUpdate error: {}------", e.getMessage());
			throw e;
		} finally {
			if (conn != null && conn.getAutoCommit() == true) {
				freeConnection();
			}
			log.info("DB execSql end ");
		}
		return count;
	}

	/**
	 * 
	 * @Title:       insertSql   
	 * @Description: TODO(带可变参数, 执行sql插入，返回新增记录的自增主键。注意： 若插入的表无自增主键则返回 0，异常的话则返回 null)   
	 * @param:       @param sql 执行的sql语句
	 * @param:       @param para 参数
	 * @param:       @return
	 * @param:       @throws SQLException      
	 * @return:      Long      
	 * @throws
	 */
	public Long insertSql(String sql, Object... para) throws SQLException {
		log.info("InsertSql: {}, para: {}", sql, ToStringBuilder.reflectionToString(para));
		QueryRunner runner = new QueryRunner();
		Connection conn = null;
		Long id = null;
		try {
			conn = getConnection();
			id = (Long) runner.insert(conn, sql, new ScalarHandler<Object>(), para);
		} catch (SQLException e) {
			log.error("------insertSql error: {}------", e.getMessage());
			throw e;
		} finally {
			if (conn != null && conn.getAutoCommit() == true) {
				freeConnection();
			}
			log.info("DB execute InsertSql end ");
		}
		return id;
	}

	/**
	 * 
	 * @Title:       executeBatch   
	 * @Description: TODO(批量更新)   
	 * @param:       @param sql 执行的sql语句
	 * @param:       @param params 二维数组参数
	 * @param:       @throws SQLException      
	 * @return:      void      
	 * @throws
	 */
	public void executeBatch(String sql, Object[][] params) throws SQLException {
		log.info("executeBatch: {}, params:{}", sql, ToStringBuilder.reflectionToString(params));
		QueryRunner runner = new QueryRunner();
		Connection conn = null;
		try {
			conn = getConnection();
			runner.batch(conn, sql, params);
		} catch (SQLException e) {
			log.error("------executeBatch Error:{}------", e.getMessage());
			throw e;
		} finally {
			if (conn != null && conn.getAutoCommit() == true) {
				freeConnection();
			}
			log.info("DB executeBatch end ");
		}
	}

	/**
	 * 
	 * @Title:       executeBatch   
	 * @Description: TODO(批量更新)   
	 * @param:       @param sql 执行的sql语句
	 * @param:       @param params 参数
	 * @param:       @throws SQLException      
	 * @return:      void      
	 * @throws
	 */
	public void executeBatch(String sql, List<Object[]> params) throws SQLException {
		Object[][] paramArr = params.toArray(new Object[0][]);
		executeBatch(sql, paramArr);
	}

	/**
	 * 
	 * @Title:       freeConnection   
	 * @Description: TODO(释放数据库连接)   
	 * @param:             
	 * @return:      void      
	 * @throws
	 */
	public void freeConnection() {
		log.info("------释放数据库连接------");
		Connection conn = threadLocal.get();
		if (conn != null) {
			DbUtils.closeQuietly(conn);
			threadLocal.remove(); // 解除当前线程上绑定conn
		}
	}

	// ************** 事务操作 **************

	/**
	 * 
	 * @Title:       startTransaction   
	 * @Description: TODO(开启事务)   
	 * @param:             
	 * @return:      void      
	 * @throws
	 */
	public void startTransaction() {
		log.info("------开启事务-------");
		try {
			Connection conn = threadLocal.get();
			if (conn == null) {
				conn = getConnection();
				threadLocal.set(conn);
			}
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 
	 * @Title:       commit   
	 * @Description: TODO(提交事务)   
	 * @param:             
	 * @return:      void      
	 * @throws
	 */
	public void commit() {
		log.info("------提交事务-------");
		try {
			Connection conn = threadLocal.get();
			if (conn != null) {
				conn.commit();
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 
	 * @Title:       rollback   
	 * @Description: TODO(回滚事务)   
	 * @param:             
	 * @return:      void      
	 */
	public void rollback() {
		log.info("------ 系统异常，回滚事务------");
		try {
			Connection conn = threadLocal.get();
			if (conn != null) {
				conn.rollback();
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}