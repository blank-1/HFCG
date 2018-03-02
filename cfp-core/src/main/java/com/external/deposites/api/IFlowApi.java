package com.external.deposites.api;

/**
 * <pre>
 * 工作流接口
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/22
 */
public interface IFlowApi {

    /**
     * 执行流并结束
     *
     * @param responseClass 返回值类型
     * @param <T>           实际返回值
     * @return 返回入参对像
     */
    <T> T merge(Class responseClass);

}
