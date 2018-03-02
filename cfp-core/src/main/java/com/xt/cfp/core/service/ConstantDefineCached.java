package com.xt.cfp.core.service;

import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.ConstantDefine;

public interface ConstantDefineCached {

    Map<String,Long> getSystemAccount();

    List<ConstantDefine> getAll();

    List<ConstantDefine> getByTypeCode(String typeCode);

    List<ConstantDefine> getByTypeCodeAndParent(String typeCode, long pConstantDefine);

    ConstantDefine getpConstantByChild(String typeCode, String typeValue);

    ConstantDefine getConstantByValue(String typeCode, String typeValue);

    void reset();
    
    /**
     * 获取费用子类型
     * @param 借款类型   1--借款  2--出借
     * @param 费用类别   1--违约  2--费用
     * */
    List<ConstantDefine> getFeesItemChildType(char lendType, char itemType);

}
