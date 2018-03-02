package com.xt.cfp.core.event.command;


import com.xt.cfp.core.constants.PayOffEnum;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.event.service.PrepayInterestKit;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-23 上午10:22.
 */
public class NewRightsRepaymentDetailCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");

    private Date today;
    private List<RightsRepaymentDetail> rightsRepaymentDetails;
    private CreditorRights newRights;
    private long customerAccountId;
    private long newLendOrderId;
    private boolean totalTurnOut;
    private BigDecimal turnRatio;
    private AccountValueChangedQueue avcq;
    private CreditorRightsHistory creditorRightsHistory;

    private Map<Long, BigDecimal> interestMap = new HashMap<Long, BigDecimal>();

    private long systemAccountId;
    private long adminId;
    private LendOrder oldLender;
    private long lendOrderId;
    private long lendAccountId;

    private RightsRepaymentDetailService rightsRepaymentDetailService;
    private UserAccountService userAccountService;
    private LendOrderService lendOrderService;
    private CreditorRights creditorRights;
    private RightsPrepayDetailService rightsPrepayDetailService;
    private LendOrderReceiveService lendOrderReceiveService;
    private UserInfoService userInfoService;

    private BigDecimal sumInterest;

    public NewRightsRepaymentDetailCommand(Date today, List<RightsRepaymentDetail> rightsRepaymentDetails,
                                           CreditorRights newRights, long customerAccountId, long newLendOrderId,
                                           boolean totalTurnOut, BigDecimal turnRatio, AccountValueChangedQueue avcq,
                                           CreditorRightsHistory creditorRightsHistory,
                                           Map<Long, BigDecimal> interestMap,
                                           long systemAccountId,  LendOrder oldLender, long lendOrderId, long lendAccountId,
                                           CreditorRights creditorRights,
                                           LendOrderService lendOrderService,
                                           RightsRepaymentDetailService rightsRepaymentDetailService,
                                           UserAccountService userAccountService,
                                           RightsPrepayDetailService rightsPrepayDetailService, BigDecimal sumInterest,
                                           LendOrderReceiveService lendOrderReceiveService,UserInfoService userInfoService) {
        this.today = today;
        this.rightsRepaymentDetails = rightsRepaymentDetails;
        this.newRights = newRights;
        this.customerAccountId = customerAccountId;
        this.newLendOrderId = newLendOrderId;
        this.totalTurnOut = totalTurnOut;
        this.turnRatio = turnRatio;
        this.avcq = avcq;
        this.creditorRightsHistory = creditorRightsHistory;
        this.interestMap = interestMap;

        this.systemAccountId = systemAccountId;
        this.oldLender = oldLender;
        this.lendOrderId = lendOrderId;
        this.lendAccountId = lendAccountId;
        this.rightsRepaymentDetailService = rightsRepaymentDetailService;
        this.userAccountService = userAccountService;
        this.lendOrderService = lendOrderService;
        this.creditorRights = creditorRights;
        this.rightsPrepayDetailService = rightsPrepayDetailService;
        this.userInfoService = userInfoService;
        this.sumInterest = sumInterest;
        this.lendOrderReceiveService = lendOrderReceiveService;
    }

    @Override
    public Object execute() throws Exception {
        //保存新债权分配明细
        int sectionCode = 1;

        BigDecimal sumRatio = BigDecimal.ZERO;
        int repaymentDetailIndex = 0;
        logger.debug("开始创建新债权的债权分配明细");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_SAVENEWCREDITORDETAIL);
        for (RightsRepaymentDetail detail : rightsRepaymentDetails) {
            if (detail.getIsPayOff() == RightsRepaymentDetail.ISPAYOFF_NO) {
                if (DateUtil.after(detail.getRepaymentDayPlanned(), today, DateUtil.PATTERNTYPE_DATE) || DateUtil.equal(detail.getRepaymentDayPlanned(), today, DateUtil.PATTERNTYPE_DATE)) {
                    RightsRepaymentDetail newDetail1 = new RightsRepaymentDetail();
                    BeanUtils.copyProperties(detail, newDetail1);
                    newDetail1.setCreditorRightsId(newRights.getCreditorRightsId());
                    newDetail1.setLendAccountId(customerAccountId);
                    newDetail1.setLendOrderId(newLendOrderId);
                    newDetail1.setRepaymentRecordId(0);
                    newDetail1.setSectionCode(sectionCode++);


                    //如之前已产生部分还款，则新债权明细应在应还金额基础上，减去已还金额
                    if (totalTurnOut) {
                        if (BigDecimalUtil.compareTo(detail.getFactBalance(), BigDecimal.ZERO, true, 2) > 0) {
                            newDetail1.setShouldBalance(detail.getShouldBalance().subtract(detail.getFactBalance()));
                            newDetail1.setShouldInterest(detail.getShouldInterest().subtract(detail.getFactInterest()));
                            newDetail1.setShouldCapital(detail.getShouldCapital().subtract(detail.getFactCalital()));

                        } else {
                            //直接取原分配明细的应还本金、应还利息和应还总额
                        }
                        detail.setRightsDetailState(RightsRepaymentDetail.RIGHTSDETAILSTATE_TURNOUT);//修改原债权分配明细的状态
                    } else {
                        if (BigDecimalUtil.compareTo(turnRatio, BigDecimal.ONE, true, 2) < 0) {
                            if (repaymentDetailIndex < rightsRepaymentDetails.size()) {
                                newDetail1.setShouldCapital(detail.getShouldCapital().multiply(turnRatio));
                                newDetail1.setShouldInterest(detail.getShouldInterest().multiply(turnRatio));
                                sumRatio = sumRatio.add(turnRatio);
                            } else {
                                newDetail1.setShouldCapital(detail.getShouldCapital().multiply(BigDecimal.ONE.subtract(sumRatio)));
                                newDetail1.setShouldInterest(detail.getShouldInterest().multiply(BigDecimal.ONE.subtract(sumRatio)));
                            }
                            newDetail1.setShouldBalance(newDetail1.getShouldCapital().add(newDetail1.getShouldInterest()));


                            detail.setShouldCapital(detail.getShouldCapital().subtract(newDetail1.getShouldCapital()));
                            detail.setShouldBalance(detail.getShouldBalance().subtract(newDetail1.getShouldBalance()));
                            detail.setShouldInterest(detail.getShouldInterest().subtract(newDetail1.getShouldInterest()));
                        } else {
                            if (BigDecimalUtil.compareTo(detail.getFactBalance(), BigDecimal.ZERO, true, 2) > 0) {
                                newDetail1.setShouldCapital(detail.getShouldCapital().subtract(detail.getFactCalital()));
                                newDetail1.setShouldBalance(detail.getShouldBalance().divide(detail.getShouldCapital(), 18, BigDecimal.ROUND_CEILING).multiply(newDetail1.getShouldCapital()));
                                newDetail1.setShouldInterest(newDetail1.getShouldBalance().subtract(newDetail1.getShouldCapital()));


                            } else {
                                //直接取原分配明细的应还本金、应还利息和应还总额
                            }
                            detail.setRightsDetailState(RightsRepaymentDetail.RIGHTSDETAILSTATE_TURNOUT);//修改原债权分配明细的状态
                        }

                    }
                    logger.debug("\t第" + newDetail1.getSectionCode() + "期，应还本金:" + newDetail1.getShouldCapital() + ",应还利息:" + newDetail1.getShouldInterest());


                    //新增新债权分配明细
                    rightsRepaymentDetailService.addRightsRepaymentDetail(newDetail1);


                    BigDecimal shouldInterest = interestMap.get(detail.getRightsRepaymentDetailId());
                    if (interestMap.containsKey(detail.getRightsRepaymentDetailId()) && BigDecimalUtil.compareTo(shouldInterest, BigDecimal.ZERO, true, 2) > 0) {

                        /*PrepayInterestKit.savePrepayInterest(today, shouldInterest, totalTurnOut, systemAccountId, oldLender, avcq,
                                creditorRights, newDetail1, detail,creditorRightsHistory, lendOrderService, rightsPrepayDetailService, lendOrderReceiveService);*/
                        //平台不垫付，出借人垫付

                        //原债权明细的应还利息做相应的修改
                        detail.setShouldInterest(shouldInterest);
                        detail.setShouldInterest2(BigDecimalUtil.up(detail.getShouldInterest(), 2));

                        //原债权的价格要增加，垫付的部分
                        creditorRights.setLendPrice(creditorRights.getLendPrice().add(shouldInterest));


                    }


                    newDetail1.setShouldBalance2(BigDecimalUtil.down(newDetail1.getShouldBalance(), 2));
                    newDetail1.setShouldInterest2(BigDecimalUtil.down(newDetail1.getShouldInterest(), 2));
                    newDetail1.setShouldCapital2(newDetail1.getShouldBalance2().subtract(newDetail1.getShouldInterest2()));

                    //修改新债权的分配明细

                    Map<String,Object> newDetail1Map = new HashMap<String,Object>();
                    newDetail1Map.put("shouldBalance2",newDetail1.getShouldBalance2());
                    newDetail1Map.put("shouldInterest2",newDetail1.getShouldInterest2());
                    newDetail1Map.put("shouldCapital2",newDetail1.getShouldCapital2());
                    rightsRepaymentDetailService.update(newDetail1Map);
                    //修改原债权分配明细（或修改状态为已转出，或修改应还本金、利息和金额）
                    detail.setShouldCapital2(BigDecimalUtil.up(detail.getShouldCapital(), 2));
                    detail.setShouldBalance2(BigDecimalUtil.up(detail.getShouldBalance(), 2));
                    detail.setShouldInterest2(detail.getShouldBalance2().subtract(detail.getShouldCapital2()));
                    Map<String,Object> detailMap = new HashMap<String,Object>();
                    detailMap.put("shouldBalance2",detail.getShouldBalance2());
                    detailMap.put("shouldInterest2",detail.getShouldInterest2());
                    detailMap.put("shouldCapital2",detail.getShouldCapital2());
                    rightsRepaymentDetailService.update(detailMap);

                    sumInterest = sumInterest.add(shouldInterest);

                }
            }
        }
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);
        taskExecLog.setLogInfo("新债权分配明细保存成功.");
        logger.debug("债权分配明细创建完成");
        return null;

    }
}
