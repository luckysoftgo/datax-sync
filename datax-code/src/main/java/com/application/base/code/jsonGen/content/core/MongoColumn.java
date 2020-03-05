package com.application.base.code.jsonGen.content.core;

/**
 * @author : admin
 * @NAME: MongoColumn
 * @DESC: 列信息
 **/
public class MongoColumn extends BasicColumn{
	
	private String spliter;
	
	public MongoColumn() {
	}
	
	public MongoColumn(String name, String type) {
		super(name,type);
	}
	
	public MongoColumn(String name, String type, String spliter) {
		super(name,type);
		this.spliter = spliter;
	}
	
	public String getSpliter() {
		return spliter;
	}
	
	public void setSpliter(String spliter) {
		this.spliter = spliter;
	}
}
