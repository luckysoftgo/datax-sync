package com.application.base.admin.tool.datax.writer;

import com.google.common.collect.Maps;
import com.application.base.admin.tool.pojo.DataxElasticPojo;
import com.application.base.admin.tool.pojo.DataxHivePojo;

import java.util.Map;

/**
 * hive writer构建类
 *
 * @author admin
 * @version 2.0
 * @since 2020/01/05
 */
public class ElasticWriter extends BaseWriterPlugin implements DataxWriterInterface {

    @Override
    public String getName() {
        return "elasticsearchwriter";
    }

    @Override
    public Map<String, Object> buildElastic(DataxElasticPojo plugin) {
        Map<String, Object> writerObj = Maps.newLinkedHashMap();
        writerObj.put("name", getName());

        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        parameterObj.put("endpoint", plugin.getEndpoint());
        parameterObj.put("accessId", plugin.getAccessId());
        parameterObj.put("accessKey", plugin.getAccessKey());
        parameterObj.put("index", plugin.getIndex());
        parameterObj.put("type", plugin.getType());
        //parameterObj.put("settings", null);
        parameterObj.put("cleanup", plugin.isCleanup());
        parameterObj.put("discovery", plugin.isDiscovery());
        parameterObj.put("batchSize", plugin.getBatchSize());
        parameterObj.put("splitter", plugin.getSplitter());
        parameterObj.put("column", plugin.getColumns());
        writerObj.put("parameter", parameterObj);
        return writerObj;
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }

}
