package com.application.base.admin.tool.query;

import com.application.base.admin.entity.JobJdbcDatasource;

import java.sql.SQLException;

/**
 * elasticsearch
 * @author admin
 * @version 2.0
 */
public class ElasticSearchQueryTool extends BaseQueryTool implements QueryToolInterface {

    /**
     * 构造方法
     * @param jobJdbcDatasource
     */
    public ElasticSearchQueryTool(JobJdbcDatasource jobJdbcDatasource) throws SQLException {
        super(jobJdbcDatasource);
    }

    @Override
    public Boolean dataSourceTest() {
        return true;
    }
}
