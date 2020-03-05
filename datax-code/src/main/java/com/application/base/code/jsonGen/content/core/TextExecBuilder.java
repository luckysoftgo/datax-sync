package com.application.base.code.jsonGen.content.core;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : admin
 * @NAME: TextExecBuilder
 * @DESC: 创建 text 的对象
 **/
public class TextExecBuilder {
	
	/********************************************************************text reader start *******************************************************************************/
	
	/**
	 * 文件处理
	 * @param path
	 * @param encoding
	 * @param column
	 * @param fieldDelimiter
	 * @return
	 */
	public static Map<String,Object> builderTextReader(String path, String encoding, List<Map<String,Object>> column, String fieldDelimiter){
		Map<String,Object> commonReaderMap = new HashMap<>(16);
		commonReaderMap.put("path",path);
		if (column!=null && column.size()>0){
			commonReaderMap.put("column",column);
		}
		if (StringUtils.isNotBlank(encoding)){
			commonReaderMap.put("encoding",encoding);
		}else{
			commonReaderMap.put("encoding","UTF-8");
		}
		if (StringUtils.isNotBlank(fieldDelimiter)){
			commonReaderMap.put("fieldDelimiter",fieldDelimiter);
		}else {
			commonReaderMap.put("fieldDelimiter",",");
		}
		return commonReaderMap;
	}

	/********************************************************************text reader end *******************************************************************************/
	
	
	/********************************************************************text writer start *******************************************************************************/
	
	/**
	 * 文件读取
	 * @param path
	 * @param fileName
	 * @param writeMode
	 * @param dateFormat
	 * @return
	 */
	private static Map<String,Object> builderTextWriter(String path,String fileName, String writeMode, String dateFormat){
		Map<String,Object> commonWriterMap = new HashMap<>(16);
		commonWriterMap.put("path",path);
		commonWriterMap.put("fileName",fileName);
		if (StringUtils.isNotBlank(writeMode)){
			commonWriterMap.put("writeMode",writeMode);
		}else{
			commonWriterMap.put("writeMode","truncate");
		}
		if (StringUtils.isNotBlank(dateFormat)){
			commonWriterMap.put("dateFormat",dateFormat);
		}else{
			commonWriterMap.put("dateFormat","yyyy-MM-dd HH:mm:ss");
		}
		return commonWriterMap;
	}
	
	/********************************************************************text writer end *******************************************************************************/
	
}
