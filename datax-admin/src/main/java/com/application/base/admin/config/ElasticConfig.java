package com.application.base.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : 孤狼
 * @NAME: DataxConfig
 * @DESC: DataxConfig类设计
 **/
@Configuration
@ConfigurationProperties(prefix = "elastic")
public class ElasticConfig {

    /**
     * es 地址ip:port,ip:port
     */
    private String esCluster;
    private String serverIps;
    private String esName;
    private String esPass;
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

    public String getEsCluster() {
        return esCluster;
    }

    public void setEsCluster(String esCluster) {
        this.esCluster = esCluster;
    }

    public String getServerIps() {
        return serverIps;
    }

    public void setServerIps(String serverIps) {
        this.serverIps = serverIps;
    }

    public String getEsName() {
        return esName;
    }

    public void setEsName(String esName) {
        this.esName = esName;
    }

    public String getEsPass() {
        return esPass;
    }

    public void setEsPass(String esPass) {
        this.esPass = esPass;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}
