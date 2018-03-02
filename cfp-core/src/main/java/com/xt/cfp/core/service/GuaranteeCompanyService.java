package com.xt.cfp.core.service;

import com.xt.cfp.core.constants.GuaranteeCompanyStatus;
import com.xt.cfp.core.pojo.GuaranteeCompany;
import com.xt.cfp.core.pojo.RechargeOrder;
import com.xt.cfp.core.pojo.WithDraw;
import com.xt.cfp.core.pojo.ext.GuaranteeCompanyExt;
import com.xt.cfp.core.util.Pagination;

import java.util.List;
import java.util.Map;

/**
 * Created by luqinglin on 2015/7/1.
 */
public interface GuaranteeCompanyService {
    /**
     * 获取分页担保公司信息
     *
     * @param pageNum
     * @param pageSize
     * @param guaranteeCompany
     * @param customParams
     * @return
     */
    Pagination<GuaranteeCompanyExt> getGuaranteeCompanyPaging(int pageNum, int pageSize, GuaranteeCompany guaranteeCompany, Map<String, Object> customParams);

    /**
     * 添加担保公司
     * @param guaranteeCompany
     * @return
     */
    public GuaranteeCompany addGuaranteeCompany(GuaranteeCompany guaranteeCompany);

    /**
     * 修改担保公司状态
     * @param companyId
     * @param guaranteeCompanyStatus
     */
    public void changeGuaranteeCompanyStatus(Long companyId,GuaranteeCompanyStatus guaranteeCompanyStatus);

    /**
     * 根据id查询
     * @param companyId
     * @return
     */
    public GuaranteeCompany getGuaranteeCompanyByCompanyId(Long companyId);
    /**
     * 根据id查询详情
     * @param companyId
     * @return
     */
    public GuaranteeCompanyExt getGuaranteeCompanyDetailByCompanyId(Long companyId);

    /**
     * 修改渠道信息
     * @param guaranteeCompany
     */
    public void updateBondSource(GuaranteeCompany guaranteeCompany);

    /**
     * 担保公司充值
     * @param rechargeOrder
     * @return
     */
    public RechargeOrder recharge(RechargeOrder rechargeOrder);


    /**
     * 提现申请
     * @param withDraw
     */
    WithDraw withDraw(WithDraw withDraw);
    
    /**
     * 查询所有担保公司
     * @return
     */
    List<GuaranteeCompany> findAll(GuaranteeCompany guaranteeCompany);
}
