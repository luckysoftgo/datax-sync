package com.application.base.admin.controller;

import com.application.base.admin.core.util.I18nUtil;
import com.application.base.admin.entity.JobInfo;
import com.application.base.admin.entity.JobLogGlue;
import com.application.base.admin.mapper.JobLogGlueMapper;
import com.application.base.core.biz.model.ReturnT;
import com.application.base.admin.mapper.JobInfoMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by admin on 2019/11/17
 */
@RestController
@RequestMapping("/jobcode")
@Api(tags = "任务状态接口")
public class JobCodeController {
	
	@Resource
	private JobInfoMapper jobInfoMapper;
	@Resource
	private JobLogGlueMapper jobLogGlueMapper;

	
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	@ApiOperation("保存任务状态")
	public ReturnT<String> save(Model model, int id, String glueSource, String glueRemark) {
		// valid
		if (glueRemark==null) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_glue_remark")) );
		}
		if (glueRemark.length()<4 || glueRemark.length()>100) {
			return new ReturnT<String>(500, I18nUtil.getString("jobinfo_glue_remark_limit"));
		}
		JobInfo exists_jobInfo = jobInfoMapper.loadById(id);
		if (exists_jobInfo == null) {
			return new ReturnT<String>(500, I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
		}

		// update new code
		exists_jobInfo.setGlueSource(glueSource);
		exists_jobInfo.setGlueRemark(glueRemark);
		exists_jobInfo.setGlueUpdatetime(new Date());

		exists_jobInfo.setUpdateTime(new Date());
		jobInfoMapper.update(exists_jobInfo);
		
		// log old code
		JobLogGlue jobLogGlue = new JobLogGlue();
		jobLogGlue.setJobId(exists_jobInfo.getId());
		jobLogGlue.setGlueType(exists_jobInfo.getGlueType());
		jobLogGlue.setGlueSource(glueSource);
		jobLogGlue.setGlueRemark(glueRemark);
		
		jobLogGlue.setAddTime(new Date());
		jobLogGlue.setUpdateTime(new Date());
		jobLogGlueMapper.save(jobLogGlue);

		// remove code backup more than 30
		jobLogGlueMapper.removeOld(exists_jobInfo.getId(), 30);

		return ReturnT.SUCCESS;
	}
	
}
