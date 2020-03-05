package com.application.base.admin.tool.query;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.application.base.core.util.Constant;
import com.application.base.admin.core.util.LocalCacheUtil;
import com.application.base.admin.entity.JobJdbcDatasource;
import com.application.base.admin.tool.database.ColumnInfo;
import com.application.base.admin.tool.database.DasColumn;
import com.application.base.admin.tool.database.TableInfo;
import com.application.base.admin.tool.meta.DatabaseInterface;
import com.application.base.admin.tool.meta.DatabaseMetaFactory;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * 抽象查询工具
 *
 * @author admin
 * @ClassName BaseQueryTool
 * @Version 1.0
 * @since 2019/7/18 9:22
 */
public abstract class BaseQueryTool implements QueryToolInterface {

    protected static final Logger logger = LoggerFactory.getLogger(BaseQueryTool.class);
    /**
     * 用于获取查询语句
     */
    public DatabaseInterface sqlBuilder;

    public DataSource datasource;

    public Connection connection;
    /**
     * 当前数据库名.
     */
    public String currentSchema;
    /**
     * 列的信息.
     */
    public String columns;
    /**
     * 构造方法
     *
     * @param jobJdbcDatasource
     */
    BaseQueryTool(JobJdbcDatasource jobJdbcDatasource) throws SQLException {
       this(jobJdbcDatasource,null);
    }

    /**
     * 构造方法
     *
     * @param jobJdbcDatasource
     */
    BaseQueryTool(JobJdbcDatasource jobJdbcDatasource,String columns) throws SQLException {
        String currentDbType = JdbcUtils.getDbType(jobJdbcDatasource.getJdbcUrl(), jobJdbcDatasource.getJdbcDriverClass());
        if (currentDbType==null){
            currentDbType = jobJdbcDatasource.getDatasource();
        }
        if (!currentDbType.equalsIgnoreCase(JdbcUtils.KYLIN) && LocalCacheUtil.get(jobJdbcDatasource.getDatasourceName()) == null) {
            //这里默认使用 hikari 数据源
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setUsername(jobJdbcDatasource.getJdbcUsername());
            dataSource.setPassword(jobJdbcDatasource.getJdbcPassword());
            dataSource.setJdbcUrl(jobJdbcDatasource.getJdbcUrl());
            dataSource.setDriverClassName(jobJdbcDatasource.getJdbcDriverClass());
            dataSource.setMaximumPoolSize(1);
            dataSource.setMinimumIdle(0);
            dataSource.setConnectionTimeout(30000);
            this.datasource = dataSource;
            this.connection = this.datasource.getConnection();
        }else if(currentDbType.equalsIgnoreCase(JdbcUtils.KYLIN)){
            try {
                Driver driver = (Driver) Class.forName(jobJdbcDatasource.getJdbcDriverClass()).newInstance();
                Properties info = new Properties();
                info.put("user", jobJdbcDatasource.getJdbcUsername());
                info.put("password",jobJdbcDatasource.getJdbcPassword());
                this.connection = driver.connect(jobJdbcDatasource.getJdbcUrl(),info);
            }catch (Exception e){
                logger.error("获取kylin数据源失败了,失败信息是：{}",e.getMessage());
            }
        }else {
            this.connection = (Connection) LocalCacheUtil.get(jobJdbcDatasource.getDatasourceName());
        }
        this.columns = columns;
        sqlBuilder = DatabaseMetaFactory.getByDbType(currentDbType);
        currentSchema = getSchema(jobJdbcDatasource.getJdbcUsername());
        LocalCacheUtil.set(jobJdbcDatasource.getDatasourceName(), this.connection, 4 * 60 * 60 * 1000);
    }

