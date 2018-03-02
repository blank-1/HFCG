package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by lenovo on 2015/6/18.
 */
public enum UserSource implements EnumsCanDescribed {
    PLATFORM("0","平台"),
    WEB("1","web"),
    ISO("2","ISO"),
    ANDROID("3","安卓"),
    WECHAT("4","微信公众平台"),
    SALES_PLANTFORM("5","电话销售平台"),
    ;


    private final String value;
    private final String desc;

    UserSource(String value, String desc) {
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

    public static UserSource getBydesc(String desc) {
        for (UserSource userSource : UserSource.values()) {
            if (userSource.getDesc().equals(desc)) {
                return userSource;
            }
        }
        return null;
    }

}
