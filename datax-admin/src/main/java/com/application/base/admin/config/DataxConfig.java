package com.application.base.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author : 孤狼
 * @NAME: DataxConfig
 * @DESC: DataxConfig类设计
 **/
@Configuration
public class DataxConfig {
	
	@Bean
	public RestTemplate customRestTemplate(){
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(30000);
		httpRequestFactory.setConnectTimeout(30000);
		httpRequestFactory.setReadTimeout(30000);
		return new RestTemplate(httpRequestFactory);
	}
	
}
