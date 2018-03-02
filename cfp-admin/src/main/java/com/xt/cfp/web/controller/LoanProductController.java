package com.xt.cfp.web.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LendProduct;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.pojo.LoanProductFeesItem;
import com.xt.cfp.core.service.ConstantDefineCached;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.FeesItemService;
import com.xt.cfp.core.service.LoanProductFeesItemService;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.Pagination;

@Controller
@RequestMapping("/jsp/product/loan")
public class LoanProductController extends BaseController {
	
	@Autowired
	private LoanProductService loanProductService;
	
	@Autowired
	private ConstantDefineCached constantDefineCached;
	
	@Autowired
	private ConstantDefineService constantDefineService;
	
	@Autowired 
	private LoanProductFeesItemService loanProductFeesItemService;
	
	@Autowired
	private FeesItemService feesItemService;
	
	/**
	 * 跳转到：借款产品列表页
	 */
    @RequestMapping(value = "/to_loan_product_list")
    public ModelAndView loanProductList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/product/loan/loan_product_list");
        return mv;
    }
    
    // 列表页数据加载
    @RequestMapping(value = "/loanProductPage", method = RequestMethod.POST)
    @ResponseBody
    public Object loanProductList(HttpServletRequest request, @ModelAttribute("loanProduct") LoanProduct loanProduct) throws Exception {
        int pageSize = Integer.parseInt(request.getParameter("rows"));
        int pageNo = Integer.parseInt(request.getParameter("page"));
        if (loanProduct.getSearchDueTime() != null && loanProduct.getSearchDueTime().trim().length() > 0) {
            String serchDueTime = loanProduct.getSearchDueTime();
            if (serchDueTime.indexOf("month") > 0) {
                loanProduct.setDueTime(Integer.parseInt(serchDueTime.replaceAll("month", "")));
                loanProduct.setDueTimeType(LoanProduct.DUETIMETYPE_MONTH);
            } else if (serchDueTime.indexOf("day") > 0) {
                loanProduct.setDueTime(Integer.parseInt(serchDueTime.replaceAll("day", "")));
                loanProduct.setDueTimeType(LoanProduct.DUETIMETYPE_DAY);
            }
        } else {
            loanProduct.setSearchDueTime(null);
        }

        if (loanProduct.getProductName() != null) {
            loanProduct.setProductName(loanProduct.getProductName().trim());
            if (loanProduct.getProductName() != null && loanProduct.getProductName().length() == 0) {
                loanProduct.setProductName(null);
            }
        }

        Pagination<LoanProduct> loanProducts = loanProductService.findAllByPage(pageNo, pageSize, loanProduct);
        return loanProducts;
    }
    
	// 跳转到添加页
    @RequestMapping(value = "/addLoanProduct")
    public ModelAndView addLoanProduct() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("add", true);
        mv.addObject("loadProductId", -1);
        List<ConstantDefine> itemTypeList = constantDefineCached.getByTypeCode("itemType");
        List<Map> itemTypes = new ArrayList<Map>();
        for (ConstantDefine constantDefine : itemTypeList) {
            if (String.valueOf(constantDefine.getParentConstant()).equals(String.valueOf(FeesItem.ITEMKIND_LOAN))) {
                Map map = new HashMap();
                map.put("constantValue", constantDefine.getConstantValue());
                map.put("constantName", constantDefine.getConstantName());
                List<ConstantDefine> childDefineList = constantDefineService.findByTypeCodeAndParentConstant("itemChildType", constantDefine.getConstantDefineId());
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
        mv.addObject("itemTypes", itemTypes);
        mv.setViewName("jsp/product/loan/loan_product_add");
        return mv;
    }
    
    @RequestMapping(value = "/getConstantDefines")
    @ResponseBody
    public Object getConstantDefines() {
        return constantDefineCached.getAll();
    }
    
    /**
     * 判断借款产品是否可以修改
     *
     * @param loanProductId
     * @return
     */
    @RequestMapping(value = "/findLoanApplicationState")
    @ResponseBody
    public Object findLoanApplicationState(@RequestParam("loanProductId") long loanProductId) {
        List<LoanProduct> loanProduct = loanProductService.findLoanApplicationState(loanProductId);
        if (loanProduct.size() > 0) {//有合同占用，该产品不可修改
            return "error";
        } else {
            return "success";
        }
    }
    
    /**
     * 处理修改信息回显
     * 根据id查找指定的借款产品
     *
     * @param loanProductId
     * @return
     */
    @RequestMapping(value = "/editLoanProduct")
    @ResponseBody
    public ModelAndView editLoanProduct(@ModelAttribute("loanProductId") long loanProductId) throws Exception {

        LoanProduct loanProduct = loanProductService.findById(loanProductId);

        FeesItem feesItem = new FeesItem();
        feesItem.setFeesItemId(loanProductId);
        feesItem.setItemKind(FeesItem.ITEMKIND_LOAN);
        List<FeesItem> feesItemByLoanProduct = loanProductService.feesItemByLoanProductId(feesItem);
        List<FeesItem> feesItemByLoanProductId = new ArrayList<FeesItem>();
        for (FeesItem feesItemRadices : feesItemByLoanProduct) {
            if (feesItemRadices.getRadicesType() != 0) {
                String constantValue = String.valueOf(feesItemRadices.getRadicesType());
                ConstantDefine constant = new ConstantDefine();
                constant.setConstantTypeCode("radicesType");
                constant.setConstantValue(constantValue);
                ConstantDefine constantDefine = constantDefineService.findConstantByTypeCodeAndValue(constant);
                //把基数名称和费用比例封装到基数显示名中，便于修改页面显示收费规则
                feesItemRadices.setRadiceName(constantDefine.getConstantName() + "*" + feesItemRadices.getFeesRate() + "%");
            } else {
                //把基数名称和费用比例封装到基数显示名中，便于修改页面显示收费规则
                feesItemRadices.setRadiceName(feesItemRadices.getRadiceName() + "*" + String.valueOf(feesItemRadices.getFeesRate()) + "%");
            }
            feesItemByLoanProductId.add(feesItemRadices);
        }
        //设置前台费用费用详情回显操作
        List<LoanProductFeesItem> loanProductFeesItem = loanProductFeesItemService.getByProductId(loanProductId);
        List<ConstantDefine> itemTypeList = constantDefineCached.getByTypeCode("itemType");
        List<Map> itemTypes = new ArrayList<Map>();
        for (ConstantDefine constantDefine : itemTypeList) {
            if (String.valueOf(constantDefine.getParentConstant()).equals(String.valueOf(FeesItem.ITEMKIND_LOAN))) {
                Map map = new HashMap();
                map.put("constantValue", constantDefine.getConstantValue());
                map.put("constantName", constantDefine.getConstantName());
                List<ConstantDefine> childDefineList = constantDefineService.findByTypeCodeAndParentConstant("itemChildType", constantDefine.getConstantDefineId());
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
        ModelAndView model = new ModelAndView();
        model.addObject("add", false);
        model.addObject("loadProductId", loanProductId);
        model.addObject("loanProduct", loanProduct);
        model.addObject("itemTypes", itemTypes);
        model.addObject("loanProductFeesItemList", loanProductFeesItem);
        model.addObject("feesItemByLoanProductId", feesItemByLoanProductId);
        model.setViewName("jsp/product/loan/loan_product_edit");
        return model;
    }
    
    @RequestMapping(value = "/enableOrdisableLoanProduct")
    @ResponseBody
    public void enableOrdisableLoanProduct(@RequestParam("loanProductId") long loanProductId, @RequestParam("Type") String Type) {
        LoanProduct loanProduct = new LoanProduct();
        if (Type.equalsIgnoreCase("disableLoanProduct")) {
            loanProduct.setProductState(LoanProduct.PUBLISHSTATE_INVALID);
        } else {
            loanProduct.setProductState(LoanProduct.PUBLISHSTATE_VALID);
        }
        loanProduct.setLoanProductId(loanProductId);
        loanProductService.enableOrDisableLoanProduct(loanProduct);
    }
    
    /**
     * 处理查看详情
     *
     * @param loanProductId
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "findLoanProduct")
    @ResponseBody
    public ModelAndView findLoanProduct(@RequestParam("loanProductId") long loanProductId) throws Exception {
        LoanProduct loanProduct = loanProductService.findById(loanProductId);
        List<ConstantDefine> LoanProductFeesList = loanProductFeesItemService.findLoanProductFeesById(loanProductId);
        List<ConstantDefine> itemTypeList = constantDefineCached.getByTypeCode("itemType");
        List<ConstantDefine> loanItemTypeList = new ArrayList<ConstantDefine>();
        for (ConstantDefine constantDefine : itemTypeList) {
            if (String.valueOf(constantDefine.getParentConstant()).equals(String.valueOf(FeesItem.ITEMKIND_LOAN))) {
                loanItemTypeList.add(constantDefine);
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("jsp/product/loan/loan_product_detail");
        modelAndView.addObject("loanProduct", loanProduct);
        modelAndView.addObject("LoanProductFeesList", LoanProductFeesList);
        modelAndView.addObject("itemTypeList", loanItemTypeList);
        return modelAndView;
    }
    
    /**
     * 处理查询利率下拉框的数据呈现
     *
     * @return Object
     */
    @RequestMapping(value = "/findAnnualRate")
    @ResponseBody
    public JSONArray findAnnualRate() {
        JSONArray array = new JSONArray();
        List<LoanProduct> findAnnualRate = loanProductService.findAnnualRate();
        JSONObject obj1 = new JSONObject();
        obj1.put("selected", true);
        obj1.put("annualRateId", "-1");
        obj1.put("annualRateValue", "全部");
        array.add(obj1);
        for (LoanProduct loanProduct : findAnnualRate) {
            JSONObject obj = new JSONObject();
            obj.put("annualRateId", loanProduct.getAnnualRate());
            obj.put("annualRateValue", loanProduct.getAnnualRate() + "%");
            array.add(obj);
        }
        return array;
    }
    
    /**
     * 处理查询期限下拉框的数据呈现
     *
     * @return Object
     */
    @RequestMapping(value = "/findDueTime")
    @ResponseBody
    public JSONArray findDueTime() {
        JSONArray array = new JSONArray();
        List<LoanProduct> findDueTimeMonth = loanProductService.findDueTimeMonth();
        JSONObject obj1 = new JSONObject();
        obj1.put("selected", true);
        obj1.put("dueTimeId", "-1");
        obj1.put("dueTimeValue", "全部");
        array.add(obj1);
        for (LoanProduct loanProduct : findDueTimeMonth) {
            JSONObject obj = new JSONObject();
            String dueTimes = loanProduct.getDueTime() + "month";
            obj.put("dueTimeId", dueTimes);
            obj.put("dueTimeValue", loanProduct.getDueTime() + "(月)");
            array.add(obj);
        }
        List<LoanProduct> findDueTimeDay = loanProductService.findDueTimeDay();
        for (LoanProduct loanProduct : findDueTimeDay) {
            JSONObject obj = new JSONObject();
            String dueTimes = loanProduct.getDueTime() + "day";
            obj.put("dueTimeId", dueTimes);
            obj.put("dueTimeValue", loanProduct.getDueTime() + "(天)");
            array.add(obj);
        }
        return array;
    }
    
    /**
     * 判断当前产品是否有效
     *
     * @param productName
     * @return
     */
    @RequestMapping(value = "/doProductStateByName")
    @ResponseBody
    public Object doProductStateByName(@RequestParam("productName") String productName) {
        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductName(productName);
        loanProduct.setProductState(LoanProduct.PUBLISHSTATE_VALID);//有效
        List<LoanProduct> loanProductList = loanProductService.doProductStateByName(loanProduct);
        if (loanProductList.size() > 0) {//借款产品名已存在，并且产品状态为有效
            return "error";
        } else {
            return "success";
        }
    }
    
    /**
     * 判断同一产品只能为一个版本号
     *
     * @param loanProductId
     * @param versionCode
     * @return
     */
    @RequestMapping(value = "/doProductVersionByName")
    @ResponseBody
    public Object doProductVersionByName(@ModelAttribute("versionCode") String versionCode, @RequestParam("loanProductId") long loanProductId,@RequestParam("productName") String productName) {
        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setVersionCode(versionCode.trim());
        boolean tf = true;
        List<LoanProduct> loanProductList = loanProductService.doProductVersionByName(loanProduct);
        for (LoanProduct product : loanProductList) {
            if (product.getLoanProductId() != loanProductId) {
                if(product.getProductName().equals(productName)){
                    tf = false;
                    break;
                }
            }
        }
        return tf;
    }
    
    @RequestMapping(value = "/getConstantByType", method = RequestMethod.POST)
    @ResponseBody
    public Object getConstantByType(@RequestParam("typeCode") String typeCode,
    		@RequestParam (value="productType", required = false) String productType) {
    	List<ConstantDefine> cdList = constantDefineCached.getByTypeCode(typeCode);
    	List<ConstantDefine> newCdList = new ArrayList<>();
    	//省心计划返息周期和续费的过滤
    	if(productType != null && StringUtils.equals(productType, "finance")){
    		for(ConstantDefine cd : cdList){
    			if(typeCode.equals("toInterestPoint")){
    				if(cd.getConstantValue().equals("6")){
    					newCdList.add(cd);
    				}
    			}else if(typeCode.equals("renewalCycleType")){
    				if(cd.getConstantValue().equals("1")){
    					newCdList.add(cd);
    				}
    			}
    		}
    	}else{
    		newCdList = cdList;
    	}
        return newCdList ;
    }
    
    /**
     * 处理选择费用时，费用基数和费用比例的回显
     *
     * @param feesItemId
     * @return
     */
    @RequestMapping(value = "/findFeesRadicesAndRate")
    @ResponseBody
    public JSONArray findFeesRadicesAndRate(@RequestParam("feesItemId") Long feesItemId) {
        JSONArray array = new JSONArray();
        FeesItem feesItem = feesItemService.findById(feesItemId);
        if (feesItem.getRadicesType() != 0) {
            String constantValue = String.valueOf(feesItem.getRadicesType());
            ConstantDefine constant = new ConstantDefine();
            constant.setConstantTypeCode("radicesType");
            constant.setConstantValue(constantValue);
            ConstantDefine constantDefine = constantDefineService.findConstantByTypeCodeAndValue(constant);
            //把基数名称和费用比例封装到基数显示名中，便于修改页面显示收费规则
            feesItem.setRadiceName(constantDefine.getConstantName() + "*" + feesItem.getFeesRate() + "%");
        } else {
            //把基数名称和费用比例封装到基数显示名中，便于修改页面显示收费规则
            feesItem.setRadiceName(feesItem.getRadiceName() + "*" + String.valueOf(feesItem.getFeesRate()) + "%");
        }
        array.add(feesItem);
        return array;
    }
    
    /**
     * 借款产品的保存（添加+编辑）
     * @param loanProduct
     * @param addFlag
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveLoanProduct", method = RequestMethod.POST)
    @ResponseBody
    public Object saveLoanProduct(LoanProduct loanProduct
            , @ModelAttribute("addFlag") Boolean addFlag
            , HttpServletRequest request) {
    	
    	// 注意：临时赋值
        AdminInfo currentUser = getCurrentUser();
        if(currentUser==null)
            return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.LONGIN_EXIST.getDesc())
                    .toJson();
        loanProduct.setAdminId(currentUser.getAdminId());
    	
        if (addFlag) {

            List<LoanProductFeesItem> loanProductFeesItems = new ArrayList<LoanProductFeesItem>();
            String[] feeItemStrs = request.getParameterValues("feeItems");
            if (feeItemStrs != null) {
                for (String str : feeItemStrs) {
                    LoanProductFeesItem loanProductFeesItem = new LoanProductFeesItem();
                    loanProductFeesItem.setChargeCycle((request.getParameter("chargePoint_" + str).charAt(0)));
                    System.out.print(request.getParameter("feesItems_" + str));
                    loanProductFeesItem.setFeesItemId(Integer.parseInt(request.getParameter("feesItems_" + str)));
                    loanProductFeesItem.setWorkflowRatio(new BigDecimal(request.getParameter("workflowRatio_" + str)).divide(new BigDecimal("100"), 18, BigDecimal.ROUND_CEILING));
                    loanProductFeesItems.add(loanProductFeesItem);
                }
            }
            loanProductService.addLoanProduct(loanProduct, loanProductFeesItems);
        } else {
            List<LoanProductFeesItem> loanProductFeesItems = new ArrayList<LoanProductFeesItem>();
            String[] feeItemStrs = request.getParameterValues("feeItems");
            if (feeItemStrs != null) {
                for (String str : feeItemStrs) {
                    LoanProductFeesItem loanProductFeesItem = new LoanProductFeesItem();
                    loanProductFeesItem.setChargeCycle((request.getParameter("chargePoint_" + str).charAt(0)));
                    String feesItems = request.getParameter("feesItems_" + str);
                    if (feesItems != null && !feesItems.equals("")) {
                        loanProductFeesItem.setFeesItemId(Integer.parseInt(request.getParameter("feesItems_" + str)));
                    }
                    loanProductFeesItem.setWorkflowRatio(new BigDecimal(request.getParameter("workflowRatio_" + str)).divide(new BigDecimal("100"), 18, BigDecimal.ROUND_CEILING));

                    loanProductFeesItems.add(loanProductFeesItem);
                }
            }
            loanProductService.updateLoanProduct(loanProduct, loanProductFeesItems);
        }
        return "success";
    }

    /**
     * 查询借款产品
     * @param productType
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/getLoanProductList")
    @ResponseBody
    public Object getLoanProductList(@RequestParam("productType") String productType) throws ParseException {
        List<LoanProduct> list= new ArrayList<LoanProduct>();
        if (String.valueOf(productType).equals(LendProduct.PRODUCTTYPE_FINANCING)) {
            for (LoanProduct product : loanProductService.findAll()) {
                if(product.getProductState() == LoanProduct.PUBLISHSTATE_VALID  && product.getStartDate().before(new Date()) && product.getEndDate().after(new Date())){
                    list.add(product);
                }
            }
        } else {
            for (LoanProduct product : loanProductService.findNoLendProduct()) {
                /*if((formatterTime(product.getStartDate()).before(formatterTime(new Date()))
                        && formatterTime(product.getEndDate()).after(new Date()))
                        ||
                        (formatterTime(product.getStartDate()).equals(formatterTime(new Date()))
                                && formatterTime(product.getEndDate()).equals(formatterTime(new Date())))){
                    list.add(product);
                }*/
				if (DateUtil.parseStrToDate(DateUtil.getDateLong(product.getEndDate()), "yyyy-MM-dd").after(
						DateUtil.parseStrToDate(DateUtil.getDateLong(new Date()), "yyyy-MM-dd"))) {
					list.add(product);
				}
            }
        }
        return list;
    }
    
    /**
     * 加载借款产品下拉框
     * @param selectedDisplay
     */
	@RequestMapping(value = "/loadLoanProduct")
	@ResponseBody
	public Object loadLoanProduct(@RequestParam(value = "selectedDisplay", required = false) String selectedDisplay){
		if("selected".equals(selectedDisplay)){
			selectedDisplay = "请选择";
		}else {
			selectedDisplay = "全部";
		}
		List<LoanProduct> list = loanProductService.findAllByValid();
		List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> map;
		map = new HashMap<Object, Object>();
		map.put("PRODUCTNAME", selectedDisplay);
		map.put("LOANPRODUCTID", "");
		map.put("selected", true);
		maps.add(map);
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				LoanProduct loanProduct = list.get(i);
				map = new HashMap<Object, Object>();
				map.put("PRODUCTNAME", loanProduct.getProductName());
				map.put("LOANPRODUCTID", loanProduct.getLoanProductId());
				maps.add(map);
			}
		}
		return maps;
	}
	
	/**
	 * 获得借款产品详情
	 * @param loanproductid 借款产品ID
	 */
 	@RequestMapping("/getLoanProductDetail")
 	@ResponseBody
 	public Object getLoanProductDetail(@RequestParam(value = "loanproductid", required = false) Long loanproductid) {
 		Map map = new HashMap();
 		try {
 			// 合法性验证。
 			if(null == loanproductid || "".equals(loanproductid)){
 				return returnResultMap(false, null, "check", "借款产品ID不能为空！");
 			}else{
 				LoanProduct loanProduct = loanProductService.findById(loanproductid);
 	 			
 	 			// annualRate 年利率
 	 			// dueTime 期限时长
 	 			// dueTimeType 期限类型
 	 			// repaymentType 还款方式
 	 			
 	 			map.put("annualRate", loanProduct.getAnnualRate());
 	 			map.put("dueTime", loanProduct.getDueTime() + loanProduct.getDueTimeTypeStr(loanProduct.getDueTimeType()));
 	 			map.put("repaymentType", loanProduct.getRepaymentTypeStr(loanProduct.getRepaymentType()));
 	 			
 	 			List<Map> feesItemList = new ArrayList<Map>();
 	 			List<LoanProductFeesItem> productFeesItems = loanProductFeesItemService.getByProductId(loanproductid);
 	 			if(null != productFeesItems && productFeesItems.size() > 0){
 	 				for (int i = 0; i < productFeesItems.size(); i++) {
 	 					LoanProductFeesItem productFeesItem = productFeesItems.get(i);
 	 					
 	 					Map itemMap = new HashMap();
 	 					
 	 					//itemType 费用类别
 	 					FeesItem feesItem = feesItemService.findById(productFeesItem.getFeesItemId());
 	 					feesItem.getItemTypeStr(feesItem.getItemType());
 	 					
 	 					//chargeCycle 收费周期,收取时机
 	 					productFeesItem.getChargeCycle();
 	 					feesItem.getChargeCycleStr(productFeesItem.getChargeCycle());
 	 					
 	 					//itemName 费用名称
 	 					feesItem.getItemName();
 	 					
 	 					itemMap.put("chargeCycle", feesItem.getChargeCycleStr(productFeesItem.getChargeCycle()));
 	 					itemMap.put("itemType", feesItem.getItemTypeStr(feesItem.getItemType()));
 	 					itemMap.put("itemName", feesItem.getItemName());
 	 					
 	 					feesItemList.add(itemMap);
 					}
 	 			}
 	 			map.put("feesItemList", feesItemList);
 			}
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 			return returnResultMap(false, null, null, e.getMessage());
 		}
 		return returnResultMap(true, map, null, null);
 	}
	

}
