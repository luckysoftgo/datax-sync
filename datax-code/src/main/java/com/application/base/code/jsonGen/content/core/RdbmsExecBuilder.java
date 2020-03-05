package com.application.base.code.jsonGen.content.core;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : admin
 * @NAME: RdbmsExecBuilder
 * @DESC: 创建 rdbms 的对象
 **/
public class RdbmsExecBuilder {
	
	/********************************************************************rdbms reader start *******************************************************************************/
	
	/**
	 * 构建通用的信息.
	 * @param username rdbms 连接的用户名.
	 * @param password rdbms 连接的密码.
	 * @param column 列名,如果为空,则默认为"*".
	 * @param splitPK 分片的主键.
	 * @param where sql的where条件
	 * @param querySql 查询的sql
	 * @param fetchSize 该配置项定义了插件和数据库服务器端每次批量数据获取条数，该值决定了DataX和服务器端的网络交互次数，能够较大的提升数据抽取性能。<br />
	 * @param session 控制写入数据的时间格式，时区等的配置，如果表中有时间字段，配置该值以明确告知写入 rdbms 的时间格式。通常配置的参数为：NLS_DATE_FORMAT,NLS_TIME_FORMAT。其配置的值为 json 格式
	 * @return
	 */
	public static Map<String,Object> builderCommonReader(String username, String password, List<String> column, String splitPK,String where,String[] querySql,Integer fetchSize,String[] session){
		Map<String,Object> commonReaderMap = new HashMap<>(16);
		commonReaderMap.put("username",username);
		commonReaderMap.put("password",password);
		if (column==null ){
			column=new ArrayList<>();
			column.add("*");
			commonReaderMap.put("column",column);
		}else {
			commonReaderMap.put("column",column);
		}
		if (StringUtils.isNotBlank(splitPK)){
			commonReaderMap.put("splitPK",splitPK);
		}
		if (StringUtils.isNotBlank(where)){
			commonReaderMap.put("where",where);
		}
		if (querySql!=null && querySql.length>0){
			commonReaderMap.put("querySql",querySql);
		}
		if (fetchSize!=null){
			commonReaderMap.put("fetchSize",fetchSize);
		}
		if (StringUtils.isAnyBlank(session)){
			commonReaderMap.put("session",session);
		}
		return commonReaderMap;
	}
	
	/**
	 * 构建connect的信息
	 * @param jdbcUrls jdbcUrl 的连接信息,可以使用多个来实现.
	 * @param tables 要同步的表的信息.
	 * @return
	 */
	public static Map<String,Object> builderConnectionReader(List<String> tables, List<String> jdbcUrls){
		Map<String,Object> connectionMap = new HashMap<>(12);
		connectionMap.put("jdbcUrl",jdbcUrls);
		connectionMap.put("table",tables);
		List<Map<String,Object>> connList=new ArrayList();
		connList.add(connectionMap);
		Map<String,Object> connection = new HashMap<>(16);
		connection.put("connection",connList);

		return connection;
	}
	
	/********************************************************************rdbms reader end *******************************************************************************/
	
	
	/********************************************************************rdbms writer start *******************************************************************************/

	/**
	 * 公共的读取信息的
	 * @param writeMode 控制写入数据到目标表采用 `insert into` 或者 `replace into` 或者 `ON DUPLICATE KEY UPDATE` 语句
	 * @param batchSize 一次性批量提交的记录数大小，该值可以极大减少DataX与Rdbms的网络交互次数，并提升整体吞吐量。但是该值设置过大可能会造成DataX运行进程OOM情况。
	 * @param username rdbms 连接的用户名.
	 * @param password rdbms 连接的密码.
	 * @param column 列名,如果为空,则默认为"*".
	 * @param preSql 预处理的sql. delete from test or truncate test
	 * @param postSql
	 * @param session
	 * @return
	 */
	public static Map<String,Object> builderCommonWriter(String writeMode,Integer batchSize,String username, String password, List<String> column,List<String> preSql,List<String> postSql,List<String>  session){
		Map<String,Object> commonWriterMap = new HashMap<>(16);
		if (StringUtils.isBlank(writeMode)){
			commonWriterMap.put("writeMode","insert ");
		}else{
			commonWriterMap.put("writeMode",writeMode);
		}
		if (batchSize!=null){
			commonWriterMap.put("batchSize",batchSize);
		}
		commonWriterMap.put("username",username);
		commonWriterMap.put("password",password);
		if (column==null){
			column=new ArrayList<>();
			column.add("*");
			commonWriterMap.put("column",column);
		}else {
			commonWriterMap.put("column",column);
		}
		if (preSql!=null && preSql.size()>0){
			commonWriterMap.put("preSql",preSql);
		}
		if (postSql!=null && postSql.size()>0){
			commonWriterMap.put("postSql",postSql);
		}
		if (session!=null && session.size()>0){
			commonWriterMap.put("session",session);
		}
		return commonWriterMap;
	}
	
	/**
	 * 构建connect的信息
	 * @param jdbcUrl jdbcUrl 的连接信息,可以使用多个来实现.
	 * @param tables 要同步的表的信息.
	 * @return
	 */
	public static Map<String,Object> builderConnectionWriter(String jdbcUrl,List<String> tables){
		Map<String,Object> connectionMap = new HashMap<>(12);
		connectionMap.put("jdbcUrl",jdbcUrl);
		connectionMap.put("table",tables);
		List<Map<String,Object>> connList=new ArrayList();
		connList.add(connectionMap);
		Map<String,Object> connection = new HashMap<>(16);
		connection.put("connection",connList);
		
		return connection;
	}
	
	/********************************************************************rdbms writer end *******************************************************************************/
	
}
