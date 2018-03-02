package com.xt.cfp.core.pojo.ext;

/**
 * 加息券产品表，条件json字符串封装类
 */
public class RateProductConditionVO {
	
//	使用条件（0.无条件；）【condition0_nothing】
//	使用条件（1.标的期限；）【condition1_term】
//	使用条件（2.标的类型；）【condition2_type】
//	使用条件（3.起投金额）【condition3_amount】
//	1条件值:标的最小期限【con1_min】
//	1条件值:标的最大期限【con1_max】
//	2条件值:'0'信贷；【con2_0】
//	2条件值:'1'房贷；【con2_1】
//	2条件值:'2'企业车贷；【con2_2】
//	2条件值:'3'企业信贷；【con2_3】
//	2条件值:'4'企业保理；【con2_4】
//	2条件值:'5'基金；【con2_5】
//	2条件值:'6'企业标；【con2_6】
//	3条件值:起投金额【con3_start_amount】

	//实例json
//	{"condition0_nothing":true,"condition1_term":true,"con1_min":1,"con1_max":99,"condition2_type":true,"con2_0":true,"con2_1":true,"con2_2":true,"con2_3":true,"con2_4":true,"con2_5":true,"con2_6":true,"con3_start_amount":100,"condition3_amount":true}

	
	private boolean condition0_nothing;
	private boolean condition1_term;
	private boolean condition2_type;
	private boolean condition3_amount;
	private Integer con1_min = 0;
	private Integer con1_max = 0;
	private boolean con2_0;
	private boolean con2_1;
	private boolean con2_2;
	private boolean con2_3;
	private boolean con2_4;
	private boolean con2_5;
	private boolean con2_6;
	private Integer con3_start_amount = 0;
	
	public boolean isCondition0_nothing() {
		return condition0_nothing;
	}

	public void setCondition0_nothing(boolean condition0_nothing) {
		this.condition0_nothing = condition0_nothing;
	}

	public boolean isCondition1_term() {
		return condition1_term;
	}

	public void setCondition1_term(boolean condition1_term) {
		this.condition1_term = condition1_term;
	}

	public boolean isCondition2_type() {
		return condition2_type;
	}

	public void setCondition2_type(boolean condition2_type) {
		this.condition2_type = condition2_type;
	}

	public boolean isCondition3_amount() {
		return condition3_amount;
	}

	public void setCondition3_amount(boolean condition3_amount) {
		this.condition3_amount = condition3_amount;
	}

	public Integer getCon1_min() {
		return con1_min;
	}

	public void setCon1_min(Integer con1_min) {
		this.con1_min = con1_min;
	}

	public Integer getCon1_max() {
		return con1_max;
	}

	public void setCon1_max(Integer con1_max) {
		this.con1_max = con1_max;
	}

	public boolean isCon2_0() {
		return con2_0;
	}

	public void setCon2_0(boolean con2_0) {
		this.con2_0 = con2_0;
	}

	public boolean isCon2_1() {
		return con2_1;
	}

	public void setCon2_1(boolean con2_1) {
		this.con2_1 = con2_1;
	}

	public boolean isCon2_2() {
		return con2_2;
	}

	public void setCon2_2(boolean con2_2) {
		this.con2_2 = con2_2;
	}

	public boolean isCon2_3() {
		return con2_3;
	}

	public void setCon2_3(boolean con2_3) {
		this.con2_3 = con2_3;
	}

	public boolean isCon2_4() {
		return con2_4;
	}

	public void setCon2_4(boolean con2_4) {
		this.con2_4 = con2_4;
	}

	public boolean isCon2_5() {
		return con2_5;
	}

	public void setCon2_5(boolean con2_5) {
		this.con2_5 = con2_5;
	}

	public boolean isCon2_6() {
		return con2_6;
	}

	public void setCon2_6(boolean con2_6) {
		this.con2_6 = con2_6;
	}

	public Integer getCon3_start_amount() {
		return con3_start_amount;
	}

	public void setCon3_start_amount(Integer con3_start_amount) {
		this.con3_start_amount = con3_start_amount;
	}
	
}
