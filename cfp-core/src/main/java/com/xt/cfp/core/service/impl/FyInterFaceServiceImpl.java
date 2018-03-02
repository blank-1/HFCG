package com.xt.cfp.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.external.deposites.exception.HfApiException;
import com.external.deposites.exception.UnqualifiedException;
import com.external.deposites.model.datasource.BalanceDataSource;
import com.external.deposites.model.datasource.FreezeDataSource;
import com.external.deposites.model.datasource.TransferAccountsDataSource;
import com.external.deposites.model.datasource.TransferFreeze2FreezeDataSource;
import com.external.deposites.model.fyResponse.FyDetailResponse;
import com.external.deposites.model.fyResponse.FyRechargeAndWithdrawResponse;
import com.external.deposites.model.fyResponse.FyResponse;
import com.external.deposites.model.fyResponse.FyTradeResponse;
import com.external.deposites.model.fyResponse.FyUserInfoResponse;
import com.external.deposites.model.fydatasource.FyDetailDataSource;
import com.external.deposites.model.fydatasource.FyRechargeAndWithdrawDataSource;
import com.external.deposites.model.fydatasource.FyUserInfoDataSource;
import com.external.deposites.model.fydatasource.InvestDetail;
import com.external.deposites.model.fydatasource.TransactionQueryDataSource;
import com.external.deposites.model.response.FreezeResponse;
import com.external.deposites.model.response.QueryBalanceResponse;
import com.external.deposites.model.response.TransferAccountsResponse;
import com.external.deposites.model.response.TransferFreeze2FreezeResponse;
import com.external.deposites.model.response.UnfreezeResponse;
import com.external.deposites.service.fyInterFaceService.FyPayService;
import com.external.deposites.service.fyInterFaceService.FyQueryUserInfoService;
import com.external.deposites.service.fyInterFaceService.FyTransferService;
import com.external.deposites.service.internal.AbstractApiService;
import com.external.deposites.utils.PropertiesUtils;
import com.xt.cfp.core.constants.ResponseEnum;
import com.xt.cfp.core.constants.ResponseStatusEnum;
import com.xt.cfp.core.constants.TradeOperateEnum;
import com.xt.cfp.core.constants.TradeTypeEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.Trade;
import com.xt.cfp.core.pojo.fyInterFace.TradUserInfo;
import com.xt.cfp.core.service.FyInterFaceService;
import com.xt.cfp.core.service.TradeService;

/**
 * 富友接口实现类
 * @author zuowansheng
 *
 */
@Service
public class FyInterFaceServiceImpl extends AbstractApiService implements FyInterFaceService {

    private static Logger logger = Logger.getLogger(FyInterFaceServiceImpl.class);
    
