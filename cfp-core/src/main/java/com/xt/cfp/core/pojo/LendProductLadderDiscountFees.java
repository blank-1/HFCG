package com.xt.cfp.core.pojo;

import java.math.BigDecimal;

/**
 * 产品阶段优惠费用
 */
public class LendProductLadderDiscountFees {
	public long lpldfId;
	public long lpldId; // 阶段优惠ID
	public long lpfiId;// 产品相关费用ID
	public BigDecimal discountRate;

	private String itemName;
	private long feesItemId;
	private LendProductLadderDiscount lendProductLadderDiscount;

	public long getFeesItemId() {
		return feesItemId;
	}

	public void setFeesItemId(long feesItemId) {
		this.feesItemId = feesItemId;
	}

	public LendProductLadderDiscount getLendProductLadderDiscount() {
		return lendProductLadderDiscount;
	}

	public void setLendProductLadderDiscount(LendProductLadderDiscount lendProductLadderDiscount) {
		this.lendProductLadderDiscount = lendProductLadderDiscount;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public long getLpldfId() {
		return lpldfId;
	}

	public void setLpldfId(long lpldfId) {
		this.lpldfId = lpldfId;
	}

	public long getLpldId() {
		return lpldId;
	}

	public void setLpldId(long lpldId) {
		this.lpldId = lpldId;
	}

	public long getLpfiId() {
		return lpfiId;
	}

	public void setLpfiId(long lpfiId) {
		this.lpfiId = lpfiId;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}
}
