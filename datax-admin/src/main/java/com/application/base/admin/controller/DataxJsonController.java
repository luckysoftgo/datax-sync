package com.application.base.admin.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.application.base.admin.core.util.I18nUtil;
import com.application.base.admin.dto.DataxJsonDto;
import com.application.base.admin.service.DataxJsonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组装datax  json的控制器
 *
 * @author admin
 * @ClassName DataxJsonController
 * @Version 1.0
 * @since 2019/7/31 20:47
 */

@RestController
@RequestMapping("api/dataxJson")
@Api(tags = "组装datax  json的控制器")
public class DataxJsonController extends ApiController {

    @Autowired
    private DataxJsonService dataxJsonService;

    @PostMapping("/buildJson")
    @ApiOperation("JSON构建")
    public R<String> buildJobJson(@RequestBody DataxJsonDto dataxJsonDto) {
	    if (dataxJsonDto.getReaderDatasourceId()==null) {
		    return failed(I18nUtil.getString("system_please_choose")+I18nUtil.getString("jobinfo_field_readerDataSource"));
	    }
	    if (dataxJsonDto.getWriterDatasourceId()==null) {
		    return failed(I18nUtil.getString("system_please_choose")+I18nUtil.getString("jobinfo_field_writerDataSource"));
	    }
	    if (CollectionUtils.isEmpty(dataxJsonDto.getReaderColumns())) {
		    return failed(I18nUtil.getString("system_please_choose")+I18nUtil.getString("jobinfo_field_readerColumns"));
	    }
	    if (CollectionUtils.isEmpty(dataxJsonDto.getWriterColumns())) {
		    return failed(I18nUtil.getString("system_please_choose")+I18nUtil.getString("jobinfo_field_writerColumns"));
	    }
        return success(dataxJsonService.buildJobJson(dataxJsonDto));
    }

}
