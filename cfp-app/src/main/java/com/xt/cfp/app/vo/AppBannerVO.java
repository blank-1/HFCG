package com.xt.cfp.app.vo;

public class AppBannerVO {
	private String show;//是否显示
    private String imageSrc;//banner图片
    private String httpUrl;//banner跳转地址
    private ShareInfo shareInfo;//分享信息
    
	public String getShow() {
		return show;
	}
	public void setShow(String show) {
		this.show = show;
	}
	public String getImageSrc() {
		return imageSrc;
	}
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
	public String getHttpUrl() {
		return httpUrl;
	}
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	public ShareInfo getShareInfo() {
		return shareInfo;
	}
	public void setShareInfo(ShareInfo shareInfo) {
		this.shareInfo = shareInfo;
	}
    
}
