package com.xt.cfp.wechat.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSON;
import com.xt.cfp.core.Exception.code.ext.BidErrorCode;
import com.xt.cfp.core.constants.SpecialBiddingEnum;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.propertyEditor.BigDecimalPropertyEditor;
import com.xt.cfp.core.propertyEditor.DatePropertyEditor;
import com.xt.cfp.core.propertyEditor.LongPropertyEditor;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.MD5Util;
import com.xt.cfp.core.util.Sign;
import com.xt.cfp.core.util.StringUtils;

public class BaseController extends MultiActionController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisCacheManger redisCacheManger;

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private LendOrderService lendOrderService;

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
     * 获得当前登录用户信息
     *
     * @param request
     * @return
     */
    public UserInfo getCurrentUser(HttpServletRequest request) {
        String userToken = request.getParameter("userToken");
        if (null != userToken && !"".equals(userToken)) {
            String userId = redisCacheManger.getRedisCacheInfo(userToken);
            if (null != userId) {
                return userInfoService.getUserByUserId(Long.valueOf(userId));
            } else {
                return null;
            }
        }
        return null;
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
    @SuppressWarnings({"unchecked", "rawtypes"})
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
     * 判断openid是否为空 保存到session中
     *
     * @param request
     * @return
     */
    protected String openIdValite(HttpServletRequest request) {
        String resultFlag = "";
        logger.info("微信请求方式=" + request.getMethod());
        //获取微信绑定的当前用户登录
        String code = request.getParameter("code");
        logger.info("code=" + code);
        if (code != null && !"".equals(code)) {
            String openId = Sign.getOpenId(code);
            logger.info("opendId=" + openId);
            if (openId != null && !"".equals(openId)) {
                resultFlag = openId;
                request.getSession().setAttribute("openId", openId);
            }
        }
        return resultFlag;
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
        String path = "";
        String type = loanApplicationService
                .countOtypeByLoanApplicationId(loanApplicationId);
        if (type != null && !type.equals("0")) {
            if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_PASSWORD.getValue()
                    .equals(type)) {// 定向密码
                if (oPass == null || "".equals(oPass.trim())) {
                    request.setAttribute("errorMsg", BidErrorCode.NOT_SPECAILPASS.getDesc());
                    request.setAttribute("errorCode", BidErrorCode.NOT_SPECAILPASS.getCode());
                    path = "/error";
                }
                String oPassWord = userInfoService
                        .getOrientPassLoan(loanApplicationId);
                if (!oPass.equals(oPassWord)) {
                    request.setAttribute("errorMsg", BidErrorCode.NOT_SPECAILPASSERROR.getDesc());
                    request.setAttribute("errorCode", BidErrorCode.NOT_SPECAILPASSERROR.getCode());
                    path = "/error";
                }
            } else if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_USER
                    .getValue().equals(type)) {// 定向用户
                int normalOrOrienta = userInfoService.normalOrOrienta(userId,
                        loanApplicationId);
                if (normalOrOrienta == 0) {// 没有定向权限
                    request.setAttribute("errorMsg", BidErrorCode.NOT_NOTSPECIALUSER.getDesc());
                    request.setAttribute("errorCode", BidErrorCode.NOT_NOTSPECIALUSER.getCode());
                    path = "/error";
                }
            } else if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_NEWUSER
                    .getValue().equals(type)) {// 新手用户
                String[] querys = {"1", "2", "5", "6"};
                int mCount = lendOrderService.countMakedLoan(userId, querys);
                if (mCount != 0) {// 是新手用户
                    request.setAttribute("errorMsg", BidErrorCode.NOT_NEWUSERBIDDING.getDesc());
                    request.setAttribute("errorCode", BidErrorCode.NOT_NEWUSERBIDDING.getCode());
                    path = "/error";
                }

            }
        }

        return path;
    }
}
