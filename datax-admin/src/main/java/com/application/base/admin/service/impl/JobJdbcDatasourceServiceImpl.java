package com.application.base.admin.service.impl;

import com.alibaba.druid.util.JdbcConstants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.application.base.admin.entity.JobJdbcDatasource;
import com.application.base.admin.kylin.KylinConstant;
import com.application.base.admin.mapper.JobJdbcDatasourceMapper;
import com.application.base.admin.service.IJobJdbcDatasourceService;
import com.application.base.admin.tool.query.BaseQueryTool;
import com.application.base.admin.tool.query.QueryToolFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * jdbc数据源配置表服务实现类
 *
 * @author admin
 * @version v1.0
 * @since 2019-07-30
 */
@Service
@Transactional(readOnly = true)
public class JobJdbcDatasourceServiceImpl extends ServiceImpl<JobJdbcDatasourceMapper, JobJdbcDatasource> implements IJobJdbcDatasourceService {

    @Override
    public Boolean dataSourceTest(JobJdbcDatasource jdbcDatasource)  {
        if (jdbcDatasource.getDatasource().equalsIgnoreCase(KylinConstant.ELASTICSEARCH)
                || jdbcDatasource.getDatasource().equalsIgnoreCase(JdbcConstants.ELASTIC_SEARCH)){
            return true;
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
        return queryTool.dataSourceTest();
    }

}