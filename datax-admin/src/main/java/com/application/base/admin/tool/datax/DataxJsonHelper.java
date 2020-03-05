package com.application.base.admin.tool.datax;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.application.base.admin.dto.*;
import com.application.base.admin.entity.JobJdbcDatasource;
import com.application.base.admin.kylin.KylinConstant;
import com.application.base.admin.tool.datax.reader.*;
import com.application.base.admin.tool.datax.writer.*;
import com.application.base.admin.tool.pojo.DataxElasticPojo;
import com.application.base.admin.tool.pojo.DataxHivePojo;
import com.application.base.admin.tool.pojo.DataxRdbmsPojo;
import com.application.base.admin.util.Constant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 构建 com.westcredit.datax json的工具类
 *
 * @author admin
 * @ClassName DataxJsonHelper
 * @Version 1.0
 */
@Data
public class DataxJsonHelper implements DataxJsonInterface {
	
	/**
	 * 读取的表，根据datax示例，支持多个表（先不考虑，后面再去实现， 这里先用list保存吧）
	 * <p>
	 * 目的表的表名称。支持写入一个或者多个表。当配置为多张表时，必须确保所有表结构保持一致
	 */
	private List<String> readerTables;
	/**
	 * 读取的字段列表
	 */
	private List<String> readerColumns;
	/**
	 * reader jdbc 数据源
	 */
	private JobJdbcDatasource readerDatasource;
	/**
	 * writer jdbc 数据源
	 */
	private JobJdbcDatasource writerDatasource;
	/**
	 * 写入的表
	 */
	private List<String> writerTables;
	/**
	 * 写入的字段列表
	 */
	private List<String> writerColumns;
	
	private Map<String, Object> buildReader;
	
	private Map<String, Object> buildWriter;
	
	private BaseDataxPlugin readerPlugin;
	
	private BaseDataxPlugin writerPlugin;
	
	private HiveReaderDto hiveReaderDto;
	
	private HiveWriterDto hiveWriterDto;
	
	private RdbmsReaderDto rdbmsReaderDto;
	
	private RdbmsWriterDto rdbmsWriterDto;

	private ElasticWriterDto elasticWriterDto;
	
	//用于保存额外参数
	private Map<String, Object> extraParams = Maps.newHashMap();
	
	public void initReader(DataxJsonDto dataxJsonDto, JobJdbcDatasource readerDatasource) {
		this.readerDatasource = readerDatasource;
		this.readerTables = dataxJsonDto.getReaderTables();
		this.readerColumns = dataxJsonDto.getReaderColumns();
		this.hiveReaderDto = dataxJsonDto.getHiveReader();
		this.rdbmsReaderDto = dataxJsonDto.getRdbmsReader();
		// reader 插件
		String readerDbType = JdbcUtils.getDbType(readerDatasource.getJdbcUrl(), readerDatasource.getJdbcDriverClass());
		if (StringUtils.isBlank(readerDbType)){
			readerDbType = readerDatasource.getDatasource();
		}
		if (JdbcConstants.MYSQL.equalsIgnoreCase(readerDbType)) {
			readerPlugin = new MysqlReader();
		} else if (JdbcConstants.ORACLE.equalsIgnoreCase(readerDbType)) {
			readerPlugin = new OracleReader();
		} else if (JdbcConstants.SQL_SERVER.equalsIgnoreCase(readerDbType)) {
			readerPlugin = new SqlServerReader();
		} else if (JdbcConstants.POSTGRESQL.equalsIgnoreCase(readerDbType)) {
			readerPlugin = new PostgresqlReader();
		} else if (JdbcConstants.HIVE.equalsIgnoreCase(readerDbType)) {
			readerPlugin = new HiveReader();
		}else if(JdbcConstants.KYLIN.equalsIgnoreCase(readerDbType)){
			readerPlugin = new KylinReader();
		}
		if(JdbcConstants.HIVE.equalsIgnoreCase(readerDbType)){
			buildReader = buildHiveReader();
		}else {
			buildReader = buildReader();
		}
	}

	@Override
	public Map<String, Object> buildReader() {
		DataxRdbmsPojo dataxPluginPojo = new DataxRdbmsPojo();
		dataxPluginPojo.setJdbcDatasource(readerDatasource);
		dataxPluginPojo.setTables(readerTables);
		dataxPluginPojo.setRdbmsColumns(readerColumns);
		dataxPluginPojo.setSplitPk(rdbmsReaderDto.getReaderSplitPk());
		if (StringUtils.isNotBlank(rdbmsReaderDto.getQuerySql())) {
			dataxPluginPojo.setQuerySql(rdbmsReaderDto.getQuerySql());
		}
		//where
		if (StringUtils.isNotBlank(rdbmsReaderDto.getWhereParams())) {
			dataxPluginPojo.setWhereParam(rdbmsReaderDto.getWhereParams());
		}
		return readerPlugin.build(dataxPluginPojo);
	}

