package com.application.base.code.jsonGen.cont;

/**
 * @author : admin
 * @NAME: DataXType
 * @DESC: datax 的类型配置
 **/
public enum  DataXType {
	
	/**
	 * mysql读取数据!
	 */
	MYSQLREADER("mysqlreader","mysql读取数据!"),
	/**
	 *mysql写入数据!
	 */
	MYSQLWRITER("mysqlwriter","mysql写入数据!"),
	/**
	 * mongodb读取数据!
	 */
	MONGODBREADER("mongodbreader","mongodb读取数据!"),
	/**
	 * mongodb写入数据!
	 */
	MONGODBWRITER("mongodbwriter","mongodb写入数据!"),
	/**
	 * hdfs 读取数据!
	 */
	HDFSREADER("hdfsreader","hdfs读取数据!"),
	/**
	 * hdfs 写入数据!
	 */
	HDFSWRITER("hdfswriter","hdfs写入数据!"),
	
	
	
	
	
	
	;
	
	private String value;
	private String desc;
	
	DataXType(String value,String desc){
		this.value = value;
		this.desc = desc;
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
}
