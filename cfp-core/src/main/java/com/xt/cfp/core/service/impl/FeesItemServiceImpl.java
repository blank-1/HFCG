package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.xt.cfp.core.constants.RadiceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LendProduct;
import com.xt.cfp.core.service.FeesItemService;
import com.xt.cfp.core.service.ProductFeesCached;
import com.xt.cfp.core.util.Pagination;

@Service
public class FeesItemServiceImpl implements FeesItemService {
	
	@Autowired
	private MyBatisDao myBatisDao;
	
	@Autowired
    private ProductFeesCached productFeesCached;
	
    @Override
    public List<FeesItem> getFeesItemsByTypeAndKind(String itemType, char itemKind) {
        FeesItem feesItem = new FeesItem();
        feesItem.setItemType(String.valueOf(itemType));
        feesItem.setItemKind(itemKind);
        feesItem.setItemState(FeesItem.ITEMSTATE_ENABLED);
        return myBatisDao.getList("FEESITEM.getFeesItemsByTypeAndKind",feesItem);
    }
    
    @Override
    public FeesItem findById(long feesItemId) {
        return myBatisDao.get("FEESITEM.findById", feesItemId);
    }
    
    @Override
    public List<FeesItem> findAll(FeesItem feesItem) {
    	return myBatisDao.getList("FEESITEM.findAllFeesItemPaging");
    }
    
    @Override
    public Pagination<FeesItem> findAllByPage(int page, int limit, FeesItem feesItem) {
        Pagination<FeesItem> pagination = new Pagination<FeesItem>();
        pagination.setCurrentPage(page);
        pagination.setPageSize(limit);
        List<FeesItem> FeesItem = myBatisDao.getListForPaging("findAllFeesItemPaging", feesItem, page, limit);
        pagination.setRows(FeesItem);
        pagination.setTotal(this.myBatisDao.count("findAllFeesItemPaging", feesItem));
        return pagination;
    }

    @Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
    public void addFeesItem(FeesItem feesItem) {
        myBatisDao.insert("FEESITEM.insertSelective", feesItem);
        productFeesCached.resetFeesItems();
    }

    @Override
    public List<ConstantDefine> findConstantValueOrCode(ConstantDefine constantDefine) {
        return myBatisDao.getList("CONSTANTDEFINE.findConstantValueOrCode", constantDefine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateFeesItem(FeesItem feesItem) {
        //判断修改自定义基数时，如果不是自定义，则吧基数名和逻辑清空
        if (!String.valueOf(feesItem.getRadicesType()).equals("0")) {
            feesItem.setRadiceName("");
            feesItem.setRadiceLogic("");
        }
        myBatisDao.update("FEESITEM.updateByPrimaryKeySelective", feesItem);
        productFeesCached.resetFeesItems();
    }
    
	@Override
	public List<LendProduct> disableLendProductFees(FeesItem feesItem) {
		return myBatisDao.getList("LEND_PRODUCT.disableLendProductFees", feesItem);
	}
	
    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public List<FeesItem> disableLoanProductFees(FeesItem feesItem) {
        return myBatisDao.getList("FEESITEM.disableLoanProductFees", feesItem);
    }
    
    @Override
	public List<FeesItem> findFeesByName(String feesName) {
		return myBatisDao.getList("FEESITEM.findFeesByName", feesName);
	}
    
	@Override
	public List<ConstantDefine> findConstantTypeCode(String boxMess) {
		return myBatisDao.getList("CONSTANTDEFINE.findConstantTypeCode", boxMess);
	}

    @Override
    public BigDecimal calculateFeesBalance(long feesItemId, BigDecimal allCalital, BigDecimal allInterest,
                                           BigDecimal currentCalital, BigDecimal currentInterest, BigDecimal allProfit, BigDecimal currentProfit) {
        BigDecimal result = BigDecimal.ZERO;
        FeesItem feesItem = productFeesCached.getFeesItemById(feesItemId);
        BigDecimal feesRate = new BigDecimal(String.valueOf(feesItem.getFeesRate())).divide(new BigDecimal("100"), 18, BigDecimal.ROUND_CEILING);
        int type = feesItem.getRadicesType();
        if (type == RadiceTypeEnum.PRINCIPAL.value2Int()) {
            result = allCalital.multiply(feesRate);
        } else if (type == RadiceTypeEnum.ALLINTEREST.value2Int()) {
            result = allInterest.multiply(feesRate);
        } else if (type == RadiceTypeEnum.ALLPROFIT.value2Int()) {
            result = allProfit.multiply(feesRate);
        } else if (type == RadiceTypeEnum.ALLPI.value2Int()) {
            result = allCalital.add(allInterest).multiply(feesRate);
        } else if (type == RadiceTypeEnum.SUMPROFIT.value2Int()) {
            result = currentProfit.multiply(feesRate);
        } else if (type == RadiceTypeEnum.CURRENTINTEREST.value2Int()) {
            result = currentInterest.multiply(feesRate);
        } else if (type == RadiceTypeEnum.CURRENTPRINCIPAL.value2Int()) {
            result = currentCalital.multiply(feesRate);
        } else if (type == RadiceTypeEnum.CURRENTPI.value2Int()) {
            result = currentCalital.add(currentInterest).multiply(feesRate);
        }

        return result;
    }

	@Override
	public BigDecimal calculateFeesBalance2(long feesItemId, BigDecimal... bigDecimals) {
		BigDecimal result = BigDecimal.ZERO;
		FeesItem feesItem = productFeesCached.getFeesItemById(feesItemId);
		BigDecimal feesRate = new BigDecimal(String.valueOf(feesItem.getFeesRate())).divide(new BigDecimal("100"), 18, BigDecimal.ROUND_CEILING);
		int type = feesItem.getRadicesType();
		if (type == RadiceTypeEnum.TRANSFERPRINCIPAL.value2Int()) {
			for (BigDecimal bigDecimal : bigDecimals) {
				result = bigDecimal.multiply(feesRate);
			}
		}
		return result;
	}
	
}
