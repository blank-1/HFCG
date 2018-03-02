package com.external.deposites.exception;

/**
 * <pre>
 * 不合格
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/17
 */
public class UnqualifiedException extends Exception {
    public UnqualifiedException() {
    }

    public UnqualifiedException(String message) {
        super(message);
    }

    public UnqualifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnqualifiedException(Throwable cause) {
        super(cause);
    }

    public UnqualifiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
