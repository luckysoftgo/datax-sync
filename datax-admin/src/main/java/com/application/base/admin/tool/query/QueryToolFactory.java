package com.application.base.admin.tool.query;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.application.base.admin.entity.JobJdbcDatasource;
import com.application.base.admin.kylin.KylinConstant;
import com.application.base.admin.util.RdbmsException;

import java.sql.SQLException;

/**
 * 工具类，获取单例实体
 *
 * @author admin
 * @ClassName QueryToolFactory
 * @Version 1.0
 * @since 2019/7/18 9:36
 */
public class QueryToolFactory {

    public static final BaseQueryTool getByDbType(JobJdbcDatasource jobJdbcDatasource) {
        //获取dbType
        String dbType = JdbcUtils.getDbType(jobJdbcDatasource.getJdbcUrl(), jobJdbcDatasource.getJdbcDriverClass());
        if (dbType==null){
            dbType = jobJdbcDatasource.getDatasource().trim();
        }
        if (JdbcConstants.MYSQL.equalsIgnoreCase(dbType)) {
            return getMySQLQueryToolInstance(jobJdbcDatasource);
        } else if (JdbcConstants.ORACLE.equalsIgnoreCase(dbType)) {
            return getOracleQueryToolInstance(jobJdbcDatasource);
        } else if (JdbcConstants.POSTGRESQL.equalsIgnoreCase(dbType)) {
            return getPostgresqlQueryToolInstance(jobJdbcDatasource);
        } else if (JdbcConstants.SQL_SERVER.equalsIgnoreCase(dbType)) {
            return getSqlserverQueryToolInstance(jobJdbcDatasource);
        }else if (JdbcConstants.HIVE.equalsIgnoreCase(dbType)) {
            return getHiveQueryToolInstance(jobJdbcDatasource);
        }else if (JdbcConstants.KYLIN.equalsIgnoreCase(dbType)){
            return getKylinQueryToolInstance(jobJdbcDatasource);
        }else if (JdbcConstants.ELASTIC_SEARCH.equalsIgnoreCase(dbType) || KylinConstant.ELASTICSEARCH.equalsIgnoreCase(dbType)){
            return getElasticSearchQueryToolInstance(jobJdbcDatasource);
        }
        throw new UnsupportedOperationException("找不到该类型: ".concat(dbType));
    }

    private static BaseQueryTool getElasticSearchQueryToolInstance(JobJdbcDatasource jdbcDatasource) {
        try {
            return new ElasticSearchQueryTool(jdbcDatasource);
        } catch (Exception e) {
            throw RdbmsException.asConnException(JdbcConstants.ELASTIC_SEARCH,
                    e,jdbcDatasource.getJdbcUsername(),jdbcDatasource.getDatasourceName());
        }
    }

    private static BaseQueryTool getKylinQueryToolInstance(JobJdbcDatasource jdbcDatasource) {
        try {
            return new KylinQueryTool(jdbcDatasource);
        } catch (Exception e) {
            throw RdbmsException.asConnException(JdbcConstants.KYLIN,
                    e,jdbcDatasource.getJdbcUsername(),jdbcDatasource.getDatasourceName());
        }
    }

    private static BaseQueryTool getMySQLQueryToolInstance(JobJdbcDatasource jdbcDatasource) {
        try {
            return new MySQLQueryTool(jdbcDatasource);
        } catch (Exception e) {
            throw RdbmsException.asConnException(JdbcConstants.MYSQL,
                    e,jdbcDatasource.getJdbcUsername(),jdbcDatasource.getDatasourceName());
        }
    }

    private static BaseQueryTool getOracleQueryToolInstance(JobJdbcDatasource jdbcDatasource) {
        try {
            return new OracleQueryTool(jdbcDatasource);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(JdbcConstants.ORACLE,
                    e,jdbcDatasource.getJdbcUsername(),jdbcDatasource.getDatasourceName());
        }
    }

    private static BaseQueryTool getPostgresqlQueryToolInstance(JobJdbcDatasource jdbcDatasource) {
        try {
            return new PostgresqlQueryTool(jdbcDatasource);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(JdbcConstants.POSTGRESQL,
                    e,jdbcDatasource.getJdbcUsername(),jdbcDatasource.getDatasourceName());
        }
    }

    private static BaseQueryTool getSqlserverQueryToolInstance(JobJdbcDatasource jdbcDatasource) {
        try {
            return new SqlServerQueryTool(jdbcDatasource);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(JdbcConstants.SQL_SERVER,
                    e,jdbcDatasource.getJdbcUsername(),jdbcDatasource.getDatasourceName());
        }
    }

    private static BaseQueryTool getHiveQueryToolInstance(JobJdbcDatasource jdbcDatasource) {
        try {
            return new HiveQueryTool(jdbcDatasource);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(JdbcConstants.HIVE,
                    e,jdbcDatasource.getJdbcUsername(),jdbcDatasource.getDatasourceName());
        }
    }
}
