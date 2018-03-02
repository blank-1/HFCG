package com.external.deposites.api;


import com.external.deposites.exception.UnqualifiedException;
import com.external.deposites.model.datasource.AbstractDataSource;

import java.util.List;

/**
 * <pre>
 * 校验接口，加载校验条件，并进行验证
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/15
 */
public interface IValidation {
    /**
     * 加载接口参数的校验条件
     */
    List<? extends IConditionType> loadConditions();

    /**
     * 执行校验
     *
     * @param dataSource 要校验的数据源
     * @param fastFail   一个条件不通过就停止校验
     * @throws UnqualifiedException
     */
    void validate(boolean fastFail, AbstractDataSource dataSource) throws UnqualifiedException;
}
