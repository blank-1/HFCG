package com.xt.cfp.app.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.app.security.Des3;
import com.xt.cfp.app.vo.InviteUserVO;
import com.xt.cfp.app.vo.UserAccountHisVO;
import com.xt.cfp.app.vo.VouchVO;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.AccountConstants.AccountOperateEnum;
import com.xt.cfp.core.constants.AccountConstants.BusinessTypeEnum;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.constants.UserIsVerifiedEnum;
import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.constants.VoucherConstants;
import com.xt.cfp.core.constants.VoucherConstants.UsageScenario;
import com.xt.cfp.core.pojo.InvitationCode;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.UserAccountHis;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.pojo.ext.VoucherVO;
import com.xt.cfp.core.service.DefaultInterestDetailService;
import com.xt.cfp.core.service.LendOrderReceiveService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LoanFeesDetailService;
import com.xt.cfp.core.service.RepaymentPlanService;
import com.xt.cfp.core.service.SmsService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.VoucherService;
import com.xt.cfp.core.service.financePlan.FinancePlanProcessModule;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.MD5Util;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.core.util.TimeInterval;

@Controller
@RequestMapping(value = "/person")
public class PersonController extends BaseController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private LendOrderReceiveService lendOrderReceiveService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private LoanFeesDetailService loanFeesDetailService;
    @Autowired
    private DefaultInterestDetailService defaultInterestDetailService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private RedisCacheManger redisCacheManger;
    @Autowired
    private SmsService smsService;
    @Autowired
    private FinancePlanProcessModule financePlanProcessModule;
    /**
     * 【APP接口】
     * 资产概要接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/account/overview", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object accountOverview(HttpServletRequest request) {
        try {
        	//登录验证
    		UserInfo currentUser = getCurrentUser(request);
    		if(null == currentUser){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
    		
    		//待回本金（投标）
            BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(null, currentUser.getUserId(), LendProductTypeEnum.RIGHTING,LendProductTypeEnum.CREDITOR_RIGHTS);
            
            //待回收益（投标）
            BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(null, currentUser.getUserId(), LendProductTypeEnum.RIGHTING,LendProductTypeEnum.CREDITOR_RIGHTS);
            
            //冻结、可用资金
            UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
        	
            //已获收益
            BigDecimal totalProfit = lendOrderService.getAllProfit(currentUser.getUserId());
            BigDecimal totalAward = userAccountService.getUserTotalAward(currentUser.getUserId());
            
            //获取用户减少的奖励（如：取消）
            BigDecimal totalReduceAward = userAccountService.getUserTotalReduceAward(currentUser.getUserId());
            totalAward = totalAward.subtract(totalReduceAward);//实际得奖=励总的奖励-减少的奖励
            
            //可用的财富卷张数
            Map<String, Object> customParams = new HashMap<String, Object>();
            customParams.put("timeInterval", null);
            VoucherVO voucherVO = new VoucherVO();
            customParams.put("fstatus", VoucherConstants.VoucherFrontStatus.CAN_USAGE.getValue());//可用
            voucherVO.setUserId(currentUser.getUserId());
            Pagination<VoucherVO> voucherPaging = voucherService.getVoucherPaging(1, 999, voucherVO, customParams);
            
            // 返回结果
            DecimalFormat df = new DecimalFormat("#,##0.00");
            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("netAsset", df.format(getNetAsset(currentUser)));//净资产
            resultMap.put("capitalRecive", df.format(capitalRecive));//待回本金（投标）
            resultMap.put("frozeValue2", df.format(cashAccount.getFrozeValue2()));//已冻结
            resultMap.put("availValue2", df.format(cashAccount.getAvailValue2()));//可用资金
            resultMap.put("allProfit", df.format(totalProfit.add(totalAward)));//已获收益
            resultMap.put("voucherValue", String.valueOf(voucherPaging.getTotal()));//财富券张数
            resultMap.put("loginName", currentUser.getLoginName());//登录名
            resultMap.put("totalRecive", df.format(capitalRecive.add(interestRecive)));//待回资金=待回本金+待回收益
            
            return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
            logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }

    /**
     * 【APP接口工具】
     * 获取用户净资产
     * @param currentUser
     * @return
     */
    private BigDecimal getNetAsset(UserInfo currentUser) {
        //净资产=账户余额+（待回本金+待回利息+持有理财计划）-（待还本金+待还利息+待缴费用+待还罚息）
        UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
        //待回本金（投标）
        BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(null, currentUser.getUserId(), LendProductTypeEnum.RIGHTING,LendProductTypeEnum.CREDITOR_RIGHTS);
        //待回收益（投标）
        BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(null, currentUser.getUserId(), LendProductTypeEnum.RIGHTING,LendProductTypeEnum.CREDITOR_RIGHTS);
        
        //持有全部省心计划
        BigDecimal totalHoldFinancePlan = BigDecimal.ZERO;
        List<LendOrder> financeOrderList = lendOrderService.getFinancialPlanListByUserId(currentUser.getUserId());
        BigDecimal financeAccountTurnValue = userAccountService.getFinanceAccountPayValue(currentUser.getUserId());
        //用户省心计划已获但未回账户的奖励
        BigDecimal totalFinanceAward = BigDecimal.ZERO;
        for(LendOrder financeOrder : financeOrderList){
    		totalHoldFinancePlan = totalHoldFinancePlan.add(financeOrder.getBuyBalance()).add(financeOrder.getCurrentProfit());
			totalFinanceAward = userAccountService.getUserTotalFinanceAwardByLendOrderId(financeOrder.getLendOrderId());
        }
        //获取用户所有省心账户-计算省心账户子标所有扣费
  		BigDecimal financeFees = financePlanProcessModule.getAllFeesByUserId(currentUser.getUserId());
        //用户所有省心计划获取的奖励
        totalHoldFinancePlan = totalHoldFinancePlan.add(totalFinanceAward);
        totalHoldFinancePlan = totalHoldFinancePlan.subtract(financeAccountTurnValue).subtract(financeFees);
        //省心计划带回款利息
		BigDecimal waitInterest = lendOrderService.getFinancialWaitInterestByUserId(currentUser.getUserId());
		totalHoldFinancePlan = totalHoldFinancePlan.add(waitInterest);
		
        //待还本金
        BigDecimal replaymentCapital = repaymentPlanService.getRepaymentCapitalByUserId(currentUser.getUserId());
        //待还利息
        BigDecimal replaymentInterest = repaymentPlanService.getRepaymentInterestByUserId(currentUser.getUserId());
        //待缴费用
        BigDecimal loanFeeNopaied = loanFeesDetailService.getLoanFeeNoPaied(currentUser.getUserId());
        //待还罚息
        BigDecimal interestPaid = defaultInterestDetailService.getDefaultInterestByUserId(currentUser.getUserId());

        return cashAccount.getValue2().add(totalHoldFinancePlan).add(capitalRecive).add(interestRecive).subtract(replaymentCapital).subtract(replaymentInterest).subtract(loanFeeNopaied).subtract(interestPaid);
    }

    /**
     * 【APP接口】
     * 资金流水详情
     * @param request
     * @param hisId 记录ID值
     * @return
     */
    @RequestMapping(value = "/fundManageInfo")
    @ResponseBody
    public Object fundManageInfo(HttpServletRequest request, String hisId) {
    	try {
    		//登录验证
    		UserInfo currentUser = getCurrentUser(request);
    		if(null == currentUser){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
    		//参数验证
    		if(null == hisId || "".equals(hisId)){
    			return returnResultMap(false, null, "check", "参数不能为空");
    		}
    		//获取数据
    		UserAccountHis uah = userAccountService.getUserAccountHisById(Long.valueOf(hisId));
    		if(null == uah){
    			return returnResultMap(false, null, "check", "参数不合法");
    		}
    		// 返回结果
    		DecimalFormat df = new DecimalFormat("#,##0.00");
            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("hisId", uah.getHisId());//ID
    		resultMap.put("changeTime", DateUtil.getPlusTime(uah.getChangeTime()));//日期
    		resultMap.put("changeType", uah.getChangeType());//流水类型 1.收入 2.3.支出 4.冻结 5.解冻
    		resultMap.put("changeTypeStr", AccountOperateEnum.getAccountOperateByValue(uah.getChangeType()).getDesc());//流水类型中文显示
    		resultMap.put("busType", uah.getBusType());//费用类型
    		resultMap.put("busTypeStr", BusinessTypeEnum.getBusinessTypeByValue(uah.getBusType()).getDesc());//费用类型中文显示
    		resultMap.put("changeValue2", uah.getChangeValue2());//交易金额
    		//冻结/解冻 （4、5 值为交易金额）
    		if (AccountOperateEnum.FREEZE.getValue().equals(uah.getChangeType()) || AccountOperateEnum.UNFREEZE.getValue().equals(uah.getChangeType())) {
    			resultMap.put("frozenOrThaw", uah.getChangeValue2());
    			resultMap.put("changeValue2", "0.00");//如果流水类型，为冻结/解冻，则交易金额为0
			}else{
				if(AccountOperateEnum.INCOM.getValue().equals(uah.getChangeType())){//+收入
					resultMap.put("changeValue2", "+"+uah.getChangeValue2());//交易金额
				}
				if(AccountOperateEnum.PAY.getValue().equals(uah.getChangeType())){//-支出
					resultMap.put("changeValue2", "-"+uah.getChangeValue2());//交易金额
				}
				resultMap.put("frozenOrThaw", "0.00");//如果流水类型，不为冻结/解冻，冻结/解冻金额为0
			}
    		//账户余额
    		resultMap.put("valueAfter2", uah.getValueAfter2());
    		//备注 desc
    		resultMap.put("desc", uah.getDesc());
    		
    		return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e) {
 			e.printStackTrace();
 			logger.error(e.getMessage(),e);
 			return returnResultMap(false, null, "exception", e.getMessage());
 		}
    }
    
    /**
     * 【APP接口】
     * 资金流水列表
     * @param request
     * @param session
     * @param pageSize 每页条数
     * @param pageNo 第几页
     * @param flowType 流水类型
     * @return
     */
    @RequestMapping(value = "/fundManageList", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object fundManageList(HttpServletRequest request, HttpSession session,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(value = "flowType", required = false) String flowType) {
    	try {
    		//登录验证
    		UserInfo currentUser = getCurrentUser(request);
    		if(null == currentUser){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
            
    		//用户账户ID
    		UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
            
    		//流水类型
    		String[] flowTypeArray = null;
    		if(null != flowType && !"".equals(flowType)){
    			if(AccountOperateEnum.INCOM.getValue().equals(flowType)){
    				flowTypeArray = new String[]{"1"};
    			}else if (AccountOperateEnum.PAY.getValue().equals(flowType)) {
    				flowTypeArray = new String[]{"2","3"};
    			}else if (AccountOperateEnum.FREEZE.getValue().equals(flowType)) {
    				flowTypeArray = new String[]{"4"};
    			}else if (AccountOperateEnum.UNFREEZE.getValue().equals(flowType)) {
    				flowTypeArray = new String[]{"5"};
    			}
    		}else {
    			flowTypeArray = new String[]{"1","2","3","4","5"};
    		}
    		
    		//查询日期
    		String[] searchDateArray = new String[]{"t_7","t_1","t_6"};
    		Pagination<UserAccountHis> pagination = userAccountService.getUserAccountHisByAccId(pageNo, pageSize, cashAccount.getAccId(), flowTypeArray, searchDateArray, AccountConstants.VisiableEnum.DISPLAY);
    		
    		// 返回结果
    		DecimalFormat df = new DecimalFormat("#,##0.00");
            Map<String,Object> resultMap = new HashMap<String,Object>();
            List<UserAccountHisVO> hisVOs = new ArrayList<>();
            UserAccountHisVO hisVO = null;
            
            List<UserAccountHis> uahList = pagination.getRows();
            if(null != uahList && uahList.size() > 0){
            	for (UserAccountHis uah : uahList) {
            		hisVO = new UserAccountHisVO();
            		
            		hisVO.setHisId(String.valueOf(uah.getHisId()));

            		//日期changeTime
            		hisVO.setChangeTime(DateUtil.getPlusTime(uah.getChangeTime()));

            		//流水类型changeType  1.收入 2.3.支出 4.冻结 5.解冻
            		hisVO.setChangeType(uah.getChangeType());
            		
            		//流水类型中文显示
            		hisVO.setChangeTypeStr(AccountOperateEnum.getAccountOperateByValue(uah.getChangeType()).getDesc());
            		
            		//费用类型busType
            		hisVO.setBusType(uah.getBusType());

            		//交易金额changeValue2
            		hisVO.setChangeValue2(df.format(uah.getChangeValue2()));

            		//冻结/解冻 （changeType =4=5 值为交易金额）
            		if (AccountOperateEnum.FREEZE.getValue().equals(uah.getChangeType()) || AccountOperateEnum.UNFREEZE.getValue().equals(uah.getChangeType())) {
            			hisVO.setFrozenOrThaw(df.format(uah.getChangeValue2()));
//            			hisVO.setChangeValue2("0.00");//如果流水类型，为冻结/解冻，则交易金额为0
        			}else{
        				hisVO.setFrozenOrThaw("0.00");//如果流水类型，不为冻结/解冻，冻结/解冻金额为0
        			}

            		//账户余额valueAfter2
            		hisVO.setValueAfter2(df.format(uah.getValueAfter2()));

            		//备注 desc
            		hisVO.setDesc(uah.getDesc());
            		
            		
            		hisVOs.add(hisVO);
				}
            }
            
            resultMap.put("pageSize", pagination.getPageSize());
            resultMap.put("pageNo", pagination.getCurrentPage());
            resultMap.put("total", pagination.getTotal());
            resultMap.put("rows", hisVOs);
            return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e) {
 			e.printStackTrace();
 			logger.error(e.getMessage(),e);
 			return returnResultMap(false, null, "exception", e.getMessage());
 		}
    }

    /**
     * 【APP接口】
     * 获取邀请码信息
     * @return
     */
    @RequestMapping(value = "/to_invite_friends", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object to_invite_friends(HttpServletRequest request) {
    	try {
    		//登录验证
    		UserInfo currentUser = getCurrentUser(request);
    		if(null == currentUser){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
    		
    		//查询用户言邀请码
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", currentUser.getUserId());
            param.put("type", 0);
            InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
            
            //读取share json文件
            String shareContent = "";
			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/share.json";
			JSONObject jsonObject = this.getJSONObjectByJson(path);
			if(null != jsonObject){
				shareContent = jsonObject.getString("shareContent");
			}
            
            //返回结果
            Map<String,String> resultMap = new HashMap<String,String>();
            if (invite != null) {
                String invitecode = invite.getInvitationCode();
                resultMap.put("invitecode", invitecode);
                resultMap.put("loginName", currentUser.getLoginName());
                resultMap.put("inviteURL", this.getM_BASEPATH(request) + "/person/beinvite?invite_code=" + invitecode);
                resultMap.put("shareContent", shareContent);
            }
            return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e) {
 			e.printStackTrace();
 			logger.error(e.getMessage(),e);
 			return returnResultMap(false, null, "exception", e.getMessage());
 		}
    }

    /**
     * 【APP接口】
     * 获取邀请好友list
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/invite_friends_list", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object invite_friends_list(HttpServletRequest request,
    				@RequestParam(value = "pageSize", defaultValue = "999") int pageSize,
                    @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
    	try {
    		//登录验证
    		UserInfo currentUser = getCurrentUser(request);
    		if(null == currentUser){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
    		
    		//获取数据
            Pagination<UserInfoVO> pagination = userInfoExtService.getUserInviteFriends(pageNo, pageSize, currentUser.getUserId());
            
            //返回结果
            Map<String,Object> resultMap = new HashMap<String,Object>();
            List<InviteUserVO> inviteUserVOs = new ArrayList<InviteUserVO>();
            if(null != pagination){
            	List<UserInfoVO> inviteUserList = pagination.getRows();
            	InviteUserVO inviteUserVO = null; 
            	if(null != inviteUserList && inviteUserList.size() > 0){
            		for (UserInfoVO vo : inviteUserList) {
            			inviteUserVO = new InviteUserVO();
            			inviteUserVO.setLoginName(vo.getLoginName());//用户名
            			inviteUserVO.setCreateTime(DateUtil.getDateLong(vo.getCreateTime()));//注册时间
            			inviteUserVO.setInviteAction("链接邀请");//邀请方式
            			inviteUserVO.setPlatformAward("--");//平台奖励
            			inviteUserVOs.add(inviteUserVO);
					}
            	}
            }
            resultMap.put("rows", inviteUserVOs);
            return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e) {
 			e.printStackTrace();
 			logger.error(e.getMessage(),e);
 			return returnResultMap(false, null, "exception", e.getMessage());
 		}
    }

    /**
     * 【APP接口】
     * 身份证认证
     * @param request
     * @param session
     * @param idCard 身份证号
     * @param trueName 真实姓名
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "/identityAuthentication", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object identityAuthentication(HttpServletRequest request, HttpSession session,
                                         @RequestParam(value = "idCard") String idCard,
                                         @RequestParam(value = "trueName") String trueName) {

    	try {
    		
    		//解密处理
    		idCard = Des3.decode(idCard);
    		trueName = Des3.decode(trueName);
    		
    		// 验证操作
    		UserInfo user = getCurrentUser(request);
    		if(null == user){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
    		if ("".equals(idCard) || idCard == null) {
                return returnResultMap(false, null, "check", "身份证为空");
            }
            if ("".equals(trueName) || trueName == null) {
                return returnResultMap(false, null, "check", "用户名字为空");
            }
            if (!idCardValidate(idCard)) {
                return returnResultMap(false, null, "check", "身份证格式不正确");
            }
            
            int userExist = userInfoExtService.identityBindingExist(idCard, trueName);
    		if(userExist > 0){
    			return returnResultMap(false, null, "check", "该身份证已被占用");
    		}
    		
    		//验证是否已经认证过
            UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(user.getUserId());
            if(UserIsVerifiedEnum.YES.getValue().equals(userInfoExt.getIsVerified())){
            	return returnResultMap(false, null, "check", "当前用户已经认证过");
            }
    		
            //认证操作
            boolean resultFlag = userInfoExtService.identityAuthentication(idCard, trueName, user.getUserId());
            if (resultFlag) {
            	Map<String,Object> resultMap = new HashMap<String,Object>();
                resultMap.put("realName", trueName);
                resultMap.put("idCardNo", StringUtils.getEncryptIdCard(idCard));
                return returnResultMap(true, resultMap, null, null);
            }
            return returnResultMap(false, null, "check", "身份证和姓名验证失败");
		} catch (SystemException se) {
			logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
		}
    }

    /**
     * 【APP接口】
     * 获取用户个人信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/singlePersonInformation", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object singlePersonInformation(HttpServletRequest request){
    	try {
    		//登录验证
    		UserInfo currentUser = getCurrentUser(request);
    		if(null == currentUser){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
    		
    		// 获取用户详细信息
        	UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        	
        	// 返回结果
            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("loginName", userExt.getLoginName());//用户名
            resultMap.put("realName", userExt.getRealName());//姓名
            resultMap.put("idCardNo", userExt.getEncryptIdCardNo());//身份证号(打码后)
            resultMap.put("mobileNo", userExt.getEncryptMobileNo());//手机号(打码后)
            
            return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
    
    /**
     * 【APP接口】
     * 财富券列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/voucherList", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object voucherList(HttpServletRequest request,
                              @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                              @RequestParam(value = "startDate", defaultValue = "", required = false) String startDate,
                              @RequestParam(value = "endDate" ,defaultValue = "", required = false) String endDate,
                              @RequestParam(value = "state", defaultValue = "-1") String state) {
    	try {
    		//登录验证
    		UserInfo currentUser = getCurrentUser(request);
    		if(null == currentUser){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
    		
            //封装参数
            TimeInterval timeInterval = new TimeInterval(// 日期间隔
                    "".equals(startDate) ? null : DateUtil.parseStrToDate(
                            startDate, "yyyy-MM-dd"),
                    "".equals(endDate) ? null : DateUtil.parseStrToDate(
                            endDate, "yyyy-MM-dd"));


            Map<String, Object> customParams = new HashMap<String, Object>();
            customParams.put("timeInterval", timeInterval);
            //财富券
            VoucherVO voucherVO = new VoucherVO();
            switch (state){
                case "-1":
                    break;
                case "0":
                    //可用
                    customParams.put("fstatus", VoucherConstants.VoucherFrontStatus.CAN_USAGE.getValue());
                    break;
                case "1":
                    //不可用
                    customParams.put("fstatus", VoucherConstants.VoucherFrontStatus.CAN_NOT_USAGE.getValue());
                    break;
            }
            voucherVO.setUserId(currentUser.getUserId());
            Pagination<VoucherVO> pagination = voucherService.getVoucherPaging(pageNo, pageSize, voucherVO, customParams);
            
            //返回结果
            Map<String,Object> resultMap = new HashMap<String,Object>();
            List<VouchVO> vouchVOs = new ArrayList<>();
            VouchVO vouch = null;
            
            List<VoucherVO> vvList = pagination.getRows();
            if(null != vvList && vvList.size() > 0){
            	for (VoucherVO vv : vvList) {
            		vouch = new VouchVO();
            		
            		vouch.setVoucherId(vv.getVoucherId().toString());//ID
            		vouch.setVoucherName(vv.getVoucherName());//财富券名称
            		vouch.setAmount(vv.getAmount().toString());//面额
            		vouch.setCreateDate(DateUtil.getDateLong(vv.getCreateDate()));//获取时间
            		vouch.setEndDate(DateUtil.getDateLong(vv.getEndDate()));//到期时间
            		vouch.setSourceStr(vv.getSourceStr());//来源描述
            		vouch.setStatus(vv.getStatus());//状态编号（0可用，1不可用）
            		if(UsageScenario.WITHDRAW.getValue().equals(vv.getUsageScenario())){//如果为提现专用
            			vouch.setRemark("抵扣提现手续费");//描述
            		}else {
            			vouch.setRemark(vv.getVoucherRemark());//描述
					}
            		vouch.setUsageScenario(vv.getUsageScenario());//使用场景编号（0全业务，1投标使用，2购买理财计划使用，3提现专用）
            		vouch.setDetailRemark(vv.getDetailRemark()==null?"":vv.getDetailRemark());//不可用说明
            		vouchVOs.add(vouch);
				}
            }
            
            resultMap.put("pageSize", pagination.getPageSize());
            resultMap.put("pageNo", pagination.getCurrentPage());
            resultMap.put("total", pagination.getTotal());
            resultMap.put("rows", vouchVOs);
            return returnResultMap(true, resultMap, null, null);
    		
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
    
    /**
     * 【APP接口】
     * 执行：【重置密码】发送短信
     * @param request
     * @param pass_type 类型
     * @param mobile_no 手机号
     * @return
     */
    @RequestMapping(value="/forgetPswFinal_sendMsg", method=RequestMethod.POST)
    @ResponseBody
    public Object forgetPswFinal_sendMsg(HttpServletRequest request,
    		@RequestParam(value = "pass_type", required = false) String pass_type,
    		@RequestParam(value = "mobile_no", required = false) String mobile_no) {
        try {
        	if(null == pass_type || "".equals(pass_type)){
        		return returnResultMap(false, null, "check", "缺少参数，非法请求");
        	}else if (!"nologin".equals(pass_type) && !"login".equals(pass_type) && !"trans".equals(pass_type)) {
        		return returnResultMap(false, null, "check", "缺少参数，非法请求");
			}
            
            if("nologin".equals(pass_type)){
            	if(null == mobile_no || "".equals(mobile_no)){
            		return returnResultMap(false, null, "check", "手机号码不能为空");
            	}
                if(!mobileNoValidate(mobile_no)){
                	return returnResultMap(false, null, "check", "手机号码格式不正确");
                }
            	UserInfo userInfo = userInfoService.getUserByMobileNo(mobile_no);
            	if(null == userInfo){
            		return returnResultMap(false, null, "check", "手机号码不存在");
            	}
            	userInfoService.sendRetrievePassword(userInfo.getMobileNo());//找回登录密码，短信发送
            }else {
            	UserInfo currentUser = getCurrentUser(request);
        		if(null == currentUser){
        			return returnResultMap(false, null, "needlogin", "请先登录");
        		}
            	UserInfo userInfo = userInfoService.getUserByUserId(currentUser.getUserId());
            	if ("login".equals(pass_type)) {
            		userInfoService.sendRetrievePassword(userInfo.getMobileNo());//找回登录密码，短信发送
    			}else if ("trans".equals(pass_type)) {
    				userInfoService.sendResetTradePassMsg(userInfo.getMobileNo());//找回交易密码，短信发送
    			}
			}
            return returnResultMap(true, null, null, null);
        } catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
    
    /**
     * 【APP接口】
     * 执行：【重置密码】验证短信
     * @param request
     * @param pass_type 类型
     * @param mobile_no 手机号
     * @param checkCode 验证码
     * @return
     */
    @RequestMapping(value="/forgetPswFinal_checkMsg", method=RequestMethod.POST)
    @ResponseBody
    public Object forgetPswFinal_checkMsg(HttpServletRequest request,
    		@RequestParam(value = "pass_type", required = false) String pass_type,
    		@RequestParam(value = "mobile_no", required = false) String mobile_no,
    		@RequestParam(value = "checkCode", required = false) String checkCode) {
        try {
        	if(null == pass_type || "".equals(pass_type)){
        		return returnResultMap(false, null, "check", "缺少参数，非法请求");
        	}else if (!"nologin".equals(pass_type) && !"login".equals(pass_type) && !"trans".equals(pass_type)) {
        		return returnResultMap(false, null, "check", "缺少参数，非法请求");
			}
        	if(null == checkCode || "".equals(checkCode)){
 				return returnResultMap(false, null, "check", "验证码不能为空");
 			}
        	if(checkCode.length() != 6 || !StringUtils.isPattern("^([0-9]+)$", checkCode)){
				return returnResultMap(false, null, "check", "验证码格式错误");
			}
        	
        	Map<String,Object> resultMap = new HashMap<String,Object>();
        	String isVerified = UserIsVerifiedEnum.NO.getValue();//是否实名认证
        	if("nologin".equals(pass_type)){
        		if(null == mobile_no || "".equals(mobile_no)){
            		return returnResultMap(false, null, "check", "手机号码不能为空");
            	}
                if(!mobileNoValidate(mobile_no)){
                	return returnResultMap(false, null, "check", "手机号码格式不正确");
                }
                UserInfo userInfo = userInfoService.getUserByMobileNo(mobile_no);
            	if(null == userInfo){
            		return returnResultMap(false, null, "check", "手机号码不存在");
            	}
                boolean checkResult = this.checkMsgNum(mobile_no);//记录短信校验次数
                if(!checkResult){
                	return returnResultMap(false, null, "check", "验证次数过多，请24小时后再操作");
                }
        		boolean bool = smsService.validateMsg(mobile_no, checkCode, TemplateType.SMS_RETRIEVEPASSWORD_VM, true);
        		if(!bool){
        			return returnResultMap(false, null, "check", "验证码不正确");
        		}
        		this.destroyCheckMsgNum(mobile_no);//销毁短信校验次数记录
        		
        		//获取是否实名认证信息
        		UserInfoExt userExt = userInfoExtService.getUserInfoExtById(userInfo.getUserId());
            	if(UserIsVerifiedEnum.YES.getValue().equals(userExt.getIsVerified())){
            		isVerified = UserIsVerifiedEnum.YES.getValue();
                }
            	//存dtoken
        		resultMap.put("dtoken", Des3.encode(mobile_no));
        	}else {
        		UserInfo currentUser = getCurrentUser(request);
        		if(null == currentUser){
        			return returnResultMap(false, null, "needlogin", "请先登录");
        		}
        		UserInfo userInfo = userInfoService.getUserByUserId(currentUser.getUserId());
        		boolean checkResult = this.checkMsgNum(userInfo.getMobileNo());//记录短信校验次数
                if(!checkResult){
                	return returnResultMap(false, null, "check", "验证次数过多，请明日再操作");
                }
        		if ("login".equals(pass_type)) {
            		boolean bool = smsService.validateMsg(userInfo.getMobileNo(), checkCode, TemplateType.SMS_RETRIEVEPASSWORD_VM, true);
                    if(!bool){
                    	return returnResultMap(false, null, "check", "验证码不正确");
                    }
    			}else if ("trans".equals(pass_type)) {
    				boolean bool = smsService.validateMsg(userInfo.getMobileNo(), checkCode, TemplateType.SMS_RESETTRADEPASSMSG_VM, true);
    	            if(!bool){
    	            	return returnResultMap(false, null, "check", "验证码不正确");
    	            }
    			}
        		this.destroyCheckMsgNum(userInfo.getMobileNo());//销毁短信校验次数记录
        		
        		//获取是否实名认证信息
        		UserInfoExt userExt = userInfoExtService.getUserInfoExtById(userInfo.getUserId());
            	if(UserIsVerifiedEnum.YES.getValue().equals(userExt.getIsVerified())){
            		isVerified = UserIsVerifiedEnum.YES.getValue();
                }
			}
        	resultMap.put("isVerified", isVerified);
        	return returnResultMap(true, resultMap, null, null);
        } catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
    
    /**
     * 【工具】记录短信校验次数
     */
    private boolean checkMsgNum(String mobile_no) throws ParseException{
    	int seconds = DateUtil.getLeftTimesToday();//当天剩余时间，秒
    	Integer num = 0;
    	String numStr = redisCacheManger.getRedisCacheInfo("app_forgetPsw_" + mobile_no);
    	if(null != numStr){
    		num = Integer.valueOf(numStr);
    	}
    	num = num + 1;
    	redisCacheManger.setRedisCacheInfo("app_forgetPsw_" + mobile_no, String.valueOf(num), seconds);//key=mobile_no;value=checkMsgNum
    	if(num >= 10){
    		return false;
    	}
    	return true;
    }
    
    /**
     * 【工具】销毁短信校验次数记录
     */
    private void destroyCheckMsgNum(String mobile_no){
    	redisCacheManger.destroyRedisCacheInfo("app_forgetPsw_" + mobile_no);
    }
    
    /**
     * 【APP接口】
     * 执行：【重置密码】身份验证
     * @param request
     * @param pass_type 类型
     * @param idcard 身份证号
     * @param mobile_no 手机号
     * @param dtoken 密钥
     * @return
     */
    @RequestMapping(value = "/identityProving_authentication", method=RequestMethod.POST)
    @ResponseBody
    public Object identityProving_authentication(HttpServletRequest request,
    		@RequestParam(value = "pass_type", required = false) String pass_type,
    		@RequestParam(value = "idcard", required = false) String idcard,
    		@RequestParam(value = "mobile_no", required = false) String mobile_no,
    		@RequestParam(value = "dtoken", required = false) String dtoken){
        try {
        	if(null == pass_type || "".equals(pass_type)){
        		return returnResultMap(false, null, "check", "缺少参数，非法请求");
        	}else if (!"nologin".equals(pass_type) && !"login".equals(pass_type) && !"trans".equals(pass_type)) {
        		return returnResultMap(false, null, "check", "缺少参数，非法请求");
			}
        	if (null == idcard || "".equals(idcard)) {
                return returnResultMap(false, null, "check", "身份证为空");
            }
            if (!idCardValidate(idcard)) {
                return returnResultMap(false, null, "check", "身份证格式不正确");
            }
            
            if("nologin".equals(pass_type)){
            	if(null == mobile_no || "".equals(mobile_no)){
            		return returnResultMap(false, null, "check", "手机号码不能为空");
            	}
                if(!mobileNoValidate(mobile_no)){
                	return returnResultMap(false, null, "check", "手机号码格式不正确");
                }
                //dtoken【开始】
    			try {
    				if(null == dtoken || "".equals(dtoken)){
    					throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
    				}
    				String des3_mobile_no = Des3.encode(mobile_no);
    				if(!des3_mobile_no.equals(dtoken)){
    					throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
    				}
    			} catch (Exception e) {
    				throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
    			}
    			//dtoken【结束】
            	UserInfo userInfo = userInfoService.getUserByMobileNo(mobile_no);
            	if(null == userInfo){
            		return returnResultMap(false, null, "check", "手机号码不存在");
            	}
            	UserInfoExt userExt = userInfoExtService.getUserInfoExtById(userInfo.getUserId());
            	if(!UserIsVerifiedEnum.YES.getValue().equals(userExt.getIsVerified())){
                	return returnResultMap(false, null, "check", "还未身份认证");
                }
            	if(!userExt.getIdCard().equals(idcard)){
                	return returnResultMap(false, null, "check", "身份证号码与认证的不符");
                }
            }else {
            	UserInfo currentUser = getCurrentUser(request);
        		if(null == currentUser){
        			return returnResultMap(false, null, "needlogin", "请先登录");
        		}
                UserInfoExt userExt = userInfoExtService.getUserInfoExtById(currentUser.getUserId());
                if(!UserIsVerifiedEnum.YES.getValue().equals(userExt.getIsVerified())){
                	return returnResultMap(false, null, "check", "还未身份认证");
                }
            	if(!userExt.getIdCard().equals(idcard)){
                	return returnResultMap(false, null, "check", "身份证号码与认证的不符");
                }
			}
            return returnResultMap(true, null, null, null);
		} catch(SystemException se) {
			logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
		}
    }
    
    /**
     * 【APP接口】
     * 执行：【重置密码】保存新密码
     * @param request
     * @param pass_type 类型
     * @param mobile_no 手机号
     * @param dtoken 密钥
     * @param newPass 新密码
     * @return
     */
    @RequestMapping(value="/installNewPassword_savePass", method=RequestMethod.POST)
    @ResponseBody
    public Object installNewPassword_savePass(HttpServletRequest request,
    		@RequestParam(value = "pass_type", required = false) String pass_type,
    		@RequestParam(value = "mobile_no", required = false) String mobile_no,
    		@RequestParam(value = "dtoken", required = false) String dtoken,
            @RequestParam(value = "newPass", required = false) String newPass) {
        try {
        	if(null == pass_type || "".equals(pass_type)){
        		return returnResultMap(false, null, "check", "缺少参数，非法请求");
        	}else if (!"nologin".equals(pass_type) && !"login".equals(pass_type) && !"trans".equals(pass_type)) {
        		return returnResultMap(false, null, "check", "缺少参数，非法请求");
			}
            if (null == newPass || "".equals(newPass)) {
                return returnResultMap(false, null, "check", "密码不能为空");
            }
            if (!passwordValidate(newPass)) {
                return returnResultMap(false, null, "check", "密码为6-16位字符，支持字母及数字,字母区分大小写");
            }
            
            if("nologin".equals(pass_type)){
            	if(null == mobile_no || "".equals(mobile_no)){
            		return returnResultMap(false, null, "check", "手机号码不能为空");
            	}
                if(!mobileNoValidate(mobile_no)){
                	return returnResultMap(false, null, "check", "手机号码格式不正确");
                }
                
                //dtoken【开始】
        		try {
        			if(null == mobile_no || "".equals(mobile_no) || null == dtoken || "".equals(dtoken)){
        				throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
        			}
        			String des3_mobile_no = Des3.encode(mobile_no);
        			if(!des3_mobile_no.equals(dtoken)){
        				throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
        			}
        		} catch (Exception e) {
        			throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
        		}
        		//dtoken【结束】
                
            	UserInfo userInfo = userInfoService.getUserByMobileNo(mobile_no);
            	if(null == userInfo){
            		return returnResultMap(false, null, "check", "手机号码不存在");
            	}
            	userInfo = userInfoService.getUserByUserId(userInfo.getUserId());
            	String md5_login_new = MD5Util.MD5Encode(newPass.trim(), "utf-8");
                userInfo.setLoginPass(md5_login_new);
                userInfoService.updateUser(userInfo);
            }else{
            	UserInfo currentUser = getCurrentUser(request);
        		if(null == currentUser){
        			return returnResultMap(false, null, "needlogin", "请先登录");
        		}
                UserInfo userInfo = userInfoService.getUserByUserId(currentUser.getUserId());
            	if ("login".equals(pass_type)) {
            		String md5_login_new = MD5Util.MD5Encode(newPass.trim(), "utf-8");
                    userInfo.setLoginPass(md5_login_new);
    			}else if ("trans".equals(pass_type)) {
    				String md5_trade_new = MD5Util.MD5Encode(newPass.trim(), "utf-8");
                    userInfo.setBidPass(md5_trade_new);
    			}
                userInfoService.updateUser(userInfo);
            }
            
            return returnResultMap(true, null, null, null);
        } catch(SystemException se) {
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
    
    /**
     * 【APP接口】
     * 执行：修改用户名信息
     * @param request
     * @param username 登录名
     * @return
     */
    @RequestMapping(value="/saveLoginName", method=RequestMethod.POST)
    @ResponseBody
    public Object saveLoginName(HttpServletRequest request,
    		@RequestParam(value = "username", required = false) String username) {
        try {
            // 合法性验证
            if (null == username || "".equals(username)) {
                return returnResultMap(false, null, "check", "用户名不能为空");
            } else if (!loginNameValidate(username)) {
                return returnResultMap(false, null, "check", "用户名含非法字符");
            }

            // 获取当前登录用户
            UserInfo currentUser = getCurrentUser(request);
    		if(null == currentUser){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
            // 根据用户ID，加载一条数据
            UserInfo userInfo = userInfoService.getUserByUserId(currentUser.getUserId());

            // 判断用户名是否已经存在，非本人
            UserInfo info = userInfoService.getUserByLoginName(username);
            if (null != info && !userInfo.getLoginName().equals(info.getLoginName())) {
                return returnResultMap(false, null, "check", "用户名已经存在");
            }

            // 赋值
            userInfo.setLoginName(username);

            // 更改操作
            userInfoService.updateUser(userInfo);
            return returnResultMap(true, null, null, null);
        } catch(SystemException se) {
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
    
    /**
     * 【APP接口】
     * 执行：修改手机号，发送验证码
     * @param request
     * @param mobileNo 手机号
     * @return
     */
    @RequestMapping(value = "/mobileAuthentication", method=RequestMethod.POST)
    @ResponseBody
    public Object mobileAuthentication(HttpServletRequest request,
    		@RequestParam(value = "mobileNo", required = false) String mobileNo) {
        try {
            // 验证手机号格式
            if (!mobileNoValidate(mobileNo)) {
            	return returnResultMap(false, null, "check", "手机号码格式错误");
            }
            // 获取当前登录用户
            UserInfo currentUser = getCurrentUser(request);
    		if(null == currentUser){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
    		// 验证手机号
            boolean mobileExist = userInfoService.isMobileExist(mobileNo);
            if (mobileExist) {
            	return returnResultMap(false, null, "check", "与原手机号相同或手机号已被使用");
            }
            // 发送验证码
            userInfoService.sendmobileAuthenticationMsg(mobileNo);
            return returnResultMap(true, null, null, null);
        } catch(SystemException se) {
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
    
    /**
     * 【APP接口】
     * 执行：修改手机号， 校验验证码，并保存新手机号
     * @param request
     * @param validMsg 短信验证码
     * @param mobileNo 手机号
     * @return
     */
    @RequestMapping(value = "/mobileAuthentValidate", method=RequestMethod.POST)
    @ResponseBody
    public Object mobileAuthentValidate(HttpServletRequest request, 
    		@RequestParam(value = "validMsg", required = false) String validMsg,
    		@RequestParam(value = "mobileNo", required = false) String mobileNo,
    		@RequestParam(value = "userSource", required = false) String userSource) {
    	try {
    		// 校验短信
            boolean bool = smsService.validateMsg(mobileNo, validMsg, TemplateType.SMS_MOBILEAUTHENTICATION_VM,true);
            if (bool) {
            	// 获取当前登录用户
            	UserInfo currentUser = getCurrentUser(request);
        		if(null == currentUser){
        			return returnResultMap(false, null, "needlogin", "请先登录");
        		}
        		// 验证手机号
                boolean mobileExist = userInfoService.isMobileExist(mobileNo);
                if (mobileExist) {
                	return returnResultMap(false, null, "check", "与原手机号相同或手机号已被使用");
                }
                //验证用户来源
                if(null == userSource || "".equals(userSource)){
                	return returnResultMap(false, null, "check", "用户来源不能为空");
                }
                //用户来源
                UserSource source = UserSource.ISO;
                if(UserSource.ANDROID.getValue().equals(userSource)){
                	source = UserSource.ANDROID;
                }
                //修改手机验证
                currentUser = userInfoService.mobileAuthentValidate(currentUser, mobileNo, source);
                return returnResultMap(true, null, null, null);
            } else {
            	return returnResultMap(false, null, "check", UserErrorCode.VALIDATE_CODE_ERROR.getDesc());
            }
        } catch(SystemException se) {
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
    
}
