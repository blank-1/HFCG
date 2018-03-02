package com.xt.cfp.app.vo;


/**
 * 抵押车信息
 */
public class APPMortgageCarSnapshotVO {
	private String arrived;// 几抵
	private String automobileBrand;// 汽车品牌
	private String carModel;// 汽车型号
	private String marketPrice;// 市场价格
	private String frameNumber;// 车架号
	private String changeDesc;// 变更信息

	public String getArrived() {
		return arrived;
	}

	public void setArrived(String arrived) {
		this.arrived = arrived;
	}

	public String getAutomobileBrand() {
		return automobileBrand;
	}

	public void setAutomobileBrand(String automobileBrand) {
		this.automobileBrand = automobileBrand;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getFrameNumber() {
		return frameNumber;
	}

	public void setFrameNumber(String frameNumber) {
		this.frameNumber = frameNumber;
	}

	public String getChangeDesc() {
		return changeDesc;
	}

	public void setChangeDesc(String changeDesc) {
		this.changeDesc = changeDesc;
	}

}