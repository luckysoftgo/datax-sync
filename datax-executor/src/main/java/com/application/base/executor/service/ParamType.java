package com.application.base.executor.service;

/**
 * @author admin
 */
public enum ParamType {
	
	JVM(1,"jvm","设置虚拟机参数"),
	RDBS(2,"rdbs","设置连接库,连接表信息"),
	SQL(3,"sql","sql语句相关配置"),
	
	;
	
	private Integer type;
	private String value;
	private String desc;
	
	
	ParamType(Integer type,String value,String desc){
		this.desc = desc;
		this.value = value;
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	/**
	 * 获得类型
	 * @return
	 */
	public static String getValue(Integer type){
		ParamType[] paramTypes = ParamType.values();
		for (ParamType paramType : paramTypes) {
			if (type.intValue()==paramType.getType()){
				return paramType.getValue();
			}
		}
		return null;
	}
	
}
