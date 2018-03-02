package com.xt.cfp.core.service.impl;

import com.external.llpay.PayDataBean;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.CustomerCardVO;
import com.xt.cfp.core.service.CustomerCardService;
import com.xt.cfp.core.service.RechargeOrderService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.WithDrawService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luqinglin on 2015/6/29.
 */
@Service
@Transactional
public class CustomerCardServiceImpl implements CustomerCardService {

    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private WithDrawService withDrawService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private CgBizService cgBizService;


    @Override
    public CustomerCard findById(long customerCardId) {
        return myBatisDao.get("CUSTOMERCARD.selectByPrimaryKey", customerCardId);
    }

    @Override
    @Transactional
    public CustomerCard addCustomerCard(CustomerCard customerCard) {
        myBatisDao.insert("CUSTOMERCARD.insertSelective", customerCard);
        return customerCard;
    }

    @Override
    public CustomerCard getCustomerBindCardByUserId(Long userId, PayConstants.PayChannel payChannel) {
        if (null == userId)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
        if (null == payChannel)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("payChannel", "null");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("belong", payChannel.getValue());
        return myBatisDao.get("CUSTOMERCARD.getCustomerBindCardByThirdChannel", params);
    }

    @Override
    public CustomerCard getCustomerBindCardUseful(Long userId) {
        return myBatisDao.get("CUSTOMERCARD.getCustomerBindCardUsefulWithBelongchannel", userId);
    }

    @Override
    public void changeUserCard(Long userId,  String cardNo, String code, String name, String cardType) {
        UserInfoExt ue = userInfoExtService.getUserInfoExtById(userId);
        CustomerCard cc = getCustomerBindCardUseful(userId);
        if (null != cc) {
            cc.setStatus("0");
            cc.setBindStatus("0");
            saveOrUpdateCustomerCard(cc);
        }
        cc = getCustomerBindCardByUserId(userId, PayConstants.PayChannel.HF);
        if (null == cc) {
            CustomerCard customerCard = new CustomerCard();
            customerCard.setUserId(userId);
            if (null == cardType || "".equals(cardType))
                customerCard.setCardType("1");
            else
                customerCard.setCardType("3");

            customerCard.setMobile(ue.getMobileNo());
            customerCard.setStatus(CustomerCardStatus.NORMAL.getValue());
            customerCard.setBelongChannel(PayConstants.CardChannel.HF.getValue());
            customerCard.setBindStatus(CustomerCardBindStatus.BINDED.getValue());
            customerCard.setCardCode(cardNo);
            customerCard.setCardcustomerName(ue.getRealName());
            customerCard.setUpdateTime(new Date());
            customerCard.setBankCode(cgBizService.getBankInfo(code, CgBank.IdTypeEnum.PERSON).getIconCode());
            customerCard.setAgreeNo(null);//签约协议号
            customerCard.setBankNum(code);
            customerCard.setBranchName(name);
            addCustomerCard(customerCard);
        }
        cgBizService.flowRecord();

    }

    @Override
    public List<CustomerCard> getCustomerCardByUserId(Long userId, PayConstants.PayChannel payChannel) {
        if (null == userId)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
        if (null == payChannel)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("payChannel", "null");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("belong", payChannel.getValue());
        return myBatisDao.getList("CUSTOMERCARD.getCustomerCardByThirdChannel", params);
    }


    @Override
    public CustomerCardVO getCustomerCardVOById(Long cardId) {
        return myBatisDao.get("CUSTOMERCARD.findVOById", cardId);
    }

    @Override
    @Transactional
    public CustomerCard saveOrUpdateCustomerCard(CustomerCard customerCard) {
        // 判断accid是否为null
        if (null == customerCard)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("customerCard", "null");
        customerCard.setUpdateTime(new Date());
        if (null == customerCard.getCustomerCardId())
            this.addCustomerCard(customerCard);
        else
            myBatisDao.update("CUSTOMERCARD.updateByPrimaryKeySelective", customerCard);
        return customerCard;
    }

    @Override
    @Transactional
    public void removeCustomerCard(Long customerCardId) {
        // 判断accid是否为null
        if (null == customerCardId)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("customerCardId", "null");
        CustomerCard customerCard = new CustomerCard();
        customerCard.setCustomerCardId(customerCardId);
        customerCard.setStatus(CustomerCardStatus.DISABLED.getValue());
        this.saveOrUpdateCustomerCard(customerCard);
    }

    @Override
    public List<CustomerCard> getCustomerCardsByUserId(Long userId) {
        return myBatisDao.getList("CUSTOMERCARD.getCustomerCardsByUserId", userId);
    }

