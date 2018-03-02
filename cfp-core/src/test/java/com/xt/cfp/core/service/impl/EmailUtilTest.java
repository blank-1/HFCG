/*
package com.xt.cfp.core.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.xt.cfp.core.service.message.EmailMessageBody;
import com.xt.cfp.core.util.EmailUtil;

*/
/**
 * 
 * @author chenbo
 * @创建时间 2015年9月10日 下午2:39:50
 *
 *//*

public class EmailUtilTest {

	@Test
	public void testSendEmail() {
		EmailMessageBody emailDetails = new EmailMessageBody();
		String[] tos = { "123@qq.com" };
		String[] name = { "chen张三" };
		String[] date = { "2015-09-099" };
		String[] code = { "521" };
		// 多封邮件
		// String[] tos = { "hello1@qq.com", "hello2@xtwealth.com" };
		// String[] name = { "张三", "李四" };
		// String[] date = { "2015-09-09", "2015-09-30" };
		// String[] code = { "09", "08" };
		Map<String, Object[]> param = new HashMap<String, Object[]>();
		param.put("name", name);
		param.put("date", date);
		param.put("code", code);
		emailDetails.setModType("0001");
		emailDetails.setAddress(tos);
		emailDetails.setKeyWords(param);
		emailDetails.setSubject("那年秋天的茄子");
		String filePath = "D:\\test\\邮件测试123.txt";
		String fileName = "邮件测试123.txt";
//		try {
//			EmailUtil.sendEmailAttachmentByThird(emailDetails, filePath, fileName);
//			// EmailUtil.sendEmailByThird(emailDetails);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		EmailUtil.sendEmail(emailDetails);
	}

}
*/
