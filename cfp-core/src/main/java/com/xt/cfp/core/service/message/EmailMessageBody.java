package com.xt.cfp.core.service.message;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xt.cfp.core.util.EmailUtil;

/**
 * 
 * @author chenbo
 * @创建时间 2015年9月9日 下午6:17:54
 *
 */
public class EmailMessageBody extends MessageBody {

	private static final long serialVersionUID = 4162331099822829116L;
	private static Logger logger = Logger.getLogger(EmailMessageBody.class);

	private String modType;// 模板号
	private String subject;// 邮件主题
	private String[] address;// 收件人
	private Map<String, Object[]> keyWords;// 关键字

	public String getModType() {
		return modType;
	}

	public void setModType(String modType) {
		this.modType = modType;
	}

	public String[] getAddress() {
		return address;
	}

	public void setAddress(String[] address) {
		this.address = address;
	}

	public Map<String, Object[]> getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(Map<String, Object[]> keyWords) {
		this.keyWords = keyWords;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public void handle() {
		// 发送短信
		EmailMessageBody emailDetails = new EmailMessageBody();
		emailDetails.setModType(modType);
		emailDetails.setAddress(address);
		emailDetails.setKeyWords(keyWords);
		emailDetails.setSubject(subject);
		try {
			EmailUtil.sendEmailByThird(emailDetails);
		} catch (IOException e) {
			logger.error("消息队列邮件发送失败", e);
		}

	}

}
