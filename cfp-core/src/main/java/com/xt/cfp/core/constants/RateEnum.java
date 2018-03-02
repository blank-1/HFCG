package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-27.
 */
public class RateEnum{
	
	/**
	 * 总状态（0未使用；1使用中；2使用完；3已过期）
	 * */
	public enum RateUserStatusEnum implements EnumsCanDescribed {
		UNUSED("0","未使用"),
	    USING("1","使用中"),
	    USEUP("2","使用完"),
	    TIMEOUT("3","已过期"),
	    ;


	    private final String value;
	    private final String desc;

	    private RateUserStatusEnum(String value, String desc) {
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

	    public char value2Char() {
	        return value.charAt(0);
	    }
	    
	    public static RateUserStatusEnum getRateUserStatusEnumByValue(String value){
	    	RateUserStatusEnum[] rateUserStatusEnum = RateUserStatusEnum.values();
	        for (RateUserStatusEnum rus : rateUserStatusEnum){
	            if (rus.getValue().equals(value))
	                return rus;
	        }
	        return null;
	    }
	}
	
	/**
	 * 加息券来源（0.平台奖励；1.其他；2.投标奖励）
	 * */
	public enum RateUserSourceEnum implements EnumsCanDescribed {
		PLAT_AWARD("0","平台奖励"),
		BIDDING_AWARD("2","投标奖励"),
		OTHER("1","其他"),
		;
		
		
		private final String value;
		private final String desc;
		
		private RateUserSourceEnum(String value, String desc) {
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
		
		public char value2Char() {
			return value.charAt(0);
		}
	}
	/**
	 * 状态（0有效；1.无效）
	 * */
	public enum RateUsageHisStateEnum implements EnumsCanDescribed {
		VALID("0","有效"),
		UN_VALID("1","无效"),
		;
		
		
		private final String value;
		private final String desc;
		
		private RateUsageHisStateEnum(String value, String desc) {
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
		
		public char value2Char() {
			return value.charAt(0);
		}
	}
	
	/**
	 * 使用场景（1所有；2投标；3购买省心计划）
	 * */
	public enum RateProductScenarioEnum implements EnumsCanDescribed {
		ALL("1","所有"),
		BIDDING("2","投标"),
		FINANCE("3","购买省心计划"),
		;
		
		
		private final String value;
		private final String desc;
		
		private RateProductScenarioEnum(String value, String desc) {
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
		
		public char value2Char() {
			return value.charAt(0);
		}
		
		public static RateProductScenarioEnum getRateProductScenarioEnumByValue(String value){
			RateProductScenarioEnum[] rateProductScenarioEnum = RateProductScenarioEnum.values();
	        for (RateProductScenarioEnum rpse : rateProductScenarioEnum){
	            if (rpse.getValue().equals(value))
	                return rpse;
	        }
	        return null;
	    }
	}
	/**
	 * 叠加财富券（0.不可以；1.可以）
	 * */
	public enum RateProductIsOverlayEnum implements EnumsCanDescribed {
		DISABLED("0","不可以"),
		ABLED("1","可以"),
		;
		
		
		private final String value;
		private final String desc;
		
		private RateProductIsOverlayEnum(String value, String desc) {
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
		
		public char value2Char() {
			return value.charAt(0);
		}
	}

	/**
	 * 状态（0.有效；1.已使用；2.已过期；3.无效）
	 * */
	public enum RateProductStatusEnum implements EnumsCanDescribed {
		VALID("0", "有效"), HAS_USED("1", "已使用"), TIME("2", "已过期"), UN_VALID("3", "无效");

		private final String value;
		private final String desc;

		private RateProductStatusEnum(String value, String desc) {
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

		public char value2Char() {
			return value.charAt(0);
		}
		
	    public static RateProductStatusEnum getRateProductStatusEnumByValue(String value){
	    	RateProductStatusEnum[] rateProductStatusEnums = RateProductStatusEnum.values();
	        for (RateProductStatusEnum rps :rateProductStatusEnums){
	            if (rps.getValue().equals(value))
	                return rps;
	        }
	        return null;
	    }

	}
	
	/**
	 * 加息类型（0.投标奖励；1.加息券,2.活动奖励）
	 * */
	public enum RateLendOrderTypeEnum implements EnumsCanDescribed {
		AWARD("0", "投标奖励"),
		RATE_COUPON("1", "加息券"), 
		ACTIVITY("2", "活动奖励"), 
		;
		
		private final String value;
		private final String desc;
		
		private RateLendOrderTypeEnum(String value, String desc) {
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
		
		public char value2Char() {
			return value.charAt(0);
		}
		
	}
	/**
	 * 奖励时机（1.放款；2.周期还款；3.结清）
	 * */
	public enum RateLendOrderPointEnum implements EnumsCanDescribed {
		MAKE_LOAN("1", "放款"),
		CYCLE_RAPAYMENT("2", "周期还款"), 
		SETTLE("3", "结清"), 
		;
		
		private final String value;
		private final String desc;
		
		private RateLendOrderPointEnum(String value, String desc) {
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
		
		public char value2Char() {
			return value.charAt(0);
		}
		
	}
	
	/**
	 * 状态（0.有效；1.无效）
	 * */
	public enum RateLendOrderStatusEnum implements EnumsCanDescribed {
		VALID("0", "有效"),
		UN_VALID("1", "无效"), 
		;
		
		private final String value;
		private final String desc;
		
		private RateLendOrderStatusEnum(String value, String desc) {
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
		
		public char value2Char() {
			return value.charAt(0);
		}
		
	}
	
}
