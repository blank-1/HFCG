package com.external.deposites.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 校验规则，用在接口field上时，这些参数就会被进行基本的值验证处理
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/17
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validated {

    /**
     * 条件类型
     *
     * @return
     */
    HfValidations.BasicValidation[] type();

    /**
     * 校验失败信息
     */
    String message() default "";


}
