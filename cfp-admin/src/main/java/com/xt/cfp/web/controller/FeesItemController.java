package com.xt.cfp.web.controller;


import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LendProduct;
import com.xt.cfp.core.service.ConstantDefineCached;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.FeesItemService;

@Controller
@RequestMapping("/jsp/product/feesitem")
public class FeesItemController extends BaseController {
	
	@Autowired
	private FeesItemService feesItemService;
	
	@Autowired
	private ConstantDefineCached constantDefineCached;
	
	@Autowired
	ConstantDefineService constantDefineService;
	
	/**
	 * 跳转到：费用管理列表页
	 */
    @RequestMapping(value = "/tofeesItemList")
    public ModelAndView loanProductList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/jsp/product/feesitem/feesItemList");
        return mv;
    }
	
	
	/**
	 * 根据费用类型和费用分类  查找费用项目的信息
	 * @param itemType
	 * @param itemKind
	 * @return
	 */
    @RequestMapping(value = "/getFeesItemsByItemType", method = RequestMethod.POST)
    @ResponseBody
    public Object getFeesItemsByItemType(@RequestParam("itemType") String itemType, @RequestParam("itemKind") char itemKind) {
        List<FeesItem> feesItems = feesItemService.getFeesItemsByTypeAndKind(itemType, itemKind);
        return feesItems;
    }
    
    /**
     * 初始化常量信息
     *
     * @return 所有常量信息
     */
    @RequestMapping(value = "/initConstantDefine")
    @ResponseBody
    public Object initConstantDefine() {
        return constantDefineCached.getAll();
    }
    
    /**
     * 列表
     * @param request
     * @param feesItem
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listByPage", method = RequestMethod.POST)
    @ResponseBody
    public Object constantsByPage(HttpServletRequest request, @ModelAttribute("feesItem") FeesItem feesItem) throws Exception {
        int pageSize = Integer.parseInt(request.getParameter("rows"));
        int pageNo = Integer.parseInt(request.getParameter("page"));
        
        if (feesItem.getItemName() != null) {
            feesItem.setItemName(feesItem.getItemName().trim());
            if (feesItem.getItemName() != null && feesItem.getItemName().length() == 0) {
                feesItem.setItemName(null);
            }
        }
        if (feesItem.getItemDesc() != null) {
            feesItem.setItemDesc(feesItem.getItemDesc().trim());
            if (feesItem.getItemDesc() != null && feesItem.getItemDesc().length() == 0) {
                feesItem.setItemDesc(null);
            }
        }
        return feesItemService.findAllByPage(pageNo, pageSize, feesItem);
    }
    /**
     * 产品的禁用
     *
     * @return
     */
    @RequestMapping(value = "disableFees")
    @ResponseBody
    public Object disableFees(@RequestParam("feesItemId") long feesItemId) {
        FeesItem feesItem = feesItemService.findById(feesItemId);
        feesItem.setItemState(FeesItem.ITEMSTATE_DISABLED);//0 启用  1禁用
        feesItemService.updateFeesItem(feesItem);
        return "success";
    }
    /**
     * 启用费用操作
     *
     * @param feesItemId
     */
    @RequestMapping(value = "/enableFees")
    public void enableFees(@RequestParam("id") long feesItemId, HttpServletResponse response) throws IOException {
        FeesItem feesItem = feesItemService.findById(feesItemId);
        feesItem.setItemState('0');//0启用  1禁用
        feesItemService.updateFeesItem(feesItem);
        String enableMess = "启用成功";
        response.getWriter().print(enableMess);
    }
    
    /**
     *根据相应的id查找对应的属性
     * @return array
     * 
     */
    @RequestMapping(value = "/editFeesItem")
    public ModelAndView editFeesItem(HttpServletRequest request) {
        /*String feesItemId = request.getParameter("feesItemId");
        Long feesLongId = Long.parseLong(feesItemId);
        //查找费用类别
        FeesItem findFeesItem = feesItemService.findById(feesLongId);
        ConstantDefine constantDefine = constantDefineService.doFeesTypeEcho(findFeesItem.getItemType());
        ConstantDefine constantDefineChild = constantDefineService.findById(Long.valueOf(findFeesItem.getItemType()));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("constantDefine", constantDefine);
        modelAndView.addObject("constantDefineChild", constantDefineChild);
        modelAndView.addObject("feesItem", findFeesItem);
        modelAndView.addObject("add", false);
        modelAndView.addObject("feesItemId", feesItemId);
        modelAndView.addObject("itemState", findFeesItem.getItemState());
        modelAndView.setViewName("/jsp/product/feesitem/addFees");
        return modelAndView;*/
    	 String feesItemId = request.getParameter("feesItemId");
         Long feesLongId = Long.parseLong(feesItemId);
         //查找费用类别
         FeesItem findFeesItem = feesItemService.findById(feesLongId);
         ConstantDefine constantDefine = constantDefineService.doFeesTypeEcho(findFeesItem.getItemType());
         ModelAndView modelAndView = new ModelAndView();
         modelAndView.addObject("constantDefine", constantDefine);
         modelAndView.addObject("feesItem", findFeesItem);
         modelAndView.addObject("add", false);
         modelAndView.addObject("feesItemId", feesItemId);
         modelAndView.addObject("itemState", findFeesItem.getItemState());
         modelAndView.setViewName("/jsp/product/feesitem/addFees");
         return modelAndView;
    }
    /**
     * 判断费用是否可做修改操作
     *
     * @param feesItemId
     * @return
     */
    @RequestMapping(value = "findFeesEditState")
    @ResponseBody
    public Object findFeesEditState(@RequestParam("feesItemId") long feesItemId) {
        FeesItem feesItemState = new FeesItem();
        feesItemState.setFeesItemId(feesItemId);
        List<LendProduct> lendProduct = feesItemService.disableLendProductFees(feesItemState);
        if (lendProduct.size() > 0) {//费用是否可修改
            return "error";
        } else {
            List<FeesItem> loanProduct = feesItemService.disableLoanProductFees(feesItemState);
            if (loanProduct.size() > 0) {//费用是否可修改
                return "error";
            } else {
                return "success";
            }
        }
    }
    /**
     * 详情
     * @param feesItemId
     * @return
     */
    @RequestMapping(value = "/findFeesMess")
    @ResponseBody
    public ModelAndView findFeesMess(@ModelAttribute("feesItemId") long feesItemId) {
        FeesItem findFeesItem = feesItemService.findById(feesItemId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("feesItem", findFeesItem);
        modelAndView.setViewName("/jsp/product/feesitem/seeFeesDetails");
        return modelAndView;
    }
    
    /**
     *费用分类
     * @return array
     */
    @RequestMapping(value = "/itemKindBox", method = RequestMethod.POST)
    @ResponseBody
    public Object itemKindBox() {
        List<ConstantDefine> constantDefine = feesItemService.findConstantTypeCode("itemKind");
        return constantDefine;
    }
    /**
     * 费用类别
     *
     * @return array
     */
    @RequestMapping(value = "/itemTypeBox", method = RequestMethod.POST)
    @ResponseBody
    public Object itemTypeBox(HttpServletRequest request) {

        String constantValue = request.getParameter("constantValue");
        ConstantDefine constantDefine = new ConstantDefine();
        constantDefine.setConstantValue(constantValue);
        constantDefine.setConstantTypeCode("itemKind");
        List<ConstantDefine> constantDefineList = feesItemService.findConstantValueOrCode(constantDefine);
        return constantDefineList;
    }

    /**
     * 费用子类别
     *
     * @return array
     */
    @RequestMapping(value = "/itemTypeChildBox", method = RequestMethod.POST)
    @ResponseBody
    public Object itemTypeChildBox(HttpServletRequest request) {
        String constantValue = request.getParameter("constantValue");
        ConstantDefine constantDefine = new ConstantDefine();
        constantDefine.setConstantValue(constantValue);
        constantDefine.setConstantTypeCode("itemType");

        List<ConstantDefine> constantDefineList = feesItemService.findConstantValueOrCode(constantDefine);

        return constantDefineList;
    }
    /**
     * 基数
     *
     * @return array
     */
    @RequestMapping(value = "/radicesTypeBox", method = RequestMethod.POST)
    @ResponseBody
    public Object radicesTypeBox() {
        List<ConstantDefine> constantDefine = feesItemService.findConstantTypeCode("radicesType");
        return constantDefine;
    }
    /**
     * 是否存在相同的费用
     * @param feesName
     * @return
     */
    @RequestMapping(value = "validFeesByName")
    @ResponseBody
    public Object validFeesByName(@RequestParam("feesName") String feesName) {

        List<FeesItem> feesItemList = feesItemService.findFeesByName(feesName);
        boolean tf = false;
        if (feesItemList.size() > 0) {//存在相同的费用
            tf = true;
        }
        return tf;
    }
    
    /**
     * @return modelAndView
     * 费用保存/更新方法
     */
    @RequestMapping(value = "/saveFeesItem", method = RequestMethod.POST)
    @ResponseBody
    public Object saveFeesItem(@ModelAttribute("feesItem") FeesItem feesItem
            , @ModelAttribute("addFlag") Boolean addFlag
    ) {
    	feesItem.setCreateTime(new Date());
        if (addFlag) {
            feesItem.setItemState(FeesItem.ITEMSTATE_ENABLED);
            feesItemService.addFeesItem(feesItem);
        } else {
            feesItemService.updateFeesItem(feesItem);
        }

        return "success";
    }
    /**
     * 打开添加费用窗口
     * @return modelAndView
     */
    @RequestMapping(value = "/addFeesItem")
    public ModelAndView addFeesItem() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("add", true);//由于新建费用和编辑费用是同一个页面,此标识用于表示新建费用
        modelAndView.addObject("feesItemId", -1);//对于新建费用来说,此字段为-1
        modelAndView.addObject("itemState", FeesItem.ITEMSTATE_ENABLED);//新建费用项目的默认状态为启用
        modelAndView.setViewName("/jsp/product/feesitem/addFees");
        return modelAndView;
    }

    
    @RequestMapping(value = "/getFeesItemsByItemKind", method = RequestMethod.POST)
    @ResponseBody
    public Object getFeesItemsByItemKind(@RequestParam("itemKind") char itemKind ,@RequestParam("lendTypeSelect") String lendTypeSelect) {
        FeesItem feesItem = new FeesItem();
        List<FeesItem> feesItems = feesItemService.findAll(feesItem);
        List<FeesItem> feesList = new ArrayList<FeesItem>();
        for (FeesItem fees : feesItems) {
            if ((fees.getItemKind() == itemKind) && (fees.getItemState() == FeesItem.ITEMSTATE_ENABLED)) {
            	if(lendTypeSelect.equals("1")){
            		if(fees.getItemName().indexOf("不收费")== -1){
            			  feesList.add(fees);
            		}
            	}else if(lendTypeSelect.equals("2")){
            		if(fees.getItemName().indexOf("不收费")!= -1){
            			  feesList.add(fees);
            		}
            	}else{
            		  feesList.add(fees);
            	}
              
            }
        }

        return feesList;
    }

}
