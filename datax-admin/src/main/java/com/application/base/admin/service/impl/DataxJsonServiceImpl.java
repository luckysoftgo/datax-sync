package com.application.base.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.application.base.admin.config.PlatformConfig;
import com.application.base.admin.dto.DataxJsonDto;
import com.application.base.admin.entity.JobJdbcDatasource;
import com.application.base.admin.service.DataxJsonService;
import com.application.base.admin.service.IJobJdbcDatasourceService;
import com.application.base.admin.tool.datax.DataxJsonHelper;
import com.application.base.admin.util.RemoteApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * com.westcredit.datax json构建实现类
 *
 * @author admin
 * @ClassName DataxJsonServiceImpl
 * @Version 2.0
 * @since 2020/01/11 17:15
 */
@Service
public class DataxJsonServiceImpl implements DataxJsonService {

    @Autowired
    private IJobJdbcDatasourceService jobJdbcDatasourceService;

    @Autowired
    private PlatformConfig platformConfig;
    @Autowired
    private RemoteApiUtil remoteApi;

    @Override
    public String buildJobJson(DataxJsonDto dataxJsonDto) {
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();
        // reader
        JobJdbcDatasource readerDatasource = null;
        if (platformConfig.isPswitch()){
            readerDatasource = remoteApi.getOneDataSource(dataxJsonDto.getReaderDatasourceId());
        }else {
            readerDatasource = jobJdbcDatasourceService.getById(dataxJsonDto.getReaderDatasourceId());
        }
        // reader plugin init
        dataxJsonHelper.initReader(dataxJsonDto,readerDatasource);
        JobJdbcDatasource writerDatasource = null;
        if (platformConfig.isPswitch()){
            writerDatasource = remoteApi.getOneDataSource(dataxJsonDto.getWriterDatasourceId());
        }else{
            writerDatasource = jobJdbcDatasourceService.getById(dataxJsonDto.getWriterDatasourceId());
        }
        dataxJsonHelper.initWriter(dataxJsonDto,writerDatasource);
        return JSON.toJSONString(dataxJsonHelper.buildJob());
    }
}
