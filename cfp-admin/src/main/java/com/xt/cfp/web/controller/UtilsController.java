package com.xt.cfp.web.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.NameValue;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ExcelErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.RechargeChannelEnum;
import com.xt.cfp.core.constants.AgreementEnum.AgreementStatusEnum;
import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.AgreementInfo;
import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.service.AgreementInfoService;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.DistributionInviteService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.UtilsService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.ExcelUtil;
import com.xt.cfp.core.util.JsonUtil;
import com.xt.cfp.core.util.ValidationUtil;

/**
 * Created by yulei on 2015/7/27.
 */
@Controller
@RequestMapping(value = "/sysutils")
public class UtilsController extends BaseController {

    @Autowired
    private UtilsService utilsService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private AgreementInfoService agreementInfoService;

    @RequestMapping(value = "/toUtils")
    String toUtils(HttpServletRequest request) {
    	request.setAttribute("channels", RechargeChannelEnum.values());
        return "jsp/system/utils";
    }

    @ResponseBody
    @RequestMapping(value = "/repairAcc")
    Object repairAcc() {
        try {
            utilsService.repairAcc();
        } catch (Exception e) {
            return "error";
        }

        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/reSendAward")
    Object reSendAward(@RequestParam(value =  "dateLimit", required = false) String dateLimit) {
        //(1)数据校验
        AdminInfo currentUser = getCurrentUser();
        if (currentUser==null)
            return super.returnResultMap(false, null, null, "session过期，请重新登陆");

        ValidationUtil.checkRequiredPara(new NameValue<String, Object>("dateLimit", dateLimit));
        String[] split = dateLimit.split("_");
        ValidationUtil.checkRequiredPara(
                new NameValue<String, Object>("startDate", split[0]),
                new NameValue<String, Object>("endDate", split[1])
        );

        //(2)执行
        this.utilsService.reSendAward(DateUtil.parseStrToDate(split[0], "yyyy-MM-dd"), DateUtil.parseStrToDate(split[1], "yyyy-MM-dd"));

        //(3)返回结果
        return super.returnResultMap(true, null, null, null);
    }

    @ResponseBody
    @RequestMapping(value = "/doSysRecharge")
    Object doSysRecharge(@RequestParam(value = "userId", required = false) Long userId,
                         @RequestParam(value = "amount", required = false) BigDecimal amount,
                         @RequestParam(value = "externalNo", required = false) String externalNo,
                         @RequestParam(value = "channelCode", required = false) String channelCode,
                         @RequestParam(value = "rechargeReason", required = false) String rechargeReason,
                         @RequestParam(value = "rechargeDesc", required = false) String rechargeDesc) {
        ValidationUtil.checkRequiredPara(
                new NameValue<String, Object>("userId", userId),
                new NameValue<String, Object>("amount", amount),
                new NameValue<String, Object>("rechargeCode", externalNo),
                new NameValue<String, Object>("channelCode", channelCode),
                new NameValue<String, Object>("rechargeReason", rechargeReason)
        );

        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(rechargeDesc))
            map = JsonUtil.fromJson(rechargeDesc);

        AdminInfo currentUser = getCurrentUser();
        if (currentUser==null)
            return super.returnResultMap(false, null, null, "session过期，请重新登陆");
        UserInfo userByMobileNo = userInfoService.getUserByUserId(userId);
        Map<String,Object> rsMap = this.utilsService.doRecharge(rechargeReason, userByMobileNo.getUserId(), amount, map,
        		currentUser,externalNo,channelCode);
        if(rsMap != null){
        	return super.returnResultMap((Boolean)rsMap.get("flag"), null, null, (String)rsMap.get("info"));
        }
        	
        return super.returnResultMap(true, null, null, null);
    }

    @ResponseBody
    @RequestMapping(value = "/doSysRechargeForAward")
    Object doSysRechargeForAward(MultipartFile fi_award, String rechargeDesc) {
        if (fi_award == null)
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("paramName", fi_award);

        ValidationUtil.checkRequiredPara(
                new NameValue<String, Object>("rechargeDesc", rechargeDesc)
        );

        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(rechargeDesc))
            map = JsonUtil.fromJson(rechargeDesc);

        String[] title = {"用户姓名", "手机号", "金额"};
        List<Map<String, Object>> result = ExcelUtil.getUploadExcelData(fi_award);
        if (result == null)
            throw new SystemException(ExcelErrorCode.ANALYSIS_FAILE);
        this.utilsService.doRechargeForAward(title, result, map);

