package com.xt.cfp.web.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.constants.WechatActConstants;
import com.xt.cfp.core.pojo.PrizePool;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserShare;
import com.xt.cfp.core.pojo.ext.IncreaseVO;
import com.xt.cfp.core.pojo.ext.VoucherPhoneVO;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.VoucherPhoneService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.SecurityUtil;
import com.xt.cfp.web.annotation.DoNotNeedLogin;

/**
 * Created by wangxudong on 2015/7/2.
 */
@Controller
@RequestMapping(value = "/activity")
public class ActivityController extends BaseController {
	@Autowired
	LendOrderService lendOrderService;
	@Autowired
	VoucherPhoneService voucherPhoneService;

	/**
	 * web大转盘
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/toWebTurntable")
	public ModelAndView webTurntable(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		UserInfo currentUser = SecurityUtil.getCurrentUser(false);
		BigDecimal allBuyBalance = BigDecimal.ZERO;// 累计出借金额
		if (currentUser != null) {
			allBuyBalance = lendOrderService.getTotalLendAmount(currentUser.getUserId());
			//获取抽奖次数
			UserShare userShare = voucherPhoneService.getUserSHareByUserId(currentUser.getUserId(), "franchiseeV07");
			mv.addObject("shareNum", calculatePrizeNum(userShare, allBuyBalance, currentUser.getUserId()));
		}else {
			//获取抽奖次数
			mv.addObject("shareNum", -1);
		}
		mv.addObject("allBuyBalance", allBuyBalance);
		mv.addObject("allBuyBalance", allBuyBalance);
		Date date = new Date();
		if(date.getTime()-DateUtil.parseStrToDate("2016-01-31 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime()<=0){
			mv.setViewName("/activity/sudokuSweepstake/sweepstakeNew");
		}else{
			mv.setViewName("/activity/sudokuSweepstake/sweepstakeEnd");
		}
		
		return mv;
	}
    /**
	 * 计算抽奖次数
	 * @param list
	 * @return
	 */
	public int calculatePrizeNum(UserShare userShare, BigDecimal allBuyBalance, Long userId){
		int prizeNum = 0;
		if(allBuyBalance.compareTo(new BigDecimal(100)) >=0 && allBuyBalance.compareTo(new BigDecimal(10000)) < 0){
			prizeNum = 1;
		}else if(allBuyBalance.compareTo(new BigDecimal(10000)) >=0 && allBuyBalance.compareTo(new BigDecimal(100000)) < 0){
			prizeNum = 2;
		}else if(allBuyBalance.compareTo(BigDecimal.ZERO) >=0 && allBuyBalance.compareTo(new BigDecimal(100)) < 0){
			prizeNum = 0;
		}else{
			prizeNum = 3;
		}
		if(userShare != null){
			if(prizeNum > userShare.getLuckDrawNum()){
				userShare.setLuckDrawNum(prizeNum);
				voucherPhoneService.updateUserShare(userShare);
			}
		}else{
			UserShare userShareNew = new UserShare();
			userShareNew.setUserId(userId);
			userShareNew.setActivityNumber("franchiseeV07");
			userShareNew.setCreateTime(new Date());
			userShareNew.setIsShare(WechatActConstants.IsShareEnum.NOT_SHARE.getValue());
			userShareNew.setLuckDrawNum(prizeNum);
			userShareNew.setUsedLuckDrawNum(0);
			voucherPhoneService.addUserShare(userShareNew);
			userShare = userShareNew;
		}
		return userShare.getLuckDrawNum()-userShare.getUsedLuckDrawNum();
	}
	@ResponseBody
	@RequestMapping(value="/toTurntableDraw")
	public String toTurntableDraw(HttpServletRequest request){
		int gameWinningLevel = -1;
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		List<PrizePool> prizePoolList = voucherPhoneService.getPrizePoolList(false);
		UserShare userShare = voucherPhoneService.getUserSHareByUserId(currentUser.getUserId(), "franchiseeV07");
		if(userShare !=null && userShare.getLuckDrawNum()-userShare.getUsedLuckDrawNum()>0){
			BigDecimal allBuyBalance = lendOrderService.getTotalLendAmount(currentUser.getUserId());
			if(allBuyBalance.compareTo(BigDecimal.ZERO)<=0)
				return JsonView.JsonViewFactory.create().success(false).info("您尚未投标").put("id", "nolend")
		                .toJson();
			gameWinningLevel = voucherPhoneService.turntableDraw(currentUser, userShare, allBuyBalance);
		}else{
			return JsonView.JsonViewFactory.create().success(false).info("您抽奖次数不足").put("id", "nonum")
	                .toJson();
		}
		return JsonView.JsonViewFactory.create().success(true).info("").put("id", gameWinningLevel==-1?prizePoolList.size()-1:gameWinningLevel-1)
                .toJson();
	}
	@ResponseBody
	@RequestMapping(value="/toIsTurntableDraw")
	public String toIsTurntableDraw(HttpServletRequest request){
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		int num = 0;
		UserShare userShare = voucherPhoneService.getUserSHareByUserId(currentUser.getUserId(), "franchiseeV07");
		if(userShare !=null && userShare.getLuckDrawNum()-userShare.getUsedLuckDrawNum()>0){
			BigDecimal allBuyBalance = lendOrderService.getTotalLendAmount(currentUser.getUserId());
			if(allBuyBalance.compareTo(BigDecimal.ZERO)<=0)
				return JsonView.JsonViewFactory.create().success(false).info("您尚未投标").put("id", "nolend")
		                .toJson();
			num = calculatePrizeNum(userShare, allBuyBalance, currentUser.getUserId());
		}else{
			return JsonView.JsonViewFactory.create().success(false).info("您抽奖次数不足").put("id", "nonum")
	                .toJson();
		}
		return JsonView.JsonViewFactory.create().success(true).info("").put("num", num)
                .toJson();
	}

