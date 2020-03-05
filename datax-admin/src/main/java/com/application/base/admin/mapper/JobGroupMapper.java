package com.application.base.admin.mapper;

import com.application.base.admin.entity.JobGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by admin on 16/9/30.
 */
@Mapper
public interface JobGroupMapper {

    public List<JobGroup> findAll();

    public List<JobGroup> findByAddressType(@Param("addressType") int addressType);

    public int save(JobGroup xxlJobGroup);

    public int update(JobGroup xxlJobGroup);

    public int remove(@Param("id") int id);

    public JobGroup load(@Param("id") int id);
}
