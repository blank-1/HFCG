package com.xt.cfp.wechat.controller;

import com.external.llpay.LLPayRequest;
import com.external.llpay.LLPayUtil;
import com.external.yeepay.TZTService;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.PayConstants.PayChannel;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.CustomerCard;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/bankcard")
public class BankCardController extends BaseController {
    private static Logger logger = Logger.getLogger(BankCardController.class);

    @Autowired
    private CustomerCardService customerCardService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ConstantDefineService constantDefineService;

    @Autowired
    private WithDrawService withDrawService;

    @Autowired
    private UserInfoExtService userInfoExtService;

    @Autowired
    private RechargeOrderService rechargeOrderService;

    /**
     * 绑定成功
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/bidding_success")
    public ModelAndView bidding_success(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("bankcard/bidding_success");
        return mv;
    }

    /**
     * 绑卡页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/to_bidding")
    public ModelAndView to_bidding(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        TokenUtils.setNewToken(request);
        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        //历史卡信息
        CustomerCard hisCustomerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.YB);
        if (hisCustomerCard != null) {
            boolean isSupport = true;
            ConstantDefine bankInfo = constantDefineService.findById(hisCustomerCard.getBankCode());
            if (bankInfo.getConstantStatus().equals("1")) {//无效银行卡
                isSupport = false;
            }
            request.setAttribute("hisCustomerCard", hisCustomerCard);
            request.setAttribute("isSupport", isSupport);
        }
        String name = userExt.getRealName();
        if (!StringUtils.isNull(name) && !name.equals("")) {
            if (name.length() == 2) {
                name = name.substring(0, 1) + "*";
            } else {
                name = name.substring(0, 1) + "*" + name.substring(name.length() - 1, name.length());
            }
            userExt.setRealName(name);
        }
        String idCard = userExt.getIdCard();
        if (!StringUtils.isNull(idCard) && !idCard.equals("")) {
            idCard = idCard.substring(0, 3) + "********" + idCard.substring(idCard.length() - 4, idCard.length());
            userExt.setIdCard(idCard);
        }
        mv.setViewName("bankcard/to_bidding");
        request.setAttribute("userExt", userExt);
        return mv;
    }

    /**
     * 跳转到：银行卡添加页
     */
    @RequestMapping(value = "/to_bankcard_add")
    public ModelAndView to_bankcard_add(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        TokenUtils.setNewToken(request);
        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        request.setAttribute("userExt", userExt);

        mv.setViewName("bankcard/backcard_binding");
        return mv;
    }