    @Autowired
    private FyPayService fyPayService;
    @Autowired
    private FyTransferService fyTransferService;
    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private FyQueryUserInfoService fyQueryUserInfoService;
    /**
     * @param LoanApplication 标的信息
     * @param investDetail i投资信息
     * 投资接口
     * @throws HfApiException 
     */
	@SuppressWarnings("static-access")
	@Override
	public FyResponse investment(LoanApplication loanApplication, InvestDetail investDetail) throws HfApiException {
		List<Trade> reTradeList = new ArrayList<Trade>();
		//先进行未响应查询
		Trade queryTrade = new Trade();
		queryTrade.setLoanid(loanApplication.getLoanApplicationId());
		queryTrade.setInvest_id(investDetail.getInvestId());
		queryTrade.setTrade_status(ResponseStatusEnum.Unresponsive.getValue());
		queryTrade.setMessage_id(TradeTypeEnum.Tender.getValue());
		List<Trade> unresponsiveList = tradeService.selectTrade(queryTrade);
		for(Trade trade : unresponsiveList) {
			TransactionQueryDataSource transactionQueryDataSource = this.initQueryParams(trade);
			try {
				FreezeResponse freezeResponse = fyPayService.queryPayStatus(transactionQueryDataSource);
				if(freezeResponse!=null) {
					if(freezeResponse.isSuccess()) {
						trade.setTrade_status(ResponseStatusEnum.Success.getValue());
					}else {
						trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
					}
					this.tradeService.updateByPrimaryKey(trade);
				}
			} catch (UnqualifiedException e) {
				throw new HfApiException(e.getMessage());
			}
		}
		
		List<FreezeDataSource> investDetailList = initInvestmentParams(investDetail);
		boolean flag = false;
		//根据投资标识查询所有成功交易流水
		queryTrade.setTrade_status(ResponseStatusEnum.Success.getValue());
		List<Trade> list = tradeService.selectTrade(queryTrade);
		//如果流水表记录为空时，取参数直接转换请求，如果已经有流水时，对比数据成功的不在发送
		if(!list.isEmpty()) {
			List<FreezeDataSource> successList = new ArrayList<FreezeDataSource>();
			for(Trade trade : list) {
				for(FreezeDataSource freezeDataSource : investDetailList) {
					if(freezeDataSource.getCust_no().equals(trade.getRequest_trading_account())) {
						successList.add(freezeDataSource);
						flag = true;
					}
				}
			}
			//移除成功的请求
			investDetailList.removeAll(successList);
		}
		ResponseStatusEnum responseStatusEnum = ResponseStatusEnum.Failue;
		ResponseEnum responseEnum = ResponseEnum.Success;
		FreezeResponse freezeResponse = null;
		for(FreezeDataSource freezeDataSource : investDetailList) {
			//流水记录
			Trade trade = new Trade();
			//基础校验
			try {
				basicValidation(freezeDataSource);
			} catch (UnqualifiedException e) {
				return FyResponse.getInstance(responseStatusEnum==ResponseStatusEnum.Failue&&flag?ResponseEnum.Partial_success:ResponseEnum.Failue, e.getMessage(),reTradeList);
			}
			try {
				//发送请求
				freezeResponse = fyPayService.freeze(freezeDataSource);
			} catch (HfApiException e) {
				responseStatusEnum = ResponseStatusEnum.Unresponsive;
				logger.debug("投资接口调用：{}", e.getCause());
				responseEnum = ResponseEnum.Failue;
			}
			//放入流水记录
			trade.setLoanid(loanApplication.getLoanApplicationId());
			trade.setMessage_id(TradeTypeEnum.Tender.getValue());
			trade.setTrade_operate(TradeOperateEnum.Freeze.getValue());
			trade.setRequest_trading_account(freezeDataSource.getCust_no());
			trade.setRequest_message(freezeDataSource.regSignVal());
			trade.setRequest_trading_amount(freezeDataSource.getAmt());
			trade.setRequest_organization(investDetail.getInvestUserRealName());
        	trade.setSerial_number(freezeDataSource.getMchnt_txn_ssn());
        	trade.setResponse_trading_account(loanApplication.getCustomerAccountId().toString());
        	trade.setResponse_organization(investDetail.getBorrowerUserRealName());
        	trade.setInvest_id(investDetail.getInvestId());
        	trade.setTrade_date(new Date());
        	if(freezeDataSource.getCust_no().equals(PropertiesUtils.property("hf-config", "cg.hf.platform_account_id"))){
        		trade.setRequest_organization(PropertiesUtils.property("hf-config", "cg.hf.platform_name"));
        	}
        	
			if(freezeResponse!=null) {
				trade.setResponse_message(freezeResponse.regSignVal());
				trade.setFail_reason(freezeResponse.getResp_desc());
				if(freezeResponse.isSuccess()) {
					responseStatusEnum = ResponseStatusEnum.Success;
					flag = true;
				}else {
					responseStatusEnum = ResponseStatusEnum.Failue;
				}
			}
			trade.setTrade_status(responseStatusEnum.getValue());
			tradeService.addTrade(trade);
        	if(freezeResponse==null || !freezeResponse.isSuccess()) {
        		reTradeList.add(trade);
        		return FyResponse.getInstance(responseStatusEnum==ResponseStatusEnum.Failue&&flag?ResponseEnum.Partial_success:ResponseEnum.Failue,responseStatusEnum.getDesc(),reTradeList);
        	}

		}
		return FyResponse.getInstance(responseEnum,reTradeList);
	}
	
