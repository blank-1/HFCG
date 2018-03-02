package com.xt.cfp.wechat.controller;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.pojo.InvitationCode;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.service.SmsService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.pay.Request;
import com.xt.cfp.core.util.*;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.wechat.annotation.DoNotNeedLogin;
import org.apache.commons.lang.*;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luqinglin on 2015/7/2.
 */
@Controller
@RequestMapping(value = "/user")
public class UserRegisterController extends BaseController {

    @Autowired
    private UserInfoService userInfoSerivce;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    /**
     * 跳转至注册页面
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/home")
    public String toRegister(HttpServletRequest request){
        //用于记录页面登陆成功后的跳转页面
        request.setAttribute("pastUrl", "/");
        return "/register/register";
    }
    
    /**
     * 跳转至分享注册页面
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/share")
    public String toShareRegister(HttpServletRequest request){
    	System.out.println(request.getParameter("invite_code"));
    	request.setAttribute("invite_code", request.getParameter("invite_code"));
        //用于记录页面登陆成功后的跳转页面
        request.setAttribute("pastUrl", "/");
        return "/register/shareRegister";
    }

    /**
     * 判断用户名是否存在
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/isLoginNameExist")
    @ResponseBody
    public String isLoginNameExist(String loginName){
        if(!loginNameValidate(loginName)){
            return JsonView.JsonViewFactory.create().success(false).info("用户名格式错误").put("id", "username")
                    .toJson();
        }
        //手机号验证
        if(mobileNoValidate(loginName)){
            return JsonView.JsonViewFactory.create().success(false).info("手机号不可作为用户名使用'").put("id", "username")
                    .toJson();
        }
        boolean loginNameExist = userInfoSerivce.isLoginNameExist(loginName);
        if(loginNameExist){
            return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.LOGINNAME_EXIST.getDesc()).put("id", "username")
                    .toJson();
        }
        return JsonView.JsonViewFactory.create().success(true).info("").put("id", "username")
                .toJson();
    }

    /**
     * 判断手机号是否存在
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/isMobileNoExist")
    @ResponseBody
    public String isMobileNoExist(String mobileNo){
        if(!mobileNoValidate(mobileNo)){
            return JsonView.JsonViewFactory.create().success(false).info("手机号码格式错误").put("id","phone")
                    .toJson();
        }
        boolean mobileExist = userInfoSerivce.isMobileExist(mobileNo);
        if(mobileExist){
            return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.MOBILENO_EXIST.getDesc()).put("id", "phone")
                    .toJson();
        }
        return JsonView.JsonViewFactory.create().success(true).info("").put("id","phone")
                .toJson();
    }

    /**
     * 发送短信验证码
     * @param mobileNo
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/getRegisterMsg")
    @ResponseBody
    public String getRegisterMsg(HttpServletRequest request,String mobileNo){
        //post请求验证
        RequestUtil.validateRequestMethod(request,RequestMethod.POST);
        
        try {
            //验证手机号是否存在
            if(!mobileNoValidate(mobileNo)){
                return JsonView.JsonViewFactory.create().success(false).info("手机号码格式错误").put("id","phone")
                        .toJson();
            }
            boolean mobileExist = userInfoSerivce.isMobileExist(mobileNo);
            if(mobileExist){
                return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.MOBILENO_EXIST.getDesc()).put("id", "phone")
                        .toJson();
            }
            userInfoSerivce.sendRegisterMsg(mobileNo);
            return JsonView.JsonViewFactory.create().success(true).info("")
                    .toJson();
        }catch (SystemException se){
            logger.error(se.getMessage(),se);
            return  JsonView.JsonViewFactory.create().success(false).info("")
                    .toJson();
        }
    }

    /**
     * 注册
     * @param request
     * @param userInfo
     * @param validCode
     * @param inviteUserId 邀请好友id
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/register")
    public String register(HttpServletRequest request,UserInfo userInfo,String validCode,String inviteCode,String inviteUserId){
        try {
        	System.out.println(userInfo.getMobileNo()+" "+validCode+" "+userInfo.getLoginName()+" "+userInfo.getLoginPass()+" "+inviteCode);
            //后端验证注册信息
            Map<String,String> validate = (Map<String, String>) validate(userInfo.getMobileNo(), validCode, userInfo.getLoginName(), userInfo.getLoginPass(),inviteCode,true);
            for(String key:validate.keySet()){
                String result = validate.get(key);
                Map<String, Object> jsonMap = JsonUtil.fromJson(result);
                boolean isSuccess = (Boolean)jsonMap.get("isSuccess");
                if (!isSuccess){
                    //todo 跳转错误页面
                   throw new SystemException(UserErrorCode.REGIST_INFO_ILLEGAL);
                }
            }
            //注册
            UserInfo user = null;
            InvitationCode invitationCode = userInfoSerivce.getInvitationCodeByCode(inviteCode);
            String openId = (String) request.getSession().getAttribute("openId");
            user = userInfoSerivce.registBinding(userInfo, UserSource.WECHAT, openId, invitationCode);
            //保存用户session值
            request.getSession().setAttribute(Constants.USER_ID_IN_SESSION,user);
            return "redirect:/?flag=1";
        }catch (SystemException se){
            se.printStackTrace();
            throw se;
        }
    }

    /**
     * 转到注册成功页面
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/registerSuccess")
    public String registerSuccess(){
    	System.out.println(12312312);
        return "/register/registerSuccess";
    }

    /**
     * 校验验证码
     * @param validCode
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/validate")
    @ResponseBody
    public Object validate(String mobile,String validCode,String loginName,String password,String inviteCode,boolean flag) {

        HashMap<String,String> map = new HashMap<String, String>();
        //验证登陆名
        map.put("username",isLoginNameExist(loginName));
        //验证手机号
        map.put("phone",isMobileNoExist(mobile));
        //验证密码
        map.put("password",JsonView.JsonViewFactory.create().success(passwordValidate(password)).info("请输入6~16位字符，字母加数字组合，字母区分大小写").put("id", "password")
                .toJson());
        //验证短信码,并返回
        map.put("valid",validate(mobile, validCode,flag));
        //验证邀请码

        if(org.apache.commons.lang.StringUtils.isNotEmpty(inviteCode)&& !"邀请码 (非必填)".equals(inviteCode)){
            map.put("visate",validateInviteCode(inviteCode.trim()));
        }

        return map;
    }

    /**
     * 校验邀请码
     * @param inviteCode
     * @return
     */
    private String validateInviteCode(String inviteCode) {
        InvitationCode invitationCode = userInfoSerivce.getInvitationCodeByCode(inviteCode);
        if(invitationCode==null){
            return JsonView.JsonViewFactory.create().success(false).info("你输入的邀请码不存在").put("id", "visate")
                    .toJson();
        }else{
            return JsonView.JsonViewFactory.create().success(true).info("").put("id", "visate")
                    .toJson();
        }
    }

