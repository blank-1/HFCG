package com.xt.cfp.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.constants.WechatActConstants;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.IncreaseVO;
import com.xt.cfp.core.pojo.ext.VoucherPhoneVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.*;
import com.xt.cfp.wechat.annotation.DoNotNeedLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/game")
public class GameController extends BaseController {

    @Autowired
    private VoucherPhoneService voucherPhoneService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ConstantDefineService constantDefineService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private UserInfoService userInfoSerivce;

    @DoNotNeedLogin
    @RequestMapping(value = "/validCode")
    @ResponseBody
    public String validCode(HttpServletRequest request, String mobileNo, String code) {
        try {
            //验证手机号是否存在
            if (!mobileNoValidate(mobileNo)) {
                return JsonView.JsonViewFactory.create().success(false).info("手机号码格式错误").put("id", "phone").toJson();
            }
            boolean bool = smsService.validateMsg(mobileNo, code, TemplateType.SMS_VALID_VM, true);
            if (bool) {
                UserInfo loginUser = userInfoSerivce.getUserByMobileNo(mobileNo);
                if (null == loginUser)
                    return JsonView.JsonViewFactory.create().success(false).info("用户信息为空").toJson();
                request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, loginUser);
                userInfoSerivce.recordUser(loginUser);
                return JsonView.JsonViewFactory.create().success(true).info("").put("id", "valid").toJson();
            } else {
                return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.VALIDATE_CODE_ERROR.getDesc()).put("id", "valid").toJson();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonView.JsonViewFactory.create().success(false).info("").toJson();
        }
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/toValidCode")
    public String validCode(HttpServletRequest request) {

        return "/games/bill/validCode";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/sendMsg")
    @ResponseBody
    public String sendMsg(HttpServletRequest request, String mobileNo) {
        //post请求验证
        RequestUtil.validateRequestMethod(request, RequestMethod.POST);

        try {
            //验证手机号是否存在
            if (!mobileNoValidate(mobileNo)) {
                return JsonView.JsonViewFactory.create().success(false).info("手机号码格式错误").put("id", "phone").toJson();
            }
            userInfoSerivce.sendValidCode(mobileNo);
            return JsonView.JsonViewFactory.create().success(true).info("").toJson();
        } catch (SystemException se) {
            logger.error(se.getMessage(), se);
            return JsonView.JsonViewFactory.create().success(false).info("").toJson();
        }
    }


    /**
     * 跳转至年度账单index页面  第一个页面
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/yearBillIndex")
    public String yearBillIndex(Model model) {
        Calendar instance = Calendar.getInstance();
        int currentYear = instance.get(Calendar.YEAR);
        int lastYear = currentYear - 1;
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("lastYear", lastYear);
        return "/games/bill/index";
    }

    /**
     * 跳转至年度账单data页面 第二个页面
     *
     * @param request
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/yearBillData")
    public String yearBillData(HttpServletRequest request) {
        return "/games/bill/data";
    }

    /**
     * 跳转至年度账单data页面 第三个页面
     *
     * @param request
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/yearBillCheck")
    public String yearBillCheck(HttpServletRequest request) {
        return "/games/bill/validCode";
    }


    /**
     * 跳转至年度账单data页面 第四个页面
     *
     * @param request
     * @return
     */

    @DoNotNeedLogin
    @RequestMapping(value = "/yearBill")
    public String yearBill(HttpServletRequest request) {
        UserInfo user = (UserInfo) request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
        if (null == user) {
            return "/games/bill/validCode";
        }
        PlatformBill bill = utilsService.getPlatformBillByUserId(user.getUserId());
        if (null != bill && null != bill.getIsview() && bill.getIsview() != 1) {
            bill.setIsview(1);
            bill.setFastViewTime(new Date());
            utilsService.updatePlatformBill(bill);
        }
        request.setAttribute("bill", bill);
        return "/games/bill/detail";
    }

    /**
     * 手撕鬼子页跳转
     *
     * @param request
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/result")
    public String result(HttpServletRequest request) {
        request.setAttribute("num", request.getAttribute("num"));
        return "/games/handteardevil/result";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/illustrate")
    public String illustrate(HttpServletRequest request) {
        return "/games/handteardevil/illustrate";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request) {
        return "/games/handteardevil/index";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/toValidate")
    public String toValidate(HttpServletRequest request) {
        return "/games/handteardevil/validate";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/validate")
    public String validate(HttpServletRequest request, @RequestParam(value = "phone") String phone) {
        if (phone == null) {
            return "/games/handteardevil/repeat";
        }
        if (!mobileNoValidate(phone)) {
            return "/games/handteardevil/repeat";
        }
        //查存不存在
        VoucherPhone voucherPhone = voucherPhoneService.getVoucherPhone(phone);
        //没有添加
        if (voucherPhone == null) {
            VoucherPhone addVoucherPhone = new VoucherPhone();
            addVoucherPhone.setMobileNo(phone);
            voucherPhoneService.addVoucherPhone(addVoucherPhone);
            return "/games/handteardevil/receive";
        } else {
            return "/games/handteardevil/repeat";
        }
    }


    /**
     * 中秋活动页跳转
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/telphoney")
    public String telphoney() {
        return "/games/midautumnfestival/telephony";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/converse")
    public String converse(HttpServletRequest request) {
        return "/games/midautumnfestival/converse";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/activity")
    public String activity(HttpServletRequest request) {
        return "/games/midautumnfestival/activity";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/receive")
    public String receive(HttpServletRequest request) {
        return "/games/midautumnfestival/receive";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/message")
    public String message(HttpServletRequest request) {
        return "/games/midautumnfestival/message";
    }

    /**
     * 加盟商大会
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/toAllianceWares")
    public String toAllianceWares(HttpServletRequest request) {
        TokenUtils.setNewToken(request);
        return "/games/franchiseeConference/AllianceWares";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/franchiseeSuccess", method = RequestMethod.POST)
    public String franchiseeSuccess(HttpServletRequest request, @RequestParam(value = "phone") String phone,
                                    @RequestParam(value = "realName") String realName, @RequestParam(value = "activityNum") String activityNum) {
        RequestUtil.validateRequestMethod(request, RequestMethod.POST);
        TokenUtils.validateToken(request);
        VoucherPhone voucherPhone = new VoucherPhone();
        voucherPhone.setMobileNo(phone);
        voucherPhone.setRealName(realName);
        voucherPhone.setCreateTime(new Date());
        voucherPhone.setActivityNumber(activityNum);
        voucherPhoneService.addVoucherPhone(voucherPhone);
        return "/games/franchiseeConference/success";
    }

    /**
     * 财富卷大转盘活动
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/toRoulette")
    public String toRoulette(HttpServletRequest request) {
        String token = UUID.randomUUID().toString().replace("-", "");
        request.getSession().setAttribute("token", token);
        //获取微信绑定的当前用户登录
        logger.info("微信请求方式=" + request.getMethod());
        String code = request.getParameter("code");
        logger.info("code=" + code);
        if (code != null && !"".equals(code)) {
            String openId = Sign.getOpenId(code);
            logger.info("opendId=" + openId);
            if (openId != null && !"".equals(openId)) {
                logger.info("微信state=" + request.getParameter("state"));
                List<ConstantDefine> wechatVoucherTotalList = constantDefineService.getConstantDefinesByType("wechatVoucherTotal");
                BigDecimal wechatVoucherTotal = new BigDecimal(wechatVoucherTotalList.get(0).getConstantValue());
                if (wechatVoucherTotal.compareTo(new BigDecimal("75500")) >= 0) {
                    request.setAttribute("meg", "活动已结束");
                    return "/games/turntableDraw/activeOver";
                }
                ActivityBinding ab = voucherPhoneService.getActivityBindingByOpenId(openId);
                if (ab != null) {
                    //修改绑定表是否已分享
                    logger.info("微信state=" + request.getParameter("state"));
                    if ("0".equals(request.getParameter("state"))) {
                        ab.setUsageFrequency(Long.valueOf(1));
                        voucherPhoneService.updateActivityBinding(ab);
                    }
                    request.setAttribute("phone", ab.getMobileNo());
                    boolean mobileExist = userInfoService.isMobileExist(ab.getMobileNo());

                    request.setAttribute("mobileExist", mobileExist);

                    List<VoucherPhone> voucherPhoneList = voucherPhoneService.getVoucherPhoneBindingPhone(ab.getMobileNo(), "franchiseeV02");
                    if (ab.getUsageFrequency() == 0) {
                        if (voucherPhoneList.size() > 0) {
                            request.setAttribute("useNum", 0);
                            request.setAttribute("totalNum", 1);
                        } else {
                            request.setAttribute("useNum", 1);
                            request.setAttribute("totalNum", 1);
                        }
                    } else {
                        if (voucherPhoneList.size() > 0) {
                            request.setAttribute("useNum", voucherPhoneList.size() >= 2 ? 0 : 1);
                            request.setAttribute("totalNum", 0);
                        } else {
                            request.setAttribute("useNum", 2);
                            request.setAttribute("totalNum", 0);
                        }
                    }
                    return "/games/turntableDraw/luckDraw";
                } else {
                    request.getSession().setAttribute("openId", openId);
                    request.setAttribute("openId", openId);
                }
            }
        }
        return "/games/turntableDraw/form";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/roulette")
    public ModelAndView roulette(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/games/turntableDraw/form");
        logger.info("微信请求方式=" + request.getMethod());
        //获取微信绑定的当前用户登录
        String code = request.getParameter("code");
        logger.info("code=" + code);
        if (code != null && !"".equals(code)) {
            String phone = request.getParameter("state");
            String openId = Sign.getOpenId(code);
            logger.info("opendId=" + openId);
            if (openId != null && !"".equals(openId)) {
                if (!mobileNoValidate(phone)) {
                    mv.addObject("meg", "手机号为空或格式错误");
                    mv.setViewName("/games/turntableDraw/form");
                    return mv;
                }
                ActivityBinding ab = voucherPhoneService.getActivityBindingByOpenId(openId);
                ActivityBinding ab1 = voucherPhoneService.getActivityBindingByPhone(phone);
                if (ab == null && ab1 == null) {
                    ab = voucherPhoneService.addActivityBindingAndVoucherPhone(openId, phone, "franchiseeV02");
                } else {
                    mv.addObject("meg", "你已参与过!");
                    mv.setViewName("/games/turntableDraw/form");
                    return mv;
                }
                request.setAttribute("phone", phone);
                boolean mobileExist = userInfoService.isMobileExist(phone);
                request.setAttribute("mobileExist", mobileExist);
                List<VoucherPhone> voucherPhoneList = voucherPhoneService.getVoucherPhoneBindingPhone(phone, "franchiseeV02");
                if (ab.getUsageFrequency() == 0) {
                    if (voucherPhoneList.size() > 0) {
                        request.setAttribute("useNum", 0);
                        request.setAttribute("totalNum", 1);
                    } else {
                        request.setAttribute("useNum", 1);
                        request.setAttribute("totalNum", 1);
                    }
                } else {
                    if (voucherPhoneList.size() > 0) {
                        request.setAttribute("useNum", voucherPhoneList.size() >= 2 ? 0 : 1);
                        request.setAttribute("totalNum", 0);
                    } else {
                        request.setAttribute("useNum", 2);
                        request.setAttribute("totalNum", 0);
                    }
                }
                mv.setViewName("/games/turntableDraw/luckDraw");
            }
        }
        return mv;
    }


    @DoNotNeedLogin
    @RequestMapping(value = "/rouletteAlgorit")
    @ResponseBody
    public Object rouletteAlgorit(HttpServletRequest request, @RequestParam(value = "phone") String phone, @RequestParam(value = "prizeNum") int prizeNum) {

        List<ConstantDefine> wechatVoucherTotalList = constantDefineService.getConstantDefinesByType("wechatVoucherTotal");
        BigDecimal wechatVoucherTotal = new BigDecimal(wechatVoucherTotalList.get(0).getConstantValue());
        if (wechatVoucherTotal.compareTo(new BigDecimal("75500")) >= 0) {
            return JsonView.JsonViewFactory.create().success(false).info("抽奖结束").put("id", "")
                    .toJson();
        }

        ActivityBinding ab = voucherPhoneService.getActivityBindingByPhone(phone);
        if (ab == null)
            return JsonView.JsonViewFactory.create().success(false).info("手机号不存在").put("id", "noPhone")
                    .toJson();
        List<VoucherPhone> voucherPhoneList = voucherPhoneService.getVoucherPhoneBindingPhone(phone, "franchiseeV02");
        if (voucherPhoneList.size() >= 2)
            return JsonView.JsonViewFactory.create().success(false).info("已没有抽奖次数").put("id", "noSize")
                    .toJson();
        UserInfo userInfo = userInfoService.getUserByMobileNo(ab.getMobileNo());

        if (userInfo != null) {
            voucherPhoneService.updateVoucherPhoneAndPrize(prizeNum, userInfo.getUserId(), "franchiseeV02", ab.getBindingId());
        } else {
            request.getSession().setAttribute("prizeNum", prizeNum);
            request.getSession().setAttribute("bindingId", ab.getBindingId());
        }
        return JsonView.JsonViewFactory.create().success(true).info("")
                .toJson();
    }

    /**
     * 跳转至分享注册页面
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/activeOver")
    public String activeOver(HttpServletRequest request) {
        return "/games/turntableDraw/activeOver";
    }

    /**
     *  1、5元财富券 15%
     2、10元财富券 25%
     3、20元财富券 10%
     4、50元财富券 15%
     5、100元财富券 35%
     */
    /*@DoNotNeedLogin
	@RequestMapping(value = "rouletteAlgorithm")
	@ResponseBody
	public Object getRouletteAlgorithm(HttpServletRequest request, @RequestParam(value = "phone") String phone){
		if(phone == null){
			return JsonView.JsonViewFactory.create().success(false).info("手机号异常").put("phone", null)
					.toJson();
		}
		VoucherPhone voucherPhone = voucherPhoneService.getVoucherPhoneBindingPhone(phone, "franchiseeV02");
		String vouStr = rouletteAlgorithm();
		if(!"".equals(vouStr)){
			if(voucherPhone.getTotalNum()-voucherPhone.getUsageFrequency()==0){
				return JsonView.JsonViewFactory.create().success(false).info("你已经都抽过了").put("useCheck", false)
						.toJson();
			}else{
				voucherPhone.setUsageFrequency(voucherPhone.getUsageFrequency()+1);
				voucherPhoneService.updateVoucherPhone(voucherPhone);
			}
			return JsonView.JsonViewFactory.create().success(true).info(vouStr)
					.toJson();
		}
		return JsonView.JsonViewFactory.create().success(false).info("抽奖结束")
						.toJson();
	}
	
	public String rouletteAlgorithm(){
		int a = 15;
		int b = 40;
		int c = 50;
		int d = 65;
		int e = 100;
		int f = 10;
		Random random = new Random();
		int ranInt = random.nextInt(e);
		if(ranInt<a && ranInt>=0){
			System.out.println("5元" + ranInt);
				return "5";
		}else if(ranInt<b && ranInt>=a){
			System.out.println("10元" + ranInt);
				return "10";
		}else if(ranInt<c && ranInt>=b){
			System.out.println("20元" + ranInt);
				return "20";
		}else if(ranInt<d && ranInt>=c){
			System.out.println("50元" + ranInt);
				return "50";
		}else if(ranInt<e && ranInt>=d){
			System.out.println("100元" + ranInt);
				return "100";
		}else{
			System.out.println(ranInt);
			return "";
		}
	}*/

    /**
     * 跳转至分享注册页面
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/share")
    public String toShareRegister(HttpServletRequest request) {
        System.out.println(request.getParameter("invite_code"));
        request.setAttribute("invite_code", request.getParameter("invite_code"));
        //用于记录页面登陆成功后的跳转页面
        request.setAttribute("pastUrl", "/");
        return "/games/turntableDraw/shareRegister";
    }

    /**
     * 分享注册
     *
     * @param request
     * @param userInfo
     * @param validCode
     * @param inviteUserId 邀请好友id
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/shareRegister")
    public String shareRegister(HttpServletRequest request, UserInfo userInfo, String validCode, String inviteCode, String inviteUserId) {
        System.out.println(userInfo.getMobileNo() + " " + validCode + " " + userInfo.getLoginName() + " " + userInfo.getLoginPass() + " " + inviteCode);
        //后端验证注册信息
        Map<String, String> validate = (Map<String, String>) validate(userInfo.getMobileNo(), validCode, userInfo.getLoginName(), userInfo.getLoginPass(), inviteCode, true);
        for (String key : validate.keySet()) {
            String result = validate.get(key);
            Map<String, Object> jsonMap = JsonUtil.fromJson(result);
            boolean isSuccess = (Boolean) jsonMap.get("isSuccess");
            if (!isSuccess) {
                request.setAttribute("meg", "信息输入错误!");
                return "/games/turntableDraw/shareRegister";
            }
        }
        //注册
        UserInfo user = null;
        InvitationCode invitationCode = userInfoService.getInvitationCodeByCode(inviteCode);
        if (invitationCode == null) {
            user = userInfoService.regist(userInfo, UserSource.WECHAT);
        } else {
            user = userInfoService.regist(userInfo, UserSource.WECHAT, invitationCode.getUserId() + "");
        }
        Integer prizeNum = (Integer) request.getSession().getAttribute("prizeNum");
        Long bindingId = (Long) request.getSession().getAttribute("bindingId");
        System.out.println(prizeNum + " " + bindingId);
        if (prizeNum != null && bindingId != null)
            voucherPhoneService.updateVoucherPhoneAndPrize(prizeNum, userInfo.getUserId(), "franchiseeV02", bindingId);
        return "/games/turntableDraw/registerSuccess";
    }

    /**
     * 校验验证码
     *
     * @param validCode
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/validate")
    @ResponseBody
    public Object validate(String mobile, String validCode, String loginName, String password, String inviteCode, boolean flag) {

        HashMap<String, String> map = new HashMap<String, String>();
        //验证登陆名
        map.put("username", isLoginNameExist(loginName));
        //验证手机号
        map.put("phone", isMobileNoExist(mobile));
        //验证密码
        map.put("password", JsonView.JsonViewFactory.create().success(passwordValidate(password)).info("请输入6~16位字符，字母加数字组合，字母区分大小写").put("id", "password")
                .toJson());
        //验证短信码,并返回
        map.put("valid", validate(mobile, validCode, flag));
        //验证邀请码

        if (org.apache.commons.lang.StringUtils.isNotEmpty(inviteCode) && !"邀请码 (非必填)".equals(inviteCode)) {
            map.put("visate", validateInviteCode(inviteCode.trim()));
        }

        return map;
    }

    /**
     * 判断用户名是否存在
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/isLoginNameExist")
    @ResponseBody
    public String isLoginNameExist(String loginName) {
        if (!loginNameValidate(loginName)) {
            return JsonView.JsonViewFactory.create().success(false).info("用户名格式错误").put("id", "username")
                    .toJson();
        }
        //手机号验证
        if (mobileNoValidate(loginName)) {
            return JsonView.JsonViewFactory.create().success(false).info("手机号不可作为用户名使用'").put("id", "username")
                    .toJson();
        }
        boolean loginNameExist = userInfoService.isLoginNameExist(loginName);
        if (loginNameExist) {
            return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.LOGINNAME_EXIST.getDesc()).put("id", "username")
                    .toJson();
        }
        return JsonView.JsonViewFactory.create().success(true).info("").put("id", "username")
                .toJson();
    }

    /**
     * 判断手机号是否存在
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/regist/isMobileNoExist")
    @ResponseBody
    public String isMobileNoExist(String mobileNo) {
        if (!mobileNoValidate(mobileNo)) {
            return JsonView.JsonViewFactory.create().success(false).info("手机号码格式错误").put("id", "phone")
                    .toJson();
        }
        boolean mobileExist = userInfoService.isMobileExist(mobileNo);
        if (mobileExist) {
            return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.MOBILENO_EXIST.getDesc()).put("id", "phone")
                    .toJson();
        }
        return JsonView.JsonViewFactory.create().success(true).info("").put("id", "phone")
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
        //post请求验证
        RequestUtil.validateRequestMethod(request, RequestMethod.POST);

        try {
            //验证手机号是否存在
            if (!mobileNoValidate(mobileNo)) {
                return JsonView.JsonViewFactory.create().success(false).info("手机号码格式错误").put("id", "phone")
                        .toJson();
            }
            boolean mobileExist = userInfoService.isMobileExist(mobileNo);
            if (mobileExist) {
                return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.MOBILENO_EXIST.getDesc()).put("id", "phone")
                        .toJson();
            }
            userInfoService.sendRegisterMsg(mobileNo);
            return JsonView.JsonViewFactory.create().success(true).info("")
                    .toJson();
        } catch (SystemException se) {
            logger.error(se.getMessage(), se);
            return JsonView.JsonViewFactory.create().success(false).info("")
                    .toJson();
        }
    }

    /**
     * 校验邀请码
     *
     * @param inviteCode
     * @return
     */
    private String validateInviteCode(String inviteCode) {
        InvitationCode invitationCode = userInfoService.getInvitationCodeByCode(inviteCode);
        if (invitationCode == null) {
            return JsonView.JsonViewFactory.create().success(false).info("你输入的邀请码不存在").put("id", "visate")
                    .toJson();
        } else {
            return JsonView.JsonViewFactory.create().success(true).info("").put("id", "visate")
                    .toJson();
        }
    }

    /**
     * 校验短信验证码
     *
     * @param validCode
     * @return
     */
    public String validate(String mobile, String validCode, boolean flag) {
        try {
            boolean bool = smsService.validateMsg(mobile, validCode, TemplateType.SMS_REGISTER_VM, flag);
            if (bool)
                return JsonView.JsonViewFactory.create().success(true).info("").put("id", "valid")
                        .toJson();
            else
                return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.VALIDATE_CODE_ERROR.getDesc()).put("id", "valid")
                        .toJson();
        } catch (SystemException se) {
            logger.error(se.getMessage(), se);
            return JsonView.JsonViewFactory.create().success(false).info(se.getMessage()).put("id", "valid")
                    .toJson();
        }
    }

