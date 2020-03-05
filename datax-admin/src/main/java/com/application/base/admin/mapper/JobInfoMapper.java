package com.application.base.admin.mapper;

import com.application.base.admin.entity.JobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * job info
 * @author admin 2016-1-12 18:03:45
 */
@Mapper
public interface JobInfoMapper {
	
	public List<JobInfo> pageList(@Param("offset") int offset,
	                              @Param("pagesize") int pagesize,
	                              @Param("jobGroup") int jobGroup,
	                              @Param("triggerStatus") int triggerStatus,
	                              @Param("jobDesc") String jobDesc,
	                              @Param("glueType") String glueType,
	                              @Param("author") String author);
	public int pageListCount(@Param("offset") int offset,
	                         @Param("pagesize") int pagesize,
	                         @Param("jobGroup") int jobGroup,
	                         @Param("triggerStatus") int triggerStatus,
	                         @Param("jobDesc") String jobDesc,
	                         @Param("glueType") String glueType,
	                         @Param("author") String author);
	
	public int save(JobInfo info);
	
	public JobInfo loadById(@Param("id") int id);
	
	public int update(JobInfo jobInfo);
	
	public int delete(@Param("id") long id);
	
	public List<JobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);
	
	public int findAllCount();
	
	public List<JobInfo> scheduleJobQuery(@Param("maxNextTime") long maxNextTime, @Param("pagesize") int pagesize);
	
	public int scheduleUpdate(JobInfo xxlJobInfo);
	
	public int incrementTimeUpdate(@Param("id") int id, @Param("incStartTime") Date incStartTime);
	
	
	
}
