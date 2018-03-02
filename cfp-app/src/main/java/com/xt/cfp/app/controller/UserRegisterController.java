package com.xt.cfp.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.app.annotation.DoNotNeedLogin;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.pojo.InvitationCode;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.service.SmsService;
import com.xt.cfp.core.service.UserInfoService;

@Controller
@RequestMapping(value = "/user")
public class UserRegisterController extends BaseController {

    @Autowired
    private UserInfoService userInfoSerivce;
    @Autowired
    private SmsService smsService;

    /**
     * 【APP接口工具】
     * 判断用户名是否存在
     * @return
     */
    private String isLoginNameExist(String loginName){
        if(!loginNameValidate(loginName)){
            return "用户名格式错误";
        }
        if(mobileNoValidate(loginName)){
            return "手机号不可作为用户名使用";
        }
        boolean loginNameExist = userInfoSerivce.isLoginNameExist(loginName);
        if(loginNameExist){
            return UserErrorCode.LOGINNAME_EXIST.getDesc();
        }
        return null;
    }

    /**
     * 【APP接口工具】
     * 判断手机号是否存在
     * @return
     */
    private String isMobileNoExist(String mobileNo){
        if(!mobileNoValidate(mobileNo)){
            return "手机号码格式错误";
        }
        boolean mobileExist = userInfoSerivce.isMobileExist(mobileNo);
        if(mobileExist){
            return UserErrorCode.MOBILENO_EXIST.getDesc();
        }
        return null;
    }

    /**
     * 【APP接口】
     * 发送短信验证码
     * @param mobileNo 手机号码
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/getRegisterMsg", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object getRegisterMsg(String mobileNo){
        try {
            //格式验证
            if(null == mobileNo || !mobileNoValidate(mobileNo)){
                return returnResultMap(false, null, "check", "手机号码格式错误");
            }
            //是否存在
            boolean mobileExist = userInfoSerivce.isMobileExist(mobileNo);
            if(mobileExist){
                return returnResultMap(false, null, "check", UserErrorCode.MOBILENO_EXIST.getDesc());
            }
            //发送
            userInfoSerivce.sendRegisterMsg(mobileNo);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 【APP接口】
     * 注册
     * @param request
     * @param loginName 用户名
     * @param mobileNo 手机号码
     * @param loginPass 登录密码
     * @param validCode 短信验证码
     * @param inviteCode 邀请码
     * @param userSource 用户来源
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/register", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object register(HttpServletRequest request, String loginName, String mobileNo,
    		String loginPass, String validCode, String inviteCode, String userSource){
        try {
        	logger.info(loginName+" "+mobileNo+" "+validCode+" "+inviteCode+" "+userSource);
            
            //验证登录名
            String isLoginNameExist = isLoginNameExist(loginName);
            if(null != isLoginNameExist){
            	return returnResultMap(false, null, "check", isLoginNameExist);
            }
            //验证手机号
            String isMobileNoExist = isMobileNoExist(mobileNo);
            if(null != isMobileNoExist){
            	return returnResultMap(false, null, "check", isMobileNoExist);
            }
            //验证密码
            if(!passwordValidate(loginPass)){
            	return returnResultMap(false, null, "check", "密码请输入6~16位字符，字母加数字组合，字母区分大小写");
            }
            if(null == validCode || "".equals(validCode)){
            	return returnResultMap(false, null, "check", "请输入验证码");
            }
            //验证短信码,并返回
            String validateMsg = validate(mobileNo, validCode,true);
            if(null != validateMsg){
            	return returnResultMap(false, null, "check", validateMsg);
            }
            //验证邀请码
            if(org.apache.commons.lang.StringUtils.isNotEmpty(inviteCode) && !"邀请码 (非必填)".equals(inviteCode)){
                String validateInviteCode = validateInviteCode(inviteCode.trim());
                if(null != validateInviteCode){
                	return returnResultMap(false, null, "check", validateInviteCode);
                }
            }
            //验证用户来源
            if(null == userSource || "".equals(userSource)){
            	return returnResultMap(false, null, "check", "用户来源不能为空");
            }
            
            //注册
            UserInfo user = new UserInfo();
            user.setLoginName(loginName);
            user.setLoginPass(loginPass);
            user.setMobileNo(mobileNo);
            //用户来源
            UserSource source = UserSource.ISO;
            if(UserSource.ANDROID.getValue().equals(userSource)){
            	source = UserSource.ANDROID;
            }
            //邀请码
            InvitationCode invitationCode = userInfoSerivce.getInvitationCodeByCode(inviteCode);
            if(invitationCode==null){
                user = userInfoSerivce.regist(user, source);
            }else{
                user = userInfoSerivce.regist(user, source, invitationCode.getUserId()+"");
            }
        }catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        }catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 【APP接口工具】
     * 校验邀请码
     * @param inviteCode
     * @return
     */
    private String validateInviteCode(String inviteCode) {
        InvitationCode invitationCode = userInfoSerivce.getInvitationCodeByCode(inviteCode);
        if(invitationCode==null){
            return "输入的邀请码不存在";
        }else{
            return null;
        }
    }

    /**
     * 【APP接口工具】
     * 校验短信验证码
     * @param validCode
     * @return
     */
    private String validate(String mobile,String validCode,boolean flag) {
        try {
            boolean bool = smsService.validateMsg(mobile, validCode, TemplateType.SMS_REGISTER_VM,flag);
            if(bool){
            	return null;
            }else {
            	return UserErrorCode.VALIDATE_CODE_ERROR.getDesc();
			}
        }catch(SystemException se){
            logger.error(se.getMessage(),se);
            return se.getMessage();
        }
    }
    
}
