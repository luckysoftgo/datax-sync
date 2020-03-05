package com.application.base.code.altas.bean;

import java.util.List;

/**
 * @author : admin
 * @NAME: TableAltasColumn
 * @DESC: TableColumnInfo类设计
 **/
public class DbRdbmsInfo {
	/**
	 * dbUrl
	 */
	private String dbUrl;
	/**
	 * 表名称.
	 */
	private String dbName;
	
	/**
	 * 表的信息.
	 */
	List<TableInfo> tableInfos;
	
	public String getDbUrl() {
		return dbUrl;
	}
	
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	
	public String getDbName() {
		return dbName;
	}
	
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	public List<TableInfo> getTableInfos() {
		return tableInfos;
	}
	
	public void setTableInfos(List<TableInfo> tableInfos) {
		this.tableInfos = tableInfos;
	}
	
	/**
	 * 生成对象
	 * @return
	 */
	public DbRdbmsInfo.TableInfo createTable(){
		return new DbRdbmsInfo.TableInfo();
	}
	
	/**
	 * 主要信息.
	 */
	public class TableInfo {
		private String tableName;
		private String tableDesc;
		private String qualifiedName;
		private String dataGuid;

		public String getTableName() {
			return tableName;
		}
		
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		
		public String getTableDesc() {
			return tableDesc;
		}
		
		public void setTableDesc(String tableDesc) {
			this.tableDesc = tableDesc;
		}
		
		public String getQualifiedName() {
			return qualifiedName;
		}
		
		public void setQualifiedName(String qualifiedName) {
			this.qualifiedName = qualifiedName;
		}
		
		public String getDataGuid() {
			return dataGuid;
		}
		
		public void setDataGuid(String dataGuid) {
			this.dataGuid = dataGuid;
		}
	}
}
