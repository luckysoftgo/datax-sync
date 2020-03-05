package com.application.base.code.jsonGen.content.core;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : admin
 * @NAME: HiveExecBuilder
 * @DESC: 创建 hive 的对象
 **/
public class HiveExecBuilder {
	
	/********************************************************************hive reader start *******************************************************************************/
	
	/**
	 * hive 的请求头配置
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
	public static Map<String,Object> builderHiveReader(String path, String defaultFS, String fileType, List<BasicColumn> column){
		Map<String,Object> commonReaderMap = new HashMap<>(16);
		commonReaderMap.put("path",path);
		commonReaderMap.put("defaultFS",defaultFS);
		commonReaderMap.put("column",column);
		if (StringUtils.isNotBlank(fileType)){
			commonReaderMap.put("fileType",fileType);
		}else{
			commonReaderMap.put("fileType","text");
		}
		commonReaderMap.put("fieldDelimiter",",");
		commonReaderMap.put("encoding","UTF-8");
		return commonReaderMap;
	}
	
	/**
	 * hive 的请求头配置
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
	 * @param kerberosKeytabFilePath
	 * @return
	 */
	public static Map<String,Object> builderHiveReader(String path, String defaultFS, String fileType, List<BasicColumn> column,String fieldDelimiter,
	                                                      String encoding,String nullFormat,Boolean haveKerberos,String kerberosKeytabFilePath){
		Map<String,Object> commonReaderMap = new HashMap<>(16);
		commonReaderMap.put("path",path);
		commonReaderMap.put("defaultFS",defaultFS);
		commonReaderMap.put("column",column);
		commonReaderMap.put("kerberosKeytabFilePath",kerberosKeytabFilePath);
		if (StringUtils.isNotBlank(fieldDelimiter)){
			commonReaderMap.put("fieldDelimiter",fieldDelimiter);
		}else{
			commonReaderMap.put("fieldDelimiter",",");
		}
		if (StringUtils.isNotBlank(fileType)){
			commonReaderMap.put("fileType",fileType);
		}else{
			commonReaderMap.put("fileType","text");
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
	
	/********************************************************************hive reader end *******************************************************************************/
	
	
	/********************************************************************hive writer start *******************************************************************************/
	
	/**
	 * 构建 hive 的writer
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
	public static Map<String,Object> builderHiveWriter(String defaultFS,String filtType,String path, List<BasicColumn> column,String compress,String fieldDelimiter,String writeMode,String fileName){
		Map<String,Object> commonWriterMap = new HashMap<>(16);
		if (StringUtils.isBlank(writeMode)){
			commonWriterMap.put("writeMode","append");
		}else{
			commonWriterMap.put("writeMode",writeMode);
		}
		commonWriterMap.put("defaultFS",defaultFS);
		if (StringUtils.isNotBlank(filtType)){
			commonWriterMap.put("fileType",filtType);
		}else{
			commonWriterMap.put("fileType","text");
		}
		commonWriterMap.put("path",path);
		commonWriterMap.put("column",column);
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
	
	
	/********************************************************************hive writer end *******************************************************************************/
	
}
