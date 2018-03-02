package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.constants.DisplayEnum;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.LendOrderBidDetail;
import com.xt.cfp.core.pojo.ext.LenderRecordVO;
import com.xt.cfp.core.pojo.ext.LenderVO;
import com.xt.cfp.core.service.LendOrderBidDetailService;
import com.xt.cfp.core.util.Pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ren yulin on 15-6-30.
 */
@Service
public class LendOrderBidDetailServiceImpl implements LendOrderBidDetailService {
    @Autowired
    private MyBatisDao myBatisDao;

    private static String sql_prefix = "LENDORDERBIDDETAIL";

    @Override
    public List<LendOrderBidDetail> findByLendOrderId(Long lendOrderId, LendOrderBidStatusEnum... lendOrderBidStatusEnums) {
        List<String> statusList = new ArrayList<String>();
        if (lendOrderBidStatusEnums == null || lendOrderBidStatusEnums.length == 0) {
            statusList = null;
        } else {
            for (LendOrderBidStatusEnum lendOrderBidStatusEnum : lendOrderBidStatusEnums) {
                statusList.add(lendOrderBidStatusEnum.getValue());
            }
        }



        Map<String, Object> param = new HashMap<String, Object>();
        param.put("lendOrderId", lendOrderId);
        param.put("statusList", statusList);
        return myBatisDao.getList(sql_prefix + ".findByLendOrderId", param);
    }

    @Override
    public List<LendOrderBidDetail> findByLendAndLoan(Long lendOrderId, Long loanApplicationId) {
        LendOrderBidDetail orderBidDetail = new LendOrderBidDetail();
        orderBidDetail.setLoanApplicationId(loanApplicationId);
        orderBidDetail.setLendOrderId(lendOrderId);
        return myBatisDao.getList("LENDORDERBIDDETAIL.findBy",orderBidDetail);
    }

    @Override
    public List<LendOrderBidDetail> findByLoanApplicationId(long loanApplicationId) {
        return findByLoanApplicationId(loanApplicationId, null);
    }

    @Override
    public List<LendOrderBidDetail> findByLoanApplicationId(long loanApplicationId, LendOrderBidStatusEnum... lendOrderBidStatusEnums) {
        List<String> statusList = new ArrayList<String>();
        if (lendOrderBidStatusEnums == null || lendOrderBidStatusEnums.length == 0) {
            statusList = null;
        } else {
            for (LendOrderBidStatusEnum lendOrderBidStatusEnum : lendOrderBidStatusEnums) {
                statusList.add(lendOrderBidStatusEnum.getValue());
            }
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("loanApplicationId", loanApplicationId);
        param.put("statusList", statusList);
        return myBatisDao.getList(sql_prefix + ".findByLoanApplicationId", param);
    }

    @Override
    public BigDecimal sumByLoanPdtIdAndLendOrderId(long loanProductId, long lendOrderId,long customAccountId) {
        Map map = new HashMap();
        if (loanProductId != -1l) {
            map.put("loanProductId",loanProductId);
        }
        map.put("lendOrderId",lendOrderId);
        map.put("customAccountId",new Long(customAccountId));
        return myBatisDao.get("LENDORDERBIDDETAIL.sumByLoanPdtIdAndLendOrderId", map);
    }

    @Override
    public void updateStatus(long detailId, char status,long creditorRightsId) {
        LendOrderBidDetail orderBidDetail = new LendOrderBidDetail();
        orderBidDetail.setDetailId(detailId);
        orderBidDetail.setStatus(status);
        if (creditorRightsId != 0l) {
            orderBidDetail.setCreditorRightsId(creditorRightsId);
        }
        myBatisDao.update("LENDORDERBIDDETAIL.update",orderBidDetail);
    }

    @Override
    public void insert(LendOrderBidDetail lendOrderBidDetail) {
        myBatisDao.insert("LENDORDERBIDDETAIL.insert",lendOrderBidDetail);
    }

    @Override
    public BigDecimal sumCreByUserAndLoanApp(long userId, long loanApplicationId) {
        Map map = new HashMap();
        map.put("userId",userId);
        map.put("loanApplicationId",loanApplicationId);
        BigDecimal tmp = myBatisDao.get("LENDORDERBIDDETAIL.getSumCreByUserAndLoanApp",map);
        return tmp == null ? BigDecimal.ZERO : tmp;
    }

    @Override
    public BigDecimal sumCreByLoanApp(Long loanApplicationId, LendOrderBidStatusEnum lendOrderBidStatusEnums) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationId", loanApplicationId);
        params.put("status", lendOrderBidStatusEnums.getValue());
        BigDecimal tmp = myBatisDao.get("LENDORDERBIDDETAIL.sumByLoanApplicationId", params);
        return tmp == null ? BigDecimal.ZERO : tmp;
    }

