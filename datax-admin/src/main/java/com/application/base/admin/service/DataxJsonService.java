package com.application.base.admin.service;

import com.application.base.admin.dto.DataxJsonDto;

/**
 * com.westcredit.datax json构建服务层接口
 *
 * @author admin
 * @version 1.0
 * @since 2019/8/1
 */
public interface DataxJsonService {

    /**
     * 用map接收
     *
     * @param dataxJsonDto
     * @return
     */
    String buildJobJson(DataxJsonDto dataxJsonDto);

}
