package com.xt.cfp.web.controller;

import com.alibaba.fastjson.JSON;
import com.xt.cfp.core.constants.SpecialBiddingEnum;
import com.xt.cfp.core.propertyEditor.BigDecimalPropertyEditor;
import com.xt.cfp.core.propertyEditor.DatePropertyEditor;
import com.xt.cfp.core.propertyEditor.LongPropertyEditor;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.MD5Util;
import com.xt.cfp.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseController extends MultiActionController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private UserInfoService userInfoService;

    @InitBinder
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DatePropertyEditor());
        binder.registerCustomEditor(BigDecimal.class, new BigDecimalPropertyEditor());
        binder.registerCustomEditor(Long.class, new LongPropertyEditor());
    }

    /**
     * 获取Request对象
     */
    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }


    /**
     * 手机验证
     *
     * @param mobileNo
     * @return
     */
    boolean mobileNoValidate(String mobileNo) {
        return StringUtils.mobileNoValidate(mobileNo);
    }

    /**
     * 用户名验证
     *
     * @param loginName
     * @return
     */
    boolean loginNameValidate(String loginName) {
        String regex = "([a-z]|[A-Z]|[0-9_-]|[\\u4e00-\\u9fa5]){4,20}+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(loginName);
        boolean b = m.matches();
        return b;
    }

    /**
     * 密码验证
     *
     * @param password
     * @return
     */
    boolean passwordValidate(String password) {
        String regex = "^[0-9a-zA-Z]{6,16}+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        boolean b = m.matches();
        return b;
    }

    /**
     * 身份证验证
     *
     * @param password
     * @return
     */
    boolean idCardValidate(String password) {
        String regex = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        boolean b = m.matches();
        return b;
    }

    /**
     * 返回json格式结果。
     *
     * @param isSuccess 是否成功
     * @param data      数据
     * @param errCode   错误编号
     * @param errMsg    错误消息
     */
    protected Object returnResultMap(boolean isSuccess, Object data, String errCode, String errMsg) {
        Map resultMap = new HashMap();
        if (isSuccess) {
            resultMap.put("result", "success");
            resultMap.put("data", data);
        } else {
            resultMap.put("result", "error");
            resultMap.put("errCode", errCode);
            resultMap.put("errMsg", errMsg);
        }
        return JSON.toJSON(resultMap);
    }

    /**
     * 返回json格式结果，自定义封装格式
     */
    protected void printReturnData(String resp, HttpServletResponse response) {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            out.write(resp.getBytes());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     * 判断定向标
     * @param userId
     * @param loanApplicationId
     * @param oPass
     * @author wangyadong
     * @return
     */
    public String chioceWhichReturnBySpecialLoanApplication(long userId,
                                                            Long loanApplicationId, String oPass, HttpServletRequest request) {
        String path = "/order/payError";
        String type = loanApplicationService.countOtypeByLoanApplicationId(loanApplicationId);
        if (type != null && !type.equals("0")) {
            if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_PASSWORD.getValue().equals(type)) {// 定向密码
                if (oPass == null || "".equals(oPass.trim())) {
                    oPass = (String) request.getSession().getAttribute("opass_" + loanApplicationId);
                    if (null == oPass || "".equals(oPass)) {
                        request.setAttribute("errorMsg", "定向密码不可以为空");
                        return path;
                    }
                }
                String oPassWord = userInfoService.getOrientPassLoan(loanApplicationId);
                if (!MD5Util.MD5Encode(oPass, "utf-8").equals(oPassWord)) {
                    request.setAttribute("errorMsg", "定向密码错误");
                    return path;
                }
            } else if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_USER.getValue().equals(type)) {// 定向用户
                int normalOrOrienta = userInfoService.normalOrOrienta(userId, loanApplicationId);
                if (normalOrOrienta == 0) {// 没有定向权限
                    request.setAttribute("errorMsg", "您还不是定向用户");
                    return path;
                }
            } else if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_NEWUSER.getValue().equals(type)) {// 新手用户
                String[] querys = {"1", "2", "5", "6"};
                int mCount = lendOrderService.countMakedLoan(userId, querys);
                if (mCount != 0) {// 是新手用户
                    request.setAttribute("errorMsg", "您还不是新手用户");
                    return path;
                }

            }
        }

        return "";
    }

    public String chioceWhichReturnBySpecialLoanApplication2(long userId,
                                                            Long loanApplicationId, String oPass, HttpServletRequest request) {
        String type = loanApplicationService.countOtypeByLoanApplicationId(loanApplicationId);
        if (type != null && !type.equals("0")) {
            if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_PASSWORD.getValue().equals(type)) {// 定向密码
                if (oPass == null || "".equals(oPass.trim())) {
                    oPass = (String) request.getSession().getAttribute("opass_" + loanApplicationId);
                    if (null == oPass || "".equals(oPass)) {
                        return JsonView.JsonViewFactory.create().success(false).info("定向密码不可以为空").put("id","redirect").toJson();
                    }
                }
                String oPassWord = userInfoService.getOrientPassLoan(loanApplicationId);
                if (!MD5Util.MD5Encode(oPass, "utf-8").equals(oPassWord)) {
                    return JsonView.JsonViewFactory.create().success(false).info("定向密码错误").put("id","redirect").toJson();
                }
            } else if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_USER.getValue().equals(type)) {// 定向用户
                int normalOrOrienta = userInfoService.normalOrOrienta(userId, loanApplicationId);
                if (normalOrOrienta == 0) {// 没有定向权限
                    return JsonView.JsonViewFactory.create().success(false).info("您还不是定向用户").put("id","redirect").toJson();
                }
            } else if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_NEWUSER.getValue().equals(type)) {// 新手用户
                String[] querys = {"1", "2", "5", "6"};
                int mCount = lendOrderService.countMakedLoan(userId, querys);
                if (mCount != 0) {// 是新手用户
                    return JsonView.JsonViewFactory.create().success(false).info("您还不是新手用户").put("id","redirect").toJson();
                }
            }
        }
        return null;
    }

}
