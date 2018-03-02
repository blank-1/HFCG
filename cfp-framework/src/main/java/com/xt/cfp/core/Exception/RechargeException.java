package com.xt.cfp.core.Exception;

/**
 * <pre>
 * 充值
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/7
 */
public class RechargeException extends Throwable {
    public RechargeException() {
    }

    public RechargeException(String message) {
        super(message);
    }

    public RechargeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RechargeException(Throwable cause) {
        super(cause);
    }

    public RechargeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
