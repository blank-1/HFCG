package com.xt.cfp.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.xt.cfp.core.constants.DisActivityEnums;

import com.xt.cfp.core.constants.DisActivityEnums.DarCommiRatioTypeEnum;
import com.xt.cfp.core.constants.DisActivityEnums.DisAcivityUserTypeEnum;
import com.xt.cfp.core.constants.DisActivityEnums.DisActivityStateEnum;
import com.xt.cfp.core.constants.LendProductPublishStateEnum;
import com.xt.cfp.core.pojo.DisActivity;
import com.xt.cfp.core.pojo.DisActivityRules;
import com.xt.cfp.core.pojo.LendProduct;

import com.xt.cfp.core.pojo.ext.CommitProfitVO;
import com.xt.cfp.core.service.DisActivityRulesService;

import com.xt.cfp.core.pojo.ext.DisActivityRulesExt;
import com.xt.cfp.core.pojo.ext.DisActivityVO;
import com.xt.cfp.core.pojo.ext.DistributorVO;
import com.xt.cfp.core.service.DisActivityRulesService;
import com.xt.cfp.core.service.DisActivityService;
import com.xt.cfp.core.service.DistributionInviteService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.core.util.TimeInterval;

/**
 * 三级分销
 * 
 * @创建时间 2016年4月26日 下午3:38:24
 *
 */
@Controller
@RequestMapping(value = "/disActivity")
public class DisActivityController extends BaseController {

	@Autowired
	private DisActivityService disActivityService;
	
	@Autowired
	private DisActivityRulesService disActivityRulesService;
	
	

 

	@Autowired
	private LendProductService lendProductService;
 
	
	
	
	/**
	 * 分销产品详情展示
	 * @author wangyadong
	 */
	@RequestMapping(value = "/toActivitProductDetail")
	public String toActivitProductDetail(
			@RequestParam(value = "productId", defaultValue = "", required = false) String productId
			,HttpServletRequest request) {
		request.setAttribute("productId", productId);
			 
		return "jsp/disactivity/toActivitProductDetail";
	}
 
	
	@Autowired
	private DistributionInviteService distributionInviteService;
 

	
	/**
	 * 导出excel表格
	 * @author wangyadong
	 */
	@RequestMapping(value = "/toExportEccelProduct")
	public String toExportEccelProduct(
			@RequestParam(value = "productId", defaultValue = "", required = false) String productId
			,HttpServletRequest request,
			@RequestParam(value = "mobileNo", defaultValue = "", required = false) String mobileNo,
			@RequestParam(value = "name", defaultValue = "", required = false) String name) {
		request.setAttribute("productId", productId);
		
		
			 
		return "";
	}
	
	
	
	/**
	 * 分销产品详情展示
	 * @author wangyadong
	 */
	@RequestMapping(value = "/activitProductDetail")
	@ResponseBody
	public Object activitProductDetail(
			@RequestParam(value = "productId", defaultValue = "", required = false) String productId,
			@RequestParam(value = "mobileNo", defaultValue = "", required = false) String mobileNo,
			@RequestParam(value = "name", defaultValue = "", required = false) String name,
			@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo
			) {
		//根据产品id查询对应的邀请人的信息和对应的被邀请人的投标信息
		Pagination<CommitProfitVO> disActivityPaging = null;
		Map<String, Object> customParams = new HashMap<String, Object>();
			if(productId!=null&&!"".equals(productId)) 
				customParams.put("productId", productId);
			if(mobileNo!=null&&!"".equals(mobileNo)) 
				customParams.put("mobileNo", mobileNo);
			if(name!=null&&!"".equals(name.trim())) 
				customParams.put("custName", name);
			
		 
			disActivityPaging =  disActivityRulesService.getDisActivityRulesByProductIds(pageNo, pageSize, customParams);
		return disActivityPaging;
	}
	
	
	
	

