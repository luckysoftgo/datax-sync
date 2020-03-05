package com.application.base.code.jsonGen.content.core;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : admin
 * @NAME: FtpExecBuilder
 * @DESC: 创建 ftp 的对象
 **/
public class FtpExecBuilder {
	
	/********************************************************************ftp reader start *******************************************************************************/
	
	/**
	 * 构建ftp请求的读信息
	 * @param protocol
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param column
	 * @param path
	 * @param encoding
	 * @param fieldDelimiter
	 * @return
	 */
	public static Map<String,Object> builderFtpReader(String protocol, String host, Integer port, String username, String password, List<Map<String,Object>> column, String path, String encoding, String fieldDelimiter){
		Map<String,Object> commonReaderMap = new HashMap<>(16);
		commonReaderMap.put("protocol",protocol);
		commonReaderMap.put("host",host);
		commonReaderMap.put("port",port);
		commonReaderMap.put("username",username);
		commonReaderMap.put("password",password);
		commonReaderMap.put("column",column);
		if (StringUtils.isNotBlank(encoding)){
			commonReaderMap.put("encoding",encoding);
		}else{
			commonReaderMap.put("encoding","UTF-8");
		}
		if (StringUtils.isNotBlank(path)){
			commonReaderMap.put("path",path);
		}
		if (StringUtils.isNotBlank(fieldDelimiter)){
			commonReaderMap.put("fieldDelimiter",fieldDelimiter);
		}else {
			commonReaderMap.put("fieldDelimiter",",");
		}
		return commonReaderMap;
	}
	
	/********************************************************************ftp reader end *******************************************************************************/
	
	
	/********************************************************************ftp writer start *******************************************************************************/
	
	
	/**
	 * 构建ftp请求的写信息
	 * @param protocol
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param timeout
	 * @param path
	 * @param fileName
	 * @param writeMode
	 * @param fieldDelimiter
	 * @param dateFormat
	 * @param fileFormat
	 * @return
	 */
	public static Map<String,Object> builderFtpWriter(String protocol, String host, Integer port, String username, String password, Integer timeout, String path, String fileName, String writeMode,String fieldDelimiter,String dateFormat,String fileFormat){
		Map<String,Object> commonWriterMap = new HashMap<>(16);
		commonWriterMap.put("protocol",protocol);
		commonWriterMap.put("host",host);
		commonWriterMap.put("port",port);
		commonWriterMap.put("username",username);
		commonWriterMap.put("password",password);
		commonWriterMap.put("path",path);
		commonWriterMap.put("timeout",timeout);
		commonWriterMap.put("fileName",fileName);
		if (StringUtils.isNotBlank(writeMode)){
			commonWriterMap.put("writeMode",writeMode);
		}else {
			commonWriterMap.put("writeMode","truncate|append|nonConflict");
		}
		if (StringUtils.isNotBlank(fieldDelimiter)){
			commonWriterMap.put("fieldDelimiter",fieldDelimiter);
		}else {
			commonWriterMap.put("fieldDelimiter",",");
		}
		if (StringUtils.isNotBlank(dateFormat)){
			commonWriterMap.put("dateFormat",dateFormat);
		}else{
			commonWriterMap.put("dateFormat","yyyy-MM-dd HH:mm:ss");
		}
		if (StringUtils.isNotBlank(fileFormat)){
			commonWriterMap.put("fileFormat",fileFormat);
		}else {
			commonWriterMap.put("fileFormat","csv");
		}
		return commonWriterMap;
	}
	
	/********************************************************************ftp writer end *******************************************************************************/
	
}
