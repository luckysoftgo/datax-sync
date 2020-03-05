package com.application.base.code.codeGen;


import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.sql.SQLException;

public class MyBatisPlusGenerator {

	public static void main(String[] args) throws SQLException {
		//1. 全局配置
		com.baomidou.mybatisplus.generator.config.GlobalConfig config = new com.baomidou.mybatisplus.generator.config.GlobalConfig();
		// 是否支持AR模式
		config.setActiveRecord(true)
				// 作者
				.setAuthor("admin")
				// 生成路径
				.setOutputDir("D:\\githubCode\\datax-sync\\datax-executor\\src\\main\\java\\")
				// 文件覆盖
				.setFileOverride(true)
				// 主键策略
				.setIdType(com.baomidou.mybatisplus.enums.IdType.AUTO)
				// 设置生成的service接口的名字的首字母是否为I
				.setServiceName("%sService")
				//生成基本的resultMap
				.setBaseResultMap(true)
				//生成基本的SQL片段
				.setBaseColumnList(true);
		
		//2. 数据源配置
		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setDbType(com.baomidou.mybatisplus.generator.config.rules.DbType.MYSQL);
		dsc.setUrl("jdbc:mysql://192.168.10.143:3306/datax_web?useUnicode=true&useSSL=false&characterEncoding=utf8");
		// dsc.setSchemaName("public");
		dsc.setDriverName("com.mysql.jdbc.Driver");
		dsc.setUsername("root");
		dsc.setPassword("db#@!123WC");
		
		
		//3. 策略配置globalConfiguration中
		StrategyConfig stConfig = new StrategyConfig();
		//全局大写命名
		stConfig.setCapitalMode(true)
				// 数据库表映射到实体的命名策略
				.setNaming(NamingStrategy.underline_to_camel)
				//.setTablePrefix("tbl_")
				// 生成的表
				.setInclude("dynamic_param_input");
		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
		strategy.setEntityLombokModel(true);
		strategy.setRestControllerStyle(true);
		// 公共父类
		strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
		// 写于父类中的公共字段
		strategy.setSuperEntityColumns("id");
		strategy.setControllerMappingHyphenStyle(true);
		
		//4. 包名策略配置
		PackageConfig pkConfig = new PackageConfig();
		pkConfig.setParent("com.westcredit.base.executor.core")
				//dao
				.setMapper("mapper")
				//servcie
				.setService("service")
				//controller
				//.setController("controller")
				.setEntity("entity")
				//mapper.xml
				.setXml("mapper");
		
		//5. 整合配置
		AutoGenerator  ag = new AutoGenerator();
		ag.setGlobalConfig(config)
				.setDataSource(dsc)
				.setStrategy(stConfig)
				.setPackageInfo(pkConfig);
		
		//6. 执行
		ag.execute();
		System.out.println("执行完成!");
	}
}
