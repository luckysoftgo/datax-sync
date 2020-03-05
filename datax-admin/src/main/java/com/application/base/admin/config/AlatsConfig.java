package com.application.base.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : 孤狼
 * @NAME: AlatsConfig
 * @DESC: AlatsConfig类设计
 **/
@Configuration
@ConfigurationProperties(prefix = "altas")
public class AlatsConfig {
	
	/**
	 * 地址:http://192.168.10.185:21000/
	 */
	private String address;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
