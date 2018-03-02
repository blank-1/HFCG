package com.xt.cfp.core.event.command;


import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.Event;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.DateUtil;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Renyulin on 14-8-27 下午3:55.
 */
public class CalculateSystemPrepayInterestCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");

    private List<RightsRepaymentDetail> rightsRepaymentDetails;
    private Date today;
    private boolean canntTurn;
    private BigDecimal turnRatio;
    private Date nextRepaymentDate;
    private CreditorRights creditorRights;
    private Map<Long, BigDecimal> interestMap = new HashMap<Long, BigDecimal>();

    public CalculateSystemPrepayInterestCommand(List<RightsRepaymentDetail> _rightsRepaymentDetails,
                                                Date _today, boolean _canntTurn, BigDecimal _turnRatio, Date _nextRepaymentDate,
                                                CreditorRights _creditorRights ) throws Exception {

        this.rightsRepaymentDetails = _rightsRepaymentDetails;

        this.today = _today;
        this.canntTurn = _canntTurn;
        this.turnRatio = _turnRatio;
        this.nextRepaymentDate = _nextRepaymentDate;
        this.creditorRights = _creditorRights;



        logger.debug("开始计算利息");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_CALCULATESYSTEMPREPAYINTEREST);
        int repaymnetIndex = 0;
        int interestDays = 0;
        boolean isCalculateComplete = false;
        Map<Integer, BigDecimal> everySecMap = new HashMap<Integer, BigDecimal>();
        for (RightsRepaymentDetail detail : rightsRepaymentDetails) {

            if (detail.getIsPayOff() == RightsRepaymentDetail.ISPAYOFF_NO) {
                BigDecimal shouldInterest = BigDecimal.ZERO;
                if (repaymnetIndex == rightsRepaymentDetails.size() - 1) {
                    //如果当前为还款计划中的最后一期，且逾期，则此债权转让给平台
                    if (DateUtil.before(detail.getRepaymentDayPlanned(), today, DateUtil.PATTERNTYPE_DATE)) {
                        logger.debug(" 该债权当前处于最后一期并逾期中，因此该债权转让给平台");
                        canntTurn = true;
                    }
                }
                if (DateUtil.before(detail.getRepaymentDayPlanned(), today, DateUtil.PATTERNTYPE_DATE)) {
                    shouldInterest = shouldInterest.multiply(turnRatio).add(detail.getShouldInterest().subtract(detail.getFactInterest()));
                    nextRepaymentDate = detail.getRepaymentDayPlanned();
                    logger.debug("还款日早于今天，因此属于逾期，购买本期全部利息");
                } else {
                    if (!isCalculateComplete) {
                        int cycleDays = 0;
                        if (repaymnetIndex == 0) { //第一期未还清
                            interestDays = DateUtil.daysBetween(creditorRights.getAgreementStartDate(), today);
                            cycleDays = DateUtil.daysBetween(creditorRights.getAgreementStartDate(), detail.getRepaymentDayPlanned());
                            nextRepaymentDate = detail.getRepaymentDayPlanned();
                        } else {
                            RightsRepaymentDetail theLastRightsRepaymentDetail = rightsRepaymentDetails.get(repaymnetIndex - 1);
                            interestDays = DateUtil.daysBetween(theLastRightsRepaymentDetail.getRepaymentDayPlanned(), today);//持有利息天数
                            cycleDays = DateUtil.daysBetween(theLastRightsRepaymentDetail.getRepaymentDayPlanned(), detail.getRepaymentDayPlanned());//当期天数
                            nextRepaymentDate = theLastRightsRepaymentDetail.getRepaymentDayPlanned();//下期还款日
                        }
                        shouldInterest = shouldInterest.add(new BigDecimal(String.valueOf(interestDays)).divide(new BigDecimal(String.valueOf(cycleDays)), 18, BigDecimal.ROUND_DOWN).multiply(detail.getShouldInterest().multiply(turnRatio)));
                        isCalculateComplete = true;

                    }
                }
                logger.debug("第" + detail.getSectionCode() + "期，需要购买利息:" + shouldInterest);
                interestMap.put(detail.getRightsRepaymentDetailId(), shouldInterest);
                everySecMap.put(detail.getSectionCode(), shouldInterest);
            } else {
                everySecMap.put(detail.getSectionCode(), BigDecimal.ZERO);
            }
            repaymnetIndex++;


        }
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);
        StringBuilder str = new StringBuilder("");
        Iterator<Integer> iterator = everySecMap.keySet().iterator();
        while (iterator.hasNext()) {
            int secCode = iterator.next();
            str.append("第" + secCode + "期，垫付：" + BigDecimalUtil.down(everySecMap.get(secCode), 2));

        }
        taskExecLog.setLogInfo("平台垫付利息计算完成.其中：" + str.toString());
    }


    @Override
    public Object execute() throws Exception {
        return null;
    }

    public boolean isCanntTurn() {
        return canntTurn;
    }

    public Date getNextRepaymentDate() {
        return nextRepaymentDate;
    }

    public Map<Long, BigDecimal> getInterestMap() {
        return interestMap;
    }

}
