package com.application.base.code.altas;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.application.base.code.altas.bean.Clazficateion;
import com.application.base.code.altas.bean.DbRdbmsInfo;
import com.application.base.code.altas.bean.TableAltasColumn;
import com.application.base.code.altas.bean.TableBasicBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : admin
 * @NAME: AltasClient
 * @DESC: AltasClient类设计
 **/
public class AltasClient {
	
	static Logger logger = LoggerFactory.getLogger(AltasClient.class.getName());
	
	/**
	 * 源地址:
	 */
	public static String basicUrl="http://192.168.10.185:21000/";
	
	/**
	 * 实例对象.
	 */
	private static AltasClient instance;
	
	/**
	 * 构造函数.
	 * @param basicUrl
	 */
	public AltasClient(String basicUrl){
		AltasClient.basicUrl = basicUrl;
	}
	
	/**
	 * 单例模式.
	 * @param altasUrl
	 * @return
	 */
	public static synchronized AltasClient getInstance(String altasUrl) {
		if (instance == null) {
			instance = new AltasClient(altasUrl);
		}
		return instance;
	}
	
	/**
	 * 测试数据.
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		AltasClient client=new AltasClient(basicUrl);
		String cookie = client.getCookie("admin","admin");
		if (StringUtils.isEmpty(cookie)){
			System.out.println("认证失败!");
			return;
		}
		
		List<Clazficateion> classList = client.getClassificate(cookie);
		System.out.println("infos="+JSON.toJSONString(classList));
		List<TableBasicBean> tableBasicBeans  = client.getAltasInfos("1.1.企业登记信息数据源","hive_table","hive_table",cookie,true);
		Map<String,Object> mapInfo = client.getHiveTableInfo(tableBasicBeans.get(0).getGuid(),cookie);
		System.out.println("1 datas="+JSON.toJSONString(mapInfo));
		
		tableBasicBeans = client.getAltasInfos(null,"rdbms_db","rdbms_db",cookie,false);
		DbRdbmsInfo rdbmsInfo = client.getRdbmsTables(tableBasicBeans.get(0).getGuid(),cookie);
		Map<String,Object> hiveColumn = client.getRdbmsTableInfo(rdbmsInfo.getTableInfos().get(0).getDataGuid(),cookie);
		System.out.println("2 datas="+JSON.toJSONString(hiveColumn));
	}
	
	/**
	 * 获取hive的表信息.
	 * @param guid
	 * @param cookie
	 * @return
	 */
	public Map<String,Object> getHiveTableInfo(String guid,String cookie){
		guid = Objects.toString(guid,"").trim();
		cookie = Objects.toString(cookie,"").trim();
		Map<String,Object> resutMap = new HashMap<>();
		TableAltasColumn tableColumnInfo = getAltasColumn(guid,cookie);
		String hdfsLocal="";
		if (tableColumnInfo!=null){
			hdfsLocal=getHiveLocal(tableColumnInfo.getSdGuid(),cookie);
			resutMap.put("tableName",tableColumnInfo.getTableName());
			resutMap.put("hdfsLocal",hdfsLocal);
			resutMap.put("tableComment",tableColumnInfo.getComment());
			resutMap.put("tableColumns",tableColumnInfo.getColumns());
			resutMap.put("tableHiveSql",getHiveCreateSql(tableColumnInfo,true));
			resutMap.put("tableRdbmsSql",getRdbmsCreateSql(tableColumnInfo,true));
		}
		return resutMap;
	}
	
	/**
	 * 获取hive的表信息.
	 * @param guid
	 * @param cookie
	 * @return
	 */
	public Map<String,Object> getRdbmsTableInfo(String guid,String cookie){
		guid = Objects.toString(guid,"").trim();
		cookie = Objects.toString(cookie,"").trim();
		Map<String,Object> resutMap = new HashMap<>();
		TableAltasColumn tableColumnInfo = getAltasColumn(guid,cookie);
		if (tableColumnInfo!=null){
			resutMap.put("tableName",tableColumnInfo.getTableName());
			resutMap.put("tableComment",tableColumnInfo.getComment());
			resutMap.put("tableColumns",tableColumnInfo.getColumns());
			resutMap.put("tableHiveSql",getHiveCreateSql(tableColumnInfo,false));
			resutMap.put("tableRdbmsSql",getRdbmsCreateSql(tableColumnInfo,false));
		}
		return resutMap;
	}
	
