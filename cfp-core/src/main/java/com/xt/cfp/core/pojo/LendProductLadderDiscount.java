package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品阶段优惠
 */
public class LendProductLadderDiscount {
	private long lpldId;
	private long lendProductId;
	private BigDecimal minAmount;
	private BigDecimal maxAmount;

	private List<LendProductLadderDiscountFees> lendProductLadderDiscountFeesList;

	public long getLpldId() {
		return lpldId;
	}

	public void setLpldId(long lpldId) {
		this.lpldId = lpldId;
	}

	public long getLendProductId() {
		return lendProductId;
	}

	public void setLendProductId(long lendProductId) {
		this.lendProductId = lendProductId;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public List<LendProductLadderDiscountFees> getLendProductLadderDiscountFeesList() {
		return lendProductLadderDiscountFeesList;
	}

	public void setLendProductLadderDiscountFeesList(List<LendProductLadderDiscountFees> lendProductLadderDiscountFeesList) {
		this.lendProductLadderDiscountFeesList = lendProductLadderDiscountFeesList;
	}
}
