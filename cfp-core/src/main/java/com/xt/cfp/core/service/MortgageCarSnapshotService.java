package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.CarChangeSnapshot;
import com.xt.cfp.core.pojo.EnterpriseCarLoanSnapshot;
import com.xt.cfp.core.pojo.MortgageCarSnapshot;
import com.xt.cfp.core.pojo.UserAccountHis;
import com.xt.cfp.core.util.Pagination;

public interface MortgageCarSnapshotService {
    /**
     * 添加抵押车
     */
	MortgageCarSnapshot addMortgageCarSnapshot(MortgageCarSnapshot mortgageCarSnapshot);

    /**
     * 根据抵押车ID加载一条数据 
     */
	MortgageCarSnapshot getMortgageCarSnapshotById(Long mortgageCarId);

    /**
     * 编辑抵押车
     */
	MortgageCarSnapshot editMortgageCarSnapshot(MortgageCarSnapshot mortgageCarSnapshot);
	
	/**
	 * 保存抵押车信息
	 * @param enterpriseCarLoanSnapshot 车贷快照
	 * @param mortgageCarSnapshotList 抵押车信息列表
	 * @return
	 */
	EnterpriseCarLoanSnapshot saveMortgageCarSnapshot(EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot, List<MortgageCarSnapshot> mortgageCarSnapshotList);
	
	/**
	 * 根据车贷ID，获取抵押车信息列表
	 */
	List<MortgageCarSnapshot> getListByCarLoanId(Long carLoanId);
	
	/**
	 * 根据抵押ID，删除一条抵押信息
	 */
	void deleteById(Long mortgageCarId);
	
	/**
	 * 保存抵押车记录
	 */
	MortgageCarSnapshot changeMortgageCar(MortgageCarSnapshot beforeCar, MortgageCarSnapshot afterCar, CarChangeSnapshot changeSnapshot);
	
    /**
     * 添加抵押变更记录
     */
	CarChangeSnapshot addCarChangeSnapshot(CarChangeSnapshot changeSnapshot);


	/**
	 * 抵押车列表
	 * @param pageNo
	 * @param pageSize
	 * @param mortgageCarSnapshot
	 *@param customParams  @return
	 */
	Pagination<MortgageCarSnapshot> getMortgageCarSnapshotPaging(int pageNo, int pageSize,MortgageCarSnapshot mortgageCarSnapshot, Map<String, Object> customParams);

	/**
	 * 抵押车列表(不分页)
	 * @param pageNo
	 * @param pageSize
	 * @param mortgageCarSnapshot
	 *@param customParams  @return
	 */
	List<MortgageCarSnapshot> getMortgageCarSnapshotList(MortgageCarSnapshot mortgageCarSnapshot);
	
	/**
	 * 汽车市场价总和
	 * @param enterpriseCarLoanId
	 * @return
	 */
	BigDecimal getTotalPriceByCardLoanId(Long enterpriseCarLoanId);
}