    //根据connection获取schema
    private String getSchema(String jdbcUsername) {
        String res = null;
        try {
            res = connection.getCatalog();
        } catch (SQLException e) {
            try {
                res = connection.getSchema();
            } catch (SQLException e1) {
                logger.error("[SQLException getSchema Exception] --> "
                        + "the exception message is:" + e1.getMessage());
            }
            logger.error("[getSchema Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        // 如果res是null，则将用户名当作 schema
        if (StrUtil.isBlank(res)) {
            res = jdbcUsername.toUpperCase();
        }
        return res;
    }

    @Override
    public TableInfo buildTableInfo(String tableName) {
        //获取表信息
        List<Map<String, Object>> tableInfos = this.getTableInfo(tableName);
        if (tableInfos.isEmpty()) {
            throw new NullPointerException("查询出错! ");
        }

        TableInfo tableInfo = new TableInfo();
        //表名，注释
        List tValues = new ArrayList(tableInfos.get(0).values());

        tableInfo.setName(StrUtil.toString(tValues.get(0)));
        tableInfo.setComment(StrUtil.toString(tValues.get(1)));


        //获取所有字段
        List<ColumnInfo> fullColumn = getColumns(tableName);
        tableInfo.setColumns(fullColumn);

        //获取主键列
        List<String> primaryKeys = getPrimaryKeys(tableName);
        logger.info("主键列为：{}", primaryKeys);

        //设置ifPrimaryKey标志
        fullColumn.forEach(e -> {
            if (primaryKeys.contains(e.getName())) {
                e.setIfPrimaryKey(true);
            } else {
                e.setIfPrimaryKey(false);
            }
        });
        return tableInfo;
    }

    //无论怎么查，返回结果都应该只有表名和表注释，遍历map拿value值即可
    @Override
    public List<Map<String, Object>> getTableInfo(String tableName) {
        String sqlQueryTableNameComment = sqlBuilder.getSQLQueryTableNameComment();
        logger.info(sqlQueryTableNameComment);
        List<Map<String, Object>> res = null;
        try {
            res = JdbcUtils.executeQuery(connection, sqlQueryTableNameComment, ImmutableList.of(currentSchema, tableName));
        } catch (SQLException e) {
            logger.error("[getTableInfo Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> getTables() {
        String sqlQueryTables = sqlBuilder.getSQLQueryTables();
        logger.info(sqlQueryTables);
        List<Map<String, Object>> res = null;
        try {
            res = JdbcUtils.executeQuery(connection, sqlQueryTables, ImmutableList.of(currentSchema));
        } catch (SQLException e) {
            logger.error("[getTables Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        return res;
    }

    @Override
    public List<ColumnInfo> getColumns(String tableName) {

        List<ColumnInfo> fullColumn = Lists.newArrayList();
        //获取指定表的所有字段
        try {
            //获取查询指定表所有字段的sql语句
            String querySql = sqlBuilder.getSQLQueryFields(tableName);
            logger.info("querySql: {}", querySql);

            //获取所有字段
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            ResultSetMetaData metaData = resultSet.getMetaData();

            List<DasColumn> dasColumns = buildDasColumn(tableName, metaData);
            statement.close();

            //构建 fullColumn
            fullColumn = buildFullColumn(dasColumns);

        } catch (SQLException e) {
            logger.error("[getColumns Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        return fullColumn;
    }

    private List<ColumnInfo> buildFullColumn(List<DasColumn> dasColumns) {
        List<ColumnInfo> res = Lists.newArrayList();
        dasColumns.forEach(e -> {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setName(e.getColumnName());
            columnInfo.setComment(e.getColumnComment());
            columnInfo.setType(e.getColumnClassName());
            res.add(columnInfo);
        });
        return res;
    }

    //构建DasColumn对象
    private List<DasColumn> buildDasColumn(String tableName, ResultSetMetaData metaData) {
        List<DasColumn> res = Lists.newArrayList();
        try {
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                DasColumn dasColumn = new DasColumn();
                dasColumn.setColumnClassName(metaData.getColumnClassName(i));
                dasColumn.setColumnTypeName(metaData.getColumnTypeName(i));
                dasColumn.setColumnName(metaData.getColumnName(i));
                res.add(dasColumn);
            }
            Statement statement = connection.createStatement();
            res.forEach(e -> {
                String sqlQueryComment = sqlBuilder.getSQLQueryComment(currentSchema, tableName, e.getColumnName());
                //查询字段注释
                try {
                    ResultSet resultSetComment = statement.executeQuery(sqlQueryComment);
                    while (resultSetComment.next()) {
                        e.setColumnComment(resultSetComment.getString(1));
                    }
                    JdbcUtils.close(resultSetComment);
                } catch (SQLException e1) {
                    logger.error("[buildDasColumn executeQuery Exception] --> "
                            + "the exception message is:" + e1.getMessage());
                }
            });
            JdbcUtils.close(statement);
        } catch (SQLException e) {
            logger.error("[buildDasColumn Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        return res;
    }

    //获取指定表的主键，可能是多个，所以用list
    private List<String> getPrimaryKeys(String tableName) {
        List<String> res = Lists.newArrayList();
        String sqlQueryPrimaryKey = sqlBuilder.getSQLQueryPrimaryKey();
        try {
            List<Map<String, Object>> pkColumns = JdbcUtils.executeQuery(connection, sqlQueryPrimaryKey, ImmutableList.of(currentSchema, tableName));
            //返回主键名称即可
            pkColumns.forEach(e -> res.add((String) new ArrayList<>(e.values()).get(0)));
        } catch (SQLException e) {
            logger.error("[getPrimaryKeys Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        return res;
    }

    @Override
    public List<String> getColumnNames(String tableName, String datasource){
        return getColumnNames(tableName,datasource,null,false);
    }

    @Override
    public List<String> getColumnNames(String tableName, String datasource,String columns) {
       return getColumnNames(tableName,datasource,columns,false);
    }

    @Override
    public List<String> getEsColumnNames(String tableName, String datasource) {
        return getColumnNames(tableName,datasource,null,true);
    }

    @Override
    public List<String> getEsColumnNames(String tableName, String datasource, String columns) {
        return getColumnNames(tableName,datasource,columns,true);
    }

    /**
     * 获取 rdbms 的类型.
     * @param type
     * @return
     */
    private String getKylin2ElasticType(String type) {
        String result = "";
        if (type.equalsIgnoreCase("varchar")){
            result = "keyword";
        }else if (type.equalsIgnoreCase("tinyint")||
                type.equalsIgnoreCase("smallint")||
                type.equalsIgnoreCase("int")||
                type.equalsIgnoreCase("bigint") ){
            result = "int";
        }else if (type.equalsIgnoreCase("float")){
            result = "float";
        }else if (type.equalsIgnoreCase("double")){
            result = "double";
        }else if (type.equalsIgnoreCase("date")||
                type.equalsIgnoreCase("timestamp")||
                type.equalsIgnoreCase("datetime")){
            result = "date";
        }else{
            result = "keyword";
        }
        return result;
    }

    /**
     * 获取列信息
     * @param tableName
     * @param columns
     * @return
     */
    private LinkedList<String> getColumnNames(String tableName, String datasource,String columns,boolean esColumn){
        LinkedList<String> res = Lists.newLinkedList();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //获取查询指定表所有字段的sql语句
            String querySql = "";
            if (StringUtils.isNotBlank(columns)){
                querySql = sqlBuilder.getSQLQueryFields(tableName,columns);
            }else{
                querySql = sqlBuilder.getSQLQueryFields(tableName);
            }
            logger.info("querySql: {}", querySql);
            //获取所有字段
            stmt = connection.createStatement();
            rs = stmt.executeQuery(querySql);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                if (esColumn){
                    if (columnName.contains(Constant.SPLIT_POINT)) {
                        res.add(i - 1 + Constant.SPLIT_SCOLON + columnName.substring(columnName.indexOf(Constant.SPLIT_POINT) + 1) + Constant.SPLIT_SCOLON + getKylin2ElasticType(metaData.getColumnTypeName(i)));
                    } else {
                        res.add(i - 1 + Constant.SPLIT_SCOLON + columnName + Constant.SPLIT_SCOLON + getKylin2ElasticType(metaData.getColumnTypeName(i)));
                    }
                }else {
                    if (JdbcConstants.HIVE.equalsIgnoreCase(datasource)) {
                        if (columnName.contains(Constant.SPLIT_POINT)) {
                            res.add(i - 1 + Constant.SPLIT_SCOLON + columnName.substring(columnName.indexOf(Constant.SPLIT_POINT) + 1) + Constant.SPLIT_SCOLON + metaData.getColumnTypeName(i));
                        } else {
                            res.add(i - 1 + Constant.SPLIT_SCOLON + columnName + Constant.SPLIT_SCOLON + metaData.getColumnTypeName(i));
                        }
                    } else {
                        res.add(columnName);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("[getColumnNames Exception] --> "
                    + "the exception message is:" + e.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return res;
    }

    @Override
    public List<String> getTableNames() {
        List<String> tables = new ArrayList<String>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = getSQLQueryTables();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tableName = rs.getString(1);
                tables.add(tableName);
            }
        } catch (SQLException e) {
            logger.error("[getTableNames Exception] --> "
                    + "the exception message is:" + e.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return tables;
    }

    public Boolean dataSourceTest() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            if (metaData.getDatabaseProductName().length() > 0) {
                return true;
            }
        } catch (SQLException e) {
            logger.error("[dataSourceTest Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        return false;
    }

    /**
     * 不需要其他参数的可不重写
     *
     * @return
     */
    protected String getSQLQueryTables() {
        return sqlBuilder.getSQLQueryTables();
    }

    @Override
    public List<String> getColumnsByQuerySql(String querySql) {

        List<String> res = Lists.newArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //拼装sql语句，在后面加上 where 1=0 即可
            String sql = querySql.concat(" where 1=0");
            //判断是否已有where，如果是，则加 and 1=0
            //从最后一个 ) 开始找 where，或者整个语句找
            if (querySql.contains(")")) {
                if (querySql.substring(querySql.indexOf(")")).contains("where")) {
                    sql = querySql.concat(" and 1=0");
                }
            } else {
                if (querySql.contains("where")) {
                    sql = querySql.concat(" and 1=0");
                }
            }
            //获取所有字段
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                res.add(metaData.getColumnName(i));
            }
        } catch (SQLException e) {
            logger.error("[getColumnsByQuerySql Exception] --> "
                    + "the exception message is:" + e.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return res;
    }
	
}
