package com.xt.cfp.core.util;

import com.alibaba.druid.support.json.JSONUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class AccessLogUtil {
    private static final String SESSION_COOKIE_NAME = "JSESSIONID";
    public static final String SESSION_USER_ID_KEY = "__UID__";
    public static final String ACCESS_LOG_KEY = "__ACCESS__";
    public static final String STATUS_KEY = "access.log.status";
    public static final String EXCEPTION_KEY = "access.log.exception";
    private static final String IGNORE_HEADER_NAMES = "User-Agent,X-Real-Ip,X-Forwarded-For,X-Forwarded-Proto,geo," +
            "Connection,Accept-Encoding,Cookie,Host," +
            "Pragma,Cache-Control,Accept-Language,Accept-Encoding,Referer";

    private static final Set<String> excludeHeaderNames = new HashSet<>(
            Arrays.asList(IGNORE_HEADER_NAMES.toUpperCase().split(","))
    );

    private static Logger logger = LoggerFactory.getLogger(AccessLogUtil.class);

    private static ThreadLocal<AccessLogUtil> holder = new ThreadLocal<AccessLogUtil>();

    private final HttpServletRequest request;
    private final DateTime startTime;
    private DateTime endTime;
    private Object uid;
    private Throwable ex;

    private HttpServletResponse response;

    public AccessLogUtil(HttpServletRequest request) {
        this.request = request;
        startTime = DateTime.now();
        request.setAttribute(ACCESS_LOG_KEY, this);

        holder.set(this);
    }

    public void end(HttpServletResponse response, Exception ex) {
        holder.set(null);

        this.response = response;
        if (request.getAttribute(EXCEPTION_KEY) != null) {
            ex = (Exception) request.getAttribute(EXCEPTION_KEY);
        }
        this.ex = ex;

        findUserId();
        endTime = DateTime.now();

        String json = toJson(false);
        logger.info(json);
    }

    private void findUserId() {
        this.uid = request.getAttribute(SESSION_USER_ID_KEY);
    }

    private HashMap<String, String> buildCookies() {
        Cookie[] httpCookies = request.getCookies();
        if (httpCookies == null) {
            return null;
        }

        HashMap<String, String> cookies = new HashMap<>(8);
        for (Cookie cookie : httpCookies) {
            cookies.put(cookie.getName(), cookie.getValue());
        }
        return cookies;
    }

    private String getSessionId() {
        String fromSession = request.getSession(false) == null ? null : request.getSession(false).getId();
        String fromCookie = null;
        Cookie cookie = getCookie(SESSION_COOKIE_NAME);
        if (cookie != null) {
            fromCookie = cookie.getValue();
        }
        if (StringUtils.equalsIgnoreCase(fromCookie, fromSession) || StringUtils.isBlank(fromCookie)) {
            return fromSession;
        } else {
            return fromSession + "," + fromCookie;
        }
    }

    private Cookie getCookie(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }


    private String toJson(boolean ignoreParam) {
        Map<String, Object> data = new HashMap<>();
        data.put("queryString", RequestUtil.getQueryString(request));
        data.put("startTime", startTime.toString());
        data.put("endTime", endTime.toString());
        //秒
        data.put("duration", (endTime.getMillis() - startTime.getMillis()) / 1000.0);
        data.put("ip", request.getRemoteAddr());
        data.put("serverIp", request.getLocalAddr());
        data.put("scheme", request.getScheme());
        data.put("method", request.getMethod());
        data.put("path", request.getRequestURI());
        data.put("sessionId", getSessionId());
        data.put("cookies", buildCookies());
        data.put("host", request.getHeader("Host"));
        data.put("referer", request.getHeader("Referer"));
        data.put("userAgent", request.getHeader("User-Agent"));
        if (!ignoreParam) {
            data.put("paramsString", RequestUtil.JSONString(request, "loginPwd", "loginName"));
            data.put("paramsArray", RequestUtil.JSONArray(request, "loginPwd", "loginName"));
        }
        data.put("thread", Thread.currentThread().getName());

        Object status = request.getAttribute(STATUS_KEY);
        if (status == null) {
            status = response.getStatus();
        }
        data.put("status", status);

        if (ex != null) {
            Map<String, Object> exceptionData = new HashMap<>();
            exceptionData.put("message", ex.getMessage());
            exceptionData.put("stackTrace", ex.getStackTrace());
            data.put("exception", exceptionData);
        }

        data.put("uid", uid);

        Map<String, String> validHeaders = extractValidHeaders(request);
        if (!validHeaders.isEmpty()) {
            data.put("headers", validHeaders);
        }

        return JsonUtil.json(data);
    }

    private Map<String, String> extractValidHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (excludeHeaderNames.contains(name.toUpperCase())) {
                continue;
            }
            String value = request.getHeader(name);
            headers.put(name, value);
        }
        return headers;
    }
}
