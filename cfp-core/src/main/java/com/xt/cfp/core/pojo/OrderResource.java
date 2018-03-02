package com.xt.cfp.core.pojo;

import java.util.Date;

public class OrderResource {
	
	private Long resourceId;
	private Long resourceType;
	private String resourceDesc;
	private Date createTime;
	private Date endTime;
	private Long valid;
	private Long parentId;
	
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	public Long getResourceType() {
		return resourceType;
	}
	public void setResourceType(Long resourceType) {
		this.resourceType = resourceType;
	}
	public String getResourceDesc() {
		return resourceDesc;
	}
	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Long getValid() {
		return valid;
	}
	public void setValid(Long valid) {
		this.valid = valid;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
