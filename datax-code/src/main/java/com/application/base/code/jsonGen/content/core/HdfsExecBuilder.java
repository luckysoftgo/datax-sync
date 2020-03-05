package com.application.base.code.jsonGen.content.core;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : admin
 * @NAME: HdfsExecBuilder
 * @DESC: 创建 hdfs 的对象
 **/
public class HdfsExecBuilder {
	
	
	/********************************************************************hdfs reader start *******************************************************************************/
	
	/**
	 * hdfs 的请求头配置
	 * path（必选、默认无）
	 * defaultFS（必选，默认无）
	 * fileType（必选，默认无） ”text”、”orc”、”rc”、”seq”、”csv”。
	 * column（必选，默认无）
	 * fieldDelimiter（非必选，默认逗号）
	 * encoding（非必选，默认UTF-8）
	 * nullFormat（非必选，默认无）
	 * haveKerberos（非必选，默认false）
	 * kerberosKeytabFilePath（非必选，默认
	 * kerberosPrincipal（非必选，默认无）
	 * compress（非必选，默认无）
	 * hadoopConfig（非必选，默认无）
	 * csvReaderConfig（非必须，默认无）
	 * @param path
	 * @param defaultFS
	 * @param fileType
	 * @param column
	 * @return
	 */
	private static Map<String,Object> builderHdfsReader(String path, String defaultFS, String fileType, String[] column){
		Map<String,Object> commonReaderMap = new HashMap<>(16);
		commonReaderMap.put("path",path);
		commonReaderMap.put("defaultFS",defaultFS);
		if (StringUtils.isAnyBlank(column)){
			commonReaderMap.put("column","*");
		}else {
			commonReaderMap.put("column",column);
		}
		if (StringUtils.isNotBlank(fileType)){
			commonReaderMap.put("fileType",fileType);
		}
		commonReaderMap.put("fieldDelimiter",",");
		commonReaderMap.put("encoding","UTF-8");
		return commonReaderMap;
	}
	
	/**
	 * hdfs 的请求头配置
	 * path（必选、默认无）
	 * defaultFS（必选，默认无）
	 * fileType（必选，默认无） 	 * fileType（必选，默认无） ”text”、”orc”、”rc”、”seq”、”csv”。
	 * column（必选，默认无）
	 * fieldDelimiter（非必选，默认逗号）
	 * encoding（非必选，默认UTF-8）
	 * nullFormat（非必选，默认无）
	 * haveKerberos（非必选，默认false）
	 * kerberosKeytabFilePath（非必选，默认
	 * kerberosPrincipal（非必选，默认无）
	 * compress（非必选，默认无）
	 * hadoopConfig（非必选，默认无）
	 * csvReaderConfig（非必须，默认无）
	 * @param path
	 * @param defaultFS
	 * @param fileType
	 * @param column
	 * @param fieldDelimiter
	 * @param encoding
	 * @param nullFormat
	 * @param haveKerberos
	 * @return
	 */
	private static Map<String,Object> builderHdfsReader(String path, String defaultFS, String fileType, String[] column,String fieldDelimiter,
	                                                    String encoding,String nullFormat,Boolean haveKerberos){
		Map<String,Object> commonReaderMap = new HashMap<>(16);
		commonReaderMap.put("path",path);
		commonReaderMap.put("defaultFS",defaultFS);
		if (StringUtils.isAnyBlank(column)){
			commonReaderMap.put("column","*");
		}else {
			commonReaderMap.put("column",column);
		}
		if (StringUtils.isNotBlank(fileType)){
			commonReaderMap.put("fileType",fileType);
		}
		if (StringUtils.isNotBlank(fieldDelimiter)){
			commonReaderMap.put("fieldDelimiter",fieldDelimiter);
		}else{
			commonReaderMap.put("fieldDelimiter",",");
		}
		if (StringUtils.isNotBlank(encoding)){
			commonReaderMap.put("encoding",encoding);
		}else{
			commonReaderMap.put("encoding","UTF-8");
		}
		if (StringUtils.isNotBlank(nullFormat)){
			commonReaderMap.put("nullFormat",nullFormat);
		}
		if (haveKerberos!=null){
			commonReaderMap.put("haveKerberos",haveKerberos);
		}else{
			commonReaderMap.put("haveKerberos",false);
		}
		return commonReaderMap;
	}
	
	/********************************************************************hdfs reader end *******************************************************************************/
	
	
	/********************************************************************hdfs writer start *******************************************************************************/
	
	/**
	 * 构建 hdfs 的writer
	 * * defaultFS（必选，默认无）
	 * * filtType（必选，默认无） 	 * fileType（必选，默认无） ”text”、”orc”、”rc”、”seq”、”csv”。
	 * * path（必选，默认无）
	 * * column（必选，默认无）
	 * * hadoopConfig（非必选，默认无）
	 * * kerberos相关配置
	 * @param defaultFS
	 * @param filtType
	 * @param path
	 * @param column
	 * @param compress
	 * @param fieldDelimiter
	 * @param writeMode
	 * @param fileName
	 * @return
	 */
	private static Map<String,Object> builderHdfsWriter(String defaultFS,String filtType,String path, String[] column,String compress,String fieldDelimiter,String writeMode,String fileName){
		Map<String,Object> commonWriterMap = new HashMap<>(16);
		if (StringUtils.isBlank(writeMode)){
			commonWriterMap.put("writeMode","append");
		}else{
			commonWriterMap.put("writeMode",writeMode);
		}
		commonWriterMap.put("defaultFS",defaultFS);
		commonWriterMap.put("filtType",filtType);
		commonWriterMap.put("path",path);
		if (StringUtils.isAnyBlank(column)){
			commonWriterMap.put("column","*");
		}else {
			commonWriterMap.put("column",column);
		}
		if (StringUtils.isNotBlank(compress)){
			commonWriterMap.put("compress",compress);
		}
		if (StringUtils.isNotBlank(fieldDelimiter)){
			commonWriterMap.put("fieldDelimiter",fieldDelimiter);
		}else{
			commonWriterMap.put("fieldDelimiter",",");
		}
		if (StringUtils.isNotBlank(fileName)){
			commonWriterMap.put("fileName",fileName);
		}
		return commonWriterMap;
	}
	
	
	/********************************************************************hdfs writer end *******************************************************************************/
	
}
