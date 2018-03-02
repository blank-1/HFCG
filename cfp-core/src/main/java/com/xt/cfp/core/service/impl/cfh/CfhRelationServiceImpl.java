package com.xt.cfp.core.service.impl.cfh;

import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.HttpErrorCode;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.ext.cfh.CfhUserRelation;
import com.xt.cfp.core.security.util.Des3;
import com.xt.cfp.core.service.SmsService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.cfh.CfhRelationService;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.PropertiesUtils;
import com.xt.cfp.core.util.Property;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.core.util.TemplateUtil;

@Property
@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class CfhRelationServiceImpl implements CfhRelationService {
	@Autowired
	private SmsService smsService;
	@Autowired
	private RedisCacheManger redisCacheManger;
	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private UserInfoExtService userInfoExtService;

	private static Logger logger = Logger.getLogger(CfhRelationServiceImpl.class);
	private static Integer smsCodeTime;

	@Override
	public CfhUserRelation getCfpUserByCfhUserId(Long cfhUserId) {
		return myBatisDao.get("CFH_USER_RELATION.getCfpUserByCfhUserId", cfhUserId);
	}

	@Override
	public CfhUserRelation getCfpUserById(Long relationId) {
		return myBatisDao.get("CFH_USER_RELATION.selectByPrimaryKey", relationId);
	}

	@Override
	public CfhUserRelation addCfhUserRelation(CfhUserRelation cfhUserRelation) {
		myBatisDao.insert("CFH_USER_RELATION.insert", cfhUserRelation);
		return cfhUserRelation;
	}

	@Override
	public void sendSms(String mobileNo) {
		VelocityContext vel = new VelocityContext();
		String code = StringUtils.generateVerifyCode();
		vel.put("code", code);
		System.out.println("bindSendSms=================================" + code + "======================================");
		String smsContent = TemplateUtil.getStringFromTemplate(TemplateType.SMS_CFH_BIND_VM, vel);
		boolean boo = redisCacheManger.setRedisCacheInfo(TemplateType.SMS_CFH_BIND_VM.getPrekey() + mobileNo, code, smsCodeTime);
		if (boo)
			smsService.sendMsg(mobileNo, smsContent);
	}

	public static Integer getSmsCodeTime() {
		return smsCodeTime;
	}

	@Property(name = "register_smscode_time")
	public static void setSmsCodeTime(Integer smsCodeTime) {
		CfhRelationServiceImpl.smsCodeTime = smsCodeTime;
	}

	@Override
	public void updateCfhUserRelation(CfhUserRelation cfhUserRelation) {
		myBatisDao.update("CFH_USER_RELATION.updateByPrimaryKeySelective", cfhUserRelation);
	}

	/**
	 * 发送当前用户关联的认证信息
	 */
	@Override
	@Transactional
	public void sendRelationVerified(Long relationId) {
		try {
			// 根据绑定ID，查询绑定数据，赋值回调开始时间
			CfhUserRelation cfhUserRelation = this.getCfpUserById(relationId);
			cfhUserRelation.setReqStartTime(new Date());
			this.updateCfhUserRelation(cfhUserRelation);
			// 根据财富派用户ID，查询用户信息
			UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(cfhUserRelation.getUserId());
			// 根据财富汇用户ID，查询hToken值
			String hToken = redisCacheManger.getRedisCacheInfo("cfh_app_login_" + cfhUserRelation.getCfhUserId().toString());

			logger.info("[cfh]开始发送关联认证信息，relationId：" + cfhUserRelation.getRelationId() + "，hToken：" + hToken);
			// 发送【开始】
			HttpClient client = new HttpClient();
			NameValuePair[] data = { new NameValuePair("hToken", hToken), new NameValuePair("realName", Des3.encode(userInfoExt.getRealName())),
					new NameValuePair("idCard", Des3.encode(userInfoExt.getIdCard())) };

			PostMethod method = new PostMethod(PropertiesUtils.getInstance().get("CFH_CALLBACK_VERIFIED_URL"));
			method.setRequestBody(data);// post请求方式添加参数
			int status = client.executeMethod(method);
			logger.info("[cfh]认证信息回调HttpStatus：" + status);
			if (status == HttpStatus.SC_OK) {
				JSONObject jsonObject = JSONObject.parseObject(method.getResponseBodyAsString());
				String json_result = jsonObject.getString("result");
				logger.info("[cfh]认证信息回调json_result：" + json_result);
				if ("success".equals(json_result)) {

					CfhUserRelation effectiveCfpUser = getEffectiveRelationCfpBind(relationId);
					CfhUserRelation effectiveCfhUser = getEffectiveRelationCfhBind(relationId);

					if (null != effectiveCfhUser) {
						// 财富汇用户已经绑定
						effectiveCfhUser.setStatus(CfhUserRelation.UserStatus.DISABLED.getValue());
						effectiveCfpUser.setUpdateTime(new Date());
						updateCfhUserRelation(effectiveCfhUser);
					}
					if (null != effectiveCfpUser) {
						// 财富派用户已经绑定
						effectiveCfpUser.setStatus(CfhUserRelation.UserStatus.DISABLED.getValue());
						effectiveCfpUser.setUpdateTime(new Date());
						updateCfhUserRelation(effectiveCfpUser);
					}

					// 修改操作：绑定状态、回调状态、回调完成时间
					cfhUserRelation.setStatus(CfhUserRelation.UserStatus.NORMAL.getValue());
					cfhUserRelation.setReqStatu(CfhUserRelation.ReqStatu.SUCCESS.getValue());
					cfhUserRelation.setReqEndTime(new Date());
					this.updateCfhUserRelation(cfhUserRelation);

				} else {
					String json_errorMsg = jsonObject.getString("errorMsg");
					logger.info("[cfh]认证信息回调json_errorMsg：" + json_errorMsg);
				}
			}
			method.releaseConnection();// 释放连接
			// 发送【结束】
		} catch (Exception e) {
			logger.info("[cfh]认证信息回调Exception relationId：" + relationId + e.getMessage());
			throw new SystemException(HttpErrorCode.CAN_HANDLE_POST).set("relationId", relationId);
		}

	}

	@Override
	public CfhUserRelation getEffectiveRelationCfpBind(Long relationId) {
		return myBatisDao.get("CFH_USER_RELATION.getEffectiveRelationCfpBind", relationId);
	}

	@Override
	public CfhUserRelation getEffectiveRelationCfhBind(Long relationId) {
		return myBatisDao.get("CFH_USER_RELATION.getEffectiveRelationCfhBind", relationId);
	}

}