	/**
	 * 生成建表的语句.
	 * @param tableColumnInfo
	 * @return
	 */
	private String getHiveCreateSql(TableAltasColumn tableColumnInfo,boolean hive) {
		StringBuffer buffer = new StringBuffer("create table if not exists "+tableColumnInfo.getTableName()+" ( ");
		List<TableAltasColumn.ColumnInfo> columns = tableColumnInfo.getColumns();
		for (int i = 0; i < columns.size() ; i++) {
			TableAltasColumn.ColumnInfo columnInfo = columns.get(i);
			if (hive){
				buffer.append(" " + columnInfo.getName()+ " " + columnInfo.getType()+" comment '"+ Objects.toString(columnInfo.getComment(),"") +"'");
			}else {
				buffer.append(" " + columnInfo.getName()+ " " + getRdbms2HiveType(columnInfo.getType())+" comment '"+Objects.toString(columnInfo.getComment(),"")+"'");
			}
			if (i==columns.size()-1){
				buffer.append(" ");
			}else{
				buffer.append(", ");
			}
		}
		buffer.append(") ");
		buffer.append("row format delimited fields terminated by '\t' null defined as '' stored as textfile ");
		return buffer.toString();
	}
	
	
	/**
	 * 生成建表的语句.
	 * @param tableColumnInfo
	 * @return
	 */
	private String getRdbmsCreateSql(TableAltasColumn tableColumnInfo,boolean hive) {
		StringBuffer buffer = new StringBuffer("create table if not exists "+tableColumnInfo.getTableName()+" ( ");
		List<TableAltasColumn.ColumnInfo> columns = tableColumnInfo.getColumns();
		for (int i = 0; i < columns.size() ; i++) {
			TableAltasColumn.ColumnInfo columnInfo = columns.get(i);
			if (hive){
				buffer.append(" " + columnInfo.getName()+ " " + getHive2RdbmsType(columnInfo.getType())+" comment '"+Objects.toString(columnInfo.getComment(),"")+"'");
			}else{
				buffer.append(" " + columnInfo.getName()+ " " + getRdbms2RdbmsType(columnInfo.getType())+" comment '"+Objects.toString(columnInfo.getComment(),"")+"'");
			}
			if (i==columns.size()-1){
				buffer.append(" ");
			}else{
				buffer.append(", ");
			}
		}
		buffer.append(") ");
		buffer.append(" engine = innodb auto_increment = 0 character set = utf8mb4 collate = utf8mb4_general_ci row_format = dynamic ");
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
	 * 获得了hive的local地址.
	 * @param guid
	 * @param cookie
	 * @return
	 */
	public  String getHiveLocal(String guid,String cookie){
		String result = "";
		try {
			String reqUrl=basicUrl+"api/atlas/v2/entity/guid/"+guid;
			String reqParams = "minExtInfo=true";
			String returnStr = HttpRequestUtils.sendGet(reqUrl,reqParams,cookie);
			if (StringUtils.isEmpty(returnStr)){
				logger.info("请求altas获得结果为空!");
				return result;
			}
			JSONObject object = JSON.parseObject(returnStr);
			JSONObject oattributes =object.getJSONObject("entity").getJSONObject("attributes");
			if (oattributes==null){
				logger.info("请求altas返回串中attributes结果为空!");
				return result;
			}
			result = oattributes.getString("location");
		}catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 通过guid查询表信息.
	 * @param guid
	 * @param cookie
	 * @return
	 */
	public  TableAltasColumn getAltasColumn(String guid,String cookie){
		guid = Objects.toString(guid,"").trim();
		cookie = Objects.toString(cookie,"").trim();
		TableAltasColumn tableColumnInfo = new TableAltasColumn();
		try {
			String reqUrl=basicUrl+"api/atlas/v2/entity/guid/"+guid;
			String reqParams = "minExtInfo=true";
			String result = HttpRequestUtils.sendGet(reqUrl,reqParams,cookie);
			if (StringUtils.isEmpty(result)){
				logger.info("请求altas获得结果为空!");
				return tableColumnInfo;
			}
			JSONObject object = JSON.parseObject(result);
			JSONObject oattributes =object.getJSONObject("entity").getJSONObject("attributes");
			if (oattributes==null){
				logger.info("请求altas返回串中attributes结果为空!");
				return tableColumnInfo;
			}
			tableColumnInfo.setComment(oattributes.getString("comment"));
			tableColumnInfo.setTableName(oattributes.getString("name"));
			JSONArray guids = oattributes.getJSONArray("columns");
			JSONObject sd = oattributes.getJSONObject("sd");
			String sdGuid = "";
			if (sd!=null){
				sdGuid = sd.getString("guid");
				tableColumnInfo.setSdGuid(sdGuid);
			}
			List<String> guidList = new ArrayList<>();
			if (guids==null || guids.size()==0){
				logger.info("请求altas返回串中attributes中的columns结果为空!");
				return tableColumnInfo;
			}
			for (int i = 0; i < guids.size() ; i++) {
				JSONObject tmpObj = guids.getJSONObject(i);
				guidList.add(tmpObj.getString("guid"));
			}
			JSONObject referredEntities = object.getJSONObject("referredEntities");
			List<TableAltasColumn.ColumnInfo> columns = new ArrayList<>();
			for (int i = 0; i < guidList.size(); i++) {
				String tmpStr = referredEntities.getString(guidList.get(i));
				JSONObject instance = JSON.parseObject(tmpStr);
				TableAltasColumn.ColumnInfo columnInfo = tableColumnInfo.createColumn();
				columnInfo.setGuid(instance.getString("guid"));
				JSONObject attributes = instance.getJSONObject("attributes");
				if (attributes==null){
					continue;
				}
				String qualifiedName = attributes.getString("qualifiedName");
				columnInfo.setQualifiedName(qualifiedName);
				String findName = attributes.getString("name");
				columnInfo.setName(findName);
				String comment = attributes.getString("comment");
				columnInfo.setComment(comment);
				String typeName=instance.getString("typeName");
				String type =null;
				if (typeName.equalsIgnoreCase("rdbms_column")){
					type = attributes.getString("data_type");
				}
				if (typeName.equalsIgnoreCase("hive_column")){
					type = attributes.getString("type");
				}
				columnInfo.setType(type);
				columns.add(columnInfo);
			}
			tableColumnInfo.setColumns(columns);
		}catch (Exception e){
			e.printStackTrace();
		}
		return tableColumnInfo;
	}
	
	
	/**
	 * 通过guid查询表信息.
	 * @param guid
	 * @param cookie
	 * @return
	 */
	public  DbRdbmsInfo getRdbmsTables(String guid,String cookie){
		guid = Objects.toString(guid,"").trim();
		cookie = Objects.toString(cookie,"").trim();
		DbRdbmsInfo rdbmsInfo = new DbRdbmsInfo();
		try {
			String reqUrl=basicUrl+"api/atlas/v2/entity/guid/"+guid;
			String reqParams = "minExtInfo=true";
			String result = HttpRequestUtils.sendGet(reqUrl,reqParams,cookie);
			if (StringUtils.isEmpty(result)){
				logger.info("请求altas获得结果为空!");
				return rdbmsInfo;
			}
			JSONObject object = JSON.parseObject(result);
			JSONObject oattributes =object.getJSONObject("entity").getJSONObject("attributes");
			if (oattributes==null){
				logger.info("请求altas返回串中attributes结果为空!");
				return rdbmsInfo;
			}
			JSONArray guids = oattributes.getJSONArray("tables");
			List<String> guidList = new ArrayList<>();
			if (guids==null || guids.size()==0){
				logger.info("请求altas返回串中attributes中的tables结果为空!");
				return rdbmsInfo;
			}
			for (int i = 0; i < guids.size() ; i++) {
				JSONObject tmpObj = guids.getJSONObject(i);
				String typeName = tmpObj.getString("typeName");
				if (typeName.equalsIgnoreCase("rdbms_table")){
					guidList.add(tmpObj.getString("guid"));
				}
			}
			rdbmsInfo.setDbUrl(oattributes.getString("qualifiedName"));
			rdbmsInfo.setDbName(oattributes.getString("name"));
			JSONObject referredEntities = object.getJSONObject("referredEntities");
			List<DbRdbmsInfo.TableInfo> tableInfos = new ArrayList<>();
			for (int i = 0; i < guidList.size(); i++) {
				String dataGuid = guidList.get(i);
				String tmpStr = referredEntities.getString(dataGuid);
				JSONObject instance = JSON.parseObject(tmpStr);
				DbRdbmsInfo.TableInfo tableInfo = rdbmsInfo.createTable();
				tableInfo.setDataGuid(dataGuid);
				JSONObject attributes = instance.getJSONObject("attributes");
				if (attributes==null){
					continue;
				}
				String findName = attributes.getString("name");
				tableInfo.setTableName(findName);
				String qualifiedName = attributes.getString("qualifiedName");
				tableInfo.setQualifiedName(qualifiedName);
				String description = attributes.getString("description");
				tableInfo.setTableDesc(description);
				tableInfos.add(tableInfo);
			}
			rdbmsInfo.setTableInfos(tableInfos);
		}catch (Exception e){
			e.printStackTrace();
		}
		return rdbmsInfo;
	}
	
	/**
	 * 查询altas上所有 hive 表信息.
	 * @param classification
	 * @param cookie
	 * @param filter
	 * @return
	 */
	public List<TableBasicBean> getAltasInfos(String classification, String query, String typeName, String cookie, boolean filter){
		classification = Objects.toString(classification,"").trim();
		query = Objects.toString(query,"").trim();
		typeName = Objects.toString(typeName,"").trim();
		cookie = Objects.toString(cookie,"").trim();
		List<TableBasicBean> tableBasicBeans = new ArrayList<>();
		try {
			String reqUrl=basicUrl+"api/atlas/v2/search/basic";
			Map<String,String> reqParams = new HashMap<>();
			reqParams.put("query",query);
			reqParams.put("typeName",typeName);
			reqParams.put("limit","1000");
			reqParams.put("offset","0");
			reqParams.put("excludeDeletedEntities","true");
			reqParams.put("includeSubClassifications","true");
			reqParams.put("includeSubTypes","true");
			reqParams.put("includeClassificationAttributes","true");
			if (StringUtils.isNotBlank(classification)){
				reqParams.put("classification",classification);
			}
			String result = HttpRequestUtils.sendPost(reqUrl,JSON.toJSONString(reqParams),cookie);
			if (StringUtils.isEmpty(result)){
				logger.info("请求altas返回串中结果为空!");
				return tableBasicBeans;
			}
			JSONObject object =JSON.parseObject(result);
			JSONArray entities = object.getJSONArray("entities");
			if (entities.isEmpty()){
				logger.info("请求altas返回串中entities结果为空!");
				return tableBasicBeans;
			}
			for (int i = 0; i < entities.size(); i++) {
				TableBasicBean basicBean =new TableBasicBean();
				JSONObject instance = entities.getJSONObject(i);
				String tableName = instance.getString("typeName");
				basicBean.setTypeName(tableName);
				String guid = instance.getString("guid");
				basicBean.setGuid(guid);
				JSONObject attributes = instance.getJSONObject("attributes");
				if (attributes==null){
					continue;
				}
				String qualifiedName = attributes.getString("qualifiedName");
				basicBean.setQualifiedName(qualifiedName);
				String findName = attributes.getString("name");
				if (tableName.equalsIgnoreCase("hive_table")){
					if (filter){
						//String regex="^*invalid|valid|profile|feed$";
						String regex="^*invalid|valid|profile$";
						Pattern pattern = Pattern.compile(regex);
						Matcher matcher = pattern.matcher(findName);
						if(matcher.find()){
							continue;
						}
					}
				}
				basicBean.setName(findName);
				JSONArray classificats = instance.getJSONArray("classificationNames");
				if (classificats!=null && classificats.size()>0) {
					String[] classNames = new String[classificats.size()];
					for (int j = 0; j < classificats.size(); j++) {
						classNames[j] = classificats.get(j).toString();
					}
					basicBean.setClassificationNames(classNames);
				}
				tableBasicBeans.add(basicBean);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return tableBasicBeans;
	}
	
	/**
	 * 目录查询
	 * @param cookie
	 * @return
	 */
	public List<Clazficateion> getClassificate(String cookie){
		List<Clazficateion> classList = new ArrayList<>();
		try {
			String reqUrl=basicUrl+"api/atlas/v2/types/typedefs";
			String reqParams="type=classification";
			String result = HttpRequestUtils.sendGet(reqUrl,reqParams,cookie);
			JSONObject object = JSON.parseObject(result);
			JSONArray entities = object.getJSONArray("classificationDefs");
			if (entities.isEmpty()){
				logger.info("请求altas返回串中classificationDefs结果为空!");
				return classList;
			}
			for (int i = 0; i < entities.size(); i++) {
				JSONObject instance = entities.getJSONObject(i);
				Clazficateion clasDefs = JSON.parseObject(instance.toJSONString(), Clazficateion.class);
				classList.add(clasDefs);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return classList;
	}
	
	/**
	 * 认证
	 * @param userName
	 * @param passWord
	 */
	public String getCookie(String userName,String passWord){
		userName = Objects.toString(userName,"").trim();
		passWord = Objects.toString(passWord,"").trim();
		String key = "Set-Cookie";
		String result = "";
		try {
			String reqParams="j_spring_security_check?j_username="+userName+"&j_password="+passWord;
			String reqUrl = basicUrl+reqParams;
			Map<String, List<String>> map = HttpRequestUtils.getHeaders(reqUrl,null);
			if (map.isEmpty()){
				return "";
			}else{
				if (map.containsKey(key)){
					result = map.getOrDefault(key,new ArrayList<>()).get(0);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
