package com.application.base.admin.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.application.base.admin.config.PlatformConfig;
import com.application.base.admin.dto.DataSource;
import com.application.base.admin.dto.RemoteVo;
import com.application.base.admin.entity.JobJdbcDatasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RemoteApiUtil {

    @Autowired
    private PlatformConfig platformConfig;
    /**
     * 数据源设置.
     */
    public static final ConcurrentHashMap<Long, JobJdbcDatasource>  remoteDataSourceMap = new ConcurrentHashMap<Long, JobJdbcDatasource>();


    /***
     * 获取数据源
     * @return
     */
    public IPage<JobJdbcDatasource> getPageDataSources() {
        String url = platformConfig.getSourceUrl();
        String data = OpenApiUtil.get(url);
        IPage<JobJdbcDatasource> page = new Page<JobJdbcDatasource>();
        List<JobJdbcDatasource> datasources = new ArrayList<JobJdbcDatasource>();
        RemoteVo remoteVo = new Gson().fromJson(data, RemoteVo.class);
        if (remoteVo!=null && remoteVo.getCode()==0){
            List<DataSource> tmpSources = (List<DataSource>) remoteVo.getData();
            if (tmpSources!=null && tmpSources.size()>0){
                Long index = 0L;
                for (DataSource source:tmpSources){
                    index++;
                    JobJdbcDatasource datasource = new JobJdbcDatasource();
                    datasource.setDatasourceName(source.getSourceName());
                    datasource.setDatasource(source.getDbTypeName());
                    datasource.setDatasourceGroup("Default");
                    datasource.setJdbcUsername(source.getUserName());
                    datasource.setJdbcPassword(source.getPassword());
                    datasource.setJdbcUrl(source.getUrl());
                    datasource.setJdbcDriverClass(source.getDriverClassName());
                    datasource.setComments(source.getSourceTypeName());
                    datasource.setId(index);
                    datasources.add(datasource);
                    remoteDataSourceMap.put(index,datasource);
                }
            }
        }
        page.setRecords(datasources);
        return page;
    }

    /**
     * 返回单个
     * @param id
     * @return
     */
    public JobJdbcDatasource getOneDataSource(Serializable id) {
        return remoteDataSourceMap.get(id);
    }
}
