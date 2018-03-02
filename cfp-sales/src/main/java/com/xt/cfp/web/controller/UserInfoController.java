package com.xt.cfp.web.controller;

import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.BondSourceExt;
import com.xt.cfp.core.pojo.ext.SalesAdminUserInfoVo;
import com.xt.cfp.core.pojo.ext.SalesUserInfo;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.pojo.ext.phonesell.LendOrderVO;
import com.xt.cfp.core.pojo.ext.phonesell.PrepaidVO;
import com.xt.cfp.core.pojo.ext.phonesell.WithdrawVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.MD5Util;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.web.annotation.DoNotNeedLogin;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user")
public class UserInfoController extends BaseController {

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private SalesAdminInfoService salesAdminInfoService;
	@Autowired
	private LendOrderService lendOrderService;
	@Autowired
	private WithDrawService withDrawService;
	@Autowired
	private RedisCacheManger redisCacheManger;

	@Autowired
	private RechargeOrderService rechargeOrderService;
	/**
	 * 用户注册
	 */
	@RequestMapping(value = "/regist")
	public ModelAndView regist() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsp/user/regist");
		return mv;
	}

	/**
	 * 用户注册成功
	 */
	@RequestMapping(value = "/registSuccess")
	public ModelAndView registSuccess(String type,String phone) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("type",type);
		mv.addObject("phone",phone);
		UserInfo userByMobileNo = userInfoService.getUserByMobileNo(phone);
		mv.addObject("user",userByMobileNo);
		mv.setViewName("jsp/user/registSuccess");
		return mv;
	}

	/**
	 * 执行：登录
	 */
	@DoNotNeedLogin
	@RequestMapping(value="/registUser", method = RequestMethod.POST)
	@ResponseBody
	public Object registUser(HttpServletRequest request, HttpSession session,
								   @ModelAttribute("phone") String phone) {

		if (StringUtils.isEmpty(phone)){
			return returnResultMap(false, null, "check", "手机号不能为空！");
		}

		if (!this.mobileNoValidate(phone)){
			return returnResultMap(false, null, "check", "手机号格式不正确！");
		}

		boolean mobileExist = userInfoService.isMobileExist(phone);
		if (mobileExist){
			return returnResultMap(true, "此手机号已注册过！", "check", "此手机号已注册过！");
		}
		String key = "sales_registing"+phone;
		String value = (String)redisCacheManger.getRedisCacheInfo(key);
		if(StringUtils.isEmpty(value)){
			//向redis服务器添加标识
			redisCacheManger.setRedisCacheInfo(key,"1");
			//注册
			try{
				userInfoService.regist(phone);
			}catch (Exception e){
				e.printStackTrace();
				return returnResultMap(false, null, "check", "注册异常！");
			}finally {
				//从redis服务器删除标记
				redisCacheManger.destroyRedisCacheInfo(key);
			}
		}else{
			return returnResultMap(false, null, "check", "此手机号已提交注册，请勿重复提交！");
		}
		return returnResultMap(true, null, "check", "null");
	}

	/**
	 * 跳转至用户列表页面
	 * @return
	 */
	@RequestMapping(value = "userList")
	public String toBondSourceList() {
		return "jsp/user/userList";
	}
	/**
	 * 跳转至用户列表页面
	 * @return
	 */
	@RequestMapping(value = "detail")
	public String detail(Long userId,HttpServletRequest request) {
		UserInfo userInfo = userInfoService.getUserByUserId(userId);

		SalesAdminInfo salesAdminByUserId = salesAdminInfoService.getSalesAdminByUserId(userInfo.getUserId());
		if (salesAdminByUserId==null){
			return "jsp/user/detail";
		}
		request.setAttribute("userInfo",userInfo);
		UserInfoVO userInfoExt = userInfoService.getUserExtByUserId(userId);
		request.setAttribute("userInfoExt",userInfoExt);
		//用户账户信息
		UserAccount cashAccount = userAccountService.getCashAccount(userId);
		request.setAttribute("cashAccount",cashAccount);
		BigDecimal totalProfit = lendOrderService.getAllProfit(userId);
		BigDecimal totalAward = lendOrderService.getTotalAward(userId);
		request.setAttribute("totalProfit", totalProfit);
		request.setAttribute("allProfit", totalProfit.add(totalAward));
		//投资总额
		BigDecimal totalLendAmount = lendOrderService.getTotalLendAmount(userId);
		request.setAttribute("totalLendAmount", totalLendAmount);
		//充值总额
		BigDecimal allRechargeValue = rechargeOrderService.getAllRechargeValueByUserId(userId);
		request.setAttribute("allRechargeValue", allRechargeValue);
		//提现总额
		BigDecimal allWithDrawValue = withDrawService.getAllWithDrawAmountByUserId(userId);
		request.setAttribute("allWithDrawValue", allWithDrawValue);
		//变更记录
		List<SalesAdminUserInfoVo> recordList = salesAdminInfoService.getChangeRecord(userId);
		request.setAttribute("recordList", recordList);
		
		SalesAdminInfo adminInfo=(SalesAdminInfo)request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
		request.setAttribute("adminCode", adminInfo.getLoginName());
		
		Map<String, Object> customParams = new HashMap<String, Object>();
		int pageNo=1,pageSize=5;
		//充值记录
		customParams.put("userCode", userInfo.getLoginName());
		List<PrepaidVO> prepaids = rechargeOrderService.getPrepaidPaging(pageNo, pageSize, customParams).getRows();
		request.setAttribute("prepaids", prepaids);
		//投资记录
		List<LendOrderVO> orders = lendOrderService.getPhonesellOrder(pageNo, pageSize, customParams).getRows();
		request.setAttribute("orders", orders);
		//提现记录
		List<WithdrawVO> withdraws = withDrawService.phonesellWithDrawPaging(pageNo, pageSize, customParams).getRows();
		request.setAttribute("withdraws", withdraws);
		
		//账户图表
		request.setAttribute("chartData",getAccountChartData(userId));

		return "jsp/user/detail";
	}

	private String[] getAccountChartData(Long userId) {

		Date now = new Date();
		String monthStr = "[";
		//投资额
		String str = "[{ name:'充值额',data:[";
		for(int i=-5;i<=0;i++){
			Date date = DateUtil.addDate(now, Calendar.MONTH, i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			monthStr += "'" + sdf.format(date) + "',";
			BigDecimal totalRechargeAmount = rechargeOrderService.getAllRechargeValueByUserId(userId, sdf.format(date));
			str += totalRechargeAmount+",";
		}
		str = str.substring(0,str.length()-1)+"]},";

		//充值额
		str += "{ name:'投资额',data:[";
		for(int i=-5;i<=0;i++){
			Date date = DateUtil.addDate(now, Calendar.MONTH, i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			BigDecimal totalLendAmount = lendOrderService.getTotalLendAmount(userId, sdf.format(date));
			str += totalLendAmount+",";
		}
		str = str.substring(0,str.length()-1)+"]},";
		//提现额
		str += "{ name:'提现额',data:[";
		for(int i=-5;i<=0;i++){
			Date date = DateUtil.addDate(now, Calendar.MONTH, i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			BigDecimal totalWithDrawAmount = withDrawService.getAllWithDrawAmountByUserId(userId, sdf.format(date));
			str += totalWithDrawAmount+",";
		}
		str = str.substring(0,str.length()-1)+"]}]";
		monthStr = monthStr.substring(0, monthStr.length() - 1) + "]";
		String[] _str = {monthStr,str};
		return _str;
	}

	/**
	 * 客户变更
	 * @param userId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "changAdmin")
	public String changAdmin(Long userId,HttpServletRequest request) {
		UserInfo userInfo = userInfoService.getUserByUserId(userId);
		request.setAttribute("userInfo",userInfo);
		UserInfoVO userInfoExt = userInfoService.getUserExtByUserId(userId);
		request.setAttribute("userInfoExt",userInfoExt);
		//所属客服
		SalesAdminInfo salesAdmin = salesAdminInfoService.getSalesAdminByUserId(userId);
		request.setAttribute("salesAdmin",salesAdmin);

		//所有员工
		List<SalesAdminInfo> allSalesAdmin = salesAdminInfoService.getAllSalesAdmin();
		request.setAttribute("allSalesAdmin",allSalesAdmin);
		return "jsp/user/changeAdmin";
	}

	/**
	 * 客户列表
	 * @param request
	 * @param salesUserInfo
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	@RequestMapping(value = "list")
	@ResponseBody
	public Object list(HttpServletRequest request,
					   @ModelAttribute SalesUserInfo salesUserInfo,
					   @RequestParam(value = "rows", defaultValue = "10") int pageSize,
					   @RequestParam(value = "page", defaultValue = "1") int pageNo) {
		SalesAdminInfo salesAdminInfo = (SalesAdminInfo) request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
		salesUserInfo.setAdminId(salesAdminInfo.getAdminId());

		Pagination<SalesUserInfo> userInfo = userInfoService.getSalesUserPaging(pageNo, pageSize, salesUserInfo, null);

		return userInfo;
	}

	/**
	 * 变更
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "change")
	@ResponseBody
	public Object list(HttpServletRequest request,
					   @RequestParam Long adminId ,Long userId) {
		salesAdminInfoService.updateAdminUserInfo(userId,adminId);
		return "success";
	}



}
