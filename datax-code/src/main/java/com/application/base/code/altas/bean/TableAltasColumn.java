package com.application.base.code.altas.bean;

import java.util.List;

/**
 * @author : admin
 * @NAME: TableAltasColumn
 * @DESC: TableColumnInfo类设计
 **/
public class TableAltasColumn {
	/**
	 * sdl-guid
	 */
	private String sdGuid;
	/**
	 * 列信息.
	 */
	private List<ColumnInfo> columns;
	/**
	 * 表名称.
	 */
	private String tableName;
	/**
	 * 表的描述
	 */
	private String comment;
	/**
	 * hdfsLocal 的文件地址.
	 */
	private String hdfsLocal;
	
	public String getSdGuid() {
		return sdGuid;
	}
	
	public void setSdGuid(String sdGuid) {
		this.sdGuid = sdGuid;
	}
	
	public List<ColumnInfo> getColumns() {
		return columns;
	}
	
	public void setColumns(List<ColumnInfo> columns) {
		this.columns = columns;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getHdfsLocal() {
		return hdfsLocal;
	}
	
	public void setHdfsLocal(String hdfsLocal) {
		this.hdfsLocal = hdfsLocal;
	}
	
	/**
	 * 生成对象
	 * @return
	 */
	public ColumnInfo createColumn(){
		return new ColumnInfo();
	}
	
	/**
	 * 主要信息.
	 */
	public class ColumnInfo {
		private String qualifiedName;
		private String name;
		private String comment;
		private String type;
		private String guid;
		
		public String getQualifiedName() {
			return qualifiedName;
		}
		
		public void setQualifiedName(String qualifiedName) {
			this.qualifiedName = qualifiedName;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getComment() {
			return comment;
		}
		
		public void setComment(String comment) {
			this.comment = comment;
		}
		
		public String getType() {
			return type;
		}
		
		public void setType(String type) {
			this.type = type;
		}
		
		public String getGuid() {
			return guid;
		}
		
		public void setGuid(String guid) {
			this.guid = guid;
		}
	}
}
