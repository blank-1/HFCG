package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CarChangeSnapshot;
import com.xt.cfp.core.pojo.EnterpriseCarLoanSnapshot;
import com.xt.cfp.core.pojo.MortgageCarSnapshot;
import com.xt.cfp.core.service.EnterpriseCarLoanSnapshotService;
import com.xt.cfp.core.service.MortgageCarSnapshotService;
import com.xt.cfp.core.util.Pagination;

@Service
public class MortgageCarSnapshotServiceImpl implements
		MortgageCarSnapshotService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	@Autowired
	private EnterpriseCarLoanSnapshotService enterpriseCarLoanSnapshotService;
	
	@Override
	public MortgageCarSnapshot addMortgageCarSnapshot(
			MortgageCarSnapshot mortgageCarSnapshot) {
		myBatisDao.insert("MORTGAGE_CAR_SNAPSHOT.insert", mortgageCarSnapshot);
		return mortgageCarSnapshot;
	}

	@Override
	public MortgageCarSnapshot getMortgageCarSnapshotById(Long mortgageCarId) {
		return myBatisDao.get("MORTGAGE_CAR_SNAPSHOT.selectByPrimaryKey", mortgageCarId);
	}

	@Override
	public MortgageCarSnapshot editMortgageCarSnapshot(
			MortgageCarSnapshot mortgageCarSnapshot) {
		myBatisDao.update("MORTGAGE_CAR_SNAPSHOT.updateByPrimaryKey", mortgageCarSnapshot);
		return mortgageCarSnapshot;
	}

	@Override
	@Transactional
	public EnterpriseCarLoanSnapshot saveMortgageCarSnapshot(
			EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot,
			List<MortgageCarSnapshot> mortgageCarSnapshotList) {
		
		enterpriseCarLoanSnapshot = enterpriseCarLoanSnapshotService.editEnterpriseCarLoanSnapshot(enterpriseCarLoanSnapshot);
		
		// 根据车贷ID，查询所有抵押车信息列表，并删除
		List<MortgageCarSnapshot> carSnapshots = this.getListByCarLoanId(enterpriseCarLoanSnapshot.getEnterpriseCarLoanId());
		if(null != carSnapshots && carSnapshots.size() > 0){
			for (int i = 0; i < carSnapshots.size(); i++) {
				this.deleteById(carSnapshots.get(i).getMortgageCarId());
			}
		}
		
		// 添加抵押车信息
		if(null != mortgageCarSnapshotList && mortgageCarSnapshotList.size() > 0){
			for (int i = 0; i < mortgageCarSnapshotList.size(); i++) {
				MortgageCarSnapshot mortgageCar = mortgageCarSnapshotList.get(i);
				mortgageCar.setCarLoanId(enterpriseCarLoanSnapshot.getEnterpriseCarLoanId());
				this.addMortgageCarSnapshot(mortgageCar);
			}
		}
		
		return enterpriseCarLoanSnapshot;
	}

	@Override
	public List<MortgageCarSnapshot> getListByCarLoanId(Long carLoanId) {
		return myBatisDao.getList("MORTGAGE_CAR_SNAPSHOT.getListByCarLoanId", carLoanId);
	}

	@Override
	public void deleteById(Long mortgageCarId) {
		myBatisDao.delete("MORTGAGE_CAR_SNAPSHOT.deleteByPrimaryKey", mortgageCarId);
	}

	@Override
	@Transactional
	public MortgageCarSnapshot changeMortgageCar(MortgageCarSnapshot beforeCar,
			MortgageCarSnapshot afterCar, CarChangeSnapshot changeSnapshot) {
		
		//1.修改变更前抵押车 状态 为不可用
		this.editMortgageCarSnapshot(beforeCar);
        
        //2.创建新抵押车 状态可用 并含有 变更信息
		afterCar = this.addMortgageCarSnapshot(afterCar);
        
        //3.创建变更记录
		changeSnapshot.setBeforeId(beforeCar.getMortgageCarId());
		changeSnapshot.setAfterId(afterCar.getMortgageCarId());
		changeSnapshot = this.addCarChangeSnapshot(changeSnapshot);
		
		return afterCar;
	}

	@Override
	public CarChangeSnapshot addCarChangeSnapshot(CarChangeSnapshot changeSnapshot) {
		myBatisDao.insert("CAR_CHANGE_SNAPSHOT.insert", changeSnapshot);
		return changeSnapshot;
	}

	@Override
	public Pagination<MortgageCarSnapshot> getMortgageCarSnapshotPaging(int pageNo, int pageSize, MortgageCarSnapshot mortgageCarSnapshot, Map<String, Object> customParams) {
		Pagination<MortgageCarSnapshot> re = new Pagination<MortgageCarSnapshot>();
		re.setCurrentPage(pageNo);
		re.setPageSize(pageSize);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mortgageCarSnapshot", mortgageCarSnapshot);
		params.put("customParams", customParams);


		int totalCount = this.myBatisDao.count("getMortgageCarSnapshotPaging", params);
		List<MortgageCarSnapshot> uah = this.myBatisDao.getListForPaging("getMortgageCarSnapshotPaging", params, pageNo, pageSize);

		re.setTotal(totalCount);
		re.setRows(uah);

		return re;
	}

	@Override
	public BigDecimal getTotalPriceByCardLoanId(Long enterpriseCarLoanId) {
		return (BigDecimal)myBatisDao.get("MORTGAGE_CAR_SNAPSHOT.getTotalPriceByCardLoanId", enterpriseCarLoanId);
	}

	@Override
	public List<MortgageCarSnapshot> getMortgageCarSnapshotList(MortgageCarSnapshot mortgageCarSnapshot) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mortgageCarSnapshot", mortgageCarSnapshot);
		return this.myBatisDao.getList("getMortgageCarSnapshotPaging", params);
	}

}
