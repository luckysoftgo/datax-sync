package com.application.base.admin.tool.datax.writer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.application.base.admin.entity.JobJdbcDatasource;
import com.application.base.admin.tool.datax.BaseDataxPlugin;
import com.application.base.admin.tool.pojo.DataxElasticPojo;
import com.application.base.admin.tool.pojo.DataxHivePojo;
import com.application.base.admin.tool.pojo.DataxRdbmsPojo;

import java.util.Map;

/**
 * TODO
 *
 * @author admin
 * @ClassName BaseWriterPlugin
 * @Version 1.0
 */
public abstract class BaseWriterPlugin extends BaseDataxPlugin {

    @Override
    public Map<String, Object> build(DataxRdbmsPojo plugin) {
        Map<String, Object> writerObj = Maps.newLinkedHashMap();
        writerObj.put("name", getName());

        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
//        parameterObj.put("writeMode", "insert");
        JobJdbcDatasource jobJdbcDatasource = plugin.getJdbcDatasource();
        parameterObj.put("username", jobJdbcDatasource.getJdbcUsername());
        parameterObj.put("password", jobJdbcDatasource.getJdbcPassword());
        parameterObj.put("column", plugin.getRdbmsColumns());
        // preSql
        parameterObj.put("preSql", ImmutableList.of(plugin.getPreSql()));

        Map<String, Object> connectionObj = Maps.newLinkedHashMap();
        connectionObj.put("table", plugin.getTables());
        connectionObj.put("jdbcUrl", jobJdbcDatasource.getJdbcUrl());

        parameterObj.put("connection", ImmutableList.of(connectionObj));
        writerObj.put("parameter", parameterObj);

        return writerObj;
    }

    @Override
    public Map<String, Object> buildHive(DataxHivePojo dataxHivePojo) {
        return null;
    }

    @Override
    public Map<String, Object> buildElastic(DataxElasticPojo dataxElasticPojo) {
        return null;
    }
}
