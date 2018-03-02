package com.external.deposites.api;


import com.external.deposites.model.datasource.AbstractDataSource;

/**
 * <pre>
 * 校验条件类型
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/17
 */
public interface IConditionType {


    /**
     * 取条件类型名称
     */
    String getName();

    /**
     * 进行本条件项校验
     */
    ValidationResult validate(AbstractDataSource abstractDataSource);
}