	@Override
	public Map<String, Object> buildHiveReader() {
		DataxHivePojo dataxHivePojo = new DataxHivePojo();
		dataxHivePojo.setJdbcDatasource(readerDatasource);
		List<Map<String, Object>> columns = Lists.newArrayList();
		//20200119
		readerColumns.forEach(c -> {
			String[] array = c.split(Constant.SPLIT_SCOLON);
			Map<String, Object> column = Maps.newLinkedHashMap();
			column.put("index", array[0]);
			column.put("name", array[1]);
			column.put("type", convertLong(array[2]));
			columns.add(column);
		});
		dataxHivePojo.setColumns(columns);
		dataxHivePojo.setReaderDefaultFS(hiveReaderDto.getReaderDefaultFS());
		dataxHivePojo.setReaderFieldDelimiter(hiveReaderDto.getReaderFieldDelimiter());
		dataxHivePojo.setReaderFileType(hiveReaderDto.getReaderFileType());
		dataxHivePojo.setReaderPath(hiveReaderDto.getReaderPath());
		return readerPlugin.buildHive(dataxHivePojo);
	}

	public void initWriter(DataxJsonDto dataxJsonDto, JobJdbcDatasource writerDatasource) {
		this.writerDatasource = writerDatasource;
		this.writerTables = dataxJsonDto.getWriterTables();
		this.writerColumns = dataxJsonDto.getWriterColumns();
		this.hiveWriterDto = dataxJsonDto.getHiveWriter();
		this.rdbmsWriterDto = dataxJsonDto.getRdbmsWriter();
		this.elasticWriterDto = dataxJsonDto.getElasticWriter();
		// writer
		String writerDbType = JdbcUtils.getDbType(writerDatasource.getJdbcUrl(), writerDatasource.getJdbcDriverClass());
		if (StringUtils.isBlank(writerDbType)){
			writerDbType = writerDatasource.getDatasource();
		}
		if (JdbcConstants.MYSQL.equalsIgnoreCase(writerDbType)) {
			writerPlugin = new MysqlWriter();
		} else if (JdbcConstants.ORACLE.equalsIgnoreCase(writerDbType)) {
			writerPlugin = new OraclelWriter();
		} else if (JdbcConstants.SQL_SERVER.equalsIgnoreCase(writerDbType)) {
			writerPlugin = new SqlServerlWriter();
		} else if (JdbcConstants.POSTGRESQL.equalsIgnoreCase(writerDbType)) {
			writerPlugin = new PostgresqllWriter();
		} else if (JdbcConstants.HIVE.equalsIgnoreCase(writerDbType)) {
			writerPlugin = new HiveWriter();
		} else if (JdbcConstants.KYLIN.equalsIgnoreCase(writerDbType)){
			writerPlugin = new KylinWriter();
		} else if (JdbcConstants.ELASTIC_SEARCH.equalsIgnoreCase(writerDbType)||
				KylinConstant.ELASTICSEARCH.equalsIgnoreCase(writerDbType)){
			writerPlugin = new ElasticWriter();
		}
		if(JdbcConstants.HIVE.equalsIgnoreCase(writerDbType) || JdbcConstants.KYLIN.equalsIgnoreCase(writerDbType)){
			buildWriter = this.buildHiveWriter();
		}else if (JdbcConstants.ELASTIC_SEARCH.equalsIgnoreCase(writerDbType)||
				KylinConstant.ELASTICSEARCH.equalsIgnoreCase(writerDbType)){
			buildWriter = this.buildElasticWriter();
		}else{
			buildWriter = this.buildWriter();
		}
	}

	@Override
	public Map<String, Object> buildWriter() {
		DataxRdbmsPojo dataxPluginPojo = new DataxRdbmsPojo();
		dataxPluginPojo.setJdbcDatasource(writerDatasource);
		dataxPluginPojo.setTables(writerTables);
		dataxPluginPojo.setRdbmsColumns(writerColumns);
		dataxPluginPojo.setPreSql(rdbmsWriterDto.getPreSql());
		return writerPlugin.build(dataxPluginPojo);
	}

