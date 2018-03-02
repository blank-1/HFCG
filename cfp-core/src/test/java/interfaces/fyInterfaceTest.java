package interfaces;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.BalanceDataSource;
import com.external.deposites.model.datasource.FreezeDataSource;
import com.external.deposites.model.datasource.TransferFreeze2FreezeDataSource;
import com.external.deposites.model.fyResponse.FyDetailResponse;
import com.external.deposites.model.fyResponse.FyRechargeAndWithdrawResponse;
import com.external.deposites.model.fyResponse.FyResponse;
import com.external.deposites.model.fydatasource.FyDetailDataSource;
import com.external.deposites.model.fydatasource.FyRechargeAndWithdrawDataSource;
import com.external.deposites.model.fydatasource.InvestDetail;
import com.external.deposites.model.fydatasource.PersonalDataSource;
import com.external.deposites.model.fydatasource.TransactionQueryDataSource;
import com.external.deposites.model.response.QueryBalanceResponse;
import com.external.deposites.model.response.TransferFreeze2FreezeResponse;
import com.external.deposites.model.response.UnfreezeResponse;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import com.xt.cfp.core.constants.ResponseEnum;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.fyInterFace.TradUserInfo;
import com.xt.cfp.core.service.FyInterFaceService;
import com.xt.cfp.core.service.task.LoanReportTask;
import com.xt.cfp.core.service.task.TradeReportTask;
import com.xt.cfp.core.service.task.UserReportTask;


public class fyInterfaceTest extends ServiceTestWrapper {

    @Autowired
    private IhfApi hfApi;
    @Autowired
    private FyInterFaceService fyInterFaceService;
    @Autowired
    private UserReportTask userReportTask;
    @Autowired
    private LoanReportTask loanReportTask;
    @Autowired
    private TradeReportTask tradeReportTask;

    /**
     * 个人开户
     */
    //@Test
    public void testPersonalOpenAccount() {
    	PersonalDataSource dataSource = new PersonalDataSource();
        {
            dataSource.setCust_nm("李小小");
            dataSource.setCertif_tp(HfUtils.CertifTpEnum.ID_CARD.code());
            dataSource.setCertif_id("130723198710081216");
            dataSource.setMobile_no("18155667788");
            //dataSource.setCity_id("1000");
            dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.open_account.pc.person.self.page_notify_url"));
//            dataSource.setCity_id(HfUtils.address().findProvinceByName("河北").findCityByName("张家口").code());
//            HfUtils.findProvinceByName("河北").code();
//            HfUtils.findProvinceByName("河北").findCityByName("张家口").code();
//            HfUtils.findProvinceByName("河北").findCityByName("张家口").findCountyByName("张北").code();
            dataSource.setCapAcntNo("6226221215644757");

        }
        PersonalDataSource personalDataSource = null;
        try {
        	personalDataSource = hfApi.openAccountBySelfs(dataSource);
        } catch (HfApiException e) {
            e.printStackTrace();
        }
        assert personalDataSource != null : "响应为空了";
        System.out.println(HfUtils.codeMapper().code(personalDataSource.getResp_code()));
        System.out.printf("\n\n  openAccountResponse: %s \n\n", personalDataSource);


    }
    
    /**
     * 冻结到冻结
     */
   // @Test
    public void testTransferBuAndFreeze2Freeze() {
    	TransferFreeze2FreezeDataSource dataSource = new TransferFreeze2FreezeDataSource();
        {
        	dataSource.setOut_cust_no("15701611000");
        	dataSource.setIn_cust_no("18110270514");
        	dataSource.setAmt(100L);//单位为分，注意转换

        }
        TransferFreeze2FreezeResponse dataSource1 = new TransferFreeze2FreezeResponse();
        try {
        	dataSource1 = hfApi.transferFreezeToFreezes(dataSource);
        	if(dataSource1!=null&&"0000".equals(dataSource1.getResp_code())){//成功(验签已在封装方法中进行过了)
        		
        	}else{//失败
        		
        	}
        } catch (HfApiException e) {
            e.printStackTrace();
        }
        assert dataSource != null : "响应为空了";
        System.out.println(HfUtils.codeMapper().code(dataSource1.getResp_code()));
        System.out.printf("\n\n  openAccountResponse: %s \n\n", dataSource1);


    }
    /**
     * 解冻
     */
    //@Test
    public void testUnFreeze() {
    	  FreezeDataSource dataSource = new FreezeDataSource();
          {
              dataSource.setCust_no("18110270514");
              dataSource.setAmt(100L);//单位为分，注意转换
              dataSource.setRem("解冻你的钱");
          }
          UnfreezeResponse response = null;
          try {
              response = hfApi.unfreezes(dataSource);
              if(response!=null&&"0000".equals(response.getResp_code())){//成功
            	  System.out.println(response.getResp_desc());
              }else{//失败
            	  System.out.println(response.getResp_desc());
              }
          } catch (HfApiException e) {
              e.printStackTrace();
          }
          assert response != null : "响应为空了";
          System.out.println(response.getResp_desc());
          System.out.printf("\n\n  response: %s \n\n", response);
      }

