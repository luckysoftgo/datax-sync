package com.application.base.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : 孤狼
 * @NAME: AlatsConfig
 * @DESC: AlatsConfig类设计
 **/
@Configuration
@ConfigurationProperties(prefix = "platform")
public class PlatformConfig {

	/**
	 * 开关设置.
	 */
	private boolean pswitch;
	/**
	 * url
	 */
	private String url;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 驱动
	 */
	private String driver;
	/**
	 *访问标识:true 外网,false内网
	 */
	private boolean reqTag;
	/**
	 *内网token
	 */
	private String tokenInnerUrl;
	/**
	 *外网token
	 */
	private String tokenOuterUrl;
	/**
	 *source Url
	 */
	private String sourceUrl;

	/**
	 * 访问地址
	 * @return
	 */
	public String getTokenUrl(){
		if (isReqTag()){
			return getTokenOuterUrl();
		}
		return getTokenInnerUrl();
	}

	public boolean isPswitch() {
		return pswitch;
	}

	public void setPswitch(boolean pswitch) {
		this.pswitch = pswitch;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public boolean isReqTag() {
		return reqTag;
	}

	public void setReqTag(boolean reqTag) {
		this.reqTag = reqTag;
	}

	public String getTokenInnerUrl() {
		return tokenInnerUrl;
	}

	public void setTokenInnerUrl(String tokenInnerUrl) {
		this.tokenInnerUrl = tokenInnerUrl;
	}

	public String getTokenOuterUrl() {
		return tokenOuterUrl;
	}

	public void setTokenOuterUrl(String tokenOuterUrl) {
		this.tokenOuterUrl = tokenOuterUrl;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
}
