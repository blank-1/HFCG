package com.xt.cfp.core.service;

import java.util.List;

import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.BalanceDataSource;
import com.external.deposites.model.fyResponse.FyDetailResponse;
import com.external.deposites.model.fyResponse.FyRechargeAndWithdrawResponse;
import com.external.deposites.model.fyResponse.FyResponse;
import com.external.deposites.model.fyResponse.FyTradeResponse;
import com.external.deposites.model.fyResponse.FyUserInfoResponse;
import com.external.deposites.model.fydatasource.FyDetailDataSource;
import com.external.deposites.model.fydatasource.FyRechargeAndWithdrawDataSource;
import com.external.deposites.model.fydatasource.InvestDetail;
import com.external.deposites.model.fydatasource.TransactionQueryDataSource;
import com.external.deposites.model.response.QueryBalanceResponse;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.fyInterFace.TradUserInfo;
/**
 * 富友接口
 * @author Administrator
 *
 */
public interface FyInterFaceService {

	/**
	 * 投资接口
	 * @param loanApplication 标的信息
	 * @param investDetail 投资信息
	 * @return FyResponse 响应返回
	 * @throws Exception
	 * @throws HfApiException 
	 */
	 public FyResponse investment(LoanApplication loanApplication,InvestDetail investDetail) throws HfApiException;
	 
	 /**
	  * 流标接口
	  * @param loanApplication 标的信息
	  * @param investDetailList 投资信息列表
	  * @return ResponseEnum 响应返回
	  * @throws Exception
	  */
	 public FyResponse passLoan(LoanApplication loanApplication,List<InvestDetail> investDetailList) throws HfApiException;
	 
	 /**
	  * 放款接口
	  * @return
	  * @throws Exception
	  */
	 public FyResponse makeLoan(LoanApplication loan,List<TradUserInfo> TradUserInfo,Long formalities,String borrowName) throws HfApiException;
	 
	 /**
	  * 还款接口
	  * @return
	  * @throws Exception
	  */
	 public FyResponse repayment(LoanApplication loan,List<TradUserInfo> tradUserInfo,Long serviceCost,String borrowName) throws HfApiException;
	 
	 /**
	  * 用户信息查询接口
	  * @param userPhone
	  * @return
	  * @throws HfApiException
	  */
	public FyUserInfoResponse queryUserInfo(String userPhone)throws HfApiException;
	
	/**
	 * 交易查询接口
	 * @return
	 * @throws HfApiException
	 */
	public FyTradeResponse queryTrade(TransactionQueryDataSource dataSource) throws HfApiException;
	
	/**
	 * 明细查询接口
	 * @param dataSource
	 * @return
	 * @throws HfApiException 
	 */
	public FyDetailResponse queryDetail(FyDetailDataSource dataSource) throws HfApiException;
	
	/**
	 * 余额查询
	 * @param dataSource
	 * @return
	 * @throws HfApiException
	 */
	public QueryBalanceResponse queryBalance(BalanceDataSource dataSource)throws HfApiException;
	
	/**
	 * 充值提现查询接口
	 * @param dataSource
	 * @return
	 */
	public FyRechargeAndWithdrawResponse queryRechargeAndWithdraw(FyRechargeAndWithdrawDataSource dataSource)throws HfApiException;
}
