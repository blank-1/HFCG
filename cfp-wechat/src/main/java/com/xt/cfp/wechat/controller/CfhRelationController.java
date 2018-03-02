package com.xt.cfp.wechat.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.constants.UserIsVerifiedEnum;
import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.ext.cfh.CfhUserRelation;
import com.xt.cfp.core.security.util.Des3;
import com.xt.cfp.core.service.DefaultInterestDetailService;
import com.xt.cfp.core.service.LendOrderReceiveService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LoanFeesDetailService;
import com.xt.cfp.core.service.RepaymentPlanService;
import com.xt.cfp.core.service.SmsService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.cfh.CfhRelationService;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.RequestUtil;
import com.xt.cfp.core.util.SecurityUtil;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.wechat.annotation.DoNotNeedLogin;

/**
 * 财富汇-财富派关联
 * 
 * @创建时间 2016年4月20日 下午2:12:29
 *
 */
@Controller
@RequestMapping(value = "/cfhRelation")
public class CfhRelationController extends BaseController {
	private static Logger logger = Logger.getLogger(CfhRelationController.class);
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private UserInfoExtService userInfoExtService;
	@Autowired
	private CfhRelationService cfhRelationService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private RedisCacheManger redisCacheManger;
	@Autowired
	private LendOrderService lendOrderService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private LendOrderReceiveService lendOrderReceiveService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private LoanFeesDetailService loanFeesDetailService;
	@Autowired
	private DefaultInterestDetailService defaultInterestDetailService;

