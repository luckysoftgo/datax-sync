package com.application.base.core.biz.model;

import java.io.Serializable;

/**
 * @author : 孤狼
 * @NAME: IncrementalParam
 * @DESC:
 **/
public class IncrementalParam  implements Serializable {
	
	private static final long serialVersionUID = 42L;
	
	private String replaceParam;
	private String jvmParam;
	private int timeOffset;
	
	public String getReplaceParam() {
		return replaceParam;
	}
	
	public void setReplaceParam(String replaceParam) {
		this.replaceParam = replaceParam;
	}
	
	public String getJvmParam() {
		return jvmParam;
	}
	
	public void setJvmParam(String jvmParam) {
		this.jvmParam = jvmParam;
	}
	
	public int getTimeOffset() {
		return timeOffset;
	}
	
	public void setTimeOffset(int timeOffset) {
		this.timeOffset = timeOffset;
	}
}
