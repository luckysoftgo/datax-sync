package com.application.base.executor.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 动态参数管理
 * </p>
 *
 * @author admin
 * @since 2019-12-18
 */
@TableName("dynamic_param_input")
public class DynamicParamInput extends Model<DynamicParamInput> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增长的主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 删除标志,1删除,0正常使用
     */
    private Integer disabled;
    /**
     * 描述信息
     */
    @TableField("info_desc")
    private String infoDesc;
    /**
     * 创建者
     */
    @TableField("create_user")
    private String createUser;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新者
     */
    @TableField("update_user")
    private String updateUser;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 所对应的的任务id
     */
    @TableField("job_id")
    private Integer jobId;
    /**
     * 参数类型:1:jvm; 2:rdbs (用空格隔开:-Dsdbname=test  -Dstable=test); 3:sql 
     */
    @TableField("param_type")
    private Integer paramType;
    /**
     * 操作类型:1:>; 2:=; 3:>=; 4:<; 5:<=; 6:between..and..
     */
    @TableField("oper_type")
    private Integer operType;
    /**
     * 参数获取的:key,多个值用";"隔开
     */
    @TableField("param_array")
    private String paramArray;
    /**
     * 参数对应的值,如果有多个,用$站位符号,多个值用";"隔开
     */
    @TableField("param_value")
    private String paramValue;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public String getInfoDesc() {
        return infoDesc;
    }

    public void setInfoDesc(String infoDesc) {
        this.infoDesc = infoDesc;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getParamType() {
        return paramType;
    }

    public void setParamType(Integer paramType) {
        this.paramType = paramType;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public String getParamArray() {
        return paramArray;
    }

    public void setParamArray(String paramArray) {
        this.paramArray = paramArray;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DynamicParamInput{" +
        ", id=" + id +
        ", disabled=" + disabled +
        ", infoDesc=" + infoDesc +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", jobId=" + jobId +
        ", paramType=" + paramType +
        ", operType=" + operType +
        ", paramArray=" + paramArray +
        ", paramValue=" + paramValue +
        "}";
    }
}
