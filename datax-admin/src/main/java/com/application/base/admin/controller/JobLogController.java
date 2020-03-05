package com.application.base.admin.controller;

import com.application.base.admin.core.kill.KillJob;
import com.application.base.admin.core.scheduler.JobScheduler;
import com.application.base.admin.core.util.I18nUtil;
import com.application.base.admin.entity.JobInfo;
import com.application.base.admin.entity.JobLog;
import com.application.base.admin.mapper.JobInfoMapper;
import com.application.base.admin.mapper.JobLogMapper;
import com.application.base.core.biz.ExecutorBiz;
import com.application.base.core.biz.model.LogResult;
import com.application.base.core.biz.model.ReturnT;
import com.application.base.core.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2019/11/17
 */
@RestController
@RequestMapping("/api/log")
@Api(tags = "任务运行日志接口")
public class JobLogController {
	private static Logger logger = LoggerFactory.getLogger(JobLogController.class);

	@Resource
	public JobInfoMapper jobInfoMapper;
	@Resource
	public JobLogMapper jobLogMapper;
	
	@GetMapping("/pageList")
	@ApiOperation("运行日志列表")
	public ReturnT<Map<String, Object>> pageList(
										@RequestParam(required = false, defaultValue = "0") int current,
										@RequestParam(required = false, defaultValue = "10") int size,
										int jobGroup, int jobId, int logStatus, String filterTime) {

		// valid permission
		//JobInfoController.validPermission(request, jobGroup);	// 仅管理员支持查询全部；普通用户仅支持查询有权限的 jobGroup

		// parse param
		Date triggerTimeStart = null;
		Date triggerTimeEnd = null;
		if (filterTime!=null && filterTime.trim().length()>0) {
			String[] temp = filterTime.split(" - ");
			if (temp.length == 2) {
				triggerTimeStart = DateUtil.parseDateTime(temp[0]);
				triggerTimeEnd = DateUtil.parseDateTime(temp[1]);
			}
		}

		// page query
		List<JobLog> list = jobLogMapper.pageList((current-1)*size, size, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus);
		int list_count = jobLogMapper.pageListCount((current-1)*size, size, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus);

		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("recordsTotal", list_count);		// 总记录数
		maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
		maps.put("data", list);  					// 分页列表
		return new ReturnT<>(maps);
	}

	@RequestMapping(value ="/logDetailCat",method = RequestMethod.GET)
	@ApiOperation("运行日志详情")
	public ReturnT<LogResult> logDetailCat(String executorAddress, long triggerTime, long logId, int fromLineNum){
		try {
			ExecutorBiz executorBiz = JobScheduler.getExecutorBiz(executorAddress);
			ReturnT<LogResult> logResult = executorBiz.log(triggerTime, logId, fromLineNum);

			// is end
			if (logResult.getContent()!=null && logResult.getContent().getFromLineNum() > logResult.getContent().getToLineNum()) {
				JobLog jobLog = jobLogMapper.load(logId);
				if (jobLog.getHandleCode() > 0) {
					logResult.getContent().setEnd(true);
				}
			}

			return logResult;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ReturnT<LogResult>(ReturnT.FAIL_CODE, e.getMessage());
		}
	}

	@RequestMapping(value="/logKill",method = RequestMethod.POST)
	@ApiOperation("kill任务")
	public ReturnT<String> logKill(int id){
		// base check
		JobLog log = jobLogMapper.load(id);
		JobInfo jobInfo = jobInfoMapper.loadById(log.getJobId());
		if (jobInfo==null) {
			return new ReturnT<String>(500, I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
		}
		if (ReturnT.SUCCESS_CODE != log.getTriggerCode()) {
			return new ReturnT<String>(500, I18nUtil.getString("joblog_kill_log_limit"));
		}

		// request of kill
		ReturnT<String> runResult = null;
		try {
			ExecutorBiz executorBiz = JobScheduler.getExecutorBiz(log.getExecutorAddress());
			runResult = executorBiz.kill(jobInfo.getId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			runResult = new ReturnT<String>(500, e.getMessage());
		}

		if (ReturnT.SUCCESS_CODE == runResult.getCode()) {
			log.setHandleCode(ReturnT.FAIL_CODE);
			log.setHandleMsg( I18nUtil.getString("joblog_kill_log_byman")+":" + (runResult.getMsg()!=null?runResult.getMsg():""));
			log.setHandleTime(new Date());
			jobLogMapper.updateHandleInfo(log);
			return new ReturnT<String>(runResult.getMsg());
		} else {
			return new ReturnT<String>(500, runResult.getMsg());
		}
	}

	@RequestMapping(value = "/clearLog",method = RequestMethod.POST)
	@ApiOperation("清理日志")
	public ReturnT<String> clearLog(int jobGroup, int jobId, int type){

		Date clearBeforeTime = null;
		int clearBeforeNum = 0;
		if (type == 1) {
			clearBeforeTime = DateUtil.addMonths(new Date(), -1);	// 清理一个月之前日志数据
		} else if (type == 2) {
			clearBeforeTime = DateUtil.addMonths(new Date(), -3);	// 清理三个月之前日志数据
		} else if (type == 3) {
			clearBeforeTime = DateUtil.addMonths(new Date(), -6);	// 清理六个月之前日志数据
		} else if (type == 4) {
			clearBeforeTime = DateUtil.addYears(new Date(), -1);	// 清理一年之前日志数据
		} else if (type == 5) {
			clearBeforeNum = 1000;		// 清理一千条以前日志数据
		} else if (type == 6) {
			clearBeforeNum = 10000;		// 清理一万条以前日志数据
		} else if (type == 7) {
			clearBeforeNum = 30000;		// 清理三万条以前日志数据
		} else if (type == 8) {
			clearBeforeNum = 100000;	// 清理十万条以前日志数据
		} else if (type == 9) {
			clearBeforeNum = 0;			// 清理所有日志数据
		} else {
			return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("joblog_clean_type_unvalid"));
		}

		List<Long> logIds = null;
		do {
			logIds = jobLogMapper.findClearLogIds(jobGroup, jobId, clearBeforeTime, clearBeforeNum, 1000);
			if (logIds!=null && logIds.size()>0) {
				jobLogMapper.clearLog(logIds);
			}
		} while (logIds!=null && logIds.size()>0);

		return ReturnT.SUCCESS;
	}

	@ApiOperation("停止该job作业")
	@PostMapping("/killJob")
	public ReturnT<String> killJob(@RequestBody JobLog log){
		return KillJob.trigger(log.getId(), log.getTriggerTime(), log.getExecutorAddress(),log.getProcessId());
	}
}
