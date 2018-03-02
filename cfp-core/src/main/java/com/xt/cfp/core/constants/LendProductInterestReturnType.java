package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品返息周期
 * Created by yulei on 2015/7/3.
 */
public enum LendProductInterestReturnType implements EnumsCanDescribed {
    WEEK("1", "周"),
    MONTH("2", "月"),
    SEASON("3", "季"),
    HALF_YEAR("4", "半年"),
    YEAR("5", "年"),
    EXIT("6", "退出时"),
    ;

    LendProductInterestReturnType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String desc;
    private String value;

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    private static final List<LendProductInterestReturnType> INTEREST_RETURN_BY_CYCLE = new ArrayList<LendProductInterestReturnType>();
    private static final List<LendProductInterestReturnType> INTEREST_RETURN_WHEN_EXIT = new ArrayList<LendProductInterestReturnType>();

    static {
        INTEREST_RETURN_BY_CYCLE.add(WEEK);
        INTEREST_RETURN_BY_CYCLE.add(MONTH);
        INTEREST_RETURN_BY_CYCLE.add(SEASON);
        INTEREST_RETURN_BY_CYCLE.add(HALF_YEAR);
        INTEREST_RETURN_BY_CYCLE.add(YEAR);

        INTEREST_RETURN_WHEN_EXIT.add(EXIT);
    }

    /**
     * 获取周期返息的类型列表
     * @return
     */
    public static final List<LendProductInterestReturnType> createInterestReturnByCycleList() {
        List<LendProductInterestReturnType> typeList = new ArrayList<LendProductInterestReturnType>();
        typeList.addAll(INTEREST_RETURN_BY_CYCLE);
        return typeList;
    }

    /**
     * 获取退出返本息的类型列表
     * @return
     */
    public static final List<LendProductInterestReturnType> createInterestReturnWhenExitList() {
        List<LendProductInterestReturnType> typeList = new ArrayList<LendProductInterestReturnType>();
        typeList.addAll(INTEREST_RETURN_WHEN_EXIT);
        return typeList;
    }
}
