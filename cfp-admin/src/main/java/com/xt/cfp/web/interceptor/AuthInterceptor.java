package com.xt.cfp.web.interceptor;

import com.xt.cfp.core.pojo.AdminInfo;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthInterceptor implements HandlerInterceptor {

    private static final String[] filters = new String[]{
            "/login",
            "/agreement/fetchTransAgreementHtml",
            "/agreement/service_loan",
            "/agreement/service_creditor",
            "/agreement/service_creditor_assignment",
            "/agreement/enterprise_service_loan",
            "/agreement/service_creditor_guarantee",
            "/agreement/service_creditor_assignment_guarantee",
            "/agreement/directional_commissioned",
            "/agreement/permission_financeplan",
            "/agreement/service_loan_direct_house",
            "/agreement/service_loan_direct_house_entrust",
            "/agreement/service_loan_person_car",
            "/agreement/service_loan_cash",
            "/img/argreen.png",
            "/jsp/custom/customer/personalOpenAccount",
    };

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        String requestUrl = request.getRequestURI();

        String u1 = "/css/";
        Pattern p1 = Pattern.compile(u1);
        Matcher m1 = p1.matcher(requestUrl);
        boolean b1 = m1.find();

        String u2 = "/js/";
        Pattern p2 = Pattern.compile(u2);
        Matcher m2 = p2.matcher(requestUrl);
        boolean b2 = m2.find();

        String rootPath = request.getContextPath();
        boolean b3 = false;

        for (String path : filters) {
            StringBuffer sb = new StringBuffer();
            sb.append(rootPath).append(path);
            if (requestUrl.equals(sb.toString())) {
                b3 = true;
                break;
            }
        }

        if (b1 == true || b2 == true || b3 == true) {//符合条件放行，否则验证session。
            return true;
        } else {
            HttpSession session = request.getSession();
            AdminInfo adminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
            if (null == adminInfo) {
                response.sendRedirect(request.getContextPath());
                return false;
            } else {
                request.setAttribute("__UID__", adminInfo.getAdminId());
                return true;
            }
        }

    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}
