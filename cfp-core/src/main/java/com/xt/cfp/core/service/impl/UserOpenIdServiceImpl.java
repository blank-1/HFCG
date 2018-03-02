package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserOpenId;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.UserOpenIdService;
import com.xt.cfp.core.util.Sign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class UserOpenIdServiceImpl implements UserOpenIdService {

	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private ConstantDefineService constantDefineService;
	
	@Override
	@Transactional
	public UserOpenId addUserOpenId(UserInfo userInfo, UserOpenId userOpenId) {
		userInfo.setCreateTime(new Date());
        Date loginTime=myBatisDao.get("USER_INFO.getUserLastLoginTime", userInfo.getUserId());
        if(null==loginTime){
            myBatisDao.insert("USER_INFO.recordUser", userInfo);
        }else{
            myBatisDao.update("USER_INFO.updateRecordUser", userInfo);
        }
        myBatisDao.insert("USER_OPENID.insert", userOpenId);
		return userOpenId;
	}

	@Override
	public UserOpenId getUserOpenId(String openId) {
		return myBatisDao.get("USER_OPENID.getUserIdByOpenId", openId);
	}
	
	@Override
	public UserOpenId getUserOpenIdByUserId(Long userId) {
		return myBatisDao.get("USER_OPENID.getOpenIdByUserId", userId);
	}

	@Override
	public UserOpenId addUserOpenId(UserOpenId userOpenId) {
		myBatisDao.insert("USER_OPENID.insert", userOpenId);
		return userOpenId;
	}

	@Override
	@Transactional
	public void updateUserOpenId(UserOpenId oldUserOpenId, UserInfo userInfo, UserOpenId newUserOpenId) {
		if(newUserOpenId != null){
			UserOpenId uoi = new UserOpenId();
			addUserOpenId(userInfo, newUserOpenId);
		}
		myBatisDao.update("USER_OPENID.updateByPrimaryKeySelective", oldUserOpenId);
	}

	@Override
	public UserOpenId getOpenIdByCondition(Long userId, String openId,
			String type) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("openId", openId);
		params.put("type", type);
		return myBatisDao.get("USER_OPENID.getOpenIdByCondition", params);
	}

	@Override
	@Transactional
	public Map<String, String> getConfig(String url) {
		Map<String,String> resultMap = null;
		List<ConstantDefine> tokenSurvivalTimeList = constantDefineService.getConstantDefinesByType("tokenSurvivalTime");
        List<ConstantDefine> accessTokenList = constantDefineService.getConstantDefinesByType("accessToken");
        List<ConstantDefine> ticketSurvivalTimeList = constantDefineService.getConstantDefinesByType("ticketSurvivalTime");
        List<ConstantDefine> jsapiTicketList = constantDefineService.getConstantDefinesByType("jsapiTicket");
        Date date = new Date();
        if("0".equals(tokenSurvivalTimeList.get(0).getConstantValue().toString()) || (date.getTime()/1000) > Long.valueOf(tokenSurvivalTimeList.get(0).getConstantValue().toString())){
        	//to do重新获取
        	String access_token = Sign.getAccessToken();
        	Date resultTime = new Date();
        	tokenSurvivalTimeList.get(0).setConstantValue(String.valueOf(resultTime.getTime()/1000+3600));
        	accessTokenList.get(0).setConstantValue(access_token);
        	constantDefineService.updateConstantDefine(tokenSurvivalTimeList.get(0));
	        constantDefineService.updateConstantDefine(accessTokenList.get(0));
        }
        if("0".equals(ticketSurvivalTimeList.get(0).getConstantValue().toString()) || (date.getTime()/1000) > Long.valueOf(ticketSurvivalTimeList.get(0).getConstantValue().toString())){
        	//to do重新获取
        	String jsapi_ticket = Sign.getJsapiTicket(accessTokenList.get(0).getConstantValue());
        	Date resultTime = new Date();
        	ticketSurvivalTimeList.get(0).setConstantValue(String.valueOf(resultTime.getTime()/1000+3600));
        	jsapiTicketList.get(0).setConstantValue(jsapi_ticket);
        	constantDefineService.updateConstantDefine(ticketSurvivalTimeList.get(0));
	        constantDefineService.updateConstantDefine(jsapiTicketList.get(0));
	        resultMap = Sign.sign(jsapi_ticket, url);
        }else{
        	resultMap = Sign.sign(jsapiTicketList.get(0).getConstantValue(), url);
        }
		return resultMap;
	}

    @Override
    public void deleteOpenId(UserOpenId userOpenId) {
        myBatisDao.delete("USER_OPENID.deleteByPrimaryKey", userOpenId);
    }

}
