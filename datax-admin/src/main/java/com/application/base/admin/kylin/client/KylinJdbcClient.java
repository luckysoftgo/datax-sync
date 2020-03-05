package com.application.base.admin.kylin.client;

import com.application.base.admin.kylin.KylinConstant;
import com.application.base.admin.kylin.KylinException;
import com.application.base.admin.kylin.config.KylinTaskConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

/**
 * @author : 孤狼
 * @NAME: KylinJdbcClient
 * @DESC: jdbc 客户端操作.
 *  http://kylin.apache.org/docs23/howto/howto_jdbc.html
 **/
@Component
public class KylinJdbcClient {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * kylin的driver
	 */
	private String kylinDriver="org.apache.kylin.jdbc.Driver";

	@Autowired
	private KylinTaskConfig kylinTaskConfig;

	/**
	 * 获取连接
	 * @return
	 */
	public Connection getConnection() throws KylinException {
		try {
			Driver driver = (Driver) Class.forName(kylinDriver).newInstance();
			Properties info = new Properties();
			info.put(KylinConstant.USER, kylinTaskConfig.getKylinName());
			info.put(KylinConstant.PASSWORD, kylinTaskConfig.getKylinPass());
			Connection connection = driver.connect(kylinTaskConfig.getKylinUrl(),info);
			return connection;
		}catch (Exception e){
			logger.error("kylin获得连接异常了,异常信息是:{}",e.getMessage());
			throw new KylinException("kylin获得连接异常了,异常信息是:{"+e.getMessage()+"}");
		}
	}
	
	/**
	 * 使用PreparedStatement查询数据
	 * @param sql
	 * @param param
	 * @return
	 */
	public LinkedList<Map<String,Object>> selectSQL(String sql, String [] param) throws KylinException {
		ResultSet resultSet = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		LinkedList<Map<String,Object>> finalList = new LinkedList<>();
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(sql);
			if (param!=null && param.length>0){
				for (int i = 1; i <= param.length; i++) {
					pstmt.setString(i, param[i]);
				}
			}
			resultSet = pstmt.executeQuery();
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int count = rsmd.getColumnCount();
			Set<String> columns = new HashSet<>();
			for (int i = 1; i <= count ; i++) {
				columns.add(rsmd.getColumnName(i));
			}
			while (resultSet.next()) {
				Map<String,Object> data = new HashMap<>();
				for (String column : columns ) {
					data.put(column,resultSet.getObject(column));
				}
				finalList.add(data);
			}
		} catch (SQLException e) {
			logger.error("kylin通过PrepareStatement获得数据异常了,异常信息是:{}",e.getMessage());
			throw new KylinException("kylin通过PrepareStatement获得数据异常了,异常信息是:{"+e.getMessage()+"}");
		}finally {
			close(connection,pstmt,resultSet);
		}
		return finalList;
	}
	
	/**
	 * 使用Statement查询数据
	 * @param sql
	 * @return
	 */
	public LinkedList<Map<String,Object>> selectSQL(String sql) throws KylinException {
		return selectSQL(sql,null);
	}
	
	/**
	 * 使用PreparedStatement查询数据
	 * @param sql
	 * @param param
	 * @return
	 */
	public LinkedList<Map<String,Object>> selectSQL(String sql, String [] param,boolean nullback) throws KylinException {
		ResultSet resultSet = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		LinkedList<Map<String,Object>> finalList = new LinkedList<>();
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(sql);
			if (param!=null && param.length>0){
				for (int i = 1; i <= param.length; i++) {
					pstmt.setString(i, param[i]);
				}
			}
			resultSet = pstmt.executeQuery();
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int count = rsmd.getColumnCount();
			Set<String> columns = new HashSet<>();
			for (int i = 1; i <= count ; i++) {
				columns.add(rsmd.getColumnName(i));
			}
			while (resultSet.next()) {
				Map<String,Object> data = new HashMap<>();
				for (String column : columns ) {
					String value = Objects.toString(resultSet.getObject(column),"");
					if (StringUtils.isBlank(value)){
						if (nullback){
							data.put(column,resultSet.getObject(column));
						}
					}else{
						data.put(column,resultSet.getObject(column));
					}
				}
				finalList.add(data);
			}
		} catch (SQLException e) {
			logger.error("kylin通过PrepareStatement获得数据异常了,异常信息是:{}",e.getMessage());
			throw new KylinException("kylin通过PrepareStatement获得数据异常了,异常信息是:{"+e.getMessage()+"}");
		}finally {
			close(connection,pstmt,resultSet);
		}
		return finalList;
	}
	
	/**
	 * 使用Statement查询数据
	 * @param sql
	 * @return
	 */
	public LinkedList<Map<String,Object>> selectSQL(String sql,boolean nullback) throws KylinException {
		ResultSet resultSet = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		LinkedList<Map<String,Object>> finalList = new LinkedList<>();
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(sql);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int count = rsmd.getColumnCount();
			Set<String> columns = new HashSet<>();
			for (int i = 1; i <= count ; i++) {
				columns.add(rsmd.getColumnName(i));
			}
			while (resultSet.next()) {
				Map<String,Object> data = new HashMap<>();
				for (String column : columns ) {
					String value = Objects.toString(resultSet.getObject(column),"");
					if (StringUtils.isBlank(value)){
						if (nullback){
							data.put(column,resultSet.getObject(column));
						}
					}else{
						data.put(column,resultSet.getObject(column));
					}
				}
				finalList.add(data);
			}
		} catch (SQLException e) {
			logger.error("kylin通过PrepareStatement获得数据异常了,异常信息是:{}",e.getMessage());
			throw new KylinException("kylin通过PrepareStatement获得数据异常了,异常信息是:{"+e.getMessage()+"}");
		}finally {
			close(connection,pstmt,resultSet);
		}
		return finalList;
	}
	
	/**
	 * 使用Statement查询数据
	 * @param tableName
	 * @return
	 */
	public LinkedList<Map<String,Object>> selectMetaSQL(String tableName) throws KylinException {
		ResultSet resultSet = null;
		Connection connection = null;
		LinkedList<Map<String,Object>> finalList = new LinkedList<>();
		try {
			connection = getConnection();
			resultSet = connection.getMetaData().getTables(null,null,tableName,null);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int count = rsmd.getColumnCount();
			Set<String> columns = new HashSet<>();
			for (int i = 1; i <= count ; i++) {
				columns.add(rsmd.getColumnName(i));
			}
			while (resultSet.next()) {
				Map<String,Object> data = new HashMap<>();
				for (String column : columns ) {
					data.put(column,resultSet.getObject(column));
				}
				finalList.add(data);
			}
		} catch (SQLException e) {
			logger.error("kylin通過Statement获得数据异常了,异常信息是:{}",e.getMessage());
			throw new KylinException("kylin通過Statement获得数据异常了,异常信息是:{"+e.getMessage()+"}");
		}
		return finalList;
	}
	
	/**
	 * 检测是否连接
	 */
	public boolean check(String projectName){
		boolean result = false;
		try {
			result = getConnection().isClosed();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 关闭连接
	 * @param connection
	 * @param pstmt
	 * @param resultSet
	 */
	public void close(Connection connection,PreparedStatement pstmt,ResultSet resultSet){
		if(pstmt!=null){
			try {
				pstmt.close();
				pstmt=null;
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
		if(resultSet!=null){
			try {
				resultSet.close();
				resultSet=null;
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
		if(connection!=null){
			try {
				connection.close();
				connection=null;
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
