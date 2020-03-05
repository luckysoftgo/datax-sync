package com.application.base.code.jsonGen.setting;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : admin
 * @NAME: CommonJsonBuilder
 * @DESC: json 数据生成.
 **/
public class CommonJsonBuilder {
	
	/**
	 * 构建settings信息.
	 *
	 * @param speed
	 * @param errorLimit
	 * @return
	 */
	public static Map<String, Object> builderSetting(Map<String, Object> speed, Map<String, Object> errorLimit) {
		Map<String, Object> settingMap = new HashMap<>(16);
		if (speed!=null){
			settingMap.put("speed", speed);
		}
		if (errorLimit!=null){
			settingMap.put("errorLimit", errorLimit);
		}
		return settingMap;
	}
	
	/**
	 * 分为任务流控、脏数据限制设置，输入流，输出流三大块。
	 * 流量控制，控制作业速度，让作业可以在承受范围内达到最佳同步速度，共有三种模式：
	 *
	 * @param channel 为其定义管道数（并发）
	 * @param bytes   记录流速度，单位为byte/s，DataX会尽可能接近该值但是不会超过它。
	 * @param record  字节流
	 * @return
	 */
	public static Map<String, Object> builderSpeed(Integer channel, Integer bytes, Integer record) {
		Map<String, Object> speedMap = new HashMap<>(16);
		if (channel != null) {
			speedMap.put("channel", channel);
		}
		if (bytes != null) {
			speedMap.put("byte", bytes);
		}
		if (record != null) {
			speedMap.put("record", record);
		}
		return speedMap;
	}
	
	/**
	 * 脏数据限制
	 *
	 * @param record     脏数据的上限，如果一个表超过该值，那么就会导入失败并报错
	 * @param percentage 脏数据所占比例上限，1.0代表100%，当前2%
	 * @return
	 */
	public static Map<String, Object> builderErrorLimit(Integer record, BigDecimal percentage) {
		Map<String, Object> errorLimitMap = new HashMap<>(16);
		if (record != null) {
			errorLimitMap.put("record", record);
		}
		if (percentage != null) {
			errorLimitMap.put("percentage", percentage);
		}
		return errorLimitMap;
	}
	
}