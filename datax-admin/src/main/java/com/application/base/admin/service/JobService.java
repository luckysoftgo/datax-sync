package com.application.base.admin.service;


import com.application.base.admin.entity.JobInfo;
import com.application.base.core.biz.model.ReturnT;

import java.util.Map;

/**
 * core job action for datax-web
 * 
 * @author admin 2016-5-28 15:30:33
 */
public interface JobService {
	/**
	 * page list
	 *
	 * @param start
	 * @param length
	 * @param jobGroup
	 * @param jobDesc
	 * @param glueType
	 * @param author
	 * @return
	 */
	public Map<String, Object> pageList(int start, int length, int jobGroup, int triggerStatus, String jobDesc, String glueType, String author);
	
	/**
	 * add job
	 *
	 * @param jobInfo
	 * @return
	 */
	public ReturnT<String> add(JobInfo jobInfo);
	
	/**
	 * update job
	 *
	 * @param jobInfo
	 * @return
	 */
	public ReturnT<String> update(JobInfo jobInfo);
	
	/**
	 * remove job
	 * 	 *
	 * @param id
	 * @return
	 */
	public ReturnT<String> remove(int id);
	
	/**
	 * start job
	 *
	 * @param id
	 * @return
	 */
	public ReturnT<String> start(int id);
	
	/**
	 * stop job
	 *
	 * @param id
	 * @return
	 */
	public ReturnT<String> stop(int id);
	
	/**
	 * dashboard info
	 *
	 * @return
	 */
	public Map<String, Object> dashboardInfo();
	
	/**
	 * chart info
	 * @return
	 */
	public ReturnT<Map<String, Object>> chartInfo();
}