    /**
     * 领取提现劵活动
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/toEntryCellPhoneNumber")
    public ModelAndView toEntryCellPhoneNumber(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/games/receiveCashCoupon/receiveRepeat");
        logger.info("微信请求方式=" + request.getMethod());
        //获取微信绑定的当前用户登录
        String code = request.getParameter("code");
        logger.info("code=" + code);
        if (code != null && !"".equals(code)) {
            String openId = Sign.getOpenId(code);
            logger.info("opendId=" + openId);
            if (openId != null && !"".equals(openId)) {
                List<ConstantDefine> wechatVoucherTotalList = constantDefineService.getConstantDefinesByType("withdrawVoucherSize");
                BigDecimal wechatVoucherTotal = new BigDecimal(wechatVoucherTotalList.get(0).getConstantValue());
                if (wechatVoucherTotal.compareTo(new BigDecimal("200")) >= 0) {
                    mv.setViewName("/games/turntableDraw/activeOver");
                }
                ActivityBinding ab = voucherPhoneService.getActivityBindingByOpenId(openId);
                request.getSession().setAttribute("openId", openId);
                if (ab == null) {
                    mv.setViewName("/games/receiveCashCoupon/entryCellPhoneNumber");
                } else {
                    UserInfo userInfo = userInfoService.getUserByMobileNo(ab.getMobileNo());
                    if (userInfo == null) {
                        mv.setViewName("/games/receiveCashCoupon/notRegistered");
                    } else {
                        List<VoucherPhone> voucherPhoneList = voucherPhoneService.getVoucherPhoneBindingPhone(ab.getMobileNo(), "franchiseeV03");
                        if (voucherPhoneList.size() == 0) {
                            voucherPhoneService.sendVoucherWithDraw(userInfo.getUserId(), "franchiseeV03", null, ab.getBindingId());
                            mv.setViewName("/games/receiveCashCoupon/receiveSuccess");
                        } else {
                            mv.setViewName("/games/receiveCashCoupon/receiveRepeat");
                        }
                    }
                }
            }
        }
        return mv;
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/entryCellPhoneNumber", method = RequestMethod.POST)
    @ResponseBody
    public String entryCellPhoneNumber(HttpServletRequest request, @RequestParam(value = "phone") String phone) {
        if (phone != null && "".equals(phone)) {
            //手机好空
            return JsonView.JsonViewFactory.create().success(false).info("手机好空")
                    .toJson();
        }
        String openId = (String) request.getSession().getAttribute("openId");
        if (openId != null && "".equals(openId)) {
            //非法请求
            return JsonView.JsonViewFactory.create().success(false).info("非法请求")
                    .toJson();
        }
        ActivityBinding ab = voucherPhoneService.getActivityBindingByPhone(phone);
        ActivityBinding ab1 = voucherPhoneService.getActivityBindingByOpenId(openId);
        if (ab == null && ab1 == null) {
            ActivityBinding activityBinding = new ActivityBinding();
            activityBinding.setCreateTime(new Date());
            activityBinding.setMobileNo(phone);
            activityBinding.setOpenId(openId);
            UserInfo userInfo = userInfoService.getUserByMobileNo(phone);
            if (userInfo == null) {
                return JsonView.JsonViewFactory.create().success(false).info("").put("id", "noRegister")
                        .toJson();
            } else {
                voucherPhoneService.sendVoucherWithDraw(userInfo.getUserId(), "franchiseeV03", activityBinding, null);
                return JsonView.JsonViewFactory.create().success(true).info("")
                        .toJson();
            }
        } else {
            List<VoucherPhone> voucherPhoneList = voucherPhoneService.getVoucherPhoneBindingPhone(phone, "franchiseeV03");
            if (ab1.getOpenId().equals(ab.getOpenId()) && ab1.getMobileNo().equals(ab.getMobileNo())) {
                if (voucherPhoneList.size() == 0) {
                    UserInfo userInfo = userInfoService.getUserByMobileNo(phone);
                    if (userInfo == null) {
                        return JsonView.JsonViewFactory.create().success(false).info("").put("id", "noRegister")
                                .toJson();
                    } else {
                        voucherPhoneService.sendVoucherWithDraw(userInfo.getUserId(), "franchiseeV03", null, ab.getBindingId());
                        return JsonView.JsonViewFactory.create().success(true).info("")
                                .toJson();
                    }
                } else {
                    return JsonView.JsonViewFactory.create().success(false).info("").put("id", "repeat")
                            .toJson();
                }
            } else {
                return JsonView.JsonViewFactory.create().success(false).info("").put("id", "repeat")
                        .toJson();
            }
        }
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/toReceiveRepeat")
    public ModelAndView toReceiveRepeat(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/games/receiveCashCoupon/receiveRepeat");
        return mv;
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/toReceiveSuccess")
    public ModelAndView toReceiveSuccess(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/games/receiveCashCoupon/receiveSuccess");
        return mv;
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/toNotRegistered")
    public ModelAndView toNotRegistered(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/games/receiveCashCoupon/notRegistered");
        return mv;
    }

    /**
     * 企业文化书
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/corporateCulture")
    public ModelAndView corporateCulture(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/games/corporateCulture");
        return mv;
    }

    /**
     * 微信里程碑
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/toWechatMilepost")
    public ModelAndView wechatMilepost(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/games/anniversary/AlreadyReceive");
        logger.info("微信请求方式=" + request.getMethod());
        //获取微信绑定的当前用户登录
        String code = request.getParameter("code");
        logger.info("code=" + code);
        if (code != null && !"".equals(code)) {
            String openId = Sign.getOpenId(code);
            logger.info("opendId=" + openId);
            if (openId != null && !"".equals(openId)) {
                request.getSession().setAttribute("openId", openId);
                ActivityBinding ab = voucherPhoneService.getActivityBindingByOpenId(openId);
                if (ab == null) {
                    mv.addObject("appFlag", "0");
                } else {
                    mv.addObject("appFlag", "1");
                    mv.addObject("phone", ab.getMobileNo());
                    List<VoucherPhone> voucherPhoneList = voucherPhoneService.getVoucherPhoneBindingPhone(ab.getMobileNo(), "franchiseeV05");
                    if (voucherPhoneList.size() > 0) {
                        mv.setViewName("/games/anniversary/AlreadyReceive");
                        return mv;
                    }
                }
            }
            mv.setViewName("/games/anniversary/Anniversary");
        }
        return mv;
    }

    /**
     * app里程碑
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/toAppMilepost")
    public ModelAndView appMilepost(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/games/turntableDraw/activeOver");
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return mv;
        }
        mv.setViewName("/games/anniversary/Anniversary");
        mv.addObject("appFlag", "1");
        mv.addObject("phone", currentUser.getMobileNo());
        mv.addObject("userToken", request.getParameter("userToken"));
        List<VoucherPhone> voucherPhoneList = voucherPhoneService.getVoucherPhoneBindingPhone(currentUser.getMobileNo(), "franchiseeV05");
        if (voucherPhoneList.size() > 0) {
            mv.setViewName("/games/anniversary/APPAlreadyReceive");
        }
        return mv;
    }

    /**
     * 验证
     *
     * @param request
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/milepostValidate")
    public ModelAndView milepostValidate(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/games/turntableDraw/activeOver");
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser != null) {
            mv.addObject("phone", currentUser.getMobileNo());
            ActivityBinding ab = voucherPhoneService.getActivityBindingByPhone(currentUser.getMobileNo());
            if (ab == null) {
                ab = voucherPhoneService.addActivityBindingAndVoucherPhone(UUID.randomUUID().toString().trim().replaceAll("-", ""), currentUser.getMobileNo(), "franchiseeV05");
            }
            List<VoucherPhone> voucherPhoneList = voucherPhoneService.getVoucherPhoneBindingPhone(currentUser.getMobileNo(), "franchiseeV05");
            if (voucherPhoneList.size() > 0) {
                mv.setViewName("/games/anniversary/APPAlreadyReceive");//app已领取
            } else {
                //todo  领取
                BigDecimal countAount = lendOrderService.getAllBuyBalance(currentUser.getUserId());
                if (countAount.compareTo(new BigDecimal("0")) == 0) {
                    voucherPhoneService.AddVoucherPhoneAndPrize(100, currentUser.getUserId(), "franchiseeV05", ab.getBindingId());
                    mv.addObject("voucher", "100");
                } else {
                    voucherPhoneService.AddVoucherPhoneAndPrize(5, currentUser.getUserId(), "franchiseeV05", ab.getBindingId());
                    mv.addObject("voucher", "5");
                }
                mv.setViewName("/games/anniversary/APPReceiveSuccess");//领取成功
            }
        } else {
            String openId = (String) request.getSession().getAttribute("openId");
            if (openId != null && !"".equals(openId)) {
                mv.addObject("phone", request.getParameter("phone"));
                String phone = request.getParameter("phone");
                boolean mobileExist = userInfoService.isMobileExist(phone);
                if (!mobileExist) {
                    mv.setViewName("/games/anniversary/NoRegistered");//去注册
                    return mv;
                }
                ActivityBinding ab = voucherPhoneService.getActivityBindingByOpenId(openId);
                ActivityBinding ab1 = voucherPhoneService.getActivityBindingByPhone(phone);
                if (ab == null && ab1 == null) {
                    ab = voucherPhoneService.addActivityBindingAndVoucherPhone(openId, phone, "franchiseeV05");
                }
                List<VoucherPhone> voucherPhoneList = voucherPhoneService.getVoucherPhoneBindingPhone(ab != null ? ab.getMobileNo() : ab1.getMobileNo(), "franchiseeV05");
                if (voucherPhoneList.size() > 0) {
                    mv.setViewName("/games/anniversary/AlreadyReceive");//微信已领取
                } else {
                    //todo  领取
                    UserInfo user = userInfoService.getUserByMobileNo(phone);
                    BigDecimal countAount = lendOrderService.getAllBuyBalance(user.getUserId());
                    if (countAount.compareTo(new BigDecimal("0")) == 0) {
                        voucherPhoneService.AddVoucherPhoneAndPrize(100, user.getUserId(), "franchiseeV05", ab.getBindingId());
                        mv.addObject("voucher", "100");
                    } else {
                        voucherPhoneService.AddVoucherPhoneAndPrize(5, user.getUserId(), "franchiseeV05", ab.getBindingId());
                        mv.addObject("voucher", "5");
                    }
                    mv.setViewName("/games/anniversary/ReceiveSuccess");//领取成功
                }
            }
        }
        return mv;
    }

    /**
     * 微信大转盘
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/toWechatTurntable")
    public ModelAndView wechatTurntable(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/games/sudokuSweepstake/carnival");
        String openId = openIdValite(request);
        if (!StringUtils.isNull(openId)) {
            //String openId = (String) request.getSession().getAttribute("openId");
            ActivityBinding ab = voucherPhoneService.getActivityBindingByOpenId(openId);
            if (ab == null) {
                return mv;
            } else {
                UserInfo currentUser = userInfoService.getUserByMobileNo(ab.getMobileNo());
                BigDecimal allBuyBalance = BigDecimal.ZERO;// 累计出借金额
                if (currentUser != null) {
                    allBuyBalance = lendOrderService.getTotalLendAmount(currentUser.getUserId());
                    //获取抽奖次数
                    UserShare userShare = voucherPhoneService.getUserSHareByUserId(currentUser.getUserId(), "franchiseeV07");
                    //分享增加抽奖次数
                    int shareState = 0;
                    if ("0".equals(request.getParameter("state")) && allBuyBalance.compareTo(BigDecimal.ZERO) > 0) {
                        if (WechatActConstants.IsShareEnum.NOT_SHARE.getValue().equals(userShare.getIsShare()))
                            //userShare = voucherPhoneService.shareAddNum(currentUser.getUserId(),  "franchiseeV07");
                            shareState = 1;
                    }
                    mv.addObject("shareNum", calculatePrizeNum(userShare, allBuyBalance, currentUser.getUserId(), shareState));
                    //是否注册
                    if (allBuyBalance.compareTo(BigDecimal.ZERO) == 0) {
                        mv.addObject("registerFlag", "2");
                    } else {
                        mv.addObject("registerFlag", "1");
                    }
                    mv.addObject("phone", currentUser.getMobileNo());
                } else {
                    //获取抽奖次数
                    mv.addObject("shareNum", 0);
                    //是否注册
                    mv.addObject("registerFlag", "0");
                    mv.addObject("phone", ab.getMobileNo());
                }
                mv.addObject("allBuyBalance", allBuyBalance);
//				List<VoucherPhone> voucherPhoneList = voucherPhoneService.getVoucherPhoneBindingPhone(ab.getMobileNo(), "franchiseeV07");
//		        if(voucherPhoneList.size() > 0){
//		        	mv.setViewName("/games/anniversary/AlreadyReceive");
//		        	return mv;
//		        }
                mv.addObject("allBuyBalance", allBuyBalance);
                Date date = new Date();
                if (date.getTime() - DateUtil.parseStrToDate("2016-01-31 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime() <= 0) {
                    mv.setViewName("/games/sudokuSweepstake/sweepstakeNew");
                } else {
                    mv.setViewName("/games/sudokuSweepstake/sweepstake");
                }
            }
        }
        return mv;
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/carnival")
    public ModelAndView carnival(HttpServletRequest request, @RequestParam(value = "phone") String phone) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/games/sudokuSweepstake/carnival");
        if (phone != null && "".equals(phone)) {
            //手机好空
            return mv;
        }
        String openId = (String) request.getSession().getAttribute("openId");
        if (openId == null || "".equals(openId)) {
            //非法请求
            return mv;
        }
        ActivityBinding ab = voucherPhoneService.getActivityBindingByPhone(phone);
        ActivityBinding ab1 = voucherPhoneService.getActivityBindingByOpenId(openId);
        if (ab == null && ab1 == null) {
            ActivityBinding activityBinding = new ActivityBinding();
            activityBinding.setCreateTime(new Date());
            activityBinding.setMobileNo(phone);
            activityBinding.setOpenId(openId);
            ab1 = voucherPhoneService.addActivityBindingAndVoucherPhone(openId, phone, "franchiseeV07");
        }
        UserInfo currentUser = userInfoService.getUserByMobileNo(ab1 != null ? ab1.getMobileNo() : ab.getMobileNo());
        BigDecimal allBuyBalance = BigDecimal.ZERO;// 累计出借金额
        if (currentUser != null) {
            allBuyBalance = lendOrderService.getTotalLendAmount(currentUser.getUserId());
            //获取抽奖次数
            UserShare userShare = voucherPhoneService.getUserSHareByUserId(currentUser.getUserId(), "franchiseeV07");
            mv.addObject("shareNum", calculatePrizeNum(userShare, allBuyBalance, currentUser.getUserId(), 0));
            //是否注册
            if (allBuyBalance.compareTo(BigDecimal.ZERO) == 0) {
                mv.addObject("registerFlag", "2");
            } else {
                mv.addObject("registerFlag", "1");
            }
            mv.addObject("phone", currentUser.getMobileNo());
        } else {
            //获取抽奖次数
            mv.addObject("shareNum", 0);
            //是否注册
            mv.addObject("registerFlag", "0");
            mv.addObject("phone", phone);
        }
        mv.addObject("allBuyBalance", allBuyBalance);
        Date date = new Date();
        if (date.getTime() - DateUtil.parseStrToDate("2016-01-31 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime() <= 0) {
            mv.setViewName("/games/sudokuSweepstake/sweepstakeNew");
        } else {
            mv.setViewName("/games/sudokuSweepstake/sweepstake");
        }
        return mv;
    }

    /**
     * app大转盘
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/toAppTurntable")
    public ModelAndView appTurntable(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        UserInfo currentUser = getCurrentUser(request);
        BigDecimal allBuyBalance = BigDecimal.ZERO;// 累计出借金额
        if (currentUser != null) {
            allBuyBalance = lendOrderService.getTotalLendAmount(currentUser.getUserId());
            //获取抽奖次数
            UserShare userShare = voucherPhoneService.getUserSHareByUserId(currentUser.getUserId(), "franchiseeV07");
            //分享增加抽奖次数
            int shareState = 0;
            if ("0".equals(request.getParameter("state")) && allBuyBalance.compareTo(BigDecimal.ZERO) > 0) {
                if (WechatActConstants.IsShareEnum.NOT_SHARE.getValue().equals(userShare.getIsShare()))
                    //userShare = voucherPhoneService.shareAddNum(currentUser.getUserId(),  "franchiseeV07");
                    shareState = 1;
            }
            mv.addObject("shareNum", calculatePrizeNum(userShare, allBuyBalance, currentUser.getUserId(), shareState));
            //是否注册
            if (allBuyBalance.compareTo(BigDecimal.ZERO) == 0) {
                mv.addObject("registerFlag", "2");
            } else {
                mv.addObject("registerFlag", "1");
            }
            mv.addObject("phone", currentUser.getMobileNo());
            mv.addObject("shareFlag", "1");
        } else {
            //获取抽奖次数
            mv.addObject("shareNum", 0);
            throw new SystemException(UserErrorCode.LONGIN_EXIST);
        }
        mv.addObject("allBuyBalance", allBuyBalance);
        mv.addObject("userToken", request.getParameter("userToken"));
        mv.addObject("allBuyBalance", allBuyBalance);
        Date date = new Date();
        if (date.getTime() - DateUtil.parseStrToDate("2016-01-31 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime() <= 0) {
            mv.setViewName("/games/sudokuSweepstake/sweepstakeNew");
        } else {
            mv.setViewName("/games/sudokuSweepstake/sweepstake");
        }
        return mv;
    }


    /**
     * 分享回调
     * @param request
     * @return
     */
//	@DoNotNeedLogin
//	@RequestMapping(value="/shareBack")
//	@ResponseBody
//    public Object shareBack(HttpServletRequest request){
//		ModelAndView mv = new ModelAndView();
//		UserInfo currentUser = getCurrentUser(request);
//		BigDecimal allBuyBalance = BigDecimal.ZERO;// 累计出借金额
//		//app访问
//		if(currentUser != null){
//			allBuyBalance = lendOrderService.getTotalLendAmount(currentUser.getUserId());
//			if(allBuyBalance.compareTo(BigDecimal.ZERO)>0){
//				//分享计数
//				voucherPhoneService.shareAddNum(currentUser.getUserId(), "franchiseeV07");
//			}
//		}
//		else{//微信访问
//			String openId = (String) request.getSession().getAttribute("openId");
//			if(!StringUtils.isNull(openId)){
//				ActivityBinding ab = voucherPhoneService.getActivityBindingByOpenId(openId);
//				if(ab != null){
//					UserInfo user = userInfoService.getUserByMobileNo(ab.getMobileNo());
//					if(user != null){
//						allBuyBalance = lendOrderService.getTotalLendAmount(user.getUserId());
//						if(allBuyBalance.compareTo(BigDecimal.ZERO)>0){
//							//分享计数
//							voucherPhoneService.shareAddNum(user.getUserId(), "franchiseeV07");
//						}
//					}
//				}
//			}
//		}
//		mv.setViewName("/games/sudokuSweepstake/sweepstake");
//		return "";
//    }