    /**
     * 校验用户名、手机号
     * @param validCode
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/validate_name_phone")
    @ResponseBody
    public Object validateNamePhone(String mobile,String validCode,String loginName,String password) {

        HashMap<String,String> map = new HashMap<String, String>();
        //验证登陆名
        map.put("username",isLoginNameExist(loginName));
        //验证手机号
        map.put("phone",isMobileNoExist(mobile));
        return map;
    }

    /**
     * 校验短信验证码
     * @param validCode
     * @return
     */
    public String validate(String mobile,String validCode,boolean flag) {
        try {
            boolean bool = smsService.validateMsg(mobile, validCode, TemplateType.SMS_REGISTER_VM,flag);
            if(bool)
                return JsonView.JsonViewFactory.create().success(true).info("").put("id", "valid")
                        .toJson();
            else
                return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.VALIDATE_CODE_ERROR.getDesc()).put("id", "valid")
                        .toJson();
        }catch(SystemException se){
            logger.error(se.getMessage(),se);
            return JsonView.JsonViewFactory.create().success(false).info(se.getMessage()).put("id", "valid")
                    .toJson();
        }
    }



    /**
     * 检查用户填写的交易密码是否正确，此检查涉及两个判断，
     * 1是检查当前登录用户是否设定了交易密码，
     * 2是如果请求参数中包含有bidPass,就检查该bidPass和用户设定的交易密码是否一致
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkBidPass")
    public String checkBidPass(HttpServletRequest request) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfo userByUserId = this.userInfoSerivce.getUserByUserId(currentUser.getUserId());
        if (userByUserId.getBidPass() == null)
            return JsonView.JsonViewFactory.create().success(false).info("还未设定交易密码").toJson();

        if (RequestUtil.isExist(request, "bidPass"))
            if (!MD5Util.MD5Encode(request.getParameter("bidPass"), null).equals(userByUserId.getBidPass()))
                return JsonView.JsonViewFactory.create().success(false).info("交易密码不正确").toJson();

        return JsonView.JsonViewFactory.create().success(true).info("").toJson();
    }

    /**
     * 转到找回密码页 1
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/rePasswordOne")
    public String rePasswordOne(HttpServletRequest request){
    	TokenUtils.setNewToken(request);
        //检查用户是否已经登陆
        UserInfo currentUser = SecurityUtil.getCurrentUser(false);
        if (currentUser!=null){
            return "redirect:/person/account/overview";
        }
        return "/retrievePass/rePasswordOne";
    }
    
    /**
     * 判断手机号是否存在  存在发送验证码
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/isMobileNoToSendMsg")
    @ResponseBody
    public String isMobileNoToSendMsg(HttpServletRequest request, String mobileNo){
        //post验证
        RequestUtil.validateRequestMethod(request,RequestMethod.POST);
        if(!mobileNoValidate(mobileNo)){
            return JsonView.JsonViewFactory.create().success(false).info("手机号码格式错误").put("id","phone")
                    .toJson();
        }
        boolean mobileExist = userInfoSerivce.isMobileExist(mobileNo);
        if(mobileExist){
            try {
                userInfoSerivce.sendRetrievePassword(mobileNo);
                return JsonView.JsonViewFactory.create().success(true).info("")
                        .toJson();
            }catch (SystemException se){
                logger.error(se.getMessage(),se);
                return  JsonView.JsonViewFactory.create().success(false).info(se.getMessage())
                        .toJson();
            }
        } 
        return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.MOBILENO_NOEXIST.getDesc()).put("id", "phone")
                .toJson(); 
    }
   
    /**
     * 验证码验证
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/rePasswordValidate", method = RequestMethod.POST)
    @ResponseBody
    public String rePasswordValidate(HttpServletRequest request, String valid, String phone){
    	//String phone = (String)request.getSession().getAttribute("zh_mobile");
    	if(phone == null){
    		return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.VALIDATE_CODE_ERROR.getDesc()).put("id", "valid")
                    .toJson();
    	}
    	 try {
             boolean bool = smsService.validateMsg(phone, valid, TemplateType.SMS_RETRIEVEPASSWORD_VM,true);
             if(bool){
            	 request.getSession().setAttribute("zh_mobile", phone);
                 return JsonView.JsonViewFactory.create().success(true).info("").put("id", "valid")
                         .toJson(); 
             }
             else
                 return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.VALIDATE_CODE_ERROR.getDesc()).put("id", "valid")
                         .toJson();
         }catch(SystemException se){
             logger.error(se.getMessage(),se);
             return JsonView.JsonViewFactory.create().success(false).info(se.getMessage()).put("id", "valid")
                     .toJson();
         }
    }
    
    /**
     * 转到找回密码页 2
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/rePasswordTwo", method = RequestMethod.POST)
    public String rePasswordTwo(HttpServletRequest request){
    	TokenUtils.validateToken(request);
    	String phone = (String)request.getSession().getAttribute("zh_mobile");
    	if(phone == null){
    		throw new SystemException(UserErrorCode.MOBILENO_NOEXIST);
    	}
    	TokenUtils.setNewToken(request);
    	return "/retrievePass/rePasswordTwo";
    }
    
    /**
     * 判断是否身份验证  和生成注册日期
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/getRegistrationTime", method = RequestMethod.POST)
    @ResponseBody
    public Object getRegistrationTime(HttpServletRequest request){
    	String phone = (String)request.getSession().getAttribute("zh_mobile");
    	if(phone == null){
    		return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.MOBILENO_NOT_EXIST.getDesc()).put("id", "valid")
                    .toJson();
    	}
		boolean identityType = false;
		if(StringUtils.isNull(phone))
			throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("phone", phone);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		UserInfo userInfo = userInfoSerivce.getUserByMobileNo(phone);
		UserInfoExt uie = userInfoExtService.getUserInfoExtById(userInfo.getUserId());
		if("".equals(uie.getIdCard()) || uie.getIdCard()== null)
		{
			identityType = true;
		}
		resultMap.put("dateList", getRandomDate(userInfo.getCreateTime()));
		resultMap.put("identityType", identityType);	
    	return resultMap;
    }
    
    /**
     * 身份证匹配
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/identityMatch", method = RequestMethod.POST)
    @ResponseBody
    public String identityMatch(HttpServletRequest request, String before, String after, String registTime){
    	String mobile = (String)request.getSession().getAttribute("zh_mobile");
    	if(mobile == null){
    		return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.MOBILENO_NOT_EXIST.getDesc()).put("id", "valid")
                    .toJson();
    	}
    	UserInfoExt userInfoExt = userInfoExtService.getUserExtByMobileAanIndentInfo(mobile, before, after);
    	if(userInfoExt != null)
    	{
    		UserInfo userInfo = userInfoSerivce.getUserByMobileNo(mobile);
    		SimpleDateFormat sdfYm = new SimpleDateFormat("yyyy-MM");
    		if(registTime.equals(sdfYm.format(userInfo.getCreateTime())))
    		{
    			return JsonView.JsonViewFactory.create().success(true).info("")
            			.toJson();
    		}
    		else
    		{
    			return JsonView.JsonViewFactory.create().success(false).info("注册日期错误").put("id", "regist")
            			.toJson();
    		}
    		
    	}
    	return JsonView.JsonViewFactory.create().success(false).info("身份证错误").put("id", "indent")
    			.toJson();
    }
    
    public String[] getRandomDate(Date date) {
    	Calendar calendar=Calendar.getInstance();   
	    calendar.setTime(date);
	    
	    try {
	    	calendar.add(Calendar.MONTH, -1);
			String nowBefore = DateUtil.getYearMonth(calendar.getTime());
			String now = DateUtil.getYearMonth(date);
			calendar.add(Calendar.MONTH, 2);
			String nowAfterOne = DateUtil.getYearMonth(calendar.getTime());
			calendar.add(Calendar.MONTH, 1);
			String nowAfterTwo = DateUtil.getYearMonth(calendar.getTime());
			List<String> dateList = new ArrayList<String>();
			dateList.add(nowBefore);
			dateList.add(now);
			dateList.add(nowAfterOne);
			dateList.add(nowAfterTwo);
			Random random = new Random();
	        String[] result = new String[] {"", "", "", ""};
	        List exist = new ArrayList();
	        for (String aa5 : dateList) {
	            while(true){
	                int i = random.nextInt(4);
	                if (!exist.contains(i)) {
	                    exist.add(i);
	                    result[i] = aa5;
	                    break;
	                } else
	                    continue;
	            }
	        }
	        return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    /**
     * 转到找回密码页 3
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/rePasswordThree", method = RequestMethod.POST)
    public String rePasswordThree(HttpServletRequest request){
    	TokenUtils.validateToken(request);
    	String phone = (String)request.getSession().getAttribute("zh_mobile");
    	if(phone == null){
    		throw new SystemException(UserErrorCode.MOBILENO_NOEXIST);
    	}
    	TokenUtils.setNewToken(request);
        return "/retrievePass/rePasswordThree";
    }
    /**
     * 转到找回密码页 4
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/rePasswordFour", method = RequestMethod.POST)
    public String reposswordFour(HttpServletRequest request, String password){
    	TokenUtils.validateToken(request);
    	String phone = (String)request.getSession().getAttribute("zh_mobile");
    	if(phone == null){
    		return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.MOBILENO_NOT_EXIST.getDesc()).put("id", "valid")
                    .toJson();
    	}
    	if(password == null || "".equals(password)){
    		request.setAttribute("message", "密码不能为空");
    		return "/retrievePass/rePasswordThree";
    	}else if(!passwordValidate(password)){
    		request.setAttribute("message", "密码格式错误");
    		return "/retrievePass/rePasswordThree";
    	}
    	UserInfo userInfo = userInfoSerivce.getUserByMobileNo(phone);
    	String newPassword = MD5Util.MD5Encode(password, "utf-8");
		userInfo.setLoginPass(newPassword);
    	userInfoSerivce.updateUser(userInfo);
		return "/retrievePass/rePasswordFour";
    }
    
    /**
     * 分享注册
     * @param request
     * @param userInfo
     * @param validCode
     * @param inviteUserId 邀请好友id
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/shareRegister")
    public String shareRegister(HttpServletRequest request,UserInfo userInfo,String validCode,String inviteCode,String inviteUserId){
        try {
        	System.out.println(userInfo.getMobileNo()+" "+validCode+" "+userInfo.getLoginName()+" "+userInfo.getLoginPass()+" "+inviteCode);
            //后端验证注册信息
            Map<String,String> validate = (Map<String, String>) validate(userInfo.getMobileNo(), validCode, userInfo.getLoginName(), userInfo.getLoginPass(),inviteCode,true);
            for(String key:validate.keySet()){
                String result = validate.get(key);
                Map<String, Object> jsonMap = JsonUtil.fromJson(result);
                boolean isSuccess = (Boolean)jsonMap.get("isSuccess");
                if (!isSuccess){
                    //todo 跳转错误页面
                   throw new SystemException(UserErrorCode.REGIST_INFO_ILLEGAL);
                }
            }
            //注册
            UserInfo user = null;
            InvitationCode invitationCode = userInfoSerivce.getInvitationCodeByCode(inviteCode);
            if(invitationCode==null){
                user = userInfoSerivce.regist(userInfo, UserSource.WECHAT);
            }else{
                user = userInfoSerivce.regist(userInfo, UserSource.WECHAT, invitationCode.getUserId()+"");
            }
            //保存用户session值
            request.getSession().setAttribute(Constants.USER_ID_IN_SESSION,user);
            return "redirect:registerSuccess?invite_code="+inviteCode;
        }catch (SystemException se){
            se.printStackTrace();
            throw se;
        }
    }
    /**
     * wangyadong
     * 跳转至注册协议页面
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/registeraGreement")
    public String registeraGreement(){
        //用于记录页面登陆成功后的跳转页面
      
        return "/register/registeraGreement";
    }

}
