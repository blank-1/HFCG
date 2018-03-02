package com.xt.cfp.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.constants.CreditorRightsConstants;
import com.xt.cfp.core.constants.DisplayEnum;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.ext.LenderRecordVO;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.service.ApiService;
import com.xt.cfp.core.util.Pagination;

@Service
public class ApiServiceImpl implements ApiService {
	
	@Autowired
	private MyBatisDao myBatisDaoRead;

	@Override
    public Pagination<LoanApplicationListVO> getLoanApplicationPaging(int pageNo, int pageSize, LoanApplicationListVO loanVo, Map<String, Object> customParams) {
        Pagination<LoanApplicationListVO> re = new Pagination<LoanApplicationListVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanVo", loanVo);
        params.put("preheatTime", new Date());
        if(null != customParams && customParams.containsKey("isNewUserLoan")){
        	params.put("isNewUserLoan", customParams.get("isNewUserLoan"));
        }
        int totalCount = this.myBatisDaoRead.count("getLoanApplicationPaging", params);
        List<LoanApplicationListVO> uah = this.myBatisDaoRead.getListForPaging("getLoanApplicationPaging", params, pageNo, pageSize);
        re.setTotal(totalCount);
        re.setRows(uah);
        return re;
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
        int totalCount = this.myBatisDaoRead.count("findLendOrderDetailPaging",params);
        List<LenderRecordVO> users = this.myBatisDaoRead.getListForPaging("findLendOrderDetailPaging", params, pageNo, pageSize);
        re.setTotal(totalCount);
        re.setRows(users);
        return re;
    }

	@Override
    public Pagination<LenderRecordVO> findLendOrderDetailPaging(int pageNo, int pageSize, int pageNo1, int pageSize1, Long loanApplicationId, CreditorRightsConstants.CreditorRightsStateEnum... effective) {
        Pagination<LenderRecordVO> re = new Pagination<LenderRecordVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);
        List<String> statusList = new ArrayList<String>();
        if (effective == null || effective.length == 0) {
            statusList = null;
        } else {
            for (CreditorRightsConstants.CreditorRightsStateEnum status : effective) {
                statusList.add(status.getValue());
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationId", loanApplicationId);
        params.put("statusList", statusList);
        int totalCount = this.myBatisDaoRead.count("getLendOrderDetailPaging", params);
        List<LenderRecordVO> users = this.myBatisDaoRead.getListForPaging("getLendOrderDetailPaging", params, pageNo, pageSize);
        re.setTotal(totalCount);
        re.setRows(users);
        return re;
    }

    @Override
    public LoanApplication findById(long loanApplicationId) {
        return myBatisDaoRead.get("LOANAPPLICATION.selectByPrimaryKey", loanApplicationId);
    }

    @Override
    public Integer findLendOrderDetailCount(Long loanApplicationId, LendOrderBidStatusEnum biding) {
    	Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationId", loanApplicationId);
        params.put("status", biding.getValue());
        return this.myBatisDaoRead.count("findLendOrderDetailPaging",params);
    }

    @Override
    public Integer findLendOrderDetailCount(Long loanApplicationId, CreditorRightsConstants.CreditorRightsStateEnum... effective) {
        List<String> statusList = new ArrayList<String>();
        if (effective == null || effective.length == 0) {
            statusList = null;
        } else {
            for (CreditorRightsConstants.CreditorRightsStateEnum status : effective) {
                statusList.add(status.getValue());
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationId", loanApplicationId);
        params.put("statusList", statusList);
        int totalCount = this.myBatisDaoRead.count("getLendOrderDetailPaging", params);
        return totalCount;
    }

}
