package com.xt.cfp.core.event.command;

import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.AwardPointEnum;
import com.xt.cfp.core.constants.VisiableEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by ren yulin on 15-7-14.
 */
public class RepaymentAward2LendCommand extends Command {
    private long systemOperationIdAccountId;
    private AccountValueChangedQueue avcq;
    private LoanApplication loanApplication;
    private List<RightsRepaymentDetail> rightsRepaymentDetails;


    private LoanPublishService loanPublishService;
    private AwardDetailService awardDetailService;
    private UserAccountService userAccountService;
    private UserInfoService userInfoService;
    private LendOrderService lendOrderService;
    private CreditorRightsService creditorRightsService;

    public RepaymentAward2LendCommand(long systemOperationIdAccountId, AccountValueChangedQueue avcq,
                                      LoanApplication loanApplication, List<RightsRepaymentDetail> rightsRepaymentDetails,
                                      LoanPublishService loanPublishService, AwardDetailService awardDetailService,
                                      UserAccountService userAccountService, UserInfoService userInfoService,
                                      LendOrderService lendOrderService, CreditorRightsService creditorRightsService) {
        this.systemOperationIdAccountId = systemOperationIdAccountId;
        this.avcq = avcq;
        this.loanApplication = loanApplication;
        this.rightsRepaymentDetails = rightsRepaymentDetails;
        this.loanPublishService = loanPublishService;
        this.awardDetailService = awardDetailService;
        this.userAccountService = userAccountService;
        this.userInfoService = userInfoService;
        this.lendOrderService = lendOrderService;
        this.creditorRightsService = creditorRightsService;
    }

    @Override
    public Object execute() throws Exception {
        Date now = new Date();
        LoanPublish loanPublish = loanPublishService.findById(loanApplication.getLoanApplicationId());
        if (loanPublish.getAwardRate() == null || BigDecimalUtil.compareTo(loanPublish.getAwardRate(), BigDecimal.valueOf(0), 2) <= 0) {
            return null;
        }

        //奖励为年利率，月利为年利率除12
        BigDecimal ratio100 = BigDecimal.valueOf(100);
        BigDecimal monthAwardRate = loanPublish.getAwardRate().divide(ratio100).divide(new BigDecimal("12"),18,
                BigDecimal.ROUND_CEILING);
        BigDecimal awardBalance = loanApplication.getConfirmBalance().multiply(monthAwardRate);


        for (RightsRepaymentDetail rightsRepaymentDetail : rightsRepaymentDetails) {
        	//对于当期债权还款详情，只对已还清的进行发放奖励
            if(rightsRepaymentDetail.getRightsDetailState() == RightsRepaymentDetail.RIGHTSDETAILSTATE_COMPLETE ){

                if (awardDetailService.findByRightsRepaymentDetailId(rightsRepaymentDetail.getRightsRepaymentDetailId()) == null) {
                    BigDecimal theAward2Lend = BigDecimalUtil.down(awardBalance.multiply(rightsRepaymentDetail.getProportion().divide(ratio100)),2);
                    CreditorRights creditorRights = creditorRightsService.findById(rightsRepaymentDetail.getCreditorRightsId(), false);
                    LendOrder lendOrder = lendOrderService.findById(creditorRights.getLendOrderId());
                    UserInfoVO userInfoVO = userInfoService.getUserExtByUserId(lendOrder.getLendUserId());
                    AwardDetail awardDetail = awardDetailService.insertAwardDetail(now, rightsRepaymentDetail, theAward2Lend,
                            lendOrder, loanApplication.getLoanApplicationId(), AwardPointEnum.ATREPAYMENT,RateLendOrderTypeEnum.AWARD);

                    //平台支出奖励
                    String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AWARD_PAY, userInfoVO.getLoginName(), loanPublish.getLoanTitle());
                    AccountValueChanged avcPay = new AccountValueChanged(systemOperationIdAccountId, theAward2Lend,
                            theAward2Lend, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_AWARD.getValue(),
                            "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
                            AccountConstants.OwnerTypeEnum.SYS_ACC.getValue(),
                            systemOperationIdAccountId, now, descPay, false);
                    avcq.addAccountValueChanged(avcPay);

                    UserAccount userAccountWh = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
                    UserAccount cashUserAccount = null;
                    //如果是理财账户，奖励发放至理财账户中；如果是非理财账户，奖励发放到资金账户
                    if(userAccountWh.getAccTypeCode().equals(AccountConstants.AccountTypeEnum.ORDER_ACCOUNT.getValue())){
                    	cashUserAccount = userAccountWh;
                    }else{
                    	//出借人资金账户收入奖励
                    	cashUserAccount = userAccountService.getCashAccount(lendOrder.getLendUserId());
                    }
                    String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AWARD_INCOME, loanPublish.getLoanTitle(), AwardPointEnum.ATREPAYMENT.getDesc());
                    AccountValueChanged avcIncome = new AccountValueChanged(cashUserAccount.getAccId(), theAward2Lend,
                            theAward2Lend, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(),
                            "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
                            AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(),
                            cashUserAccount.getAccId(), now, descIncome, true);
                    avcq.addAccountValueChanged(avcIncome);
                }
            }
        }
        return null;

    }


}
