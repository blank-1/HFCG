package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.service.CustomerCardService;
import com.xt.cfp.core.service.UserAccountOperateService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.WithDrawService;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by luqinglin on 2015/6/16.
 */

public class UserAccountTest extends TestCase {

    private UserAccountOperateService userAccountOperateService;

    private UserAccountService userAccountService;

    private CustomerCardService customerCardService;

    private WithDrawService withDrawService;

    @Before
    public void setUp() throws Exception {
//        ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
//        userAccountOperateService = (UserAccountOperateService)applicationContext.getBean("userAccountOperateServiceImpl");
//        userAccountService = (UserAccountService)applicationContext.getBean("userAccountServiceImpl");
//        customerCardService = (CustomerCardService)applicationContext.getBean("customerCardServiceImpl");
//        withDrawService = (WithDrawService)applicationContext.getBean("withDrawServiceImpl");
    }

    @Test
    public void testexecute(){

//        AccountValueChangedQueue avcq = new AccountValueChangedQueue();
//
//        //向账户7充值
//
//        AccountValueChanged avc = new AccountValueChanged(7L,new BigDecimal(4000f),new BigDecimal(4000d), AccountConstants.AccountOperateEnum.INCOM.getValue(),"1","1","1",1L,"1",1L,new Date(),"充值4000",false);
//        //冻结账户7
//        AccountValueChanged avc4 = new AccountValueChanged(7L,new BigDecimal(3000f),new BigDecimal(3000d), AccountConstants.AccountOperateEnum.FREEZE.getValue(),"1","1","1",1L,"1",1L,new Date(),"冻结3000",true);
//
//        //用账户7支付至账户8、账户9、账户10
//        AccountValueChanged avc0 = new AccountValueChanged(7L,new BigDecimal(1500f),new BigDecimal(1500d), AccountConstants.AccountOperateEnum.PAY_FROZEN.getValue(),"1","1","1",1L,"1",1L,new Date(),"支出1500",true);
//
//        AccountValueChanged avc1 = new AccountValueChanged(8L,new BigDecimal(500f),new BigDecimal(500d), AccountConstants.AccountOperateEnum.INCOM.getValue(),"1","1","1",1L,"1",1L,new Date(),"充值500",false);
//        AccountValueChanged avc2 = new AccountValueChanged(9L,new BigDecimal(500f),new BigDecimal(500d), AccountConstants.AccountOperateEnum.INCOM.getValue(),"1","1","1",1L,"1",1L,new Date(),"充值500",false);
//        AccountValueChanged avc3 = new AccountValueChanged(10L,new BigDecimal(500f),new BigDecimal(500d), AccountConstants.AccountOperateEnum.INCOM.getValue(),"1","1","1",1L,"1",1L,new Date(),"充值500",false);
//
//        //解冻账户7
//        AccountValueChanged avc5 = new AccountValueChanged(7L,new BigDecimal(500f),new BigDecimal(500d), AccountConstants.AccountOperateEnum.UNFREEZE.getValue(),"1","1","1",1L,"1",1L,new Date(),"解冻500",true);
//
//        //账户7支付
//        AccountValueChanged avc6 = new AccountValueChanged(7L,new BigDecimal(500f),new BigDecimal(500d), AccountConstants.AccountOperateEnum.PAY.getValue(),"1","1","1",1L,"1",1L,new Date(),"支付500",true);
//
//        //冻结账户7
//        AccountValueChanged avc7 = new AccountValueChanged(7L,new BigDecimal(6000f),new BigDecimal(6000d), AccountConstants.AccountOperateEnum.FREEZE.getValue(),"1","1","1",1L,"1",1L,new Date(),"冻结6000",true);
//
//        avcq.addAccountValueChanged(avc);
//        avcq.addAccountValueChanged(avc4);
//        avcq.addAccountValueChanged(avc0);
//        avcq.addAccountValueChanged(avc1);
//        avcq.addAccountValueChanged(avc2);
//        avcq.addAccountValueChanged(avc3);
//        avcq.addAccountValueChanged(avc5);
//        avcq.addAccountValueChanged(avc6);
//        avcq.addAccountValueChanged(avc7);
//
//
//        userAccountOperateService.execute(avcq);

    }

//    @Test
    /*public void testUpdateAccountValue(){
        userAccountService.correctionPrecision();
    }*/

   /* @Test
    public void testOutLineWithDraw(){
        String sysMobileNo = "15522670182";
        String cardNo = "4367420064580334149";
        String mobileNo = "13902021616";
        Long bankId = 329L;
        BigDecimal amount = new BigDecimal("101226");
        withDrawService.outLineWithDraw(sysMobileNo,bankId,cardNo,mobileNo,amount);
    }*/

  /*  @Test
    public void testUserAccountPayWithOutCheck(){
        AccountValueChangedQueue avcq = new AccountValueChangedQueue();
        Long accId = 0L;
        BigDecimal amount = new BigDecimal("20000");
        AccountValueChanged avc = new AccountValueChanged(accId,amount,amount, AccountConstants.AccountOperateEnum.PAY.getValue(),"1","1","1",1L,"1",1L,new Date(),"扣除"+amount.toString()+"元",false);
    }*/
}
