package com.xt.cfp.wechat.controller;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.pojo.InvitationCode;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserOpenId;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.UserOpenIdService;
import com.xt.cfp.core.util.*;
import com.xt.cfp.wechat.annotation.DoNotNeedLogin;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by luqinglin on 2015/7/2.
 */
@Controller
@RequestMapping(value = "/user")
public class UserLoginController extends BaseController {
	private static Logger logger = Logger.getLogger(UserLoginController.class);
    @Autowired
    private UserInfoService userInfoSerivce;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private UserOpenIdService userOpenIdService;

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
    public Object login(HttpServletRequest request,String loginName,String loginPass,String pastUrl){
    	RequestUtil.validateRequestMethod(request,RequestMethod.POST);
    	List<String> result = new ArrayList<String>();
        //数据格式校验
        boolean validate = validate(loginName, loginPass, result);
        if (!validate)
            return result;
        //登陆
        try{
            UserInfo loginUser = userInfoSerivce.login(loginName, loginPass);
            if (loginUser!=null){
                //保存登陆信息
                request.getSession().setAttribute(Constants.USER_ID_IN_SESSION,loginUser);
                //记录用户登陆信息  和  记录openid和user绑定
                String openId = (String)request.getSession().getAttribute("openId");
                
                if(openId == null || "".equals(openId)){
                	userInfoSerivce.recordUser(loginUser);
                }else{
                    UserOpenId seachUserOpenId = userOpenIdService.getOpenIdByCondition(loginUser.getUserId(), null, "1");
                    UserOpenId seachUserOpenId2 = userOpenIdService.getOpenIdByCondition(null, openId, "1");
                    if(null != seachUserOpenId && null != seachUserOpenId2 && seachUserOpenId.getUserId().compareTo(seachUserOpenId2.getUserId())==0){
                        userInfoSerivce.recordUser(loginUser);
                    }else{
                        if(null != seachUserOpenId){
                            userOpenIdService.deleteOpenId(seachUserOpenId);
                        }
                        if(null != seachUserOpenId2){
                            userOpenIdService.deleteOpenId(seachUserOpenId2);
                        }
                        UserOpenId userOpenId = new UserOpenId();
                        userOpenId.setOpenId(openId);
                        userOpenId.setUserId(loginUser.getUserId());
                        userOpenId.setType("1");
                        userOpenId.setGetTime(new Date());
                        userOpenIdService.addUserOpenId(loginUser, userOpenId);
                    }

                }
                //redis中存入用户标识
                userInfoSerivce.saveUserJsession(loginUser.getUserId().toString(),request.getRequestedSessionId());
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
        	se.printStackTrace();
            result.add(JsonView.JsonViewFactory.create().success(false).info(se.getMessage()).put("id", "unlogin")
                    .toJson());
            return result;
        }

    }

    @RequestMapping(value = "/logout")
    @DoNotNeedLogin
    public String logout(HttpServletRequest request){
    	UserInfo currentUser = SecurityUtil.getCurrentUser(true);
    	UserOpenId seachUserOpenId = userOpenIdService.getOpenIdByCondition(currentUser.getUserId(), null, null);
    	if(seachUserOpenId != null){
            userOpenIdService.deleteOpenId(seachUserOpenId);
    	}
    	request.getSession().removeAttribute(Constants.USER_ID_IN_SESSION);
        //进入首页
        return "redirect:/";
    }


    /**
     * 跳转至登陆页
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping("/toLoginCreditor")
    public String toLoginCreditor(HttpServletRequest request){
    	String flag = request.getParameter("flag");
    	if(flag!=null)
    		request.setAttribute("pastUrl", "/finance/creditRightBidding?creditorRightsApplyId="+flag);
    	else
    		request.setAttribute("pastUrl","/");
        return "/login/login";
    }
    
    /**
     * 跳转至登陆页
     * @return
     * @author wangyadong
     */
    @DoNotNeedLogin
    @RequestMapping("/toLogin")
    public String toLoginPage(HttpServletRequest request,String invite_code,String pastUrl){
    	if(!StringUtils.isNull(invite_code)){
    		request.setAttribute("invite_code",invite_code);
    	}
    	String flag = request.getParameter("flag");
    	if(flag!=null)
    		request.setAttribute("pastUrl", "/finance/bidding?loanApplicationNo="+flag);
    	else
    		if(StringUtils.isNull(pastUrl)){
    			request.setAttribute("pastUrl","/");
    		}else{
    			request.setAttribute("pastUrl",pastUrl);
    		}
    		
        return "/login/login";
    }
    /**
     * 跳转至微信主页
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping("/toIndex")
    public String toIndex(HttpServletRequest request){
        return "/index";
    }


    /**
     * 进入404页
     */
    @RequestMapping(value = "/404")
    @DoNotNeedLogin
    public String toError(HttpServletRequest request) {
        return "404";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/getConfig")
    @ResponseBody
    public String getWechatConfig(HttpServletRequest request){
    	//request.getHeader("Referer");
    	//2 获得当次请求的URI请求相对路径和请求参数
    	//request.getRequestURI()+"?"+request.getQueryString();
    	//System.out.println(request.getRequestURI());
    	String inviteURL = "http://m.caifupad.com/person/beinvite";
    	String codeIsNull= "0";
    	Object attribute = WebUtil.getHttpServletRequest().getSession().getAttribute(Constants.USER_ID_IN_SESSION);
    	if(attribute != null){
    		UserInfo user = (UserInfo) attribute;
    		Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", user.getUserId());
            param.put("type", 0);
            InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
            if (invite != null) {
                String invitecode = invite.getInvitationCode();
                request.setAttribute("invitecode",invitecode);
                inviteURL="http://m.caifupad.com/person/beinvite" + "?invite_code=" + invitecode;
            }
    	}else{
    		codeIsNull="1";
    	}
    	//System.out.println(request.getHeader("Referer"));
    	Map<String,String> resultMap = null;
    	String url = request.getHeader("Referer");//request.getRequestURI()+"?"+request.getQueryString();
    	/*Date date = new Date();
        if(request.getSession().getAttribute("expiredTime") == null || (date.getTime()/1000) > Long.valueOf(request.getSession().getAttribute("expiredTime").toString())){
        	//to do重新获取
        	String access_token = Sign.getAccessToken();
        	String jsapi_ticket = Sign.getJsapiTicket(access_token);
        	Date resultTime = new Date();
        	request.getSession().setAttribute("expiredTime", resultTime.getTime()/1000+3600);
        	request.getSession().setAttribute("access_token", access_token);
        	request.getSession().setAttribute("jsapi_ticket", jsapi_ticket);
        	resultMap = Sign.sign(jsapi_ticket, url);
        }else{
        	resultMap = Sign.sign(request.getSession().getAttribute("jsapi_ticket").toString(), url);
        }*/
    	resultMap = userOpenIdService.getConfig(url);
        logger.info("结果"+resultMap);
        if(resultMap == null){
        	return JsonView.JsonViewFactory.create().success(false).info("")
                    .toJson();
        }else{
        	resultMap.put("inviteURL", inviteURL);
        	resultMap.put("codeIsNull", codeIsNull);
        	resultMap.put("appid", PropertiesUtils.getInstance().get("WECHAT_APPID"));
        	return JsonView.JsonViewFactory.create().success(true).info("").put("resultMap", resultMap)
                    .toJson();
        }
        
    }
}
