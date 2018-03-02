package com.xt.cfp.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.RateEnum.RateProductStatusEnum;
import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.RateProduct;
import com.xt.cfp.core.pojo.ext.RateProductConditionVO;
import com.xt.cfp.core.pojo.ext.RateProductVO;
import com.xt.cfp.core.service.RateProductService;
import com.xt.cfp.core.service.RateUserService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.core.util.TimeInterval;

@Controller
@RequestMapping(value = "/jsp/rate/product")
public class RateProductController extends BaseController {
	
	@Autowired
    private RateProductService rateProductService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RateUserService rateUserService;

	/**
	 * 跳转至：加息券产品列表页
	 */
    @RequestMapping(value = "to_list")
    public String to_list(){
        return "jsp/rate/product/list";
    }
    
    /**
     * 执行：加息券产品列表
     * @param rateProduct
     * @param startDate
     * @param endDate
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public Object list(@ModelAttribute RateProduct rateProduct,
                       @RequestParam(value = "startDateStr", defaultValue = "", required = false) String startDate,
                       @RequestParam(value = "endDateStr" ,defaultValue = "", required = false) String endDate,
                       @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                       @RequestParam(value = "page", defaultValue = "1") int pageNo){
        //封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));
        
        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);
        
        //验证非法字符
        if(null != rateProduct){
        	if(!StringUtils.isNull(rateProduct.getRateProductName())){
        		if(StringUtils.checkSQLStr(rateProduct.getRateProductName())){
        			return new Pagination<RateProductVO>();
        		}
        	}
        }

        Pagination<RateProductVO> pagination = rateProductService.getRateProductPaging(pageNo, pageSize, rateProduct, customParams);
        
        //数据处理[开始]
        List<RateProductVO> vos =  pagination.getRows();
        if(null != vos && vos.size() > 0){
        	for (RateProductVO v : vos) {
				v.getRateProductConditionValue(v.getCondition());
				v.setStartAmount(v.getRateProductConditionVO().getCon3_start_amount());
			}
        }
        //数据处理[结束]
        
        return pagination;
    }
    
    /**
     * 执行：停用
     * @param request
     * @param rateProductId
     * @return
     */
    @RequestMapping(value = "stopUse")
    @ResponseBody
    public String stopUse(HttpServletRequest request,Long rateProductId){
        //修改状态为无效
    	rateProductService.changeRateProductStatus(rateProductId, RateProductStatusEnum.UN_VALID);
        return "success";
    }

    /**
     * 执行：启用
     * @param request
     * @param rateProductId
     * @return
     */
    @RequestMapping(value = "startUse")
    @ResponseBody
    public String startUse(HttpServletRequest request,Long rateProductId){
        //修改状态为有效
    	Integer count = rateUserService.getCountByRateProductId(rateProductId);
    	if(count > 0){//如果已使用
    		rateProductService.changeRateProductStatus(rateProductId, RateProductStatusEnum.HAS_USED);
    	}else {
    		rateProductService.changeRateProductStatus(rateProductId, RateProductStatusEnum.VALID);
		}
        return "success";
    }
    
	/**
	 * 跳转至：加息券产品添加页
	 */
    @RequestMapping(value = "to_add")
    public String to_add(){
        return "jsp/rate/product/add";
    }
    
    /**
     * 执行：新增财富券产品
     * @param rateProduct
     * @return
     */
    @RequestMapping(value = "add")
    @ResponseBody
    public String add(RateProduct rateProduct, RateProductConditionVO rateProductConditionVO){
        AdminInfo currentUser = getCurrentUser();
        if (currentUser==null)
            return "session已过期";
        try {
        	
        	//将条件实体，转换为json格式串
        	RateProductVO rateProductVO = new RateProductVO();
        	String condition = rateProductVO.setRateProductConditionValue(rateProductConditionVO);
        	
        	rateProduct.setCondition(condition);
        	rateProduct.setStatus(RateProductStatusEnum.VALID.getValue());
        	rateProduct.setCreateTime(new Date());
        	rateProduct.setLastUpdateTime(new Date());
        	rateProduct.setAdminId(currentUser.getAdminId());
        	
        	rateProductService.addRateProduct(rateProduct);

            return "success";
        }catch (SystemException se){
            logger.error(se.getDetailDesc(),se);
            se.printStackTrace();
            return se.getMessage();
        }
    }
    
    /**
     * 跳转至：详情页面
     * @param request
     * @param rateProductId
     * @return
     */
    @RequestMapping(value = "detail")
    public String detail(HttpServletRequest request,Long rateProductId) {
    	RateProduct rateProduct = rateProductService.getRateProductById(rateProductId);
    	
    	RateProductVO rateProductVO = new RateProductVO();
    	RateProductConditionVO rateProductConditionVO = rateProductVO.getRateProductConditionValue(rateProduct.getCondition());
    	
        request.setAttribute("rateProduct", rateProduct);
        request.setAttribute("rateProductConditionVO", rateProductConditionVO);
        return "jsp/rate/product/detail";
    }
	
}