	/**
	 * 跳转至创建产品列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "to_dis_activity_list")
	public String to_dis_activity_list(HttpServletRequest request) {
		List<DisActivityVO> disProductList = disActivityService.getAllDisActivityProducts();
		request.setAttribute("disProductList", disProductList);
		request.setAttribute("disActivityState", DisActivityEnums.DisActivityStateEnum.values());
		return "jsp/disactivity/to_dis_activity_list";
	}

	/**
	 * 跳转至修改产品页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "to_edit_activity")
	public String to_update_activity(HttpServletRequest req, Long disId) {
		DisActivityVO disActivity = disActivityService.getDisActivityVoById(disId);
		disActivity.setRuleStartDateStr(DateUtil.getDateLong(disActivity.getRuleStartDate()));
		disActivity.setRuleEndDateStr(DateUtil.getDateLong(disActivity.getRuleEndDate()));
		List<DisActivityRulesExt> rulesList = disActivityRulesService.getDisActivityRulesByDisId(disId);

		StringBuffer lendProductIds = new StringBuffer();
		StringBuffer commiPaidNodes = new StringBuffer();
		for (int i = 0; i < rulesList.size(); i++) {
			lendProductIds.append(rulesList.get(i).getLendProductId()).append(",");
			commiPaidNodes.append(rulesList.get(i).getCommiPaidNode()).append(",");
		}

		req.setAttribute("disActivity", disActivity);
		req.setAttribute("rulesList", rulesList);
		req.setAttribute("commiPaidNodes", commiPaidNodes.substring(0, commiPaidNodes.length() - 1));
		req.setAttribute("lendProductIds", lendProductIds.substring(0, lendProductIds.length() - 1));
		return "jsp/disactivity/to_edit_activity";
	}

	/**
	 * 跳转至创建财富券页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "add")
	public String add() {
		return "jsp/disactivity/to_add_dis_activity";
	}

	@RequestMapping(value = "disActivityList")
	@ResponseBody
	public Object disActivityList(@ModelAttribute DisActivityVO disActivityVO,
			@RequestParam(value = "ruleStartDateStr", defaultValue = "", required = false) String startDate,
			@RequestParam(value = "ruleEndDateStr", defaultValue = "", required = false) String endDate,
			@RequestParam(value = "rows", defaultValue = "10") int pageSize, @RequestParam(value = "page", defaultValue = "1") int pageNo) {
		// 封装参数
		TimeInterval timeInterval = new TimeInterval(// 日期间隔
				"".equals(startDate) ? null : DateUtil.parseStrToDate(startDate, "yyyy-MM-dd"), "".equals(endDate) ? null : DateUtil.parseStrToDate(
						endDate, "yyyy-MM-dd"));

		Map<String, Object> customParams = new HashMap<String, Object>();
		if(StringUtils.isNull(disActivityVO.getState())){
			disActivityVO.setState(null);
		}
		customParams.put("timeInterval", timeInterval);

		Pagination<DisActivityVO> disActivityPaging = disActivityService.getDisActivityListPaging(pageNo, pageSize, disActivityVO, customParams);
		return disActivityPaging;
	}

	/**
	 * 保存产品
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public String save(String me, DisActivity disActivity, String lendProductIds, String commiPaidNodes, String firstRates, String secondRates,
			String thirdRates, String salesPointStart, String salesPointEnd) {

		if (disActivity.getRuleStartDate().getTime() > disActivity.getRuleEndDate().getTime()) {
			return JsonView.JsonViewFactory.create().success(false).info("规则开始时间不能大于结束时间").toJson();
		}

		String[] arrayLendProductIds = lendProductIds.split(",");
		String[] arrayCommiPaidNodes = commiPaidNodes.split(",");
		String[] arrayFirstRates = firstRates.split(",");
		String[] arraySecondRates = secondRates.split(",");
		String[] arrayThirdRates = thirdRates.split(",");

		List<DisActivityRules> rulesList = new ArrayList<DisActivityRules>();

		DisActivityRules rules = null;
		for (int i = 0; i < arrayLendProductIds.length; i++) {
			rules = new DisActivityRules();
			if (StringUtils.isNull(arrayLendProductIds[i])) {
				return JsonView.JsonViewFactory.create().success(false).info("出借产品不能为空").toJson();
			}
			if (StringUtils.isNull(arrayCommiPaidNodes[i])) {
				return JsonView.JsonViewFactory.create().success(false).info("佣金发放节点不能为空").toJson();
			}
			if (StringUtils.isNull(arrayFirstRates[i])) {
				return JsonView.JsonViewFactory.create().success(false).info("一级佣金值不能为空").toJson();
			}
			if (StringUtils.isNull(arraySecondRates[i])) {
				return JsonView.JsonViewFactory.create().success(false).info("二级佣金值不能为空").toJson();
			}
			if (StringUtils.isNull(arrayThirdRates[i])) {
				return JsonView.JsonViewFactory.create().success(false).info("三级佣金值不能为空").toJson();
			}

			for (int j = i + 1; j < arrayLendProductIds.length; j++) {
				// 存在同一个出借产品
				if (arrayLendProductIds[i].equals(arrayLendProductIds[j])) {
					return JsonView.JsonViewFactory.create().success(false).info("同一时间不能存在两个出借产品规则").toJson();
				}
			}
			List<String> targetUser = new ArrayList<>();
			if(!disActivity.getTargetUser().equals(DisActivityEnums.DisAcivityUserTypeEnum.ALL_USER.getValue())){
				targetUser.add(DisActivityEnums.DisAcivityUserTypeEnum.ALL_USER.getValue());
				targetUser.add(disActivity.getTargetUser());
			}
			List<DisActivityVO> vos = disActivityService.getDisActByStateAndLendProId(Long.valueOf(arrayLendProductIds[i]),
					DisActivityStateEnum.STATE_PUBLISH,targetUser);
			for (DisActivityVO vo : vos) {
				// 时间重叠
				if (DateUtil.dateIn(disActivity.getRuleStartDate(), vo.getRuleStartDate(), vo.getRuleEndDate())
						|| DateUtil.dateIn(disActivity.getRuleEndDate(), vo.getRuleStartDate(), vo.getRuleEndDate())) {
					return JsonView.JsonViewFactory
							.create()
							.success(false)
							.info(vo.getDisProductName() + "在" + DateUtil.getDateLong(vo.getRuleStartDate()) + "到"
									+ DateUtil.getDateLong(vo.getRuleEndDate()) + "内已有发布记录").toJson();
				}
			}

			rules.setLendProductId(Long.valueOf(arrayLendProductIds[i]));
			rules.setCommiPaidNode(arrayCommiPaidNodes[i]);
			rules.setFirstRate(new BigDecimal(arrayFirstRates[i]));
			rules.setSecondRate(new BigDecimal(arraySecondRates[i]));
			rules.setThirdRate(new BigDecimal(arrayThirdRates[i]));
			rules.setSalesPointStart(new BigDecimal(salesPointStart));
			rules.setSalesPointEnd(new BigDecimal(salesPointEnd));
			rules.setCommiRatioType(DarCommiRatioTypeEnum.CYCLE_INTEREST.getValue());
			rulesList.add(rules);
		}

		disActivity.setState(DisActivityStateEnum.STATE_DISPUBLISH.getValue());
		disActivity.setCreateDate(new Date());

		if (me.equals("add")) {
			disActivityService.addDisActivityInfo(disActivity, rulesList);
		} else {
			disActivityService.updateDisActivityInfo(disActivity, rulesList);
		}

		return JsonView.JsonViewFactory.create().success(true).toJson();

	}

	/**
	 * 加载下拉列表。
	 * 
	 * @param constantTypeCode
	 *            常量类型
	 * @param parentConstant
	 *            常量父编号
	 * @param selectedDisplay
	 *            默认显示值
	 */
	@RequestMapping(value = "/loadLendProduct")
	@ResponseBody
	public Object loadLendProduct(@RequestParam(value = "selectedDisplay", required = false) String selectedDisplay) {
		if ("selected".equals(selectedDisplay)) {
			selectedDisplay = "请选择";
		} else {
			selectedDisplay = "全部";
		}
		List<LendProduct> list = lendProductService.findLendProductByPublishState(LendProductPublishStateEnum.SELLING);
		List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> map;
		map = new HashMap<Object, Object>();
		map.put("CONSTANTNAME", selectedDisplay);
		map.put("CONSTANTVALUE", "");
		map.put("CONSTANTID", "");
		map.put("selected", true);
		maps.add(map);
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				LendProduct pub = list.get(i);
				map = new HashMap<Object, Object>();
				map.put("CONSTANTNAME", pub.getProductName());
				map.put("CONSTANTVALUE", pub.getLendProductId());
				map.put("CONSTANTID", pub.getLendProductId());
				maps.add(map);
			}
		}
		return maps;
	}

	/**
	 * 分销产品详情
	 * 
	 * @param request
	 * @param disId
	 *            分销活动表ID
	 * @return
	 */
	@RequestMapping(value = "disActivityDetail")
	public String detail(HttpServletRequest request, Long disId) {
		// 根据分销活动ID，加载一条分销数据
		DisActivity disActivity = disActivityService.getDisActivityById(disId);
		// 根据分销活动ID，查询规则列表
		List<DisActivityRulesExt> disActivityRuleList = disActivityRulesService.getDisActivityRulesByDisId(disId);
		// 从规则表，获取分销奖励限制
		if (null != disActivityRuleList && disActivityRuleList.size() > 0) {
			DisActivityRulesExt activityRulesExt = disActivityRuleList.get(0);
			if (null != activityRulesExt) {
				request.setAttribute("salesPointStart", activityRulesExt.getSalesPointStart());
				request.setAttribute("salesPointEnd", activityRulesExt.getSalesPointEnd());
			}
		}
		request.setAttribute("disActivity", disActivity);
		request.setAttribute("disActivityRuleList", disActivityRuleList);
		Map<String,String> targetUserMap = new HashMap<>();
		for(DisAcivityUserTypeEnum e : DisAcivityUserTypeEnum.values()){
			targetUserMap.put(e.getValue().toString(), e.getDesc());
		}
		request.setAttribute("targetUserMap", targetUserMap);
		return "jsp/disactivity/to_dis_activity_detail";
	}
	
	@RequestMapping(value = "dis_customer_list")
	public String dis_customer_list(HttpServletRequest request) {
		
		return "jsp/disactivity/dis_customer_list";
	}
	
	@RequestMapping(value = "/disCustomerList")
    @ResponseBody
    public Object disCustomerList(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "status", required=false) String status,
			@RequestParam(value = "userName", required=false) String userName,
			@RequestParam(value = "userLogin", required=false) String userLogin,
			@RequestParam(value = "userMobile", required=false) String userMobile,
			@RequestParam(value = "userIdcard", required=false) String userIdcard) throws Exception {

		// 填充参数
        Map params = new HashMap();
        if(!StringUtils.isNull(status)&&!status.equals("-1")){
        	params.put("status", status);
        }
        if(!StringUtils.isNull(userName)){
        	params.put("realName", userName);
        }
        if(!StringUtils.isNull(userLogin)){
        	params.put("loginName", userLogin);
        }
        if(!StringUtils.isNull(userMobile)){
        	params.put("mobile", userMobile);
        }
        if(!StringUtils.isNull(userIdcard)){
        	params.put("idCard", userIdcard);
        }
        return distributionInviteService.findAllDistributionCustomerByPage(pageNo, pageSize, params);
    }
	
	@RequestMapping(value = "disCustomerDetail")
	public String disCustomerDetail(HttpServletRequest request,String userId) {
		request.setAttribute("userId", userId);
		DistributorVO distributorVO=distributionInviteService.findDistributionCustomerDetailByUserId(Long.parseLong(userId));
		request.setAttribute("distributorVO", distributorVO);
		return "jsp/disactivity/dis_customer_detail";
	}
	
	@RequestMapping(value = "/disCustomerDetailList")
    @ResponseBody
    public Object disCustomerDetailList(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "disLevel", required=false) String disLevel,
			@RequestParam(value = "userName", required=false) String userName,
			@RequestParam(value = "userLogin", required=false) String userLogin,
			@RequestParam(value = "userMobile", required=false) String userMobile,
			@RequestParam(value = "startTime", required=false) String startTime,
			@RequestParam(value = "endTime", required=false) String endTime,
			@RequestParam(value = "userId", required=false) String userId) throws Exception {

		// 填充参数
        Map params = new HashMap();
        if(!StringUtils.isNull(disLevel)&&!disLevel.equals("0")){
        	params.put("disLevel", disLevel);
        }
        if(!StringUtils.isNull(userName)){
        	params.put("realName", userName);
        }
        if(!StringUtils.isNull(userLogin)){
        	params.put("loginName", userLogin);
        }
        if(!StringUtils.isNull(userMobile)){
        	params.put("mobile", userMobile);
        }
        if(!StringUtils.isNull(startTime)){
        	params.put("startTime", startTime);
        }
        if(!StringUtils.isNull(endTime)){
        	params.put("endTime", endTime);
        }
        if(!StringUtils.isNull(userId)){
        	params.put("userId", userId);
        }
        return distributionInviteService.findUserCustomersByPage(pageNo, pageSize, params);
    }
	
	/**
	 * 失效分销活动
	 * @param disId
	 * */
	@RequestMapping(value="/disabledDisAct")
	@ResponseBody
	public Object disabledDisAct(HttpServletRequest request, Long disId){
		try {
			// 根据分销活动ID，加载一条分销数据
			DisActivity disActivity = disActivityService.getDisActivityById(disId);
			disActivity.setState(DisActivityEnums.DisActivityStateEnum.STATE_NO.getValue());
			disActivityService.updateDisActivityState(disActivity);
			return "success";
		} catch (Exception e) {
			logger.error("失效失败，失败原因：",e);
//			return e;
			return null;
		}
	}

	/**
	 * 发布分销活动
	 * 
	 * @param disId
	 * */
	@RequestMapping(value = "/publishDisAct")
	@ResponseBody
	public Object publishDisAct(HttpServletRequest request, Long disId) {
		try {
			// 根据分销活动ID，加载一条分销数据
			DisActivity disActivity = disActivityService.getDisActivityById(disId);

			List<DisActivityRules> countList = disActivityRulesService.checkLendProIdCountByDisId(disId);
			if (0 != countList.size()) {
				return JsonView.JsonViewFactory.create().success(false).info("发布的活动存在重复的出借产品").toJson();
			}
			List<String> targetUser = new ArrayList<>();
			if(!disActivity.getTargetUser().equals(DisActivityEnums.DisAcivityUserTypeEnum.ALL_USER.getValue())){
				targetUser.add(DisActivityEnums.DisAcivityUserTypeEnum.ALL_USER.getValue());
				targetUser.add(disActivity.getTargetUser());
			}
			List<DisActivityRulesExt> rulesList = disActivityRulesService.getDisActivityRulesByDisId(disId);

			for (int j = 0; j < rulesList.size(); j++) {
				List<DisActivityVO> vos = disActivityService.getDisActByStateAndLendProId(rulesList.get(j).getLendProductId(),
						DisActivityStateEnum.STATE_PUBLISH,targetUser);
				for (DisActivityVO vo : vos) {
					// 时间重叠
					if (DateUtil.dateIn(disActivity.getRuleStartDate(), vo.getRuleStartDate(), vo.getRuleEndDate())
							|| DateUtil.dateIn(disActivity.getRuleEndDate(), vo.getRuleStartDate(), vo.getRuleEndDate())) {
						return JsonView.JsonViewFactory
								.create()
								.success(false)
								.info(vo.getDisProductName() + "在" + DateUtil.getDateLong(vo.getRuleStartDate()) + "到"
										+ DateUtil.getDateLong(vo.getRuleEndDate()) + "内已有发布记录").toJson();
					}
				}
			}

			disActivity.setState(DisActivityEnums.DisActivityStateEnum.STATE_PUBLISH.getValue());
			disActivityService.updateDisActivityState(disActivity);
			return JsonView.JsonViewFactory.create().success(true).toJson();
		} catch (Exception e) {
			logger.error("发布失败，失败原因：", e);
			return JsonView.JsonViewFactory.create().success(false).info("系统异常").toJson();
		}
	}
	
}
