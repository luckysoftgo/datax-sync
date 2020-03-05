package com.application.base.admin.platform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.application.base.admin.config.PlatformConfig;
import com.application.base.admin.entity.JobJdbcDatasource;
import com.application.base.admin.util.RemoteApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author : 孤狼
 * @NAME: PlatformServiceImpl
 * @DESC: PlatformServiceImpl类设计
 **/

@Service
public class PlatformServiceImpl implements PlatformService {
	
	@Autowired
	private PlatformConfig platformConfig;
	@Autowired
    private RemoteApiUtil remoteApi;

	/**
	 * 获取连接
	 * @return
	 */
	public Connection getConn(){
		Connection connection=null;
		try {
			Class.forName(platformConfig.getDriver());
			connection = DriverManager.getConnection(platformConfig.getUrl(),platformConfig.getUsername(),platformConfig.getPassword());
		}catch (Exception e){
		}
		return connection;
	}

	@Override
	public IPage<JobJdbcDatasource> getRemoteDataSources() {
		return remoteApi.getPageDataSources();
	}

	@Override
	public IPage<JobJdbcDatasource> getDataSources(int current,int size) {
		int start = (current-1)*size;
		String sql = "select a.source_name,a.port,a.url,a.db_name,a.user_name,a.password,b.driver_class_name from ds_source_info a,ds_db_type b where a.db_type = b.db_type_id and a.source_type = b.source_type_id limit "+start+","+size;
		Connection connection = getConn();
		try {
			PreparedStatement pstmt=connection.prepareStatement(sql);
			ResultSet res = pstmt.executeQuery();
			while (res.next()){

			}
		}catch (Exception e){
		
		}
		return null;
	}

	@Override
	public JobJdbcDatasource getRemoteById(Serializable id) {
		return remoteApi.getOneDataSource(id);
	}
}
