package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-8-1.
 */
public class AgreementEnum {
    public enum AgreementTypeEnum implements EnumsCanDescribed {
    LEND_AGREEMENT("1","出借协议"),
    TURN_AGREEMENT("2","债权转让协议"),
    ENTRUST_AGREEMENT("3","授权委托书"),
    ;


    private final String value;
    private final String desc;

    private AgreementTypeEnum(String value, String desc) {
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
    public enum AgreementStatusEnum implements EnumsCanDescribed {
        UNCREATE("1","未生成"),
        CREATED("2","已生成"),
        INVALID("3","已失效"),
        ;


        private final String value;
        private final String desc;

        private AgreementStatusEnum(String value, String desc) {
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
    
    public enum AreementCodeTypeEnum implements EnumsCanDescribed {
    	
    	PERSON_HOUSE_CREDITOR_LOAN("HJRD-PF-ZJK","个人房屋债权标借款"),
        PERSON_HOUSE_CREDITOR_ASSIGNMENT("HJRD-PF-ZZ","个人房屋债权标转让"),
        PERSON_HOUSE_LOAN_LOAN("HJRD-PF-JJK","个人房屋借款标借款"),
        
        PERSON_CREDIT_CREDITOR_LOAN("HJRD-PX-ZJK","个人信用债权标借款"),
        PERSON_CREDIT_CREDITOR_ASSIGNMENT("HJRD-PX-ZZ","个人信用债权标转让"),
        PERSON_CREDIT_LOAN_LOAN("HJRD-PX-JJK","个人信用借款标借款"),	
        
        COMPANY_CAR_CREDITOR_LOAN("HJRD-CC-ZJK","企业车贷债权标借款"),
        COMPANY_CAR_CREDITOR_ASSIGNMENT("HJRD-CC-ZZ","企业车贷债权标转让"),
        COMPANY_CAR_LOAN_LOAN("HJRD-CC-JJK","企业车贷借款标借款"),
        
        COMPANY_FACTORING_CREDITOR_LOAN("HJRD-CB-ZJK","企业保理债权标借款"),
        COMPANY_FACTORING_CREDITOR_ASSIGNMENT("HJRD-CB-ZZ","企业保理债权标转让"),
        COMPANY_FACTORING_LOAN_LOAN("HJRD-CB-JJK","企业保理借款标借款"),
        
        COMPANY_CREDIT_CREDITOR_LOAN("HJRD-CX-ZJK","企业信用借款标借款"),
        COMPANY_CREDIT_CREDITOR_ASSIGNMENT("HJRD-CX-ZZ","企业信用债权标转让"),
        COMPANY_CREDIT_LOAN_LOAN("HJRD-CX-JJK","企业信用借款标借款"),
        
        COMPANY_FOUN_CREDITOR_LOAN("HJRD-CF-ZJK","企业基金借款标借款"),
        COMPANY_FOUN_CREDITOR_ASSIGNMENT("HJRD-CF-ZZ","企业基金债权标转让"),
        COMPANY_FOUN_LOAN_LOAN("HJRD-CF-JJK","企业基金借款标借款"),
        
        COMPANY_PLEDGE_CREDITOR_LOAN("HJRD-CZ-ZJK","企业标债权标借款"),//企业质押标
        COMPANY_PLEDGE_CREDITOR_ASSIGNMENT("HJRD-CZ-ZZ","企业标债权标转让"),//企业质押标
        COMPANY_PLEDGE_LOAN_LOAN("HJRD-CZ-JJK","企业标借款标借款"),//企业质押标
        
        PERSON_DIRECT_HOUSE_LOAN_LOAN("HJRD-PDHLL-FZT","个人房产直投标借款"),//借款标，个人房产直投(借款及服务协议)
        PERSON_DIRECT_HOUSE_LOAN_ENTRUST("HJRD-PDHLE-FZT","个人房产直投标授权委托"),//借款标，个人房产直投(授权委托书)
        FINANCE_PLAN_PERMISSION("HJRD-SXJH","省心计划委托协议"),
        
        PERSON_CAR_CREDITOR_LOAN("HJRD-PC-ZJK","个人车贷债权标借款"),
        PERSON_CAR_CREDITOR_ASSIGNMENT("HJRD-PC-ZZ","个人车贷债权标转让"),
        PERSON_CAR_LOAN_LOAN("HJRD-PC-JJK","个人车贷借款标借款"),
		;
    	
        private AreementCodeTypeEnum(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

    	private final String value ;
    	private final String desc ;
		@Override
		public String getDesc() {
			return desc;
		}

		@Override
		public String getValue() {
			return value;
		}
    	
    }
    
    
	public enum FinancePlanAgreementStatusEnum implements EnumsCanDescribed {
		UNCREATE_AGREEMENT("1","未生成"),
		AVALID_AGREEMENT("2", "已生成"), 
		DISABLED_AGREEMENT("3", "已失效"),
		;

		private final String value;
		private final String desc;

		private FinancePlanAgreementStatusEnum(String value, String desc) {
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