    /**
     * 企业开户
     */
   /* @Test
    public void testEnterpriseOpenAccount() {
        OpenAccount4ApiEnterpriseDataSource dataSource = new OpenAccount4ApiEnterpriseDataSource();
        {
            dataSource.setCust_nm("北京大企业");
            dataSource.setArtif_nm("李企业");
            dataSource.setCertif_id("130723198710082000");
            dataSource.setMobile_no("15701612001");
            dataSource.setCity_id("1000");
            dataSource.setParent_bank_id("0305"); // 提现 民生
            dataSource.setCapAcntNo("6226221205642000");
        }
        CommonOpenAccountResponse commonOpenAccountResponse = null;
        try {
            commonOpenAccountResponse = hfApi.openAccount(dataSource);
        } catch (HfApiException e) {
            e.printStackTrace();
        }
        assert commonOpenAccountResponse != null : "响应为空了";
        System.out.println(HfUtils.codeMapper().code(commonOpenAccountResponse.getResp_code()));
        System.out.printf("\n\n  openAccountResponse: %s \n\n", commonOpenAccountResponse);
    }*/
    /**
     * 放款测试
     * @throws HfApiException
     */
//    @Test
    public void test()throws HfApiException{
    	LoanApplication loan = new LoanApplication();
    	loan.setLoanApplicationId(2382L);
    	loan.setRepaymentAccountId(13737359938L);
    	loan.setResultBalance(new BigDecimal(100));
    	List<TradUserInfo> tradUserInfo = new ArrayList<TradUserInfo>();
    	TradUserInfo tradUser = new TradUserInfo();
    	for(int i=0;i<1;i++){
    		tradUser.setFyUserId("15249910004");
    		tradUser.setRealName("测试004");
    		tradUser.setAmt(1000L);
    		tradUser.setInvestId(10L);
    		tradUserInfo.add(tradUser);
    	}
    	Long shouxufei = 100L;
    	String borrwName= "测试企业001";
    	FyResponse fyResponse =fyInterFaceService.makeLoan(loan, tradUserInfo,shouxufei,borrwName);
    	assert fyResponse.getResponseEnum() == ResponseEnum.Success : "失败了";
    }
    
    /**
     * 还款测试
     * @throws HfApiException
     */
//    @Test
    public void test1()throws HfApiException{
    	LoanApplication loan = new LoanApplication();
    	loan.setLoanApplicationId(2382L);
    	loan.setRepaymentAccountId(13737359938L);
    	loan.setResultBalance(new BigDecimal(50));
    	List<TradUserInfo> tradUserInfo = new ArrayList<TradUserInfo>();
    	TradUserInfo tradUser = new TradUserInfo();
    	for(int i=0;i<1;i++){
    		tradUser.setFyUserId("15249910004");
    		tradUser.setRealName("测试004");
    		tradUser.setAmt(50L);
    		tradUser.setInvestId(10L);
    		tradUser.setPlatformIncreaseAmt(50L);
    		tradUserInfo.add(tradUser);
    	}
    	Long fuwufei = 50L;
    	String borrwName= "测试企业001";
    	FyResponse fyResponse =fyInterFaceService.repayment(loan, tradUserInfo,fuwufei,borrwName);
    	assert fyResponse.getResponseEnum() == ResponseEnum.Success : "失败了";
    }
    //@Test
    public void test2()throws HfApiException{
    	fyInterFaceService.queryUserInfo("18110270513");
    }
    
    //@Test
    public void test3()throws HfApiException{
    	TransactionQueryDataSource dataSource = new TransactionQueryDataSource();
    	dataSource.setBusi_tp("PWDJ");
    	dataSource.setStart_day("20171201");
    	dataSource.setEnd_day("20171216");
    	fyInterFaceService.queryTrade(dataSource);
    }
    
    /**
     * 投资接口测试
     * @throws HfApiException 
     * 
     */
//    @Test
    public void investInterface() throws HfApiException {
    	LoanApplication loanApplication = new LoanApplication();
    	loanApplication.setCustomerAccountId(9808L);
    	loanApplication.setLoanApplicationId(2382L);
    	InvestDetail investDetail = new InvestDetail();
    	investDetail.setInvestAcount("15249910004");
    	investDetail.setInvestUserRealName("测试004");
    	investDetail.setAmt(1000L);
    	investDetail.setInvestId(10001L);
    	investDetail.setWealthVolume(100L);
    	FyResponse fyResponse = fyInterFaceService.investment(loanApplication, investDetail);
        assert fyResponse.getResponseEnum() == ResponseEnum.Success : "失败了";
    }
    
    /**
     * 报备测试
     */
   // @Test
    public void testReport(){
    	userReportTask.excute();
    	loanReportTask.excute();
    	tradeReportTask.excute();
    }
    
   //@Test
    public void testDteail() throws HfApiException{
    	FyDetailDataSource dataSource = new FyDetailDataSource();
    	dataSource.setUser_ids("18110270513");
    	dataSource.setStart_day("20171201");
    	dataSource.setEnd_day("20171231");
    	FyDetailResponse response =fyInterFaceService.queryDetail(dataSource);
    }
   //@Test
   public void test5() throws HfApiException{
	   BalanceDataSource dataSource = new BalanceDataSource();
	   dataSource.setCust_no("18110270513");
	   dataSource.setMchnt_txn_dt("20180103");
	   QueryBalanceResponse response = fyInterFaceService.queryBalance(dataSource);
   }
   @Test
   public void test6() throws HfApiException{
	   FyRechargeAndWithdrawDataSource dataSource= new FyRechargeAndWithdrawDataSource();
	   dataSource.setBusi_tp("PW11");
	   dataSource.setStart_time("2017-12-10 00:00:00");
	   dataSource.setEnd_time("2017-12-30 00:00:00");
	   dataSource.setCust_no("18110270513");
	   FyRechargeAndWithdrawResponse  response = fyInterFaceService.queryRechargeAndWithdraw(dataSource);
   }
}
