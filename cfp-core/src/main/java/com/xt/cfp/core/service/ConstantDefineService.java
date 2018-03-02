package com.xt.cfp.core.service;

import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.util.Pagination;

public interface ConstantDefineService {

	List<ConstantDefine> findAll();

    Pagination<ConstantDefine> findAllByPage(int page, int limit, ConstantDefine constantDefine);

    void addConstantDefine(ConstantDefine constantDefine);

    ConstantDefine findById(long constantDefineId);

    List<ConstantDefine> findsById(long constantDefineId);

    List<ConstantDefine> getConstantDefinesByType(String typeCode);

    /**
     * 处理费用类别数据回显
     *
     * @param constantValue
     * @return
     */
    ConstantDefine doFeesTypeEcho(String constantValue);

    void updateConstantDefine(ConstantDefine constantDefine);

    ConstantDefine findConstantByTypeCodeAndValue(ConstantDefine constantDefine);

    List<ConstantDefine> findByTypeCodeAndParentConstant(String constantTypeCode, long parentConstant);


    Pagination<ConstantDefine> getConstantDefinePaging(int pageNo, int pageSize, ConstantDefine constantDefine, Map<String, Object> customParams);

    /**
     * 根据父子节点的常量值确定子节点
     * @param constantTypeCode
     * @param key
     * @param parentkey
     * @return
     */
    ConstantDefine findByTypeCodeAndValueAndParentValue(String constantTypeCode, String key, String parentkey);

	List<ConstantDefine> getConstantDefinesByTypeNeedSort(String typeCode);
}
