package com.application.base.code.jsonGen.content;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : admin
 * @NAME: ContentJsonBuilder
 * @DESC: content 内容构建 builder
 **/
public class ContentJsonBuilder {
	
	/**
	 * 构建 content 中 reader 的信息
	 * @param name reader 的名字.
	 * @param parameter 读取的参数.
	 * @return
	 */
	public static Map<String,Object> builderReader(String name,Map<String,Object> parameter){
		Map<String,Object> readerMap = new HashMap<>(16);
		readerMap.put("name",name);
		readerMap.put("parameter",parameter);
		Map<String,Object> readerInfo = new HashMap<>(12);
		readerInfo.put("reader",readerMap);
		return readerInfo;
	}
	
	/**
	 * 构建 content 中 writer 的信息
	 * @param name writer 的名字.
	 * @param parameter 读取的参数.
	 * @return
	 */
	public static Map<String,Object> builderWeader(String name,Map<String,Object> parameter){
		Map<String,Object> writerMap = new HashMap<>(12);
		writerMap.put("name",name);
		writerMap.put("parameter",parameter);
		Map<String,Object> writerInfo = new HashMap<>(16);
		writerInfo.put("writer",writerMap);
		return writerInfo;
	}
}
