package com.xt.cfp.core.pojo;

import java.util.Date;

public class OrderResourceMapping {
	
	private Long orderResourceId;
	private Long resourceId;
	private Long resourceType;
	private Date createTime;
	private Long orderId;
	
	public OrderResourceMapping(long resourceId,long resourceType,long orderId,Date createTime){
		this.resourceId=resourceId;
		this.resourceType=resourceType;
		this.orderId=orderId;
		this.createTime=createTime;
	}
	
	public Long getOrderResourceId() {
		return orderResourceId;
	}
	public void setOrderResourceId(Long orderResourceId) {
		this.orderResourceId = orderResourceId;
	}
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
}
