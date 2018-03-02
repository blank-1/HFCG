package com.xt.cfp.web.controller.fyInterface;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.external.deposites.model.fyResponse.FyModifyUserInoBackResponse;
import com.external.deposites.model.fyResponse.FyRechargeBackResponse;
import com.external.deposites.model.fyResponse.FyTradeBackResponse;
import com.external.deposites.model.fyResponse.FyUserCancellationBackResponse;
import com.external.deposites.model.fyResponse.FyWithdrawalBackResponse;
import com.external.deposites.utils.SecurityUtils;
import com.xt.cfp.web.controller.BaseController;

/**
 * 同步回调
 * 如果有通知的需要，将以下通知地址配在对应的back_notify_url中
 */
@Controller
@RequestMapping("/fyInterFace/backNotice/sync")
public class FyBackNoticeController extends BaseController {
   
   /**
    * 充值通知
    * @return
    */
   @RequestMapping(value="/backRecharge")
   public Object backRecharge(FyRechargeBackResponse dataSource){
	   String inputStr = dataSource.regSignVal();
	   boolean b =  SecurityUtils.verifySign(inputStr, dataSource.getSignature());//进行验签
	   if(b){
		   //业务处理
		   String plain="";
		   plain +="<plain>";
		   plain +="<resp_code>"+0000+"</resp_code>";//0000成功，失败其他
		   plain +="<mchnt_cd>"+dataSource.getMchnt_cd()+"</mchnt_cd>";
		   plain +="<mchnt_txn_ssn>"+dataSource.getMchnt_txn_ssn()+"<mchnt_txn_ssn>";
		   plain +="</plain>";
		   String singature = SecurityUtils.sign(plain);
		   String data1 = "<?xml version='1.0' encoding='UTF-8'?><ap>";
		   data1 += plain;
		   data1 +="<signature>"+singature+"</signature>";
		   data1 +="</ap>";
		   return data1;//业务处理成功返回
		   
	   }
	return null;
	   
   }
   
   /**
    * 提现通知
    * @param dataSource
    * @return
    */
   @RequestMapping(value="/backWithdrawal")
   public Object backWithdrawal(FyWithdrawalBackResponse dataSource){
	   String inputStr = dataSource.regSignVal();
	   boolean b =  SecurityUtils.verifySign(inputStr, dataSource.getSignature());//进行验签
	   if(b){
		   //业务处理
		   String plain="";
		   plain +="<plain>";
		   plain +="<resp_code>"+0000+"</resp_code>";//0000成功，失败其他
		   plain +="<mchnt_cd>"+dataSource.getMchnt_cd()+"</mchnt_cd>";
		   plain +="<mchnt_txn_ssn>"+dataSource.getMchnt_txn_ssn()+"<mchnt_txn_ssn>";
		   plain +="</plain>";
		   String singature = SecurityUtils.sign(plain);
		   String data1 = "<?xml version='1.0' encoding='UTF-8'?><ap>";
		   data1 += plain;
		   data1 +="<signature>"+singature+"</signature>";
		   data1 +="</ap>";
		   return data1;//业务处理成功返回
		   
	   }
	return null;
	   
   }
   
   /**
    * 交易通知
    * @param dataSource
    * @return
    */
   @RequestMapping(value="/backTrade")
   public Object backTrade(FyTradeBackResponse dataSource){
	   String inputStr = dataSource.regSignVal();
	   boolean b =  SecurityUtils.verifySign(inputStr, dataSource.getSignature());//进行验签
	   if(b){
		   //业务处理
		   String plain="";
		   plain +="<plain>";
		   plain +="<resp_code>"+0000+"</resp_code>";//0000成功，失败其他
		   plain +="<mchnt_cd>"+dataSource.getMchnt_cd()+"</mchnt_cd>";
		   plain +="<mchnt_txn_ssn>"+dataSource.getMchnt_txn_ssn()+"<mchnt_txn_ssn>";
		   plain +="</plain>";
		   String singature = SecurityUtils.sign(plain);
		   String data1 = "<?xml version='1.0' encoding='UTF-8'?><ap>";
		   data1 += plain;
		   data1 +="<signature>"+singature+"</signature>";
		   data1 +="</ap>";
		   return data1;//业务处理成功返回
		   
	   }
	return null;
	   
   }
   
   /**
    * 提现退票通知
    * @param dataSource
    * @return
    */
   @RequestMapping(value="/backWithdrawalRefund")
   public Object backWithdrawalRefund(FyWithdrawalBackResponse dataSource){
	   String inputStr = dataSource.regSignVal();
	   boolean b =  SecurityUtils.verifySign(inputStr, dataSource.getSignature());//进行验签
	   if(b){
		   //业务处理
		  return "SUCCESS";//业务处理成功返回SUCCESS
	   }
	return null;
	   
   }
   
   
   /**
    * 用户修改信息通知
    * @param dataSource
    * @return
    */
   @RequestMapping(value="/backModifyUserIno")
   public Object backModifyUserIno(FyModifyUserInoBackResponse dataSource){
	   String inputStr = dataSource.regSignVal();
	   boolean b =  SecurityUtils.verifySign(inputStr, dataSource.getSignature());//进行验签
	   if(b){
		   //业务处理
		   String plain="";
		   plain +="<plain>";
		   plain +="<resp_code>"+0000+"</resp_code>";//0000成功，失败其他
		   plain +="<mchnt_cd>"+dataSource.getMchnt_cd()+"</mchnt_cd>";
		   plain +="<mchnt_txn_ssn>"+dataSource.getMchnt_txn_ssn()+"<mchnt_txn_ssn>";
		   plain +="</plain>";
		   String singature = SecurityUtils.sign(plain);
		   String data1 = "<?xml version='1.0' encoding='UTF-8'?><ap>";
		   data1 += plain;
		   data1 +="<signature>"+singature+"</signature>";
		   data1 +="</ap>";
		   return data1;//业务处理成功返回
		   
	   }
	return null;
	   
   }
   
   /**
    * 用户注销通知
    * @param dataSource
    * @return
    */
   @RequestMapping(value="/backUserCancellation")
   public Object backUserCancellation(FyUserCancellationBackResponse dataSource){
	   String inputStr = dataSource.regSignVal();
	   boolean b =  SecurityUtils.verifySign(inputStr, dataSource.getSignature());//进行验签
	   if(b){
		   //业务处理
		   String plain="";
		   plain +="<plain>";
		   plain +="<resp_code>"+0000+"</resp_code>";//0000成功，失败其他
		   plain +="<mchnt_cd>"+dataSource.getMchnt_cd()+"</mchnt_cd>";
		   plain +="<mchnt_txn_ssn>"+dataSource.getMchnt_txn_ssn()+"<mchnt_txn_ssn>";
		   plain +="</plain>";
		   String singature = SecurityUtils.sign(plain);
		   String data1 = "<?xml version='1.0' encoding='UTF-8'?><ap>";
		   data1 += plain;
		   data1 +="<signature>"+singature+"</signature>";
		   data1 +="</ap>";
		   return data1;//业务处理成功返回
		   
	   }
	return null;
	   
   }
}
