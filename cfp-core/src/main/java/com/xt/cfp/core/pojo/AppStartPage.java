package com.xt.cfp.core.pojo;

import java.util.Date;

/**
 * APP启动页
 */
public class AppStartPage {
    private Long appStartPageId;//启动页ID
    private String appType;//APP类型
    private String pageTitle;//标题
    private String picUrl;//图片请求路径
    private String picPath;//图片物理路径
    private String status;//状态
    private Date updateTime;//最后更改时间
    private Long adminId;//操作人ID
    
    //辅助字段
    private String adminDisplayName;//管理员名称

    public Long getAppStartPageId() {
        return appStartPageId;
    }

    public void setAppStartPageId(Long appStartPageId) {
        this.appStartPageId = appStartPageId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType == null ? null : appType.trim();
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle == null ? null : pageTitle.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath == null ? null : picPath.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

	public String getAdminDisplayName() {
		return adminDisplayName;
	}

	public void setAdminDisplayName(String adminDisplayName) {
		this.adminDisplayName = adminDisplayName;
	}
    
}