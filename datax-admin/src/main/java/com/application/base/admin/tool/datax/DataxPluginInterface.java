package com.application.base.admin.tool.datax;

import com.application.base.admin.tool.pojo.DataxElasticPojo;
import com.application.base.admin.tool.pojo.DataxHivePojo;
import com.application.base.admin.tool.pojo.DataxRdbmsPojo;

import java.util.Map;

/**
 * 插件基础接口
 *
 * @author admin
 * @ClassName DataxPluginInterface
 * @Version 1.0
 */
public interface DataxPluginInterface {
    /**
     * 获取reader插件名称
     *
     * @return
     */
    String getName();

    /**
     * 构建
     *
     * @return dataxPluginPojo
     */
    Map<String, Object> build(DataxRdbmsPojo dataxPluginPojo);


    /**
     *  构建 hive 机制.
     * @param dataxHivePojo
     * @return
     */
    Map<String, Object> buildHive(DataxHivePojo dataxHivePojo);

    /**
     * 构建es的数据.
     * @param dataxElasticPojo
     * @return
     */
    Map<String, Object> buildElastic(DataxElasticPojo dataxElasticPojo);

    /**
     * 获取示例
     *
     * @return
     */
    Map<String, Object> sample();
}