    @Override
    public List<CustomerCard> getCustomerAllCardsByUserId(Long userId) {
        return myBatisDao.getList("CUSTOMERCARD.getCustomerAllCardsByUserId", userId);
    }

    @Override
    public List<ConstantDefine> searchBank(String typeCode) {
        return myBatisDao.getList("CONSTANTDEFINE.getConstantDefinesByType", typeCode);
    }

    /**
     * 获取所有用户卡信息
     */
    @Override
    public List<CustomerCard> getAllCustomerCard(CustomerCard customerCard) {
        return myBatisDao.getList("CUSTOMERCARD.getAllCustomerCard", customerCard);
    }

    @Override
    public BigDecimal getCardWithdrawAmount(Long customerCardId) {
        List<WithDraw> withDraws = withDrawService.getWithdrawListByCardId(customerCardId);

        BigDecimal result = BigDecimal.ZERO;
        for (WithDraw withDraw : withDraws) {
            if (withDraw.getTransStatus().equals(WithDrawTransferStatus.TRANSFER_SUCCESS.getValue()))
                result = result.add(withDraw.getWithdrawAmount());
        }

        return result;
    }

    @Override
    public List<CustomerCard> getCardByEnterpriseId(Long enterpriseId) {
        return myBatisDao.getList("CUSTOMERCARD.getCardByEnterpriseId", enterpriseId);
    }

    @Override
    @Transactional
    public void bindCustomCard(PayDataBean payDataBean) {
        //todo 先验证是否可以绑卡、绑卡（用户要锁定）
        RechargeOrder rechargeOrder = rechargeOrderService.getRechargeOrderByOrderCode(payDataBean.getNo_order(), false);
        if (rechargeOrder == null)
            return;
        //锁定用户
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(rechargeOrder.getUserId(), true);
        if (rechargeOrder.getCustomerCardId() == null) {
            //无卡回调（前台）
            //连连卡、有效卡都置为无效
            List<CustomerCard> cards = this.getCustomerCardByUserId(userInfoExt.getUserId(), PayConstants.PayChannel.LL);
            if (cards != null) {
                for (CustomerCard c : cards) {
                    c.setStatus(CustomerCardStatus.DISABLED.getValue());
                    this.saveOrUpdateCustomerCard(c);
                }
            }
            //绑定新的银行卡
            CustomerCard customerCard = new CustomerCard();
            customerCard.setUserId(rechargeOrder.getUserId());
            customerCard.setCardType(CardType.FULL_CARD.getValue());
            customerCard.setMobile(null);
            customerCard.setStatus(CustomerCardStatus.NORMAL.getValue());
            customerCard.setBelongChannel(PayConstants.CardChannel.LL.getValue());
            customerCard.setBindStatus(CustomerCardBindStatus.BINDED.getValue());
            customerCard.setCardCode(rechargeOrder.getCardNo());
            customerCard.setCardcustomerName(userInfoExt.getRealName());
            customerCard.setUpdateTime(new Date());
            customerCard.setBankCode(Long.valueOf(rechargeOrder.getBankCode()));
            customerCard.setAgreeNo(payDataBean.getNo_agree());//签约协议号
            addCustomerCard(customerCard);

            //修改充值单使用银行卡
            rechargeOrder.setCustomerCardId(customerCard.getCustomerCardId());
            this.myBatisDao.update("RECHARGE_ORDER.updateByPrimaryKeySelective", rechargeOrder);
        } else {
            //有卡回调
            CustomerCard card = findById(rechargeOrder.getCustomerCardId());
            if (!card.getBindStatus().equals(CustomerCardBindStatus.BINDED.getValue())) {
                card.setBindStatus(CustomerCardBindStatus.BINDED.getValue());
                card.setAgreeNo(payDataBean.getNo_agree());
                this.saveOrUpdateCustomerCard(card);
            }
            //已绑定的卡需要记录一下签约协议号
            if (card.getBindStatus().equals(CustomerCardBindStatus.BINDED.getValue())
                    && StringUtils.isEmpty(card.getAgreeNo())
                    && StringUtils.isNotEmpty(payDataBean.getNo_agree())) {
                card.setAgreeNo(payDataBean.getNo_agree());
                this.saveOrUpdateCustomerCard(card);
            }
            //连连卡、有效卡、未绑定置为无效
            List<CustomerCard> cards = this.getCustomerCardByUserId(userInfoExt.getUserId(), PayConstants.PayChannel.LL);
            if (cards != null) {
                for (CustomerCard c : cards) {
                    if (c.getBindStatus().equals(CustomerCardBindStatus.UNBINDING.getValue())) {
                        c.setStatus(CustomerCardStatus.DISABLED.getValue());
                        this.saveOrUpdateCustomerCard(c);
                    }
                }
            }
        }
    }
}
