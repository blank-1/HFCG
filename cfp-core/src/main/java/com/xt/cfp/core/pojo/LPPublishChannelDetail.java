package com.xt.cfp.core.pojo;

import java.math.BigDecimal;

public class LPPublishChannelDetail {
	private long lpPublishChannelDetailId;
	private long lendProductPublishId;
	private char channel;
	private BigDecimal publishBalance;
	private char publishBalanceType;

	/**
	 * 1为金额，2为比例
	 */
	public static final char CHANNELBALANCETYPE_BALANCE = '1';
	public static final char CHANNELBALANCETYPE_RATIO = '2';

	public char getPublishBalanceType() {
		return publishBalanceType;
	}

	public void setPublishBalanceType(char publishBalanceType) {
		this.publishBalanceType = publishBalanceType;
	}

	public long getLpPublishChannelDetailId() {
		return lpPublishChannelDetailId;
	}

	public void setLpPublishChannelDetailId(long lpPublishChannelDetailId) {
		this.lpPublishChannelDetailId = lpPublishChannelDetailId;
	}

	public long getLendProductPublishId() {
		return lendProductPublishId;
	}

	public void setLendProductPublishId(long lendProductPublishId) {
		this.lendProductPublishId = lendProductPublishId;
	}

	public char getChannel() {
		return channel;
	}

	public void setChannel(char channel) {
		this.channel = channel;
	}

	public BigDecimal getPublishBalance() {
		return publishBalance;
	}

	public void setPublishBalance(BigDecimal publishBalance) {
		this.publishBalance = publishBalance;
	}
}
