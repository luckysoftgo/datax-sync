package com.application.base.admin.mapper;

import com.application.base.admin.entity.JobLogReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * job log
 * @author admin 2019-11-22
 */
@Mapper
public interface JobLogReportMapper {

	public int save(JobLogReport xxlJobLogReport);

	public int update(JobLogReport xxlJobLogReport);

	public List<JobLogReport> queryLogReport(@Param("triggerDayFrom") Date triggerDayFrom,
                                             @Param("triggerDayTo") Date triggerDayTo);

	public JobLogReport queryLogReportTotal();

}