    /**
     * 计算抽奖次数
     *
     * @param list
     * @return
     */
    public int calculatePrizeNum(UserShare userShare, BigDecimal allBuyBalance, Long userId, int shareState) {
        int prizeNum = 0;
        if (allBuyBalance.compareTo(new BigDecimal(100)) >= 0 && allBuyBalance.compareTo(new BigDecimal(10000)) < 0) {
            prizeNum = 1;
        } else if (allBuyBalance.compareTo(new BigDecimal(10000)) >= 0 && allBuyBalance.compareTo(new BigDecimal(100000)) < 0) {
            prizeNum = 2;
        } else if (allBuyBalance.compareTo(BigDecimal.ZERO) >= 0 && allBuyBalance.compareTo(new BigDecimal(100)) < 0) {
            prizeNum = 0;
        } else {
            prizeNum = 3;
        }
        if (userShare != null) {
            if (prizeNum >= userShare.getLuckDrawNum())
                userShare.setLuckDrawNum(prizeNum);
            if (shareState == 1) {
                userShare.setIsShare(WechatActConstants.IsShareEnum.IS_SHARE.getValue());
                userShare.setLuckDrawNum(userShare.getLuckDrawNum() + 1);
            }
            voucherPhoneService.updateUserShare(userShare);
        } else {
            UserShare userShareNew = new UserShare();
            userShareNew.setUserId(userId);
            userShareNew.setActivityNumber("franchiseeV07");
            userShareNew.setCreateTime(new Date());
            if (shareState == 1) {
                userShareNew.setIsShare(WechatActConstants.IsShareEnum.IS_SHARE.getValue());
                userShareNew.setLuckDrawNum(prizeNum + 1);
            } else {
                userShareNew.setIsShare(WechatActConstants.IsShareEnum.NOT_SHARE.getValue());
                userShareNew.setLuckDrawNum(prizeNum);
            }
            userShareNew.setUsedLuckDrawNum(0);
            voucherPhoneService.addUserShare(userShareNew);
            userShare = userShareNew;
        }
        return userShare.getLuckDrawNum() - userShare.getUsedLuckDrawNum();
    }

