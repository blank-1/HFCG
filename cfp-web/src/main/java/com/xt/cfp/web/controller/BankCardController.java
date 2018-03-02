package com.xt.cfp.web.controller;

import com.external.llpay.BankInfo;
import com.external.llpay.LLPayRequest;
import com.external.llpay.LLPayUtil;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.impl.CgBizService;
import com.xt.cfp.core.util.*;
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
	
	@Autowired
	private CustomerCardService customerCardService;
	
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private CityInfoService cityInfoService;
	
	@Autowired
	private ConstantDefineService constantDefineService;
	@Autowired
	private UserInfoExtService userInfoExtService ;
	@Autowired
	private WithDrawService withDrawService;
	@Autowired
	private RechargeOrderService rechargeOrderService;
	@Autowired
	private CgBizService cgBizService;
	
	/**
	 * 跳转到：银行卡添加页
	 */
    public ModelAndView to_bankcard_add(HttpServletRequest request) {
    	ModelAndView mv = new ModelAndView();
    	
    	UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        TokenUtils.setNewToken(request);
     		UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
		request.setAttribute("userExt",userExt);
		//历史卡信息
		CustomerCard hisCustomerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(),PayConstants.PayChannel.YB);
		if(hisCustomerCard!=null){
			boolean isSupport = true;
			ConstantDefine bankInfo = constantDefineService.findById(hisCustomerCard.getBankCode());
			if (bankInfo.getConstantStatus().equals("1")){//无效银行卡
				isSupport = false;
			}
			request.setAttribute("hisCustomerCard", hisCustomerCard);
			request.setAttribute("isSupport", isSupport);
		}
		request.setAttribute("userExt",userExt);
		mv.setViewName("bankcard/bankcard_add");
        return mv;
    }
    
    /**
     * 跳转到：银行卡列表页
     */
    @RequestMapping(value = "/to_bankcard_list")
    public ModelAndView to_bankcard_list(HttpServletRequest request) {
    	ModelAndView mv = new ModelAndView();
        try {
        	UserInfo userInfo = SecurityUtil.getCurrentUser(true);
        	List<CustomerCard> customerCards = customerCardService.getCustomerAllCardsByUserId(userInfo.getUserId());
        	CustomerCard customerCardEnable = null;
        	List<CustomerCard> customerCardDisableList = new ArrayList<CustomerCard>();
        	if(null != customerCards && customerCards.size() > 0){
        		for (CustomerCard customerCard : customerCards) {
        			BigDecimal bigDecimal = withDrawService.getWithdrawAmountSumByCardId(customerCard.getCustomerCardId());
        			if(null != bigDecimal){
        				customerCard.setWithdrawAmountSum(bigDecimal);
        			}
					CgBank cgBank = cgBizService.getBankInfo(customerCard.getBankNum(), CgBank.IdTypeEnum.PERSON);
    				if(CustomerCardStatus.NORMAL.getValue().equals(customerCard.getStatus())
    						&& CustomerCardBindStatus.BINDED.getValue().equals(customerCard.getBindStatus())
							&& PayConstants.PayChannel.HF.getValue().equals(customerCard.getBelongChannel())){
    					customerCardEnable = customerCard;
						customerCardEnable.setBankCode(cgBank.getIconCode());
					}else {
						customerCard.setBankCode(cgBank.getIconCode());
    					customerCardDisableList.add(customerCard);
    				}
    			}
        	}

        	mv.addObject("customerCardEnable", customerCardEnable);
        	mv.addObject("customerCardDisableList", customerCardDisableList);
        	if(null == customerCardEnable){// 如果没有可用银行卡，则跳转到添加页
        		return this.to_bankcard_add(request);
        	}else {
        		mv.setViewName("bankcard/bankcard_list");	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return mv;
    }


	/**
     * 跳转到：银行卡列表页
     */
    @RequestMapping(value = "/to_edit_bank")
    public String to_edit_bank(HttpServletRequest request) {
    	ModelAndView mv = new ModelAndView();
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.HF);
		request.setAttribute("card",card);
		/*ConstantDefine bank = constantDefineService.findById(card.getBankCode());
		request.setAttribute("bank",bank);*/
		UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
		request.setAttribute("userExt",userExt);
		List<LianLianProvinceCity> lianLianProvinceCities = cityInfoService.getLianLianProvinceCityByPID(0L);
		request.setAttribute("province",lianLianProvinceCities);

		if (card.getProvinceCityId()!=null){
			LianLianProvinceCity city = cityInfoService.getLianLianProvinceCityById(card.getProvinceCityId());
			request.setAttribute("city",city);
			LianLianProvinceCity provinceSelect = cityInfoService.getLianLianProvinceCityById(city.getpProvinceCityId());
			request.setAttribute("provinceSelect",provinceSelect);
			List<LianLianProvinceCity> citys = cityInfoService.getLianLianProvinceCityByPID(provinceSelect.getProvinceCityId());
			request.setAttribute("citys",citys);
		}

		return "bankcard/bankcard_edit";
    }

	/**
	 * 查找对应省份的城市列表
	 */
	@RequestMapping(value = "/getCity")
	@ResponseBody
	public Object getCity(HttpServletRequest request,Long provinceId) {
		if(provinceId==null)
			return returnResultMap(false, null, "check", "请先选择省份！");
		List<LianLianProvinceCity> lianLianProvinceCities = cityInfoService.getLianLianProvinceCityByPID(provinceId);
		return lianLianProvinceCities;
	}

	/**
	 * 查找对应银行列表
	 */
	@RequestMapping(value = "/getBankList")
	@ResponseBody
	public Object getBankList(HttpServletRequest request,Long cityId,String bankName) {
		if(cityId==null)
			return returnResultMap(false, null, "check", "请先选择城市！");
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
		LianLianProvinceCity city = cityInfoService.getLianLianProvinceCityById(cityId);
		Map<String, String> params = new HashMap<String,String>();
		params.put("card_no",card.getCardCode());
		params.put("city_code",city.getProvinceCityCode());
		params.put("brabank_name",bankName);
		List<BankInfo> cnapsCode = LLPayUtil.getCNAPSCode(params);
		return cnapsCode;
	}


	/**
     * 执行：保存用户卡信息（页面点击保存事件）。
     * @param bankid 银行卡号
     */
 	@RequestMapping("/save_bankcard_add")
 	@ResponseBody
 	public Object saveBankCardAdd(HttpServletRequest request, HttpSession session,
 			@RequestParam(value = "bankid", required = false) String bankid,BigDecimal amount){

		// 合法性验证。
		if(null == bankid || "".equals(bankid)){
			return returnResultMap(false, null, "check", "银行卡号不能为空！");
		}else {
			bankid = bankid.replace(" ", "");
			if(bankid.length() < 15 || !StringUtils.isPattern("^([0-9]+)$", bankid)){
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
		if (org.apache.commons.lang.StringUtils.isEmpty(bankid)){
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
			if(bankCode.equals(constantDefine.getConstantValue())){
				bankId = constantDefine.getConstantDefineId();
				break;
			}
		}
		if (bankId == null)
			return JsonView.JsonViewFactory.create().success(false).info("不支持该银行卡").put("id", "bankid")
					.toJson();

		//校验-用户是否已经绑卡
		if (card != null)
			return JsonView.JsonViewFactory.create().success(false).info( "您已经绑过一张尾号：" + card.getCardCodeShort() + "卡了").put("id", "bankid")
					.toJson();

		card = createNewCard(bankid, currentUser, userInfoExt, bankId);

		//构造请求
		LLPayRequest llPayRequest = rechargeOrderService.createRechargeRequest(amount, currentUser, userInfoExt, card,ClientEnum.WEB_CLIENT);
		return JsonView.JsonViewFactory.create().success(true).info(llPayRequest.getUrl()).put("rechargeCode",llPayRequest.getRechargeOrder().getRechargeCode())
				.toJson();

 	}

	/**
	 * 支付前要先记录支付请求所使用的银行卡，
	 * 支付成功后标记此卡为绑定状态
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
 	 * @param sf_name 姓名
 	 * @param sf_card 身份证号
 	 */
 	@RequestMapping("/check_identity") 
 	@ResponseBody
 	public Object checkIdentity(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "sf_name", required = false) String sf_name,
    		@RequestParam(value = "sf_card", required = false) String sf_card){
 		try {
 			// 合法性验证。
 			if(null == sf_name || "".equals(sf_name)){
 				return returnResultMap(false, null, "check", "姓名不能为空！");
 			}else if(!StringUtils.isPattern("^[\u4E00-\u9FA5]{2,4}$", sf_name)){
				return returnResultMap(false, null, "check", "姓名格式不正确！");
			}
 			if(null == sf_card || "".equals(sf_card)){
 				return returnResultMap(false, null, "check", "身份证号不能为空！");
 			}else if(!StringUtils.isPattern("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$", sf_card)){
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
 	 * 执行：校验银行卡信息（页面鼠标移除事件）。
 	 * @param cardNo 银行卡号
 	 */
 	@RequestMapping("/check_card")
 	@ResponseBody
 	public Object checkCard(HttpServletRequest request, HttpSession session,
 			@RequestParam(value = "cardNo", required = false) String cardNo){
		//post验证
		RequestUtil.validateRequestMethod(request, RequestMethod.POST);
 		Map resultMap = new HashMap();
 		try {
 			// 合法性验证。
 			if(null == cardNo || "".equals(cardNo)){
 				return returnResultMap(false, null, "check", "银行卡号不能为空！");
 			}else {
 				cardNo = cardNo.replace(" ", "");
				if(cardNo.length() < 15 || !StringUtils.isPattern("^([0-9]+)$", cardNo)){
					return returnResultMap(false, null, "check", "银行卡号格式不正确！");
				}
			}
 			
 			// 获取当前登录用户
 			UserInfo currentUser = SecurityUtil.getCurrentUser(true);
 			
 	        //校验-用户是否已经绑卡
 	        CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
 	        if (card != null){
 	        	return returnResultMap(false, null, "check", "您已经绑过一张尾号：" + card.getCardCodeShort() + "卡了");
 	        }

 	        //校验-该卡是否有效
			Map<String, String> result = LLPayUtil.bankCardCheck(cardNo);
			if (!"0000".equals(result.get("ret_code")))
				return returnResultMap(false, null, "check", "无效的银行卡");
			//校验-必须是借记卡
			if (result.get("card_type").equals("3"))
				return returnResultMap(false, null, "check", "不支持信用卡");
 	        
 	        if (null == result.get("bank_name") && "".equals(result.get("bank_name"))){
 	        	return returnResultMap(false, null, "check", "银行卡名称获取失败");
	        }

 	        //校验该卡是否在支持的银行列表中
			String bankCode = result.get("bank_code");
 		    Long bankid = null;
 	        List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("bank");
 	        if(null != constantDefines && constantDefines.size() > 0){
 	        	for (ConstantDefine constantDefine : constantDefines) {
					if(bankCode.equals(constantDefine.getConstantValue())){
						bankid = constantDefine.getConstantDefineId();
						break;
					}
				}
 	        }else {
 	        	return returnResultMap(false, null, "check", "银行卡列表获取失败");
			}
			if (bankid == null)
				return returnResultMap(false, null, "check", "不支持该银行卡");
 	        //封装返回值
 	        resultMap.put("bankname", result.get("bank_name"));
 	        resultMap.put("bankid", bankid);
 		} catch (Exception e) {
 			e.printStackTrace();
 			return returnResultMap(false, null, null, e.getMessage());
 		}
 		return returnResultMap(true, resultMap, null, null);
 	}

	@RequestMapping("/editBank")
	public String editBank(HttpServletRequest request, HttpSession session,String flag,
						   @RequestParam(value = "cityId") Long cityId,@RequestParam(value = "prcptcd",required = false) String prcptcd,@RequestParam(value = "text") String text){
		RequestUtil.validateRequestMethod(request,RequestMethod.POST);

		if (cityId==null)
			return "/error";

		if (org.apache.commons.lang.StringUtils.isEmpty(text)){
			return "/error";
		}
		// 获取当前登录用户
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
		customerCard.setRegisteredBank(text);
		customerCard.setBankLineNumber(prcptcd);
		customerCard.setProvinceCityId(cityId);

		customerCardService.saveOrUpdateCustomerCard(customerCard);
		if ("1".equals(flag))
			return "redirect:/bankcard/to_bankcard_list";
		else
			return "redirect:/person/toWithDraw";

	}
    
}
