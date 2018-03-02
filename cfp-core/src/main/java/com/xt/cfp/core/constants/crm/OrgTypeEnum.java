package com.xt.cfp.core.constants.crm;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public enum OrgTypeEnum implements EnumsCanDescribed {
	ORG_TYPE("0", "crmType"), 
	ORG_TYPE_AREA("0", "crmArea"), 
	ORG_TYPE_CITY("1", "crmCity"),
	ORG_TYPE_FRANC("2", "crmFranc"),
	ORG_TYPE_COMP("3", "crmComp"),
	ORG_TYPE_SHOP("4", "crmShop"),
	ORG_TYPE_GROUP("5", "crmGroup"),
	ORG_TYPE_PEOPLE("6", "crmPeople"),
	;

	private final String value;
	private final String desc;

	private OrgTypeEnum(String value, String desc) {
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
