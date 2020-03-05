package com.application.base.admin.mapper;

import com.application.base.admin.entity.JobTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * job info
 * @author admin 2016-1-12 18:03:45
 */
@Mapper
public interface JobTemplateMapper {

	public List<JobTemplate> pageList(@Param("offset") int offset,
	                                  @Param("pagesize") int pagesize,
	                                  @Param("jobGroup") int jobGroup,
	                                  @Param("jobDesc") String jobDesc,
	                                  @Param("executorHandler") String executorHandler,
	                                  @Param("author") String author);
	public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("jobGroup") int jobGroup,
                             @Param("jobDesc") String jobDesc,
                             @Param("executorHandler") String executorHandler,
                             @Param("author") String author);

	public int save(JobTemplate info);

	public JobTemplate loadById(@Param("id") int id);

	public int update(JobTemplate xxlJobTemplate);

	public int delete(@Param("id") long id);

}
