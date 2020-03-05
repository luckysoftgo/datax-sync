package com.application.base.code.jsonGen.content.core;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : admin
 * @NAME: MongoExecBuilder
 * @DESC: 创建 mongo 的对象
 **/
public class MongoExecBuilder {
	
	/********************************************************************mongo reader start *******************************************************************************/
	
	/**
	 * mongo 中读取数据.
	 * @param address
	 * @param userName
	 * @param userPassword
	 * @param column
	 * @param dbName
	 * @param collectionName
	 * @return
	 */
	public static Map<String,Object> builderMongoReader(String[] address, String userName, String userPassword, List<MongoColumn> column, String dbName, String collectionName){
		Map<String,Object> commonReaderMap = new HashMap<>(16);
		commonReaderMap.put("address",address);
		commonReaderMap.put("userName",userName);
		commonReaderMap.put("userPassword",userPassword);
		commonReaderMap.put("column",column);
		commonReaderMap.put("dbName",dbName);
		commonReaderMap.put("collectionName",collectionName);
		return commonReaderMap;
	}
	
	/********************************************************************mongo reader end *******************************************************************************/
	
	
	/********************************************************************mongo writer start *******************************************************************************/
	/**
	 *
	 * @param isUpsert
	 * @param upsertKey
	 * @return
	 */
	public static Map<String,Object> builderUpsertInfo(String isUpsert ,String upsertKey){
		Map<String,Object> upsertInfoMap = new HashMap<>(16);
		if (StringUtils.isNotBlank(isUpsert)){
			upsertInfoMap.put("isUpsert",isUpsert);
		}else{
			upsertInfoMap.put("isUpsert","true");
		}
		if (StringUtils.isNotBlank(upsertKey)){
			upsertInfoMap.put("upsertKey",upsertKey);
		}
		Map<String,Object> returnMap = new HashMap<>(16);
		returnMap.put("upsertInfo",upsertInfoMap);
		return returnMap;
	}
	
	/**
	 * mongo 中读取数据.
	 * @param address
	 * @param userName
	 * @param userPassword
	 * @param column
	 * @param dbName
	 * @param collectionName
	 * @return
	 */
	public static Map<String,Object> builderMongoWriter(String[] address, String userName, String userPassword, List<MongoColumn> column, String dbName, String collectionName){
		Map<String,Object> commonReaderMap = new HashMap<>(16);
		commonReaderMap.put("address",address);
		commonReaderMap.put("userName",userName);
		commonReaderMap.put("userPassword",userPassword);
		commonReaderMap.put("column",column);
		commonReaderMap.put("dbName",dbName);
		commonReaderMap.put("collectionName",collectionName);
		return commonReaderMap;
	}
	
	/********************************************************************mongo writer end *******************************************************************************/
	
}
