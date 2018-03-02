package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.xt.cfp.core.constants.LendProductPublishIsRecommendEnum;

/**
 * 
 */
public class LendProductPublish {

	private Long lendProductPublishId;
	private Long lendProductId;
	private String publishName;
	private String publishCode;
	private BigDecimal publishBalance;
	private char publishBalanceType;
	private BigDecimal soldBalance = BigDecimal.ZERO;
	private Date startDate;
	private Date endDate;
	private char publishState;
	private Date publishTime;
	private Date preheatTime;
	//省心计划推荐标识  1 -- 推荐  ， 0 -- 不推荐 LendProductPublishIsRecommendEnum
	private String recommend;
	private String startsAt;
	
	/***
	 * 	名字，收益率，封闭期类型和天数，期数，，发布状态是否售罄等，id,金额
		-->
		  select lpp.publish_name , lpp.publish_code,lp.closing_type,,lp.closing_date
 		  lp.profit_rate , lpp.publish_state as status ,lpp.lend_product_id,
          lpp.publish_balance-lpp.sold_balance as  money    from lend_product_publish lpp
          left join lend_product lp  on lp.lend_product_id =lpp.lend_product_id
          where lp.product_type=2 order by lpp.publish_time desc 	
	 */
	// 省心计划收益率
	private String profitRate;
	private String profitRateMax;
	// 省心计划 剩余金额
	private BigDecimal money;
	// 省心计划封闭期
	private String closingDate;
	// 省心计划封闭期类型
	private String closingType; 
    public Boolean annRateOrder;
    //期限排序排序
    public Boolean durationOrder;
    //信用等级排序
    public String getOrderBy(){
        String orderBy = "";
        String str1 = annRateOrder==null?null:(  annRateOrder?"  lp.profit_rate  asc":"  lp.profit_rate   desc");
        String str2 = durationOrder==null?null:(durationOrder?" lp.time_limit asc":" lp.time_limit desc");
        orderBy += str1==null?"":str1;
        orderBy += str2==null?"":(StringUtils.isEmpty(orderBy)?str2:","+str2);
        return orderBy;
}
	
	
	public String getStartsAt() {
		return startsAt;
	}


	public void setStartsAt(String startsAt) {
		this.startsAt = startsAt;
	}


	public String getClosingType() {
		return closingType;
	}

	public Boolean getAnnRateOrder() {
		return annRateOrder;
	}

	public void setAnnRateOrder(Boolean annRateOrder) {
		this.annRateOrder = annRateOrder;
	}

	public Boolean getDurationOrder() {
		return durationOrder;
	}

	public void setDurationOrder(Boolean durationOrder) {
		this.durationOrder = durationOrder;
	}

	public void setClosingType(String closingType) {
		this.closingType = closingType;
	}

	public String getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(String closingDate) {
		this.closingDate = closingDate;
	}

	public String getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(String profitRate) {
		this.profitRate = profitRate;
	}
	
	public String getProfitRateMax() {
		return profitRateMax;
	}

	public void setProfitRateMax(String profitRateMax) {
		this.profitRateMax = profitRateMax;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	private List<LPPublishChannelDetail> publishChannelDetails;

	private LendProduct lendProduct;

	public LendProduct getLendProduct() {
		return lendProduct;
	}

	public void setLendProduct(LendProduct lendProduct) {
		this.lendProduct = lendProduct;
	}

	/**
	 * 发售金额类型：1为匹配当前债权，2为指定金额
	 */
	public static char PUBLISHBALANCETYPE_RIGHTS = '1';
	public static char PUBLISHBALANCETYPE_SPEC = '2';



	public List<LPPublishChannelDetail> getPublishChannelDetails() {
		return publishChannelDetails;
	}

	public void setPublishChannelDetails(List<LPPublishChannelDetail> publishChannelDetails) {
		this.publishChannelDetails = publishChannelDetails;
	}

	public char getPublishBalanceType() {
		return publishBalanceType;
	}

	public void setPublishBalanceType(char publishBalanceType) {
		this.publishBalanceType = publishBalanceType;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Long getLendProductPublishId() {
		return lendProductPublishId;
	}

	public void setLendProductPublishId(Long lendProductPublishId) {
		this.lendProductPublishId = lendProductPublishId;
	}

	public Long getLendProductId() {
		return lendProductId;
	}

	public void setLendProductId(Long lendProductId) {
		this.lendProductId = lendProductId;
	}

	public String getPublishName() {
		return publishName;
	}

	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}

	public String getPublishCode() {
		return publishCode;
	}

	public void setPublishCode(String publishCode) {
		this.publishCode = publishCode;
	}

	public BigDecimal getPublishBalance() {
		return publishBalance;
	}

	public void setPublishBalance(BigDecimal publishBalance) {
		this.publishBalance = publishBalance;
	}

	public BigDecimal getSoldBalance() {
		return soldBalance;
	}

	public void setSoldBalance(BigDecimal soldBalance) {
		this.soldBalance = soldBalance;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public char getPublishState() {
		return publishState;
	}

	public void setPublishState(char publishState) {
		this.publishState = publishState;
	}

	public Date getPreheatTime() {
		return preheatTime;
	}

	public void setPreheatTime(Date preheatTime) {
		this.preheatTime = preheatTime;
	}


	public String getRecommend() {
		return recommend;
	}


	public void setRecommend(String recommend) {
		if(StringUtils.isBlank(recommend)){
			this.recommend = LendProductPublishIsRecommendEnum.DISRECOMMEND.getValue();
		}else{
			this.recommend = recommend;
		}
	}


}
