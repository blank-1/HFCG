package com.xt.cfp.web.controller.api;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.Ebank2RechargeDataSource;
import com.external.deposites.model.datasource.RechargeDataSource;
import com.external.deposites.model.datasource.ResetPasswordDataSource;
import com.external.deposites.model.datasource.WithdrawPCDataSource;
import com.external.deposites.utils.PropertiesUtils;
import com.xt.cfp.core.Exception.code.ext.CreditorErrorCode;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.pojo.ext.VoucherVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.*;
import com.xt.cfp.web.controller.BaseController;
import jodd.util.NameValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/24
 */
@Controller
@RequestMapping(value = "/api/recharge")
public class RechargeApiController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IhfApi ihfApi;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private RateUserService rateUserService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private RateProductService rateProductService;
    @Autowired
    private CreditorRightsTransferAppService creditorRightsTransferAppService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private LendProductService lendProductService;

    @RequestMapping
    public Object home() {
        return "forward:/api/recharge/toRecharge";
    }

    @RequestMapping("toRecharge")
    public Object toRecharge(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String nowDateStr = sdf.format(new Date());
        model.addAttribute("now", nowDateStr);

        return "api/toRecharge";
    }

    /**
     * 4种充值方法
     */
    @RequestMapping(value = "doRecharge")
    public Object doRecharge(RechargeDataSource dataSource, Ebank2RechargeDataSource ebank2RechargeDataSource, Model model) {
        try {
//            dataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.pc.personal.back_notify_url"));
//            dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.pc.personal.page_notify_url"));
//            dataSource = ihfApi.pcQuickRecharge(dataSource);
            dataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.app.personal.back_notify_url"));
            dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.app.personal.page_notify_url"));
            dataSource = ihfApi.appRecharge4personal(dataSource);
//            dataSource = ihfApi.ebankRecharge(ebank2RechargeDataSource);
//            ebank2RechargeDataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.E-bank2.back_notify_url"));
//            ebank2RechargeDataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.E-bank2.page_notify_url"));
//            ebank2RechargeDataSource = ihfApi.ebankRecharge2(ebank2RechargeDataSource);
            if (StringUtils.isNull(ebank2RechargeDataSource.getMchnt_cd())) {
                model.addAttribute("params", dataSource);
            } else {
                model.addAttribute("params", ebank2RechargeDataSource);
            }

        } catch (HfApiException e) {
            logger.error(e.getMessage(), e);
        }
        return "api/doRecharge";
    }

    /**
     * 提现方法
     */
    @RequestMapping(value = "doWithdraw")
    public Object doWithdraw(WithdrawPCDataSource dataSource, Model model) {
        try {
            dataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.withdraw.pc.back_notify_url"));
            dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.withdraw.pc.page_notify_url"));
//            dataSource = ihfApi.pcWithdraw(dataSource);
            dataSource = ihfApi.appWithdraw(dataSource);
            model.addAttribute("params", dataSource);

        } catch (HfApiException e) {
            logger.error(e.getMessage(), e);
        }
        return "api/doWithdraw";
    }

    /**
     * 重置密码方法
     */
    @RequestMapping(value = "doResetPassword")
    public Object doResetPassword(ResetPasswordDataSource dataSource, Model model) {
        try {
            dataSource.setBack_url(PropertiesUtils.property("hf-config", "cg.hf.api.change.password.app.page_notify_url"));
            dataSource = ihfApi.resetPassword(dataSource);
            model.addAttribute("params", dataSource);

        } catch (HfApiException e) {
            logger.error(e.getMessage(), e);
        }
        return "api/doResetPassword";
    }

    @ResponseBody
    @RequestMapping(value = "signRecharge")
    public Object signRecharge(HttpServletRequest request, String mobile, BigDecimal amount, String flag) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        RechargeDataSource dataSource = new RechargeDataSource();
        try {
            dataSource.setLogin_id(mobile);
            dataSource.setAmt(amount.multiply(new BigDecimal(100)).longValue());

            dataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.E-bank.back_notify_url"));
            dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.E-bank.page_notify_url"));

            if (flag.equals("1")) {
                dataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.pc.personal.back_notify_url"));
                dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.pc.personal.page_notify_url"));
                dataSource = ihfApi.pcQuickRecharge(dataSource);
            } else {
                dataSource = ihfApi.ebankRecharge(dataSource);
            }
            CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.HF);
            rechargeOrderService.createHFRechargeRequest(amount, currentUser, card, flag.equals("1") ? RechargeChannelEnum.HF_AUTHPAY : RechargeChannelEnum.HF_GATEPAY, ClientEnum.WEB_CLIENT,null, dataSource.getMchnt_txn_ssn());
        } catch (HfApiException e) {
            logger.error(e.getMessage(), e);
        }
        return dataSource;
    }

    @ResponseBody
    @RequestMapping(value = "signPayRecharge")
    public Object signPayRecharge(HttpServletRequest request,String mobile, BigDecimal amount, String flag,Long lendOrderId,
                                  String jypassword,String voucherIds,String rateUserIds,BigDecimal accountPayValue,BigDecimal rechargePayValue) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        RechargeDataSource dataSource=new RechargeDataSource();
        try {
            //校验-参数必填
            ValidationUtil.checkRequiredPara(new NameValue<String, Object>("lendOrderId", lendOrderId));
            LendOrder lendOrder = lendOrderService.findById(lendOrderId);
            request.setAttribute("lendOrder", lendOrder);
            //校验-如果订单已经被处理过
            if (!lendOrder.getOrderState().equals(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue())) {
                return JsonView.JsonViewFactory.create().success(false).info("订单已处理过").put("id", "redirect").toJson();
            }
            UserInfo userByUserId = userInfoService.getUserByUserId(currentUser.getUserId());
            //校验交易密码
            if (userByUserId.getBidPass() == null){
                return JsonView.JsonViewFactory.create().success(false).info("请您输入交易密码！").put("id", "jypassword").toJson();
            }
            if (!MD5Util.MD5Encode(jypassword, null).equals(userByUserId.getBidPass())){
                return JsonView.JsonViewFactory.create().success(false).info("交易密码错误").put("id", "jypassword").toJson();
            }

            //定向标验证
            List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
            LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
            LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());
            String _result = chioceWhichReturnBySpecialLoanApplication2(currentUser.getUserId(),loanApplicationListVO.getLoanApplicationId(),null,request);
            if(_result!=null){
                return _result;
            }
            // 加息券校验
            RateUser rateUser = null;
            RateProduct rateProduct = null;
            if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
                if (!StringUtils.isNull(rateUserIds)) {
                    String[] arrayRateUserIds = rateUserIds.split(",");
                    if (arrayRateUserIds.length > 1) {
                        return JsonView.JsonViewFactory.create().success(false).info("加息券不能叠加使用").toJson();
                    }
                    rateUser = rateUserService.findByRateUserId(Long.valueOf(arrayRateUserIds[0]));
                    if (null == rateUser) {
                        return JsonView.JsonViewFactory.create().success(false).info("该加息券不存在").toJson();
                    }
                    List<LendOrderBidDetail> details = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId(), LendOrderBidStatusEnum.WAITING_PAY);
                    if (details.size() == 0) {
                        return JsonView.JsonViewFactory.create().success(false).info("该订单出现异常").toJson();
                    }
                    LoanApplicationListVO loanApp = loanApplicationService.getLoanApplicationVoById(details.get(0).getLoanApplicationId());
                    rateProduct = rateProductService.getRateProductById(rateUser.getRateProductId());
                    JsonView checkRateUser = rateUserService.checkRateUser(rateUser, rateProduct, currentUser.getUserId(), lendOrder.getTimeLimit(), loanApp.getLoanType(), lendOrder.getBuyBalance());
                    if (!checkRateUser.isSuccess()) {
                        return checkRateUser.toJson();
                    }
                    if (!rateProduct.getUsageScenario().equals(RateEnum.RateProductScenarioEnum.ALL.getValue())) {
                        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())) {
                            if (!rateProduct.getUsageScenario().equals(RateEnum.RateProductScenarioEnum.FINANCE.getValue())) {
                                return JsonView.JsonViewFactory.create().success(false).info("该加息券不能购买理财").toJson();
                            }
                        } else if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
                            if (!rateProduct.getUsageScenario().equals(RateEnum.RateProductScenarioEnum.BIDDING.getValue())) {
                                return JsonView.JsonViewFactory.create().success(false).info("该加息券不能购买投标产品").toJson();
                            }
                        } else if (lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue())) {
                            return JsonView.JsonViewFactory.create().success(false).info("该加息券不能债权转让产品").toJson();
                        }
                    }
                    if (!com.xt.cfp.core.util.StringUtils.isNull(voucherIds)) {
                        if (rateProduct.getIsOverlay().equals(RateEnum.RateProductIsOverlayEnum.DISABLED.getValue())) {
                            return JsonView.JsonViewFactory.create().success(false).info("该加息券不能和财富券叠加使用").toJson();
                        }
                    }

                }
            }

            //todo 财富券
            List<VoucherVO> voucherVOs = new ArrayList<VoucherVO>();
            BigDecimal voucherPayValue=BigDecimal.ZERO;
            if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)){
                String[] voucher_Ids = voucherIds.split(",");
                //先校验财富券
                for (String voucherId:voucher_Ids){
                    VoucherVO vo = voucherService.getVoucherById(Long.valueOf(voucherId));
                    voucherVOs.add(vo);
                    //计算财富券总额
                    voucherPayValue = voucherPayValue.add(vo.getVoucherValue());
                }
                JsonView jsonView = voucherService.voucherValidate(currentUser, lendOrder, voucherVOs);
                if (!jsonView.isSuccess()){
                    return jsonView.toJson();
                }
            }
            //校验-财富券拆分
            if (lendOrder.getBuyBalance().compareTo(voucherPayValue) < 0) {
                return JsonView.JsonViewFactory.create().success(false).info("财富券金额不能打于订单金额").put("id", "redirect").toJson();
            }

            //校验-购买金额加和是否等于订单金额
            if (lendOrder.getBuyBalance().compareTo(accountPayValue.add(rechargePayValue).add(voucherPayValue)) != 0) {
                return JsonView.JsonViewFactory.create().success(false).info("购买金额异常！").put("id", "redirect").toJson();
            }

            //todo 连连支付可以做当日限额和当月限额校验
            // 购买金额是否合法
            _result = amountValidate(lendOrder);
            if(_result!=null){
                return _result;
            }

            dataSource.setLogin_id(mobile);
            dataSource.setAmt(amount.multiply(new BigDecimal(100)).longValue());

            dataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.E-bank.back_notify_url"));
            dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.E-bank.page_notify_url"));

            if(flag.equals("1")){
                dataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.pc.personal.back_notify_url"));
                dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.pc.personal.page_notify_url"));
                dataSource = ihfApi.pcQuickRecharge(dataSource);
            }else{
                dataSource = ihfApi.ebankRecharge(dataSource);
            }
            CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.HF);
            rechargeOrderService.createHFPayRequest(accountPayValue, rechargePayValue, lendOrder, currentUser, voucherVOs, voucherPayValue, card, ClientEnum.WEB_CLIENT,rateUser,rateProduct, flag.equals("1")?RechargeChannelEnum.HF_AUTHPAY:RechargeChannelEnum.HF_GATEPAY,dataSource.getMchnt_txn_ssn());
        } catch (HfApiException e) {
            logger.error(e.getMessage(), e);
        }
        return dataSource;
    }

    /**
     * 支付金额验证
     * @param lendOrder
     * @return 返回null表示验证通过
     */
    private String amountValidate( LendOrder lendOrder){

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())){
            //投标金额验证
            List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
            LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
            LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());

            if(currentUser!=null){
                //获取账户余额
                if(lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getRemain())>0){
                    return JsonView.JsonViewFactory.create().success(false).info("金额已超出剩余金额！").put("id","redirect").toJson();
                }
                BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
                if(lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy))>0){
                    //无法购买剩余标
                    JsonView.JsonViewFactory.create().success(false).info("金额已超出剩余金额！").put("id","redirect").toJson();
                }
                //计算账户已投金额
                BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(currentUser.getUserId(), loanApplicationListVO.getLoanApplicationId());
                if (totalLendAmountPerson != null) {
                    if(loanApplicationListVO.getMaxBuyBalance()==null){
                        loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
                    }else{
                        loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
                    }
                }
            }

            if(lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getMaxBuyBalanceNow())>0)
                return JsonView.JsonViewFactory.create().success(false).info("金额已超出当前最大可投金额！").put("id", "redirect").toJson();
        } else if (lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue())) {
            CreditorRightsTransferApplication creditorRights = creditorRightsTransferAppService.getTransferApplicationByLendOrderId(lendOrder.getLendOrderId());
            Long creditorRightsId = creditorRights.getApplyCrId();
            BigDecimal amount = lendOrder.getBuyBalance();

            BigDecimal totalBuy = creditorRightsTransferAppService.getRemainingRightsPrice(creditorRights.getCreditorRightsApplyId());
            CreditorRightsTransferApplication crta = creditorRightsTransferAppService.getEffectiveTransferApplyByCreditorRightsId(creditorRightsId);
            if (new BigDecimal("100").compareTo(crta.getApplyPrice().subtract(totalBuy).subtract(amount)) > 0
                    && BigDecimal.ZERO.compareTo(crta.getApplyPrice().subtract(totalBuy).subtract(amount)) < 0){
                return JsonView.JsonViewFactory.create().success(false).info(CreditorErrorCode.CREDITOR_RESIDUAL_MIN.getDesc()).put("id", "redirect").toJson();
            }
            if (amount.compareTo(crta.getApplyPrice().subtract(totalBuy)) > 0) {
                // 购买金额变为剩余可投金额
                if (crta.getApplyPrice().compareTo(totalBuy) == 0) {
                    return JsonView.JsonViewFactory.create().success(false).info(CreditorErrorCode.CREDITOR_AMOUNT_ZONE.getDesc()).put("id", "redirect").toJson();
                }
            }
        }else{
            //省心计划金额验证
            if (!lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())){
                return JsonView.JsonViewFactory.create().success(false).info(SystemErrorCode.ILLEGAL_REQUEST.getDesc()).put("id","redirect").toJson();
            }
            LendProductPublish lendProductPublish = lendProductService.getLendProductPublishByPublishId(lendOrder.getLendProductPublishId());
            //校验金额是否合法
            BigDecimal waitingBuyBalance = lendProductPublish.getPublishBalance().subtract(lendProductPublish.getSoldBalance());
            if(LendProductPublish.PUBLISHBALANCETYPE_SPEC == lendProductPublish.getPublishBalanceType()){//如果购买的是指定金额的省心计划
                if (lendOrder.getBuyBalance().compareTo(waitingBuyBalance)>0){
                    return JsonView.JsonViewFactory.create().success(false).info("购买金额已超出剩余可购买金额").put("id","redirect").toJson();
                }
            }
        }
        return null;
    }
}
