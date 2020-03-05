package com.application.base.admin.controller;

import com.application.base.admin.service.JobService;
import com.application.base.core.biz.model.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * index controller
 * @author admin 2015-12-19 16:13:16
 */
@RestController
@Api(tags = "首页接口")
@RequestMapping("/api")
public class IndexController {

	@Resource
	private JobService jobService;


	@GetMapping("/index")
	@ApiOperation("监控图")
	public ReturnT<Map<String, Object>> index() {
		return new ReturnT<>(jobService.dashboardInfo());
	}

    @RequestMapping(value = "/chartInfo",method  = RequestMethod.POST)
	@ApiOperation("图表信息")
	public ReturnT<Map<String, Object>> chartInfo() {
        ReturnT<Map<String, Object>> chartInfo = jobService.chartInfo();
        return chartInfo;
    }

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
}
