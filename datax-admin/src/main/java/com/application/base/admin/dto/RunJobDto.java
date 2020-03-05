package com.application.base.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于启动任务接收的实体
 *
 * @author admin
 * @ClassName RunJobDto
 * @Version 1.0
 * @since 2019/6/27 16:12
 */
@Data
public class RunJobDto implements Serializable {

    private String jobJson;

    private Long jobConfigId;
}