	/**
	 * 加息券抽奖
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DoNotNeedLogin
	@RequestMapping(value = "/increaseRate")
	public String increaseRate(HttpServletRequest request, ModelMap model) {
		String result = "ok";
		if(new Date().getTime()<DateUtil.parseStrToDate("2016-08-25","yyyy-MM-dd").getTime()){
			result="活动还没开始"; } 
		if(new Date().getTime()>=DateUtil.parseStrToDate("2016-08-27","yyyy-MM-dd").getTime()){
			result="活动已经结束"; }
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
			List<VoucherPhoneVO> list = voucherPhoneService
					.getHasLotteryDrawUsers(map);
			if (list != null && list.size() > 0) {
				for (VoucherPhoneVO vo : list) {
					vo.setMobileNo(vo.getMobileNo().substring(0, 3)
							+ "****"
							+ vo.getMobileNo().substring(
									vo.getMobileNo().length() - 4));
				}
				model.put("list", list);
			}
		}
		return "/activity/increaseRate/index";
	}

	/**
	 * 加息券开始抽奖
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/startIncreaseRateGame")
	public String startIncreaseRateGame(HttpServletRequest request) {
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		Map map = new HashMap();
		map.put("result", "1");
		map.put("msg", "ok");
		if(new Date().getTime()>= DateUtil.parseStrToDate("2016-08-27","yyyy-MM-dd").getTime()){
			map.put("result", "0");
			map.put("msg", "活动已经结束");
			return JSONObject.toJSONString(map);
		}
		if(new Date().getTime()<= DateUtil.parseStrToDate("2016-08-25","yyyy-MM-dd").getTime()){
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
			List<VoucherPhoneVO> users = voucherPhoneService
					.getHasLotteryDrawUsers(map);
			in.setUsers(users);
			map.clear();
			map.put("name", "increaseRate");
			map.put("number", 21);
			List<VoucherPhoneVO> list = voucherPhoneService
					.getHasLotteryDrawUsers(map);
			if (list != null && list.size() > 0) {
				for (VoucherPhoneVO vo : list) {
					vo.setMobileNo(vo.getMobileNo().substring(0, 3)
							+ "****"
							+ vo.getMobileNo().substring(
									vo.getMobileNo().length() - 4));
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
