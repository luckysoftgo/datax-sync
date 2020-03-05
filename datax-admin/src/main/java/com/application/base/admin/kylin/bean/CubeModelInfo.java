package com.application.base.admin.kylin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : 孤狼
 * @NAME: CubeModelInfo
 * @DESC: CubeModelInfo类设计
 **/
@Data
@AllArgsConstructor
public class CubeModelInfo implements Serializable {

	private String name;
	private String fact_table;
	private String description;
	private List<String> metrics;
	private List<Dimensions> dimensions;
	private PartitionDesc partition_desc;
	
}