    @Override
    public Pagination<LenderRecordVO> findLendOrderDetailPaging(int pageNo, int pageSize, Long loanApplicationId, LendOrderBidStatusEnum biding, DisplayEnum displayEnum) {
        Pagination<LenderRecordVO> re = new Pagination<LenderRecordVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationId", loanApplicationId);
        params.put("status", biding.getValue());
        if(null != displayEnum){
        	params.put("displayState", displayEnum.getValue());
        }

        int totalCount = this.myBatisDao.count("findLendOrderDetailPaging",params);
        List<LenderRecordVO> users = this.myBatisDao.getListForPaging("findLendOrderDetailPaging", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(users);

        return re;
    }

    @Override
    public Pagination<LenderVO> findLendOrderDetail(int pageNo, int pageSize, Long loanApplicationId, LendOrderBidStatusEnum biding) {
        Pagination<LenderVO> re = new Pagination<LenderVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationId", loanApplicationId);
        params.put("status", biding.getValue());

        int totalCount = this.myBatisDao.count("getLendOrderDetail",params);
        List<LenderVO> users = this.myBatisDao.getListForPaging("getLendOrderDetail", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(users);

        return re;
    }
    
    /**
     * 获取该借款申请投标人数
     * @param loanApplicationId 借款申请ID
     * @param biding 出借订单状态
     * @return
     */
    @Override
    public Integer findLendOrderDetailCount(Long loanApplicationId, LendOrderBidStatusEnum biding) {
    	Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationId", loanApplicationId);
        params.put("status", biding.getValue());
        return this.myBatisDao.count("findLendOrderDetailPaging",params);
    }

	@Override
	public List<LendOrderBidDetail> getByLendOrderId(Long lendOrderId, LendOrderBidStatusEnum status) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("lendOrderId", lendOrderId);
        params.put("status", status.getValue());
		return myBatisDao.getList("LENDORDERBIDDETAIL.getByLendOrderId", params);
	}

    @Override
    public LendOrderBidDetail getLendOrderBidDetailByCreditorRightsId(Long creditorRightsId) {
        return myBatisDao.get("LENDORDERBIDDETAIL.getLendOrderBidDetailByCreditorRightsId", creditorRightsId);
    }

	@Override
	public Pagination<LenderRecordVO> getCreditorRightsLenderPaging(int pageNo, int pageSize, Long creditorRightsApplyId) {
		Pagination<LenderRecordVO> re = new Pagination<LenderRecordVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("creditorRightsApplyId", creditorRightsApplyId);

        int totalCount = this.myBatisDao.count("getCreditorRightsLenderPaging",params);
        List<LenderRecordVO> users = this.myBatisDao.getListForPaging("getCreditorRightsLenderPaging", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(users);

        return re;
	}
	
	/**
     * 获取该债权转让，投标人数
     * @param creditorRightsApplyId 债权转让申请ID
     * @return
     */
	@Override
	public Integer getCreditorRightsLenderCount(Long creditorRightsApplyId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("creditorRightsApplyId", creditorRightsApplyId);
        return this.myBatisDao.count("getCreditorRightsLenderPaging", params);
	}

	@Override
	public List<LendOrderBidDetail> getLendOrderBidDetailByLendOrderBidIdAndCustomAccId(
			Long lendOrderId, Long accountId) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("lendOrderId", lendOrderId);
        params.put("accountId", accountId);
		return myBatisDao.getList("LENDORDERBIDDETAIL.getLendOrderBidDetailByLendOrderBidIdAndCustomAccId", params);
	}
	
	@Override
    public Pagination<LenderRecordVO> findSXJHLendOrderDetailPaging(int pageNo, int pageSize, Long lendProductPublishId, LendOrderBidStatusEnum biding) {
        Pagination<LenderRecordVO> re = new Pagination<LenderRecordVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lendProductPublishId", lendProductPublishId);
        params.put("status", biding.getValue());
        int totalCount = this.myBatisDao.count("findSXJHLendOrderDetailPaging",params);
        List<LenderRecordVO> users = this.myBatisDao.getListForPaging("findSXJHLendOrderDetailPaging", params, pageNo, pageSize);
        re.setTotal(totalCount);
        re.setRows(users);
        return re;
    }

}
