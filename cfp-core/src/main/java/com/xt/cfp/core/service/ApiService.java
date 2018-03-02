package com.xt.cfp.core.service;

import java.util.Map;

import com.xt.cfp.core.constants.CreditorRightsConstants;
import com.xt.cfp.core.constants.DisplayEnum;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.ext.LenderRecordVO;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.util.Pagination;

public interface ApiService {
	/**
     * 借款列表（前端使用）from loanApplicationService
     * @param pageNo
     * @param pageSize
     * @param loanVo
     * @param customParams
     * @return
     */
    Pagination<LoanApplicationListVO> getLoanApplicationPaging(int pageNo, int pageSize,  LoanApplicationListVO loanVo, Map<String, Object> customParams);
    
    /**
     * 查询指定标的，投资列表 from lendOrderBidDetailService
     * @param pageNo
     * @param pageSize
     * @param loanApplicationId 借款申请ID
     * @param biding 
     * @param displayEnum 是否显示（1显示2不显示，对应lendorder标displayState）
     * @return
     */
    Pagination<LenderRecordVO> findLendOrderDetailPaging(int pageNo, int pageSize, Long loanApplicationId, LendOrderBidStatusEnum biding, DisplayEnum displayEnum);
    
    /**
     * 查询 分页 投标记录 from creditorRightsService
     * @param pageNo
     * @param pageSize
     * @param pageNo1
     * @param pageSize1
     * @param loanApplicationId
     * @param effective
     * @return
     */
    Pagination<LenderRecordVO> findLendOrderDetailPaging(int pageNo, int pageSize, int pageNo1, int pageSize1, Long loanApplicationId, CreditorRightsConstants.CreditorRightsStateEnum... effective);
    
    /**
     * 根据ID查询 from loanApplicationService
     * @param loanApplicationId
     * @return
     */
    LoanApplication findById(long loanApplicationId);
    
    /**
     * 获取该借款申请投标人数 from lendOrderBidDetailService
     * @param loanApplicationId 借款申请ID
     * @param biding 出借订单状态
     * @return
     */
    Integer findLendOrderDetailCount(Long loanApplicationId, LendOrderBidStatusEnum biding);
    
    /**
     * 获取投资记录数量 from creditorRightsService
     * @param loanApplicationId
     * @param effective
     * @return
     */
    Integer findLendOrderDetailCount(Long loanApplicationId, CreditorRightsConstants.CreditorRightsStateEnum... effective);
    
}
