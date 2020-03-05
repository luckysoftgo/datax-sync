package com.application.base.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 构建json dto
 *
 * @author admin
 * @ClassName DataxJsonDto
 * @Version 2.0
 * @since 2020/01/11 17:15
 */
@Data
public class DataxJsonDto implements Serializable {

    private Long readerDatasourceId;

    private List<String> readerTables;

    private List<String> readerColumns;

    private Long writerDatasourceId;

    private List<String> writerTables;

    private List<String> writerColumns;

    private HiveReaderDto hiveReader;

    private HiveWriterDto hiveWriter;

    private RdbmsReaderDto rdbmsReader;

    private RdbmsWriterDto rdbmsWriter;

    private ElasticWriterDto elasticWriter;

}
