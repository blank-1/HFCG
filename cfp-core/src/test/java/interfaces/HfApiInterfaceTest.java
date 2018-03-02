package interfaces;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.*;
import com.external.deposites.model.fyResponse.FyUserInfoResponse;
import com.external.deposites.model.response.*;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import com.xt.cfp.core.service.FyInterFaceService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.ReflectionUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;


public class HfApiInterfaceTest extends ServiceTestWrapper {

    @Autowired
    private IhfApi hfApi;
    @Autowired
    private FyInterFaceService fyInterFaceService;

    /**
     * 个人开户
     */
    @Test
    public void testPersonalOpenAccount() {
        OpenAccount4ApiPersonalDataSource dataSource = new OpenAccount4ApiPersonalDataSource();
        {
            dataSource.setCust_nm("小明24");
            dataSource.setCertif_tp(HfUtils.CertifTpEnum.ID_CARD.code());
            dataSource.setCertif_id("130723198710080015");
            dataSource.setMobile_no("15544440024");
            dataSource.setCity_id("1000");
//            dataSource.setCity_id(HfUtils.address().findProvinceByName("河北").findCityByName("张家口").code());
//            HfUtils.findProvinceByName("河北").code();
//            HfUtils.findProvinceByName("河北").findCityByName("张家口").code();
//            HfUtils.findProvinceByName("河北").findCityByName("张家口").findCountyByName("张北").code();
            dataSource.setParent_bank_id("0305"); // 民生
            dataSource.setCapAcntNo("6226221205600024");
            dataSource.setBank_nm("夺在");
            dataSource.setLpassword("25d55ad283aa400af464c76d713c07ad");
            dataSource.setPassword("25d55ad283aa400af464c76d713c07ad");

        }
        CommonOpenAccount4ApiResponse commonOpenAccount4ApiResponse = null;
        try {
            commonOpenAccount4ApiResponse = hfApi.openAccount(dataSource);
            System.out.println("is ok = " + commonOpenAccount4ApiResponse.verifySign(false));
        } catch (HfApiException e) {
            e.printStackTrace();
        }
        assert commonOpenAccount4ApiResponse != null : "响应为空了";
        System.out.println(HfUtils.codeMapper().code(commonOpenAccount4ApiResponse.getResp_code()));
        System.out.printf("\n\n  openAccountResponse: %s \n\n", commonOpenAccount4ApiResponse);


    }

    /**
     * 企业开户
     */
    @Test
    public void testEnterpriseOpenAccount() {
        OpenAccount4ApiEnterpriseDataSource dataSource = new OpenAccount4ApiEnterpriseDataSource();
        {
            dataSource.setCust_nm("北京大企业");
            dataSource.setArtif_nm("李企业");
            dataSource.setCertif_id("130723198710086002");
            dataSource.setMobile_no("15701616002");
            dataSource.setCity_id("1000");
            dataSource.setParent_bank_id("0305"); // 提现 民生
            dataSource.setCapAcntNo("6226221205646000");
        }
        CommonOpenAccount4ApiResponse commonOpenAccount4ApiResponse = null;
        try {
            commonOpenAccount4ApiResponse = hfApi.openAccount(dataSource);
        } catch (HfApiException e) {
            e.printStackTrace();
        }
        assert commonOpenAccount4ApiResponse != null : "响应为空了";
        System.out.println(commonOpenAccount4ApiResponse.getResp_desc());
        System.out.printf("\n\n  openAccountResponse: %s \n\n", commonOpenAccount4ApiResponse);
    }