	/**
	 * 流标接口
	 */
	@SuppressWarnings("static-access")
	@Override
	public FyResponse passLoan(LoanApplication loanApplication, List<InvestDetail> investDetailList) throws HfApiException {
		List<Trade> reTradeList = new ArrayList<Trade>();
		boolean flag = false;
		ResponseStatusEnum responseStatusEnum = ResponseStatusEnum.Success;
		ResponseEnum responseEnum = ResponseEnum.Success;
		//根据标查询所有冻结的流水
		Trade queryTrade = new Trade();
		queryTrade.setLoanid(loanApplication.getLoanApplicationId());
		queryTrade.setTrade_status(ResponseStatusEnum.Success.getValue());
		queryTrade.setMessage_id(TradeTypeEnum.Tender.getValue());
		List<Trade> list = myBatisDao.getList("TRADE.selectTrade", queryTrade);
		//根据标查询所有解冻成功的流水
		queryTrade.setMessage_id(TradeTypeEnum.Loss.getValue());
		List<Trade> successTradeList = myBatisDao.getList("TRADE.selectTrade", queryTrade);
		if(!successTradeList.isEmpty()) {
			List<Trade> successList = new ArrayList<Trade>();
			for(Trade trade : list) {
				for(Trade successtrade : successTradeList) {
					if(trade.getSerial_number().equals(successtrade.getSerial_number_tx())) {
						successList.add(trade);
						flag = true;
					}
				}
			}
			//移除成功的冻结流水
			list.removeAll(successList);
		}
		UnfreezeResponse unfreezeResponse = null;
		for(Trade reTrade : list) {
			FreezeDataSource freezeDataSource = this.convertParams(reTrade);
			//基础校验
			try {
				basicValidation(freezeDataSource);
			} catch (UnqualifiedException e) {
				return FyResponse.getInstance(responseStatusEnum==ResponseStatusEnum.Failue&&flag?ResponseEnum.Partial_success:responseEnum, e.getMessage(),reTradeList);
			}
			
			try {
				//发起请求
				unfreezeResponse = this.fyPayService.unfreeze(freezeDataSource);
			} catch (HfApiException e) {
				responseStatusEnum = ResponseStatusEnum.Failue;
				logger.debug("流标接口调用：{}", e.getCause());
				responseEnum = responseEnum.Failue;
			}
			//流水记录
			Trade trade = new Trade();
			trade.setLoanid(loanApplication.getLoanApplicationId());
			trade.setMessage_id(TradeTypeEnum.Loss.getValue());
			trade.setRequest_trading_account(freezeDataSource.getCust_no());
			trade.setRequest_message(freezeDataSource.regSignVal());
			trade.setRequest_trading_amount(freezeDataSource.getAmt());
        	trade.setSerial_number(freezeDataSource.getMchnt_txn_ssn());
        	trade.setSerial_number_tx(reTrade.getSerial_number());
            trade.setInvest_id(reTrade.getInvest_id());
			if(unfreezeResponse!=null) {
				if(unfreezeResponse.isSuccess()) {
					responseStatusEnum = ResponseStatusEnum.Success;
					flag = true;
				}else {
					responseStatusEnum = ResponseStatusEnum.Failue;
				}
				trade.setResponse_message(unfreezeResponse.regSignVal());
        	}else {
				responseStatusEnum = ResponseStatusEnum.Unresponsive;
			}
			
			trade.setTrade_status(responseStatusEnum.getValue());
			myBatisDao.insert("TRADE.insert", trade);
        	if(unfreezeResponse==null || !unfreezeResponse.isSuccess()) {
        		reTradeList.add(trade);
        		return FyResponse.getInstance(responseStatusEnum==ResponseStatusEnum.Failue&&flag?ResponseEnum.Partial_success:responseEnum,responseStatusEnum.getDesc(),reTradeList);
        	}
		}
		return FyResponse.getInstance(responseEnum, reTradeList);
	}
	
	/**
	 * 初始化查询接口
	 * @throws UnqualifiedException 
	 */
	private TransactionQueryDataSource initQueryParams(Trade trade)  {
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
	    String timeString = dataFormat.format(trade.getTrade_date());
		TransactionQueryDataSource transactionQueryDataSource = new TransactionQueryDataSource();
		transactionQueryDataSource.setCust_no(trade.getRequest_trading_account());
		transactionQueryDataSource.setTxn_ssn(trade.getSerial_number());
		transactionQueryDataSource.setBusi_tp("PWDJ");
		transactionQueryDataSource.setStart_day(timeString);
		transactionQueryDataSource.setEnd_day(timeString);
		try {
			basicValidation(transactionQueryDataSource);
		} catch (Exception e) {
		}
		return transactionQueryDataSource;
	}
	
