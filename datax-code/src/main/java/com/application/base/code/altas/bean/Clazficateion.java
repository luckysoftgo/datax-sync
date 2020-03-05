package com.application.base.code.altas.bean;

/**
 * @author : admin
 * @NAME: Clazficateion
 * @DESC: ClassificationDefs类设计
 **/
public class Clazficateion {
	/**
	 * 分类信息描述.
	 */
	private String name;
	private String description;
	private String category;
	private String guid;
	private String createdBy;
	private String updatedBy;
	private String[] attributeDefs;
	private String[] superTypes;
	private String[] entityTypes;
	private String[] subTypes;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getGuid() {
		return guid;
	}
	
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getUpdatedBy() {
		return updatedBy;
	}
	
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public String[] getAttributeDefs() {
		return attributeDefs;
	}
	
	public void setAttributeDefs(String[] attributeDefs) {
		this.attributeDefs = attributeDefs;
	}
	
	public String[] getSuperTypes() {
		return superTypes;
	}
	
	public void setSuperTypes(String[] superTypes) {
		this.superTypes = superTypes;
	}
	
	public String[] getEntityTypes() {
		return entityTypes;
	}
	
	public void setEntityTypes(String[] entityTypes) {
		this.entityTypes = entityTypes;
	}
	
	public String[] getSubTypes() {
		return subTypes;
	}
	
	public void setSubTypes(String[] subTypes) {
		this.subTypes = subTypes;
	}
}
