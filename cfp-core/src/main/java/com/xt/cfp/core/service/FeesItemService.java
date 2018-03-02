package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LendProduct;
import com.xt.cfp.core.util.Pagination;

public interface FeesItemService {
	List<FeesItem> getFeesItemsByTypeAndKind(String itemType, char itemKind);
	FeesItem findById(long feesItemId);
    List<FeesItem> findAll(FeesItem feesItem);
    
    
    /**
     * 列表
     * @param page
     * @param limit
     * @param feesItem
     * @return
     */
    public Pagination<FeesItem> findAllByPage(int page, int limit,FeesItem feesItem);
    /**
     * 添加
     * @param feesItem
     */
    public void addFeesItem(FeesItem feesItem);
    /**
     * 找父节点
     * @param constantDefine
     * @return
     */
    public List<ConstantDefine> findConstantValueOrCode(ConstantDefine constantDefine);
    /**
     * 修改
     * @param feesItem
     */
    public void updateFeesItem(FeesItem feesItem);
    
    /**
     * 处理禁用项目操作
     * @param feesItem
     * @return
     */
    public List<LendProduct> disableLendProductFees(FeesItem feesItem);
    
    public List<FeesItem> disableLoanProductFees(FeesItem feesItem);
    /**
     * 是否存在相同的费用
     * @param feesName
     * @return
     */
    public List<FeesItem> findFeesByName(String feesName);
    
    /**
     *新增费用时查询下拉框的公共方法
     * @param boxMess
     * @return List<ConstantDefine>
     */
    public List<ConstantDefine> findConstantTypeCode(String boxMess);

    /**
     * 计算费用项目
     * @param feesItemId
     * @param allCalital 所有本金
     * @param allInterest 所有利息
     * @param currentCalital 当期本金
     * @param currentInterest 当期利息
     * @param allProfit 所有收益
     * @param currentProfit 当期收益
     * @return
     */
    BigDecimal calculateFeesBalance(long feesItemId, BigDecimal allCalital, BigDecimal allInterest,
                                    BigDecimal currentCalital, BigDecimal currentInterest, BigDecimal allProfit, BigDecimal currentProfit);
    
    /**
     * 计算费用项目（后期新加的费用项计算）
     * @param feesItemId
     * @param allCalital 所有本金
     * @param allInterest 所有利息
     * @param currentCalital 当期本金
     * @param currentInterest 当期利息
     * @param allProfit 所有收益
     * @param currentProfit 当期收益
     * @return
     */
	BigDecimal calculateFeesBalance2(long feesItemId, BigDecimal... bigDecimals);
}
