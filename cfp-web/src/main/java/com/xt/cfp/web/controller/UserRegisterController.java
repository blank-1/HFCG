package com.xt.cfp.web.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.HttpErrorCode;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.constants.UserRegisterRedisKeyEnum;
import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.pojo.InvitationCode;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.service.SmsService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.Captcha;
import com.xt.cfp.core.util.CheckMobile;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.JsonUtil;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.MD5Util;
import com.xt.cfp.core.util.RequestUtil;
import com.xt.cfp.core.util.SecurityUtil;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.core.util.TokenUtils;
import com.xt.cfp.web.annotation.DoNotNeedLogin;
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
	 * 
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/regist/home")
	public String toRegister(HttpServletRequest request) {
		// 用于记录页面登陆成功后的跳转页面		
		request.setAttribute("pastUrl", "/");
		return userInfoSerivce.getRegisterPath();
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
            return JsonView.JsonViewFactory.create().success(false).info("请输入4 - 20位字符，支持汉字、字母、数字及 '-'、'_组合'").put("id", "username")
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
	 * 
	 * @param mobileNo
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/regist/getRegisterMsg")
	@ResponseBody
	public String getRegisterMsg(HttpServletRequest request, String mobileNo) {
		// 验证token
		TokenUtils.validateToken(request);
		// post请求验证
		RequestUtil.validateRequestMethod(request, RequestMethod.POST);
		// Referer验证
		RequestUtil.validateRequestReferer(request, "/user/regist/home",
				"/person/beinvite");
		try {
			// 验证手机号是否存在
			if (!mobileNoValidate(mobileNo)) {
				return JsonView.JsonViewFactory.create().success(false)
						.info("手机号码格式错误").put("id", "phone").toJson();
			}
			boolean mobileExist = userInfoSerivce.isMobileExist(mobileNo);
			if (mobileExist) {
				return JsonView.JsonViewFactory.create().success(false)
						.info(UserErrorCode.MOBILENO_EXIST.getDesc())
						.put("id", "phone").toJson();
			}
			if (!userInfoSerivce.isRegisterMsgValid(mobileNo)) {
				return JsonView.JsonViewFactory.create().success(false)
						.info("短信验证码未失效，不进行重复发短信").put("id", "phone").toJson();
			}
			if (userInfoSerivce.getRegisterMsgConfirm(mobileNo)) {
//				System.out.println("没有确认过发短信，原因可能是确认过期或者是参数不正确");
				return JsonView.JsonViewFactory.create().success(false)
						.info(UserErrorCode.HAS_NOT_IDVERIFIED.getDesc())
						.put("id", "phone").toJson();
			}
			int count = userInfoSerivce.getRegisterMsgCount(mobileNo,
					DateUtil.getNowShortDate(),
					UserRegisterRedisKeyEnum.USER_REGISTER_COUNT_KEY);
			if(count>=100){
				return JsonView.JsonViewFactory.create().success(false)
						.info(UserErrorCode.MOBILE_SMSCODE_MAX.getDesc())
						.put("id", "phone").toJson();
			}
			userInfoSerivce.sendRegisterMsg(mobileNo);
			System.out.println("==============短信验证通过，发送短信验证码===========");
			return JsonView.JsonViewFactory.create().success(true).info("")
					.toJson();
		} catch (SystemException se) {
			logger.error(se.getMessage(), se);
			return JsonView.JsonViewFactory.create().success(false).info("")
					.toJson();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return JsonView.JsonViewFactory.create().success(false).info("")
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
                user = userInfoSerivce.regist(userInfo, UserSource.WEB);
            }else{
                user = userInfoSerivce.regist(userInfo, UserSource.WEB, invitationCode.getUserId()+"");
            }
            //保存用户session值
            request.getSession().setAttribute(Constants.USER_ID_IN_SESSION,user);
            return "redirect:registerSuccess";
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
    public String registerSuccess(HttpServletRequest req){
        //获取ua，用来判断是否为移动端访问
        String userAgent = req.getHeader( "USER-AGENT" ).toLowerCase();
        if(null == userAgent){
            userAgent = "";
        }
        boolean isFromMobile=CheckMobile.check(userAgent);
        //判断是否为移动端访问
        if(isFromMobile){
            return "/register/mobileRegisterSuccess";
        } else {
            //用于记录页面登陆成功后的跳转页面
            return "register/registerSuccess";
        }
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
        map.put("password",JsonView.JsonViewFactory.create().success(passwordValidate(password)).info("请输入6~16位字符，支持字母及数字,字母区分大小写").put("id", "password")
                .toJson());
        //验证短信码,并返回
        map.put("valid",validate(mobile, validCode,flag));
        //验证邀请码

        if(org.apache.commons.lang.StringUtils.isNotEmpty(inviteCode)){
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
    public Object validateNamePhone(HttpServletRequest request ,String mobile,String validCode,String loginName,String password) {

        HashMap<String,String> map = new HashMap<String, String>();
        //验证登陆名
        map.put("username",isLoginNameExist(loginName));
        //验证手机号
        map.put("phone",isMobileNoExist(mobile));
        if (validSendSmsCode(map)) {
			userInfoSerivce.setRegisterMsgConfirm(mobile);
		}
		String token = TokenUtils.setNewToken(request);
		map.put("token", token);
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
    	UserInfo user=userInfoSerivce.getUserByMobileNo(phone);
    	if(user==null){
    		throw new SystemException(UserErrorCode.USER_NOT_EXIST);
    	}
    	UserInfoExt ue=userInfoExtService.getUserInfoExtById(user.getUserId());
    	request.setAttribute("verified", ue.getIsVerified());
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
    	UserInfo user=userInfoSerivce.getUserByMobileNo(mobile);
    	if(user==null){
    		return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.USER_NOT_EXIST.getDesc()).put("id", "regist")
        			.toJson();
    	}
    	UserInfoExt ue=userInfoExtService.getUserInfoExtById(user.getUserId());
    	if(ue!=null){
    		if(ue.getIsVerified().equals("0")){
    			before=null;
    			after=null;
    		}
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
	 * 校验手机验证码
	 * */
	@DoNotNeedLogin
	@RequestMapping(value = "/regist/validSmsCode")
	@ResponseBody
	public Object validateSmsCode(String mobile, String validCode,
			boolean flag) {
		// 校验验证码是否正确
		// 短信验证码校验成功后失败后再次校验短信，token失效
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("valid", validate(mobile, validCode,false));
		try {
			int count = userInfoSerivce.getRegisterMsgCount(mobile,
					DateUtil.getNowShortDate(),
					UserRegisterRedisKeyEnum.USER_REGISTER_COUNT_KEY);
			map.put("count", String.valueOf(count));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 校验图片验证码
	 * */
	@DoNotNeedLogin
	@RequestMapping(value = "/regist/validImgCode")
	@ResponseBody
	public Object validateImgCode(String imgCode, String auth) {
		Map<String, String> map = userInfoSerivce
				.checkImgCode(imgCode, auth);
		return map;
	}

	/**
	 * 获取验证码图片
	 * 
	 * @param auth
	 *            -- 用户访问唯一标识 用于保存对用户的验证码
	 * */
	@DoNotNeedLogin
	@RequestMapping(value = "/regist/image")
	public void getImage(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("auth");
		if (!StringUtils.isNull(id)) {
			id = id.trim();
		}
		// 设置不缓存图片
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		// 指定生成的相应图片
		response.setContentType("image/jpeg");
		try {
			String randomStr = Captcha.generateVerifyCode(4);
			userInfoSerivce.setRegisterImgCode(
					UserRegisterRedisKeyEnum.USER_REGISTER_IMG_KEY, id,
					randomStr, 60);
			BufferedImage bi = new Captcha().generate(200, 50, randomStr)
					.getImage();
			ImageIO.write(bi, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;

	}
	
	
	/**
	 * 校验用户名、手机号
	 * 
	 * @param validCode
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/regist/validate_name_phone_img")
	@ResponseBody
	public Object validateNamePhoneImg(String mobile, String validCode,
			String loginName, String password, HttpServletRequest request) {
		int count = 0;
		try {
			count = userInfoSerivce.getRegisterMsgCount(mobile,
					DateUtil.getNowShortDate(),
					UserRegisterRedisKeyEnum.USER_REGISTER_COUNT_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		// 验证登陆名
		map.put("username", isLoginNameExist(loginName));
		// 验证手机号
		map.put("phone", isMobileNoExist(mobile));
		// 验证已发验证码次数
		map.put("count", String.valueOf(count));
		map.put("phoneNo", String.valueOf(mobile));
		if (validSendSmsCode(map)) {
			userInfoSerivce.setRegisterMsgConfirm(mobile);
		}
		String token = TokenUtils.setNewToken(request);
		map.put("token", token);
		;
		return map;
	}

	/**
	 * 校验封装的map有没有false值
	 * */
	private boolean validSendSmsCode(HashMap<String, String> map) {
		for (Entry<String, String> e : map.entrySet()) {
			if (e.getValue().indexOf("false") != -1) {
				return false;
			}
		}
		return true;
	}

	
	
	/**
	 * 校验验证码
	 * 
	 * @param validCode
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/regist/validateImg")
	@ResponseBody
	public Object validateImg(String mobile, String validCode,
			String loginName, String password, String inviteCode,
			HttpServletRequest request) {

		HashMap<String, String> map = new HashMap<String, String>();
		// 验证登陆名
		map.put("username", isLoginNameExist(loginName));
		// 验证手机号
		map.put("phone", isMobileNoExist(mobile));
		// 验证密码
		map.put("password",
				JsonView.JsonViewFactory.create()
						.success(passwordValidate(password))
						.info("请输入6~16位字符，支持字母及数字,字母区分大小写")
						.put("id", "password").toJson());
		// 验证短信码,并返回
		// map.put("valid",validate(mobile, validCode,flag));
		// 验证邀请码

		if (org.apache.commons.lang.StringUtils.isNotEmpty(inviteCode)) {
			map.put("visate", validateInviteCode(inviteCode.trim()));
		}

		if (validSendSmsCode(map)) {
			userInfoSerivce.setRegisterMsgConfirm(mobile);
		}

		return map;
	}
	
}