    /**
     * 连连支付充值请求
     *
     * @param request
     * @param rechargeAmount
     * @return
     */
    @RequestMapping(value = "/llRecharge")
    @ResponseBody
    public String llRecharge(HttpServletRequest request, HttpSession session, @RequestParam(value = "cardNo", required = false) String cardNo,
                             @RequestParam(value = "cardId", required = false) String cardId,
                             @RequestParam(value = "rechargeAmount", defaultValue = "0") BigDecimal rechargeAmount) {

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(currentUser.getUserId());
        // 校验-银行卡号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(cardNo))
            return JsonView.JsonViewFactory.create().success(false).info("银行卡号为空！").put("id", "bankid").toJson();

        // 校验-卡
        CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
        Long bankid = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(cardId)) {
            // 有卡
            if (card == null) {
                // 连连没有绑定，查询易宝绑定情况
                CustomerCard ybBindCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.YB);
                if (null != ybBindCard) {
                    // 易宝绑定的卡 连连是否支持
                    boolean support = LLPayUtil.checkLLCardSupport(ybBindCard.getCardCode());
                    if (!support) {
                        return JsonView.JsonViewFactory.create().success(false).info("您已绑定的卡支付通道不支持！").put("id", "bankid").toJson();
                    } else {
                        card = createNewCard(ybBindCard.getCardCode(), currentUser, userInfoExt, ybBindCard.getBankCode());
                    }
                } else {
                    // 易宝连连都没绑定
                    return JsonView.JsonViewFactory.create().success(false).info("您还没有绑定银行卡！").put("id", "bankid").toJson();
                }
            } else {
                if (!card.getCustomerCardId().toString().equals(cardId))
                    return JsonView.JsonViewFactory.create().success(false).info("您绑定的银行卡信息异常！").put("id", "redirect").toJson();
            }
        } else {
            // 无卡
            // 校验-银行卡号是否为空
            if (org.apache.commons.lang.StringUtils.isEmpty(cardNo)) {
                return JsonView.JsonViewFactory.create().success(false).info("银行卡号为空！").put("id", "bankid").toJson();
            }
            // 连连cardbin查询
            // 校验-该卡是否有效
            cardNo = cardNo.replace(" ", "");
            Map<String, String> result = LLPayUtil.bankCardCheck(cardNo);
            logger.info(LogUtils.createSimpleLog("银行卡信息查询接口返回", result.toString()));
            if (!"0000".equals(result.get("ret_code")))
                return JsonView.JsonViewFactory.create().success(false).info("无效的银行卡").put("id", "bankid").toJson();
            // 校验-必须是借记卡
            if (result.get("card_type").equals("3"))
                return JsonView.JsonViewFactory.create().success(false).info("不支持信用卡").put("id", "bankid").toJson();
            //校验-如果该卡不在支持的银行列表中
            String bankCode = result.get("bank_code");
            List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("bank");
            for (ConstantDefine constantDefine : constantDefines) {
                if (bankCode.equals(constantDefine.getConstantValue())) {
                    bankid = constantDefine.getConstantDefineId();
                    break;
                }
            }
            if (bankid == null)
                return JsonView.JsonViewFactory.create().success(false).info("不支持" + result.get("bank_name") + "卡").put("id", "cardNo").toJson();

            // 校验-用户是否已经绑卡
            if (card != null)
                return JsonView.JsonViewFactory.create().success(false).info("您已经绑过一张尾号：" + card.getCardCodeShort() + "卡了").put("id", "bankid")
                        .toJson();
            card = createNewCard(cardNo, currentUser, userInfoExt, bankid);
        }
        // 构造请求
        ClientEnum clientEnum = ClientEnum.from(currentUser.getAppSource());
        LLPayRequest llPayRequest = rechargeOrderService.createRechargeRequest(rechargeAmount, currentUser, userInfoExt, card, clientEnum);
        return JsonView.JsonViewFactory.create().success(true).info(llPayRequest.getUrl()).put("rechargeCode", llPayRequest.getRechargeOrder().getRechargeCode()).toJson();

    }

    /**
     * 跳转到：银行卡列表页
     */
    @RequestMapping(value = "/to_bankcard_list")
    public ModelAndView to_bankcard_list(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        try {
            // 新逻辑验证卡信息
            UserInfo currentUser = SecurityUtil.getCurrentUser(true);
            //是否实名认证
            UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
            if (!userExt.getIsVerified().equals(UserIsVerifiedEnum.BIND.getValue())) {
                mv.addObject("url", "/bankcard/to_bankcard_list");
                mv.setViewName("finance/toRealName");//未实名认证，跳转到认证提示页面
                return mv;
            }
            //获取银行卡信息
            CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
            if (null == customerCard) {
                customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.YB);
            }
            if (customerCard == null) {
                customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayChannel.HF);
            }
            mv.addObject("customerCardEnable", customerCard);
            if (null == customerCard) {// 如果没有可用银行卡，则跳转到添加页
                if (userExt.getIsVerified().equals(UserIsVerifiedEnum.NO)) {
                    mv.setViewName("/person/identityAuthenticationBy");
                } else {
                    return this.to_bidding(request);
                }

            } else {
                mv.setViewName("bankcard/bankcard_tip");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 执行：保存用户卡信息（页面点击保存事件）。
     *
     * @param bankid 银行卡号
     */
    @RequestMapping("/save_bankcard_add")
    @ResponseBody
    public Object saveBankCardAdd(HttpServletRequest request, HttpSession session,
                                  @RequestParam(value = "bankid", required = false) String bankid, BigDecimal amount) {

        // 合法性验证。
        if (null == bankid || "".equals(bankid)) {
            return returnResultMap(false, null, "check", "银行卡号不能为空！");
        } else {
            bankid = bankid.replace(" ", "");
            if (bankid.length() < 15 || !StringUtils.isPattern("^([0-9]+)$", bankid)) {
                return returnResultMap(false, null, "check", "银行卡号格式不正确！");
            }
        }

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(currentUser.getUserId());
        //校验-银行卡号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(bankid))
            return JsonView.JsonViewFactory.create().success(false).info("银行卡号为空！").put("id", bankid)
                    .toJson();
        //校验-购买金额是否大于50
        if (new BigDecimal("0.01").compareTo(amount) > 0)
            return JsonView.JsonViewFactory.create().success(false).info("请输入大于0.01元的金额！").put("id", "moneyp")
                    .toJson();

        //校验-卡
        CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
        Long bankId = null;

        //校验-银行卡号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(bankid)) {
            return JsonView.JsonViewFactory.create().success(false).info("银行卡号为空！").put("id", "bankid")
                    .toJson();
        }
        //连连cardbin查询
        //校验-该卡是否有效
        Map<String, String> result = LLPayUtil.bankCardCheck(bankid);
        logger.info(LogUtils.createSimpleLog("银行卡信息查询接口返回", result.toString()));
        if (!"0000".equals(result.get("ret_code")))
            return JsonView.JsonViewFactory.create().success(false).info("无效的银行卡").put("id", "bankid")
                    .toJson();
        //校验-必须是借记卡
        if (result.get("card_type").equals("3"))
            return JsonView.JsonViewFactory.create().success(false).info("不支持信用卡").put("id", "bankid")
                    .toJson();
        //校验-如果该卡不在支持的银行列表中
        String bankCode = result.get("bank_code");

        List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("bank");
        for (ConstantDefine constantDefine : constantDefines) {
            if (bankCode.equals(constantDefine.getConstantValue())) {
                bankId = constantDefine.getConstantDefineId();
                break;
            }
        }
        if (bankId == null)
            return JsonView.JsonViewFactory.create().success(false).info("不支持该银行卡").put("id", "bankid")
                    .toJson();

        //校验-用户是否已经绑卡
        if (card != null)
            return JsonView.JsonViewFactory.create().success(false).info("您已经绑过一张尾号：" + card.getCardCodeShort() + "卡了").put("id", "bankid")
                    .toJson();

        card = createNewCard(bankid, currentUser, userInfoExt, bankId);
        // 构造请求
        ClientEnum clientEnum = ClientEnum.from(currentUser.getAppSource());
        //构造请求
        LLPayRequest llPayRequest = rechargeOrderService.createRechargeRequest(new BigDecimal(0.01), currentUser, userInfoExt, card, clientEnum);
        return JsonView.JsonViewFactory.create().success(true).info(llPayRequest.getUrl()).put("rechargeCode", llPayRequest.getRechargeOrder().getRechargeCode())
                .toJson();

    }

    /**
     * 支付前要先记录支付请求所使用的银行卡，
     * 支付成功后标记此卡为绑定状态
     *
     * @param cardNo
     * @param currentUser
     * @param userInfoExt
     * @param bankid
     * @return
     */
    private CustomerCard createNewCard(@RequestParam(value = "cardNo") String cardNo, UserInfo currentUser, UserInfoExt userInfoExt, Long bankid) {
        CustomerCard customerCard = new CustomerCard();
        customerCard.setUserId(currentUser.getUserId());
        customerCard.setCardType(CardType.FULL_CARD.getValue());
        customerCard.setMobile(null);
        customerCard.setStatus(CustomerCardStatus.DISABLED.getValue());
        customerCard.setBelongChannel(PayConstants.CardChannel.LL.getValue());
        customerCard.setBindStatus(CustomerCardBindStatus.UNBINDING.getValue());
        customerCard.setCardCode(cardNo);
        customerCard.setCardcustomerName(userInfoExt.getRealName());
        customerCard.setUpdateTime(new Date());
        customerCard.setBankCode(bankid);
        return customerCard;
    }

    /**
     * 执行：身份验证。
     *
     * @param sf_name 姓名
     * @param sf_card 身份证号
     */
    @RequestMapping("/check_identity")
    @ResponseBody
    public Object checkIdentity(HttpServletRequest request, HttpSession session,
                                @RequestParam(value = "sf_name", required = false) String sf_name,
                                @RequestParam(value = "sf_card", required = false) String sf_card) {
        try {
            // 合法性验证。
            if (null == sf_name || "".equals(sf_name)) {
                return returnResultMap(false, null, "check", "姓名不能为空！");
            } else if (!StringUtils.isPattern("^[\u4E00-\u9FA5]{2,4}$", sf_name)) {
                return returnResultMap(false, null, "check", "姓名格式不正确！");
            }
            if (null == sf_card || "".equals(sf_card)) {
                return returnResultMap(false, null, "check", "身份证号不能为空！");
            } else if (!StringUtils.isPattern("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$", sf_card)) {
                return returnResultMap(false, null, "check", "身份证号格式不正确！");
            }

            // 调用身份验证接口
            // TODO ...

            // 更改相关数据库值
            // TODO ...

        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 执行：验证码发送（页面单击获取验证码事件）。
     *
     * @param phone 手机号
     */
    @RequestMapping("/send_sms")
    @ResponseBody
    public Object sendSMS(HttpServletRequest request, HttpSession session,
                          @RequestParam(value = "cardNo", required = false) String cardNo,
                          @RequestParam(value = "phone", required = false) String phone) {
        try {
            // 合法性验证。
            if (null == cardNo || "".equals(cardNo)) {
                return returnResultMap(false, null, "check", "银行卡号不能为空！");
            } else {
                cardNo = cardNo.replace(" ", "");
                if (cardNo.length() < 15 || !StringUtils.isPattern("^([0-9]+)$", cardNo)) {
                    return returnResultMap(false, null, "check", "银行卡号格式不正确！");
                }
            }
            if (null == phone || "".equals(phone)) {
                return returnResultMap(false, null, "check", "银行预留手机号不能为空！");
            } else if (!StringUtils.isPattern("(^1[3|4|5|7|8][0-9]{9}$)", phone)) {
                return returnResultMap(false, null, "check", "银行预留手机号格式不正确！");
            }

            // 获取当前登录用户
            UserInfo currentUser = SecurityUtil.getCurrentUser(true);

            //校验-用户是否已经绑卡
            CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayChannel.LL);
            if (card != null) {
                return returnResultMap(false, null, "check", "您已经绑过一张尾号：" + card.getCardCodeShort() + "卡了");
            }

            //校验-该卡是否有效
            Map<String, String> result = TZTService.bankCardCheck(cardNo);
            if (result.get("error_code") != null) {
                return returnResultMap(false, null, "check", result.get("error_msg"));
            }

            if (result.get("isvalid").equals("0")) {
                return returnResultMap(false, null, "check", "该卡已经失效");
            }

            //校验-必须是借记卡
            if (result.get("cardtype").equals("2")) {
                return returnResultMap(false, null, "check", "不支持信用卡");
            }

            if (null == result.get("bankname") && "".equals(result.get("bankname"))) {
                return returnResultMap(false, null, "check", "银行卡名称获取失败");
            }

            //校验该卡是否在支持的银行列表中
            String bankname = result.get("bankname");
            Long bankid = null;
            List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("bank");
            if (null != constantDefines && constantDefines.size() > 0) {
                for (ConstantDefine constantDefine : constantDefines) {
                    if (bankname.equals(constantDefine.getConstantName())) {
                        bankid = constantDefine.getConstantDefineId();
                        break;
                    }
                }
            } else {
                return returnResultMap(false, null, "check", "银行卡列表获取失败");
            }

            // 获取用户扩展信息
            UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
            if (null == userExt) {
                return returnResultMap(false, null, "check", "获取用户信息失败");
            } else if (UserIsVerifiedEnum.NO.getValue().equals(userExt.getIsVerified())) {
                return returnResultMap(false, null, "check", "请先进行身份验证");
            }


            //（2）请求绑卡
            Map map = new TreeMap<String, String>();
            map.put("identityid", StringUtils.getFixLengthUserId(currentUser.getUserId().toString()));
            map.put("identitytype", "2");
            map.put("requestid", UUID.randomUUID().toString().replaceAll("-", ""));
            map.put("cardno", cardNo);
            map.put("idcardtype", "01");
            map.put("idcardno", userExt.getIdCard());
            map.put("username", userExt.getRealName());
            map.put("phone", phone);
            map.put("userip", SecurityUtil.getIpAddr(request));
            ValidationUtil.checkNotExistNullValue(map);
            result = TZTService.bindBankcard(map);
            if (result.get("error_code") != null) {
                return returnResultMap(false, null, "check", result.get("error_msg"));
            }
            //（1）新建用户卡
            CustomerCard customerCard = new CustomerCard();
            customerCard.setUserId(currentUser.getUserId());
            customerCard.setCardType(CardType.FULL_CARD.getValue());
            customerCard.setMobile(phone);
            customerCard.setStatus(CustomerCardStatus.DISABLED.getValue());
            customerCard.setCardCode(cardNo);
            customerCard.setCardcustomerName(userExt.getRealName());
            customerCard.setUpdateTime(new Date());
            customerCard.setBankCode(bankid);
            customerCard = customerCardService.addCustomerCard(customerCard);
            Map<String, String> bindCardInfo = new HashMap<String, String>();
            bindCardInfo.put("bcCardId", customerCard.getCustomerCardId().toString());
            bindCardInfo.put("bcRequestId", result.get("requestid"));
            bindCardInfo.put("bcCardNo", cardNo);
            bindCardInfo.put("bcPhone", phone);
            request.getSession().setAttribute("bindCardInfo", bindCardInfo);
            logger.info("存储银行卡银行卡信息：" + request.getSession().getAttribute("bindCardInfo") + " sessionId:" + request.getSession().getId());
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 执行：校验银行卡信息（页面鼠标移除事件）。
     *
     * @param cardNo 银行卡号
     */
    @RequestMapping("/check_card")
    @ResponseBody
    public Object checkCard(HttpServletRequest request, HttpSession session, @RequestParam(value = "cardNo", required = false) String cardNo) {
        // post验证
        RequestUtil.validateRequestMethod(request, RequestMethod.POST);
        Map resultMap = new HashMap();
        try {
            // 合法性验证。
            if (null == cardNo || "".equals(cardNo)) {
                return returnResultMap(false, null, "check", "银行卡号不能为空！");
            } else {
                cardNo = cardNo.replace(" ", "");
                if (cardNo.length() < 15 || !StringUtils.isPattern("^([0-9]+)$", cardNo)) {
                    return returnResultMap(false, null, "check", "银行卡号格式不正确！");
                }
            }

            // 获取当前登录用户
            UserInfo currentUser = SecurityUtil.getCurrentUser(true);

            // 校验-用户是否已经绑卡
            CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayChannel.LL);
            if (card != null) {
                return returnResultMap(false, null, "check", "您已经绑过一张尾号：" + card.getCardCodeShort() + "卡了");
            }

            // 校验-该卡是否有效
            Map<String, String> result = LLPayUtil.bankCardCheck(cardNo);
            logger.info(LogUtils.createSimpleLog("银行卡信息查询接口返回", result.toString()));
            if (!"0000".equals(result.get("ret_code")))
                return returnResultMap(false, null, "check", "无效的银行卡");
            // 校验-必须是借记卡
            if (result.get("card_type").equals("3"))
                return returnResultMap(false, null, "check", "不支持信用卡");
            // 校验-如果该卡不在支持的银行列表中
            String bankCode = result.get("bank_code");
            Long bankid = null;
            String bankname = null;
            List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("bank");

            if (null != constantDefines && constantDefines.size() > 0) {
                for (ConstantDefine constantDefine : constantDefines) {
                    if (bankCode.equals(constantDefine.getConstantValue())) {
                        bankid = constantDefine.getConstantDefineId();
                        bankname = constantDefine.getConstantName();
                        break;
                    }
                }
            } else {
                return returnResultMap(false, null, "check", "银行卡列表获取失败");
            }

            if (bankid == null)
                return returnResultMap(false, null, "check", "不支持该银行卡");

            // 封装返回值
            resultMap.put("bankname", bankname);
            resultMap.put("bankid", bankid);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, resultMap, null, null);
    }

    @RequestMapping(value = "/bindCardProtocol")
    public ModelAndView bindCardProtocol(HttpServletRequest request) {
        return new ModelAndView("/common/bindCardProtocol");
    }
}
