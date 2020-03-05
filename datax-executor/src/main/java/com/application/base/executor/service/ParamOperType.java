package com.application.base.executor.service;

/**
 * @author admin
 */
public enum ParamOperType {
	
	GREATER(1,">","大于"),
	EQUAL(2,"=","等于"),
	GREATEROREQUAL(3,">=","大于等于"),
	LESS(4,"<","小于"),
	LESSOREQUAL(5,"<=","小于等于"),
	NOTEQUAL(6,"!=","不等于"),
	BETWEENAND(7,"AND","在什么之间"),
	;
	
	private Integer type;
	private String value;
	private String desc;
	
	ParamOperType(Integer type,String value, String desc){
		this.type =type;
		this.desc = desc;
		this.value = value;
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
	 * between and 设置.
	 * @return
	 */
	public static String getBetweenAnd(String value){
		ParamOperType[] operTypes = ParamOperType.values();
		for (ParamOperType param : operTypes) {
			if (param.type.intValue()==ParamOperType.BETWEENAND.type){
				return String.format("between %s and %s ",value.split(";"));
			}
		}
		return null;
	}
}
