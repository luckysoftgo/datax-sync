package com.application.base.admin.dto;


import lombok.Data;

import java.util.List;

/**
 * 构建json dto
 * @ClassName DataSource
 * @author admin
 */
@Data
public class RemoteVo {
    private int code;
    private int count;
    private List<DataSource> data;
}
