package com.application.base.admin.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.application.base.code.altas.AltasClient;
import com.application.base.code.altas.bean.Clazficateion;
import com.application.base.code.altas.bean.DbRdbmsInfo;
import com.application.base.code.altas.bean.TableBasicBean;
import com.application.base.admin.config.AlatsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author : 孤狼
 * @NAME: AltasInfoController
 * @DESC: AltasInfoController类设计
 **/

@RestController
@RequestMapping("api/altas")
@Api(tags = "和altas相关的接口实现.")
public class AltasInfoController extends ApiController {
	
	/**
	 * 属性的配置.
	 */
	@Autowired
	private AlatsConfig config;
	
	
	@PostMapping("/getClassificate")
	@ApiOperation("获得目录结构")
	public R<Object> getClassificate() {
		String cookie = getCookie();
		if(StringUtils.isBlank(cookie)){
			return failed("获取altas的登录信息(cookie)失败!");
		}
		List<Clazficateion> clazficateions = AltasClient.getInstance(config.getAddress()).getClassificate(cookie);
		return success(clazficateions);
	}
	
	
	@PostMapping("/getAltasInfos")
	@ApiOperation("获得目录下的数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name="classification",value="分类名称",required=true,dataType="string"),
			@ApiImplicitParam(name="query",value="查询类型:hive_table ; rdbms_db",required=true,dataType="string"),
			@ApiImplicitParam(name="typeName",value="类型名称:hive_table ; rdbms_db",required=true,dataType="string"),
			@ApiImplicitParam(name="filter",value="是否对表的后缀:invalid|valid|profile 进行过滤:是:true;否:false",required=true,dataType="string")
	})
	public R<Object> getAltasInfos(String classification,String query,String typeName,String flag) {
		boolean filter=true;
		if (StringUtils.isNotBlank(flag)){
			if (flag.equalsIgnoreCase("false")){
				filter = false;
			}
		}
		String cookie = getCookie();
		if(StringUtils.isBlank(cookie)){
			return failed("获取altas的登录信息(cookie)失败!");
		}
		List<TableBasicBean> tableBasicBeans = AltasClient.getInstance(config.getAddress()).getAltasInfos(classification,query,typeName,cookie,filter);
		return success(tableBasicBeans);
	}
	
	
	@PostMapping("/getHiveTableInfo")
	@ApiOperation("获取altas上hive表的信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name="guid",value="搜索后数据给定的guid",required=true,dataType="string")
	})
	public R<Object> getHiveTableInfo(String guid) {
		String cookie = getCookie();
		if(StringUtils.isBlank(cookie)){
			return failed("获取altas的登录信息(cookie)失败!");
		}
		Map<String,Object> hiveTableInfo = AltasClient.getInstance(config.getAddress()).getHiveTableInfo(guid,cookie);
		return success(hiveTableInfo);
	}
	
	
	@PostMapping("/getRdbmsTables")
	@ApiOperation("获取altas上rdbms库表的信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name="guid",value="搜索后数据给定的guid",required=true,dataType="string")
	})
	public R<Object> getRdbmsTables(String guid) {
		String cookie = getCookie();
		if(StringUtils.isBlank(cookie)){
			return failed("获取altas的登录信息(cookie)失败!");
		}
		DbRdbmsInfo dbRdbmsInfo = AltasClient.getInstance(config.getAddress()).getRdbmsTables(guid,cookie);
		return success(dbRdbmsInfo);
	}
	
	
	@PostMapping("/getRdbmsTableInfo")
	@ApiOperation("获取altas上rdbms库上表的信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name="guid",value="搜索后数据给定的guid",required=true,dataType="string")
	})
	public R<Object> getRdbmsTableInfo(String guid) {
		String cookie = getCookie();
		if(StringUtils.isBlank(cookie)){
			return failed("获取altas的登录信息(cookie)失败!");
		}
		Map<String,Object> rdbmsTableInfo = AltasClient.getInstance(config.getAddress()).getRdbmsTableInfo(guid,cookie);
		return success(rdbmsTableInfo);
	}
	
	/**
	 * 获得 altas 的 cookie 信息
	 * @return
	 */
	private String getCookie(){
		 String cookie = AltasClient.getInstance(config.getAddress()).getCookie(config.getUsername(),config.getPassword());
		 return cookie;
	}
	
}
