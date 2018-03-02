package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public class DisActivityEnums {

	public enum DisActivityStateEnum implements EnumsCanDescribed {

		STATE_NO("1", "无效"), 
		STATE_PUBLISH("2","发布"),
		STATE_DISPUBLISH("3","未发布"),
		;

		private final String value;
		private final String desc;

		DisActivityStateEnum(String value, String desc) {
			this.value = value;
			this.desc = desc;
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
	
	public enum DarCommiPaidNodeEnum implements EnumsCanDescribed {

		MAKELOAN("1", "放款"), 
		REPAYMENT("2", "还款"), ;

		private final String value;
		private final String desc;

		DarCommiPaidNodeEnum(String value, String desc) {
			this.value = value;
			this.desc = desc;
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
	
	public enum DistributionInviteEnum implements EnumsCanDescribed {

		STATE_YES("0", "有效"), 
		STATE_NO("1", "无效"), ;

		private final String value;
		private final String desc;

		DistributionInviteEnum(String value, String desc) {
			this.value = value;
			this.desc = desc;
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
	
	public enum DistribututionLevelEnum implements EnumsCanDescribed {
		LEVEL_FIRST("1", "1级"), 
		LEVEL_SECOND("2", "2级"), 
		LEVEL_THIRD("3", "3级"), ;
	
		private final String value;
		private final String desc;
	
		DistribututionLevelEnum(String value, String desc) {
			this.value = value;
			this.desc = desc;
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
	
	public enum DarCommiRatioTypeEnum implements EnumsCanDescribed {
		//11,-全部本金,12-全部利息,13-全部利息和本金,21-周期还款利息,22-周期还款本金,23-周期还款本息,
		ALL_BALANCE("11", "全部本金"), 
		ALL_INTEREST("12", "全部利息"), 
		ALL_BALANCE_AND_INTEREST("13", "全部利息和本金"), 
		CYCLE_INTEREST("21", "周期还款利息"), 
		CYCLE_BALANCE("22", "周期还款本金"), 
		CYCLE_BALANCE_AND_INTEREST("23", "周期还款本息"), 
		;
		private final String value;
		private final String desc;

		DarCommiRatioTypeEnum(String value, String desc) {
			this.value = value;
			this.desc = desc;
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

	
	public enum WhiteTabsSourceEnum implements EnumsCanDescribed {

		SOURCE_IMPORT("0", "导入"), 
		SOURCE_INVITE("1", "邀请"), ;

		private final String value;
		private final String desc;

		WhiteTabsSourceEnum(String value, String desc) {
			this.value = value;
			this.desc = desc;
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
	public enum InviteWhiteTabsTypeEnum implements EnumsCanDescribed {
		
		TYPE_IMPORT("0", "参与佣金"), 
		TYPE_INVITE("1", "销售"), ;
		
		private final String value;
		private final String desc;
		
		InviteWhiteTabsTypeEnum(String value, String desc) {
			this.value = value;
			this.desc = desc;
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
	
	/**
	 * 三级分销活动指定用户类型枚举
	 * 0-全部用户
	 * 1-平台用户
	 * 2-加盟销售（销售邀请进来的用户）
	 * */
	public enum DisAcivityUserTypeEnum implements EnumsCanDescribed{
		ALL_USER("0","所有用户"),
		PLAT_USER("1","平台用户"),
		SALE_INVITE_USRE("2","加盟销售"),
		;
		private final  String value ;
		private final String desc ;
		DisAcivityUserTypeEnum (String value , String desc){
			this.value = value ;
			this.desc = desc ;
		}
		
		@Override
		public String getDesc(){
			return desc ;
		}
		
		@Override
		public String getValue(){
			return value ;
		}
	}
	
}
