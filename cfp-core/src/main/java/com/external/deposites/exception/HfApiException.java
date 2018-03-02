package com.external.deposites.exception;

/**
 * <pre>
 * 对外接口异常,所有存管接口都在有这个异常
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/24
 */
public class HfApiException extends Throwable {
    public HfApiException() {
    }

    public HfApiException(String message) {
        super(message);
    }

    public HfApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public HfApiException(Throwable cause) {
        super(cause);
    }

    public HfApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
