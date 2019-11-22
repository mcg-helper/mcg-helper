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

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.postgresql.ds.PGPoolingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcg.common.sysenum.DatabaseTypeEnum;
import com.mcg.entity.common.Table;
import com.mcg.entity.flow.data.DataRecord;
import com.mcg.entity.global.datasource.McgDataSource;
import com.mcg.plugin.dbconn.mssql.MssqlConnectImpl;
import com.mcg.plugin.dbconn.mysql.MysqlConnectImpl;
import com.mcg.plugin.dbconn.oracle.OracleConnectImpl;
import com.mcg.plugin.dbconn.postgresql.PostgresqlConnectImpl;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

//import oracle.jdbc.pool.OracleDataSource;

public class FlowDataAdapterImpl implements McgBizAdapter {

	private static Logger logger = LoggerFactory.getLogger(FlowDataAdapterImpl.class);
	
    private McgConnect mcgConnect;
    
    public FlowDataAdapterImpl(McgDataSource mcgDataSource) {
//        if(DatabaseTypeEnum.MYSQL.getValue().equals(mcgDataSource.getDbType())) {
//            MysqlDataSource mysqlDataSource = new MysqlDataSource();
//            mysqlDataSource.setServerName(mcgDataSource.getDbServer());
//            mysqlDataSource.setPort(mcgDataSource.getDbPort());
//            mysqlDataSource.setDatabaseName(mcgDataSource.getDbName());
//            mysqlDataSource.setCharacterEncoding("UTF-8");
//            mysqlDataSource.setCharacterSetResults("UTF-8");
//            mysqlDataSource.setUser(mcgDataSource.getDbUserName());
//            mysqlDataSource.setPassword(mcgDataSource.getDbPwd());
//            try {
//				mysqlDataSource.setConnectTimeout(3000);
//			} catch (SQLException e) {
//				logger.debug("mysql数据源适配失败，异常信息：{}", e.getMessage());
//			}
//            mcgConnect = new MysqlConnectImpl(mysqlDataSource);
//        } else if(DatabaseTypeEnum.ORACLE.getValue().equals(mcgDataSource.getDbType())) {
//            try {
//                OracleDataSource oracleDataSource = new OracleDataSource();
//                oracleDataSource.setDriverType("thin");
//                oracleDataSource.setNetworkProtocol("tcp");
//                oracleDataSource.setServerName(mcgDataSource.getDbServer());
//                oracleDataSource.setDatabaseName(mcgDataSource.getDbName());
//                oracleDataSource.setPortNumber(mcgDataSource.getDbPort());
//                oracleDataSource.setUser(mcgDataSource.getDbUserName());
//                oracleDataSource.setPassword(mcgDataSource.getDbPwd());
//                oracleDataSource.setLoginTimeout(3000);
//                mcgConnect = new OracleConnectImpl(oracleDataSource);
//            } catch (SQLException e) {
//            	logger.debug("oracle数据源适配失败，异常信息：{}", e.getMessage());
//            }
//        } else if(DatabaseTypeEnum.MSSQL.getValue().equals(mcgDataSource.getDbType())) {
//        	try {
//	            SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
//	            sqlServerDataSource.setDatabaseName(mcgDataSource.getDbName());
//	            sqlServerDataSource.setUser(mcgDataSource.getDbUserName());
//	            sqlServerDataSource.setPassword(mcgDataSource.getDbPwd());
//	            sqlServerDataSource.setServerName(mcgDataSource.getDbServer());
//	            sqlServerDataSource.setPortNumber(mcgDataSource.getDbPort());
//	            sqlServerDataSource.setLoginTimeout(3000);
//	            mcgConnect = new MssqlConnectImpl(sqlServerDataSource);
//        	} catch (Exception e) {
//        		logger.debug("oracle数据源适配失败，异常信息：{}", e.getMessage());
//			}
//        } else if(DatabaseTypeEnum.POSTGRESQL.getValue().equals(mcgDataSource.getDbType())) {
//        	PGPoolingDataSource pgDataSource = new PGPoolingDataSource();
//        	try {
//	            pgDataSource.setDatabaseName(mcgDataSource.getDbName());
//	            pgDataSource.setUser(mcgDataSource.getDbUserName());
//	            pgDataSource.setPassword(mcgDataSource.getDbPwd());
//	            pgDataSource.setPortNumber(mcgDataSource.getDbPort());
//	            pgDataSource.setServerName(mcgDataSource.getDbServer());
//
//				pgDataSource.setLoginTimeout(3000);
//			} catch (SQLException e) {
//				logger.debug("postgresql数据源适配失败，异常信息：{}", e.getMessage());
//			}
//            mcgConnect = new PostgresqlConnectImpl(pgDataSource);
            
//        }
    }
    
    @Override
    public List<DataRecord> getTableInfo(String tableName) throws Exception {
        
        return mcgConnect.getTableInfo(tableName);
    }

    @Override
    public List<Table> getTablesByDataSource(String dbName) throws SQLException {
        
        return mcgConnect.getTablesByDataSource(dbName);
    }

    @Override
    public List<Map<String, Object>> tableQuery(String sql, Object... para) throws SQLException {
        
        return mcgConnect.querySql(sql, para);
    }

    @Override
    public int executeUpdate(String sql, Object... para) throws SQLException {
        
        return mcgConnect.executeUpdate(sql, para);
    }

    @Override
    public boolean testConnect() {
    	boolean flag = false;
    	try {
    		flag = mcgConnect.testConnect();
    	} catch (Exception e) {
    		logger.debug("数据连接测试失败，异常信息：{}", e.getMessage());
		}
        return flag;
    }
    
}