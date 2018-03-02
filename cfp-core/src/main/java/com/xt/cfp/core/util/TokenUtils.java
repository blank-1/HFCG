package com.xt.cfp.core.util;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.pojo.UserInfo;
import jodd.util.StringUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 为了防止用户的重复提交行为，而加入的token工具
 * Created by yulei on 2015/7/3.
 */
public final class TokenUtils {

    private static final Logger LOGGER = Logger.getLogger(TokenUtils.class);

    public static final String TOKEN_INSESSION_ATTRNAME = "REQUEST_TOKEN";
    public static final String TOKEN_NAME_PREFIX = "tk.";
    public static final String TOKEN_INPAGE_ATTRNAME = "token";

    /**
     * 生成新的UUID
     * @return
     */
    private static String createTokenString() {
        return UUID.randomUUID().toString();
    }

    /**
     * 从session中获取tokenMap
     * @param session
     * @return
     */
    private static Map<String, String> getTokenMapFromSession(HttpSession session) {
        return (Map<String, String>) session.getAttribute(TOKEN_INSESSION_ATTRNAME);
    }

    /**
     * 从request获取session，如果存在就返回，不存在就抛出异常
     * @param request
     * @return
     */
    private static HttpSession getSession(HttpServletRequest request) {
//        SecurityUtil.checkSession(request);
        return request.getSession();
    }

    private static void needRefreshSession(HttpSession session) {
        session.setAttribute("__changed__", UUID.randomUUID().toString());
    }

    /**
     * 如果发现用户的tokenMap长度太大，会将tokenMap清理至10个
     * @param tokenMap
     */
    private static void checkTokenMapTooBig(Map<String, String> tokenMap) {
        if (tokenMap.size() > 100) {
            LOGGER.info(LogUtils.createSimpleLog("检测到用户的tokenMap长度异常", "长度：" + tokenMap.size()));
            Set<String> keys = new HashSet<String>();
            keys.addAll(tokenMap.keySet());

            for (String key : keys) {
                tokenMap.remove(key);
                if (tokenMap.size() < 10)
                    break;
            }
        }
    }

    /**
     * 设定新的token
     * @param request
     */
    public static String setNewToken(HttpServletRequest request) {
        Map<String, String> tokenMap;
        HttpSession session = getSession(request);
        if (session.getAttribute(TOKEN_INSESSION_ATTRNAME) != null) {
            tokenMap = getTokenMapFromSession(session);
            checkTokenMapTooBig(tokenMap);
        } else {
            tokenMap = new ConcurrentHashMap<String, String>();
            session.setAttribute(TOKEN_INSESSION_ATTRNAME, tokenMap);
        }

        String tokenString = createTokenString();
        tokenMap.put(TOKEN_NAME_PREFIX + tokenString, tokenString);
        request.setAttribute(TOKEN_INPAGE_ATTRNAME, tokenString);
        needRefreshSession(session);
        return tokenString;
    }

    /**
     * 校验request中是否存在正确的token信息，如果不存在，就抛出异常
     * @param request
     */
    public static void validateToken(HttpServletRequest request) {
        HttpSession session = getSession(request);
        String tokenString = request.getParameter(TOKEN_INPAGE_ATTRNAME);
        if ("".equals(tokenString))
            throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST).set("userName", ((UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION)).getLoginName());

        Map<String, String> tokenMap = getTokenMapFromSession(session);
        if (tokenMap == null)
            throw new SystemException(ValidationErrorCode.ERROR_TOKEN).set("userName", ((UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION)).getLoginName())
                    .set("tokenMap",null)
                    .set("requestTolen",tokenString);

        if (tokenMap.get(TOKEN_NAME_PREFIX + tokenString) == null)
            throw new SystemException(ValidationErrorCode.ERROR_TOKEN)
                    .set("userName", session.getAttribute(Constants.USER_ID_IN_SESSION))
                    .set("sessionToken",tokenMap.get(TOKEN_NAME_PREFIX + tokenString))
                    .set("requestTolen",tokenString);

        tokenMap.remove(TOKEN_NAME_PREFIX + tokenString);
        needRefreshSession(session);
    }

}
