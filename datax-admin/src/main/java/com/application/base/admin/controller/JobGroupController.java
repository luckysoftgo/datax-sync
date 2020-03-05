package com.application.base.admin.controller;

import com.application.base.admin.core.util.I18nUtil;
import com.application.base.admin.entity.JobGroup;
import com.application.base.admin.entity.JobRegistry;
import com.application.base.admin.mapper.JobRegistryMapper;
import com.application.base.core.biz.model.ReturnT;
import com.application.base.core.enums.RegistryConfig;
import com.application.base.admin.mapper.JobGroupMapper;
import com.application.base.admin.mapper.JobInfoMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by admin on 2019/11/17
 */
@RestController
@RequestMapping("/api/jobGroup")
@Api(tags = "执行器管理接口")
public class JobGroupController {

	@Resource
	public JobInfoMapper jobInfoMapper;
	@Resource
	public JobGroupMapper jobGroupMapper;
	@Resource
	private JobRegistryMapper jobRegistryMapper;

	@GetMapping("/list")
	@ApiOperation("执行器列表")
	public ReturnT<List<JobGroup>> getExecutorList() {
		// job group (executor)
		return new ReturnT<>(jobGroupMapper.findAll());
	}
	
	
	@PostMapping("/save")
	@ApiOperation("新建执行器")
	public ReturnT<String> save(@RequestBody JobGroup jobGroup){
		
		// valid
		if (jobGroup.getAppName()==null || jobGroup.getAppName().trim().length()==0) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input")+"AppName") );
		}
		if (jobGroup.getAppName().length()<4 || jobGroup.getAppName().length()>64) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appName_length") );
		}
		if (jobGroup.getTitle()==null || jobGroup.getTitle().trim().length()==0) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")) );
		}
		if (jobGroup.getAddressType()!=0) {
			if (jobGroup.getAddressList()==null || jobGroup.getAddressList().trim().length()==0) {
				return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit") );
			}
			String[] addresss = jobGroup.getAddressList().split(",");
			for (String item: addresss) {
				if (item==null || item.trim().length()==0) {
					return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid") );
				}
			}
		}
		
		int ret = jobGroupMapper.save(jobGroup);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}
	
	@PostMapping("/update")
	@ApiOperation("更新执行器")
	public ReturnT<String> update(@RequestBody JobGroup jobGroup){
		// valid
		if (jobGroup.getAppName()==null || jobGroup.getAppName().trim().length()==0) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input")+"AppName") );
		}
		if (jobGroup.getAppName().length()<4 || jobGroup.getAppName().length()>64) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appName_length") );
		}
		if (jobGroup.getTitle()==null || jobGroup.getTitle().trim().length()==0) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")) );
		}
		if (jobGroup.getAddressType() == 0) {
			// 0=自动注册
			List<String> registryList = findRegistryByAppName(jobGroup.getAppName());
			String addressListStr = null;
			if (registryList!=null && !registryList.isEmpty()) {
				Collections.sort(registryList);
				addressListStr = "";
				for (String item:registryList) {
					addressListStr += item + ",";
				}
				addressListStr = addressListStr.substring(0, addressListStr.length()-1);
			}
			jobGroup.setAddressList(addressListStr);
		} else {
			// 1=手动录入
			if (jobGroup.getAddressList()==null || jobGroup.getAddressList().trim().length()==0) {
				return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit") );
			}
			String[] addresss = jobGroup.getAddressList().split(",");
			for (String item: addresss) {
				if (item==null || item.trim().length()==0) {
					return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid") );
				}
			}
		}
		
		int ret = jobGroupMapper.update(jobGroup);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}
	
	private List<String> findRegistryByAppName(String appNameParam){
		HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
		List<JobRegistry> list = jobRegistryMapper.findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
		if (list != null) {
			for (JobRegistry item: list) {
				if (RegistryConfig.RegistType.EXECUTOR.name().equalsIgnoreCase(item.getRegistryGroup())) {
					String appName = item.getRegistryKey();
					List<String> registryList = appAddressMap.get(appName);
					if (registryList == null) {
						registryList = new ArrayList<String>();
					}
					
					if (!registryList.contains(item.getRegistryValue())) {
						registryList.add(item.getRegistryValue());
					}
					appAddressMap.put(appName, registryList);
				}
			}
		}
		return appAddressMap.get(appNameParam);
	}
	
	@RequestMapping(value = "/remove",method = RequestMethod.POST)
	@ApiOperation("移除执行器")
	public ReturnT<String> remove(int id){
		
		// valid
		int count = jobInfoMapper.pageListCount(0, 10, id, -1,  null, null, null);
		if (count > 0) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_0") );
		}
		
		List<JobGroup> allList = jobGroupMapper.findAll();
		if (allList.size() == 1) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_1") );
		}
		
		int ret = jobGroupMapper.remove(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}
	
	@RequestMapping(value = "/loadById",method = RequestMethod.POST)
	@ApiOperation("获取执行器")
	public ReturnT<JobGroup> loadById(int id){
		JobGroup jobGroup = jobGroupMapper.load(id);
		return jobGroup!=null?new ReturnT<JobGroup>(jobGroup):new ReturnT<JobGroup>(ReturnT.FAIL_CODE, null);
	}

}
