package com.application.base.code.jsonGen.content.core;

/**
 * @author : admin
 * @NAME: BasicColumn
 * @DESC: 对象的属性描述.
 **/
public class BasicColumn {
	/**
	 * 索引
	 */
	private Integer index;
	/**
	 * 列名称
	 */
	private String name;
	/**
	 * 列类型
	 */
	private String type;
	
	public BasicColumn() {
	}
	
	public BasicColumn(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public BasicColumn(Integer index, String name, String type) {
		this.index = index;
		this.name = name;
		this.type = type;
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

}
