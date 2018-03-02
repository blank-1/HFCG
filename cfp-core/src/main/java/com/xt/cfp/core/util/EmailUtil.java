package com.xt.cfp.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.MsgErrorCode;
import com.xt.cfp.core.service.MessagePublicService;
import com.xt.cfp.core.service.message.EmailMessageBody;
import com.xt.cfp.core.service.message.Message;
import com.xt.cfp.core.service.message.MessageChannel;

/**
 * 
 * @author chenbo
 * @创建时间 2015年9月9日 下午6:17:23
 *
 */
public class EmailUtil {

	private static Logger logger = Logger.getLogger(EmailUtil.class);

	/**
	 * 发送url
	 */
	private static final String emailSendUrl = PropertiesUtils.getInstance().get("EMAIL_SEND_URL");
	/**
	 * 用户名
	 */
	private static final String emailUserName = PropertiesUtils.getInstance().get("EMAIL_USERNAME");
	/**
	 * 密码
	 */
	private static final String emailPwd = PropertiesUtils.getInstance().get("EMAIL_PWD");
	/**
	 * 邮件发件人
	 */
	private static final String emailFrom = PropertiesUtils.getInstance().get("EMAIL_FROM");
	/**
	 * 邮件发件人Name
	 */
	private static final String emailFromName = PropertiesUtils.getInstance().get("EMAIL_FROMNAME");
	/**
	 * 是否开启短信 0是开 1是关
	 */
	private static final String emailFlag = PropertiesUtils.getInstance().get("EMAIL_FLAG");

	/**
	 * 发送邮件(不带附件，邮件放入消息队列，无返回值)
	 */
	public static void sendEmail(EmailMessageBody emailMessageBody) {
		MessagePublicService messagePublicService = (MessagePublicService) ApplicationContextUtil.getBean("messagePublicServiceImpl");
		if ("0".equals(emailFlag)) {
			Message message = new Message(emailMessageBody);
			// 发布邮件消息
			messagePublicService.publish(message, MessageChannel.EMAIL_MSG.getValue());
		}
	}

	/**
	 * 发送邮件（带附件）
	 */
	public static String sendEmailAttachmentByThird(EmailMessageBody emailMessageBody, String filePath, String fileName)
			throws ClientProtocolException, IOException {

		final String vars = convert(emailMessageBody);

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(emailSendUrl);

		// 涉及到附件上传, 需要使用 MultipartEntity
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
		entity.addPart("api_user", new StringBody(emailUserName, Charset.forName("UTF-8")));
		entity.addPart("api_key", new StringBody(emailPwd, Charset.forName("UTF-8")));
		entity.addPart("substitution_vars", new StringBody(vars, Charset.forName("UTF-8")));
		entity.addPart("template_invoke_name", new StringBody(emailMessageBody.getModType(), Charset.forName("UTF-8")));
		entity.addPart("from", new StringBody(emailFrom, Charset.forName("UTF-8")));
		entity.addPart("fromname", new StringBody(emailFromName, Charset.forName("UTF-8")));
		entity.addPart("subject", new StringBody(emailMessageBody.getSubject(), Charset.forName("UTF-8")));
		entity.addPart("resp_email_id", new StringBody("true"));

		// 添加附件
		// File file = new File("D:\\邮件测试.txt");
		// FileBody attachment = new FileBody(file, "application/octet-stream",
		// "UTF-8");
		// entity.addPart("files", attachment);

		// 添加附件, 文件流形式
		// String filePath = "D:\\test\\邮件测试.txt";
		// String fileName = "邮件测试.txt";
		File file = new File(filePath);
		InputStreamBody is = new InputStreamBody(new FileInputStream(file), fileName);
		entity.addPart("files", is);

		httpost.setEntity(entity);

		HttpResponse response = httpclient.execute(httpost);
		return responseSendEmail(httpclient, response);
	}

	/**
	 * 发送邮件（不带附件）
	 */
	public static String sendEmailByThird(EmailMessageBody emailMessageBody) throws ClientProtocolException, IOException {

		final String vars = convert(emailMessageBody);

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(emailSendUrl);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("api_user", emailUserName));
		params.add(new BasicNameValuePair("api_key", emailPwd));
		params.add(new BasicNameValuePair("substitution_vars", vars));
		params.add(new BasicNameValuePair("template_invoke_name", emailMessageBody.getModType()));
		params.add(new BasicNameValuePair("from", emailFrom));
		params.add(new BasicNameValuePair("fromname", emailFromName));
		params.add(new BasicNameValuePair("subject", emailMessageBody.getSubject()));
		params.add(new BasicNameValuePair("resp_email_id", "true"));

		httpost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		HttpResponse response = httpclient.execute(httpost);

		return responseSendEmail(httpclient, response);
	}

	/**
	 * 邮件参数转为合作商需要的参数格式
	 * 
	 * @param data
	 * @return
	 */
	private static String convert(EmailMessageBody data) {
		JSONObject ret = new JSONObject();
		JSONObject sub = new JSONObject();
		Map<String, Object[]> keyWords = data.getKeyWords();
		for (String key : keyWords.keySet()) {
			sub.put("%" + key + "%", keyWords.get(key));
		}
		ret.put("to", data.getAddress());
		ret.put("sub", sub);

		return ret.toString();
	}

	/**
	 * 邮件响应
	 * 
	 * @param httpclient
	 * @param response
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	private static String responseSendEmail(HttpClient httpclient, HttpResponse response) throws ParseException, IOException {
		// 处理响应
		String result = "success";
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 正常返回, 解析返回数据(发送成功返回success 失败返回错误信息)
			String entityString = EntityUtils.toString(response.getEntity());
			JSONObject json = (JSONObject) JSONObject.parseObject(entityString);
			if (!"success".equals(json.get("message").toString())) {
				result = json.get("errors").toString();
			}
			logger.info(LogUtils.createSimpleLog("邮件请求响应报文", entityString));
		} else {
			logger.info(LogUtils.createSimpleLog("邮件请求失败响应错误码", String.valueOf(response.getStatusLine().getStatusCode())));
			throw new SystemException(MsgErrorCode.EMAIL_SEND_FAILE).set("发送邮件响应结果:", response.getStatusLine().getStatusCode());
		}
		httpclient.getConnectionManager().shutdown();
		return result;
	}

}
