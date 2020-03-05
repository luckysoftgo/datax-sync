package com.application.base.admin.platform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.application.base.admin.entity.JobJdbcDatasource;

import java.io.Serializable;

/**
 * @author : 孤狼
 * @NAME: PlatformService
 * @DESC: PlatformService类设计
 **/
public interface PlatformService {
	
	/**
	 * 获取所有的数据配置
	 */
	public IPage<JobJdbcDatasource> getRemoteDataSources();

	/**
	 * 获取所有的数据配置
	 */
	public IPage<JobJdbcDatasource> getDataSources(int current, int size);

	/**
	 * 获取数据源
	 * @param id
	 * @return
	 */
    public JobJdbcDatasource getRemoteById(Serializable id);
}
