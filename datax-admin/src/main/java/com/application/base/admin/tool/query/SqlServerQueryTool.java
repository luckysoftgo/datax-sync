package com.application.base.admin.tool.query;

import com.application.base.admin.entity.JobJdbcDatasource;

import java.sql.SQLException;

/**
 * sql server
 *
 * @author admin
 * @version 1.0
 * @since 2019/8/2
 */
public class SqlServerQueryTool extends BaseQueryTool implements QueryToolInterface {
    public SqlServerQueryTool(JobJdbcDatasource jobJdbcDatasource) throws SQLException {
        super(jobJdbcDatasource);
    }
}
