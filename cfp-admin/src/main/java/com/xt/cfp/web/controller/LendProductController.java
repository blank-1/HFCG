package com.xt.cfp.web.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xt.cfp.core.constants.LendProductPublishStateEnum;
import com.xt.cfp.core.constants.LendProductTimeLimitType;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.constants.LendProductPublishStateEnum;
import com.xt.cfp.core.constants.LendProductTimeLimitType;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LPPublishChannelDetail;
import com.xt.cfp.core.pojo.LendLoanBinding;
import com.xt.cfp.core.pojo.LendProduct;
import com.xt.cfp.core.pojo.LendProductFeesItem;
import com.xt.cfp.core.pojo.LendProductLadderDiscount;
import com.xt.cfp.core.pojo.LendProductLadderDiscountFees;
import com.xt.cfp.core.pojo.LendProductPublish;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.pojo.ext.LendProductVO;
import com.xt.cfp.core.service.ConstantDefineCached;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.FeesItemService;
import com.xt.cfp.core.service.LendLoanBindingService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.service.ProductFeesCached;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.JsonUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;

@Controller
@RequestMapping("/jsp/product/lend")
public class LendProductController extends BaseController {
	@Autowired
	private LendProductService lendProductService;
	@Autowired
	private ConstantDefineCached constantDefineCached;
	@Autowired
	private LendLoanBindingService lendLoanBindingService;
	@Autowired
	private ProductFeesCached productFeesCached;
	@Autowired
	private ConstantDefineService constantDefineService;
	@Autowired
	private FeesItemService feesItemService;
	@Autowired
	private LoanProductService loanProductService ;

