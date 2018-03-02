package com.xt.cfp.app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.constants.CustomerCardBindStatus;
import com.xt.cfp.core.constants.CustomerCardStatus;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.CustomerCard;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.CustomerCardService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.WithDrawService;
import com.xt.cfp.core.util.StringUtils;

@Controller
@RequestMapping("/bankcard")
public class BankCardController extends BaseController {
	
	@Autowired
	private CustomerCardService customerCardService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private ConstantDefineService constantDefineService;
	
	@Autowired
	private WithDrawService withDrawService;
	
	/**
	 * 【APP接口】
	 * 绑定银行卡，获取用户信息
	 */
    @RequestMapping(value = "/to_bankcard_add", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object to_bankcard_add(HttpServletRequest request) {
    	try {
    		//登录验证
    		UserInfo currentUser = getCurrentUser(request);
    		if(null == currentUser){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
    		
    		//获取用户信息
            UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
            if(null == userExt){
            	return returnResultMap(false, null, "exception", "用户信息不存在");
            }
            
            // 返回结果
            Map<String,String> resultMap = new HashMap<String,String>();
            resultMap.put("realName", userExt.getRealName());
            return returnResultMap(true, resultMap, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
		}
    }
    
    /**
     * 【APP接口】
     * 获取银行卡信息（已绑定的）
     */
    @RequestMapping(value = "/to_bankcard_list", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object to_bankcard_list(HttpServletRequest request) {
        try {
        	//登录验证
    		UserInfo currentUser = getCurrentUser(request);
    		if(null == currentUser){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
    		
    		//获取卡信息
        	List<CustomerCard> customerCards = customerCardService.getCustomerCardsByUserId(currentUser.getUserId());
        	CustomerCard customerCardEnable = null;//正在用的一张
        	List<CustomerCard> customerCardDisableList = new ArrayList<CustomerCard>();//历史卡列表
        	if(null != customerCards && customerCards.size() > 0){
        		for (CustomerCard customerCard : customerCards) {
        			BigDecimal bigDecimal = withDrawService.getWithdrawAmountSumByCardId(customerCard.getCustomerCardId());
        			if(null != bigDecimal){
        				customerCard.setWithdrawAmountSum(bigDecimal);
        			}
    				if(CustomerCardStatus.NORMAL.getValue().equals(customerCard.getStatus())
    						&& CustomerCardBindStatus.BINDED.getValue().equals(customerCard.getBindStatus())){
    					customerCardEnable = customerCard;
    				}else {
    					customerCardDisableList.add(customerCard);
    				}
    			}
        	}
        	
        	// 返回结果
            Map<String,Object> resultMap = new HashMap<String,Object>();
            if(null != customerCardEnable){
            	resultMap.put("customerCardId", customerCardEnable.getCustomerCardId());//客户卡ID
                resultMap.put("bankCode", customerCardEnable.getBankCode());//所属银行
                resultMap.put("bankLogo", "/images/banklogo/" + customerCardEnable.getBankCode() + "_2.png");//所属银行LOGO
                resultMap.put("encryptCardNo", StringUtils.getPreStr(customerCardEnable.getCardCode(), 4) + "********" + StringUtils.getLastStr(customerCardEnable.getCardCode(), 4));//银行卡后四位
                //银行名称
            	ConstantDefine define = constantDefineService.findById(customerCardEnable.getBankCode());
            	if(null != define){
            		resultMap.put("bankName", define.getConstantName());
            	}
            }
            
            return returnResultMap(true, resultMap, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
		}
    }
 	
}
