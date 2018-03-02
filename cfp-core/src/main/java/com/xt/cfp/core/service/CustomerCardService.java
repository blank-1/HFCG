package com.xt.cfp.core.service;

import com.external.llpay.PayDataBean;
import com.xt.cfp.core.constants.PayConstants;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.CustomerCard;
import com.xt.cfp.core.pojo.ext.CustomerCardVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by luqinglin on 2015/6/29.
 */
public interface CustomerCardService {

    /**
     * 通过主键查询客户卡信息
     *
     * @param customerCardId
     * @return
     */
    CustomerCard findById(long customerCardId);

    /**
     * 添加客户卡
     *
     * @param customerCard
     * @return
     */
    CustomerCard addCustomerCard(CustomerCard customerCard);


    /**
     * 通过客户id查询已经绑定的有效卡信息
     *
     * @param userId
     * @return
     */
    CustomerCard getCustomerBindCardByUserId(Long userId, PayConstants.PayChannel payChannel);

    /**
     * 查询用户可用卡
     *
     * @param userId
     * @return
     */
    CustomerCard getCustomerBindCardUseful(Long userId);

    /**
     * 更改用户卡
     *
     */
    void changeUserCard(Long userId,  String cardNo, String code, String name, String cardType);

    /**
     * 通过客户id查询有效卡信息
     *
     * @param userId
     * @return
     */
    List<CustomerCard> getCustomerCardByUserId(Long userId, PayConstants.PayChannel payChannel);

    /**
     * 通过客户id查询所有卡信息
     *
     * @param userId
     * @return
     */
    List<CustomerCard> getCustomerCardsByUserId(Long userId);

    List<CustomerCard> getCustomerAllCardsByUserId(Long userId);


    CustomerCardVO getCustomerCardVOById(Long cardId);

    /**
     * 保存或更新卡信息
     *
     * @param customerCard
     * @return
     */
    CustomerCard saveOrUpdateCustomerCard(CustomerCard customerCard);

    /**
     * 逻辑删除卡信息
     *
     * @param customerCardId
     */
    void removeCustomerCard(Long customerCardId);

    /**
     * 查询所有银行信息
     *
     * @return
     */
    List<ConstantDefine> searchBank(String typeCode);

    /**
     * 获取所有用户卡信息
     */
    List<CustomerCard> getAllCustomerCard(CustomerCard customerCard);

    /**
     * 获取银行卡的累计提现金额
     *
     * @param customerCardId
     * @return
     */
    BigDecimal getCardWithdrawAmount(Long customerCardId);


    /**
     * 获取企业虚拟用户及人员用户的卡信息
     *
     * @param enterpriseId 企业ID
     */
    List<CustomerCard> getCardByEnterpriseId(Long enterpriseId);

    /**
     * 本地绑卡
     *
     * @param payDataBean
     */
    void bindCustomCard(PayDataBean payDataBean);
}