	/**
	 * 冻结参数转换 
	 * @param investDetail 投资详情
	 * @return
	 */
	private List<FreezeDataSource> initInvestmentParams(InvestDetail investDetail) {
		List<FreezeDataSource> result = new ArrayList<FreezeDataSource>();
		//冻结用户账户
		FreezeDataSource freezeDataSource = new FreezeDataSource();
		freezeDataSource.setCust_no(investDetail.getInvestAcount());
		freezeDataSource.setAmt(investDetail.getAmt());
		freezeDataSource.setRem("投资金额冻结");
		result.add(freezeDataSource);
		
		//冻结平台账户
		FreezeDataSource freezeDataSource2 = new FreezeDataSource();
		freezeDataSource2.setCust_no(PropertiesUtils.property("hf-config", "cg.hf.platform_account_id"));
		freezeDataSource2.setAmt(investDetail.getWealthVolume());
		freezeDataSource2.setRem("用户财富卷");
		result.add(freezeDataSource2);
		return result;
	}

	/**
	 * 流标参数转换 
	 * @param trade 冻结详情
	 * @return
	 */
	private FreezeDataSource convertParams(Trade trade) {
		FreezeDataSource freezeDataSource = new FreezeDataSource();
		freezeDataSource.setAmt(trade.getRequest_trading_amount());
		freezeDataSource.setCust_no(trade.getRequest_trading_account());
		freezeDataSource.setRem("流标解冻");
		return freezeDataSource;
	}
	