    @DoNotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/toTurntableDraw")
    public String toTurntableDraw(HttpServletRequest request) {
        System.out.println(request.getParameter("userToken"));
        int gameWinningLevel = -1;
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser == null) {
            String openId = (String) request.getSession().getAttribute("openId");
            if (openId != null && !"".equals(openId)) {
                ActivityBinding ab = voucherPhoneService.getActivityBindingByOpenId(openId);
                if (ab == null) {
                    return JsonView.JsonViewFactory.create().success(false).info("您尚未注册").put("id", "noregist")
                            .toJson();
                } else {
                    currentUser = userInfoService.getUserByMobileNo(ab.getMobileNo());
                    if (currentUser == null)
                        return JsonView.JsonViewFactory.create().success(false).info("您尚未注册").put("id", "noregist")
                                .toJson();
                }
            }
        }
        List<PrizePool> prizePoolList = voucherPhoneService.getPrizePoolList(false);
        UserShare userShare = voucherPhoneService.getUserSHareByUserId(currentUser.getUserId(), "franchiseeV07");
        if (userShare != null && userShare.getLuckDrawNum() - userShare.getUsedLuckDrawNum() > 0) {
            BigDecimal allBuyBalance = lendOrderService.getTotalLendAmount(currentUser.getUserId());
            if (allBuyBalance.compareTo(BigDecimal.ZERO) <= 0)
                return JsonView.JsonViewFactory.create().success(false).info("您尚未投标").put("id", "nolend")
                        .toJson();
            gameWinningLevel = voucherPhoneService.turntableDraw(currentUser, userShare, allBuyBalance);
        } else {
            return JsonView.JsonViewFactory.create().success(false).info("您抽奖次数不足").put("id", "nonum")
                    .toJson();
        }
        return JsonView.JsonViewFactory.create().success(true).info("").put("id", gameWinningLevel == -1 ? prizePoolList.size() - 1 : gameWinningLevel - 1)
                .toJson();
    }

    @DoNotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/toIsTurntableDraw")
    public String toIsTurntableDraw(HttpServletRequest request) {
        System.out.println(request.getParameter("userToken"));
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser == null) {
            String openId = (String) request.getSession().getAttribute("openId");
            if (openId != null && !"".equals(openId)) {
                ActivityBinding ab = voucherPhoneService.getActivityBindingByOpenId(openId);
                if (ab == null) {
                    return JsonView.JsonViewFactory.create().success(false).info("您尚未注册").put("id", "noregist").put("num", 0)
                            .toJson();
                } else {
                    currentUser = userInfoService.getUserByMobileNo(ab.getMobileNo());
                    if (currentUser == null)
                        return JsonView.JsonViewFactory.create().success(false).info("您尚未注册").put("id", "noregist").put("num", 0)
                                .toJson();
                }
            }
        }
        List<PrizePool> prizePoolList = voucherPhoneService.getPrizePoolList(false);
        UserShare userShare = voucherPhoneService.getUserSHareByUserId(currentUser.getUserId(), "franchiseeV07");
        BigDecimal allBuyBalance = BigDecimal.ZERO;
        if (userShare != null && userShare.getLuckDrawNum() - userShare.getUsedLuckDrawNum() > 0) {
            allBuyBalance = lendOrderService.getTotalLendAmount(currentUser.getUserId());
            if (allBuyBalance.compareTo(BigDecimal.ZERO) <= 0)
                return JsonView.JsonViewFactory.create().success(false).info("您尚未投标").put("id", "nolend").put("num", calculatePrizeNum(userShare, allBuyBalance, currentUser.getUserId(), 0))
                        .toJson();
        } else {
            return JsonView.JsonViewFactory.create().success(false).info("您抽奖次数不足").put("id", "nonum").put("num", calculatePrizeNum(userShare, allBuyBalance, currentUser.getUserId(), 0))
                    .toJson();
        }
        return JsonView.JsonViewFactory.create().success(true).info("").put("id", 0).put("num", calculatePrizeNum(userShare, allBuyBalance, currentUser.getUserId(), 0))
                .toJson();
    }

    /**
     * 加息券抽奖
     *
     * @param request
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @DoNotNeedLogin
    @RequestMapping(value = "/increaseRate")
    public String increaseRate(HttpServletRequest request, ModelMap model) {
        String result = "ok";
        if (new Date().getTime() < DateUtil.parseStrToDate("2016-08-25", "yyyy-MM-dd").getTime()) {
            result = "活动还没开始";
        }
        if (new Date().getTime() >= DateUtil.parseStrToDate("2016-08-27", "yyyy-MM-dd").getTime()) {
            result = "活动已经结束";
        }
        model.put("result", result);
        UserInfo currentUser = null;
        try {
            currentUser = SecurityUtil.getCurrentUser(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int chance = 0;
        Map map = new HashMap();
        model.put("isLogin", "0");
        if (currentUser != null && result.equals("ok")) {
            map.put("userId", currentUser.getUserId());
            map.put("date", "2016-08-25");
            map.put("account", 10000);
            map.put("limitType", 2);
            map.put("limit", 3);
            chance = lendOrderService.getUserOrderNumByCondition(map);
            map.clear();
            map.put("mobile", currentUser.getMobileNo());
            map.put("name", "increaseRate");
            int num = voucherPhoneService.getUserDrawNum(map);
            chance = chance - num >= 0 ? chance - num : 0;
            map.clear();
            map.put("name", "increaseRate");
            map.put("mobile", currentUser.getMobileNo());
            map.put("pageNum", 2000);
            List<VoucherPhoneVO> users = voucherPhoneService.getHasLotteryDrawUsers(map);
            model.put("users", users);
            model.put("isLogin", "1");
        }
        model.put("chance", chance);
        if (result.equals("ok")) {
            map.clear();
            map.put("name", "increaseRate");
            map.put("number", 21);
            List<VoucherPhoneVO> list = voucherPhoneService.getHasLotteryDrawUsers(map);
            if (list != null && list.size() > 0) {
                for (VoucherPhoneVO vo : list) {
                    vo.setMobileNo(vo.getMobileNo().substring(0, 3) + "****" + vo.getMobileNo().substring(vo.getMobileNo().length() - 4));
                }
                model.put("list", list);
            }
        }
        return "/games/increaseRate/index";
    }

    /**
     * 加息券开始抽奖
     *
     * @param request
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = "/startIncreaseRateGame")
    public String startIncreaseRateGame(HttpServletRequest request) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        Map map = new HashMap();
        map.put("result", "1");
        map.put("msg", "ok");
        if (new Date().getTime() >= DateUtil.parseStrToDate("2016-08-27", "yyyy-MM-dd").getTime()) {
            map.put("result", "0");
            map.put("msg", "活动已经结束");
            return JSONObject.toJSONString(map);
        }
        if (new Date().getTime() <= DateUtil.parseStrToDate("2016-08-25", "yyyy-MM-dd").getTime()) {
            map.put("result", "0");
            map.put("msg", "活动还没开始");
            return JSONObject.toJSONString(map);
        }
        if (currentUser == null) {
            map.put("result", "0");
            map.put("msg", "用户尚未登录");
            return JSONObject.toJSONString(map);
        }
        map = voucherPhoneService.startLotteryDraw(currentUser);
        return JSONObject.toJSONString(map);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "/reflushIncrease")
    public Object reflushIncrease(HttpServletRequest request) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        if (currentUser != null) {
            IncreaseVO in = new IncreaseVO();
            Map map = new HashMap();
            map.put("name", "increaseRate");
            map.put("mobile", currentUser.getMobileNo());
            map.put("pageNum", 2000);
            List<VoucherPhoneVO> users = voucherPhoneService.getHasLotteryDrawUsers(map);
            in.setUsers(users);
            map.clear();
            map.put("name", "increaseRate");
            map.put("number", 21);
            List<VoucherPhoneVO> list = voucherPhoneService.getHasLotteryDrawUsers(map);
            if (list != null && list.size() > 0) {
                for (VoucherPhoneVO vo : list) {
                    vo.setMobileNo(vo.getMobileNo().substring(0, 3) + "****" + vo.getMobileNo().substring(vo.getMobileNo().length() - 4));
                }
            }
            in.setList(list);
            map.clear();
            map.put("userId", currentUser.getUserId());
            map.put("date", "2016-08-25");
            map.put("account", 10000);
            map.put("limitType", 2);
            map.put("limit", 3);
            int chance = lendOrderService.getUserOrderNumByCondition(map);
            map.clear();
            map.put("mobile", currentUser.getMobileNo());
            map.put("name", "increaseRate");
            int num = voucherPhoneService.getUserDrawNum(map);
            chance = chance - num >= 0 ? chance - num : 0;
            in.setChance(chance);
            return JSONObject.toJSONString(in);
        }
        return "";
    }
}
