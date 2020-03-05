package com.application.base.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 构建elastic write dto
 *
 * @author admin
 * @ClassName elastic write dto
 * @Version 2.0
 */
@Data
public class ElasticWriterDto implements Serializable {

    /**
     * es的地址：xxx:9200
     */
    private String endpoint;
    /**
     * 连接权限配置，如果不需要权限认证，可随意配置，但不能为空，或者不配
     */
    private String accessId;
    /**
     * 连接权限配置，如果不需要权限认证，可随意配置
     */
    private String accessKey;
    /**
     * 索引
     */
    private String index;
    /**
     * 类型
     */
    private String type="_doc";
    private boolean cleanup = true;
    private boolean discovery = false;
    /**
     * 同步一次数量
     */
    private Integer batchSize=1000;
    /**
     * 分割符号
     */
    private String splitter=",";

}
