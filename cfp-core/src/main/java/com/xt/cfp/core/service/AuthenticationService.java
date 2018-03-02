package com.xt.cfp.core.service;

import com.alibaba.fastjson.JSONObject;

public interface AuthenticationService {

	/**
	 * 身份验证
	 * @param id
	 * @param trueName
	 * @return
	 */
	public boolean verifyService(String id, String trueName);
}