	/**
	 * 放款接口
	 */
	@SuppressWarnings("unused")
	@Override
	public FyResponse makeLoan(LoanApplication loan,List<TradUserInfo> tradUserInfo,Long formalities,String borrowName) throws HfApiException {
		boolean flag = false;
		ResponseEnum responseEnum = ResponseEnum.Success;
		List<TradUserInfo> failList = new ArrayList<>();
		List<Trade> failTrade = new ArrayList<>();
		TransferFreeze2FreezeDataSource dataSource= new TransferFreeze2FreezeDataSource();
		Trade trades = new Trade();
		trades.setLoanid(loan.getLoanApplicationId());
		trades.setMessage_id(TradeTypeEnum.Full.getValue());
		trades.setTrade_status(ResponseStatusEnum.Success.getValue());
		//根据条件查已执行成功的流水信息
		
		List<Trade> list = tradeService.selectTrade(trades);
		Trade queryTrade = new Trade();
		queryTrade.setLoanid(loan.getLoanApplicationId());
		//查投标成功的流水记录
//		queryTrade.setMessage_id(TradeTypeEnum.Tender.getValue());
//		queryTrade.setTrade_status(ResponseStatusEnum.Success.getValue());
//		List<Trade> successList = tradeService.selectTrade(trades);
		//从配置文件中获取平台账户id
		if(list!=null&&list.size()>0){
			for(Trade trad : list){
				for(int i=0;i<tradUserInfo.size();i++){
					if(tradUserInfo.get(i).getInvestId()!=null&&!"".equals(tradUserInfo.get(i).getInvestId())){
						if(tradUserInfo.get(i).getInvestId().equals(trad.getInvest_id())){
							tradUserInfo.remove(i);//排除掉已成功执行的
						}
					}
				}
			}
		}
		String platfromId= PropertiesUtils.property("hf-config", "cg.hf.platform_account_id");
		String borrowerId =String.valueOf(loan.getRepaymentAccountId());
		String platfromName =PropertiesUtils.property("hf-config", "cg.hf.platform_name");
		if(tradUserInfo!=null&&tradUserInfo.size()>0){
			for(TradUserInfo user : tradUserInfo){
				dataSource.setIn_cust_no(borrowerId);//借款人的富友账户id
				dataSource.setOut_cust_no(user.getFyUserId());//投资人的富友账户id
				dataSource.setAmt(user.getAmt());//投资金额
				
				//冻结到冻结
				try{
					String outputStr =dataSource.regSignVal();//请求参数明文
					TransferFreeze2FreezeResponse response =fyTransferService.transferFreezeToFreeze(dataSource);
					String inputStr = response.regSignVal();//响应参数明文
					//添加流水
					Trade trade = new Trade();
					//放款标识
					trade.setMessage_id(TradeTypeEnum.Full.getValue());
					//冻结到冻结类型
					trade.setTrade_operate(TradeOperateEnum.Initiative.getValue());
					trade.setSerial_number(dataSource.getMchnt_txn_ssn());
					trade.setRequest_message(outputStr);
					trade.setResponse_message(inputStr);
					//投资标识
					trade.setInvest_id(user.getInvestId());
					trade.setLoanid(loan.getLoanApplicationId());
					trade.setRequest_organization(user.getRealName());
					trade.setResponse_organization(borrowName);
					trade.setRequest_trading_account(user.getFyUserId());
					trade.setResponse_trading_account(borrowerId);
					trade.setRequest_trading_amount(dataSource.getAmt());
					trade.setTrade_date(new Date());
					if(response!=null){//验签通过
						if("0000".equals(response.getResp_code())){//成功(验签已在封装方法中进行过了)
							//更新流水
							trade.setTrade_status(ResponseStatusEnum.Success.getValue());
							if(!flag){
								flag = true;
							}
						}else{//失败
							trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
							if(flag) {
								responseEnum = ResponseEnum.Partial_success;
							}else {
								responseEnum = ResponseEnum.Failue;
							}
							trade.setFail_reason(response.getResp_desc());
							failTrade.add(trade);//放款失败记录
						}
					}else{//验签失败
						trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
						if(flag) {
							responseEnum = ResponseEnum.Partial_success;
						}else {
							responseEnum = ResponseEnum.Failue;
						}
						trade.setFail_reason("验签失败");
						failTrade.add(trade);//放款失败记录
					}
					
					tradeService.addTrade(trade);
				}catch (HfApiException e){
					logger.debug("放款接口调用：{}", e.getCause());
					if(flag) {
						responseEnum = ResponseEnum.Partial_success;
					}else {
						responseEnum = ResponseEnum.Failue;
					}
				}
			}
			//失败不解冻
			if(1==responseEnum.getValue()){
				//解冻
				FreezeDataSource dataSource1 = new FreezeDataSource();
				dataSource1.setAmt(loan.getResultBalance().longValue());//解冻总金额（放款金额）
				dataSource1.setCust_no(borrowerId);//借款人富友账户id
				//解冻接口调用
				try{
					UnfreezeResponse unResponse =fyTransferService.unfreeze(dataSource1);
					String outputStr =dataSource1.regSignVal();//请求参数明文
					String inputStr = unResponse.regSignVal();//响应参数明文
					//添加解冻流水
					Trade trade = new Trade();
					//放款标识
					trade.setMessage_id(TradeTypeEnum.Full.getValue());
					//解冻类型
					trade.setTrade_operate(TradeOperateEnum.Thaw.getValue());
					trade.setSerial_number(dataSource1.getMchnt_txn_ssn());
					trade.setRequest_message(outputStr);
					trade.setResponse_message(inputStr);
					trade.setRequest_organization(borrowName);
					trade.setRequest_trading_account(borrowerId);
					trade.setRequest_trading_amount(dataSource1.getAmt());
					trade.setTrade_date(new Date());
					trade.setLoanid(loan.getLoanApplicationId());
					if(unResponse!=null){//验签通过
						if("0000".equals(unResponse.getResp_code())){//成功
							trade.setTrade_status(ResponseStatusEnum.Success.getValue());
						 }else{//失败
							trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
							if(flag) {
								responseEnum = ResponseEnum.Partial_success;
							}else {
								responseEnum = ResponseEnum.Failue;
							}
							trade.setFail_reason(unResponse.getResp_desc());
							failTrade.add(trade);//放款失败记录
						 }
					}else{//验签失败
						trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
						if(flag) {
							responseEnum = ResponseEnum.Partial_success;
						}else {
							responseEnum = ResponseEnum.Failue;
						}
						trade.setFail_reason("验签失败");
						failTrade.add(trade);//放款失败记录
					}
					tradeService.addTrade(trade);
				}catch (HfApiException e) {
					logger.debug("解冻接口调用：{}", e.getCause());
					responseEnum = ResponseEnum.Failue;
				}
				
				//手续费
				try{
					TransferAccountsDataSource zzDataSource = new TransferAccountsDataSource();
					zzDataSource.setAmt(formalities);//传过来的
					zzDataSource.setIn_cust_no(platfromId);
					zzDataSource.setOut_cust_no(borrowerId);
					TransferAccountsResponse zzResponse =fyTransferService.transferAccounts(zzDataSource);
					String outputStr =zzDataSource.regSignVal();//请求参数明文
					String inputStr = zzResponse.regSignVal();//响应参数明文
					//添加交易流水
					Trade trade = new Trade();
					//放款标识
					trade.setMessage_id(TradeTypeEnum.Full.getValue());
					//转账类型
					trade.setTrade_operate(TradeOperateEnum.Thansfer.getValue());
					trade.setSerial_number(zzDataSource.getMchnt_txn_ssn());
					trade.setRequest_message(outputStr);
					trade.setResponse_message(inputStr);
					trade.setRequest_organization(borrowName);
					trade.setResponse_organization(platfromName);
					trade.setRequest_trading_account(borrowerId);
					trade.setResponse_trading_account(platfromId);
					trade.setRequest_trading_amount(zzDataSource.getAmt());
					trade.setTrade_date(new Date());
					trade.setLoanid(loan.getLoanApplicationId());
					if(zzResponse!=null){//验签通过
						if("0000".equals(zzResponse.getResp_code())){//交易成功
							trade.setTrade_status(ResponseStatusEnum.Success.getValue());
						}else{//交易失败
							trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
							if(flag) {
								responseEnum = ResponseEnum.Partial_success;
							}else {
								responseEnum = ResponseEnum.Failue;
							}
							trade.setFail_reason(zzResponse.getResp_desc());
							failTrade.add(trade);//放款失败记录
						}
					}else{//验签失败
						trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
						if(flag) {
							responseEnum = ResponseEnum.Partial_success;
						}else {
							responseEnum = ResponseEnum.Failue;
						}
						trade.setFail_reason("验签失败");
						failTrade.add(trade);//放款失败记录
					}
					tradeService.addTrade(trade);
				}catch (HfApiException e) {
					logger.debug("手续费转账接口调用：{}", e.getCause());
					responseEnum = ResponseEnum.Failue;
				}
			}
			
		}
		return FyResponse.getInstance(responseEnum,failTrade);
	}

