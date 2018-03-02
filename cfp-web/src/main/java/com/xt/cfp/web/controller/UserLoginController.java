package com.xt.cfp.web.controller;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.constants.UserIsVerifiedEnum;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.SecurityUtil;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.web.annotation.DoNotNeedLogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luqinglin on 2015/7/2.
 */
@Controller
@RequestMapping(value = "/user")
public class UserLoginController extends BaseController {

    @Autowired
    private UserInfoService userInfoSerivce;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private UserAccountService userAccountService;
    
    public boolean validate(String loginName,String password,List<String> resultMsg) {
        boolean unlogin = false;
        boolean pwlogin = false;
        //验证登陆名
        if (!loginNameValidate(loginName)&&!mobileNoValidate(loginName)){
            resultMsg.add(JsonView.JsonViewFactory.create().success(false).info("登陆名或手机号格式不正确").put("id", "unlogin")
                    .toJson());
        }else{
            unlogin=true;
            resultMsg.add(JsonView.JsonViewFactory.create().success(true).info("").put("id", "unlogin")
                    .toJson());
        }
        //验证密码
        pwlogin = passwordValidate(password);
        resultMsg.add(JsonView.JsonViewFactory.create().success(pwlogin).info("密码格式不正确").put("id", "pwlogin")
                .toJson());

        return unlogin&&pwlogin;
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/login")
    @ResponseBody
    public Object login(HttpServletRequest request,String loginName,String loginPass,String pastUrl,HttpSession session){
        List<String> result = new ArrayList<String>();
        //登陆
        try{
            //todo 登陆次数记录
            UserInfo loginUser = userInfoSerivce.login(loginName, loginPass);
            if (loginUser!=null){
                //保存登陆信息
                request.getSession().setAttribute(Constants.USER_ID_IN_SESSION,loginUser);
                UserAccount cashAccount = userAccountService.getCashAccount(loginUser.getUserId());
                session.setAttribute("cashAccount", cashAccount);
                session.setAttribute("isLogined", true);
                //记录用户登陆信息
                userInfoSerivce.recordUser(loginUser);
                result.add(JsonView.JsonViewFactory.create().success(true).info("登陆成功").put("pastUrl", pastUrl)
                        .toJson());
                return result;
            }else{//密码错误
                result.add(JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.ERROR_PASS_LOGIN.getDesc()).put("id", "pwlogin")
                        .toJson());
                return result;
            }
        }catch (SystemException se){
            //用户名或手机号不存在
            result.add(JsonView.JsonViewFactory.create().success(false).info(se.getMessage()).put("id", "unlogin")
                    .toJson());
            return result;
        }

    }

    @RequestMapping(value = "/logout")
    @DoNotNeedLogin
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute(Constants.USER_ID_IN_SESSION);
        request.getSession().removeAttribute("isLogined");
        //进入首页
        return "redirect:/";
    }

    /**
     * 跳转至快速借款通道
     * @return
     */
    @RequestMapping("/to_fastLoan")
    @ResponseBody
    public Object toFastLoan(HttpServletRequest request){
    	UserInfo currentUser = SecurityUtil.getCurrentUser(true);
    	UserInfoExt uie = userInfoExtService.getUserInfoExtById(currentUser.getUserId());
    	if(UserIsVerifiedEnum.NO.getValue().equals(uie.getIsVerified())){
    		return JsonView.JsonViewFactory.create().success(false).info("").put("id", "unIsVerified")
                    .toJson();
    	}
        return JsonView.JsonViewFactory.create().success(true).info("").put("userId", currentUser.getUserId())
        		.put("realName", uie.getRealName()).put("idCard", uie.getIdCard()).put("mobile", currentUser.getMobileNo())
                .toJson();
    }
    
    /**
     * 跳转至快速借款页面
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping("/fastLoan")
    public String fastLoan(HttpServletRequest request){
    	
    	return "/fastLoan/fastloan";
    }
    
    /**
     * 跳转至登陆页
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping("/to_login")
    public String toLoginPage(HttpServletRequest request,String flag){
    	if(!StringUtils.isNull(flag)&&"ratehd".equals(flag)){
    		request.setAttribute("pastUrl","/activity/increaseRate");
    	}else{
    		request.setAttribute("pastUrl","/");
    	}
        return "login/loginPage";
    }



    /**
     * 进入404页
     */
    @RequestMapping(value = "/404")
    @DoNotNeedLogin
    public String toError(HttpServletRequest request) {
        return "404";
    }

}