    /**
     * 预授权
     */
    @Test
    public void testPreAuth() {
        PreAuthorizationDataSource dataSource = new PreAuthorizationDataSource();
        {
            dataSource.setOut_cust_no("15701612615");
            dataSource.setIn_cust_no("15701611000");
            dataSource.setAmt(14L);
            dataSource.setRem("测试转账授权，有钱");

        }
        try {
            PreAuthorizationResponse response = hfApi.preAuth(dataSource);
            assert response != null : "响应为空了";
            System.out.println(response.getResp_desc());
            System.out.printf("\n\n  response: %s \n\n", response);
        } catch (HfApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 预授权撤销接口
     */
    @Test
    public void testCancelPreAuth() {
        CancelPreAuthDataSource dataSource = new CancelPreAuthDataSource();
        {
            dataSource.setOut_cust_no("15701612615");
            dataSource.setIn_cust_no("15701611000");
            dataSource.setContract_no("000053711509");
            dataSource.setRem("测试转账授权，有钱");

        }
        try {
            CancelPreAuthResponse response = hfApi.cancelPreAuth(dataSource);
            assert response != null : "响应为空了";
            System.out.println(response.getResp_desc());
            System.out.printf("\n\n  response: %s \n\n", response);
        } catch (HfApiException e) {
            e.printStackTrace();
        }
    }


    /**
     * 转账：可用到可用
     * 限制到单笔2亿，单月5亿了
     */
    @Test
    public void testTransfer() {
        TransferAccountsDataSource dataSource = new TransferAccountsDataSource();
        {
//            dataSource.setOut_cust_no("15701612615");
//            dataSource.setIn_cust_no(PropertiesUtils.property("hf-config", "cg.hf.account_id"));
//            dataSource.setContract_no();
            dataSource.setAmt(10000L);
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
     * 转账到冻结中
     */
    @Test
    public void testTransferMerchant2UserFreeze() {
        PreAuthorizationDataSource dataSource = new PreAuthorizationDataSource();
        {
            dataSource.setOut_cust_no(PropertiesUtils.property("hf-config", "cg.hf.account_id"));
            dataSource.setIn_cust_no("15701611000");
//            dataSource.setIn_cust_no("15701612615");
            dataSource.setAmt(10000L);
        }
        TransferAccountsResponse response = null;
        try {
            response = hfApi.transferAccountsToFreeze(dataSource);
        } catch (HfApiException e) {
            e.printStackTrace();
        }
        assert response != null : "响应为空了";
        System.out.println(response.getResp_desc());
        System.out.printf("\n\n  response: %s \n\n", response);
    }

    /**
     * 转账 冻结到冻结
     */
    @Test
    public void testTransferFreeze2Freeze() {
        TransferFreeze2FreezeDataSource dataSource = new TransferFreeze2FreezeDataSource();
        {
//            dataSource.setOut_cust_no(PropertiesUtils.property("hf-config", "cg.hf.account_id"));
            dataSource.setOut_cust_no("15544440018");
            dataSource.setIn_cust_no("15810000003");
//            dataSource.set
            dataSource.setAmt(10000L);
        }
        TransferFreeze2FreezeResponse response = null;
        try {
            response = hfApi.transferFreezeToFreeze(dataSource);
        } catch (HfApiException e) {
            e.printStackTrace();
        }
        assert response != null : "响应为空了";
        System.out.println(response.getResp_desc());
        System.out.printf("\n\n  response: %s \n\n", response);
    }

    /**
     * 冻结
     */
    @Test
    public void testFreeze() {
        FreezeDataSource dataSource = new FreezeDataSource();
        {
            dataSource.setCust_no("15544440018");
            dataSource.setAmt(10000L);
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
     * 解冻
     */
    @Test
    public void testUnfreeze() {
        FreezeDataSource dataSource = new FreezeDataSource();
        {
            dataSource.setCust_no("15701611000");
            dataSource.setAmt(100L);
            dataSource.setRem("解冻你的钱");
        }
        UnfreezeResponse response = null;
        try {
            response = hfApi.unfreeze(dataSource);
        } catch (HfApiException e) {
            e.printStackTrace();
        }
        assert response != null : "响应为空了";
        System.out.println(response.getResp_desc());
        System.out.printf("\n\n  response: %s \n\n", response);
    }

    /**
     * 查询充值、提现
     */
    @Test
    public void testQueryRecharge() throws IllegalAccessException {
        QueryRechargeWithdrawDataSource dataSource = new QueryRechargeWithdrawDataSource();
        {
//            dataSource.setCust_no("15701611000");
            dataSource.setTxn_ssn("5627edf7c2344a3d9858d3bd17217");
            dataSource.setEnd_time(DateUtil.getFormattedDateUtil(new Date(), "yyyy-MM-dd HH:mm:ss"));
            dataSource.setStart_time(DateUtil.getFormattedDateUtil(DateUtil.addDate(new Date(), -20), "yyyy-MM-dd HH:mm:ss"));
        }
        QueryRechargeWithdrawResponse response = null;
        try {
//            response = hfApi.queryRecharge(dataSource);
            dataSource.setTxn_ssn("5627edf7c2344a3d9858d3bd17217");
            response = hfApi.queryWithdraw(dataSource);
        } catch (HfApiException e) {
            e.printStackTrace();
        }
        Map<String, Field> stringFieldMap = ReflectionUtil.allAccessibleFields(QueryRechargeWithdrawResponse.class);
        for (Map.Entry<String, Field> entry : stringFieldMap.entrySet()) {
            entry.getValue().setAccessible(true);
            System.out.printf("\nfield %-30s = %s", entry.getKey(), entry.getValue().get(response));
        }
        System.out.println("\n----------------------------------");
        stringFieldMap = ReflectionUtil.allAccessibleFields(QueryRechargeResponseItem.class);
        for (QueryRechargeResponseItem item : response.getResults()) {
            for (Map.Entry<String, Field> entry : stringFieldMap.entrySet()) {
                entry.getValue().setAccessible(true);
                System.out.printf("\nfield %-30s = %s", entry.getKey(), entry.getValue().get(item));
            }
        }
        assert response != null : "响应为空了";
        System.out.println(response.getResp_desc());
        System.out.printf("\n\n  response: %s \n\n", response);
    }

    /**
     * 查询交易记录
     */
    @Test
    public void testQueryTrade() throws HfApiException, IllegalAccessException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        /*TransactionQueryDataSource dataSource = new TransactionQueryDataSource();
        dataSource.setBusi_tp("PWPC");
        dataSource.setStart_day(sdf.format(new Date()));
        dataSource.setEnd_day(sdf.format(new Date()));
        FyTradeResponse fyTradeResponse = fyInterFaceService.queryTrade(dataSource);
        System.out.println(fyTradeResponse.getResp_desc());
        Map<String, Field> stringFieldMap = ReflectionUtil.allAccessibleFields(FyTradeResponse.class);

        for (Map.Entry<String, Field> entry : stringFieldMap.entrySet()) {
            entry.getValue().setAccessible(true);
            System.out.printf("\nfield %-30s = %s", entry.getKey(), entry.getValue().get(fyTradeResponse));
        }

        for (FyTradeResponseItem fyTradeResponseItem : fyTradeResponse.getResults()) {
            System.out.println(fyTradeResponseItem);
        }*/

        QueryBusinessDataSource dataSource = new QueryBusinessDataSource();
        dataSource.setBusi_tp(QueryBusinessDataSource.BusiTpEnum.PWPC.name());
        dataSource.setStart_day(sdf.format(new Date()));
        dataSource.setEnd_day(sdf.format(new Date()));
        QueryBusinessResponse response = hfApi.queryBusiness(dataSource);
        Set<QueryBusinessResponseItem> results = response.getResults();
        for (QueryBusinessResponseItem result : results) {
            System.out.println(result);
        }
    }

    /**
     * 查询用户信息
     *
     * @throws HfApiException
     * @throws IllegalAccessException
     */
    @Test
    public void testQueryUserInfo() throws HfApiException, IllegalAccessException {
        FyUserInfoResponse fyUserInfoResponse = fyInterFaceService.queryUserInfo("15544440019");

        Map<String, Field> stringFieldMap = ReflectionUtil.allAccessibleFields(FyUserInfoResponse.class);

        for (Map.Entry<String, Field> entry : stringFieldMap.entrySet()) {
            entry.getValue().setAccessible(true);
            System.out.printf("\nfield %-30s = %s", entry.getKey(), entry.getValue().get(fyUserInfoResponse));
        }


    }

}
