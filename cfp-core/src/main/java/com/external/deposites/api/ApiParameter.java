package com.external.deposites.api;

import com.external.deposites.model.AbstractSignature;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 接口field参数
 * 作用：
 * 1、字段参与签名(sign)
 * 2、字段需要发送给接口(http[s] parameter)
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/17
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParameter {

    /**
     * 是否要忽略此字段的签名
     * @see AbstractSignature#addIgnoreSignFields(java.lang.String...)
     */
    boolean ignoreSign() default false;

}
