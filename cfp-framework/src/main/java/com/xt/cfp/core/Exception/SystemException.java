package com.xt.cfp.core.Exception;

import com.xt.cfp.core.Exception.code.ErrorCode;
import jodd.util.PrettyStringBuilder;
import org.apache.commons.lang.StringUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final Map<String, Object> properties = new TreeMap<String, Object>();
    private ErrorCode errorCode;

    public SystemException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.errorCode = errorCode;
    }

    public SystemException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SystemException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public SystemException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public static SystemException wrap(Throwable exception, ErrorCode errorCode, String customMessage) {
        String msg = StringUtils.isNotEmpty(customMessage) ? customMessage : errorCode.getDesc() + " >>>>>>>>> " + exception.getMessage();
        if (exception instanceof SystemException) {
            SystemException se = (SystemException) exception;
            if (errorCode != null && errorCode != se.getErrorCode()) {
                return new SystemException(msg, exception, errorCode);
            }
            return se;
        } else {
            return new SystemException(msg, exception, errorCode);
        }
    }

    public static SystemException wrap(Throwable exception, ErrorCode errorCode) {
        return wrap(exception, errorCode, errorCode.getDesc() + " >>>>>>>>> " + exception.getMessage());
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public SystemException setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) properties.get(name);
    }

    public SystemException set(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    public SystemException setAll(Map map) {
        properties.putAll(map);
        return this;
    }

    public void printStackTrace(PrintStream s) {
        synchronized (s) {
            printStackTrace(new PrintWriter(s));
        }
    }

    public void printStackTrace(PrintWriter s) {
        synchronized (s) {
            s.println(this);
            s.println("\t-------------------------------");
            if (errorCode != null) {
                s.println("\t" + errorCode + ":" + errorCode.getClass().getName());
            }
            for (String key : properties.keySet()) {
                s.println("\t" + key + "=" + PrettyStringBuilder.str(properties.get(key)));
            }
            s.println("\t-------------------------------");
            StackTraceElement[] trace = getStackTrace();
            for (int i = 0; i < trace.length; i++)
                s.println("\tat " + trace[i]);

            Throwable ourCause = getCause();
            if (ourCause != null) {
                ourCause.printStackTrace(s);
            }
            s.flush();
        }
    }

    /**
     * 获取基本的异常信息的描述
     * @return
     */
    public String getBasicDesc() {
        StringBuilder re = new StringBuilder();

        if (errorCode != null)
            re.append(errorCode.getDesc());

        for (String key : properties.keySet())
            re.append("--").append(key).append(":").append(properties.get(key));

        return re.toString();
    }

    /**
     * 获取详细异常信息的描述
     * @return
     */
    public String getDetailDesc() {
        StringBuilder re = new StringBuilder();

        re.append(System.getProperty("line.separator")).
                append("\t-------------------------------").
                append(System.getProperty("line.separator")).
                append("\t").append(getMessage()).
                append(System.getProperty("line.separator"));
        if (errorCode != null) {
            re.append("\t" + errorCode + ":" + errorCode.getClass().getName()).append(System.getProperty("line.separator"));
        }
        for (String key : properties.keySet()) {
            re.append("\t" + key + "=" + PrettyStringBuilder.str(properties.get(key))).append(System.getProperty("line.separator"));
        }
        re.append("\t-------------------------------").append(System.getProperty("line.separator"));
        StackTraceElement[] trace = getStackTrace();
        for (int i = 0; i < trace.length; i++) {
            re.append("\tat " + trace[i]).append(System.getProperty("line.separator"));
        }

        return re.toString();
    }

}
