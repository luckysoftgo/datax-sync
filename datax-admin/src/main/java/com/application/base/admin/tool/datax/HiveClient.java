package com.application.base.admin.tool.datax;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author : 孤狼
 * @NAME: HiveClient
 * @DESC: HiveClient类设计
 **/
public class HiveClient {
	
	/*
	CREATE TABLE `sum_data_dir`(
	  `id` string COMMENT '主键id',
	  `sort_no` int COMMENT ' 排序number',
	  `class_name` string COMMENT '分类名称',
	  `data_type` string COMMENT '数据类型',
	  `data_volume` int COMMENT '分类结果值',
	  `second_level_name` string COMMENT '二级分类',
	  `first_level_name` string COMMENT '一级分类',
	  `create_time` string)
	ROW FORMAT SERDE
	  'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
	WITH SERDEPROPERTIES (
	  'field.delim'='\t',
	  'serialization.format'='\t')
	STORED AS INPUTFORMAT
	  'org.apache.hadoop.mapred.TextInputFormat'
	OUTPUTFORMAT
	  'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
	LOCATION
	  'hdfs://manager:8020/user/hive/warehouse/sum_data_dir'
	TBLPROPERTIES (
	  'transient_lastDdlTime'='1577172432')
	 */
	
	/**
	 * 获得hive存储的信息.
	 * @param tableName
	 * @return
	 */
	public Map<String,String> getHiveInfo(Connection connn,String tableName){
		String sql = "show create table  "+tableName;
		Statement statement = null;
		ResultSet resultSet = null;
		Map<String,String> hiveInfo = new HashMap<>();
		try {
			statement = connn.createStatement();
			resultSet = statement.executeQuery(sql);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			LinkedList<String> tmpList = new LinkedList<>();
			while(resultSet.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					tmpList.add(resultSet.getString(rsmd.getColumnName(i)));
				}
			}
			if (tmpList.size()%2==0){
				for (int i = 0; i <tmpList.size() ; i++) {
					if (i<=tmpList.size()-2){
						String key = tmpList.get(i);
						String value = tmpList.get(i+1);
						hiveInfo.put(key,value);
					}else{
						hiveInfo.put(tmpList.getLast(),tmpList.getFirst());
					}
				}
			}else{
				tmpList.addFirst("NULL");
				for (int i = 0; i <tmpList.size() ; i++) {
					if (i<=tmpList.size()-2){
						String key = tmpList.get(i);
						String value = tmpList.get(i+1);
						hiveInfo.put(key,value);
					}else{
						hiveInfo.put(tmpList.getLast(),tmpList.getFirst());
					}
				}
			}
			return hiveInfo;
		} catch (SQLException e) {
			return hiveInfo;
		}finally {
			close(connn,statement,null,resultSet);
		}
	}
	/**
	 * 关闭连接
	 * @param connection
	 * @param pstmt
	 * @param resultSet
	 */
	public void close(Connection connection, Statement stmt, PreparedStatement pstmt, ResultSet resultSet){
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		if(pstmt!=null){
			try {
				pstmt.close();
			} catch (SQLException e) {
			}
		}
		if(resultSet!=null){
			try {
				resultSet.close();
			} catch (SQLException e) {
			}
		}
		if(connection!=null){
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
	}
	
}
