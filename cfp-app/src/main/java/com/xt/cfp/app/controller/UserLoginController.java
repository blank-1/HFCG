package com.xt.cfp.app.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.app.annotation.DoNotNeedLogin;
import com.xt.cfp.app.security.Des3;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.redis.RedisCacheManger;

@Controller
@RequestMapping(value = "/user")
public class UserLoginController extends BaseController {
	
	private static Logger logger = Logger.getLogger(UserLoginController.class);
	
    @Autowired
    private UserInfoService userInfoSerivce;
    @Autowired
    private RedisCacheManger redisCacheManger;

    /**
     * 【APP接口】
     * 登录
     * @param request
     * @param loginName 用户名或手机号
     * @param loginPass 登录密码
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/login", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object login(HttpServletRequest request, String loginName, String loginPass){
        try{
        	
        	//解密处理
        	loginName = Des3.decode(loginName);
        	loginPass = Des3.decode(loginPass);
        	
        	//数据格式校验
            if(!loginNameValidate(loginName)&&!mobileNoValidate(loginName)){
            	return returnResultMap(false, null, "check", "登录名或手机号格式不正确");
            }else if (!passwordValidate(loginPass)) {
            	return returnResultMap(false, null, "check", "密码请输入6~16位字符，字母加数字组合，字母区分大小写");
			}
        	
        	//登录验证
            UserInfo loginUser = userInfoSerivce.login(loginName, loginPass);
            if (loginUser != null){
            	Integer loginTokenTime = 604800;//默认时间（秒）
    			
    			//读取config json文件
    			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/config.json";
    			JSONObject jsonObject = this.getJSONObjectByJson(path);
    			if(null != jsonObject && jsonObject.getIntValue("loginTokenTime") > 0){
    				loginTokenTime = jsonObject.getIntValue("loginTokenTime");
    			}
            	
            	//清理用户重复登陆，产生的token值
            	String loginUserToken = redisCacheManger.getRedisCacheInfo("app_login_"+loginUser.getUserId());//验证用户是否已登录
            	if(null != loginUserToken){//如果已登录，清理token值和登录标记userid
            		redisCacheManger.destroyRedisCacheInfo(loginUserToken);
            		redisCacheManger.destroyRedisCacheInfo("app_login_"+loginUser.getUserId());
            	}
            	
            	//分配用户新的登录token值，将用户信息存入redis
            	String userToken = UUID.randomUUID().toString();
            	logger.info("登录user_Token:" + userToken);
            	redisCacheManger.setRedisCacheInfo(userToken, loginUser.getUserId().toString(), loginTokenTime);//key=token;value=userid
            	redisCacheManger.setRedisCacheInfo("app_login_"+loginUser.getUserId().toString(), userToken, loginTokenTime);//key=app_login_userid;value=token
            	
            	//记录登录信息
                userInfoSerivce.recordUser(loginUser);
                
                //返回结果
                Map<String,String> resultMap = new HashMap<String,String>();
                resultMap.put("userToken", userToken);
                resultMap.put("userTokenTime", String.valueOf(loginTokenTime));
                resultMap.put("loginName", loginUser.getLoginName());
                return returnResultMap(true, resultMap, null, null);
            }else{//密码错误
                return returnResultMap(false, null, "check", UserErrorCode.ERROR_PASS_LOGIN.getDesc());
            }
        }catch (SystemException se){
            logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }

    /**
     * 【APP接口】
     * 登出
     * @param request
     * @param userToken 用户Token值
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/logout", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object logout(HttpServletRequest request, String userToken){
    	try {
    		logger.info("登出userToken:" + userToken);
    		UserInfo loginUserInfo = this.getCurrentUser(request);
    		if(null != loginUserInfo){
    			redisCacheManger.destroyRedisCacheInfo("app_login_"+loginUserInfo.getUserId());
    		}
    		redisCacheManger.destroyRedisCacheInfo(userToken);
		} catch (SystemException se){
            logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    	return returnResultMap(true, null, null, null);
    }
    
    /**
     * 【APP接口】
     * 追加UserToken有效时间
     * @param request
     * @param userToken 用户Token值
     * @return
     */
    @RequestMapping(value = "/addUserTokenTime", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object addUserTokenTime(HttpServletRequest request, String userToken){
    	try {
    		logger.info("addUserTokenTime:" + userToken);
    		UserInfo loginUserInfo = this.getCurrentUser(request);
    		if(null != loginUserInfo){
    			Integer addTokenTime = 86400;//默认追加时间（秒）
    			
    			//读取config json文件
    			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/config.json";
    			JSONObject jsonObject = this.getJSONObjectByJson(path);
    			if(null != jsonObject && jsonObject.getIntValue("addTokenTime") > 0){
    				addTokenTime = jsonObject.getIntValue("addTokenTime");
    			}
    			
    			//追加有效时间
    			redisCacheManger.setRedisCacheInfo(userToken, loginUserInfo.getUserId().toString(), addTokenTime);//key=token;value=userid
            	redisCacheManger.setRedisCacheInfo("app_login_"+loginUserInfo.getUserId().toString(), userToken, addTokenTime);//key=app_login_userid;value=token
            	//返回结果
                Map<String,String> resultMap = new HashMap<String,String>();
                resultMap.put("userToken", userToken);
                resultMap.put("userTokenTime", String.valueOf(addTokenTime));
                return returnResultMap(true, resultMap, null, null);
    		}else {
    			return returnResultMap(false, null, "needlogin", "请先登录");
			}
		} catch (SystemException se){
            logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
   
}
