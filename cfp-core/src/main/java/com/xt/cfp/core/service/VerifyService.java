package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.Verify;
import com.xt.cfp.core.pojo.ext.VerifyVO;

import java.util.List;

/**
 * Created by luqinglin on 2015/7/12.
 */
public interface VerifyService {
    /**
     * 获取借款申请审核信息
     * @param applicationId
     * @return
     */
    List<VerifyVO> getVerifyByApplicationId(Long applicationId);
    
	//by mainid
    List<VerifyVO> getVerifyByMainLoanApplicationId(Long loanApplicationId);
    
    void addVerify(Verify verify);
}
