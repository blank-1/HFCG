package com.external.deposites.exception;

/**
 * <pre>
 * "需实现的" 异常
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/15
 */
public class UnimplementException extends RuntimeException {


    public UnimplementException(Class<?> targetClass) {
        this("此接口需要实现：" + targetClass.toString());
    }

    public UnimplementException() {
    }

    public UnimplementException(String message) {
        super(message);
    }

    public UnimplementException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnimplementException(Throwable cause) {
        super(cause);
    }

    public UnimplementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
