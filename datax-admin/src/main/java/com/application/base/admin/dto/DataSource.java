package com.application.base.admin.dto;

import lombok.Data;

/**
 * 构建json dto
 * @ClassName DataSource
 * @author admin
 */
@Data
public class DataSource {

    private String sourceId;                //数据源ID
    private String sourceName;              //数据源名称
    private String ipAddr;                  //连接IP地址
    private String port;                    //连接数据源端口
    private String url;                     //数据源连接URL
    private String dbName;                  //数据源库名
    private String driverClassName;         //驱动class类
    private String driverPath;              //驱动文件路径
    private String userName;                //数据库帐号
    private String password;                //数据库密码
    private String sourceType;              //数据源连接类型ID
    private String sourceTypeKey;           //数据源连接类型key
    private String sourceTypeName;          //数据源连接类型名称
    private String dbType;                  //数据库类型ID
    private String dbTypeName;              //数据库类型名称
    private String paths;                   //文件目录链接
    private String createBy;                //创建者
    private String createTime;              //创建时间
    private String updateTime;              //修改时间

}