	@Override
	public Map<String, Object> buildHiveWriter() {
		DataxHivePojo dataxHivePojo = new DataxHivePojo();
		dataxHivePojo.setJdbcDatasource(writerDatasource);
		List<Map<String, Object>> columns = Lists.newArrayList();
		writerColumns.forEach(c -> {
			Map<String, Object> column = Maps.newLinkedHashMap();
			//20200119
			String[] array = c.split(Constant.SPLIT_SCOLON);
			column.put("index", array[0]);
			column.put("name", array[1]);
			column.put("type", convertInt(array[2]));
			columns.add(column);
		});
		dataxHivePojo.setColumns(columns);
		dataxHivePojo.setWriterDefaultFS(hiveWriterDto.getWriterDefaultFS());
		dataxHivePojo.setWriteFieldDelimiter(hiveWriterDto.getWriteFieldDelimiter());
		dataxHivePojo.setWriterFileType(hiveWriterDto.getWriterFileType());
		dataxHivePojo.setWriterPath(hiveWriterDto.getWriterPath());
		dataxHivePojo.setWriteMode(hiveWriterDto.getWriteMode());
		dataxHivePojo.setWriterFileName(hiveWriterDto.getWriterFileName());
		return writerPlugin.buildHive(dataxHivePojo);
	}

	private Map<String,Object> buildElasticWriter() {
		DataxElasticPojo elasticPojo = new DataxElasticPojo();
		elasticPojo.setJdbcDatasource(writerDatasource);
		List<Map<String, Object>> columns = Lists.newArrayList();
		writerColumns.forEach(c -> {
			Map<String, Object> column = Maps.newLinkedHashMap();
			String[] array = c.split(Constant.SPLIT_SCOLON);
			//column.put("index", array[0]);
			column.put("name", array[1]);
			column.put("type", convertLong(array[2]));
			columns.add(column);
		});
		elasticPojo.setColumns(columns);
		elasticPojo.setEndpoint(elasticWriterDto.getEndpoint());
		elasticPojo.setAccessId(elasticWriterDto.getAccessId());
		elasticPojo.setAccessKey(elasticWriterDto.getAccessKey());
		elasticPojo.setCleanup(elasticWriterDto.isCleanup());
		elasticPojo.setDiscovery(elasticWriterDto.isDiscovery());
		elasticPojo.setIndex(elasticWriterDto.getIndex());
		elasticPojo.setType(elasticWriterDto.getType());
		elasticPojo.setSplitter(elasticWriterDto.getSplitter());
		elasticPojo.setBatchSize(elasticWriterDto.getBatchSize());
		return writerPlugin.buildElastic(elasticPojo);
	}

	@Override
	public Map<String, Object> buildJob() {
		Map<String, Object> res = Maps.newLinkedHashMap();
		Map<String, Object> jobMap = Maps.newLinkedHashMap();
		jobMap.put("setting", buildSetting());
		jobMap.put("content", ImmutableList.of(buildContent()));
		res.put("job", jobMap);
		return res;
	}
	
	@Override
	public Map<String, Object> buildSetting() {
		Map<String, Object> res = Maps.newLinkedHashMap();
		Map<String, Object> speedMap = Maps.newLinkedHashMap();
		Map<String, Object> errorLimitMap = Maps.newLinkedHashMap();
		speedMap.putAll(ImmutableMap.of("channel", Runtime.getRuntime().availableProcessors()));
		errorLimitMap.putAll(ImmutableMap.of("record", 0, "percentage", 0.001));
		res.put("speed", speedMap);
		res.put("errorLimit", errorLimitMap);
		return res;
	}
	
	@Override
	public Map<String, Object> buildContent() {
		Map<String, Object> res = Maps.newLinkedHashMap();
		res.put("reader", this.buildReader);
		res.put("writer", this.buildWriter);
		return res;
	}
	

	/**
	 * 类型转换.
	 * @param input
	 * @return
	 */
	private String convertLong(String input){
		if ("int".equalsIgnoreCase(input) || "bigint".equalsIgnoreCase(input) || "tinyint".equalsIgnoreCase(input) || "smallint".equalsIgnoreCase(input)){
			return "long";
		}
		return input;
	}
	
	/**
	 * 类型转换.
	 * @param input
	 * @return
	 */
	private String convertInt(String input){
		if ("int".equalsIgnoreCase(input) || "bigint".equalsIgnoreCase(input) || "tinyint".equalsIgnoreCase(input) || "smallint".equalsIgnoreCase(input)|| "long".equalsIgnoreCase(input)){
			return "int";
		}
		return input;
	}
}
