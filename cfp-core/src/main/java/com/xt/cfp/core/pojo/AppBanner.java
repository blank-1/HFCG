package com.xt.cfp.core.pojo;

import java.util.Date;

public class AppBanner {
    private Long appBannerId;//bannerId
    private String appType;//平台
    private Long orderBy;//banner顺序
    private String bannerName;//活动名称
    private String httpUrl;//banner跳转地址
    private String httpMethod;//请求方式
    private String httpIsToken;//是否传UserToken
    private String title;//分享标题
    private String desc;//分享文案
    private String link;//分享链接
    private String shareCloseUrl;//监控分享链接
    private String closeUrl;//监控活动关闭链接
    private String shareBackUrl;//分享结果回调地址
    private String imageSrc;//banner图片
    private String physicsImageSrc;//banner图片物理
    private String imgUrl;//分享小图
    private String physicsImgUrl;//分享小图物理
    private String state;//状态
    private Date updateTime;//最后更改时间
    private Long adminId;//最后更改人
    //微信字段
    private char publishState ;//发布状态1.立即，0.定时
    private Date publishDate;//发布时间
    
    //辅助字段
    private String adminDisplayName;//管理员名称
    
    public char getPublishState() {
		return publishState;
	}

	public void setPublishStatus(char publishState) {
		this.publishState = publishState;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Long getAppBannerId() {
        return appBannerId;
    }

    public void setAppBannerId(Long appBannerId) {
        this.appBannerId = appBannerId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType == null ? null : appType.trim();
    }

    public Long getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Long orderBy) {
        this.orderBy = orderBy;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName == null ? null : bannerName.trim();
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl == null ? null : httpUrl.trim();
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod == null ? null : httpMethod.trim();
    }

    public String getHttpIsToken() {
        return httpIsToken;
    }

    public void setHttpIsToken(String httpIsToken) {
        this.httpIsToken = httpIsToken == null ? null : httpIsToken.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
    }

    public String getShareCloseUrl() {
        return shareCloseUrl;
    }

    public void setShareCloseUrl(String shareCloseUrl) {
        this.shareCloseUrl = shareCloseUrl == null ? null : shareCloseUrl.trim();
    }

    public String getCloseUrl() {
        return closeUrl;
    }

    public void setCloseUrl(String closeUrl) {
        this.closeUrl = closeUrl == null ? null : closeUrl.trim();
    }

    public String getShareBackUrl() {
        return shareBackUrl;
    }

    public void setShareBackUrl(String shareBackUrl) {
        this.shareBackUrl = shareBackUrl == null ? null : shareBackUrl.trim();
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc == null ? null : imageSrc.trim();
    }

    public String getPhysicsImageSrc() {
        return physicsImageSrc;
    }

    public void setPhysicsImageSrc(String physicsImageSrc) {
        this.physicsImageSrc = physicsImageSrc == null ? null : physicsImageSrc.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getPhysicsImgUrl() {
        return physicsImgUrl;
    }

    public void setPhysicsImgUrl(String physicsImgUrl) {
        this.physicsImgUrl = physicsImgUrl == null ? null : physicsImgUrl.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
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