	/**
	 * 还款接口
	 */
	@SuppressWarnings("unused")
	@Override
	public FyResponse repayment(LoanApplication loan,List<TradUserInfo> tradUserInfo,Long serviceCost,String borrowName) throws HfApiException {
		//根据条件查已执行成功的流水信息
		boolean flag = false;
		ResponseEnum responseEnum = ResponseEnum.Success;
		Trade trades = new Trade();
		List<TradUserInfo> failList = new ArrayList<>();
		List<Trade> failTrade = new ArrayList<>();
		trades.setLoanid(loan.getLoanApplicationId());
		//还款操作
		trades.setMessage_id(TradeTypeEnum.Repayment.getValue());
		//划拨类型
		trades.setTrade_operate(TradeOperateEnum.Flowage.getValue());
		//成功状态
		trades.setTrade_status(ResponseStatusEnum.Success.getValue());
		//根据条件查已执行成功的流水信息
		List<Trade> list = tradeService.selectTrade(trades);
		//从配置文件中获取平台账户id
		if(list!=null&&list.size()>0){
			for(Trade trad : list){
				for(int i=0;i<tradUserInfo.size();i++){
					if(tradUserInfo.get(i).getInvestId()!=null&&!"".equals(tradUserInfo.get(i).getInvestId())){
						if(tradUserInfo.get(i).getInvestId().equals(trad.getInvest_id())){
							tradUserInfo.remove(i);
						}
					}
				}
			}
		}
		String platfromId= PropertiesUtils.property("hf-config", "cg.hf.platform_account_id");
		String borrowerId =String.valueOf(loan.getRepaymentAccountId());
		String platfromName =PropertiesUtils.property("hf-config", "cg.hf.platform_name");
		if(tradUserInfo!=null&&tradUserInfo.size()>0){
			TransferAccountsDataSource dataSource = new TransferAccountsDataSource();
			for(TradUserInfo user: tradUserInfo){//遍历投资人集合
				//设置出款账户为借款人账户
				dataSource.setOut_cust_no(borrowerId);
				//入款账户为投资人账户
				dataSource.setIn_cust_no(user.getFyUserId());
				dataSource.setAmt(user.getAmt());
				//转账接口
				try{
					TransferAccountsResponse trResponse =fyTransferService.transferAccounts(dataSource);
					String outputStr =dataSource.regSignVal();//请求参数明文
					String inputStr = trResponse.regSignVal();//响应参数明文
					//添加转账流水
					Trade trade = new Trade();
					//还款操作
					trade.setMessage_id(TradeTypeEnum.Repayment.getValue());
					//划拨类型
					trade.setTrade_operate(TradeOperateEnum.Flowage.getValue());
					//投资标识
					trade.setInvest_id(user.getInvestId());
					trade.setSerial_number(dataSource.getMchnt_txn_ssn());
					trade.setRequest_message(outputStr);
					trade.setResponse_message(inputStr);
					trade.setRequest_organization(borrowName);
					trade.setRequest_trading_account(borrowerId);
					trade.setResponse_organization(user.getRealName());
					trade.setResponse_trading_account(user.getFyUserId());
					trade.setRequest_trading_amount(dataSource.getAmt());
					trade.setTrade_date(new Date());
					trade.setLoanid(loan.getLoanApplicationId());
					if(trResponse!=null){
						if("0000".equals(trResponse.getResp_code())){//成功
							if(!flag){
								flag=true;
							}
							trade.setTrade_status(ResponseStatusEnum.Success.getValue());
						}else{//失败
							trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
							if(flag) {
								responseEnum = ResponseEnum.Partial_success;
							}else {
								responseEnum = ResponseEnum.Failue;
							}
							trade.setFail_reason(trResponse.getResp_desc());
							failTrade.add(trade);//添加失败流水记录
						}
					}else{
						trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
						trade.setFail_reason("验签失败");
						failTrade.add(trade);//添加失败流水记录
						if(flag) {
							responseEnum = ResponseEnum.Partial_success;
						}else {
							responseEnum = ResponseEnum.Failue;
						}
					}
					
					myBatisDao.insert("TRADE.insert", trade);
				}catch (HfApiException e) {
					logger.debug("还款转账接口调用：{}", e.getCause());
					if(flag) {
						responseEnum = ResponseEnum.Partial_success;
					}else {
						responseEnum = ResponseEnum.Failue;
					}
				}
				
				//是否有平台加息
				if(user.getPlatformIncreaseAmt()!=null&&!"".equals(user.getPlatformIncreaseAmt())){
					TransferAccountsDataSource dataSource1 = new TransferAccountsDataSource();
					//设置出款账户为平台账户
					dataSource1.setOut_cust_no(platfromId);
					dataSource1.setIn_cust_no(user.getFyUserId());
					//平台加息金额
					dataSource1.setAmt(user.getPlatformIncreaseAmt());
					try{
						TransferAccountsResponse trResponse1 =fyTransferService.transferAccounts(dataSource1);
						String outputStr =dataSource1.regSignVal();//请求参数明文
						String inputStr = trResponse1.regSignVal();//响应参数明文
						//添加转账流水
						Trade trade = new Trade();
						//还款操作
						trade.setMessage_id(TradeTypeEnum.Repayment.getValue());
						//转账类型
						trade.setTrade_operate(TradeOperateEnum.Thansfer.getValue());
						trade.setSerial_number(dataSource1.getMchnt_txn_ssn());
						trade.setRequest_message(outputStr);
						trade.setResponse_message(inputStr);
						trade.setRequest_organization(platfromName);
						trade.setResponse_organization(borrowName);
						trade.setRequest_trading_account(platfromId);
						trade.setResponse_trading_account(user.getFyUserId());
						trade.setRequest_trading_amount(user.getPlatformIncreaseAmt());
						trade.setTrade_date(new Date());
						trade.setLoanid(loan.getLoanApplicationId());
						if(trResponse1!=null){//验签成功
							if("0000".equals(trResponse1.getResp_code())){//成功
								trade.setTrade_status(ResponseStatusEnum.Success.getValue());
							 
							}else{//失败
								trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
								trade.setFail_reason(trResponse1.getResp_desc());
								failTrade.add(trade);//添加失败流水记录
								if(flag) {
									responseEnum = ResponseEnum.Partial_success;
								}else {
									responseEnum = ResponseEnum.Failue;
								}
							 }
						}else{//验签失败
							trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
							trade.setFail_reason("验签失败");
							failTrade.add(trade);//添加失败流水记录
							if(flag) {
								responseEnum = ResponseEnum.Partial_success;
							}else {
								responseEnum = ResponseEnum.Failue;
							}
						}
						
						myBatisDao.insert("TRADE.insert", trade);
					}catch (Exception e) {
						logger.debug("划拨接口调用：{}", e.getCause());
						responseEnum = ResponseEnum.Failue;
					}
				}
				
			}
			
			//平台收取服务费
			if(responseEnum.getValue()==1){//还款操作全部成功
				TransferAccountsDataSource dataSource2 = new TransferAccountsDataSource();
				//设置出款账户为借款人账户
				dataSource2.setOut_cust_no(borrowerId);
				//设置入款账户为平台账户
				dataSource2.setIn_cust_no(platfromId);
				//服务费金额
				dataSource2.setAmt(serviceCost);
				try{
					TransferAccountsResponse trResponse1 =fyTransferService.transferAccounts(dataSource2);
					String outputStr =dataSource2.regSignVal();//请求参数明文
					String inputStr = trResponse1.regSignVal();//响应参数明文
					Trade trade = new Trade();
					//还款操作
					trade.setMessage_id(TradeTypeEnum.Repayment.getValue());
					//转账类型
					trade.setTrade_operate(TradeOperateEnum.Thansfer.getValue());
					trade.setSerial_number(dataSource2.getMchnt_txn_ssn());
					trade.setRequest_message(outputStr);
					trade.setResponse_message(inputStr);
					trade.setRequest_organization(borrowName);
					trade.setResponse_organization(platfromName);
					trade.setRequest_trading_amount(dataSource2.getAmt());
					trade.setRequest_trading_account(borrowerId);
					trade.setResponse_trading_account(platfromId);
					trade.setTrade_date(new Date());
					trade.setLoanid(loan.getLoanApplicationId());
					if(trResponse1!=null){
						if("0000".equals(trResponse1.getResp_code())){//成功
							//添加转账流水
							trade.setTrade_status(ResponseStatusEnum.Success.getValue());
						}else{//失败
							trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
							trade.setFail_reason(trResponse1.getResp_desc());
							failTrade.add(trade);//添加失败流水记录
							if(flag) {
								responseEnum = ResponseEnum.Partial_success;
							}else {
								responseEnum = ResponseEnum.Failue;
							}
						}
					}else{
						trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
						trade.setFail_reason("验签失败");
						failTrade.add(trade);//添加失败流水记录
						if(flag) {
							responseEnum = ResponseEnum.Partial_success;
						}else {
							responseEnum = ResponseEnum.Failue;
						}
					}
					
					myBatisDao.insert("TRADE.insert", trade);
				}catch (HfApiException e) {
					logger.debug("服务费划拨接口调用：{}", e.getCause());
					responseEnum = ResponseEnum.Failue;
				}
			}
		}
		return FyResponse.getInstance(responseEnum,failTrade);
	}
	
