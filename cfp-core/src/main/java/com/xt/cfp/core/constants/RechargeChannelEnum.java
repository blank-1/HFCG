package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public enum RechargeChannelEnum implements EnumsCanDescribed {

	DEDUCTION ("划扣","0") ,
	TRANSFER ("转账","1") ,
	CASH ("现金","2") ,
	YBTZT ("易宝快捷支付","YB_TZT") ,
	YBEBK ("易宝网银支付","YB_EBK") ,
	FUIOU_POS ("富友POS刷卡","FUIOU_POS") ,
	LL_AUTHPAY ("连连认证支付","LL_AUTHPAY") ,
	LL_GATEPAY ("连连网银支付","LL_GATEPAY") ,
	HF_AUTHPAY ("恒丰快捷支付","HF_AUTHPAY") ,
	HF_GATEPAY ("恒丰网银支付","HF_GATEPAY") ,
	;
	
	private String desc ;
	
	private String value ;
	
	RechargeChannelEnum (String desc , String value ) {
		this.desc = desc ;
		this.value = value ;
	}
	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public String getValue() {
		return value;
	}

}
