package com.external.deposites.api;


import com.external.deposites.exception.UnqualifiedException;

import java.util.List;

/**
 * <pre>
 * 迭代所有条件
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/16
 * @deprecated 暂时不用了
 */
public interface IForEach {
    /**
     * 迭代所有条件
     *
     * @param conditionsList 要验证的条件
     * @param fastFail       有错误就停止不再处理其它
     */
    void iterate(boolean fastFail, List<? extends IConditionType> conditionsList) throws UnqualifiedException;
}
