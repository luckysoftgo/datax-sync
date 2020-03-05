package com.application.base.admin.tool.query;
import com.application.base.admin.entity.JobJdbcDatasource;

import java.sql.*;
/**
 * kylin
 * @author admin
 * @version 2.0
 */
public class KylinQueryTool extends BaseQueryTool implements QueryToolInterface{

    public KylinQueryTool(JobJdbcDatasource jdbcDatasource) throws SQLException {
        super(jdbcDatasource);
    }

}