	@DoNotNeedLogin
	@RequestMapping(value = "/tobind")
	public String tobind(HttpServletRequest request, String hToken) {
		if (StringUtils.isNull(hToken)) {
			throw new SystemException(SystemErrorCode.PARAM_MISS);
		}

		String cfhUserId = redisCacheManger.getRedisCacheInfo(hToken);
		if (null == cfhUserId) {
			throw new SystemException(UserErrorCode.STATUS_IS_INVALID).set("hToken", hToken);
		}

		CfhUserRelation userRelation = cfhRelationService.getCfpUserByCfhUserId(Long.valueOf(cfhUserId));
		if (null == userRelation) {
			request.setAttribute("hToken", hToken);
			return "/cfh/cfhLogin";
		} else {
			UserInfo userInfo = userInfoService.getUserByUserId(userRelation.getUserId());
			if (null == userInfo) {
				throw new SystemException(UserErrorCode.USER_NOT_EXIST).set("userId", userRelation.getUserId());
			}

			// 保存登陆信息
			request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, userInfo);
			userInfoService.recordUser(userInfo);
			
			return "redirect:/finance/list";
		}
	}

	@DoNotNeedLogin
	@RequestMapping(value = "/login")
	@ResponseBody
	public Object login(HttpServletRequest request, String loginName, String loginPass) {
		RequestUtil.validateRequestMethod(request, RequestMethod.POST);

		if (!loginNameValidate(loginName) && !mobileNoValidate(loginName)) {
			return JsonView.JsonViewFactory.create().success(false).info("登陆名或手机号格式不正确").put("id", "unlogin").toJson();
		}
		// 验证密码
		if (!passwordValidate(loginPass)) {
			return JsonView.JsonViewFactory.create().success(false).info("密码格式不正确").put("id", "pwlogin").toJson();
		}

		// 登陆
		try {
			UserInfo loginUser = userInfoService.login(loginName, loginPass);
			if (loginUser != null) {
				// 保存登陆信息
				request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, loginUser);
				userInfoService.recordUser(loginUser);

				return JsonView.JsonViewFactory.create().success(true).info("登陆成功").toJson();
			} else {// 密码错误
				return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.ERROR_PASS_LOGIN.getDesc()).put("id", "pwlogin").toJson();
			}
		} catch (SystemException se) {
			// 用户名或手机号不存在
			return JsonView.JsonViewFactory.create().success(false).info(se.getMessage()).put("id", "unlogin").toJson();
		}

	}

	@RequestMapping(value = "/toVerified")
	public String toVerified(HttpServletRequest request, String hToken, String type) {

		if (StringUtils.isNull(hToken)) {
			throw new SystemException(SystemErrorCode.PARAM_MISS).set("hToken", hToken);
		}

		UserInfo user = SecurityUtil.getCurrentUser(true);
		UserInfoExt userExt = userInfoExtService.getUserInfoExtById(user.getUserId());
		request.setAttribute("hToken", hToken);
		request.setAttribute("verifiedState", userExt.getIsVerified());
		request.setAttribute("JMmobileNo", user.getEncryptMobileNo());
		request.setAttribute("type", type);
		String mobileNo = null;
		try {
			mobileNo = Des3.encode(user.getMobileNo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("mobileNo", mobileNo);
		return "/cfh/verified";
	}

	/**
	 * 【Cfh接口】 我的投资信息接口
	 * 
	 * @param request
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/account", method = RequestMethod.POST)// [APP]
	@ResponseBody
	public Object accountOverview(HttpServletRequest request, @RequestParam(value = "hToken", required = false) String hToken) {
		try {
			DecimalFormat df = new DecimalFormat("#,##0.00");
			Map<String, Object> resultMap = new HashMap<String, Object>();
			logger.info("【Cfh接口】 我的投资信息接口,hToken=" + hToken);
			// 登录验证
			if (null != hToken && !"".equals(hToken)) {
				String cfhUserId = redisCacheManger.getRedisCacheInfo(hToken);
				if (null != cfhUserId) {
					CfhUserRelation userRelation = cfhRelationService.getCfpUserByCfhUserId(Long.valueOf(cfhUserId));
					if (null != userRelation) {
						UserInfo userInfo = userInfoService.getUserByUserId(userRelation.getUserId());
						if (null != userInfo) {
							// 待回本金（投标）
							BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(null, userInfo.getUserId(),
									LendProductTypeEnum.RIGHTING, LendProductTypeEnum.CREDITOR_RIGHTS);
							resultMap.put("netAsset", df.format(getNetAsset(userInfo)));// 净资产
							resultMap.put("capitalRecive", df.format(capitalRecive));// 待回本金（投标）
							return returnResultMap(true, resultMap, null, null);
						}else {
							logger.info("userInfo is empty!");
						}
					}else {
						logger.info("userRelation is empty!");
					}
				}else {
					logger.info("cfhUserId is empty!");
				}
			}else {
				logger.info("hToken is empty!");
			}
			resultMap.put("netAsset", "0.00");// 净资产
			resultMap.put("capitalRecive", "0.00");// 待回本金（投标）
			return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se) {
			logger.error(se.getMessage(), se);
			return returnResultMap(false, null, "exception", se.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return returnResultMap(false, null, "exception", e.getMessage());
		}
	}

	/**
	 * 【Cfh接口工具】 获取用户净资产
	 * 
	 * @param currentUser
	 * @return
	 */
	private BigDecimal getNetAsset(UserInfo currentUser) {
		// 净资产=账户余额+（待回本金+待回利息+持有理财计划）-（待还本金+待还利息+待缴费用+待还罚息）
		UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
		// 待回本金（投标）
		BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(null, currentUser.getUserId(), LendProductTypeEnum.RIGHTING,
				LendProductTypeEnum.CREDITOR_RIGHTS);
		// 待回收益（投标）
		BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(null, currentUser.getUserId(), LendProductTypeEnum.RIGHTING,
				LendProductTypeEnum.CREDITOR_RIGHTS);
		// 持有全部理财计划
		BigDecimal totalHoldFinancePlan = lendOrderService.getTotalHoldFinancePlan(currentUser.getUserId());
		// 待还本金
		BigDecimal replaymentCapital = repaymentPlanService.getRepaymentCapitalByUserId(currentUser.getUserId());
		// 待还利息
		BigDecimal replaymentInterest = repaymentPlanService.getRepaymentInterestByUserId(currentUser.getUserId());
		// 待缴费用
		BigDecimal loanFeeNopaied = loanFeesDetailService.getLoanFeeNoPaied(currentUser.getUserId());
		// 待还罚息
		BigDecimal interestPaid = defaultInterestDetailService.getDefaultInterestByUserId(currentUser.getUserId());

		return cashAccount.getValue2().add(totalHoldFinancePlan).add(capitalRecive).add(interestRecive).subtract(replaymentCapital)
				.subtract(replaymentInterest).subtract(loanFeeNopaied).subtract(interestPaid);
	}

	@ResponseBody
	@RequestMapping(value = "/sendSms")
	public Object sendSms(String mobileNo) {
		try {
			if (StringUtils.isNull(mobileNo)) {
				return returnResultMap(false, null, "check", "手机号码不能为空");
			}
			// 解密处理
			mobileNo = Des3.decode(mobileNo);
			if (!mobileNoValidate(mobileNo)) {
				return returnResultMap(false, null, "check", "手机号格式错误");
			}

			UserInfo userInfo = userInfoService.getUserByMobileNo(mobileNo);
			if (null == userInfo) {
				return returnResultMap(false, null, "check", "手机号码不存在");
			}
			boolean checkResult = this.checkMsgNum(mobileNo);// 记录短信校验次数
			if (!checkResult) {
				return returnResultMap(false, null, "check", "验证次数过多,请24小时后再试");
			}

			cfhRelationService.sendSms(mobileNo);

			return returnResultMap(true, null, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return returnResultMap(false, null, "exception", e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping(value = "/bind")
	public Object bind(String realName, String idCard, String validCode, String type, String hToken) {

		if (StringUtils.isNull(hToken)) {
			return returnResultMap(false, null, "check", "hToken不能为空");
		}

		UserInfo user = SecurityUtil.getCurrentUser(true);
		UserInfoExt userExt = userInfoExtService.getUserInfoExtById(user.getUserId());

		if (type.equals("verified")) {
			if (StringUtils.isNull(validCode)) {
				return returnResultMap(false, null, "check", "验证码不能为空");
			}
			if (StringUtils.isNull(idCard)) {
				return returnResultMap(false, null, "check", "身份证不能为空");
			}

			if (!validateBindSms(user.getMobileNo(), validCode, true)) {
				return returnResultMap(false, null, "check", "验证码错误");
			} else {
				destroyCheckMsgNum(user.getMobileNo());
			}

		}

		if (!idCardValidate(idCard)) {
			return returnResultMap(false, null, "check", "身份证格式不正确");
		}

		if (null != userExt.getIsVerified() && userExt.getIsVerified().equals("1")) {// 已认证
			if (!userExt.getIdCard().equals(idCard)) {// 已认证
				return returnResultMap(false, null, "check", "身份证匹配失败");
			}
		} else {
			if (StringUtils.isNull(realName)) {
				return returnResultMap(false, null, "check", "姓名不能为空");
			}

			int userExist = userInfoExtService.identityBindingExist(idCard, realName);
			if (userExist > 0) {
				return returnResultMap(false, null, "check", "该身份证已被占用");
			}
			//验证是否已经认证过
	        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(user.getUserId());
	        if(UserIsVerifiedEnum.YES.getValue().equals(userInfoExt.getIsVerified())){
	        	return returnResultMap(false, null, "check", "当前用户已经认证过");
	        }
			boolean resultFlag = userInfoExtService.identityAuthentication(idCard, realName, user.getUserId());
			if (!resultFlag) {
				return returnResultMap(false, null, "check", "身份证和姓名验证失败");
			}
		}

		String cfhUserId = redisCacheManger.getRedisCacheInfo(hToken);
		if (StringUtils.isNull(cfhUserId)) {
			return returnResultMap(false, null, "check", "无法查询到财富汇关联用户");
		}

		CfhUserRelation userRelation = new CfhUserRelation();
		userRelation.setUserId(user.getUserId());
		userRelation.setCfhUserId(Long.valueOf(cfhUserId));
		userRelation.setCreateTime(new Date());
		cfhRelationService.addCfhUserRelation(userRelation);
		return returnResultMap(true, userRelation.getRelationId(), null, null);

	}

	@ResponseBody
	@RequestMapping(value = "/sendRelationVerified")
	public void sendRelationVerified(String relationId) {
		if (!StringUtils.isNull(relationId)) {
			cfhRelationService.sendRelationVerified(Long.valueOf(relationId));
		}
	}

	/**
	 * 【工具】记录短信校验次数
	 */
	private boolean checkMsgNum(String mobile_no) throws ParseException {
		int seconds = DateUtil.getLeftTimesToday();// 当天剩余时间，秒
		Integer num = 0;
		String numStr = redisCacheManger.getRedisCacheInfo("app_forgetPsw_" + mobile_no);
		if (null != numStr) {
			num = Integer.valueOf(numStr);
		}
		num = num + 1;
		redisCacheManger.setRedisCacheInfo("app_forgetPsw_" + mobile_no, String.valueOf(num), seconds);// key=mobile_no;value=checkMsgNum
		if (num >= 10) {
			return false;
		}
		return true;
	}

	/**
	 * 【工具】销毁短信校验次数记录
	 */
	private void destroyCheckMsgNum(String mobile_no) {
		redisCacheManger.destroyRedisCacheInfo("app_forgetPsw_" + mobile_no);
	}

	/**
	 * 短信验证码校验
	 * 
	 * @param mobile
	 * @param validCode
	 * @param flag
	 * @return
	 */
	private boolean validate(String mobile, String validCode, boolean flag) {
		try {
			return smsService.validateMsg(mobile, validCode, TemplateType.SMS_REGISTER_VM, flag);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 绑定短信验证码校验
	 * 
	 * @param mobile
	 * @param validCode
	 * @param flag
	 * @return
	 */
	private boolean validateBindSms(String mobile, String validCode, boolean flag) {
		try {
			return smsService.validateMsg(mobile, validCode, TemplateType.SMS_CFH_BIND_VM, flag);
		} catch (Exception e) {
			return false;
		}
	}

	@DoNotNeedLogin
	@RequestMapping(value = "/regist")
	public String toRegister(HttpServletRequest req, String hToken) {
		req.setAttribute("hToken", hToken);
		// 用于记录页面登陆成功后的跳转页面
		return "/cfh/cfhRegister";
	}

	/**
	 * 注册
	 * 
	 * @param request
	 * @param userInfo
	 * @param validCode
	 * @param inviteUserId
	 *            邀请好友id
	 * @return
	 */
	@DoNotNeedLogin
	@ResponseBody
	@RequestMapping(value = "/register")
	public String register(HttpServletRequest request, UserInfo userInfo, String validCode) {
		if (!loginNameValidate(userInfo.getLoginName())) {
			return JsonView.JsonViewFactory.create().success(false).info("用户名格式错误").put("id", "username").toJson();
		}
		// 手机号验证
		if (mobileNoValidate(userInfo.getLoginName())) {
			return JsonView.JsonViewFactory.create().success(false).info("手机号不可作为用户名使用'").put("id", "username").toJson();
		}
		boolean loginNameExist = userInfoService.isLoginNameExist(userInfo.getLoginName());
		if (loginNameExist) {
			return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.LOGINNAME_EXIST.getDesc()).put("id", "username").toJson();
		}

		if (!mobileNoValidate(userInfo.getMobileNo())) {
			return JsonView.JsonViewFactory.create().success(false).info("手机号码格式错误").put("id", "phone").toJson();
		}
		boolean mobileExist = userInfoService.isMobileExist(userInfo.getMobileNo());
		if (mobileExist) {
			return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.MOBILENO_EXIST.getDesc()).put("id", "phone").toJson();
		}

		if (!passwordValidate(userInfo.getLoginPass())) {
			return JsonView.JsonViewFactory.create().success(false).info("请输入6~16位字符，字母加数字组合，字母区分大小写").put("id", "password").toJson();
		}

		if (!validate(userInfo.getMobileNo(), validCode, true)) {
			return JsonView.JsonViewFactory.create().success(false).info("短信验证码输入错误").put("id", "validCode").toJson();
		}

		// 注册
		UserInfo user = userInfoService.regist(userInfo, UserSource.WECHAT);
		// 保存用户session值
		request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, user);
		return JsonView.JsonViewFactory.create().success(true).toJson();

	}
	
	/**
	 * 【Cfh接口】 跳转到微信资产概要页面
	 * @param request
	 * @param hToken
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/toOverview")
	public String toOverview(HttpServletRequest request, String hToken) {
		if (StringUtils.isNull(hToken)) {
//			throw new SystemException(SystemErrorCode.PARAM_MISS);
			return "redirect:/user/toLogin";
		}

		String cfhUserId = redisCacheManger.getRedisCacheInfo(hToken);
		if (null == cfhUserId) {
//			throw new SystemException(UserErrorCode.STATUS_IS_INVALID).set("hToken", hToken);
			return "redirect:/user/toLogin";
		}

		CfhUserRelation userRelation = cfhRelationService.getCfpUserByCfhUserId(Long.valueOf(cfhUserId));
		if (null == userRelation) {
			request.setAttribute("hToken", hToken);
			return "/cfh/cfhLogin";
		} else {
			UserInfo userInfo = userInfoService.getUserByUserId(userRelation.getUserId());
			if (null == userInfo) {
//				throw new SystemException(UserErrorCode.USER_NOT_EXIST).set("userId", userRelation.getUserId());
				return "redirect:/user/toLogin";
			}

			// 保存登陆信息
			request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, userInfo);
			userInfoService.recordUser(userInfo);
			
			return "redirect:/person/account/overview";
		}
	}

}
