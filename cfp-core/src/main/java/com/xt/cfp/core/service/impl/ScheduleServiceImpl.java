package com.xt.cfp.core.service.impl;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.exception.UnimplementException;
import com.external.deposites.model.datasource.*;
import com.external.deposites.model.response.AbstractResponse;
import com.external.deposites.model.response.QueryRechargeResponseItem;
import com.external.deposites.model.response.QueryRechargeWithdrawResponse;
import com.external.deposites.service.DefaultApiService;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.HFOperationEnum;
import com.xt.cfp.core.constants.RechargeStatus;
import com.xt.cfp.core.constants.ScheduleEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CapitalFlow;
import com.xt.cfp.core.pojo.RechargeOrder;
import com.xt.cfp.core.pojo.Schedule;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.service.CapitalFlowService;
import com.xt.cfp.core.service.RechargeOrderService;
import com.xt.cfp.core.service.ScheduleService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private DefaultApiService defaultApiService;
    @Autowired
    private CapitalFlowService capitalFlowService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private IhfApi ihfApi;

    @Override
    public List<Schedule> findByCondition(Map map) {
        return myBatisDao.getList("SCHEDULE.findByCondition", map);
    }

    @Transactional
    @Override
    public Schedule addSchedule(Schedule schedule) {
        myBatisDao.insert("SCHEDULE.insert", schedule);
        return schedule;
    }

    @Transactional
    @Override
    public Schedule updateSchedule(Schedule schedule) {
        myBatisDao.update("SCHEDULE.updateByPrimaryKeySelective", schedule);
        return schedule;
    }

    @Override
    public void doTask() {
        Map map = new HashMap();
        map.put("status", ScheduleEnum.BUSINESS_WAITING.getValue());
        map.put("num", 10);
        List<Schedule> list = findByCondition(map);
        if (null != list && list.size() > 0) {
            boolean flag = false;
            for (Schedule sch : list) {
                map.clear();
                map.put("scheduleId", sch.getScheduleId());
                map.put("result", ScheduleEnum.RESULT_WAITING.getValue());
                List<CapitalFlow> caps = capitalFlowService.findByCondition(map);
                for (CapitalFlow cap : caps) {
                    AbstractDataSource source = combinationSource(cap);
                    try {
                        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_DOING.getValue()));
                        capitalFlowService.updateCapital(cap);
                        AbstractResponse response = chooseApi(cap.getOperationType(), source);
                        sch.setEndTime(new Date());
                        if (response.isSuccess()) {
                            cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_SUCCESS.getValue()));
                        } else {
                            cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_FAILD.getValue()));
                            flag = true;
                        }
                        cap.setMessage(response.getResp_code() + "|" + response.getResp_desc());
                        cap.setEndTime(new Date());
                        capitalFlowService.updateCapital(cap);
                        logger.info("流水单ID=" + cap.getFlowId() + "结果描述=" + response.getResp_desc());
                    } catch (HfApiException e) {
                        e.printStackTrace();
                        logger.error("流水单ID=" + cap.getFlowId() + "错误原因=" + e.getMessage());
                        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_FAILD.getValue()));
                        cap.setMessage(e.getMessage());
                        cap.setEndTime(new Date());
                        capitalFlowService.updateCapital(cap);
                        flag = true;
                    }
                }
                if (flag) {
                    if (caps.size() > 1) {
                        sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_PART_SUCCESS.getValue()));
                    } else {
                        sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_FAILD.getValue()));
                    }

                } else {
                    sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_SUCCESS.getValue()));
                }
                updateSchedule(sch);
            }
        }
    }

    /**
     * 根据对应CapitalFlow 的 OperationType 生成对应的datasource
     */
    private AbstractDataSource combinationSource(CapitalFlow cap) {
        String toUserMobile = "";
        String fromUserMobile = "";
        if (cap.getToUser() != null) {
            toUserMobile = getUserBankMobile(cap.getToUser());
        }
        if (cap.getFromUser() != null) {
            fromUserMobile = getUserBankMobile(cap.getFromUser());
        }

        long amt = cap.getAmount().multiply(new BigDecimal(100)).longValue();

        switch (HFOperationEnum.typeOf(cap.getOperationType())) {
            case FROZEN_PERSON_TO_PERSON:
            case FROZEN_PERSON_TO_COMPANY: {
                FreezeDataSource dataSource = new FreezeDataSource();
                dataSource.setAmt(amt);
                dataSource.setMchnt_txn_ssn(cap.getBusinessFlow());
                dataSource.setCust_no(fromUserMobile);
                return dataSource;
            }
            case ALLOCATION_PERSON_TO_PERSON:
            case ALLOCATION_PERSON_TO_COMPANY:
            case ALLOCATION_COMPANY_TO_PERSON:
            case TRANSFER_COMPANY_TO_MERCHANT:
            case TRANSFER_PERSON_TO_COMPANY:
            case TRANSFER_MERCHANT_TO_PERSON:
            case TRANSFER_COMPANY_TO_COMPANY: {
                TransferAccountsDataSource dataSource = new TransferAccountsDataSource();
                dataSource.setAmt(amt);
                dataSource.setMchnt_txn_ssn(cap.getBusinessFlow());
                dataSource.setOut_cust_no(fromUserMobile);
                dataSource.setIn_cust_no(toUserMobile);
                return dataSource;
            }
            case FROZEN_TO_FROZEN_PERSON_TO_PERSON:
            case FROZEN_TO_FROZEN_PERSON_TO_COMPANY:
            case FROZEN_TO_FROZEN_COMPANY_TO_PERSON: {
                TransferFreeze2FreezeDataSource dataSource = new TransferFreeze2FreezeDataSource();
                dataSource.setAmt(amt);
                dataSource.setMchnt_txn_ssn(cap.getBusinessFlow());
                dataSource.setOut_cust_no(fromUserMobile);
                dataSource.setIn_cust_no(toUserMobile);
                return dataSource;
            }
            case ALLOCATION_TO_FROZEN_PERSON_TO_PERSON:
            case ALLOCATION_TO_FROZEN_PERSON_TO_COMPANY:
            case ALLOCATION_TO_FROZEN_COMPANY_TO_PERSON:
            case TRANSFER_TO_FROZEN_MERCHANT_TO_PERSON:
            case TRANSFER_TO_FROZEN_MERCHANT_TO_COMPANY: {
                PreAuthorizationDataSource dataSource = new PreAuthorizationDataSource();
                dataSource.setAmt(amt);
                dataSource.setMchnt_txn_ssn(cap.getBusinessFlow());
                dataSource.setOut_cust_no(fromUserMobile);
                dataSource.setIn_cust_no(toUserMobile);
                return dataSource;
            }
            case UNFROZEN: {
                FreezeDataSource dataSource = new FreezeDataSource();
                dataSource.setCust_no(fromUserMobile);
                dataSource.setAmt(amt);
                dataSource.setMchnt_txn_ssn(cap.getBusinessFlow());
                return dataSource;
            }
            default:
                throw new UnimplementException("没实现的类型处理。。。。。");
        }
    }

    private String getUserBankMobile(Long userId) {
        if (userId != null) {
            UserInfoExt user = userInfoExtService.getUserInfoExtById(userId);
            return user.getMobileNo();
        }
        logger.error("进行存管交互时找不到卡信息", new Exception("进行存管交互时找不到卡信息"));
        throw new SystemException(SystemErrorCode.ILLEGAL_PARAMETER);
    }

    private AbstractResponse chooseApi(Integer type, AbstractDataSource dataSource) throws HfApiException {
        AbstractResponse response;
        switch (HFOperationEnum.typeOf(type)) {
            case FROZEN_PERSON_TO_PERSON:
            case FROZEN_PERSON_TO_COMPANY: {
                response = defaultApiService.freeze((FreezeDataSource) dataSource);
                break;
            }

            case ALLOCATION_PERSON_TO_PERSON:
            case ALLOCATION_PERSON_TO_COMPANY:
            case ALLOCATION_COMPANY_TO_PERSON:
            case TRANSFER_COMPANY_TO_MERCHANT:
            case TRANSFER_PERSON_TO_COMPANY:
            case TRANSFER_MERCHANT_TO_PERSON:
            case TRANSFER_COMPANY_TO_COMPANY: {
                response = defaultApiService.transferAccounts((TransferAccountsDataSource) dataSource);
                break;
            }

            case FROZEN_TO_FROZEN_PERSON_TO_PERSON:
            case FROZEN_TO_FROZEN_PERSON_TO_COMPANY:
            case FROZEN_TO_FROZEN_COMPANY_TO_PERSON: {
                response = defaultApiService.transferFreezeToFreeze((TransferFreeze2FreezeDataSource) dataSource);
                break;
            }

            case ALLOCATION_TO_FROZEN_PERSON_TO_PERSON:
            case ALLOCATION_TO_FROZEN_PERSON_TO_COMPANY:
            case ALLOCATION_TO_FROZEN_COMPANY_TO_PERSON:
            case TRANSFER_TO_FROZEN_MERCHANT_TO_PERSON:
            case TRANSFER_TO_FROZEN_MERCHANT_TO_COMPANY: {
                response = defaultApiService.transferAccountsToFreeze((PreAuthorizationDataSource) dataSource);
                break;
            }
            case UNFROZEN: {
                response = defaultApiService.unfreeze((FreezeDataSource) dataSource);
                break;
            }
            default:
                throw new UnimplementException("没实现的类型处理。。。。。");
        }
        return response;
    }

    @Override
    public void updateRechargeOrderStatus() {
        Map map = new HashMap();
        map.put("status", ScheduleEnum.BUSINESS_PREPARE.getValue());
        map.put("businessType", AccountConstants.BusinessTypeEnum.FEESTYPE_RECHARGE.getValue());
        map.put("startTime", DateUtil.getDateLong(new Date()));
        map.put("num", 10);
        List<Schedule> list = findByCondition(map);
        Date now = new Date();
        for (Schedule sch : list) {
            RechargeOrder rechargeOrder = rechargeOrderService.findRechargeOrderById(sch.getBusinessId());
            // 查询恒丰充值记录，更改充值状态
            boolean successResult = queryRecharge(rechargeOrder.getRechargeCode(), DateUtil.addDate(now, -7), now);
            if (successResult) {
                rechargeOrder.setStatus(RechargeStatus.SUCCESS.getValue());
            } else {
                rechargeOrder.setStatus(RechargeStatus.FAILE.getValue());
            }
            rechargeOrderService.updateRecharge(rechargeOrder);
        }
    }

    private boolean queryRecharge(String rechargeCode, Date startDate, Date endDate) {
        try {
            QueryRechargeWithdrawDataSource dataSource = new QueryRechargeWithdrawDataSource();
            dataSource.setTxn_ssn(rechargeCode);
            dataSource.setStart_time(DateUtil.getFormattedDateUtil(startDate, "yyyy-MM-dd HH:mm:ss"));
            dataSource.setEnd_time(DateUtil.getFormattedDateUtil(endDate, "yyyy-MM-dd HH:mm:ss"));
            QueryRechargeWithdrawResponse response = ihfApi.queryRecharge(dataSource);
            logger.info("查询充值信息：{}", response);
            if (response.isSuccess()) {
                ArrayList<QueryRechargeResponseItem> responseItems = new ArrayList<>(response.getResults());
                QueryRechargeResponseItem responseItem = responseItems.get(0);
                return responseItem.isSuccess();
            } else {
                return false;
            }
        } catch (HfApiException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }
}