	/**
	 * 用户查询接口
	 */
	public FyUserInfoResponse queryUserInfo(String userPhone) throws HfApiException{
		FyUserInfoDataSource dataSource = new FyUserInfoDataSource();
		dataSource.setUser_ids(userPhone);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		String date = fmt.format(new Date());
		dataSource.setMchnt_txn_dt(date);
		FyUserInfoResponse reponse=fyQueryUserInfoService.queryUserInfo(dataSource);
		return reponse;
	}
	
	/**
	 * 交易查询接口
	 * @return
	 * @throws HfApiException
	 */
	public FyTradeResponse queryTrade(TransactionQueryDataSource dataSource) throws HfApiException{
		FyTradeResponse response = new FyTradeResponse();
		try {
			response =fyQueryUserInfoService.queryPayStatus(dataSource);
		} catch (HfApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	/***
	 * 明细查询接口
	 * @throws HfApiException 
	 */
	public FyDetailResponse queryDetail(FyDetailDataSource dataSource) throws HfApiException {
		FyDetailResponse response =fyQueryUserInfoService.queryDetail(dataSource);
		return response;
	}

	/**
	 * 余额查询
	 */
	public QueryBalanceResponse queryBalance(BalanceDataSource dataSource) throws HfApiException {
		QueryBalanceResponse response =fyQueryUserInfoService.queryBalance(dataSource);
		return response;
	}


	/**
	 * 充值提现查询接口
	 */
	public FyRechargeAndWithdrawResponse queryRechargeAndWithdraw(FyRechargeAndWithdrawDataSource dataSource)
			throws HfApiException {
		FyRechargeAndWithdrawResponse response = fyQueryUserInfoService.queryRechargeAndWithdraw(dataSource);
		return response;
	}
	
	
}
