package com.application.base.code.jsonGen;

import com.alibaba.fastjson.JSON;
import com.application.base.code.jsonGen.cont.DataXType;
import com.application.base.code.jsonGen.content.ContentJsonBuilder;
import com.application.base.code.jsonGen.content.core.BasicColumn;
import com.application.base.code.jsonGen.content.core.HiveExecBuilder;
import com.application.base.code.jsonGen.content.core.MongoColumn;
import com.application.base.code.jsonGen.content.core.MongoExecBuilder;
import com.application.base.code.jsonGen.content.core.RdbmsExecBuilder;
import com.application.base.code.jsonGen.setting.CommonJsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : admin
 * @NAME: JsonMapBuilder
 * @DESC: 用于生成json串.
 **/
public class JsonMapBuilder {
	
	/**
	 * 测试结果
	 * @param args
	 */
	public static void main(String[] args) {
		getHive2Mysql();
		//getMysql2Hive();
		//getMysql2Mysql();
		//getMongodb2Mongodb();
	}
	
	/**
	 * mysql 2 hive demo
	 */
	private static String getHive2Mysql() {
		// writer 信息
		List<BasicColumn> column =new ArrayList<>(16);
		BasicColumn column1 = new BasicColumn(0,"ID","string");
		BasicColumn column2 = new BasicColumn(1,"SORT_NO","long");
		BasicColumn column3 = new BasicColumn(2,"CLASS_NAME","string");
		BasicColumn column4 = new BasicColumn(3,"DATA_TYPE","string");
		BasicColumn column5 = new BasicColumn(4,"DATA_VOLUME","long");
		BasicColumn column6 = new BasicColumn(5,"SECOND_LEVEL_NAME","string");
		BasicColumn column7 = new BasicColumn(6,"FIRST_LEVEL_NAME","string");
		BasicColumn column8 = new BasicColumn(7,"CREATE_TIME","string");
		column.add(column1);column.add(column2);column.add(column3);column.add(column4);
		column.add(column5);column.add(column6);column.add(column7);column.add(column8);
		Map<String,Object> readerInfo = HiveExecBuilder.builderHiveReader("/user/hive/warehouse/sum_data_dir/mysql2hive*","hdfs://192.168.10.185:8020","text",column);
		readerInfo = ContentJsonBuilder.builderReader(DataXType.HDFSREADER.getValue(),readerInfo);
		
		List<String> rcolumn=new ArrayList<>(16);
		rcolumn.add("ID");
		rcolumn.add("SORT_NO");
		rcolumn.add("CLASS_NAME");
		rcolumn.add("DATA_TYPE");
		rcolumn.add("DATA_VOLUME");
		rcolumn.add("SECOND_LEVEL_NAME");
		rcolumn.add("FIRST_LEVEL_NAME");
		rcolumn.add("CREATE_TIME");
		// reader 信息.
		List<String> tables =new ArrayList<>();
		tables.add("datax_test2");
		String jdbcUrl = "jdbc:mysql://192.168.10.143:3306/datax_web?useUnicode=true&characterEncoding=utf8";
		
		Map<String,Object> writerConn = RdbmsExecBuilder.builderConnectionWriter(jdbcUrl,tables);
		Map<String,Object> writerInfo = RdbmsExecBuilder.builderCommonWriter(null,null,"root","db#@!123WC",rcolumn,null,null,null);
		writerInfo.putAll(writerConn);
		writerInfo = ContentJsonBuilder.builderWeader(DataXType.MYSQLWRITER.getValue(),writerInfo);
		
		// 信息合并
		Map<String,Object> allInfo =new HashMap<>(16);
		allInfo.putAll(readerInfo);
		allInfo.putAll(writerInfo);
		
		//信息录入list中去.
		List<Map<String,Object>> listMap = new ArrayList<>(16);
		listMap.add(allInfo);
		
		// 放入content中去.
		Map<String,Object> contentMap = new HashMap<>(16);
		contentMap.put("content",listMap);
		
		// 设置settings信息.
		Map<String, Object> speed = CommonJsonBuilder.builderSpeed(1,null,null);
		Map<String, Object> errorLimit = null; // CommonJsonBuilder.builderErrorLimit(1,new BigDecimal("0.0001"));
		Map<String, Object> setting = CommonJsonBuilder.builderSetting(speed,errorLimit);
		contentMap.put("setting",setting);
		
		// 转换成json.
		Map<String,Object> jobMap = new HashMap<>(16);
		jobMap.put("job",contentMap);
		// 返回.
		String json = JSON.toJSONString(jobMap);
		System.out.println(json);
		return json;
		
	}
	
