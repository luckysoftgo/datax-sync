package com.application.base.code.hive;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author : 孤狼
 * @NAME: HiveJdbcUtil
 * @DESC: jdbc 客户端操作.
 * http://www.manongjc.com/article/100162.html
 **/
public class HiveJdbcClient {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * hive的driver
	 */
	private String hiveDriver="org.apache.hive.jdbc.HiveDriver";
	/**
	 * 存在的连接.
	 */
	private LinkedBlockingQueue<Connection> connections = new LinkedBlockingQueue<>(1024);
	
	int maxTotal = 10,minIdle=3;
	
	/**
	 * 属性设置.
	 */
	private String driver;
	private String url;
	private String userName;
	private String userPass;
	
	/**
	 * 构造方法.
	 * @param driver
	 * @param url
	 * @param userName
	 * @param userPass
	 */
	public HiveJdbcClient(String driver,String url,String userName,String userPass){
		this.driver = driver;
		this.url = url;
		this.userName =userName;
		this.userPass = userPass;
	}
	
	/**
	 * 创建连接池.
	 */
	public Connection hiveConnect(){
		try {
			if(StringUtils.isNotBlank(driver)){
				hiveDriver = driver;
			}
			synchronized (HiveJdbcClient.class){
				Class.forName(hiveDriver);
				Connection connn = DriverManager.getConnection(url,userName,userPass);
				connections.put(connn);
				return connn;
			}
		}catch (Exception e){
			e.printStackTrace();
			logger.error("初始化连接异常了,异常信息是:{}",e.getMessage());
		}
		return null;
	}
	
	
	/**
	 * 获取连接
	 * @return
	 */
	public Connection getConnection() {
		try {
			if (connections!=null && connections.size()>0){
				if (connections.size() < minIdle){
					int addCount = maxTotal - minIdle;
					for (int i = 0; i < addCount ; i++) {
						hiveConnect();
					}
				}
			}else{
				for (int i = 0; i < maxTotal ; i++) {
					hiveConnect();
				}
			}
			return connections.take();
		}catch (InterruptedException e){
			logger.error("获取connect连接异常了,错误异常是:{}",e.getMessage());
			return null;
		}
	}
	
