package com.application.base.admin.tool.datax.writer;

import com.application.base.admin.tool.pojo.DataxElasticPojo;
import com.application.base.admin.tool.pojo.DataxHivePojo;

import java.util.Map;

/**
 * mysql writer构建类
 *
 * @author admin
 * @ClassName MysqlWriter
 * @Version 1.0
 * @since 2019/7/30 23:08
 */
public class KylinWriter extends BaseWriterPlugin implements DataxWriterInterface {

    @Override
    public String getName() {
        return "rdbmswriter";
    }

    @Override
    public Map<String, Object> buildHive(DataxHivePojo dataxHivePojo) {
        return null;
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }

    @Override
    public Map<String, Object> buildElastic(DataxElasticPojo dataxElasticPojo) {
        return null;
    }
}
