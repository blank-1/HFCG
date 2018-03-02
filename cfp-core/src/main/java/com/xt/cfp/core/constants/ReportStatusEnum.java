package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public enum ReportStatusEnum implements EnumsCanDescribed{
	UP("UP","文件上传"),
	DOWN("DOWN","回盘核检"),
	OK("OK","报备结束");
	
	private final String value;
    private final String desc;

    private ReportStatusEnum(String value, String desc) {
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
