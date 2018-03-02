package com.xt.cfp.app.vo;

public class ShareInfo {
	private Long orderBy;//banner顺序
    private String bannerName;//活动名称
    private String httpMethod;//请求方式
    private String httpIsToken;//是否传UserToken
    private String title;//分享标题
    private String desc;//分享文案
    private String link;//分享链接
    private String imgUrl;//分享小图
    private String shareCloseUrl;//监控分享链接
    private String closeUrl;//监控活动关闭链接
    private String shareBackUrl;//分享结果回调地址
    
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
		this.bannerName = bannerName;
	}
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	public String getHttpIsToken() {
		return httpIsToken;
	}
	public void setHttpIsToken(String httpIsToken) {
		this.httpIsToken = httpIsToken;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getShareCloseUrl() {
		return shareCloseUrl;
	}
	public void setShareCloseUrl(String shareCloseUrl) {
		this.shareCloseUrl = shareCloseUrl;
	}
	public String getCloseUrl() {
		return closeUrl;
	}
	public void setCloseUrl(String closeUrl) {
		this.closeUrl = closeUrl;
	}
	public String getShareBackUrl() {
		return shareBackUrl;
	}
	public void setShareBackUrl(String shareBackUrl) {
		this.shareBackUrl = shareBackUrl;
	}
    
}
