package com.xt.cfp.core.service.message;


import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.xt.cfp.core.util.Sign;

/**
 * Created by wangxudong  2015/8/17.
 */
public class WechatMessageBody extends MessageBody {
	private static Logger logger = Logger.getLogger(WechatMessageBody.class);
	//微信基本信息
	private String openId;
	private String access_token;
	//回款和提现信息
	private int sectionCode; 
	private BigDecimal balance;
	private BigDecimal shouldCapital2; 
	private BigDecimal shouldInterest2;
	private String flag;
	private String bankNo;
	private String date;
	//财富劵信息
	private String voucherName;
	private String endDate;
	private Integer count;
	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getShouldCapital2() {
		return shouldCapital2;
	}

	public void setShouldCapital2(BigDecimal shouldCapital2) {
		this.shouldCapital2 = shouldCapital2;
	}

	public BigDecimal getShouldInterest2() {
		return shouldInterest2;
	}

	public void setShouldInterest2(BigDecimal shouldInterest2) {
		this.shouldInterest2 = shouldInterest2;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public int getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(int sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getVoucherName() {
		return voucherName;
	}

	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public void handle() {
		if("0".equals(flag)){
			//回款
			Sign.sendWechatRepaymenMsg(openId, sectionCode, balance, shouldCapital2, shouldInterest2, access_token);
		}else if("1".equals(flag)){
			//提现
			Sign.sendWechatWithDrawMsg(openId, bankNo, balance, date, access_token);
		}else if("2".equals(flag)){
			//财富劵到账
			Sign.sendWechatVoucherMsg(openId, "恭喜您获得一张财富派优惠券。", "财富券可在投资时抵扣等额现金，登录财富派微信公众号查看使用详情。", voucherName+"财富劵", endDate, access_token);
		}else if("3".equals(flag)){
			//提现劵到账
			Sign.sendWechatVoucherMsg(openId, "恭喜您获得一张财富派提现券。", "提现券可在提现时抵扣手续费，登录财富派微信公众号查看使用详情。", voucherName, endDate, access_token);
		}else if("4".equals(flag)){
			//财富劵到期提醒
			Sign.sendWechatVoucherExpireMsg(openId, "您有1张财富券将在3天后过期，面额"+balance+"元，不要浪费哦！", "登录财富派微信公众号查看使用详情。", "可用于财富派平台投资", "投资时直接抵扣现金", access_token);
		}else if("5".equals(flag)){
			//提现劵到期提醒
			Sign.sendWechatVoucherExpireMsg(openId, "您有1张提现券将在3天后过期，不要浪费哦！", "登录财富派微信公众号查看使用详情。", "可用于财富派平台提现", "提现时抵扣手续费", access_token);
		}else if("6".equals(flag)){
			//回款(提前回款)
			Sign.sendWechatEarlyRepaymenMsg(openId, sectionCode, balance, shouldCapital2, shouldInterest2, access_token);
		}else if ("7".equals(flag)) {
			//加息券领取成功通知
			Sign.sendWechatVoucherMsg(openId, "恭喜您获得一张财富派加息券。", "加息券可在投资时增加年化收益率，登录财富派微信公众号查看使用详情。", voucherName, endDate, access_token);
		}else if ("8".equals(flag)) {
			//加息券到期提示通知
			Sign.sendWechatVoucherExpireMsg(openId, "您有"+count+"张加息券即将到期，不要浪费哦！", "登录财富派微信公众号查看使用详情。", "可用于财富派平台投资", "可增加年化收益率", access_token);
		}	
	}
}