	/**
	 * mysql 2 hive demo
	 */
	private static String getMysql2Hive() {
		List<String> rcolumn=new ArrayList<>(16);
		rcolumn.add("ID");
		rcolumn.add("SORT_NO");
		rcolumn.add("CLASS_NAME");
		rcolumn.add("DATA_TYPE");
		rcolumn.add("DATA_VOLUME");
		rcolumn.add("SECOND_LEVEL_NAME");
		rcolumn.add("FIRST_LEVEL_NAME");
		rcolumn.add("CREATE_TIME");
		// reader 信息.
		List<String> tables =new ArrayList<>();
		tables.add("datax_test1");
		List<String> jdbcUrls =new ArrayList<>();
		jdbcUrls.add("jdbc:mysql://192.168.10.143:3306/datax_web?useUnicode=true&characterEncoding=utf8");
		Map<String,Object> readerConn = RdbmsExecBuilder.builderConnectionReader(tables,jdbcUrls);
		Map<String,Object> readerInfo = RdbmsExecBuilder.builderCommonReader("root","db#@!123WC",rcolumn,null,null,null,null,null);
		readerInfo.putAll(readerConn);
		readerInfo = ContentJsonBuilder.builderReader(DataXType.MYSQLREADER.getValue(),readerInfo);
		
		// writer 信息
		List<BasicColumn> column =new ArrayList<>(16);
		BasicColumn column1 = new BasicColumn("ID","string");
		BasicColumn column2 = new BasicColumn("SORT_NO","int");
		BasicColumn column3 = new BasicColumn("CLASS_NAME","string");
		BasicColumn column4 = new BasicColumn("DATA_TYPE","string");
		BasicColumn column5 = new BasicColumn("DATA_VOLUME","int");
		BasicColumn column6 = new BasicColumn("SECOND_LEVEL_NAME","string");
		BasicColumn column7 = new BasicColumn("FIRST_LEVEL_NAME","string");
		BasicColumn column8 = new BasicColumn("CREATE_TIME","string");
		column.add(column1);column.add(column2);column.add(column3);column.add(column4);
		column.add(column5);column.add(column6);column.add(column7);column.add(column8);
		Map<String,Object> writerInfo = HiveExecBuilder.builderHiveWriter("hdfs://192.168.10.185:8020","text","/user/hive/warehouse/sum_data_dir",column,null,"\t","append","mysql2hive");
		writerInfo = ContentJsonBuilder.builderWeader(DataXType.HDFSWRITER.getValue(),writerInfo);
		
		// 信息合并
		Map<String,Object> allInfo =new HashMap<>(16);
		allInfo.putAll(readerInfo);
		allInfo.putAll(writerInfo);
		
		//信息录入list中去.
		List<Map<String,Object>> listMap = new ArrayList<>(16);
		listMap.add(allInfo);
		
		// 放入content中去.
		Map<String,Object> contentMap = new HashMap<>(16);
		contentMap.put("content",listMap);
		
		// 设置settings信息.
		Map<String, Object> speed = CommonJsonBuilder.builderSpeed(1,null,null);
		Map<String, Object> errorLimit = null; // CommonJsonBuilder.builderErrorLimit(1,new BigDecimal("0.0001"));
		Map<String, Object> setting = CommonJsonBuilder.builderSetting(speed,errorLimit);
		contentMap.put("setting",setting);
		
		// 转换成json.
		Map<String,Object> jobMap = new HashMap<>(16);
		jobMap.put("job",contentMap);
		// 返回.
		String json = JSON.toJSONString(jobMap);
		System.out.println(json);
		return json;
	
	}
	
	
	/**
	 * Mongodb 2 Mongodb demo.
	 * @return
	 */
	private static String getMongodb2Mongodb(){
		List<MongoColumn> rcolumns = new ArrayList<>(16);
		MongoColumn rcolumn1 = new MongoColumn("companyId","string");
		MongoColumn rcolumn2 = new MongoColumn("apiId","string");
		MongoColumn rcolumn3 = new MongoColumn("sourceId","string");
		MongoColumn rcolumn4 = new MongoColumn("apiKey","string");
		MongoColumn rcolumn5 = new MongoColumn("resultJson","string");
		MongoColumn rcolumn6 = new MongoColumn("uniqueKey","string");
		MongoColumn rcolumn7 = new MongoColumn("uniqueFlag","string");
		MongoColumn rcolumn8 = new MongoColumn("apiType","string");
		MongoColumn rcolumn9 = new MongoColumn("initFlag","string");
		MongoColumn rcolumn10 = new MongoColumn("createTime","string");
		rcolumns.add(rcolumn1);
		rcolumns.add(rcolumn2);
		rcolumns.add(rcolumn3);
		rcolumns.add(rcolumn4);
		rcolumns.add(rcolumn5);
		rcolumns.add(rcolumn6);
		rcolumns.add(rcolumn7);
		rcolumns.add(rcolumn8);
		rcolumns.add(rcolumn9);
		rcolumns.add(rcolumn10);
		
		List<MongoColumn> columns = new ArrayList<>(16);
		MongoColumn column1 = new MongoColumn("companyId","string");
		MongoColumn column2 = new MongoColumn("apiId","string");
		MongoColumn column3 = new MongoColumn("sourceId","string");
		MongoColumn column4 = new MongoColumn("apiKey","string");
		MongoColumn column5 = new MongoColumn("resultJson","string");
		MongoColumn column6 = new MongoColumn("uniqueKey","string");
		MongoColumn column7 = new MongoColumn("uniqueFlag","string");
		MongoColumn column8 = new MongoColumn("apiType","string");
		MongoColumn column9 = new MongoColumn("initFlag","string");
		MongoColumn column10 = new MongoColumn("createTime","string");
		columns.add(column1);
		columns.add(column2);
		columns.add(column3);
		columns.add(column4);
		columns.add(column5);
		columns.add(column6);
		columns.add(column7);
		columns.add(column8);
		columns.add(column9);
		columns.add(column10);
		
		// reader 信息.
		Map<String,Object> readerInfo = MongoExecBuilder.builderMongoReader(new String[]{"192.168.2.206"},"admin","123456",columns,"data_sync","data_sync_sign");
		readerInfo = ContentJsonBuilder.builderReader(DataXType.MONGODBREADER.getValue(),readerInfo);
		
		// writer 信息
		Map<String,Object> writerInfo = MongoExecBuilder.builderMongoWriter(new String[]{"192.168.2.206"},"admin","123456",rcolumns,"data_sync","data_sync_test");
		writerInfo.putAll(MongoExecBuilder.builderUpsertInfo(null,"companyId"));
		writerInfo = ContentJsonBuilder.builderWeader(DataXType.MONGODBWRITER.getValue(),writerInfo);
		
		// 信息合并
		Map<String,Object> allInfo =new HashMap<>(16);
		allInfo.putAll(readerInfo);
		allInfo.putAll(writerInfo);
		
		//信息录入list中去.
		List<Map<String,Object>> listMap = new ArrayList<>(16);
		listMap.add(allInfo);
		
		// 放入content中去.
		Map<String,Object> contentMap = new HashMap<>(16);
		contentMap.put("content",listMap);
		
		// 设置settings信息.
		Map<String, Object> speed = CommonJsonBuilder.builderSpeed(1,null,null);
		Map<String, Object> errorLimit = null; // CommonJsonBuilder.builderErrorLimit(1,new BigDecimal("0.0001"));
		Map<String, Object> setting = CommonJsonBuilder.builderSetting(speed,errorLimit);
		contentMap.put("setting",setting);
		
		// 转换成json.
		Map<String,Object> jobMap = new HashMap<>(16);
		jobMap.put("job",contentMap);
		// 返回.
		String json = JSON.toJSONString(jobMap);
		System.out.println(json);
		return json;
	}
	
	
	/**
	 * mysql 2 mysql demo .
	 * @return
	 */
	private static String getMysql2Mysql(){
		List<String> column=new ArrayList<>(16);
		column.add("ID");
		column.add("SORT_NO");
		column.add("CLASS_NAME");
		column.add("DATA_TYPE");
		column.add("DATA_VOLUME");
		column.add("SECOND_LEVEL_NAME");
		column.add("FIRST_LEVEL_NAME");
		column.add("CREATE_TIME");
		
		// reader 信息.
		List<String> rtables =new ArrayList<>();
		rtables.add("datax_test1");
		List<String> rjdbcUrls =new ArrayList<>();
		rjdbcUrls.add("jdbc:mysql://192.168.10.143:3306/datax_web?useUnicode=true&characterEncoding=utf8");
		Map<String,Object> readerConn = RdbmsExecBuilder.builderConnectionReader(rtables,rjdbcUrls);
		Map<String,Object> readerInfo = RdbmsExecBuilder.builderCommonReader("root","db#@!123WC",column,null,null,null,null,null);
		readerInfo.putAll(readerConn);
		readerInfo = ContentJsonBuilder.builderReader(DataXType.MYSQLREADER.getValue(),readerInfo);
		
		// writer 信息
		List<String> wtables =new ArrayList<>();
		wtables.add("datax_test2");
		String jdbcUrl= "jdbc:mysql://192.168.10.143:3306/datax_web?useUnicode=true&characterEncoding=utf8";
		Map<String,Object> writerConn = RdbmsExecBuilder.builderConnectionWriter(jdbcUrl,wtables);
		
		List<String> preSql= new ArrayList<>();
		preSql.add("truncate datax_test2");
		
		List<String> session= new ArrayList<>();
		session.add("set session sql_mode='ANSI'");
		
		Map<String,Object> writerInfo = RdbmsExecBuilder.builderCommonWriter(null,null,"root","db#@!123WC",column,preSql,null,session);
		writerInfo.putAll(writerConn);
		writerInfo = ContentJsonBuilder.builderWeader(DataXType.MYSQLWRITER.getValue(),writerInfo);
		
		// 信息合并
		Map<String,Object> allInfo =new HashMap<>(16);
		allInfo.putAll(readerInfo);
		allInfo.putAll(writerInfo);
		
		//信息录入list中去.
		List<Map<String,Object>> listMap = new ArrayList<>(16);
		listMap.add(allInfo);
		
		// 放入content中去.
		Map<String,Object> contentMap = new HashMap<>(16);
		contentMap.put("content",listMap);
		
		// 设置settings信息.
		Map<String, Object> speed = CommonJsonBuilder.builderSpeed(1,null,null);
		Map<String, Object> errorLimit = null;
		Map<String, Object> setting = CommonJsonBuilder.builderSetting(speed,errorLimit);
		contentMap.put("setting",setting);
		
		// 转换成json.
		Map<String,Object> jobMap = new HashMap<>(16);
		jobMap.put("job",contentMap);
		
		// 返回.
		String json = JSON.toJSONString(jobMap);
		System.out.println(json);
		return json;
	}
}
