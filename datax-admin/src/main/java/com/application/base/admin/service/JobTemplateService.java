package com.application.base.admin.service;


import com.application.base.admin.entity.JobTemplate;
import com.application.base.core.biz.model.ReturnT;

import java.util.Map;

/**
 * core job action for datax-web
 * 
 * @author admin 2016-5-28 15:30:33
 */
public interface JobTemplateService {

	/**
	 * page list
	 *
	 * @param start
	 * @param length
	 * @param jobGroup
	 * @param jobDesc
	 * @param executorHandler
	 * @param author
	 * @return
	 */
	public Map<String, Object> pageList(int start, int length, int jobGroup, String jobDesc, String executorHandler, String author);

	/**
	 * add job
	 *
	 * @param jobTemplate
	 * @return
	 */
	public ReturnT<String> add(JobTemplate jobTemplate);

	/**
	 * update job
	 *
	 * @param jobTemplate
	 * @return
	 */
	public ReturnT<String> update(JobTemplate jobTemplate);

	/**
	 * remove job
	 * 	 *
	 * @param id
	 * @return
	 */
	public ReturnT<String> remove(int id);
}
