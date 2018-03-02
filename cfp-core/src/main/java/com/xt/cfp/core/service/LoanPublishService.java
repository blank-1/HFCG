package com.xt.cfp.core.service;

import java.util.Map;

import com.xt.cfp.core.pojo.Address;
import com.xt.cfp.core.pojo.LoanPublish;
import com.xt.cfp.core.pojo.MainLoanPublish;
import com.xt.cfp.core.pojo.ext.LoanPublishVO;

import java.util.List;

/**
 * Created by ren yulin on 15-7-11.
 */
public interface LoanPublishService {
	LoanPublish findById(long loanApplicationId);

	/**
	 * 发标信息编辑保存
	 * 
	 * @param loanPublish
	 */
	void addLoanPublish(MainLoanPublish mainLoanPublish, Address address);

    void updateByMap(Map paraMap);
    
    void update(LoanPublish loanPublish);
/**
 * 
 * @param mainLoanPublish
 * @param list
 * @param opassword
 * @param newUserRadio
 * @return
 * @throws Exception
 */
    String publish(MainLoanPublish mainLoanPublish, List<Map<String, Object>> list, String opassword, String newUserRadio) throws Exception;// by mainid

    /**
	 * 查询借款发标详情
	 * @param valueOf
	 * @return
	 */
	LoanPublishVO findLoanPublishVO(Long loanApplicationId);
	
	// 查询借款发标详情 by mainid 
	LoanPublishVO findLoanPublishVOByMainId(Long mainLoanApplicationId);
	
	//get by main id
	MainLoanPublish findMainLoanPublishById(Long mainLoanApplicationId);
	
	//update by main id
	MainLoanPublish updateMainLoanPublish(MainLoanPublish mainLoanPublish);
	
	//add by main id
	MainLoanPublish addMainLoanPublish(MainLoanPublish mainLoanPublish);
	
	LoanPublish insertLoanPublish(LoanPublish loanPublish);
	
/**
 * @param opassword 定向密码
 * @param parseExcelReturnUser  定向用户
 * @return 定向是否成功 及失败消息
 * @author wangyadong
 * @param applicationId 
 */
	Map<String, String> vaDataInExcelAndPassword(String opassword, 
			List<Map<String, Object>> parseExcelReturnUser, Long applicationId,String type,String radioNewUser);
	
 
   /**
    * 删除定向设置在发生异常的时候
    * @param loanApplicationId
    */
	void deleteOrient(Long loanApplicationId);

	String updateOrientByAll(Long loanApplicationId);
}
