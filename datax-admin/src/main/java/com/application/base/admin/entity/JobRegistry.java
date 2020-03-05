package com.application.base.admin.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by admin on 16/9/30.
 */
@Data
public class JobRegistry {

    private int id;
    private String registryGroup;
    private String registryKey;
    private String registryValue;
    private Date updateTime;
}
