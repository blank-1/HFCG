package com.xt.cfp.web.controller.fyInterface;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.WithdrawalDataSource;
import com.external.deposites.model.response.WithdrawalResponse;
import com.external.deposites.utils.PropertiesUtils;
import com.external.deposites.utils.SecurityUtils;
import com.xt.cfp.web.controller.BaseController;

/**
 * 同步回调
 */
@Controller
@RequestMapping("/fyInterFace/notice/sync")
public class FyWithdrawalController extends BaseController {
	@Autowired
	private IhfApi hfApi;

	@RequestMapping(value = "/testWithdrawal")
	public Object testWithdrawal(WithdrawalDataSource dataSource, Model model) throws HfApiException {
		dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.withdrawal.page_notify_url"));
		dataSource.setLogin_id("18110270585");
		dataSource.setAmt(Long.valueOf(1000));
		//TODO
//		WithdrawalDataSource withdrawalDataSource = hfApi.withdrawal(dataSource);
//		model.addAttribute("params", withdrawalDataSource);
		return "api/toWithdrawal";
	}

	/**
	 * 商户P2P免登陆提现回调
	 */
	@ResponseBody
	@RequestMapping(value = "/withdrawal")
	public Object withdrawal(WithdrawalResponse withdrawalResponse) {
		Map<String, Object> a = new HashMap<>();
		String inputStr = withdrawalResponse.regSignVal();
		if ("0000".equals(withdrawalResponse.getResp_code())) {// 接口返回状态成功
			boolean b = SecurityUtils.verifySign(inputStr, withdrawalResponse.getSignature());// 进行验签
			if (b) {// 验签成功
				a.put("withdrawalResponse", withdrawalResponse);
			} else {// 验签失败

			}
		}
		System.out.println(withdrawalResponse);
		return a;
	}

}
