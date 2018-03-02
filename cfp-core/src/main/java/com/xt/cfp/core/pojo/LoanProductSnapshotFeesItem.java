package com.xt.cfp.core.pojo;

public class LoanProductSnapshotFeesItem {
	private long loanProductSnapshotFeesItemId;
	private long loanProductSnapshotId;
	private char chargeCycle;
	private float workflowRatio;
	private String itemName;
	private String itemType;
	private float feesRate;
	private int radicesType;
	private String radiceLogic;
	private String radiceName;

	public long getLoanProductSnapshotFeesItemId() {
		return loanProductSnapshotFeesItemId;
	}

	public void setLoanProductSnapshotFeesItemId(long loanProductSnapshotFeesItemId) {
		this.loanProductSnapshotFeesItemId = loanProductSnapshotFeesItemId;
	}

	public long getLoanProductSnapshotId() {
		return loanProductSnapshotId;
	}

	public void setLoanProductSnapshotId(long loanProductSnapshotId) {
		this.loanProductSnapshotId = loanProductSnapshotId;
	}

	public char getChargeCycle() {
		return chargeCycle;
	}

	public void setChargeCycle(char chargeCycle) {
		this.chargeCycle = chargeCycle;
	}

	public float getWorkflowRatio() {
		return workflowRatio;
	}

	public void setWorkflowRatio(float workflowRatio) {
		this.workflowRatio = workflowRatio;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public float getFeesRate() {
		return feesRate;
	}

	public void setFeesRate(float feesRate) {
		this.feesRate = feesRate;
	}

	public int getRadicesType() {
		return radicesType;
	}

	public void setRadicesType(int radicesType) {
		this.radicesType = radicesType;
	}

	public String getRadiceLogic() {
		return radiceLogic;
	}

	public void setRadiceLogic(String radiceLogic) {
		this.radiceLogic = radiceLogic;
	}

	public String getRadiceName() {
		return radiceName;
	}

	public void setRadiceName(String radiceName) {
		this.radiceName = radiceName;
	}

}
