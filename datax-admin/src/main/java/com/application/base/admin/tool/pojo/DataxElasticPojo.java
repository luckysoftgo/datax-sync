package com.application.base.admin.tool.pojo;

import com.application.base.admin.entity.JobJdbcDatasource;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 用于传参，构建json
 *
 * @author admin
 * @ClassName DataxElasticPojo
 */
@Data
public class DataxElasticPojo {

    /**
     * Elastic 列名
     */
    private List<Map<String,Object>> columns;
    /**
     * 数据源信息
     */
    private JobJdbcDatasource jdbcDatasource;

    private String endpoint;
    private String accessId;
    private String accessKey;
    private String index;
    private String type="default";
    private boolean cleanup = true;
    private boolean discovery = false;
    private Integer batchSize=1000;
    private String splitter=",";
}
