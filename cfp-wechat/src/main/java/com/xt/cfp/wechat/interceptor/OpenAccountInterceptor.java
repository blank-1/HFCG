package com.xt.cfp.wechat.interceptor;

import com.xt.cfp.core.constants.UserIsVerifiedEnum;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 是否需要引道去开户
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/18
 */
public class OpenAccountInterceptor extends HandlerInterceptorAdapter {

    public final static String OPEN_ACCOUNT_NOTICE_KEY = "__open_account_notice__";
    /**
     * 要拦截的url
     */
    private List<String> urls = new ArrayList<>();
    /**
     * 引向的地址
     */
    private String destination;

    /**
     * true: 只提示 转向目标地址
     * false: 强制转向目标地址
     */
    private boolean notify;

    @Autowired
    private UserInfoExtService userInfoExtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (urls.isEmpty()) {
            return true;
        }
        UserInfo currentUser = SecurityUtil.getCurrentUser(false);
        if (currentUser == null) {
            return true;
        }
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(currentUser.getUserId());
        if (userInfoExt == null) {
            return true;
        }
        if (UserIsVerifiedEnum.BIND.getValue().equals(userInfoExt.getIsVerified())) {
            return true;
        }

        if (notify) {
            request.setAttribute(OPEN_ACCOUNT_NOTICE_KEY, true);
            return true;
        } else {
            request.getRequestDispatcher(destination).forward(request, response);
            return false;
        }
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
