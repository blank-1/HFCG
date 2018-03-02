/*
package com.xt.cfp.core.service.impl;

import java.util.Date;

import com.xt.cfp.core.pojo.ext.UserInfoExtForm;
import com.xt.cfp.core.service.UserInfoService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.UserInfoService;
import java.util.Date;

*/
/**
 * 
 * @author chenbo
 * @创建时间 2015年6月17日 下午5:31:31
 *
 *//*

public class UserInfoExtServiceImplTest {
	@Autowired
	private UserInfoService userInfoService;;
	private Long userId = Long.valueOf(923465);

	@Before
	public void setUp() throws Exception {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		userInfoService = (UserInfoService) applicationContext.getBean("userInfoServiceImpl");
	}

	// @Ignore
	@Test
	public void testUpdateUserInfoExt() {
		UserInfoVO form = new UserInfoVO();
		form.setUserId(userId);
		form.setCity(Long.valueOf("1"));
		form.setProvince(Long.valueOf("1"));
		form.setDetail("中关村");
		form.setBirthday(new Date());
		form.setEducationLevel("1");
		form.setLoginName("loginName");
		userInfoService.updateUserInfo(form);
		UserInfoVO result = userInfoService.getUserExtByUserId(Long.valueOf(923460));
		Assert.assertEquals(result.getDetail(), "中关村");
	}

	@Test
	public void testGetUserExtByUserId() {
		UserInfoVO userExtForm = userInfoService.getUserExtByUserId(Long.valueOf(923460));
		Assert.assertNotNull(userExtForm);
	}

}
*/
