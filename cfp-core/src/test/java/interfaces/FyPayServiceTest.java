package interfaces;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.FreezeDataSource;
import com.external.deposites.model.datasource.TransferAccountsDataSource;
import com.external.deposites.model.fyResponse.FyResponse;
import com.external.deposites.model.fydatasource.InvestDetail;
import com.external.deposites.model.response.FreezeResponse;
import com.external.deposites.model.response.TransferAccountsResponse;
import com.external.deposites.utils.PropertiesUtils;
import com.xt.cfp.core.constants.ResponseEnum;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.service.FyInterFaceService;
/**
 * 
 * @author liuwei
 *
 */
public class FyPayServiceTest extends ServiceTestWrapper{
	
    @Autowired
    private IhfApi hfApi;
    @Autowired
    private FyInterFaceService fyInterFaceService;
    
    /**
     * 冻结
     */
    //@Test
    public void testFreeze() {
        FreezeDataSource dataSource = new FreezeDataSource();
        {
            dataSource.setCust_no("15701611000");
            dataSource.setAmt(900L);
            dataSource.setRem("就是要冻你的钱");
        }
        FreezeResponse response = null;
        try {
            response = hfApi.freeze(dataSource);
        } catch (HfApiException e) {
            e.printStackTrace();
        }
        assert response != null : "响应为空了";
        System.out.println(response.getResp_desc());
        System.out.printf("\n\n  response: %s \n\n", response);
    }
    
    /**
     * 转账：可用到可用
     * 限制到单笔2亿，单月5亿了
     */
    //@Test
    public void testTransfer() {
        TransferAccountsDataSource dataSource = new TransferAccountsDataSource();
        {
            dataSource.setOut_cust_no("15701612615");
            dataSource.setIn_cust_no(PropertiesUtils.property("hf-config", "cg.hf.account_id"));
            dataSource.setAmt(1L);
            dataSource.setRem("测试转账，有钱");

        }
        TransferAccountsResponse response = null;
        try {
            response = hfApi.transferAccounts(dataSource);
        } catch (HfApiException e) {
            e.printStackTrace();
        }
        assert response != null : "响应为空了";
        System.out.println(response.getResp_desc());
        System.out.printf("\n\n  response: %s \n\n", response);
    }
    
    
    /**
     * 投资接口测试
     * @throws HfApiException 
     * 
     */
    @Test
    public void investInterface() throws HfApiException {
    	LoanApplication loanApplication = new LoanApplication();
    	loanApplication.setLoanApplicationId(90092L);
    	loanApplication.setCustomerAccountId(18110270512L);
    	InvestDetail investDetail = new InvestDetail();
    	investDetail.setInvestAcount("15111112612");
    	investDetail.setAmt(10000L);
    	investDetail.setInvestId(10001L);
    	investDetail.setWealthVolume(500L);
    	FyResponse fyResponse = fyInterFaceService.investment(loanApplication, investDetail);
        assert fyResponse.getResponseEnum() == ResponseEnum.Success : "失败了";
    }
    
    
    /**
     * 流标接口测试
     * @throws HfApiException 
     * 
     */
    //@Test
    public void passLoan() throws HfApiException {
    	LoanApplication loanApplication = new LoanApplication();
    	loanApplication.setLoanApplicationId(9002L);
    	
    	InvestDetail investDetail = new InvestDetail();
    	investDetail.setInvestAcount("18110270513");
    	loanApplication.setCustomerAccountId(18110270512L);
    	investDetail.setAmt(10000L);
    	investDetail.setInvestId(10001L);
    	investDetail.setWealthVolume(500L);
    	List<InvestDetail> InvestDetails = new ArrayList<InvestDetail>();
    	InvestDetails.add(investDetail);
    	FyResponse fyResponse = fyInterFaceService.passLoan(loanApplication, InvestDetails);
        assert fyResponse.getResponseEnum() == ResponseEnum.Success : "失败了";
    }

}
