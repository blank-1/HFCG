package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.IDCardVerified;

/**
 * Created by ren yulin on 15-7-18.
 */
public interface IDCardVerifiedService {
    void insert(IDCardVerified idCardVerified);

    void insert(String realName,String idCode,char result);

    IDCardVerified findByNameIdCode(String realName,String idCode);

    void updateIDCardVerified(IDCardVerified newVerified);

	String updateUserVerified(long userId, String idCard, String realName);

	/**
	 * 返回传入身份证号的认证信息
	 * */
	IDCardVerified findByIdCode(String idCode);
}