	/**
	 * 执行 hive 相关的 sql.
	 * @param hiveSql
	 * @return
	 */
	private boolean execHiveSql(String hiveSql){
		logger.info("执行的sql是:{}",hiveSql);
		Connection connn = null;
		Statement statement = null;
		try {
			connn = getConnection();
			statement = connn.createStatement();
			return statement.execute(hiveSql);
		} catch (SQLException e) {
			logger.error("执行操作异常了,异常原因是:{}",e.getMessage());
			return false;
		}finally {
			close(connn,statement,null,null);
		}
	}
	
	
	/**
	 * 创建数据库.
	 * @param dbName
	 * @return
	 */
	public boolean createDatabase(String dbName) throws Exception {
		String sql = "create database "+dbName;
		return execHiveSql(sql);
	}
	
	
	/**
	 * 获取hive上库的信息.
	 * @return
	 */
	public LinkedList<String> showDatabases(){
		String sql = "show databases";
		Connection connn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		LinkedList<String> dataBases = new LinkedList<>();
		try {
			connn = getConnection();
			statement = connn.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String tableName = resultSet.getString(1);
				dataBases.add(tableName);
			}
			return dataBases;
		} catch (SQLException e) {
			logger.error("查询数据库异常了,异常原因是:{}",e.getMessage());
			return dataBases;
		}finally {
			close(connn,statement,null,resultSet);
		}
	}
	
	/**
	 * 仅执行hiveSql,不返回数据，只返回成功失败，比如执行创建表，加载数据等
	 * @param hiveSql
	 * @return
	 */
	public String excuteHiveSql(String hiveSql){
		logger.info("执行的sql是:{}",hiveSql);
		String result = "";
		Connection con = getConnection();
		try {
			Statement stmt = con.createStatement();
			int bool = stmt.executeUpdate(hiveSql);
			if (bool>0){
				result = "执行成功："+hiveSql;
			}
		}catch (Exception e){
			result = "执行失败："+hiveSql;
		}
		return result;
	}
	
	/**
	 * 创建表操作.
	 * @param createSql:
	 * create table test_hive(
		 * 	`empno` int,
		 * 	`ename` string,
		 * 	`job` string,
		 * 	`mgr` int,
		 * 	`hiredate` string,
		 * 	`sal` double,
		 * 	`comm` double,
		 * 	`deptno` int
		 * 	)
	 * row format delimited fields terminated by '\t' stored as textfile ;
	 * @return
	 */
	public boolean createTable(String createSql){
		return execHiveSql(createSql);
	}
	
	/**
	 * 获取生成hive表的hiveql
	 * @param tableName
	 * @param columnList : 列的名称.
	 * @return
	 */
	public boolean createStrTable(String tableName, List<String> columnList){
		StringBuffer buffer = new StringBuffer("create table "+tableName+"(");
		for (int i = 0; i < columnList.size() ; i++) {
			String column = columnList.get(i);
			if (StringUtils.isEmpty(column)){
				continue;
			}
			if (i==columnList.size()-1){
				buffer.append(" `"+column +"`    string ");
			}else{
				buffer.append(" `"+column +"`    string , ");
			}
		}
		buffer.append(") row format delimited fields terminated by ',' ");
		String sql= buffer.toString();
		logger.info("生成的sql是:{}",sql);
		return execHiveSql(sql);
	}
	
	/**
	 * 获取生成hive表的hiveql
	 * @param tableName
	 * @param columnMapList : 列和类型的集合.
	 * @return
	 */
	public boolean createTable(String tableName, List<Map<String,String >> columnMapList){
		StringBuffer buffer = new StringBuffer("create table "+tableName+"(");
		for (int i = 0; i < columnMapList.size() ; i++) {
			Map<String,String> map = columnMapList.get(i);
			Set<String> keys = map.keySet();
			if (i==columnMapList.size()-1){
				for (String key :keys){
					buffer.append(" `"+key +"`    "+map.get(key));
				}
			}else{
				for (String key :keys){
					buffer.append(" `"+key +"`    "+map.get(key) + ",");
				}
			}
		}
		buffer.append(") row format delimited fields terminated by ',' ");
		String sql= buffer.toString();
		logger.info("生成的sql是:{}",sql);
		return execHiveSql(sql);
	}
	
	/**
	 * 获取hive上表的信息.
	 * @return
	 */
	public LinkedList<String> showTables(){
		String sql = "show tables";
		Connection connn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		LinkedList<String> tables = new LinkedList<>();
		try {
			connn = getConnection();
			statement = connn.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String tableName = resultSet.getString(1);
				tables.add(tableName);
			}
			return tables;
		} catch (SQLException e) {
			logger.error("查询数据库表异常了,异常原因是:{}",e.getMessage());
			return tables;
		}finally {
			close(connn,statement,null,resultSet);
		}
	}
	
	/*
	1. 查询创建表信息（show create table）
	2. 查询表的字段信息（desc）
	3. 查看表的详细属性信息（desc formatted）
	4.  查看表的详细信息（describe extended）
	5. 查看表的partitions信息（show partitions ）
	*/
	
	/**
	 * 显示表的结构
	 * @param tableName
	 * map 的值为:
		 * "data_type" -> "bigint"
		 * "comment" -> ""
		 * "col_name" -> "account_id"
	 * @return
	 */
	public LinkedList<Map<String,String>> descTable(String tableName){
		String sql = "describe "+tableName;
		Connection connn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		LinkedList<Map<String,String>> tableDesc = new LinkedList<>();
		try {
			connn = getConnection();
			statement = connn.createStatement();
			resultSet = statement.executeQuery(sql);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			while(resultSet.next()) {
				Map<String,String> map = new HashMap<>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i), resultSet.getString(rsmd.getColumnName(i)));
				}
				tableDesc.add(map);
			}
			return tableDesc;
		} catch (SQLException e) {
			logger.error("查询数据库表结构异常了,异常原因是:{}",e.getMessage());
			return tableDesc;
		}finally {
			close(connn,statement,null,resultSet);
		}
	}
	
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
	public Map<String,String> getHiveInfo(String tableName){
		String sql = "show create table  "+tableName;
		Connection connn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Map<String,String> hiveInfo = new HashMap<>();
		try {
			connn = getConnection();
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
			logger.error("查询数据库表结构异常了,异常原因是:{}",e.getMessage());
			return hiveInfo;
		}finally {
			close(connn,statement,null,resultSet);
		}
	}
	
	/**
	 * 获取列信息.
	 * @param tableName
	 * @return
	 */
	public List<ColumnInfo> getHiveColumns(String tableName) {
		String sql = "describe "+tableName;
		Connection connn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		LinkedList<ColumnInfo> tableColumns = new LinkedList<>();
		try {
			connn = getConnection();
			statement = connn.createStatement();
			resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				String columnName = resultSet.getString(1);
				String columnType = resultSet.getString(2);
				String columnComment = resultSet.getString(3);
				ColumnInfo columnInfo = new ColumnInfo();
				columnInfo.setColumnName(columnName);
				columnInfo.setColumnHiveType(columnType);
				columnInfo.setColumnComment(columnComment);
				tableColumns.add(columnInfo);
			}
			return tableColumns;
		} catch (SQLException e) {
			logger.error("查询数据库表结构异常了,异常原因是:{}",e.getMessage());
			return tableColumns;
		}finally {
			close(connn,statement,null,resultSet);
		}
	}
	
	/**
	 * 获取列信息.
	 * @param hiveTableName
	 * @return
	 */
	public String getRdbmsCreateTableSql(String hiveTableName,String rdbmsTableName) {
		if (StringUtils.isBlank(rdbmsTableName)){
			rdbmsTableName = hiveTableName;
		}
		StringBuffer buffer = new StringBuffer(" create table "+rdbmsTableName+" (\n");
		try {
			List<ColumnInfo> columnInfos = getHiveColumns(hiveTableName);
			if (columnInfos.isEmpty()){
				logger.info("没有取得列的信息");
				return "";
			}
			for (int i = 0; i < columnInfos.size(); i++) {
				ColumnInfo columnInfo = columnInfos.get(i);
				if (i==columnInfos.size()-1){
					buffer.append(" "+columnInfo.getColumnName()+" "+getHive2RdbmsType(columnInfo.getColumnHiveType())+" comment '"+columnInfo.getColumnComment()+"'\n");
				}else{
					buffer.append(" "+columnInfo.getColumnName()+" "+getHive2RdbmsType(columnInfo.getColumnHiveType())+" comment '"+columnInfo.getColumnComment()+"',\n");
				}
			}
			buffer.append(") \n");
			buffer.append(" ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '"+rdbmsTableName+"表信息'");
		} catch (Exception e) {
			logger.error("查询数据库表结构异常了,异常原因是:{}",e.getMessage());
		}
		return buffer.toString();
	}
	
	/**
	 * 获取 rdbms 的类型.
	 * @param type
	 * @return
	 */
	private  String getRdbms2HiveType(String type) {
		String result = "";
		if (type.equalsIgnoreCase("varchar")){
			result = "string";
		}else if (type.equalsIgnoreCase("tinyint")||
				type.equalsIgnoreCase("smallint")||
				type.equalsIgnoreCase("int")||
				type.equalsIgnoreCase("bigint") ){
			result = "int";
		}else if (type.equalsIgnoreCase("double")||
				type.equalsIgnoreCase("float")){
			result = "float";
		}else if (type.equalsIgnoreCase("date")||
				type.equalsIgnoreCase("timestamp")||
				type.equalsIgnoreCase("datetime")){
			result = "date ";
		}else{
			result = "string";
		}
		return result;
	}
	
	/**
	 * 获取 rdbms 的类型.
	 * @param type
	 * @return
	 */
	private  String getRdbms2RdbmsType(String type) {
		String result = "";
		if (type.equalsIgnoreCase("varchar")){
			result = "varchar (200)";
		}else if (type.equalsIgnoreCase("tinyint")||
				type.equalsIgnoreCase("smallint")||
				type.equalsIgnoreCase("int")||
				type.equalsIgnoreCase("bigint") ){
			result = "int (11)";
		}else if (type.equalsIgnoreCase("double")||
				type.equalsIgnoreCase("float")){
			result = "decimal(15,2)";
		}else if (type.equalsIgnoreCase("date")||
				type.equalsIgnoreCase("timestamp")||
				type.equalsIgnoreCase("datetime")){
			result = "datetime ";
		}else{
			result = "varchar (200)";
		}
		return result;
	}
	
	/**
	 * 获取 hive 的类型.
	 * @param type
	 * @return
	 */
	private  String getHive2RdbmsType(String type) {
		String result = "";
		if (type.equalsIgnoreCase("string")){
			result = "varchar (200)";
		}else if (type.equalsIgnoreCase("tinyint")||
				type.equalsIgnoreCase("smallint")||
				type.equalsIgnoreCase("int")||
				type.equalsIgnoreCase("bigint") ){
			result = "int (11)";
		}else if (type.equalsIgnoreCase("double")||
				type.equalsIgnoreCase("float")){
			result = "decimal(15,2)";
		}else if (type.equalsIgnoreCase("date")||
				type.equalsIgnoreCase("timestamp")||
				type.equalsIgnoreCase("datetime")){
			result = "datetime ";
		}else{
			result = "varchar (200)";
		}
		return result;
	}
	
	/**
	 * 给固定的表添加数据文件.
	 * @param filePath
	 * @param tableName
	 * @return
	 */
	public boolean loadDataByPath(String filePath,String tableName){
		String sql= "load data local inpath '" + filePath + "' overwrite into table "+tableName;
		return execHiveSql(sql);
	}
	
	/**
	 * 给固定的表添加数据文件.
	 * @param hdfsPath
	 * @param tableName
	 * @return
	 */
	public boolean loadDataByHdfsPath(String hdfsPath,String tableName){
		String sql = "load data inpath 'hdfs:" + hdfsPath + "' overwrite into table "+tableName;
		return execHiveSql(sql);
	}
	
	/**
	 * 查询表中的数据.
	 * @param tableName
	 * @return
	 */
	public LinkedList<Map<String,Object>> selectTable(String tableName){
		String sql = "select * from "+tableName;
		return selectSqlTable(sql);
	}
	
	/**
	 * 查询表中的数据.
	 * @param sql
	 * @return
	 */
	public LinkedList<Map<String,Object>> selectSqlTable(String sql){
		Connection connn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		LinkedList<Map<String,Object>> tableDatas = new LinkedList<>();
		try {
			connn = getConnection();
			statement = connn.createStatement();
			resultSet = statement.executeQuery(sql);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			while(resultSet.next()) {
				Map<String,Object> map = new HashMap<>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i), resultSet.getObject(rsmd.getColumnName(i)));
				}
				tableDatas.add(map);
			}
			return tableDatas;
		} catch (SQLException e) {
			logger.error("查询数据库表结构异常了,异常原因是:{}",e.getMessage());
			return tableDatas;
		}finally {
			close(connn,statement,null,resultSet);
		}
	}
	
	/**
	 * 使用PreparedStatement查询数据
	 * @param sql
	 * @param param
	 * @return
	 */
	public LinkedList<Map<String,Object>> selectTable(String sql, String[] param){
		Connection connn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		LinkedList<Map<String,Object>> tableDatas = new LinkedList<>();
		try {
			connn = getConnection();
			statement=connn.prepareStatement(sql);
			if(param!=null){
				for (int i = 0; i < param.length; i++) {
					statement.setString(1+i, param [i]);
				}
			}
			resultSet = statement.executeQuery(sql);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			while(resultSet.next()) {
				Map<String,Object> map = new HashMap<>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i), resultSet.getObject(rsmd.getColumnName(i)));
				}
				tableDatas.add(map);
			}
			return tableDatas;
		} catch (SQLException e) {
			logger.error("查询数据库表结构异常了,异常原因是:{}",e.getMessage());
			return tableDatas;
		}finally {
			close(connn,statement,null,resultSet);
		}
	}
	
	/**
	 * 根据表名称、查询字段、条件、限制条数返回数据,若参数为空,请填入"";
	 * @param tableName
	 * @param columnList
	 * @param condition 条件.
	 * @param limitInfo 分页条件.
	 * @return
	 */
	public LinkedList<Map<String,Object>> selectTable(String tableName, List<String> columnList, String condition,String limitInfo){
		LinkedList<Map<String,Object>> resultList = new LinkedList<Map<String,Object>>();
		Connection con = getConnection();
		ResultSet res = null;
		StringBuffer buffer= new StringBuffer(" select ");
		
		if(columnList !=null&&columnList.size() > 0){
			for (int i = 0;i < columnList.size();i++){
				if(i != columnList.size()-1) {
					buffer.append(columnList.get(i) + ",");
				}else {
					buffer.append(columnList.get(i));
				}
			}
		}else{
			buffer.append(" * ");
		}
		buffer.append(" from " + tableName);
		if(condition!=null &&!condition.equals("")){
			buffer.append(" where "+condition);
		}
		if(limitInfo!=null&&!limitInfo.equals("")){
			buffer.append(" " + limitInfo);
		}
		try {
			Statement stmt = con.createStatement();
			res = stmt.executeQuery(buffer.toString());
			ResultSetMetaData rsmd = res.getMetaData();
			while(res.next()) {
				Map<String,Object> map = new HashMap<>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i), res.getObject(rsmd.getColumnName(i)));
				}
				resultList.add(map);
			}
		}catch (Exception e){
			logger.error("查询数据异常了,异常信息是:{}",e.getMessage());
		}
		return resultList;
	}
	
	/**
	 * 统计数据条数.
	 * @param tableName
	 * @return
	 */
	public Integer countData(String tableName){
		String sql = "select count(*) from "+tableName;
		Connection connn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		int count=0;
		try {
			connn = getConnection();
			statement = connn.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			return count;
		} catch (SQLException e) {
			logger.error("查询数据库总条数异常了,异常原因是:{}",e.getMessage());
			return count;
		}finally {
			close(connn,statement,null,resultSet);
		}
	}
	
	/**
	 * 刪除数据库.
	 * @param dbName
	 * @return
	 */
	public boolean deleteDatabase(String dbName){
		String sql = "drop database if exists "+dbName;
		return execHiveSql(sql);
	}
	
	/**
	 * 清空数据库表.
	 * @param tableName
	 * @return
	 */
	public boolean deleteTable(String tableName){
		String sql = "truncate table  "+tableName;
		return execHiveSql(sql);
	}
	
	/**
	 * 刪除数据库表.
	 * @param tableName
	 * @return
	 */
	public boolean dropTable(String tableName){
		String sql = "drop table if exists  "+tableName;
		return execHiveSql(sql);
	}
	
	/**
	 * 关闭连接
	 * @param connection
	 * @param pstmt
	 * @param resultSet
	 */
	public void close(Connection connection,Statement stmt,PreparedStatement pstmt,ResultSet resultSet){
		if(stmt!=null){
			try {
				stmt.close();
				stmt=null;
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
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
