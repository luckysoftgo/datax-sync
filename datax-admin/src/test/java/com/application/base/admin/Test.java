package com.application.base.admin;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.application.base.admin.util.Constant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author : 孤狼
 * @NAME: Test
 * @DESC: Test类设计
 **/
public class Test {
	
	public static void main(String[] args) {
		String url="jdbc:hive2://192.168.10.185:10000/test";
		String hiveDriver="org.apache.hive.jdbc.HiveDriver";
		hiveDriver="com.cloudera.hive.jdbc41.HS2Driver";
		/*
		//这里默认使用 hikari 数据源
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClassName(hiveDriver);
		dataSource.setUsername("");
		dataSource.setPassword("");
		dataSource.setInitialSize(5);
		dataSource.setMinIdle(5);
		dataSource.setMaxWait(50);
		dataSource.setMaxActive(25);
		
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
		Connection connection = getConn(url,hiveDriver,"","");
		List<String> comns = getColumnNames(connection,"sum_data_dir",hiveDriver);
		System.out.println(JSON.toJSONString(comns));
	}
	
	public static Connection getConn(String url,String driver,String name,String pass){
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url,name,pass);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> getColumnNames(Connection connection, String tableName, String driverClass) {
		List<String> res = Lists.newArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			//获取查询指定表所有字段的sql语句
			String querySql = "select *  from "+tableName+" where 1=0";
			querySql = "select *  from "+tableName+" limit 1";
			//获取所有字段
			stmt = connection.createStatement();
			rs = stmt.executeQuery(querySql);
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnName(i);
				if (JdbcConstants.HIVE_DRIVER.equals(driverClass)) {
					if (columnName.contains(Constant.SPLIT_POINT)) {
						res.add(columnName.substring(columnName.indexOf(Constant.SPLIT_POINT) + 1) + Constant.SPLIT_SCOLON + metaData.getColumnTypeName(i));
					} else {
						res.add(columnName + Constant.SPLIT_SCOLON + metaData.getColumnTypeName(i));
					}
				} else {
					res.add(columnName);
				}
				
			}
		} catch (SQLException e) {
			System.out.println("出错了.");
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(stmt);
			JdbcUtils.close(connection);
		}
		return res;
	}
}
