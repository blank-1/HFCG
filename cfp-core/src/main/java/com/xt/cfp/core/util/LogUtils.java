package com.xt.cfp.core.util;

import jodd.util.StringUtil;

import java.text.MessageFormat;


/**
 * 日志模板
 * User: yulei
 * Date: 14-2-11
 * Time: 下午12:22
 */
public class LogUtils {

    private static final String SPLIT_LINE = "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
    private static final String BR = System.getProperty("line.separator");
    private static final String TITLE_LINE = "********************************";

    private static String createTitle(String title) {
        return StringUtil.join(TITLE_LINE, " ", title, " ", TITLE_LINE);
    }

    private static String createLogPart(String title, String bodyTemplate) {
        return StringUtil.join(BR, SPLIT_LINE, BR ,
                createTitle(title), BR,
                bodyTemplate,
                BR, SPLIT_LINE, BR);
    }

    public final static String USER_LOGIN = createLogPart("用户登录", "检测到用户登录，用户名:{0}");
    public final static String USER_LOGIN_SUCCESS = createLogPart("用户登录成功", "用户名:{0}");
    public final static String USER_LOGIN_DEBUG = createLogPart("用户登录", "检测到用户登录，用户名:{0}，密码:{1}");

//    public static String createLogInfo(String template, Object... objects) {
//        return MessageFormat.format(template, objects);
//    }

    public static String createLogWithParams(String title, String logBodyTemplate, Object... objects) {
        return StringUtil.join(BR, SPLIT_LINE, BR,
                createTitle(title), BR,
                MessageFormat.format(logBodyTemplate, objects), BR,
                SPLIT_LINE, BR);
    }

    public static String createSimpleLog(String title, String logBody) {
        return StringUtil.join(BR, SPLIT_LINE, BR,
                createTitle(title), BR,
                logBody, BR,
                SPLIT_LINE, BR);
    }

}
