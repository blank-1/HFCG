package com.xt.cfp.core.service.impl;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.OpenAccount4ApiPersonalDataSource;
import com.external.deposites.model.datasource.RechargeDataSource;
import com.external.deposites.model.response.AbstractResponse;
import com.external.deposites.model.response.OpenAccount4PCPersonResponse;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.PayResult;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.financePlan.FinancePlanProcessModule;
import com.xt.cfp.core.service.financePlan.FinancePlanService;
import com.xt.cfp.core.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/5
 */
@Service
public class CgBizService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private IhfApi ihfApi;

    @Autowired
    private PayService payService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private FinancePlanService financePlanService;
    @Autowired
    private FinancePlanProcessModule financePlanProcessModule;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private CapitalFlowService capitalFlowService;

    /**
     * app个人 开户
     */
    @Transactional(rollbackFor = Exception.class)
    public void openAccount(OpenAccount4ApiPersonalDataSource dataSource, UserInfoExt currentUser) {
        try {
            userInfoService.updatePayPassword(null, dataSource.getPassword(), currentUser.getUserId());
            userInfoExtService.updateIdentityInfo(dataSource.getCertif_id(), dataSource.getCust_nm(), currentUser.getUserId(), dataSource.getCapAcntNo());
            customerCardService.changeUserCard(currentUser.getUserId(), dataSource.getCapAcntNo(), dataSource.getParent_bank_id(), dataSource.getBank_nm(), "");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获当前绑的卡
     */
    public CustomerCard findCurrentCard(Long userId) {
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(userId, PayConstants.PayChannel.HF);
        if (null == customerCard) {
            customerCard = customerCardService.getCustomerBindCardByUserId(userId, PayConstants.PayChannel.LL);
        }
        //如果连连没有卡，检查易宝是否有卡
        if (null == customerCard) {
            customerCard = customerCardService.getCustomerBindCardByUserId(userId, PayConstants.PayChannel.YB);
        }
        return customerCard;
    }

    /**
     * 查询个人、企业 银行支持
     */
    public CgBank getBankInfo(String bankCode, CgBank.IdTypeEnum idType) {
        CgBank params = new CgBank();
        params.setCode(bankCode);
        params.setIdType(idType.getCode());
        params.setType("product");
        params = myBatisDao.get("CG_BANK.getBankInfo", params);
        return params;
    }

    /**
     * app web 充值
     */
    @Transactional(rollbackFor = Exception.class)
    public RechargeDataSource preAppRecharge(Long rechargeAmount, UserInfo currentUser, CustomerCard card) throws HfApiException {
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(currentUser.getUserId());

        RechargeDataSource rechargeDataSource = new RechargeDataSource();
        rechargeDataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.app.personal.back_notify_url"));
        rechargeDataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.app.personal.page_notify_url"));
        rechargeDataSource.setAmt(new BigDecimal(rechargeAmount).multiply(new BigDecimal(100)).longValue());
        rechargeDataSource.setLogin_id(userInfoExt.getMobileNo());
        rechargeDataSource = ihfApi.appRecharge4personal(rechargeDataSource);

        rechargeOrderService.createHFRechargeRequest(new BigDecimal(rechargeAmount), currentUser, card,
                RechargeChannelEnum.HF_AUTHPAY, ClientEnum.from(currentUser.getAppSource()), null, rechargeDataSource.getMchnt_txn_ssn());

        return rechargeDataSource;
    }

    /**
     * TODO 报备用的数据记录
     */
    public void flowRecord() {

        logger.info("--------------------------------- 没有实现这个方法！！！！！！！！！");
    }

    public void recharge4Buy(AbstractResponse rechargeResponse) {
        RechargeOrder rechargeOrder = rechargeOrderService.confirmRecharge(rechargeResponse.getMchnt_txn_ssn(), "", RechargeStatus.SUCCESS.getValue());
        HfUtils.response(rechargeOrder.getStatus().equals(RechargeStatus.SUCCESS.getValue()));
        if (null != rechargeOrder.getPayId()) {
            PayResult payResult = payService.doPay(rechargeOrder.getPayId(), rechargeOrder.getResultTime());
            Map map = new HashMap();
            map.put("businessType", AccountConstants.BusinessTypeEnum.FEESTYPE_TOBUYFREEZEN.getValue());
            map.put("businessId", rechargeOrder.getPayId());
            Schedule sch = scheduleService.findByCondition(map).get(0);
            sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_WAITING.getValue()));
            //冻结充值金额
            CapitalFlow cap = new CapitalFlow();
            cap.setScheduleId(sch.getScheduleId());
            cap.setOperationType(Integer.parseInt(HFOperationEnum.FROZEN_PERSON_TO_PERSON.getValue()));
            cap.setFromUser(rechargeOrder.getUserId());
            cap.setAmount(rechargeOrder.getAmount());
            cap.setStartTime(new Date());
            cap.setBusinessId(rechargeOrder.getRechargeId());
            cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
            cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
            capitalFlowService.addCapital(cap);
            scheduleService.updateSchedule(sch);
            if (payResult.isPayResult() && payResult.isProcessResult()) {
                LendOrder lendOrder = lendOrderService.getLendOrderByPayId(
                        payResult.getPayId(), false);
                if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())) {
                    // 生成省息计划回款列表和合同
                    financePlanProcessModule.beginCalcInterest(lendOrder, new Date());
                    // 匹配
                    try {
                        financePlanService.match(lendOrder);
                    } catch (Exception e) {
                        logger.error("匹配省心计划失败，失败原因：", e);
                    }
                }
            }
        }
    }

    public void openAccount4PersonNotice(OpenAccount4PCPersonResponse response) {
        userInfoExtService.updateIdentityInfo(response.getCertif_id(), response.getCust_nm(), Long.valueOf(response.getUser_id_from()), response.getCapAcntNo());
        customerCardService.changeUserCard(Long.parseLong(response.getUser_id_from()), response.getCapAcntNo(), response.getParent_bank_id(), response.getBank_nm(), "");
    }

    public Pagination<UserInfoExt> oldUserOpenAccount(int page, int pageSize) {
        return userInfoExtService.getLLUser(null, page, pageSize);
    }
}
