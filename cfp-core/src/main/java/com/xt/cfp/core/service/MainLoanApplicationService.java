package com.xt.cfp.core.service;

import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.MainLoanApplication;
import com.xt.cfp.core.util.Pagination;

public interface MainLoanApplicationService {
	
	/**
	 * 分页查询
	 */
	Pagination<EnterpriseInfo> findAllEnterpriseMainLoanByPage(int pageNo, int pageSize, Map<String, Object> params);
	
    /**
     * 添加【主】借款申请
     */
    MainLoanApplication addMainLoanApplication(MainLoanApplication mainLoanApplication);
    
    /**
     * 根据ID，加载一条
     */
    MainLoanApplication findById(Long mainLoanApplicationId);
    
    /**
     * 修改【主】借款申请
     */
    MainLoanApplication updateMainLoanApplication(MainLoanApplication mainLoanApplication);
    
    /**
     * 根据map修改【主】借款申请
     */
    void update(Map<String,Object> map);
    
    /**
     * 执行自动发标（timer调用）
     */
    void autoPublish() throws Exception;
    
    /**
     * 查询符合自动发标的主借款申请数据（自动发标专用）
     * @return
     */
    List<MainLoanApplication> selectByAuto();
    
}