        return super.returnResultMap(true, null, null, null);
    }
    
    /**
     * 重新计算账户历史流水
     */
    @RequestMapping(value = "/doCashFlowCalculate")
	@ResponseBody
    public Object cashFlowCalculate(@RequestParam("accId") Long accId) {
    	try {
    		userAccountService.cashFlowCalculate(accId);
        	return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
    	
    }


    /**
     * 为指定的借款申请重生成放款提现单
     * @param applyIds
     * @return
     */
    @RequestMapping(value = "/reMakeWithdrawForApply")
    @ResponseBody
    public Object reMakeWithdrawForApply(@RequestParam("applyIds") String applyIds) {
        ValidationUtil.checkRequiredPara(new NameValue<String, Object>("applyIds", applyIds));
        Collection<Long> ids = new ArrayList<>();
        if (applyIds.contains(",")) {
            ids = com.xt.cfp.core.util.StringUtils.splitToSpcType(applyIds, ",", Long.class);
        } else {
            ids.add(Long.valueOf(applyIds));
        }

        try {
            this.utilsService.reMakeWithdrawForApply(ids);
        } catch (SystemException se) {
            logger.error(se);
            return returnResultMap(false, null, null, null);
        }

        return returnResultMap(true, null, null, null);
    }
    
	/**
	 * 根据满标时间，检查并重新生成丢失的合同
	 */
	@RequestMapping(value = "/checkAndCreateAgreementByFullTime")
	@ResponseBody
	public Object checkAndCreateAgreementByFullTime(@RequestParam("fullTime") String fullTime) {
		try {
			if (null == fullTime || "".equals(fullTime)) {
				return returnResultMap(false, null, "check", "满标时间不能为空！");
			}

			// 1.根据满标日期，查询借款申请列表。
			// 2.根据列表，查询债权列表，在查询合同列表。
			// 3.检查物理合同文件是否存在，如果不存在，将改借款申请的合同重新生成一遍。

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("fullTime", DateUtil.parseStrToDate(fullTime, "yyyy-MM-dd"));// 查询条件，满标时间

			// 根据满标时间，查询借款申请
			List<LoanApplication> loanApplicationList = loanApplicationService.findAllFullLoanApplication(params);
			List<Long> reCreateAgreByLoanIdList = new ArrayList<Long>();// 存储要重新生成合同的loanId集合

			if (null != loanApplicationList && loanApplicationList.size() > 0) {
				for (LoanApplication loan : loanApplicationList) {

					// 根据列表，查询债权列表
					List<CreditorRights> creditorRightList = creditorRightsService.getByLoanApplicationId(loan.getLoanApplicationId());

					for (CreditorRights right : creditorRightList) {

						// 根据债权，查询出合同，并只提取一条
						List<AgreementInfo> resList = agreementInfoService.findAgreeListByCreditorRightsId(right.getCreditorRightsId(), AgreementStatusEnum.CREATED);

						for (AgreementInfo agreementInfo : resList) {
							// 根据物理地址，查找改文件是否存在
							File file = new File(agreementInfo.getStorgePath());
							if (!file.exists()) {
								if (!reCreateAgreByLoanIdList.contains(loan.getLoanApplicationId())) {
									reCreateAgreByLoanIdList.add(loan.getLoanApplicationId());
								}
							}
						}
					}
				}
			}

			// 如果不存在，将改借款申请的合同重新生成一遍
			Integer loanCount = 0;// 借款申请数量
			String loanIdStr = "";// 借款申请ID数组
			if (null != reCreateAgreByLoanIdList && reCreateAgreByLoanIdList.size() > 0) {
				loanCount = reCreateAgreByLoanIdList.size();
				for (Long loanId : reCreateAgreByLoanIdList) {
					loanIdStr = loanIdStr + loanId + ",";
					loanApplicationService.createAllAgreement(loanId);
				}
			}

			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("loanCount", loanCount);
			resultMap.put("loanIdStr", loanIdStr);
			return returnResultMap(true, resultMap, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return returnResultMap(false, null, null, e.getMessage());
		}
	}

    
    /**
     * 修复占比100%的转让数据
     * */
    @RequestMapping(value = "/repairRightsDetailData")
	@ResponseBody
    public Object repairRightsDetailData(){
    	try {
    		utilsService.repairRightsRepaymentDetailData();
        	return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
    }
    
    
    /**
     * 补发少发的奖励
     * */
    @RequestMapping(value = "/reissuedAwardsData")
	@ResponseBody
    public Object reissuedAwardsData(String mobileStr){
    	try {
    		utilsService.reissuedAwardByTime(mobileStr);
        	return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
    }
    
    /**
     * 手动录入白名单工具(EXCEL导入)
     * */
    @RequestMapping(value = "/importWhiteExcel")
	@ResponseBody
    public Object importWhiteExcel(MultipartFile importFile){
    	try {
    		utilsService.importToWhiteTabs(importFile);
        	return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
    }
    /**
     * 手动录入白名单工具(EXCEL导入)
     * */
    @RequestMapping(value = "/importInviteWhiteExcel")
    @ResponseBody
    public Object importInviteWhiteExcel(MultipartFile importFile){
    	try {
    		utilsService.importToInviteWhiteTabs(importFile);
    		return "success";
    	} catch (Exception e) {
    		e.printStackTrace();
    		return e.getMessage();
    	}
    }
    
    @RequestMapping(value = "/capitalOperate")
	@ResponseBody
    public Object capitalOperate(String accountId,String money,String operate,String desc){
    	try {
    		String result=utilsService.capitalOperate(Long.parseLong(accountId), new BigDecimal(money), operate, desc);
        	return result;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
    }
    
    /**
     * 三级分销手动生成订单对应的佣金
     * @param lendOrderIds  // split ( "," ) 
     * @return success/error
     * */
    @RequestMapping(value="/createCommiProfitTools")
    @ResponseBody
    public Object createCommiProfitTools(String lendOrderIds,Date ccpStartTime, Date ccpEndTime){
    	try{
    		utilsService.createCommiProfitTools(lendOrderIds,ccpStartTime,ccpEndTime);
    		return "success" ;
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("手动生成佣金失败，失败原因：",e);
    		return e.getMessage();
    	}
    }

    /**
     * 处理支付成功但处理失败的支付订单
     * @return
     */
    @RequestMapping(value = "/handleUndonePayOrder")
    @ResponseBody
    public Object handleUndonePayOrder() {
        try {
            this.utilsService.handleUndonePayOrder();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("处理支付成功但处理失败的支付订单：",e);
            return e.getMessage();
        }
    }
    
    /**
     * 参与白名单和邀请白名单通过手机号录入工具
     * 查询相关人员
     * @return
     */
    @RequestMapping(value = "/checkImportWhiteTabsByMobiles")
    @ResponseBody
    public Object checkImportWhiteTabsByMobiles(String importWhiteTabsMobiles) {
    	try {
    		Map<String,Object> rsMap = this.utilsService.checkAndImportWhiteTabsByMobiles(importWhiteTabsMobiles, '0');
    		return rsMap;
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("处理支付成功但处理失败的支付订单：",e);
    		return e.getMessage();
    	}
    }
    
    /**
     * 参与白名单和邀请白名单通过手机号录入工具
     * 确认之后执行导入
     * @return
     */
    @RequestMapping(value = "/importWhiteTabsByMobiles")
    @ResponseBody
    public Object importWhiteTabsByMobiles(String importWhiteTabsMobiles) {
    	try {
    		Map<String,Object> rsMap = this.utilsService.checkAndImportWhiteTabsByMobiles(importWhiteTabsMobiles, '1');
    		return rsMap;
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("处理支付成功但处理失败的支付订单：",e);
    		return e.getMessage();
    	}
    }
    
    /**
     * 批量生成省心计划合同@RequestParam(value = "oPass", required = false) String oPass,
     * @return
     */
    @RequestMapping(value = "/exeSXJHCreateAgreement")
    @ResponseBody
    public Object exeSXJHCreateAgreement(
    		@RequestParam(value = "ccpStartTime", required = false) Date ccpStartTime, 
    		@RequestParam(value = "ccpEndTime", required = false) Date ccpEndTime) {
    	try {
    		if(ccpStartTime != null && ccpEndTime != null
    		&& ccpEndTime.before(ccpStartTime)){
    			return "开始时间不能小于结束时间！" ;
    		}
    		this.utilsService.exeSXJHCreateAgreement(ccpStartTime , ccpEndTime);
    		return "success";
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("处理支付成功但处理失败的支付订单：",e);
    		return e.getMessage();
    	}
    }
    
    /**
     * 导入多级邀请关系表
     * */
    @RequestMapping(value = "/importMultilevelInvitationExcel")
	@ResponseBody
    public Object importMultilevelInvitationExcel(MultipartFile importFile){
    	try {
    		utilsService.importMultilevelInvitationExcel(importFile);
    		
        	return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
    }
    
}
