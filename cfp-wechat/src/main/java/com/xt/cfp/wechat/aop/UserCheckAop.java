package com.xt.cfp.wechat.aop;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.constants.UserStatus;
import com.xt.cfp.core.constants.UserType;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.ApplicationContextUtil;
import com.xt.cfp.core.util.WebUtil;
import com.xt.cfp.wechat.annotation.DoNotNeedLogin;
import com.xt.cfp.wechat.controller.BaseController;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by yulei on 2015/7/2.
 */
@Aspect
@Component
public class UserCheckAop extends BaseController{
	
	private static Logger logger = Logger.getLogger(UserCheckAop.class);

	@Autowired
	UserInfoService userInfoService;

    /**
     * 检查某个操作是否需要用户登录
     *
     * @param point
     */
    @Before("execution(* com.xt.cfp.wechat.controller.*.*(..))")
    public void checkLogin(JoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();

		HttpServletRequest request = WebUtil.getHttpServletRequest();
		HttpServletResponse response = WebUtil.getHttpServletResponse();

		UserInfo currentUser = this.getCurrentUser(request);//验证userToken值
		String source = request.getParameter("source");//获取支付来源

		if(null != currentUser && null != source){
			request.getSession().setAttribute("usersources", request.getParameter("userToken"));
		}

        //只处理不带有DoNotNeedLogin注解的方法
        if (method.getAnnotation(DoNotNeedLogin.class) == null && method.getAnnotation(RequestMapping.class) != null) {

            logger.info("method.getName():" + method.getName());
            logger.info("userToken:" + request.getParameter("userToken"));
            logger.info("source:" + request.getParameter("source"));

            //【app通道】【支付和提现接口通道 begin】支付、提现、充值、绑卡 "toPayForLender".equals(method.getName())
            if("toBuyBidLoanByPayAmount".equals(method.getName()) 
            		|| "toWithDraw".equals(method.getName()) 
            		|| "toIncome".equals(method.getName())
					|| "to_bankcard_list".equals(method.getName())
            		|| "toBuyRightsByPayAmount".equals(method.getName())//债权转让专用支付接口toBuyRightsByPayAmount 
            		|| method.getName().indexOf("toMyCreditRightList")>=0
					|| "toFundManage".indexOf(method.getName())>=0
            		||	"toAllMyFinanceList".equals(method.getName())
            		||	"toMyCreditRightList".equals(method.getName())
            		|| "toVoucher".equals(method.getName())//跳转到财富券列表页接口toVoucher
            		||"accountdetailview".equals(method.getName())//资产页
            		||"getAllMyFinanceListDetailBy".equals(method.getName())//我的省心计划详情
            		||"toPaymentCalendar".equals(method.getName())//回款日历
					||"identityAuthentication".equals(method.getName())
					||"doOpenAccount".equals(method.getName())
            		)
            {

        		if(null != currentUser && null != source){
        			if(UserSource.ISO.getValue().equals(source) || UserSource.ANDROID.getValue().equals(source)){
        				currentUser.setAppSource(source);
        				request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, currentUser);
        			}
        		}
        	}
            //【app通道】【支付和提现接口通道 end】
            
            UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
            //如果用户未登录，跳转到注册页面
            if (userInfo == null) {
            	logger.debug("----------用户未登陆");
//				if (method.getAnnotation(ResponseBody.class) != null) {
//					String json = "{'errorCode': '" + UserErrorCode.LONGIN_EXIST.getCode() + "','errorMsg':'" + UserErrorCode.LONGIN_EXIST.getDesc() + "'}";
//					response.setStatus(500);
//					response.getWriter().write(json);
//				} else {
//					response.sendRedirect(request.getContextPath() + "/user/regist/home");
//				}
//				return;
                throw new SystemException(UserErrorCode.LONGIN_EXIST).set("method", method.getName());
            }else{
            	String reqHead=request.getHeader("User-Agent");
				logger.info("连接请求头为:" + reqHead);
            	if(reqHead.indexOf("MicroMessenger")>0){
					boolean result=true;
					result=userInfoService.validUserJsession(userInfo.getUserId().toString(),request.getRequestedSessionId());

					if(!result){
						throw new SystemException(UserErrorCode.LOGIN_ON_OTHERS).set("method", method.getName());
					}
				}
			}
            //如果用户的状态是不正常的，跳转到异常页面
            UserInfoService userInfoService = ApplicationContextUtil.getBean(UserInfoService.class);
            UserInfo userByUserId = userInfoService.getUserByUserId(userInfo.getUserId());
            if (!UserStatus.NORMAL.getValue().equals(userByUserId.getStatus())) {
                throw new SystemException(UserErrorCode.STATUS_IS_NOT_NORMAL).set("method", method.getName());
            }

            //如果用户类型不是普通用户，跳转到异常页面
            if (!userInfo.getType().equals(UserType.COMMON.getValue())) {
                throw new SystemException(UserErrorCode.CAN_NOT_LOGIN).set("method", method.getName());
            }

        }else {//请求不需要登录的地址

            logger.info("NotNeedLogin_method.getName():" + method.getName());
            logger.info("NotNeedLogin_userToken:" + request.getParameter("userToken"));
            logger.info("NotNeedLogin_source:" + request.getParameter("source"));

            //【app通道】【跳转到正常标和债权转让标详情接口通道 begin】
            if("bidding".equals(method.getName())
            	|| "creditRightBidding".equals(method.getName())
            	|| "increaseRate".equals(method.getName()) //加息券活动抽奖
            	|| "toSxDetail".equals(method.getName())	//省心详情
            	|| "index".equals(method.getName())){
        		if(null != currentUser && null != source){
        			if(UserSource.ISO.getValue().equals(source) || UserSource.ANDROID.getValue().equals(source)){
        				currentUser.setAppSource(source);
        				request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, currentUser);
        			}
        		}
        	}
            //【app通道】【跳转到正常标和债权转让标详情接口通道end】
			
		}
    }
}
