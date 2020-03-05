package com.application.base.code.altas.bean;

/**
 * @author : admin
 * @NAME: TableBasicBean
 * @DESC: TableBean类设计
 **/
public class TableBasicBean {
	private String typeName;
	private String qualifiedName;
	private String name;
	private String guid;
	private String[] classificationNames;
	
	public String getTypeName() {
		return typeName;
	}
	
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public String getQualifiedName() {
		return qualifiedName;
	}
	
	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getGuid() {
		return guid;
	}
	
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	public String[] getClassificationNames() {
		return classificationNames;
	}
	
	public void setClassificationNames(String[] classificationNames) {
		this.classificationNames = classificationNames;
	}
}
