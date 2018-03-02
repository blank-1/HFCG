package com.external.deposites.exception;

/**
 * <pre>
 * 未明确定义
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/15
 */
public class UnidentifiedException extends RuntimeException {
    public UnidentifiedException() {
        this("未明确定义的参数！");
    }

    public UnidentifiedException(String message) {
        super(message);
    }

    public UnidentifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnidentifiedException(Throwable cause) {
        super(cause);
    }

    public UnidentifiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
