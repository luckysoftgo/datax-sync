package com.application.base.code.jsonGen.content.core;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : admin
 * @NAME: HbaseExecBuilder
 * @DESC: 创建 hbase 的对象
 **/
public class HbaseExecBuilder {
	
	/**
	 * 构建 zk 的监听器.
	 * @return
	 */
	public static Map<String,Object> builderHbaseConfig(String rootdir,Boolean distributed,String zkquorum){
		Map<String,Object> hbaseConfigMap = new HashMap<>(16);
		if (StringUtils.isNotBlank(rootdir)){
			hbaseConfigMap.put("hbase.rootdir",rootdir);
		}
		if (distributed!=null){
			hbaseConfigMap.put("hbase.cluster.distributed",distributed);
		}
		if (StringUtils.isNotBlank(zkquorum)){
			hbaseConfigMap.put("hbase.zookeeper.quorum",zkquorum);
		}
		return hbaseConfigMap;
	}
	
	/**
	 * range构建.
	 * @param startRowkey
	 * @param endRowkey
	 * @param isBinaryRowkey
	 * @return
	 */
	public static Map<String,Object> builderHbaseRange(String startRowkey,String endRowkey, Boolean isBinaryRowkey){
		Map<String,Object> hbaseRangeMap = new HashMap<>(16);
		if (StringUtils.isNotBlank(startRowkey)){
			hbaseRangeMap.put("startRowkey",startRowkey);
		}
		if (StringUtils.isNotBlank(endRowkey)){
			hbaseRangeMap.put("endRowkey",endRowkey);
		}
		if (isBinaryRowkey!=null){
			hbaseRangeMap.put("isBinaryRowkey",isBinaryRowkey);
		}
		return hbaseRangeMap;
	}
	
	/**
	 * rowkeycolumn 构建.
	 * @param rowKeyColumn
	 * @return
	 */
	public static Map<String,Object> builderHbaseRowkeyColumn(List<Map<String,Object>> rowKeyColumn){
		Map<String,Object> hbaseRangeMap = new HashMap<>(16);
		hbaseRangeMap.put("rowKeyColumn",rowKeyColumn);
		return hbaseRangeMap;
	}
	
	/******************************************************************** hbase reader start *******************************************************************************/
	
	/**
	 * 构建请求的头信息.
	 * @param table
	 * @param column
	 * @param encoding
	 * @param mode
	 * @param scanCacheSize
	 * @param scanBatchSize
	 * @return
	 */
	public static Map<String,Object> builderHbaseReader(String table, List<Map<String,Object>> column, String encoding,String mode,Integer scanCacheSize,Integer scanBatchSize){
		Map<String,Object> commonReaderMap = new HashMap<>(16);
		commonReaderMap.put("username",table);
		commonReaderMap.put("column",column);
		if (StringUtils.isNotBlank(encoding)){
			commonReaderMap.put("encoding",encoding);
		}else {
			commonReaderMap.put("encoding","UTF-8");
		}
		if (StringUtils.isNotBlank(mode)){
			commonReaderMap.put("mode",mode);
		}
		if (scanCacheSize!=null){
			commonReaderMap.put("scanCacheSize",scanCacheSize);
		}else {
			commonReaderMap.put("scanCacheSize",1024);
		}
		if (scanBatchSize!=null){
			commonReaderMap.put("scanBatchSize",scanBatchSize);
		}else{
			commonReaderMap.put("scanBatchSize",500);
		}
		return commonReaderMap;
	}
	
	/********************************************************************hbase reader end *******************************************************************************/
	
	
	/********************************************************************hbase writer start *******************************************************************************/
	
	/**
	 *
	 * @param table
	 * @param encoding
	 * @param column
	 * @param mode
	 * @return
	 */
	private static Map<String,Object> builderCommonWriter(String table,String encoding,List<Map<String,Object>> column,String mode){
		Map<String,Object> commonWriterMap = new HashMap<>(16);
		commonWriterMap.put("path",table);
		commonWriterMap.put("column",column);
		if (StringUtils.isAnyBlank(encoding)){
			commonWriterMap.put("encoding",encoding);
		}else {
			commonWriterMap.put("encoding","UTF-8");
		}
		if (StringUtils.isNotBlank(mode)){
			commonWriterMap.put("mode",mode);
		}
		return commonWriterMap;
	}
	
	/********************************************************************hbase writer end *******************************************************************************/
	
}