	@RequestMapping(value = "/to_lend_product_list")
	public ModelAndView lendProductList() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsp/product/lend/lend_product_list");
		return modelAndView;
	}

	@RequestMapping(value = "/lendProductPage", method = RequestMethod.POST)
	@ResponseBody
	public Object lendProductPage(HttpServletRequest request, @ModelAttribute(value = "lendProductVO") LendProductVO lendProductVO) throws Exception {
		int pageSize = Integer.parseInt(request.getParameter("rows"));
		int pageNo = Integer.parseInt(request.getParameter("page"));

		if (lendProductVO.getSearchTimeLimit() != null && lendProductVO.getSearchTimeLimit().trim().length() > 0) {
			String serchTimeLimit = lendProductVO.getSearchTimeLimit();
			if (serchTimeLimit.indexOf("month") > 0) {
				lendProductVO.setTimeLimit(Integer.parseInt(serchTimeLimit.replaceAll("month", "")));
				lendProductVO.setTimeLimitType(LendProductTimeLimitType.TIMELIMITTYPE_MONTH.getValue());
			} else if (serchTimeLimit.indexOf("day") > 0) {
				lendProductVO.setTimeLimit(Integer.parseInt(serchTimeLimit.replaceAll("day", "")));
				lendProductVO.setTimeLimitType(LendProductTimeLimitType.TIMELIMITTYPE_DAY.getValue());
			}
		} else {
			lendProductVO.setSearchTimeLimit(null);
		}

		if (lendProductVO.getProductName() != null) {
			lendProductVO.setProductName(lendProductVO.getProductName().trim());
			if (lendProductVO.getProductName() != null && lendProductVO.getProductName().length() == 0) {
				lendProductVO.setProductName(null);
			}
		}
		Pagination<LendProductVO> lendProducts = lendProductService.findAllByPage(pageNo, pageSize, lendProductVO);
		return lendProducts;
	}

	@RequestMapping(value = "/toSelLoanProduct")
	public ModelAndView toSelLoanProduct(@RequestParam("productType") char productType) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("productType", productType);
		return modelAndView;
	}

	/**
	 * 查看出借产品详情
	 *
	 * @param lendProductId
	 * @return
	 */
	@RequestMapping(value = "/toViewLendProduct")
	public ModelAndView toViewLendProduct(@RequestParam("lendProductId") long lendProductId) {
		ModelAndView modelAndView = new ModelAndView();
		LendProduct lendProduct = lendProductService.findById(lendProductId);
		List<ConstantDefine> lendProductAndFeesList = lendProductService.findLendProductAndFees(lendProductId);
		List<ConstantDefine> itemTypeList = constantDefineCached.getByTypeCode("itemType");
		List<ConstantDefine> lendItemTypeList = new ArrayList<ConstantDefine>();
		for (ConstantDefine constantDefine : itemTypeList) {
			if (String.valueOf(constantDefine.getParentConstant()).equals(String.valueOf(FeesItem.ITEMKIND_LEND))) {
				lendItemTypeList.add(constantDefine);
			}
		}
		List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.fullLoanAndFeesByLendProductId(lendProductId);
		List<LendProductLadderDiscountFees> lendProductLadderDiscountFees = lendProductService.findByLendProductLDFId(lendProductId);
		modelAndView.addObject("lendProduct", lendProduct);
		modelAndView.addObject("lendLoanBindings", lendLoanBindings);
		modelAndView.addObject("itemTypeList", lendItemTypeList);
		modelAndView.addObject("lendProductAndFeesList", lendProductAndFeesList);
		modelAndView.addObject("lendProductLadderDiscountFees", lendProductLadderDiscountFees);
		modelAndView.setViewName("jsp/product/lend/lend_product_info");
		return modelAndView;
	}

	@RequestMapping(value = "/publishList")
	public ModelAndView publishList(@RequestParam("lendProductId") long lendProductId) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("lendProductId", lendProductId);
		modelAndView.setViewName("jsp/product/lend/publish_list");
		return modelAndView;
	}

	@RequestMapping(value = "/toPublish")
	public ModelAndView toPublish(@RequestParam("lendProductId") long lendProductId) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("lendProduct", lendProductService.findById(lendProductId));
		return modelAndView;
	}

	@RequestMapping(value = "/saveProductPublish")
	@ResponseBody
	public Object saveProductPublish(HttpServletRequest request, @RequestParam("channelInfo") String channelInfo,
			@RequestParam("channelBalanceType") char channelBalanceType, @RequestParam(value="recommend", required = false) String recommend,
			@RequestParam(value="publishTitle_sx", required = false) String publishTitle_sx) {
		LendProductPublish lendProductPublish = new LendProductPublish();
		lendProductPublish.setRecommend(recommend);
		lendProductPublish.setLendProductId(Long.parseLong(request.getParameter("lendProductId")));
		char publishBalanceType = request.getParameter("publishBalanceType").charAt(0);
		lendProductPublish.setPublishBalanceType(publishBalanceType);
		if (!StringUtils.isNull(request.getParameter("publishBalance"))) {
			BigDecimal publishBalance = new BigDecimal(request.getParameter("publishBalance"));
			lendProductPublish.setPublishBalance(publishBalance);
		}
		if (!StringUtils.isNull(request.getParameter("startDate")) && !StringUtils.isNull(request.getParameter("endDate"))) {
			lendProductPublish.setStartDate(DateUtil.parseStrToDate(request.getParameter("startDate"), "yyyy-MM-dd"));
			lendProductPublish.setEndDate(DateUtil.parseStrToDate(request.getParameter("endDate"), "yyyy-MM-dd"));
		}
		LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());
		lendProductPublish.setPublishTime(new Date());
		lendProductPublish.setPublishCode(StringUtils.int2Str(
				lendProductService.getMaxPublishCodeByLendProductId(lendProduct.getLendProductId()) + 1, 4));
		if(lendProduct.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()) && !StringUtils.isNull(publishTitle_sx)){
			lendProductPublish.setPublishName(publishTitle_sx);
		}else{
			lendProductPublish.setPublishName(lendProduct.getProductName() + lendProductPublish.getPublishCode());
		}

		Date now = new Date();
		try {
			now = DateUtils.parseDate(DateFormatUtils.format(now, "yyyy-MM-dd"), new String[] { "yyyy-MM-dd" });
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (lendProductPublish.getStartDate() != null && lendProductPublish.getEndDate() != null) {
			if (now.compareTo(lendProductPublish.getStartDate()) >= 0 && now.compareTo(lendProductPublish.getEndDate()) <= 0) {
				lendProductPublish.setPublishState(LendProductPublishStateEnum.SELLING.value2Char());
			} else if (now.compareTo(lendProductPublish.getStartDate()) > 0 && now.compareTo(lendProductPublish.getEndDate()) > 0) {
				lendProductPublish.setPublishState(LendProductPublishStateEnum.TIMEOUT.value2Char());
			} else {
				lendProductPublish.setPublishState(LendProductPublishStateEnum.WAITING.value2Char());
			}
		} else {
			lendProductPublish.setPublishState(LendProductPublishStateEnum.SELLING.value2Char());
		}

		List<LPPublishChannelDetail> lpPublishChannelDetails = new ArrayList<LPPublishChannelDetail>();
		List<JSONObject> channelJsonObjects = JsonUtil.getListFromPageJSON(channelInfo);
		for (JSONObject jsonObject : channelJsonObjects) {
			LPPublishChannelDetail channelDetail = new LPPublishChannelDetail();
			channelDetail.setChannel(jsonObject.getString("channel").charAt(0));

			channelDetail.setPublishBalanceType(request.getParameter("channelBalanceType").charAt(0));
			if (channelDetail.getPublishBalanceType() == LPPublishChannelDetail.CHANNELBALANCETYPE_BALANCE) {
				channelDetail.setPublishBalance(jsonObject.getBigDecimal("theValue"));
			} else {
				channelDetail.setPublishBalance(jsonObject.getBigDecimal("theValue").divide(new BigDecimal("100"), 18, BigDecimal.ROUND_HALF_UP));
			}
			lpPublishChannelDetails.add(channelDetail);
		}

		lendProductService.addProductPublish(lendProductPublish, lpPublishChannelDetails);
		return "success";
	}

	@RequestMapping(value = "/publishForPage")
	@ResponseBody
	public Object publishForPage(@RequestParam("lendProductId") long lendProductId, HttpServletRequest request) {
		int pageSize = Integer.parseInt(request.getParameter("rows"));
		int pageNo = Integer.parseInt(request.getParameter("page"));
		Map map = new HashMap();
		map.put("lendProductId", lendProductId);
		Pagination<LendProductPublish> publishByPage = lendProductService.findAllPublishByPage(pageNo, pageSize, map);
		return publishByPage;
	}

	@RequestMapping(value = "/toAddLadder")
	public ModelAndView toAddLadder(HttpServletRequest req, @RequestParam("startsAt") BigDecimal startsAt, @RequestParam("upAt") BigDecimal upAt)
			throws UnsupportedEncodingException {
		String feesItems = new String(req.getParameter("feesItems").getBytes("ISO-8859-1"), "utf-8");// 乱码转码
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("feesItems", feesItems);
		modelAndView.addObject("startsAt", startsAt);
		modelAndView.addObject("upAt", upAt);
		modelAndView.setViewName("jsp/product/lend/ladder_add");
		return modelAndView;
	}

	@RequestMapping(value = "/addLendProduct")
	public ModelAndView addLendProduct() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("add", true);
		modelAndView.addObject("lendProductId", -1);
		List<ConstantDefine> itemTypeList = constantDefineCached.getByTypeCode("itemType");
		List<Map> itemTypes = new ArrayList<Map>();
		for (ConstantDefine constantDefine : itemTypeList) {
			if (String.valueOf(constantDefine.getParentConstant()).equals(String.valueOf(FeesItem.ITEMKIND_LEND))) {
				Map map = new HashMap();
				map.put("constantValue", constantDefine.getConstantValue());
				map.put("constantName", constantDefine.getConstantName());
				List<ConstantDefine> childDefineList = constantDefineService.findByTypeCodeAndParentConstant("itemChildType",
						constantDefine.getConstantDefineId());
				List<Map> childs = new ArrayList<Map>();
				for (ConstantDefine child : childDefineList) {
					Map childMap = new HashMap();
					childMap.put("constantValue", child.getConstantValue());
					childMap.put("constantName", child.getConstantName());
					childs.add(childMap);
				}
				map.put("childItemTypes", childs);
				itemTypes.add(map);
			}
		}
		modelAndView.addObject("itemTypes", itemTypes);
		modelAndView.setViewName("jsp/product/lend/lend_product_add");
		return modelAndView;
	}

	@RequestMapping(value = "/saveLendProduct", method = RequestMethod.POST)
	@ResponseBody
	public Object saveLendProduct(@ModelAttribute("lendProduct") LendProduct lendProduct, @ModelAttribute("addFlag") Boolean addFlag,
			@RequestParam("loanProducts") String loanProducts,
//								  @RequestParam("discountFees") String discountFees,
								  HttpServletRequest request) {

		List<LendProductFeesItem> lendProductFeesItems = new ArrayList<LendProductFeesItem>();

		if (!StringUtils.isNull(request.getParameter("feeItems"))) {
			String[] feeItemStrs = request.getParameterValues("feeItems");
			for (String str : feeItemStrs) {
				LendProductFeesItem lendProductFeesItem = new LendProductFeesItem();
				lendProductFeesItem.setFeesItemId(Long.valueOf(request.getParameter("feesItems_" + str)));
				lendProductFeesItem.setChargePoint(request.getParameter("chargePoint_" + str).charAt(0));
				lendProductFeesItem.setWorkflowRatio(new BigDecimal(request.getParameter("workflowRatio_" + str)).divide(new BigDecimal("100"), 18,
						BigDecimal.ROUND_CEILING));
				lendProductFeesItems.add(lendProductFeesItem);
			}
		}

		List<LendProductLadderDiscount> ladderDiscounts = new ArrayList<LendProductLadderDiscount>();

		/**
		 * 取消阶段优惠
		Map<String, LendProductLadderDiscount> discountMap = new HashMap<String, LendProductLadderDiscount>();
		if (!StringUtils.isNull(discountFees)) {
			List<JSONObject> discountJsonObjects = JsonUtil.getListFromPageJSON(discountFees);
			for (JSONObject jsonObject : discountJsonObjects) {
				String minMax = jsonObject.getString("minAmount").trim() + "~" + jsonObject.getString("maxAmount").trim();
				if (discountMap.get(minMax) != null) {
					LendProductLadderDiscount lendProductLadderDiscount = discountMap.get(minMax);
					LendProductLadderDiscountFees ladderDiscountFees = new LendProductLadderDiscountFees();
					ladderDiscountFees.setDiscountRate(BigDecimal.ONE.subtract(jsonObject.getBigDecimal("discountRate")).setScale(2,
							BigDecimal.ROUND_CEILING));
					ladderDiscountFees.setFeesItemId(jsonObject.getLong("feesItemId"));
					lendProductLadderDiscount.getLendProductLadderDiscountFeesList().add(ladderDiscountFees);
				} else {
					LendProductLadderDiscount lendProductLadderDiscount = new LendProductLadderDiscount();
					lendProductLadderDiscount.setMinAmount(jsonObject.getBigDecimal("minAmount"));
					lendProductLadderDiscount.setMaxAmount(jsonObject.getBigDecimal("maxAmount"));
					lendProductLadderDiscount.setLendProductLadderDiscountFeesList(new ArrayList<LendProductLadderDiscountFees>());

					LendProductLadderDiscountFees ladderDiscountFees = new LendProductLadderDiscountFees();
					ladderDiscountFees.setDiscountRate(BigDecimal.ONE.subtract(jsonObject.getBigDecimal("discountRate")).setScale(2,
							BigDecimal.ROUND_CEILING));
					ladderDiscountFees.setFeesItemId(jsonObject.getLong("feesItemId"));
					lendProductLadderDiscount.getLendProductLadderDiscountFeesList().add(ladderDiscountFees);

					discountMap.put(minMax, lendProductLadderDiscount);
					ladderDiscounts.add(lendProductLadderDiscount);
				}

			}
		}
		*/
		List<LendLoanBinding> lendLoanBindings = new ArrayList<LendLoanBinding>();
		List<JSONObject> listFromPageJSON = JsonUtil.getListFromPageJSON(loanProducts);
		BigDecimal zero = BigDecimal.ZERO;
		BigDecimal productMinRate = zero;
		BigDecimal productMaxRate = zero;
		for (JSONObject jsonObject : listFromPageJSON) {
			LendLoanBinding lendLoanBinding = new LendLoanBinding();
			if (jsonObject.getLong("feesItemId") != null && !jsonObject.getLong("feesItemId").toString().equals("")) {
				lendLoanBinding.setFeesItemId(jsonObject.getLong("feesItemId"));
			}
			lendLoanBinding.setLoanProductId(jsonObject.getLong("loanProductId"));
			lendLoanBinding.setLoanRatio(jsonObject.getBigDecimal("ratio"));
			lendLoanBindings.add(lendLoanBinding);
			LoanProduct loanProduct = loanProductService.findById(lendLoanBinding.getLoanProductId());
			if(productMinRate.compareTo(zero) == 0){
				productMinRate = loanProduct.getAnnualRate();
			}
			if(productMaxRate.compareTo(zero) == 0){
				productMaxRate = loanProduct.getAnnualRate();
			}
			if(productMinRate.compareTo(loanProduct.getAnnualRate()) > 0 ){
				productMinRate = loanProduct.getAnnualRate();
			}
			if(productMaxRate.compareTo(loanProduct.getAnnualRate()) < 0 ){
				productMaxRate = loanProduct.getAnnualRate();
			}
			
		}
		String result = "" ;
		if(lendProduct.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())){
			if(lendProduct.getProfitRate().compareTo(productMinRate) != 0){
				result = "收益范围有误，请输入"+BigDecimalUtil.down(productMinRate, 2)+"% - "+BigDecimalUtil.down(productMaxRate, 2)+"% 的年化收益范围";
				return result;
			}
			if(lendProduct.getProfitRateMax().compareTo(productMaxRate) != 0){
				result = "收益范围有误，请输入"+BigDecimalUtil.down(productMinRate, 2)+"% - "+BigDecimalUtil.down(productMaxRate, 2)+"% 的年化收益范围";
				return result;
			}
		}
		if (addFlag) {
			lendProduct.setProductCode(StringUtils.getDateTimeRadomStr(3));
			lendProduct.setCreateTime(new Date());
			lendProduct.setLastMdfTime(new Date());
			lendProduct.setProductState(LendProduct.PRODUCTSTATE_VALID);
			lendProductService.addLendProduct(lendProduct, lendProductFeesItems, ladderDiscounts, lendLoanBindings);
		} else {
			lendProduct.setLastMdfTime(new Date());
			lendProductService.updateLendProduct(lendProduct, lendProductFeesItems, ladderDiscounts, lendLoanBindings);
		}
		return "success";
	}

	/**
	 * 出借产品修改回显时显示
	 *
	 * @param lendProductId
	 * @return
	 */
	@RequestMapping(value = "/editLendProduct")
	@ResponseBody
	public ModelAndView editLendProduct(@RequestParam("lendProductId") long lendProductId) {
		ModelAndView model = new ModelAndView();
		LendProduct lendProduct = lendProductService.findById(lendProductId);
		productFeesCached.resetLendProductFeesItem();// 防止增加出借产品后，lendFeesItemMap不是最新的的bug
		List<LendProductFeesItem> lendProductFeesItem = productFeesCached.getFeesItemsByLendProductId(lendProductId);
		List<FeesItem> FeesItemList = new ArrayList<FeesItem>();
		if (lendProductFeesItem != null) {
			for (LendProductFeesItem lendProductFees : lendProductFeesItem) {
				FeesItem feesItemRadices = lendProductFees.getFeesItem();
				if (feesItemRadices.getRadicesType() != 0) {
					String constantValue = String.valueOf(feesItemRadices.getRadicesType());
					ConstantDefine constant = new ConstantDefine();
					constant.setConstantTypeCode("radicesType");
					constant.setConstantValue(constantValue);
					ConstantDefine constantDefine = constantDefineService.findConstantByTypeCodeAndValue(constant);
					// 把基数名称和费用比例封装到基数显示名中，便于修改页面显示收费规则
					feesItemRadices.setRadiceName(constantDefine.getConstantName() + "*" + feesItemRadices.getFeesRate() + "%");
				} else {
					// 把基数名称和费用比例封装到基数显示名中，便于修改页面显示收费规则
					feesItemRadices.setRadiceName(feesItemRadices.getRadiceName() + "*" + String.valueOf(feesItemRadices.getFeesRate()) + "%");
				}
				FeesItemList.add(feesItemRadices);
			}
		}
		// 设置前台费用费用详情回显操作
		List<ConstantDefine> itemTypeList = constantDefineCached.getByTypeCode("itemType");
		List<Map> itemTypes = new ArrayList<Map>();
		for (ConstantDefine constantDefine : itemTypeList) {
			if (String.valueOf(constantDefine.getParentConstant()).equals(String.valueOf(FeesItem.ITEMKIND_LEND))) {// 1，借款
																													// 2，出借===========目前都是
				Map map = new HashMap();
				map.put("constantValue", constantDefine.getConstantValue());
				map.put("constantName", constantDefine.getConstantName());
				List<ConstantDefine> childDefineList = constantDefineService.findByTypeCodeAndParentConstant("itemChildType",
						constantDefine.getConstantDefineId());
				List<Map> childs = new ArrayList<Map>();
				for (ConstantDefine child : childDefineList) {
					if (child.getParentConstant() == constantDefine.getConstantDefineId()) {
						Map childMap = new HashMap();
						childMap.put("constantValue", child.getConstantValue());
						childMap.put("constantName", child.getConstantName());
						childs.add(childMap);
					}
				}
				map.put("childItemTypes", childs);
				itemTypes.add(map);
			}
		}

		model.addObject("lendProductFeesItem", lendProductFeesItem);
		model.addObject("FeesItemList", FeesItemList);
		model.setViewName("/jsp/product/lend/lend_product_edit");
		model.addObject("itemTypes", itemTypes);
		model.addObject("add", false);
		model.addObject("lendProduct", lendProduct);
		return model;
	}

	/**
	 * 获取产品的梯度优惠
	 *
	 * @param lendProductId
	 * @return
	 */
	@RequestMapping(value = "/findLendProductLadderDiscountFees")
	@ResponseBody
	public Object findLendProductLadderDiscountFees(@RequestParam("lendProductId") long lendProductId) {
		List<LendProductLadderDiscountFees> lendProductLadderDiscountFees = lendProductService.findByLendProductLDFId(lendProductId);
		return lendProductLadderDiscountFees;
	}

	/**
	 * 获取产品的适用债权
	 *
	 * @param lendProductId
	 * @return
	 */
	@RequestMapping(value = "/lendLoanBindings")
	@ResponseBody
	public Object lendLoanBindings(@RequestParam("lendProductId") long lendProductId) {
		List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.fullLoanAndFeesByLendProductId(lendProductId);
		return lendLoanBindings;
	}

	/**
	 * 判断是否可以修改出借产品
	 *
	 * @param lendProductId
	 * @return
	 */
	@RequestMapping(value = "findLendProductPublishState")
	@ResponseBody
	public Object findLendProductPublishState(@RequestParam("lendProductId") long lendProductId) {
		// todo 实现查找出借产品下是否含有出借订单
		List lendOrderList = new ArrayList();
		if (lendOrderList.size() > 0) {// 出借产品有订单占用
			return "error";
		} else {
			List<LendProductPublish> list = lendProductService.findLendProductPublishsByProductId(lendProductId);
			if (list.size() > 0) {// 只要出借产品为发布
				return "error";
			} else {
				return "success";
			}
		}
	}

	/**
	 * 产品的禁用
	 *
	 * @return
	 */
	@RequestMapping(value = "disableLendProduct")
	@ResponseBody
	public Object disableLendProduct(@RequestParam("lendProductId") long lendProductId) {
		LendProduct lendProduct = new LendProduct();
		lendProduct.setProductState(String.valueOf(LoanProduct.PUBLISHSTATE_INVALID));// 无效
		lendProduct.setLendProductId(lendProductId);
		lendProductService.updateProductState(lendProduct);
		return "success";
	}

	/**
	 * 产品的启用
	 *
	 * @return
	 */
	@RequestMapping(value = "enableLendProduct")
	@ResponseBody
	public void enableLendProduct(@RequestParam("lendProductId") long lendProductId) {
		LendProduct lendProduct = new LendProduct();
		lendProduct.setProductState(String.valueOf(LoanProduct.PUBLISHSTATE_VALID));// 有效
		lendProduct.setLendProductId(lendProductId);
		lendProductService.updateProductState(lendProduct);
	}

	/**
	 * 处理查询期限下拉框的数据呈现
	 *
	 * @return Object
	 */
	@RequestMapping(value = "/searchTimeLimit")
	@ResponseBody
	public JSONArray searchTimeLimit() {
		JSONArray array = new JSONArray();
		LendProduct lendProduct = new LendProduct();
		lendProduct.setTimeLimitType(LendProductTimeLimitType.TIMELIMITTYPE_MONTH.getValue());
		List<LendProduct> findTimeLimitMonth = lendProductService.findTimeLimit(lendProduct);
		JSONObject obj1 = new JSONObject();
		obj1.put("selected", true);
		obj1.put("timeLimitId", "-1");
		obj1.put("timeLimitValue", "全部");
		array.add(obj1);
		for (LendProduct findTimeLimit : findTimeLimitMonth) {
			JSONObject obj = new JSONObject();
			String timeLimit = findTimeLimit.getTimeLimit() + "month";
			obj.put("timeLimitId", timeLimit);
			obj.put("timeLimitValue", findTimeLimit.getTimeLimit() + "(月)");
			array.add(obj);
		}
		lendProduct.setTimeLimitType(LendProductTimeLimitType.TIMELIMITTYPE_DAY.getValue());
		List<LendProduct> findTimeLimitDay = lendProductService.findTimeLimit(lendProduct);
		for (LendProduct findTimeLimit : findTimeLimitDay) {
			JSONObject obj = new JSONObject();
			String timeLimit = findTimeLimit.getTimeLimit() + "day";
			obj.put("timeLimitId", timeLimit);
			obj.put("timeLimitValue", findTimeLimit.getTimeLimit() + "(天)");
			array.add(obj);
		}
		return array;
	}

	/**
	 * 处理查询利率下拉框的数据呈现
	 *
	 * @return Object
	 */
	@RequestMapping(value = "/searchProfitRate")
	@ResponseBody
	public JSONArray searchProfitRate() {
		JSONArray array = new JSONArray();
		List<LendProduct> findProfitRateList = lendProductService.findProfitRate();
		JSONObject obj1 = new JSONObject();
		obj1.put("selected", true);
		obj1.put("profitRateId", "-1");
		obj1.put("profitRateValue", "全部");
		array.add(obj1);
		for (LendProduct findProfitRate : findProfitRateList) {
			JSONObject obj = new JSONObject();
			obj.put("profitRateId", findProfitRate.getProfitRate());
			obj.put("profitRateValue", findProfitRate.getProfitRate() + "%");
			array.add(obj);
		}
		return array;
	}

	@RequestMapping(value = "findProductVersionByName")
	@ResponseBody
	public Object findProductVersionByName(@RequestParam("productName") String productName, @RequestParam("productType") char productType,
			@RequestParam("versionCode") String versionCode, @RequestParam("lendProductId") long lendProductId, @RequestParam("flag") String flag,
			@RequestParam("productState") char productState) {
		LendProduct lendProduct = new LendProduct();
		lendProduct.setProductName(productName.trim());
		lendProduct.setVersionCode(versionCode.trim());
		lendProduct.setProductType(String.valueOf(productType));
		boolean tf = true;
		if (flag.equals("addLendProduct")) {// 添加页面
			lendProduct.setProductState(LendProduct.PRODUCTSTATE_VALID);// 添加时默认有效
			List<LendProduct> productList = lendProductService.findProductVersionByName(lendProduct);
			if (productList.size() > 0) {// 存在同一名称且同一版本号
				tf = false;
			}

		} else {// 修改页面，需要判断是否是要修改的产品，如果是，则产品名和版本号可以与接收的一致
			LendProduct product = lendProductService.findById(lendProductId);
			if (product.getProductName().trim().equals(productName.trim()) && product.getVersionCode().trim().equals(versionCode.trim())
					&& product.getProductType().equals(String.valueOf(productType))) {// 要修改的出借产品与添加的是同一个名称和版本号
				tf = true;
			} else {
				lendProduct.setProductState(product.getProductState());// 添加时默认有效
				List<LendProduct> productList = lendProductService.findProductVersionByName(lendProduct);
				if (productList.size() > 0) {// 存在同一名称且同一版本号
					tf = false;
				}
			}
		}
		return tf;
	}

	@RequestMapping(value = "findFeesMessById")
	@ResponseBody
	public Object findFeesMessById(@ModelAttribute("feesItemId") long feesItemId) {
		FeesItem findFeesItem = feesItemService.findById(feesItemId);
		return findFeesItem.getItemType();
	}

	/**
	 * 查出已经发布的出借产品
	 * 
	 * @return
	 */
	@RequestMapping(value = "findPublishProduct")
	@ResponseBody
	public Object findPublishProduct() {
		Map params = new HashMap();
		params.put("publishState", LendProductPublishStateEnum.SELLING.value2Char());
		params.put("productType", LendProduct.PRODUCTTYPE_FINANCING);
		List<LendProductPublish> lendProductPublishList = lendProductService.findLendProductPublishBy(params);
		return lendProductPublishList;
	}

	/**
	 * 查出已经发布的出借产品
	 * 
	 * @return
	 */
	@RequestMapping(value = "findLendProductById")
	@ResponseBody
	public Object findLendProductById(@ModelAttribute("lendProductId") long lendProductId) {
		return lendProductService.findById(lendProductId);
	}

}
