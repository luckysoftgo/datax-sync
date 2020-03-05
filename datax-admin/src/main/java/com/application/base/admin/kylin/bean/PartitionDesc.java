package com.application.base.admin.kylin.bean;

import org.apache.commons.lang3.StringUtils;

/**
 * @author : 孤狼
 * @NAME: PartitionDesc
 * @DESC: PartitionDesc类设计
 **/
public class PartitionDesc {
	private String partition_date_column;
	private String partition_time_column;
	private String partition_date_start;
	private String partition_date_format;
	private String partition_time_format;
	private String partition_type;
	private String dateColumn;
	private String timeColumn;
	
	public String getPartition_date_column() {
		return partition_date_column;
	}
	
	public void setPartition_date_column(String partition_date_column) {
		this.partition_date_column = partition_date_column;
		if (StringUtils.isNotBlank(partition_date_column)){
			String[] array=partition_date_column.split("\\.");
			this.setDateColumn(array[1]);
		}
	}
	
	public String getPartition_time_column() {
		return partition_time_column;
	}
	
	public void setPartition_time_column(String partition_time_column) {
		this.partition_time_column = partition_time_column;
		if (StringUtils.isNotBlank(partition_time_column)){
			String[] array=partition_time_column.split("\\.");
			this.setTimeColumn(array[1]);
		}
	}
	
	public String getPartition_date_start() {
		return partition_date_start;
	}
	
	public void setPartition_date_start(String partition_date_start) {
		this.partition_date_start = partition_date_start;
	}
	
	public String getPartition_date_format() {
		return partition_date_format;
	}
	
	public void setPartition_date_format(String partition_date_format) {
		this.partition_date_format = partition_date_format;
	}
	
	public String getPartition_time_format() {
		return partition_time_format;
	}
	
	public void setPartition_time_format(String partition_time_format) {
		this.partition_time_format = partition_time_format;
	}
	
	public String getPartition_type() {
		return partition_type;
	}
	
	public void setPartition_type(String partition_type) {
		this.partition_type = partition_type;
	}
	
	public String getDateColumn() {
		if (StringUtils.isNotBlank(partition_date_column)){
			String[] array=partition_date_column.split("\\.");
			this.setDateColumn(array[1]);
		}
		return dateColumn;
	}
	
	public void setDateColumn(String dateColumn) {
		this.dateColumn = dateColumn;
	}
	
	public String getTimeColumn() {
		if (StringUtils.isNotBlank(partition_time_column)){
			String[] array=partition_time_column.split("\\.");
			this.setTimeColumn(array[1]);
		}
		return timeColumn;
	}
	
	public void setTimeColumn(String timeColumn) {
		this.timeColumn = timeColumn;
	}
}
