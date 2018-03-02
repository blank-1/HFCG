package com.xt.cfp.core.Exception;

import java.io.IOException;

/**
 * <pre>
 * 非捕获
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/22
 */
public class UnCatchedException extends RuntimeException {
    public UnCatchedException() {
    }

    public UnCatchedException(String message) {
        super(message);
    }

    public UnCatchedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnCatchedException(Throwable cause) {
        super(cause);
    }

    public UnCatchedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
