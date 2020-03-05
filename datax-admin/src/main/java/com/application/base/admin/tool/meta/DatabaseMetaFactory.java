package com.application.base.admin.tool.meta;
import com.alibaba.druid.util.JdbcConstants;

/**
 * TODO
 *
 * @author admin
 * @ClassName DatabaseMetaFactory
 * @Version 1.0
 */
public class DatabaseMetaFactory {

    //根据数据库类型返回对应的接口
    public static DatabaseInterface getByDbType(String dbType) {
        if (JdbcConstants.MYSQL.equalsIgnoreCase(dbType)) {
            return MySQLDatabaseMeta.getInstance();
        } else if (JdbcConstants.ORACLE.equalsIgnoreCase(dbType)) {
            return OracleDatabaseMeta.getInstance();
        } else if (JdbcConstants.POSTGRESQL.equalsIgnoreCase(dbType)) {
            return PostgresqlDatabaseMeta.getInstance();
        } else if (JdbcConstants.SQL_SERVER.equalsIgnoreCase(dbType)) {
            return SqlServerDatabaseMeta.getInstance();
        } else if (JdbcConstants.HIVE.equalsIgnoreCase(dbType)) {
            return HiveDatabaseMeta.getInstance();
        } else if (JdbcConstants.KYLIN.equalsIgnoreCase(dbType)) {
            return KylinDatabaseMeta.getInstance();
        } else {
            throw new UnsupportedOperationException("暂不支持的类型：".concat(dbType));
        }
    }
}
