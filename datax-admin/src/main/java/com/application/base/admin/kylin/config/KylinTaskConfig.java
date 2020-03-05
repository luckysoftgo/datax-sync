package com.application.base.admin.kylin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by admin on 2020/2/14.
 */
@Configuration
@ConfigurationProperties(prefix = "kylin")
public class KylinTaskConfig {

    /**
     * 请求的用户名.
     */
    private String kylinName;
    /**
     * 请求的密码.
     */
    private String kylinPass;

    /**
     * kylin的连接地址.
     */
    private String kylinUrl;

    /**
     * 请求的api地址
     */
    private String kylinApi;

    /**
     * kylin 的任务
     */
    private String kylinProject;

    public String getKylinName() {
        return kylinName;
    }

    public void setKylinName(String kylinName) {
        this.kylinName = kylinName;
    }

    public String getKylinPass() {
        return kylinPass;
    }

    public void setKylinPass(String kylinPass) {
        this.kylinPass = kylinPass;
    }

    public String getKylinUrl() {
        return kylinUrl;
    }

    public void setKylinUrl(String kylinUrl) {
        this.kylinUrl = kylinUrl;
    }

    public String getKylinApi() {
        return kylinApi;
    }

    public void setKylinApi(String kylinApi) {
        this.kylinApi = kylinApi;
    }

    public String getKylinProject() {
        return kylinProject;
    }

    public void setKylinProject(String kylinProject) {
        this.kylinProject = kylinProject;
    }
}
